package com.qianfeng.v17msg.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.v17msg.pojo.Message;
import com.qianfeng.v17msg.util.ChannelUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author pangzhenyu
 * @Date 2019/11/26
 */
@Component
@ChannelHandler.Sharable //设置handler在channel中共享
@Slf4j
public class WSHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //创建对象，管理所有客户端channel
    //private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //获取到数据
        String text = msg.text();
        System.out.println("接收到的消息为："+text);
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(text, Message.class);
        //判断处理
        if("1".equals(message.getMsgType())){
            String userId = message.getData().toString();
            log.info("与来自{}的{}建立长连接",ctx.channel().remoteAddress(),userId);
            ChannelUtils.add(userId,ctx.channel());
            log.info("{}与{}建立了映射关系",userId,ctx.channel());
        }
        ctx.fireChannelRead(message);
        //发送消息到客户端
        //channels.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress()+"说："+text));
    }

    /*@Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"连上服务器");
        //channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"断开连接");
        //channels.remove(ctx.channel());
    }*/
}
