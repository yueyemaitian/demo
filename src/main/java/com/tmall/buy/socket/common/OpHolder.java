package com.tmall.buy.socket.common;

import java.util.HashMap;
import java.util.Map;

import com.tmall.buy.socket.common.action.Action;
import com.tmall.buy.socket.common.action.ChatAction;
import com.tmall.buy.socket.common.action.LoginAction;

public class OpHolder {
	public static int OP_LOGIN = 1;
	public static int OP_CHAT = 2;
	private static Map<Integer,Action> opCode2ActionMap = new HashMap<Integer,Action>();
	static{
		opCode2ActionMap.put(OP_LOGIN, new LoginAction());
		opCode2ActionMap.put(OP_CHAT, new ChatAction());
	}
	
	public static Action getActionByOp(Integer op){
		return opCode2ActionMap.get(op);
	}

}
