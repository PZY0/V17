package com.qianfeng.Netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Author pangzhenyu
 * @Date 2019/11/26
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    //在channel注册后，会执行该初始化方法
    protected void initChannel(SocketChannel channel) throws Exception {
        //1，通过SocketChannel获取对应的管道对象
        ChannelPipeline pipeline = channel.pipeline();
        //2，给管道添加Handler
        //HttpServerCodec 作用是做编解码
        //当请求发送到服务端，服务端需要做解码，响应信息到客户端需要做编码
        //netty接收的是二进制数据
        pipeline.addLast(new HttpServerCodec());
        //3，继续给管道添加Handler
        pipeline.addLast(new HelloHandler());
    }
}
