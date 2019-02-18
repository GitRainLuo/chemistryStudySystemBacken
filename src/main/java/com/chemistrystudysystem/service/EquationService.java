package com.chemistrystudysystem.service;

import com.chemistrystudysystem.entity.Equation;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/2/16 13:40
 * @Version:1.0
 */
public interface EquationService {
    /*
     * 分页查询方程式列表
     **/
    Page<Equation> findEquationDataList(Map<String,Object> condition);
}
