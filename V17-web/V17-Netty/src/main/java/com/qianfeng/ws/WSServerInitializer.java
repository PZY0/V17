package com.qianfeng.ws;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author pangzhenyu
 * @Date 2019/11/26
 */
public class WSServerInitializer extends ChannelInitializer<SocketChannel> {
    //在channel注册后，会执行该初始化方法
    protected void initChannel(SocketChannel channel) throws Exception {
        //1，通过SocketChannel获取对应的管道对象
        ChannelPipeline pipeline = channel.pipeline();
        //2，给管道添加Handler
        //HttpServerCodec 作用是做编解码
        //当请求发送到服务端，服务端需要做解码，响应信息到客户端需要做编码
        //netty接收的是二进制数据
        pipeline.addLast(new HttpServerCodec());
        //3.考虑到传输数据，设置支持大数据流
        pipeline.addLast(new ChunkedWriteHandler());
        //4.对HTTPMessage做聚合,设置支持传输的最大长度为1024*32 字节
        pipeline.addLast(new HttpObjectAggregator(1024*32));
        //设置跟http协议相关的处理器--end
        //5,设置跟websocket相关的设置
        //用于指定给客户端连接访问的路由 : /ws
        //对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //6,设置自定义handler
        pipeline.addLast(new WSHandler());
    }
}
