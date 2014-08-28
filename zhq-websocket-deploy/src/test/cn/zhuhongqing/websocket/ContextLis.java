package cn.zhuhongqing.websocket;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.zhuhongqing.WebSocketDeploy;

public class ContextLis implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		WebSocketDeploy.addWebSocketServer(WebSocketServer.class, "/wsss");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
