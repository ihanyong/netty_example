package com.hanyong.netty.examples.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * DiscardClientHandler
 *
 * @author yong.han
 * 2018/11/13
 */
public class DiscardClientHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext ctx;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        ByteBuf b = (ByteBuf) msg;

        try{
            System.out.println("DiscardClientHandler read msg: " + b.readCharSequence(b.capacity(), CharsetUtil.UTF_8));
//            ctx.close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;

        System.out.println("> chanel active, send a msg to server.");
        ByteBuf b = ctx.alloc().buffer();
        b.writeCharSequence("test", CharsetUtil.UTF_8);
        ctx.writeAndFlush(b);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
