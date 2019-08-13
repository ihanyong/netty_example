package com.hanyong.netty.examples.discard;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * DiscardClient
 *
 * @author yong.han
 * 2018/11/13
 */
public class DiscardClient {
    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new DiscardClientHandler())
//                                    .addLast(new ChannelOutboundHandlerAdapter())
                            ;
                        }
                    });

            Channel channel = b.connect("localhost", 8080).sync().channel();

            System.out.println("-- send more");
            for (int i = 0; i < 10; i++) {
                ByteBuf temp  = channel.alloc().buffer();
                temp.writeCharSequence(("test client channel write " + i), CharsetUtil.UTF_8);
                channel.writeAndFlush(temp);
            }

            channel.closeFuture().sync();

        } finally {
            eventLoopGroup.shutdownGracefully().sync();
            System.out.println("--- exit!");
        }


    }
}
