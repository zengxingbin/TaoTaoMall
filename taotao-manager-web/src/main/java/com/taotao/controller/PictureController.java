package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.utils.FastDFSClient;
import com.taotao.utils.JsonUtils;

@Controller
public class PictureController {
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;
    @RequestMapping("/pic/upload")
    @ResponseBody
    public String uploadPictrue(MultipartFile uploadFile) {
        try {

            // 获取上传的文件名
            String originalFilename = uploadFile.getOriginalFilename();
            // 获取图片的扩展名
            String extName = originalFilename.substring(originalFilename.lastIndexOf("." + 1));
            // 使用文件上传的客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:/resource/client.conf");
            //上传文件
            String path = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            //拼接path和ip，返回完整的url
            String url = IMAGE_SERVER_URL + path;
            //返回map，也可以返回一个pojo，这里选择map
            Map result = new HashMap();
            result.put("error", 0);
            result.put("url", url);
            return JsonUtils.objectToJson(result);
        } catch (Exception e) {
            // TODO: handle exception
            Map result = new HashMap<>();
            result.put("error", 1);
            result.put("message", "图片上传失败");
            return JsonUtils.objectToJson(result);
        }

    }
}
