package com.student.service.JucTest;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j(topic = "c.test04")
public class Test04 {
    //锁
    static final Object lock = new Object();

    //t2运行标记
    static boolean t2runned = false;

    public static void main(String[] args) {
        parkMethod();
    }
    public static void waitMethod() {
        Thread t1 = new Thread(()->{
            synchronized (lock) {
                //while 防止虚假唤醒
                while(!t2runned){
                    try{
                        //等待释放锁 t2就能拿到锁
                        lock.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                log.info("1");
            }

        },"t1");

        Thread t2 = new Thread(()->{
            synchronized (lock){
                log.info("2");
                t2runned = true;
                lock.notify();
            }
        },"t2");

        t1.start();
        t2.start();
    }
    public static void parkMethod() {
        Thread t1 = new Thread(()->{
            LockSupport.park();
            log.info("1");
        },"t1");

        Thread t2 = new Thread(()->{
            log.info("2");
            LockSupport.unpark(t1);
        },"t2");

        t1.start();
        t2.start();
    }

}
