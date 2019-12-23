package com.qianfeng.v17msg.ws;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author pangzhenyu
 * @Date 2019/11/27
 */
@Component
@Slf4j
public class WSServer implements CommandLineRunner {

    @Value("${netty.server.port}")
    private int nettyServerPort;

    @Autowired
    private WSHandler wsHandler;

    @Autowired
    private HeartHandler heartHandler;

    @Override
    public void run(String... args) throws Exception {
        //1.定义主线程组，处理客户端的连接请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //2.定义从线程组，处理由主线程交过来的任务，完成具体的IO操作
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //3.创建Netty服务启动类对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //4.设置主从线程组
        serverBootstrap.group(bossGroup,workerGroup)
                //5.设置NIO的双向通道
                .channel(NioServerSocketChannel.class)
                //6.设置子处理器，用于处理workGroup的任务
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
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
                        pipeline.addLast(new ReadTimeoutHandler(9, TimeUnit.SECONDS));
                        pipeline.addLast(wsHandler);
                        pipeline.addLast(heartHandler);
                    }
                });
        //7.设置8888端口号，并启动服务,将其变为同步操作
        serverBootstrap.bind(nettyServerPort).sync();
        log.info("监听端口{}",nettyServerPort);
    }
}
