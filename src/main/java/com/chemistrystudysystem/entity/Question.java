package com.chemistrystudysystem.entity;/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/3/1 14:29
 * @Version:1.0
 */

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName:
 * @Description:
 * @Auther: hmj
 * @Date: 2019/3/1 14:29
 * @Version:1.0
 */
@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "question")
public class Question {
    /*
     * 主键
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /*
     * 问题名(问题描述)
     **/
    @Column(name = "question_title")
    private String questionTitle;

    /*
     * 问题类型Code
     **/
    @Column(name = "question_type")
    private String questionType;

    /*
     * 问题类型名
     **/
    @Column(name = "question_type_name")
    private String questionTypeName;

    /*
     * 选项A
     **/
    @Column(name = "section_a")
    private String sectionA;

    /*
    * 选项B
    **/
    @Column(name = "section_b")
    private String sectionB;

    /*
    * 选项C
    **/
    @Column(name = "section_c")
    private String sectionC;

    /*
    * 选项D
    **/
    @Column(name = "section_d")
    private String sectionD;

    /*
    * 正确选项
    **/
    @Column(name = "right_answer")
    private String rightAnswer;

    /*
    * 解析
    **/
    @Column(name = "analysis")
    private String analysis;

    /*
    * 创建时间
    **/
    @Column(name = "create_time")
    private Date createTime;

    /*
    * 是否激活
    **/
    @Column(name = "is_active")
    private Integer isActive;

    /*
    * 是否删除
    **/
    @Column(name = "is_del")
    private Integer isDel;

    /*
    * 插入时执行
    **/
    @PrePersist
    public void PrePersist(){
        this.createTime = new Date();
    }

    /*
    * 更新时执行
    **/
//    @PreUpdate
//    public void PreUpdate(){}
}
