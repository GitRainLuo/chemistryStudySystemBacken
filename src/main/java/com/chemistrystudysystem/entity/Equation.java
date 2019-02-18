package com.chemistrystudysystem.entity;/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/2/16 13:40
 * @Version:1.0
 */

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @ClassName:
 * @Description:
 * @Auther: hmj
 * @Date: 2019/2/16 13:40
 * @Version:1.0
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "equation")
public class Equation {

    /*
     * 主键
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /*
     * 方程式
     **/
    @Column(name = "equation")
    private String equation;

    /*
     * 方程式说明
     **/
    @Column(name = "equation_des")
    private String equationDes;

    /*
     * 反应物
     **/
    @Column(name = "reactant")
    private String reactant;

    /*
     * 反应物名称
     **/
    @Column(name = "reactant_name")
    private String reactantName;

    /*
     * 反应条件
     **/
    @Column(name = "reaction_condition")
    private String reactionCondition;

    /*
     * 生成物
     **/
    @Column(name = "resultant")
    private String resultant;

    /*
     * 生成物名称
     **/
    @Column(name = "resultant_name")
    private String resultantName;

    /*
     * 反应类型
     **/
    @Column(name = "reaction_type")
    private String reactionType;

    /*
     * 反应类型名
     **/
    @Column(name = "reaction_type_name")
    private String reactionTypeName;
}
