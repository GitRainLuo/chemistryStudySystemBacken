package com.chemistrystudysystem.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/5/23 10:34
 * @Version:1.0
 */
public interface FileService {
    /**
     * 文件上传
     * @param files
     * @return
     * @throws Exception
     */
    JSONObject uploadFiles(MultipartFile[] files) throws Exception;

    /**
     * excel导出
     * @param dataList
     * @param cellCode
     * @param cellName
     * @return
     */
    JSONObject export(List<Map<String,Object>> dataList, String[] cellCode, String[] cellName) throws Exception;
}
