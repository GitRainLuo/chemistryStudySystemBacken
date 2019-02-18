package com.chemistrystudysystem.api;/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/2/16 13:40
 * @Version:1.0
 */

import com.chemistrystudysystem.entity.Equation;
import com.chemistrystudysystem.service.EquationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName:
 * @Description:
 * @Auther: hmj
 * @Date: 2019/2/16 13:40
 * @Version:1.0
 */
@RestController
@RequestMapping("equation")
public class EquationServiceApi {
    /*
     * EquationService
     **/
    @Autowired
    private EquationService equationService;

    /*
     * 分页查询方程式列表
     **/
    @PostMapping("/findEquationDataList")
    public Page<Equation> findEquationDataList(@RequestBody(required = false) Map<String,Object> condition){
        return equationService.findEquationDataList(condition);
    }
}
