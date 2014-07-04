package com.tmall.buy.socket.common.message.parser;

import com.tmall.buy.socket.common.message.Message;
import com.tmall.buy.socket.common.message.RawMessage;

public interface MessageParser {
	public void check(RawMessage rawMsg) throws Exception;
	public Message parse(RawMessage rawMsg);
	public  RawMessage deParse(Message message);
	

}
