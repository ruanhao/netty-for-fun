package com.hao.demo;

import io.netty.util.Recycler;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecyclerDemo {

    private static User sharedUser;

    @SneakyThrows
    private static void sleep(long time, TimeUnit unit) {
        unit.sleep(time);
    }

    @SneakyThrows
    private static void getInAThreadAndRecycleInAnotherThread() {

        new Thread(() -> {
            User user = RECYCLER.get();
            sharedUser = user;
            log.info("User created in thread A: {}", user);
            sleep(3L, TimeUnit.SECONDS);
            User user1 = RECYCLER.get();
            log.info("Fetch user again: {}:{}", user1, user1.name);
        }).start();

        TimeUnit.SECONDS.sleep(1L);

        new Thread(() -> {
            if (sharedUser == null) {
                log.error("shared user not ready");
            } else {
                sharedUser.setName("Joey");
                sharedUser.recycle();
                log.info("User recycled in Thread B: {}", sharedUser); // not using the one cached in thread A, this is by design
            }

        }).start();
    }

    @SneakyThrows
    private static void getAndRecycleInAThreadAndGetInAnotherThread() {
        new Thread(() -> {
            User user = RECYCLER.get();
            log.info("User in thread A: {}", user);
            user.recycle();
        }).start();

        TimeUnit.SECONDS.sleep(1L);

        new Thread(() -> {
            User user = RECYCLER.get();
            log.info("User in Thread B: {}", user); // not using the one cached in thread A, this is by design
        }).start();
    }

    private static final Recycler<User> RECYCLER = new Recycler<>() {
        @Override
        protected User newObject(Handle<User> handle) {
            return new User(handle);
        }
    };

    private static void simple() {
        User user = RECYCLER.get();
        System.out.println(user);

        // when user is not used for now, it can be recycled
        user.recycle();

        // later, I want to use the obj again
        User user1 = RECYCLER.get();
        System.out.println(user1);  // it is the same as before

    }

    public static void main(String[] args) {
        // simple();
        // getAndRecycleInAThreadAndGetInAnotherThread();
        getInAThreadAndRecycleInAnotherThread();
    }
}

interface Recyclable {
    void recycle();
}


class User implements Recyclable {
    private final Recycler.Handle<User> handle;

    @Setter
    @Getter
    String name;

    public User(Recycler.Handle<User> handle) {
        this.handle = handle;
    }

    @Override
    public void recycle() {
        handle.recycle(this);
    }
}
