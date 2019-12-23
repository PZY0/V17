package com.qianfeng.v17msg.ws;

import com.qianfeng.v17msg.pojo.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author pangzhenyu
 * @Date 2019/11/27
 */
@Component
@ChannelHandler.Sharable //设置handler在channel中共享
@Slf4j
public class HeartHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        if("2".equals(message.getMsgType())){
            log.info("{}发来心跳请求",ctx.channel().remoteAddress());
            return;
        }
        ctx.fireChannelRead(message);
    }
}
