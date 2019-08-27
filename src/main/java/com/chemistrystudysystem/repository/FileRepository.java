package com.chemistrystudysystem.repository;

import com.chemistrystudysystem.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Auther: hmj
 * @Description:file仓库
 * @Date: 2019/5/31 11:27
 * @Version:1.0
 */
public interface FileRepository extends JpaRepository<FileEntity,Integer>,JpaSpecificationExecutor<FileEntity>{

}
