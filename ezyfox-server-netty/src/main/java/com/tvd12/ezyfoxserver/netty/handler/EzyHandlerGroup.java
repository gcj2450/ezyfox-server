package com.tvd12.ezyfoxserver.netty.handler;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.netty.entity.EzyNettySession;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzySocketWriterGroup;

public interface EzyHandlerGroup extends
		EzySocketDataHandlerGroup,
		EzySocketWriterGroup, 
		EzyDestroyable {

	void fireBytesSent(int bytes);
	
	void fireBytesReceived(int bytes);
	
	void fireChannelInactive();
	
	void fireChannelInactive(EzyConstant reason);

	void fireExceptionCaught(Throwable throwable);
	
	EzyNettySession fireChannelActive() throws Exception;
	
	void fireChannelRead(EzyArray msg) throws Exception;
	
}
