package com.hao.handlers;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.atomic.AtomicLong;

@ChannelHandler.Sharable
public class MetricsHandler extends ChannelDuplexHandler {

    public static final AtomicLong totalConnections = new AtomicLong();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        totalConnections.incrementAndGet();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        totalConnections.decrementAndGet();
        super.channelInactive(ctx);
    }
}
