package com.chemistrystudysystem.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther: hmj
 * @Description:文件上传地址保存
 * @Date: 2019/5/31 11:08
 * @Version:1.0
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "file")
public class FileEntity {
    /**
     *主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /**
     * 文件名
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * 文件类型
     */
    @Column(name = "file_type")
    private String fileType;

    /**
     * 文件保存地址(url)
     */
    @Column(name = "save_url")
    private String saveUrl;

    /**
     * 上传时间
     */
    @Column(name = "upload_time")
    private Date uploadTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 是否启用
     */
    @Column(name = "is_active")
    private Integer isActive;

    /**
     * 是否删除
     */
    @Column(name = "is_del")
    private Integer isDel;

    /**
     * 插入时执行
     */
    @PrePersist
    public void PrePersist(){
        this.uploadTime = new Date();
    }

    /**
     * 更新时执行
     */
    @PreUpdate
    public void PreUpdate(){
        this.updateTime = new Date();
    }
}
