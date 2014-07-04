package com.tmall.buy.socket.common.action;

import java.net.Socket;

import com.tmall.buy.socket.common.message.RawMessage;

public interface Action {
	public void doAction(RawMessage rawMessage,Socket socket) throws Exception;
}