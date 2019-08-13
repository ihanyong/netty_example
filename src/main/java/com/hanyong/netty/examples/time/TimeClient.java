package com.hanyong.netty.examples.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.function.Function;

/**
 * TimeClient
 *
 * @author yong.han
 * 2018/11/13
 */
public class TimeClient {

    private static void exec(ChannelInitializer<SocketChannel>  initializer) {

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(initializer );

            ChannelFuture f = b.connect("localhost", 8080).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }


    public static class CloseServer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                @Override
                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                    ByteBuf c = ctx.alloc().buffer(8);
                    c.writeLong(-1);
                    ctx.writeAndFlush(c);
                    ctx.close();
                }
            });
        }
    }
    public static class ReadTime extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                    ByteBuf buf = (ByteBuf) msg;
                    System.out.println("server time is " + new Date(buf.readLong()));
                    ctx.close();
                }
            });
        }
    }

    public static void main(String[] args) {
        exec(new ReadTime());
//        exec(new CloseServer());
    }
}
