package cn.zhuhongqing.websocket;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.SendHandler;
import javax.websocket.SendResult;
import javax.websocket.Session;

public class WebSocketServer extends Endpoint {

	public void onClose(Session session, CloseReason closeReason) {
		System.out.println("Connection closed: " + session.getId()
				+ " CloseReason:" + closeReason.getReasonPhrase());
	}

	public void onError(Session session, Throwable throwable) {
		System.out.println("Connection error: " + session.getId());
		throwable.printStackTrace();
	}

	public void onOpen(final Session session, EndpointConfig endpointConfig) {
		session.getAsyncRemote().sendText(
				"Client Success!Your id is: " + session.getId());
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String message) {
				session.getAsyncRemote().sendObject(message, new SendHandler() {
					@Override
					public void onResult(SendResult result) {
						System.out.println(session.getId() + ":"
								+ result.isOK());
					}
				});
			}
		});
	}
}
