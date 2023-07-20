package com.student.util.redis;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class LockRedis {

    //Redis
    private StringRedisTemplate stringRedisTemplate;
    //锁状态
    private final AtomicBoolean locked = new AtomicBoolean();
    //等待队列
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();


    public void lock() {
        boolean wasInterrupted = false;
        Thread current = Thread.currentThread();
        waiters.add(current);
        //尝试加锁 在队列中不是第一个或无法获取锁时阻塞
        while (waiters.peek() != current || !tryLock()) {
            //挂起当前线程，没有获取到锁，不继续执行
            LockSupport.park();
            //忽略已经被中断的线程
            if (Thread.interrupted()) {
                wasInterrupted = true;
            }
        }
        waiters.remove();//从队列中移除线程
        //退出时重申中断状态
        if (wasInterrupted) {
            current.interrupt();
        }
    }
    public boolean tryLock() {
        //原子操作
        return locked.compareAndSet(false, true);
    }

    public void unlock() {
        //释放锁
        locked.set(false);
        //唤醒队列中第一个线程
        LockSupport.unpark(waiters.peek());
    }


}
