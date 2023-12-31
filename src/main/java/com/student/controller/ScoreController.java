package com.student.controller;

import com.student.pojo.*;
import com.student.pojo.vo.scoreVo;
import com.student.service.ScoreService;
import com.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@RequestMapping("/score")
@RestController
public class ScoreController {

    @Value("${student.path}")
    private String basePath;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ScoreService scoreService;

    @RequestMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/All")
    @ResponseBody
    public page<student> selectAll(@RequestBody pageBean page){
        Integer Page = page.getPage();
        Integer Size = page.getSize();
        page<student> students = studentService.queryAll(Page, Size);
        return students;
    }


    @RequestMapping("/show/{id}")
    @ResponseBody
    public List<scoreVo> show(@PathVariable("id") String id){
        List<scoreVo> scoreVos = scoreService.queryScore(id);
        return scoreVos;
    }

    @RequestMapping("/query/{queryString}")
    @ResponseBody
    public page<student> query(@PathVariable("queryString") String queryString, @RequestBody pageBean page){
        Integer Page = page.getPage();
        Integer Size = page.getSize();
        page<student> studentpage = studentService.queryLike(queryString, Page, Size);
        return studentpage;
    }
}

