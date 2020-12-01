package com.forward.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: 文件上传控制器
 * @Author : 王俊
 * @date :  2020/12/1
 */
@RestController
@Slf4j
public class FileUploadController {

    //创建目录 以时间格式
    SimpleDateFormat sfd = new SimpleDateFormat("/yyyy/MM/dd/");

    /**
     * 上传文件
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public Map<String,Object> fileUpload(MultipartFile file, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        String filename = file.getOriginalFilename();
        //测试只允许pdf格式
        if(!filename.endsWith(".pdf")){
            result.put("status","error");
            result.put("msg","文件格式不合法！");
            return result;
        }
        //创建文件夹
        String format = sfd.format(new Date());
        //项目当前运行的路径
        String realPath = request.getServletContext().getRealPath("/") + format;
        //保存文件的文件夹
        File folder = new File(realPath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String newFileName = UUID.randomUUID().toString() + ".pdf";
        try {
            file.transferTo(new File(folder,newFileName));
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + format + newFileName;
            log.info("路径：" + url);
            result.put("status","success");
            result.put("url",url);
        } catch (IOException e) {
            result.put("status","error");
            result.put("msg","文件上传失败！");
        }
        return result;
    }

}
