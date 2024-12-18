package com.wanted.preonboarding.core.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.Executor;

@SpringBootTest
public class AsyncTest {

    private final Executor executor;

    public AsyncTest(@Qualifier("threadPoolTaskExecutor") Executor executor) {
        this.executor = executor;
    }


    public void executeThreads() {
        Runnable runnable = () -> {
            try {
                System.out.println("executing thread "+ Thread.currentThread().getName());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
            }
        };
        for (int i = 0; i < 10; i++) {
            executor.execute(runnable);
        }
    }
}
