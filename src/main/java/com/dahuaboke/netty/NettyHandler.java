package com.dahuaboke.netty;

import com.dahuaboke.handler.HttpController;
import com.dahuaboke.spring.SpringBeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/17 10:01
 */
public class NettyHandler extends ChannelInboundHandlerAdapter {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            HttpMethod method = fullHttpRequest.getMethod();
            String uri = fullHttpRequest.getUri();
            Map<String, String> headers = new HashMap();
            Iterator<Map.Entry<String, String>> iterator = fullHttpRequest.headers().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                headers.put(next.getKey(), next.getValue());
            }
            String body = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
            HttpController bean = SpringBeanUtil.getBean(HttpController.class);
            String result = bean.handle(method, uri, headers, body);
            ByteBuf content = Unpooled.copiedBuffer(result, CharsetUtil.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            ctx.writeAndFlush(response);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
