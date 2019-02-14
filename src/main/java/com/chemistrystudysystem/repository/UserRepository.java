package com.chemistrystudysystem.repository;

import com.chemistrystudysystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
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
    /*
    * 根据账号查找
    * */
    @Query("select u from User u where u.account = :account")
    User findByAccount(@Param("account") String account);

    /*
     * 更新信息
     **/
    @Modifying
    @Query("update User u set u.password = :password ," +
            "u.userPhone = :userPhone ," +
            "u.email = :email where u.account = :account")
    int updateUserInfo(@Param("password") String password,
                           @Param("userPhone") String userPhone,
                           @Param("email") String email,
                           @Param("account") String account);
}
