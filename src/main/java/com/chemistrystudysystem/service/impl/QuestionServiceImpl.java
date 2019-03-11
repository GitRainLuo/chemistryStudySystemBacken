package com.chemistrystudysystem.service.impl;/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/3/1 14:30
 * @Version:1.0
 */

import com.chemistrystudysystem.entity.Question;
import com.chemistrystudysystem.repository.QuestionRepository;
import com.chemistrystudysystem.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:
 * @Description:
 * @Auther: hmj
 * @Date: 2019/3/1 14:30
 * @Version:1.0
 */
@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService{
    /*
    * 仓库
    **/
    @Autowired
    QuestionRepository questionRepository;

    /*
    * 获取问题列表
    **/
    public List<Question> getQuestionList(String questionType){
        List<Question> questionList;
        if(!StringUtils.isEmpty(questionType)){
            //传了问题类型
            questionList = questionRepository.findQuestionListByQuestionType(questionType);
        }else {
            //没有 取全部
            questionList = questionRepository.findByIsActiveAndIsDel();
        }
        //LinkedHashMap ==>按照插入顺序保存元素
        //问题类型map集合
        Map<String,String> questionTypeMap = new LinkedHashMap();
        //问题类型list
        List<Question> questionTypeList;
        //按照传递的参数来筛选问题类型
        if(StringUtils.isEmpty(questionType)){
            //没有 查询全部类型
            questionTypeList = questionRepository.findQuestionType();
        }else {
            questionTypeList = questionRepository.findQuestionTypeByType(questionType);
        }
        //去重组装
       for (Question everyType : questionTypeList){
           questionTypeMap.put(everyType.getQuestionType(),everyType.getQuestionTypeName());
       }
       //返回的问题列表(按问题类型分类了)
       List dataList = new ArrayList();
       for(Map.Entry type : questionTypeMap.entrySet()){
           //根据问题类型的问题map
           Map dataMap = new LinkedHashMap();
           //根据问题类型的每个问题列表
           List questionArr = new ArrayList();
           for(Question everyQuestion : questionList){
               //每个问题map
               Map questionMap = new LinkedHashMap();
               //选项列表
               List sectionList = new ArrayList();
               if(type.getKey().equals(everyQuestion.getQuestionType())){
//                   typeData.add(everyQuestion);
                   if(!StringUtils.isEmpty(everyQuestion.getSectionA())){
                       //每个选项map
                       Map<String,String> sectionMap = new LinkedHashMap<>();
                       sectionMap.put("code","A");
                       sectionMap.put("desc",everyQuestion.getSectionA());
                       sectionList.add(sectionMap);
                   }
                   if(!StringUtils.isEmpty(everyQuestion.getSectionB())){
                       Map<String,String> sectionMap = new LinkedHashMap<>();
                       sectionMap.put("code","B");
                       sectionMap.put("desc",everyQuestion.getSectionB());
                       sectionList.add(sectionMap);
                   }
                   if(!StringUtils.isEmpty(everyQuestion.getSectionA())){
                       Map<String,String> sectionMap = new LinkedHashMap<>();
                       sectionMap.put("code","C");
                       sectionMap.put("desc",everyQuestion.getSectionC());
                       sectionList.add(sectionMap);
                   }
                   if(!StringUtils.isEmpty(everyQuestion.getSectionD())){
                       Map<String,String> sectionMap = new LinkedHashMap<>();
                       sectionMap.put("code","D");
                       sectionMap.put("desc",everyQuestion.getSectionD());
                       sectionList.add(sectionMap);
                   }
                   questionMap.put("title",everyQuestion.getQuestionTitle());
                   questionMap.put("rightAnswer",everyQuestion.getRightAnswer());
                   questionMap.put("analysis",everyQuestion.getAnalysis());
                   questionMap.put("section",sectionList);
                   questionArr.add(questionMap);
               }
           }
           dataMap.put("questionType",type.getKey());
           dataMap.put("questionList",questionArr);
           dataList.add(dataMap);
       }
        return dataList;
    }
}
