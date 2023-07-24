package com.student.service.JucTest;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "test")
public class Test {
    public static void main(String[] args) throws InterruptedException {
        DesignDemo designDemo = new DesignDemo();
        designDemo.start();
        TimeUnit.SECONDS.sleep(5);
        designDemo.stop();
    }
}

/**
 * 两阶段终止
 */
@Slf4j(topic = "designDemo")
class DesignDemo {
    private Thread monitor;

    public void start(){
        monitor = new Thread(() -> {
            while (true){
                Thread thread = Thread.currentThread();
                if(thread.isInterrupted()){
                    log.debug("线程退出前执行。。。");
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                    log.debug("线程监控中。。。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //出现异常重新打断 异常会将打断标记重置为假
                    thread.interrupt();
                }
            }
        });
        monitor.start();
    }


    public void stop(){
        monitor.interrupt();
    }
}