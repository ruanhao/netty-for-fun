package com.hao.demo;

import io.netty.util.HashedWheelTimer;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

@Slf4j
public class HashedWheelTimerDemo {


    private static void test() {

    }

    @SneakyThrows
    public static void main(String[] args) {
        HashedWheelTimer timer = new HashedWheelTimer(
                new BasicThreadFactory.Builder().namingPattern("hashed-wheel-%d").daemon(true).build(),
                10L, TimeUnit.MILLISECONDS, 1024, false
        );

        for (int i = 0; i < 5; i++) {
            final int idx = i;
            timer.newTimeout(timeout -> {
                log.info("Time up: {}", idx);
            }, 1L, TimeUnit.SECONDS);
        }

        TimeUnit.SECONDS.sleep(3L);
    }


}
