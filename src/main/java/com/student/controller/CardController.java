package com.student.controller;

import com.student.pojo.card;
import com.student.pojo.userLogin;
import com.student.service.ClassService;
import com.student.service.fileService;
import com.student.util.CommonUtil;
import com.student.util.Tess4jClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/card")
public class CardController {
    @Autowired
    private ClassService classService;

    @Autowired
    private Tess4jClient tess4jClient;

    @Value("${student.path}")
    private String path;

    @Value("${student.loadpath}")
    private String loadPath;

    @RequestMapping("/All")
    public List<card> query(){
        List<card> porn = classService.queryCard();
        return porn;
    }

    @RequestMapping("/upload")
    public Map<String,Object> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("name") String name){
        Map<String,Object> map = new HashMap<>();
        try{
            //具体逻辑处理
            BufferedImage bufferedImage = CommonUtil.InputImage(file);
            String doOCR = tess4jClient.doOCR(bufferedImage);
            map.put("data",doOCR);
            map.put("msg","识别成功");
        }catch(Exception e){
            map.put("msg",e.getMessage());
            return map;
        }
        return map;
    }

    @RequestMapping("/fileUpload")
    public Map<String,Object> saveFile(@RequestParam("file") MultipartFile file){
        String filename = file.getOriginalFilename();
        return null;
    }

//    @RequestMapping("/exec")
//    public Map<String,Object> ExecFile(@RequestParam("file") MultipartFile file){
//        Map<String,Object> map = new HashMap<>();
//        try{
//            String name = file.getOriginalFilename();
//            String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//            String suffix = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
//            String filename = System.currentTimeMillis() + suffix;
//            //1.后台保存位置
//            File dir = new File(path);
//            if(!dir.exists()){
//                dir.mkdirs();
//            }
//            file.transferTo(new File(path + filename));
//            log.info("dest:{}",path + filename);
//            Map<String, Object> result = commonService.execLinux("temp","jpg");
//            String time = (String) result.get("time");
//            File finFile = new File(loadPath + "result"+ time +".txt");
//            FileReader fs = new FileReader(finFile);
//            BufferedReader bf = new BufferedReader(fs);
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = bf.readLine()) != null ){
//                sb.append(line).append("\n");
//            }
//            map.put("data",sb.toString());
//            map.put("msg","识别成功");
//        }catch(Exception e){
//            map.put("msg",e.getMessage());
//            return map;
//        }
//        return map;
//    }
}
