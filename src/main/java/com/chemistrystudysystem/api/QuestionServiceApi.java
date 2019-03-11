package com.chemistrystudysystem.api;/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/3/1 14:30
 * @Version:1.0
 */

import com.chemistrystudysystem.entity.Question;
import com.chemistrystudysystem.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @ClassName:
 * @Description:
 * @Auther: hmj
 * @Date: 2019/3/1 14:30
 * @Version:1.0
 */
@RestController
@RequestMapping("question")
public class QuestionServiceApi {
    /*
    * questionService
    **/
    @Autowired
    QuestionService questionService;

    /*
    * 获取问题列表
    **/
    @GetMapping("/questionList")
    public List<Question> getQuestionList(@RequestParam(required = false) String questionType){
        return questionService.getQuestionList(questionType);
    }

}
