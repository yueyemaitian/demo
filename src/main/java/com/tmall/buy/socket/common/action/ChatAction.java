package com.tmall.buy.socket.common.action;

import java.net.Socket;

import com.tmall.buy.socket.bio.server.ServerWorker;
import com.tmall.buy.socket.common.message.Message;
import com.tmall.buy.socket.common.message.RawMessage;
import com.tmall.buy.socket.common.message.parser.ChatMessageParser;
import com.tmall.buy.socket.common.message.parser.MessageParser;

public class ChatAction implements Action {

	MessageParser parser = ChatMessageParser.instance;
	@Override
	public void doAction(RawMessage rawMessage, Socket socket) throws Exception {
		parser.check(rawMessage);
		Message message = parser.parse(rawMessage);
		Socket targetSocket = ServerWorker.cachedSocket.get(message.getUsername());
		if(targetSocket == null){
			throw new Exception("目标用户不存在：" + message.getUsername());
		}
		System.out.println(message.getUsername() + " say to " + message.getFriendUsername() + ": " + message.getDetail());
		targetSocket.getOutputStream().write(message.getDetail().getBytes());
	}

}
