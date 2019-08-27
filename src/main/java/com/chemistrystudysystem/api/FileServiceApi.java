package com.chemistrystudysystem.api;

import com.alibaba.fastjson.JSONObject;
import com.chemistrystudysystem.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/5/23 10:33
 * @Version:1.0
 */
@RestController
@RequestMapping("file")
public class FileServiceApi {
    @Autowired
    private FileService fileService;
    /**
     * @Author jay
     * @Description //文件上传
     */
    @PostMapping("/uploadFiles")
    public JSONObject uploadFiles(HttpServletRequest request,@RequestParam("file") MultipartFile[] files) throws Exception {
//        System.out.println(request);
//        String scheme = request.getScheme();
//        HttpSession session = request.getSession();
//        ServletContext context = request.getSession().getServletContext();
//        String fileDir = request.getSession().getServletContext().getRealPath("fileDir");
//        String serverName = request.getServerName();
//        Integer port = request.getServerPort();
//        String path = request.getContextPath();
//        String projectServerPath = request.getScheme() + "://" + request.getServerName() + ":"
//                + request.getServerPort() + request.getContextPath() + "/upload/";
//        System.out.println(projectServerPath);
//        System.out.println(files);
        return fileService.uploadFiles(files);
    }

    /**
     *
     * @Author jay
     * @Description //导出excel
     */
    @PostMapping("/export")
    public JSONObject export(HttpServletRequest request,@RequestBody JSONObject jsonObject) throws Exception{
        ServletContext servletContext = request.getServletContext();
        List<Map<String,Object>> data = (List<Map<String, Object>>) jsonObject.get("data");
//        Object key = jsonObject.get("key");
//        Object title = jsonObject.get("title");
        //转换成String[]
        List<String> keyList = (List<String>) jsonObject.get("key");
        String[] key = keyList.toArray(new String[keyList.size()]);
        List<String> titleList = (List<String>) jsonObject.get("title");
        String[] title = titleList.toArray(new String[titleList.size()]);
        return fileService.export(data,key,title);
    }

    @GetMapping("/data")
    public JSONObject data(){
        JSONObject res = new JSONObject();
        res.put("code",0);
        res.put("msg","请求成功");
        return res;
    }

    @PostMapping("/test")
    public Boolean test(@RequestBody JSONObject jsonObject){
        if("test".equals(String.valueOf(jsonObject.get("name")))){
            return true;
        }
        return false;
    }
}
