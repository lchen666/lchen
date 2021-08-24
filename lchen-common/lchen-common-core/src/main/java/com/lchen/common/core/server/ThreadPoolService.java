package com.lchen.common.core.server;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            3,
            3,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            namedThreadFactory,
            new ThreadPoolExecutor.AbortPolicy()
    );


    private static void shutdown(ExecutorService executorService) {
        // 第一步：使新任务无法提交
        executorService.shutdown();
        try {
            // 第二步：等待未完成任务结束
            if(!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                // 第三步：取消当前执行的任务
                executorService.shutdownNow();
                // 第四步：等待任务取消的响应
                if(!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Thread pool did not terminate");
                }
            }
        } catch(InterruptedException ie) {
            // 第五步：出现异常后，重新取消当前执行的任务
            executorService.shutdownNow();
            Thread.currentThread().interrupt(); // 设置本线程中断状态
        }
    }


    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            list.add(i);
        }
        try {
            list.forEach(integer -> {
                service.execute(() -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("i", integer);
                    if (integer % 2 ==0){
                        System.out.println(Thread.currentThread().getName() + Thread.currentThread().getId() + "-双数:"+ map.get("i").toString());
                    }else {
                        System.out.println(Thread.currentThread().getName() + Thread.currentThread().getId() + "-单数:"+ map.get("i").toString());
                    }
                });
            });
        }finally {
            System.out.println("关闭....");
            shutdown(service);
        }

    }
}
