package com.student.controller;

import com.student.pojo.file;
import com.student.pojo.fileView;
import com.student.pojo.userLogin;
import com.student.service.fileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    private fileService service;

    @Value("${file.uploadPath}")
    private String totalPath;

    @Value("${file.downloadPath}")
    private String loadPath;

    @RequestMapping("/addDir")
    public Map<String, Object> add(@RequestBody file file){
        Map<String, Object> map = new HashMap<>();
        userLogin user = getUserLogin();
        boolean b = service.makeNewDir(totalPath, user.getUsername(), file.getDir(), file.getFileName());
        map.put("flag", b);
        return map;
    }

    @RequestMapping("/deleteDir")
    public Map<String, Object> delete(@RequestParam(value = "father") String par){
        return null;
    }

    @RequestMapping("/All")
    public List<fileView> All(){
        userLogin user = getUserLogin();
        //String path = totalPath + user.getUsername() + File.separator;
        String path = totalPath + user.getUsername() + "/";
        File fileNew = new File(path);
        if(!fileNew.getAbsoluteFile().exists()){
            fileNew.getAbsoluteFile().mkdirs();
        }
        List<fileView> allFileAndDir = service.findAllFileAndDir(totalPath, user.getUsername(), path, 1);
        if (allFileAndDir.size() == 0) {
            File file = new File(path + System.currentTimeMillis());
            if(!file.getAbsoluteFile().exists()){
                file.getAbsoluteFile().mkdirs();
            }
            fileView fileView = new fileView(1, file.getName(), "dir", new ArrayList<>());
            allFileAndDir.add(fileView);
        }
        return allFileAndDir;
    }

    private static userLogin getUserLogin() {
        userLogin user = (userLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    @RequestMapping("/findAll")
    public Map<String, Object> fileFindAll(@RequestParam(value = "path") String fl){
        userLogin user = getUserLogin();
        String username = user.getUsername();
        List<String> list = fileSearch(new File(totalPath + username + fl), "");
        Map<String, Object> HashMap = new HashMap<>();
        HashMap.put("dir",list);
        return HashMap;
    }

    @RequestMapping("/find")
    public Map<String, Object> fileFind(){
        userLogin user = getUserLogin();
        //String username = BaseContext.getCurrentId();
        String username = user.getUsername();
        File file = null;
        if(username == null){
            file = new File(totalPath);
        } else {
            file = new File(totalPath + username + System.lineSeparator());
        }
        if(!file.getAbsoluteFile().exists()){
            file.getAbsoluteFile().mkdirs();
        }
        List<String> list = findDir(file);
        Map<String, Object> HashMap = new HashMap<>();
        HashMap.put("dir",list);
        return HashMap;
    }

    @RequestMapping (value = "/upload" ,method = RequestMethod.POST)
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
        String OriginalFilename = file.getOriginalFilename();
        // 防止重名覆盖，获取系统时间戳+原始文件的后缀名
        // fileName = System.currentTimeMillis()+"."+OriginalFilename.substring(OriginalFilename.lastIndexOf(".")+1);
        if(!fileName.equals("")){
            //fileName = fileName+"."+OriginalFilename.substring(OriginalFilename.lastIndexOf(".")+1);
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + System.currentTimeMillis() +"."+ OriginalFilename.substring(OriginalFilename.lastIndexOf(".")+1);
        } else {
            fileName = OriginalFilename;
        }
        userLogin user = getUserLogin();
        String username = user.getUsername();
        File dest=new File(totalPath + username + "/" + path + "/" + fileName);
        // 判断文件是否存在
        if(!dest.getParentFile().exists()){
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

    /*@RequestMapping("/download")
    public void downloadFile(@RequestParam(name = "path",required = true) String path, HttpServletResponse response) throws IOException {
        FileInputStream bis = null;
        BufferedOutputStream bos = null;
        userLogin user = getUserLogin();
        String username = user.getUsername();
        try {
            File file = new File(loadPath + username + path);
            if (!file.getName().contains(".")) {
                throw new IOException(file.getName() + "不是文件");
            }
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "filename=" + file.getName());
            bis = new FileInputStream(file);
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();
        } catch (Exception ex) {
            java.io.PrintWriter out1 = response.getWriter();
            response.setContentType("text/html;charset=UTF-8");
            out1.flush();
            out1.close();
        } finally {
            try {
                if (bos != null) bos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (bos != null) bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (bis != null) bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    /**
     * 返回一个文件夹下的所有子文件夹
     * @param fl
     * @return
     */
    public List<String> findDir(File fl) {
        List<String> list = new LinkedList<>();
        File[] files = fl.listFiles();
        for (File file : files) {
            if(!file.getName().contains(".")){
                list.add(file.getName());
            }
        }
        return list;
    }

    /**
     * 查找文件 或者文件夹下的所有文件
     * @param dir
     * @param FileName
     * @return
     */
    public List<String> fileSearch(File dir, String FileName) {
        List<String> list = new ArrayList<>();
        if (dir != null && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (FileName.equals("")) {
                            list.add(file.getName());
                        } else {
                            if (file.getName().contains(FileName)) {
                                list.add(file.getName());
                                return list;
                                //System.out.println("找到了："+file.getAbsolutePath());
                            }
                        }
                    } else {
                        //list.add(file.getName());
                        fileSearch(file, FileName);
                    }
                }
            }
        } else {
            log.info("不是文件夹！");
        }
        return list;
    }
}
