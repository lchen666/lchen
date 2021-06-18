package com.lchen.common.core.server;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

/**
 * 自定义线程池
 */
public class ThreadPoolService {

    /**
     * 自定义线程名称,方便的出错的时候溯源
     */
    private static ThreadFactory namedThreadFactory = new CustomizableThreadFactory("lc-thread-pool-");


    /**
     * corePoolSize    核心线程的大小
     * maximumPoolSize 线程池中允许的最大线程数量
     * keepAliveTime   当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间
     * unit            keepAliveTime 的时间单位
     * workQueue       用来储存等待执行任务的队列
     * threadFactory   创建线程的工厂类
     * handler         拒绝策略类,当线程池数量达到上线并且workQueue队列长度达到上限时就需要对到来的任务做拒绝处理
     */
    private static ExecutorService service = new ThreadPoolExecutor(
            4,
            8,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(4),
            namedThreadFactory,
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * 获取线程池
     * @return
     */
    public static ExecutorService getEs(){
        return service;
    }


    /**
     * 使用线程池创建线程并异步执行任务
     * @param runnable 任务
     */
    public static void newTask(Runnable runnable){
        service.execute(runnable);
    }


    public static void main(String[] args) {
        for (int i = 0; i < 12; i++){
            newTask(() -> System.out.println(Thread.currentThread().getName() + " is running .."));
        }
    }
}
