package com.example.webSocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/websocket/{id}")
@Component
public class MyWebSocket {
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount=0;
	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	private static ConcurrentHashMap<String, MyWebSocket> webSocketSet = new ConcurrentHashMap<>();
	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

    private String id = "";

    private static Logger log = LogManager.getLogger(MyWebSocket.class);
    /**
     * 连接建立成功调用的方法
     */
	@OnOpen
	public void onOpen(@PathParam(value="id") String id,Session session){
		this.session = session;
		this.id=id;					//接收到发送消息的人员编号
		webSocketSet.put(id,this);  //加入set中
		addOnlineCount();    	//在线数加1
		log.info("用户"+id+"加入！当前在线人数为" + getOnlineCount());
		try {
			sendMessage("连接成功");
		} catch (IOException e) {
			log.error("websocket IO异常");
		}
	}
	
	/**
	 * 连接关闭
	 * @return
	 */
	public void onClose(){
		webSocketSet.remove(this);
		subOnlineCount();
		log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
	}
	/**
	 * 
	 * @param session
	 * @param error
	 */
	public void onError(Session session,Throwable error){
		log.error("发生错误");
		error.printStackTrace();
	}
	/**
	 * 收到客户端消息后调用的方法
	 * @return
	 */
	@OnMessage
	public void onMessage(String message,Session session ){

        log.info("来自客户端的消息:" + message);
      //可以自己约定字符串内容，比如 内容|0 表示信息群发，内容|X 表示信息发给id为X的用户
        String sendMessage = message.split("[|]")[0];
        String sendUserId = message.split("[|]")[1];
        try {
            if(sendUserId.equals("0"))
                sendtoAll(sendMessage);
            else{
                sendtoUser(sendMessage,sendUserId);
            }
        }catch(IOException e){
        	e.printStackTrace();
        }
	}
	public static synchronized int getOnlineCount() {
        return onlineCount;
    }
 
    public static synchronized void addOnlineCount() {
    	MyWebSocket.onlineCount++;
    }
 
    public static synchronized void subOnlineCount() {
    	MyWebSocket.onlineCount--;
    }
	
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
    /**
     * 发送信息给指定ID用户，如果用户不在线则返回不在线信息给自己
     * @throws IOException 
     */
    public void sendtoUser(String message,String sendUserId) throws IOException{
    	if(null !=webSocketSet.get(sendUserId)){
    		if(!id.equals(sendUserId)){
    			webSocketSet.get(sendUserId).sendMessage( "用户" + id + "发来消息：" + " <br/> " + message);
    		}else{
                webSocketSet.get(sendUserId).sendMessage(message);
    		}
    	}else{

            //如果用户不在线则返回不在线信息给自己
            sendtoUser("当前用户不在线",id);
    	}
    
    }
    /**
     * 发送消息给所有人
     */
    public void sendtoAll(String message){
    	for(String key:webSocketSet.keySet()){
    		try {
				webSocketSet.get(key).sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}