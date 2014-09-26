package com.tmall.buy.socket.netty.client;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;

public class ReconnectClientTest {

	public static void main(String[] args) {
		final ClientBootstrap cbs = new ClientBootstrap(
				new NioClientSocketChannelFactory());
		cbs.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipelines = Channels.pipeline();
				pipelines.addLast("handler", new ChannelHanlder());
				pipelines.addLast("heartbeat", new HeartBeatHandler());
				return pipelines;
			}
		});
		ChannelFuture future = cbs.connect(new InetSocketAddress("127.0.0.1",
				8080));
		future.addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture future)
					throws Exception {
				System.err.println("future.isSuccess():" + future.isSuccess());
				ChannelBuffer buf = ChannelBuffers.wrappedBuffer("Hello World"
						.getBytes());
				future.getChannel().write(buf).addListener(CLOSE);
			}

		});
	}

	static class HeartBeatHandler extends IdleStateHandler {

		public HeartBeatHandler() {
			super(new HashedWheelTimer(), 0, 10, 0);
		}

		@Override
		protected void channelIdle(ChannelHandlerContext ctx, IdleState state,
				long lastActivityTimeMillis) throws Exception {
			System.out.println(state.name());
			if(state == IdleState.WRITER_IDLE && ctx.getChannel().isWritable()){
				System.out.println("HeartBeating...");
				ctx.getChannel().write(ChannelBuffers.wrappedBuffer("HeartBeat".getBytes()));
			}
		}

	}

	static class ChannelHanlder extends SimpleChannelHandler {

		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {
			System.out.println("messageReceived");
		}

		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			System.out.println("channelConnected");
		}

		@Override
		public void channelDisconnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			System.out.println("channelDisconnected");
		}

		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
				throws Exception {
			System.out.println("channelClosed");
		}

		@Override
		public void closeRequested(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			System.out.println("closeRequested");
		}

	}

}
