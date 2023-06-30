package com.student.util;


import com.student.mapper.SubjectMapper;
import com.student.pojo.vo.subjectVo;
import com.student.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


public class CallQueryThread implements Callable<List> {

    public static SpringContextUtils springContextUtil = new SpringContextUtils();

    private int start;

    private int end;

    private List datas;

    public CallQueryThread(int start,int end) {
        this.start=start;
        this.end=end;
        //每个线程查询出来的数据集合
        ExcelService Service= springContextUtil.getBean(ExcelService.class);
        List<subjectVo> count = Service.queryData(start,end);
        datas = count;
    }

    @Override
    public List call() throws Exception {
        return datas;
    }
}
