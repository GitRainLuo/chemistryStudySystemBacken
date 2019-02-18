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
                List<Predicate> predicates = new ArrayList<>();
                //反应物
                if(StringUtils.isNotBlank(condition.get("reactantName").toString())){
                    String reactantName = condition.get("reactantName").toString();
                    predicates.add(criteriaBuilder.equal(root.get("reactantName"),reactantName));
                }
                //生成物
                if(StringUtils.isNotBlank(condition.get("resultantName").toString())){
                    String resultantName = condition.get("resultantName").toString();
                    predicates.add(criteriaBuilder.equal(root.get("resultantName"),resultantName));
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
                //反应类型
                if(StringUtils.isNotBlank(condition.get("reactionType").toString())){
                    String reactionType = condition.get("reactionType").toString();
                    predicates.add(criteriaBuilder.equal(root.get("reactionType"),reactionType));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<Equation> page = equationRepository.findAll(specification,pageable);
        return page;
    }
}
