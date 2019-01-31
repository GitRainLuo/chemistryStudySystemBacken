package com.chemistrystudysystem.repository;

import com.chemistrystudysystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/1/31 11:14
 * @Version:1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer>,JpaSpecificationExecutor{
    @Query("select u from User u where u.account = :account")
    User findByAccount(@Param("account") String account);
}
