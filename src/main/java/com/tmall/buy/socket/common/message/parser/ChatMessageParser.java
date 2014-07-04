package com.tmall.buy.socket.common.message.parser;

import com.tmall.buy.socket.common.OpHolder;
import com.tmall.buy.socket.common.message.Message;
import com.tmall.buy.socket.common.message.RawMessage;

public class ChatMessageParser implements MessageParser {
	public static MessageParser instance = new ChatMessageParser();
	@Override
	public void check(RawMessage rawMsg)throws Exception {
		if(rawMsg == null || rawMsg.getMessageCount() != 3){
			throw new Exception("消息长度(MessageCount)不对，应该为3，实际为" + (rawMsg == null ? 0 : rawMsg.getMessageCount()));
		}
		if(rawMsg == null || rawMsg.getContents() == null || rawMsg.getContents().length != 3){
			throw new Exception("消息(Contents)长度不对，应该为3，实际为" + (rawMsg == null || rawMsg.getContents() == null ? 0 : rawMsg.getContents().length));
		}
	}

	@Override
	public Message parse(RawMessage rawMsg) {
		byte[][] contents = rawMsg.getContents();
		return new Message(new String(contents[0]),new String(contents[1]),new String(contents[2]));
	}

	@Override
	public RawMessage deParse(Message message) {
		int length = 8;
		byte[] usernames = message.getUsername().getBytes();
		byte[] friendUsernames = message.getFriendUsername().getBytes();
		
		byte[] detail = message.getDetail().getBytes();
		int[] contentSize = new int[3];
		length += 4 * contentSize.length;
		contentSize[0] = usernames.length;
		length += contentSize[0] ;

		contentSize[1] = friendUsernames.length;
		length += contentSize[1] ;
		
		contentSize[2] = detail.length;
		length += contentSize[2] ;
		
		byte[][] contents = new byte[3][];
		contents[0] = usernames;
		contents[1] = friendUsernames;
		contents[2] = detail;
		return new RawMessage(length,OpHolder.OP_CHAT,3,contentSize,contents);
	}

}
