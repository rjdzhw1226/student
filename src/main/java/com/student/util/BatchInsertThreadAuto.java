package com.student.util;

import com.student.service.ExcelService;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.concurrent.CountDownLatch;

public class BatchInsertThreadAuto implements Runnable {
    private ExcelService excelService;
    private CountDownLatch begin;
    private CountDownLatch end;
    private int sta;
    private int size;

    private Sheet sheet;

    private Class aClass;
    public BatchInsertThreadAuto() {
    }

    public BatchInsertThreadAuto(Sheet sheet, CountDownLatch begin, CountDownLatch end,
                             ExcelService excelService,int sta,int size,Class aClass) {
        this.begin = begin;
        this.end = end;
        this.excelService = excelService;
        this.sta=sta;
        this.size=size;
        this.sheet=sheet;
        this.aClass=aClass;
    }

    @Override
    public void run() {
        try {
            if (sheet != null) {
                //执行真正的处理
                excelService.ExcelUpDoneAuto(sheet, sta, size,aClass);
            }
            // 执行完让线程直接进入等待
            begin.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 当一个线程执行完 了计数要减一不然这个线程会被一直挂起
            end.countDown();
        }
    }
}
