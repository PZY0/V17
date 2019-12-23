package com.qianfeng.ws;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Author pangzhenyu
 * @Date 2019/11/26
 */
public class WSHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //创建对象，管理所有客户端channel
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //获取到数据
        String text = msg.text();
        System.out.println("接收到的消息为："+text);
        //发送消息到客户端
        channels.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress()+"说："+text));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"连上服务器");
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"断开连接");
        channels.remove(ctx.channel());
    }
}
