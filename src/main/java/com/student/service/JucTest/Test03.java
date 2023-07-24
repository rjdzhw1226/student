package com.student.service.JucTest;

import com.student.service.JucTest.utils.Downloader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

/**
 * 保护性暂停
 */
@Slf4j(topic = "c.test03")
public class Test03 {
    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();
        new Thread(()->{
            log.info("t1等待");
            List<String> list = (List<String>) guardedObject.get(2000);
            log.info("t1结果:{}" , list.size());

        }, "t1").start();

        new Thread(()->{
            log.info("t2下载");
            try {
                Thread.sleep(2000);
                List<String> download = Downloader.download();
                guardedObject.complete(download);
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"t2").start();
    }

    static class MailBoxes{
        private static Map<Integer,GuardedObject> boxes = new Hashtable<>();

        private static int id = 1;

        private static synchronized int generateId(){
            return id++;
        }

        public static GuardedObject createGuardedObject(){
            GuardedObject guardedObject = new GuardedObject(generateId());
            boxes.put(guardedObject.getId(),guardedObject);
            return guardedObject;
        }

        public static Set<Integer> getIds(){
            return boxes.keySet();
        }

    }

    /**
     * 用于线程之间等待并传递信息 join方法基于此实现
     */
    static class GuardedObject {

        //标识
        private int id;
        //结果
        private Object response;

        public GuardedObject(){}
        public GuardedObject(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }
        //超时时间timeout
        public Object get(long timeout){
            synchronized (this){
                //开始时间
                long begin = System.currentTimeMillis();
                //经过时间
                long passedTime = 0;
                while(response == null){
                    long waitTime = timeout - passedTime;
                    if(timeout - passedTime < 0){
                        break;
                    }
                    try {
                        //设置等待超时
                        this.wait(waitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    passedTime = System.currentTimeMillis() - begin;
                }
               return response;
            }
        }

        public void complete(Object response){
            synchronized (this){
                this.response = response;
                //获取到结果唤醒等待对列中所有的值
                this.notifyAll();
            }

        }
    }
}
