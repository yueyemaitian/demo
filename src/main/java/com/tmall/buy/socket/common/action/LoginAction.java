package com.tmall.buy.socket.common.action;

import java.net.Socket;

import com.tmall.buy.socket.bio.server.ServerWorker;
import com.tmall.buy.socket.common.message.Message;
import com.tmall.buy.socket.common.message.RawMessage;
import com.tmall.buy.socket.common.message.parser.LoginMessageParser;

public class LoginAction implements Action {

	LoginMessageParser parser = new LoginMessageParser();
	@Override
	public void doAction(RawMessage rawMessage,Socket socket) throws Exception {
		parser.check(rawMessage);
		Message message = parser.parse(rawMessage);
		Socket oldSocket;
		while ((oldSocket = ServerWorker.cachedSocket.putIfAbsent(message.getUsername(), socket)) != null) {
			if(oldSocket.isClosed()){
				ServerWorker.cachedSocket.remove(message.getUsername(), oldSocket);
				continue;
			}
			throw new Exception("登陆失败，已经有同名用户登陆");
		}
		System.out.println(message.getUsername() + " has logined!");
		
	}

}
