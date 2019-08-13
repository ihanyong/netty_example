package com.hanyong.netty.examples.im.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * ImServer
 *
 * @author yong.han
 * 2018/12/11
 */
public class ImServer {
    public static void main(String[] args) {

        EventLoopGroup bossWork = new NioEventLoopGroup();

        ServerBootstrap bs = new ServerBootstrap();
//        bs.g



    }
}
