package org.dream;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.dream.handler.WebSocketServerHandler;

/**
 * Created by Administrator on 2017/8/17.
 */
public class NettyServer {
    private static ServerBootstrap bootstrap;

    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(64*1024));
                        pipeline.addLast(new WebSocketServerHandler());
                       /* pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new ConsoleHandler());*/
                    }
                });

        // Start the client.
        ChannelFuture ch = null;
        try {
            ch = bootstrap.bind(8082).sync();
            System.out.println("start server success, you can telnet 127.0.0.1 8082 to send massage for this server");
            // Wait until the connection is closed.
            ch.channel().closeFuture().sync();
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
