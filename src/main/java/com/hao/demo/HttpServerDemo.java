package com.hao.demo;

import com.hao.handlers.HttpServerHandler;
import com.hao.util.ServerBootstrapHelper;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerDemo {

    private static void runServer() {
        ServerBootstrapHelper.startServer(ch -> {
            // ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
            ch.pipeline().addLast(new HttpServerCodec());
            ch.pipeline().addLast(new HttpServerHandler());
        }, 8080, false);
    }

    public static void main(String[] args) {
        runServer();
    }
}
