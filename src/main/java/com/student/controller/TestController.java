package com.student.controller;

import com.student.pojo.userLogin;
import com.student.service.fileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/test")
public class TestController {
    @Value("${file.downloadPath}")
    private String loadPath;

    @Autowired
    private fileService service;

    private static userLogin getUserLogin() {
        userLogin user = (userLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    @RequestMapping("/download/{name}")
    public void downloadFile(@PathVariable("name") String name, HttpServletResponse response) throws IOException {
        FileInputStream bis = null;
        BufferedOutputStream bos = null;
        userLogin user = getUserLogin();
        String username = user.getUsername();
        try {
            String pathfile = service.fileSearch(new File(loadPath + username), name.substring(0, name.lastIndexOf(".")));
            File file = new File(pathfile);
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
            PrintWriter out1 = response.getWriter();
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
    }
}
