package com.chemistrystudysystem.service;


import com.chemistrystudysystem.entity.Question;

import java.util.List;

/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/3/1 14:30
 * @Version:1.0
 */
public interface QuestionService {
    /*
    * 获取问题列表
    **/
    List<Question> getQuestionList(String questionType);
}
