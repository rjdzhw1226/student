package com.student.service;

import com.student.pojo.fileView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class fileService {

    public static void main(String[] args) {
        //System.out.println(findAllFileAndDir("", "", "D:\\DOWNLOAD\\1", 1));
        //System.out.println(makeNewDir("D:\\DOWNLOAD", "1", "a", "test"));
        System.out.println("1.jpg".substring(0, "1.jpg".lastIndexOf(".")));
    }

    public List<fileView> findAllFileAndDir(String totalPath, String username, String path, int count){
        List<fileView> list = new ArrayList<>();
        File dir = new File(path);
        if(dir != null && dir.isDirectory()){
            File[] files = dir.listFiles();
            for (File file : files) {
                if(file.isDirectory()){
                    path = file.getAbsolutePath();
                    fileView fileView = new fileView(count, file.getName(), "dir", findAllFileAndDir(totalPath, username, path, count + 1));
                    list.add(fileView);
                } else {
                    fileView fileView = new fileView(count, file.getName(), "file", new ArrayList<>());
                    list.add(fileView);
                }
            }
        }
        return list;
    }

    public boolean makeNewDir(String totalPath, String username, String path, String name){
        String separator = File.separator;
        if(isLinux()){
            File file = new File(totalPath + username + separator + path);
            if(!file.getAbsoluteFile().exists()){
                return false;
            } else {
                File fileNew = new File(file.getAbsolutePath() + separator + name);
                fileNew.getAbsoluteFile().mkdirs();
                return true;
            }
        } else {
            File file = new File(totalPath + separator + username + separator + path);
            if(!file.getAbsoluteFile().exists()){
                return false;
            } else {
                File fileNew = new File(file.getAbsolutePath() + separator + name);
                fileNew.getAbsoluteFile().mkdirs();
                return true;
            }
        }
    }
    String path = "";
    public String fileSearch(File dir, String FileName) {
        //File dir = new File(loadPath + username + File.separator);
        if (dir != null && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (file.getName().contains(FileName)) {
                            path =  file.getAbsolutePath();
                        }
                    } else {
                        fileSearch(file, FileName);
                    }
                }
            }
        } else {
            log.info("不是文件夹！");

        }
        return path;
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

}
