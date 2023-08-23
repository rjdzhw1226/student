package com.student.util;

import lombok.Getter;
import lombok.Setter;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Getter
@Setter
@Component
public class Tess4jClient {

    @Value("${tess4j.datapath}")
    private String dataPath;
    @Value("${tess4j.language}")
    private String language;

    public String doOCR(BufferedImage image) throws TesseractException {
        //创建Tesseract对象
        ITesseract tesseract = new Tesseract();
        //设置字体库路径
        tesseract.setDatapath(dataPath);
        //中文识别
        tesseract.setLanguage(language);
        //执行ocr识别
        String result = tesseract.doOCR(image);
        //替换回车和tal键  使结果为一行
        result = result.replaceAll("\\r|\\n", "-").replaceAll(" ", "");
        return result;
    }

    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        String net = "";
        URL url = new URL(net);
        URLConnection urlConnection = url.openConnection();
        InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
        int ch;
        while((ch = isr.read()) != -1){
            sb.append((char) ch);
        }
        isr.close();
        System.out.println(sb.toString());
    }

}
