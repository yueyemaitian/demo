package com.tmall.buy.socket.common.message;



public class RawMessage {
	private int op;
	private int length;
	private int messageCount;
	private int[] contentSize;
	private byte[][] contents;
	public int getLength() {
		return length;
	}
	public int getMessageCount() {
		return messageCount;
	}
	public byte[][] getContents() {
		return contents;
	}
	public int[] getContentSize() {
		return contentSize;
	}
	
	public int getOp(){
		return op;
	}
	
	public RawMessage(int length,int op,int count,int[] contentSize,byte[][] contents){
		this.op = op;
		this.length = length;
		this.contents = contents;
		this.contentSize = contentSize;
		this.messageCount = count;
	}

}
