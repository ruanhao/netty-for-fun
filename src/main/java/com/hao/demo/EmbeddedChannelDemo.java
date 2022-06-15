package com.hao.demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmbeddedChannelDemo {


    private static void demo() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new SimpleChannelInboundHandler<String>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, String s) {
                        ctx.fireChannelRead(s + "[H1]");
                    }
                },
                new SimpleChannelInboundHandler<String>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, String s) {
                        ctx.fireChannelRead(s + "[H2]");
                    }
                },
                new SimpleChannelInboundHandler<String>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, String s) {
                        ctx.fireChannelRead(s + "[H3]");
                    }
                }
        );
        embeddedChannel.writeInbound("msg");
        for (Object inboundMessage : embeddedChannel.inboundMessages()) {
            log.info("inbound message: {}", inboundMessage);
        }

    }

    public static void main(String[] args) {
        demo();
    }
}
