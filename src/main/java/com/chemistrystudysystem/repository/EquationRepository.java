package com.chemistrystudysystem.repository;

import com.chemistrystudysystem.entity.Equation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/2/16 13:41
 * @Version:1.0
 */
@Repository
public interface EquationRepository extends JpaRepository<Equation,Integer>,JpaSpecificationExecutor<Equation>{
}
