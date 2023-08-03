package com.student.service.JucTest;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Test01 {

    public static void main(String[] args) throws InterruptedException {
        WindowSell windowSell = new WindowSell(1000);

        List<Thread> listThread = new ArrayList<>();
        List<Integer> list = new Vector<>();
        for (int i = 0; i < 2000; i++) {
            Thread thread = new Thread(() -> {
                int sell = windowSell.Sell(randomAmount());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                list.add(sell);
            });
            listThread.add(thread);
            thread.start();
        }

        for (Thread thread : listThread) {
            thread.join();
        }

        log.debug("余票:{}",windowSell.getCount());
        log.debug("卖出的票:{}",list.stream().mapToInt(i->i).sum());
    }


    static Random random = new Random();

    public static int randomAmount() {
        return random.nextInt(5) + 1;
    }

}

class WindowSell {
    private int count;

    public WindowSell(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public synchronized int Sell(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }
}