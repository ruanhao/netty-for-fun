package com.hao.handlers;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class StringHandler extends SimpleChannelInboundHandler<String> {

    public static StringHandler INSTANCE = new StringHandler();

    private StringHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.print(msg);
    }
}
