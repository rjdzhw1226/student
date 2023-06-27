package com.student.util;
import java.util.concurrent.CountDownLatch;

import com.student.service.ExcelService;
import org.apache.poi.ss.usermodel.Sheet;


public class TestBatchInsertThread implements Runnable {

    private ExcelService excelService;
    /** 每个线程处理的起始数据 */
    private CountDownLatch begin;
    /** 每个线程处理的结束数据 */
    private CountDownLatch end;
    /**excel中读取的首行*/
    private int sta;
    /**excel中读取的条数*/
    private int size;

    private Sheet sheet;
    public TestBatchInsertThread() {
    }

    public TestBatchInsertThread(Sheet sheet, CountDownLatch begin, CountDownLatch end,
                                 ExcelService excelService,int sta,int size) {
        this.begin = begin;
        this.end = end;
        this.excelService = excelService;
        this.sta=sta;
        this.size=size;
        this.sheet=sheet;
    }

    @Override
    public void run() {
        try {
            if (sheet != null) {
                //执行真正的处理
                excelService.ExcelUpDone(sheet, sta, size);
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
