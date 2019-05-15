package com.mall.manager.controller;

import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.common.util.FtpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@Api(description = "图片上传管理")
public class UploadController {
    //    @Value("${FTP.ADDRESS}")
    private String host = "192.168.229.128";    // host
    //    @Value("${FTP.PORT}")
    private int port = 21;    // 端口 = 21
    //    @Value("${FTP.USERNAME}")
    private String userName = "ftpuser";    // ftp用户名
    //    @Value("${FTP.PASSWORD}")
    private String passWord = "20171009";    // ftp用户密码
    //    @Value("${FTP.BASEPATH}")
    private String basePath = "/home/ftpuser/images";    // 文件在服务器端保存的主目录
    //    @Value("${IMAGE.BASE.URL}")
    private String baseUrl = "http://192.168.229.128/images";    // 访问图片时的基础url

    @RequestMapping(value = "/upload/file", method = RequestMethod.POST)
    @ApiOperation(value = "上传图片")
    public Result<Object> upload(@RequestParam("pic") MultipartFile file) throws Exception {
//        String pathRoot = "D:/workspace/";
//        String imagePath = "";
//        if (!file.isEmpty()) {
//            //生成uuid作为文件名称
//            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//            //获得文件类型（可以判断如果不是图片，禁止上传）
//            String contentType = file.getContentType();
//            //获得文件后缀名称
//            String imageName = contentType.substring(contentType.indexOf("/") + 1);
//            imagePath = uuid + "." + imageName;
//            file.transferTo(new File(pathRoot + imagePath));
//        }
        try {
            //生成uuid作为文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //获得文件类型（可以判断如果不是图片，禁止上传）
            String contentType = file.getContentType();
            //获得文件后缀名称
            String imageName = contentType.substring(contentType.indexOf("/") + 1);
            //给上传的图片生成新的文件名
            String imagePath = uuid + "." + imageName;
            String filePath = new DateTime().toString("/yyyy/MM");
            String avatarPath = baseUrl + filePath + "/" + imagePath;
            //获取上传的io流
            InputStream input = file.getInputStream();
            //调用FtpUtil工具类进行上传
            boolean result = FtpUtil.uploadFile(host, port, userName, passWord, basePath, filePath, imagePath, input);
            if (result) {
                return new ResultUtil<Object>().setData(avatarPath);
            } else {
                return new ResultUtil<Object>().setErrorMsg("上传错误");
            }
        } catch (IOException e) {
            return new ResultUtil<Object>().setErrorMsg("上传异常");
        }
    }
}
