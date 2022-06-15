package com.hao.demo;

import com.hao.handlers.StringHandler;
import com.hao.util.ServerBootstrapHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.nio.charset.StandardCharsets;

public class LengthFieldBasedFrameDecoderDemo {

    private static final int port = 8080;

    /**
     * BEFORE DECODE                    AFTER DECODE
     * +--------+----------------+      +----------------+
     * | Length | Actual Content |----->| Actual Content |
     * | 0x000C | "HELLO, WORLD" |      | "HELLO, WORLD" |
     * +--------+----------------+      +----------------+
     */
    public static void decodeV1() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(ServerBootstrapHelper.BOSS, ServerBootstrapHelper.WORKER)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        ch.pipeline().addLast(new io.netty.handler.codec.LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2,
                                0, 2, false));
                        ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                        ch.pipeline().addLast(StringHandler.INSTANCE);
                    }
                }).bind(port);
    }

    /**
     * BEFORE DECODE                                 AFTER DECODE
     * +----------+----------+----------------+      +----------------+
     * | Header 1 |  Length  | Actual Content |----->| Actual Content |
     * |  0xCAFE  | 0x00000C | "HELLO, WORLD" |      | "HELLO, WORLD" |
     * +----------+----------+----------------+      +----------------+
     */
    public static void decodeV2() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(ServerBootstrapHelper.BOSS, ServerBootstrapHelper.WORKER)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        ch.pipeline().addLast(new io.netty.handler.codec.LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                                2, 2,
                                0, 4, false));
                        ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                        ch.pipeline().addLast(StringHandler.INSTANCE);
                    }
                }).bind(port);
    }

    /**
     * BEFORE DECODE                                 AFTER DECODE
     * +----------+----------+----------------+      +----------------+
     * |  Length  | Header 1 | Actual Content |----->| Actual Content |
     * | 0x00000C |  0xCAFE  | "HELLO, WORLD" |      | "HELLO, WORLD" |
     * +----------+----------+----------------+      +----------------+
     */
    public static void decodeV3() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(ServerBootstrapHelper.BOSS, ServerBootstrapHelper.WORKER)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        ch.pipeline().addLast(new io.netty.handler.codec.LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                                0, 3,
                                2, 5, false));
                        ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                        ch.pipeline().addLast(StringHandler.INSTANCE);
                    }
                }).bind(port);
    }

    /**
     * BEFORE DECODE                                  AFTER DECODE
     * +------+--------+------+----------------+      +----------------+
     * | HDR1 | Length | HDR2 | Actual Content |----->| Actual Content |
     * | 0xCA | 0x000C | 0xFE | "HELLO, WORLD" |      | "HELLO, WORLD" |
     * +------+--------+------+----------------+      +----------------+
     */
    public static void decodeV4() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(ServerBootstrapHelper.BOSS, ServerBootstrapHelper.WORKER)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        ch.pipeline().addLast(new io.netty.handler.codec.LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                                1, 2,
                                1, 4, false));
                        ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                        ch.pipeline().addLast(StringHandler.INSTANCE);
                    }
                }).bind(port);
    }

    /**
     * BEFORE DECODE                                  AFTER DECODE
     * +------+--------+------+----------------+      +----------------+
     * | HDR1 | Length | HDR2 | Actual Content |----->| Actual Content |
     * | 0xCA | 0x0010 | 0xFE | "HELLO, WORLD" |      | "HELLO, WORLD" |
     * +------+--------+------+----------------+      +----------------+
     * length: for whole packet
     */
    public static void decodeV5() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(ServerBootstrapHelper.BOSS, ServerBootstrapHelper.WORKER)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        // ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        ch.pipeline().addLast(new io.netty.handler.codec.LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                                1, 2,
                                -3, 4, false));
                        ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                        ch.pipeline().addLast(StringHandler.INSTANCE);
                    }
                }).bind(port);
    }

    public static void main(String[] args) {
        // decodeV1();
        // decodeV2();
        // decodeV3();
        // decodeV4();
        decodeV5();
    }
}
