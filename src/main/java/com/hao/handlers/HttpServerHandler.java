package com.hao.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
        if (httpObject instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) httpObject;
            log.info("Receive http request {} {}", httpRequest.method(), httpRequest.uri());
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                    ((HttpRequest) httpObject).protocolVersion(),
                    HttpResponseStatus.OK);

            byte[] bytes = "<h1>hello</h1>".getBytes(StandardCharsets.UTF_8);
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
            response.content().writeBytes(bytes);
            ctx.writeAndFlush(response);
        } else if (httpObject instanceof LastHttpContent) {
            LastHttpContent lastHttpContent = (LastHttpContent) httpObject;
            ByteBuf byteBuf = lastHttpContent.content();
            log.info("Receive http last content {}B", byteBuf.readableBytes());
        } else if (httpObject instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) httpObject;
            ByteBuf byteBuf = httpContent.content();
            log.info("Receive http content {}B", byteBuf.readableBytes());
        }  else { // invalid
            log.error("Receive invalid http message: {}:{}", httpObject.getClass(), httpObject);
        }
    }
}
