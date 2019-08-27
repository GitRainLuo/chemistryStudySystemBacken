package com.chemistrystudysystem.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chemistrystudysystem.entity.FileEntity;
import com.chemistrystudysystem.repository.FileRepository;
import com.chemistrystudysystem.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/5/23 10:34
 * @Version:1.0
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService{

    private final static String excel2003L = ".xls";//excel 2003前
    private final static String excel2007U = ".xlsx";//excel 2007后
    private final static String serviceUrl = "http://112.74.45.191";//服务器地址
    private final static String uploadPath= "/www/server/tomcat/webapps";//上传路径
    private final static String uploadDirectory = "/upload";//需要上传到的上传的文件夹
    private final static String exportName = "excel";//导出excel名
    private final static String tmpPath = "/tmp/excelTemporary";//临时存放路径

    /**
     *文件仓库
     */
    @Autowired
    private FileRepository fileRepository;

    /**
     * 批量或者单个上传文件
     * @param files
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject uploadFiles(MultipartFile[] files) throws Exception {
        if(files.length == 0){
            log.error("上传文件不能为空");
            throw new Exception("上传文件为空!");
        }
        JSONObject result = new JSONObject();
        //文件保存集合
        List<FileEntity> fileEntities = new ArrayList<>();
        //创建文件夹
//        File sFile = new File("E:\\savedImages");
        File sFile = new File(uploadPath+uploadDirectory);
        //boolean directory = sFile.isDirectory();
        //判断文件夹是否存在 不存在则创建
        if(!sFile.isDirectory()){
            sFile.mkdir();
        }
        //循环处理文件 上传 获取返回的保存信息
        for (MultipartFile mf : files){
            fileEntities.add(uploadFile(mf,sFile));
        }
        if(fileEntities != null && fileEntities.size()>0){
            result.put("code","success");
            result.put("msg","上传成功");
            result.put("data",fileEntities);
        }else {
            result.put("code","fail");
            result.put("msg","上传失败");
        }
        return result;
//        String projectServerPath = request.getScheme() + "://" + request.getServerName() + ":"
//                + request.getServerPort() + request.getContextPath() + "/upload/";
    }

    /**
     * 文件上传处理(单个处理)
     * @param file
     * @param filePath
     * @return 保存的文件信息
     * @throws Exception
     */
    private FileEntity uploadFile(MultipartFile file,File filePath) throws Exception {
        //获取文件名
        String fileName = file.getOriginalFilename();
        log.info("上传文件名为:"+fileName);
        //String saveName = fileName.substring(0,fileName.indexOf("."));
        //严格来说应该用lastIndexOf
        //获取后缀名
        String[] suffixArr = fileName.split("\\.");
        String suffix = suffixArr[1];
        log.info("后缀:"+suffix);
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        log.info("上传文件后缀名为:"+suffixName);
        //处理文件名
        String saveName = fileName.substring(0,fileName.lastIndexOf("."))+String.valueOf(System.currentTimeMillis()).substring(7)+suffixName;
        //生成时间戳
        FileEntity savedFile = null;
        try {
            //File tempFile = File.createTempFile(System.currentTimeMillis() + "", suffixName);
            log.info("时间戳:"+System.currentTimeMillis()+"");
            //file.transferTo(tempFile);
            byte[] bytes = new byte[1024];
//            int length = bytes.length;
//            //获取输入流
            InputStream inputStream = file.getInputStream();
            //保存文件地址
            //windows
            //String path = filePath.getPath()+"\\"+saveName;
            //linux
            String path = filePath.getPath()+"/"+saveName;
            log.info("**********"+"path:"+path+"**********");
            OutputStream outputStream = new FileOutputStream(path);
            log.info("输入流:"+inputStream);
            int len = 0;
            //读取
            while ((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
            }
            //验证是不是excel
//            if(validateExcel(suffix)){
//                //解析
//                List<List<Object>> list = getBankListByExcel(inputStream,suffixName);
//                if(list != null && list.size() > 0){
//                    for (int i = 0;i < list.size();i++){
//                        List<Object> lObj = list.get(i);
//                        System.out.println(lObj+"****"+i+"****");
//                    }
//                }
//            }
            //outputStream.write(bytes);
            inputStream.close();
            outputStream.close();
            log.info("上传文件"+fileName+"到服务器"+"成功");
            //保存上传信息
            FileEntity fileEntity = new FileEntity();
            //保存的文件名
            fileEntity.setFileName(fileName);
            //文件类型
            fileEntity.setFileType(suffix);
            //文件服务器地址链接
            //windows
            //String url = serviceUrl+uploadDirectory+"\\"+saveName;
            //Linux服务器
            String url = serviceUrl+uploadDirectory+"/"+saveName;
            fileEntity.setSaveUrl(url);
            savedFile = fileRepository.save(fileEntity);
            if(savedFile == null){
                log.error("文件{}上传保存失败",fileName);
                throw new Exception("文件上传保存失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回刚才保存的信息
        return savedFile;
    }

    /**
     * maven+springmvc+POI对上传的Excel文件进行解析并操作
     * 获取io流中的数据 组装成一个list
     */
    public List<List<Object>> getBankListByExcel(InputStream is,String suffixName) throws Exception {
        List<List<Object>> list = null;
        //创建excel工作薄
        Workbook workbook = this.getWorkbook(is,suffixName);
        if(workbook == null){
            log.error("创建工作薄失败");
            throw new Exception("创建工作簿失败");
        }
        //页
        Sheet sheet = null;
        //行
        Row row = null;
        //列
        Cell cell = null;
        list = new ArrayList<List<Object>>();
        //遍历Excel中的所有sheet
        for (int i = 0;i<workbook.getNumberOfSheets();i++){
            //获取第i页的sheet
            sheet = workbook.getSheetAt(i);
            if(sheet == null){
                continue;
            }
            //遍历当前sheet的所有行

            //这里的加一是因为下面的循环跳过取第一行表头的数据内容了
            System.out.println("***第一行"+sheet.getFirstRowNum()+"最后一行***"+sheet.getLastRowNum());
            for (int j = sheet.getFirstRowNum();j<sheet.getLastRowNum() + 1;j++){
                //去第j行
                row = sheet.getRow(j);
                System.out.println("hang:"+row.getFirstCellNum());
                if(row == null || row.getFirstCellNum() == j){
                    continue;
                }
                //遍历所有的列
                List<Object> li = new ArrayList<>();
                //获取列数
                System.out.println("---第一列"+row.getFirstCellNum()+"最后一列---"+row.getLastCellNum());
                for (int k = row.getFirstCellNum();k<row.getLastCellNum();k++){
                    //取得第k列
                    cell = row.getCell(k);
                    //li.add(cell);
                    li.add(this.getCellValue(cell));
                }
                list.add(li);
            }
        }
        //关闭工作簿
        workbook.close();
        return list;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     */
    public Workbook getWorkbook(InputStream is,String suffixName) throws Exception {
        Workbook wb = null;
        if(excel2003L.equals(suffixName)){
            wb = new HSSFWorkbook(is);
        }else if(excel2007U.equals(suffixName)){
            wb = new XSSFWorkbook(is);
        }else {
            log.error("解析文件格式有问题!");
            throw new Exception("解析文件格式有问题");
        }
        return wb;
    }

    /**
     * 对表格中的数据进行格式化
     */
    public Object getCellValue(Cell cell){
        Object value = null;
        DecimalFormat df = new DecimalFormat("0");//格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化日期格式 yyyy-MM-dd
        DecimalFormat dfn = new DecimalFormat("0.00");//格式化数字
        //列类型
        switch (cell.getCellType()){
            //string
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            //数 值
            case Cell.CELL_TYPE_NUMERIC:
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    value = df.format(cell.getNumericCellValue());
                }else if("m/d/yy".equals(cell.getCellStyle().getDataFormat())){
                    value = sdf.format(cell.getDateCellValue());
                }else {
                    value = dfn.format(cell.getNumericCellValue());
                }
                break;
            //boolean
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            //空
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

    /**
     * 校验上传文件是否是excel
     */
    private Boolean validateExcel(String suffix){
        return excel2003L.equals(suffix) || excel2007U.equals(suffix) ? true : false;
    }

    /**
     * 下载
     */
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception{
       response.setContentType("text/html;charset=UTF-8");
       BufferedInputStream bis = null;
       BufferedOutputStream bos = null;
       request.setCharacterEncoding("UTF-8");
       String path = request.getSession().getServletContext().getRealPath("/resources/");
       String fileName = "template.xlsx";
       File file = new File(path+fileName);
       if(!file.isDirectory()){
           file.mkdir();
       }
       response.setContentType("application.x-excel");
       response.setCharacterEncoding("UTF-8");
       response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
       response.setHeader("Content-Length", String.valueOf(file.length()));
       bis = new BufferedInputStream(new FileInputStream(file));
       bos = new BufferedOutputStream(response.getOutputStream());
       byte[] bytes = new byte[1024];
       int len = 0;
       while ((len = bis.read(bytes,0,bytes.length)) != -1){
           bos.write(bytes,0,len);
       }
       bis.close();
       bos.close();
    }

    /**
     * 导出excel
     */
    public JSONObject export(List<Map<String,Object>> dataList, String[] cellCode, String[] cellName) throws Exception{
        JSONObject result = new JSONObject();
        //创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        Integer activeSheetIndex = workbook.getActiveSheetIndex();
//        workbook.setSheetName(0,"sheet1");
        //创建sheet
        Sheet sheet = workbook.createSheet();
        //创建行 表头
        Row row = sheet.createRow(0);
        //创建cellStyle
        CellStyle cellStyle = workbook.createCellStyle();
        //水平居中
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //垂直居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        //创建列
        for (int i = 0;i < cellName.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(cellName[i]);
        }
        //日期格式化
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //创建除了表头的其他行
        //需要创建的行数就是dataList的length
        for (int j = 0;j < dataList.size();j++){
            //第二行开始 除了表头
            row = sheet.createRow(j+1);
            //创建列
            for (int k = 0;k < cellCode.length;k++){
                Cell cell = row.createCell(k);
                String s = cellCode[k];
                Set<Map.Entry<String, Object>> entry = dataList.get(j).entrySet();
                for(Map.Entry<String,Object> every : dataList.get(j).entrySet()){
                    String key = every.getKey();
                    Object value = every.getValue();
                    if(every.getKey().equals(cellCode[k])){
                        //自动设置列宽
                        sheet.autoSizeColumn(k,true);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(every.getValue().toString());
                    }
                }
            }
        }
        workbook.setSheetName(0,"sheet1");
        //时间戳
        String timetmp = String.valueOf(System.currentTimeMillis()).substring(7);
        //导出到本地(本地调试时)
//        String path = "E:\\Excel";
//        File file = new File(path);
//        if (!file.isDirectory()){
//            file.mkdir();
//        }
//        String excelPath = path+"\\"+exportName+timetmp+".xls";
//        OutputStream fos = new FileOutputStream(excelPath);
//        workbook.write(fos);
//        fos.close();
//        result.put("code",0);
//        result.put("msg","导出成功!");
//        result.put("path",excelPath);
        //生成临时文件 放到临时文件夹
        //String upPath = "E:\\Excel"+"\\"+exportName+timetmp+".xls";
        //windows
        //String upPath = tmpPath+"\\"+exportName+timetmp+".xls";
        //linux
//        String upPath = tmpPath+"/"+exportName+timetmp+".xls";
//        File tmpFile = new File(upPath);
//        //判断文件夹存在不 不存在就创建
//        if(!tmpFile.isDirectory()){
//            tmpFile.mkdir();
//        }
//        OutputStream os = new FileOutputStream(tmpFile);
//        //生成excel
//        workbook.write(os);
//        //取输入流
//        FileInputStream fis = new FileInputStream(tmpFile);
        File tmpFile = new File(tmpPath);
        //判断文件夹存在不 不存在就创建
        if(!tmpFile.isDirectory()){
            tmpFile.mkdir();
        }
        //windows
        //String upPath = "E:\\Excel"+"\\"+exportName+timetmp+".xls";
        //linux
        String upPath = tmpFile.getPath()+"/"+exportName+timetmp+".xls";
        OutputStream os = new FileOutputStream(upPath);
        //生成excel
        workbook.write(os);
        log.info("创建临时文件{}成功",upPath);
        File readfile = new File(upPath);
        //取输入流
        FileInputStream fis = new FileInputStream(readfile);
        log.info("读取文件字节流{}成功",fis);
        String fName = readfile.getName().substring(0,readfile.getName().indexOf(timetmp))+readfile.getName().substring(readfile.getName().lastIndexOf("."));
        //转为MultipartFile
        MockMultipartFile multipartFile = new MockMultipartFile("file",fName,"text/plain", IOUtils.toByteArray(fis));
        //MultipartFile multipartFile = new MockMultipartFile("file",tmpFile.getName(),"text/plain",IOUtils.toByteArray(fis));
        //组装
        MockMultipartFile[] multipartFiles = {multipartFile};
        //关闭流
        os.close();
        fis.close();
        //删除创建的临时文件
        boolean isDel = readfile.delete();
        if(isDel){
            log.info("临时文件{}已经删除",readfile.getName());
        }
        //请求上传文件(批量的)
        result = uploadFiles(multipartFiles);
        return result;
    }

    /*
     * 表格数据格式划
     */
    private Object formatValue(Object obj){
        Object value = null;
        return value;
    }

    /**
     * List<String>转String[]
     */
    public String[] listToStringArr(List<String> list){
        //list = ["a","b","c"]
        String[] result = null;
        //方法一
        result = list.toArray(new String[list.size()]);
        //方法二
//        result = new String[list.size()];
//        for (int i = 0;i < list.size();i++){
//            result[i] = list.get(i);
//        }
        return result;
    }

    /**
     * String[]转List<String>
     */
    public List<String> stringToList(String[] arr){
        //String[] arr = {"a","b","c"}
        List<String> list = new ArrayList<>();
        //方法一
        list = Arrays.asList(arr);
        //方法二
//        Collections.addAll(list,arr);
        //方法三
//        for (String s : arr){
//            list.add(s);
//        }
        return list;
    }
}
