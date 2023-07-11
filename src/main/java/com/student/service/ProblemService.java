package com.student.service;

import com.student.mapper.ProblemMapper;
import com.student.mapper.StudentMapper;
import com.student.pojo.item;
import com.student.pojo.student;
import com.student.pojo.text;
import com.student.pojo.vo.problemVo;
import com.student.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProblemService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ProblemMapper problemMapper;

    public void saveItem(List<item> itemList, String fatherId){
        for (int i = 0; i < itemList.size(); i++) {
            String title = itemList.get(i).getTitle();
            if(title.indexOf("-第") != -1){
                String str = title.split("-")[0];
                String substring = title.substring(title.indexOf("-第") + 2, title.indexOf("-第") + 3);
                itemList.get(i).setFather_id(fatherId);
                itemList.get(i).setFatherItem_id(substring);
                itemList.get(i).setId(String.valueOf(i));
                itemList.get(i).setTitle(str);
                itemList.get(i).setText_id(String.valueOf(i));
                for (int j = 0; j < itemList.get(i).getTextList().size(); j++) {
                    text text = new text();
                    text.setId(itemList.get(i).getText_id());
                    text.setTikey(String.valueOf(j));
                    text.setTiValue(itemList.get(i).getTextList().get(j));
                    problemMapper.saveText(text);
                }
                problemMapper.saveItem(itemList.get(i));
            }else {
                itemList.get(i).setFather_id(fatherId);
                itemList.get(i).setId(String.valueOf(i));
                itemList.get(i).setText_id(String.valueOf(i));
                for (int j = 0; j < itemList.get(i).getTextList().size(); j++) {
                    text text = new text();
                    text.setId(itemList.get(i).getText_id());
                    text.setTikey(String.valueOf(j));
                    text.setTiValue(itemList.get(i).getTextList().get(j));
                    problemMapper.saveText(text);
                }
                problemMapper.saveItem(itemList.get(i));
            }
        }
    }


    public void saveProblem(problemVo proVo,String id) {
        String studentName = proVo.getName();
        student student = studentMapper.queryByName(studentName);
        String stId = student.getId();
        String name = proVo.getForm().getName();
        Date time = proVo.getForm().getDate();
        String middle_id = CommonUtil.generateRandomString(16);
        try{
            problemMapper.saveProblem(id,time,name,middle_id);
            problemMapper.saveConnect(id,stId);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }
}
