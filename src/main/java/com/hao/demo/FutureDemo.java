package com.hao.demo;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FutureDemo {

    @SneakyThrows
    private static void jdkFuture() {
        // should use in joint with thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Future<Integer> future = executorService.submit(() -> {
            log.debug("need some time ...");
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        });
        log.debug("main thread waiting ...");
        log.debug("main thread get result: {}", future.get());
    }

    @SneakyThrows
    private static void nettyFuture() {
        // should use in joint with event loop
        EventLoopGroup eventExecutors = new DefaultEventLoopGroup();
        io.netty.util.concurrent.Future<Integer> future = eventExecutors.submit(() -> {
            log.debug("need some time ...");
            TimeUnit.SECONDS.sleep(1L);
            return 1;
        });
        future.addListener(future0 -> {
           if (future0.isSuccess()) {
               log.debug("async get result: {}", future0.get());
           }
        });
        log.debug("waiting for result ...");
        log.debug("sync get result: {}", future.get());
    }

    @SneakyThrows
    private static void nettyPromise() {
        EventLoopGroup eventLoopGroup = new DefaultEventLoopGroup();
        Promise<Integer> promise = new DefaultPromise<>(eventLoopGroup.next());
        new Thread(() -> {
            log.debug("need some time ...");
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            promise.setSuccess(1);
        }).start();
        promise.addListener(future0 -> {
            if (future0.isSuccess()) {
                log.debug("async get result: {}", future0.get());
            }
        });
        log.debug("waiting for result ...");
        log.debug("sync get result: {}", promise.get());
    }



    public static void main(String[] args) {
        // jdkFuture();
        //nettyFuture();
        nettyPromise();
    }
}
