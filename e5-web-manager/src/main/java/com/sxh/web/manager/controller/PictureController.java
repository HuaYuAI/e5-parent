package com.sxh.web.manager.controller;

import com.sxh.common.utils.FastDFSClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HuaYu on 2018/2/18.
 */
@Controller
public class PictureController {
    public static final String IMAGE_SERVER_ADDRESS="http://192.168.177.123/";

//    @Value("${image_server_address}")
//    private String imageServerUrl;

    @ResponseBody
    @RequestMapping("pic/upload")
    public Map upload(MultipartFile uploadFile){
        Map map = new HashMap();
        try {
            //使用封装好的
            FastDFSClient fds = new FastDFSClient("classpath:fdfs_client.conf");
            //把图片的字节上传
            //带上后缀,获取文件名的后缀
            String originalFilename = uploadFile.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String ext = originalFilename.substring(index + 1, originalFilename.length());
            String filePath = fds.uploadFile(uploadFile.getBytes(),ext);//group1/M00/00/00/wKgCBFpm1k6ALh_1AAEwHdk6R6M609.jpg
            //正常返回，生成的map参数为 error:0  url:图片路径
            map.put("error",0);
            map.put("url",IMAGE_SERVER_ADDRESS+filePath);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error",1);
            map.put("message","文件上传失败");
        }
        return map;
    }
}
