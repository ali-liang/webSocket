package com.example.webSocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
@Controller
public class ResultHandler extends TextWebSocketHandler{
	private static final Logger logger = LoggerFactory.getLogger(ResultHandler.class);
	
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
	}
	
	public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
		
	}
	
	public void afterConnectionClosed(WebSocketSession webSocketsession, CloseStatus status) throws Exception {
		
	}
}											