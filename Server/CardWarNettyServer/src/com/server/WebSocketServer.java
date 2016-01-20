package com.server;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class WebSocketServer {

	 private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
	    private final EventLoopGroup group = new NioEventLoopGroup();	   
	    private final GameManager gameManager = new GameManager(); 
	   
	    private Channel channel;

	    public ChannelFuture start(int address) {
	        ServerBootstrap bootstrap  = new ServerBootstrap();
	        bootstrap.group(group)
	                .channel(NioServerSocketChannel.class)
	                .childHandler(createInitializer(gameManager));
	        ChannelFuture future = bootstrap.bind(new InetSocketAddress(address));
	        future.syncUninterruptibly();
	        channel = future.channel();
	        return future;
	    }

	    protected ChannelInitializer<Channel> createInitializer(GameManager _gameManager) {
	       return new GameServerInitializer(gameManager);
	    }

	    public void destroy() {
	        if (channel != null) {
	            channel.close();
	        }
	        channelGroup.close();
	        group.shutdownGracefully();
	    }

}
