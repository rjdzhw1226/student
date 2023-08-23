package com.student.controller;

import com.student.pojo.card;
import com.student.pojo.vo.classVo;
import com.student.service.ClassService;
import com.student.util.CommonUtil;
import com.student.util.Tess4jClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/card")
public class CardController {
    @Autowired
    private ClassService classService;

    @Autowired
    private Tess4jClient tess4jClient;

    @RequestMapping("/All")
    public List<card> query(){
        List<card> porn = classService.queryCard();
        return porn;
    }

    @RequestMapping("/upload")
    public Map<String,Object> uploadFile(@RequestParam("file") MultipartFile file){
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

}
