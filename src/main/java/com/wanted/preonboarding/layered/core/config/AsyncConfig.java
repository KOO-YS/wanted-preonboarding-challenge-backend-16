package com.wanted.preonboarding.layered.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    private static final int CORE_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 30;
    private static final int QUEUE_CAPACITY = 100;
    private static final boolean WAIT_FOR_TASK = true;
    private static final int AWAIT_TERMINATION_SECONDS = 30;

    /**
     * 외부 API 요청은 비교적 시간이 오래 걸리는 작업이므로 Thread에 작업 위임
     * 최초 3개의 스레드에서 처리하다가 처리 속도가 밀릴 경우 100개 큐에서 대기하고
     * 그보다 많은 요청이 발생하면 최대 30개의 스레드를 생성해서 처리
     *
     * ref: https://dev-coco.tistory.com/186
     */
    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(CORE_POOL_SIZE); // 동시에 실행할 기본 스레드 수
        taskExecutor.setMaxPoolSize(MAX_POOL_SIZE); // threadPool에서 사용할 수 있는 최대 스레드 수
        taskExecutor.setQueueCapacity(QUEUE_CAPACITY); // 작업 Queue 사이즈
        taskExecutor.setThreadNamePrefix("Executor-");

        taskExecutor.setWaitForTasksToCompleteOnShutdown(WAIT_FOR_TASK); // 애플리케이션 종료 요청 시 queue에 남아있는 모든 작업들이 완료될 때까지 대기
        taskExecutor.setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS);

        // 최대 개수의 스레드 수가 생성되어 있으며, 설정한 queue가 가득 찬 상태에서 추가 작업이 들어올 경우 RejectedExecution 예외 발생
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());    // shutdown 상태가 아닐 때, ThreadPoolTaskExecutor에 요청한 스레드가 직접 처리
        // taskExecutor.initialize(); 명시하지 않아도 빈으로 등록되면서 실행
        return taskExecutor;
    }
}
