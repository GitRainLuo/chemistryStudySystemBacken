package com.chemistrystudysystem.service.impl;/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/2/16 13:41
 * @Version:1.0
 */

import com.chemistrystudysystem.entity.Equation;
import com.chemistrystudysystem.repository.EquationRepository;
import com.chemistrystudysystem.service.EquationService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:
 * @Description:
 * @Auther: hmj
 * @Date: 2019/2/16 13:41
 * @Version:1.0
 */
@Service
@Slf4j
public class EquationServiceImpl implements EquationService{
    /*
     * equationRepository仓库
     **/
    @Autowired
    private EquationRepository equationRepository;

    /*
     * 分页查询方程式列表
     **/
    @Override
    public Page<Equation> findEquationDataList(Map<String,Object> condition){
        //分页参数
        Integer pageNum = Integer.parseInt(condition.get("page").toString());
        Integer size = Integer.parseInt(condition.get("size").toString());
        //分页
        //PageRequest pageable = new PageRequest(pageNum - 1,size, Sort.Direction.DESC,"id");
        PageRequest pageable = new PageRequest(pageNum - 1,size);
        //拼接查询参数
        Specification<Equation> specification = new Specification<Equation>() {
            @Override
            public Predicate toPredicate(Root<Equation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //方法1
                List<Predicate> predicates = new ArrayList<>();
                //反应物
                if(StringUtils.isNotBlank(condition.get("reactantName").toString())){
                    String reactantName = condition.get("reactantName").toString();
                    predicates.add(criteriaBuilder.like(root.get("reactantName"),"%"+reactantName+"%"));
                }
                //生成物
                if(StringUtils.isNotBlank(condition.get("resultantName").toString())){
                    String resultantName = condition.get("resultantName").toString();
                    predicates.add(criteriaBuilder.like(root.get("resultantName"),"%"+resultantName+"%"));
                }
                //反应条件
                if(StringUtils.isNotBlank(condition.get("reactionCondition").toString())){
                    String reactionCondition = condition.get("reactionCondition").toString();
                    predicates.add(criteriaBuilder.equal(root.get("reactionCondition"),reactionCondition));
                }
                //方程式说明(模糊查询)
                if(StringUtils.isNotBlank(condition.get("equationDes").toString())){
                    String equationDes = condition.get("equationDes").toString();
                    predicates.add(criteriaBuilder.like(root.get("equationDes"),"%"+equationDes+"%"));
                }
//                //反应类型(单个)
//                if(StringUtils.isNotBlank(condition.get("reactionType").toString())){
//                    String reactionType = condition.get("reactionType").toString();
//                    predicates.add(criteriaBuilder.equal(root.get("reactionType"),reactionType));
//                }
                //反应类型(单个或者多个)
                if(condition.get("reactionType") instanceof ArrayList){
                    if(JSONArray.fromObject(condition.get("reactionType")).size()>0){
                        JSONArray jsonArray = JSONArray.fromObject(condition.get("reactionType"));
                        if(jsonArray.size() == 1){
                            predicates.add(criteriaBuilder.equal(root.get("reactionType"),jsonArray.get(0)));
                        }else {
                            predicates.add(root.get("reactionType").in(jsonArray));
                        }
                    }
                }
                //方法2
                //List<Predicate> predicates = makeQueryCondition(condition,root,criteriaBuilder);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<Equation> page = equationRepository.findAll(specification,pageable);
        return page;
    }

    /*
     * 拼接查询参数 (另一种方法)
     **/
    private List<Predicate> makeQueryCondition(Map<String,Object> condition,Root<Equation> root,CriteriaBuilder cb){
        List<Predicate> predicateList = new ArrayList<>();
        if(condition!=null){
            //反应物
            String reactantName;
            if(condition.get("reactantName")!=null){
                reactantName = condition.get("reactantName").toString();
                if(!StringUtils.isEmpty(reactantName)){
                    predicateList.add(cb.like(root.get("reactantName"),"%"+reactantName+"%"));
                }
            }
            //生成物
            String resultantName;
            if(condition.get("resultantName") != null){
                resultantName = condition.get("resultantName").toString();
                if(!StringUtils.isEmpty(resultantName)){
                    predicateList.add(cb.equal(root.get("resultantName"),"%"+resultantName+"%"));
                }
            }
            //反应条件
            String reactionCondition;
            if(condition.get("reactionCondition") != null){
                reactionCondition = condition.get("reactionCondition").toString();
                if(!StringUtils.isEmpty(reactionCondition)){
                    predicateList.add(cb.equal(root.get("reactionCondition"),reactionCondition));
                }
            }
            //方程式说明(模糊查询)
            String equationDes;
            if(condition.get("equationDes") != null){
                equationDes = condition.get("equationDes").toString();
                if(!StringUtils.isEmpty(equationDes)){
                    predicateList.add(cb.like(root.get("equationDes"),"%"+equationDes+"%"));
                }
            }

            //反应类型(针对于多选的)
            List<String> reactionTypeList = convertJsonToList(condition.get("reactionType"));
            if(reactionTypeList != null && reactionTypeList.size()>0){
                if(reactionTypeList.size() == 1){
                    //只有一个 取第一个
                    predicateList.add(cb.equal(root.get("reactionType"),reactionTypeList.get(0)));
                }else {
                    //多个就用in 在List里面的
                    predicateList.add(root.get("reactionType").in(reactionTypeList));
                }
            }
        }
        return predicateList;
    }

    /*
     * json转list
     **/
    public List<String> convertJsonToList(Object ob){
        if(ob instanceof String[]){
            String[] tmp = (String[])ob;
            List<String> list = Arrays.asList(tmp);
            //log.info("String[] "+((String[]) ob).toString());
            return list;
        }
        else if(ob instanceof ArrayList){
            JSONArray tmp = JSONArray.fromObject(ob);
            List<String> itemList = JSONArray.toList(tmp,new String(),new JsonConfig());
            //log.info("JSONArray "+areaCodes.toString());
            return itemList;
        }
        else if(ob instanceof JSONArray){
//            JSONArray tmp1 = (JSONArray)ob;
            JSONArray tmp1 = JSONArray.fromObject(ob);
            List<String> itmpList = JSONArray.toList(tmp1,new String(),new JsonConfig());
            return itmpList;
        }
        else if(ob instanceof com.alibaba.fastjson.JSONArray){
            com.alibaba.fastjson.JSONArray tmp = ( com.alibaba.fastjson.JSONArray)ob;
            List<String> itemList = com.alibaba.fastjson.JSONArray.parseArray(tmp.toString(),String.class);
            //log.info("com.alibaba.fastjson.JSONArray ");
            //log.info("========================result:"+areaCodes.toString());
            return itemList;
        }
        else if (ob instanceof String){
            String strob = ob.toString();
            //log.info(strob);
            strob = strob.replaceAll("\\[", "");
            strob = strob.replaceAll("]", "");
            strob = strob.replaceAll("\"","");
            if(!StringUtils.isEmpty(strob)) {
                String[] strobA = strob.split(",");
                //log.info("---size:" + strobA.length);
                List<String> list = new ArrayList<>();
                for (int i = 0; i < strobA.length; i++) {
                    //log.info("--value:" + strobA[i]+" length:"+strobA[i].length());
                    list.add(strobA[i]);
                }
                //log.info("---list:" + list);
                return list;
            }
        }
        return null;
    }
}
