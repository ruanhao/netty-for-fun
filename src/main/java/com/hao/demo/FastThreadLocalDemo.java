package com.hao.demo;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;

public class FastThreadLocalDemo {

    private static FastThreadLocal<Object> THREAD_LOCAL = new FastThreadLocal<>() {

        @Override
        protected Object initialValue() {
            return new Object();
        }
    };

    @SneakyThrows
    public static void main(String[] args) {

        new Thread(() -> System.out.println(THREAD_LOCAL.get())).start();
        new FastThreadLocalThread(() -> System.out.println(THREAD_LOCAL.get())).start();

        TimeUnit.SECONDS.sleep(1L);
    }
}
