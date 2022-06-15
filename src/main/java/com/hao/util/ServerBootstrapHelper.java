package com.hao.util;

import com.hao.handlers.EchoHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerBootstrapHelper {

    public static final NioEventLoopGroup BOSS = new NioEventLoopGroup(1, new DefaultThreadFactory("BOSS"));
    public static final NioEventLoopGroup WORKER = new NioEventLoopGroup(0, new DefaultThreadFactory("WORKER"));


    private static void bind(final ServerBootstrap serverBootstrap, final int port, boolean tryPort) {
        serverBootstrap.bind(port).addListener(
                future -> {
                    if (future.isSuccess()) {
                        log.info("Bind on port {} successfully", port);
                    } else {
                        log.error("Failed to bind on port {}, try {}", port, port + 1);
                        if (tryPort) {
                            bind(serverBootstrap, port + 1, true);
                        }
                    }
                }
        );
    }

    public static void startEchoServer(int port) {
        startServer(ch -> {
            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
            ch.pipeline().addLast(new EchoHandler());
        }, port, true);
    }

    public static void startServer(Consumer<NioSocketChannel> initializer, int port) {
        startServer(null, null, initializer, port, false);
    }

    public static void startServer(Consumer<NioSocketChannel> initializer, int port, boolean tryPort) {
        startServer(null, null, initializer, port, tryPort);
    }

    public static void startServer(NioEventLoopGroup boss, NioEventLoopGroup worker,
                                   Consumer<NioSocketChannel> initializer,
                                   int port, boolean tryPort) {
        if (boss == null) {
            boss = BOSS;
        }
        if (worker == null) {
            worker = WORKER;
        }
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_SNDBUF, 1024) // just for test, DON'T use it in production
                .option(ChannelOption.SO_RCVBUF, 1024) // just for test, DON'T use it in production
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        initializer.accept(ch);
                    }
                });

        bind(serverBootstrap, port, tryPort);
    }

}
