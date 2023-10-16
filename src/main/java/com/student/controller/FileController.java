package com.student.controller;

import com.student.pojo.file;
import com.student.util.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Value("${file.uploadPath}")
    private String totalPath;

    public void fileSearchOld(File dir, String FileName){
        File[] files = dir.listFiles();

    }
    @RequestMapping("/find")
    public Map<String, Object> fileFind(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username1 = BaseContext.getCurrentId();
        String username2 = user.getUsername();
        File file = null;
        if(username2 == null){
            file = new File(totalPath);
        } else {
            file = new File(totalPath + username2);
        }
        if(!file.getAbsoluteFile().exists()){
            file.getAbsoluteFile().mkdirs();
        }
        List<String> list = fileSearch(file, "");
        Map<String, Object> HashMap = new HashMap<>();
        HashMap.put("dir",list);
        return HashMap;
    }

    @RequestMapping("/findAll")
    public Map<String, Object> fileFindAll(@RequestBody file fl){
        List<String> list = fileSearch(new File(fl.getDir()), "");
        Map<String, Object> HashMap = new HashMap<>();
        HashMap.put("dir",list);
        return HashMap;
    }

    @PostMapping("/upload")
    public Map<String,Object> uploadFile(@RequestParam(value = "file",required = false) MultipartFile file,
                                         @RequestParam(value = "path")String path,
                                         @RequestParam(value = "name")String fileName){
        Map<String, Object> map = new HashMap<>();
        map.put("flag",true);
        // 判断文件是否为空
        if(file.isEmpty()){
            map.put("flag",false);
            return map;
        }
        // 获取传过来的文件名字
        String OriginalFilename = file.getOriginalFilename();
        // 为了防止重名覆盖，获取系统时间戳+原始文件的后缀名
//        fileName = System.currentTimeMillis()+"."+OriginalFilename.substring(OriginalFilename.lastIndexOf(".")+1);
        if(!fileName.equals("")){
            fileName = fileName+"."+OriginalFilename.substring(OriginalFilename.lastIndexOf(".")+1);
        } else {
            fileName = OriginalFilename;
        }
        File dest=new File(path + fileName);
        // 判断文件是否存在
        if(!dest.getParentFile().exists()){
            // 不存在就创建一个
            dest.getParentFile().mkdirs();
        }
        try {
            // 后台上传
            file.transferTo(dest);
            return map;
        }catch (Exception e){
            e.printStackTrace();
            map.put("flag","error");
            return map;
        }
    }

    public static void main(String[] args) {
        File[] fl = new File("D:/").listFiles();
        for (File file : fl) {
            if(!file.getName().contains(".")){
                System.out.println(file.getName());
            }
        }

    }

    public List<String> fileSearch(File dir, String FileName){
        List<String> list = new ArrayList<>();
        if (dir!=null&&dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files!=null&&files.length>0) {
                for (File file : files) {
                    if (file.isFile()) {
                        if(FileName.equals("")){
                            list.add(file.getAbsolutePath());
                        } else {
                            if(file.getName().contains(FileName)){
                                list.add(file.getAbsolutePath());
                                return list;
                                //System.out.println("找到了："+file.getAbsolutePath());
                            }
                        }
                    }else{
                        fileSearch(file,FileName);
                    }
                }
            }
        }else{
            log.info("不是文件夹！");
        }
        return list;
    }
}
