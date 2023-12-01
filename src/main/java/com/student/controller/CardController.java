package com.student.controller;

import com.alibaba.fastjson.JSON;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.student.pojo.card;
import com.student.pojo.pdf;
import com.student.pojo.userLogin;
import com.student.service.ClassService;
import com.student.service.fileService;
import com.student.util.CommonUtil;
import com.student.util.Tess4jClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
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

//    @RequestMapping("/fileUpload")
//    public Map<String,Object> saveFile(@RequestParam("file") MultipartFile file){
//        String filename = file.getOriginalFilename();
//        return null;
//    }

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


    @Value("${file.readPath}")
    private String pdfPath;

    @RequestMapping("/fileUpload")
    @ResponseBody
    public Map<String, Object> saveFile(@RequestParam("file") MultipartFile file){
        Map<String, Object> map = new HashMap<>();
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : null;
        String fileName = (UUID.randomUUID() + suffix).replaceAll("-", "");
        File dir = new File(imgPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(imgPath + fileName));
            map.put("fileName", fileName);
            map.put("path", "/product/" + fileName);
            //map.put("path", imgPath + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            map.put("fileName", "error");
        }
        return map;
    }

    /**
     * @param jsonText 填充的数据
     * @throws IOException
     */
    @RequestMapping("/printPDF")
    public void interviewReportPDF(HttpServletResponse response, @RequestBody pdf jsonText) {
        Map<String, String> entity = new HashMap<>();
        File file = null;
        String newPDFPath = null;
        File template = null;
        try {
            entity = objectToMap(jsonText);
            long millis = System.currentTimeMillis();
            newPDFPath = pdfPath + millis + ".pdf";
            String absolutePath = pdfPath + "person_demo.pdf";
            file = replaceTextFieldPdf(absolutePath, newPDFPath, entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回给前端
        response.reset();
        response.setHeader("ETag", String.valueOf(System.currentTimeMillis()));
        response.setDateHeader("Last-Modified", System.currentTimeMillis());
        response.setHeader("Access-Control-Allow-Origin", "*");
        FileInputStream in = null;
        OutputStream out = null;
        try {
            if (file != null) {
                in = new FileInputStream(file);
            } else {
                throw new RuntimeException("文件为空！");
            }
            out = response.getOutputStream();
            long fileLen = in.available();
            response.setHeader("Content-Length", String.valueOf(fileLen));
            int len;
            byte[] bs = new byte[1024];
            while ((len = in.read(bs)) != -1) {
                out.write(bs, 0, len);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            delFile(newPDFPath);
        }
    }
    @Value("${file.imgPath}")
    private String imgPath;

    /**
     * 字段处理
     * @param str
     * @return
     */
    public Map<String, String> objectToMap(pdf str) {
        str.setAvatar_img("1|" + imgPath + str.getAvatar_img());
        str.setLife1_img("1|" + imgPath + str.getLife1_img());
        str.setLife2_img("1|" + imgPath + str.getLife2_img());
        str.setLife3_img("1|" + imgPath + str.getLife3_img());
        return (Map<String, String>) JSON.parseObject(JSON.toJSONString(str), Map.class);
    }


    /**
     * 删除文件
     * @param filePathAndName 指定得路径
     */
    public void delFile(String filePathAndName) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            java.io.File myDelFile = new java.io.File(filePath);
            myDelFile.delete();
        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();
        }
    }

    public static final File replaceTextFieldPdf(String templatePdfPath, String destPdfPath,
                                                 Map<String, String> params) {
        PdfDocument pdf = null;
        try {
            // 判断文件是否存在
            File file = new File(destPdfPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 有参数才替换
            if (params != null && !params.isEmpty()) {
                pdf = new PdfDocument(new PdfReader(templatePdfPath), new PdfWriter(destPdfPath));
                PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
                Map<String, PdfFormField> fields = form.getFormFields(); // 获取所有的表单域
                for (String param : params.keySet()) {
                    PdfFormField formField = fields.get(param); // 获取某个表单域
                    if (formField != null) {
                        if(param.contains("img")){
                            replaceFieldImage(params, pdf, param, formField); // 替换图片
                        } else {
                            formField.setFont(getImportFont("SourceHanSansCN-Regular.ttf")).setValue(params.get(param)); // 替换值
//									formField.setFont(getDefaultFont()).setValue(params.get(param)); // 替换值
                        }
                    }
                }
                form.flattenFields();// 锁定表单，不让修改
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pdf != null) {
                pdf.close();
            }
        }
        return new File(destPdfPath);
    }

    private static final void replaceFieldImage(Map<String, String> params, PdfDocument pdf, String param,
                                                PdfFormField formField) throws MalformedURLException {
        String value = params.get(param);
        String[] values = value.split("\\|");
        Rectangle rectangle = formField.getWidgets().get(0).getRectangle().toRectangle(); // 获取表单域的xy坐标
        PdfCanvas canvas = new PdfCanvas(pdf.getPage(Integer.parseInt(values[0])));
        ImageData image = ImageDataFactory.create(values[1]);
        Image image1 = new Image(image);
        Canvas canvasMine = new Canvas(canvas, pdf,rectangle);
        canvasMine.add(image1);
    }

    public static PdfFont getImportFont(String fontName) {
        // 获取resource下文件夹路径
        String path = "/template/";
        String fontPath = path + fontName;
        try {
            // 处理中文乱码（支持Linux系统）
            FontProgram fontProgram = FontProgramFactory.createFont(fontPath, false);
            PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, false);
            return font;
        } catch (IOException e) {
            // 记录日志
            e.printStackTrace();
        }
        return null;
    }
}
