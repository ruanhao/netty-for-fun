package com.hao.demo;

import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VolatileDemo {

    @SneakyThrows
    public static void main(String[] args) {
        MyObj obj = new MyObj();
        new Thread(obj).start();

        Thread.sleep(3000);
        obj.setI(3);
        TimeUnit.MINUTES.sleep(1L);
    }
}

@Slf4j
class MyObj implements Runnable {
    // volatile private int i = 1;
    private int i = 1;

    public void setI(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        log.info("running start");
        while(i == 1) {}
        log.info("running over");
    }

}
