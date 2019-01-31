package com.chemistrystudysystem.entity;/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/1/31 11:13
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
 * @Date: 2019/1/31 11:13
 * @Version:1.0
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Data
@Table(name = "user")
public class User {
    /*
    * 主键id
    **/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private String id;

    /*
     * 账号
     **/
    @Column(name = "account")
    private String account;

    /*
     * 密码
     **/
    @Column(name = "password")
    private String password;

    /*
     * 手机号码
     **/
    @Column(name = "user_phone")
    private String userPhone;

    /*
     * 邮箱
     **/
    @Column(name = "email")
    private String email;

    /*
     * 用户类型
     **/
    @Column(name = "user_type")
    private String userType;

    /*
     * 创建时间
     **/
    @Column(name = "create_time",nullable = false,updatable = false)
    private Date createTime;

    /*
     * 更新时间
     **/
    @Column(name = "updateTime")
    private Date updateTime;

    /*
    * 是否启用
    **/
    @Column(name = "is_active")
    private Integer isActive;

    /*
    * 是否删除
    **/
    @Column(name = "isDel")
    private Integer isDel;

    /*
     * 插入时执行
     **/
    @PrePersist
    public void PrePersist(){
        this.createTime = new Date();
    }

    /*
     * 修改时执行
     **/
    @PreUpdate
    public void PreUpdate(){
        this.updateTime = new Date();
    }
}
