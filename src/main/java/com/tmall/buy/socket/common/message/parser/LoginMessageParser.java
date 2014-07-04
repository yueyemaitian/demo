package com.tmall.buy.socket.common.message.parser;

import com.tmall.buy.socket.common.OpHolder;
import com.tmall.buy.socket.common.message.Message;
import com.tmall.buy.socket.common.message.RawMessage;

public class LoginMessageParser implements MessageParser {
	
	public static MessageParser instance = new LoginMessageParser();

	@Override
	public Message parse(RawMessage rawMsg) {
		return new Message(new String(rawMsg.getContents()[0]),null);
	}

	@Override
	public void check(RawMessage rawMsg)throws Exception {
		if(rawMsg == null || rawMsg.getMessageCount() != 1){
			throw new Exception("消息长度(MessageCount)不对，应该为1，实际为" + (rawMsg == null ? 0 : rawMsg.getMessageCount()));
		}
		if(rawMsg == null || rawMsg.getContents() == null || rawMsg.getContents().length != 1){
			throw new Exception("消息(Contents)长度不对，应该为1，实际为" + (rawMsg == null || rawMsg.getContents() == null ? 0 : rawMsg.getContents().length));
		}
	}

	@Override
	public RawMessage deParse(Message message) {
		int length = 8;
		byte[] usernames = message.getUsername().getBytes();
		int[] contentSize = new int[1];
		contentSize[0] = usernames.length;
		length += 4 * contentSize.length;
		length += contentSize[0] ;
		byte[][] contents = new byte[1][];
		contents[0] = usernames;
		return new RawMessage(length,OpHolder.OP_LOGIN,1,contentSize,contents);
	}

}
