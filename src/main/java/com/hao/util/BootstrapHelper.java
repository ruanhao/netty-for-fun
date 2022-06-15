package com.hao.util;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BootstrapHelper {

    public static final NioEventLoopGroup WORKER = new NioEventLoopGroup(0, new DefaultThreadFactory("WORKER"));

    public static void connect(NioEventLoopGroup worker, Consumer<NioSocketChannel> initializer, String ip, int port) {
        if (worker == null) {
            worker = WORKER;
        }
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            initializer.accept(ch);
                        }
                    })
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
            ChannelFuture channelFuture = bootstrap.connect(ip, port);
            channelFuture.sync().channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (worker != WORKER) {
                worker.shutdownGracefully();
            }
        }


    }

}
