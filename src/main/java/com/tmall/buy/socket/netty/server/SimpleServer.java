package com.tmall.buy.socket.netty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class SimpleServer {

	public static void main(String[] args) {
		ServerBootstrap sbs = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newFixedThreadPool(2),Executors.newFixedThreadPool(4)));
		sbs.setPipelineFactory(new ChannelPipelineFactory(){

			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new SimpleChannelHandler(){

					@Override
					public void messageReceived(ChannelHandlerContext ctx,
							MessageEvent e) throws Exception {
						System.out.println("messageReceived:" + ((ChannelBuffer)e.getMessage()).toByteBuffer());
					}

					@Override
					public void connectRequested(ChannelHandlerContext ctx,
							ChannelStateEvent e) throws Exception {
						System.out.println("connectRequested");
					}
					
				});
			}
			
		});
		
		sbs.setOption("child.tcpNoDelay", true);
		sbs.bind(new InetSocketAddress(8080));
	}

}
