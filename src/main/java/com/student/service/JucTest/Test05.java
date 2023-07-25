package com.student.service.JucTest;

public class Test05 {

    public void print(String str, int waitFlag, int nextFlag){
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this){
                while(flag != waitFlag){
                    try{
                        this.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println(str);
        flag = nextFlag;
        this.notifyAll();
    }
    private int flag;
    private int loopNumber;

    public Test05(int flag, int loopNumber){
        this.flag = flag;
        this.loopNumber = loopNumber;
    }
}
