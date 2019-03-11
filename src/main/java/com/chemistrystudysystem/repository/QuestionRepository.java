package com.chemistrystudysystem.repository;

import com.chemistrystudysystem.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/3/1 14:31
 * @Version:1.0
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question,Integer>{

    /*
     * 根据questionType查询问题列表
     * @param questionType
     * return 问题列表
     **/
    @Query("select entity from Question entity where entity.questionType = :questionType and " +
            "entity.isActive = 1 and entity.isDel = 0")
    List<Question> findQuestionListByQuestionType(@Param("questionType") String questionType);

    /*
     * 查询激活且未删除的questionList
     * @Param
     * @return questionList
     **/
    @Query("select entity from Question entity where entity.isActive = 1 AND entity.isDel = 0")
    List<Question> findByIsActiveAndIsDel();

    /*
     * 查询问题类型列表
     **/
    @Query("select distinct entity from Question entity where entity.isActive = 1 and " +
            "entity.isDel = 0 order by entity.questionType desc ")
    List<Question> findQuestionType();

    /*
     * 根据问题类型查询问题类型列表
     * @Param questionType
     * @return 对应的questionType列表
     **/
    @Query("select distinct entity from Question entity where entity.questionType = :questionType and " +
            "entity.isActive = 1 and entity.isDel = 0 order by entity.questionType desc ")
    List<Question> findQuestionTypeByType(@Param("questionType") String questionType);
}
