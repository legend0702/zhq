package cn.zhuhongqing.websocket;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.server.ServerContainer;

import cn.zhuhongqing.WebSocketDeploy;

/**
 * Use to init {@link WebSocketDeploy#SERVER_CONTAINER}.
 * 
 * @see ServerContainer
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class WebSocketDeployContainerInitializer extends Endpoint implements
		ServletContainerInitializer {

	private ServletContext CONTEXT;

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx)
			throws ServletException {
		CONTEXT = ctx;
		initWebSocektServerContainer();
	}

	/**
	 * @see ServerContainer
	 */

	private void initWebSocektServerContainer() {
		WebSocketDeploy.SERVER_CONTAINER = (ServerContainer) CONTEXT
				.getAttribute(javax.websocket.server.ServerContainer.class
						.getName());
		if (WebSocketDeploy.SERVER_CONTAINER == null) {
			throw new RuntimeException(
					"Init WebSocketContainer fail.May be this application server is not support WebSocket or not obey the rules.");
		}
	}

	/**
	 * This method can not be called.
	 */
	@Deprecated
	@Override
	public void onOpen(Session session, EndpointConfig config) {
		try {
			session.close();
		} catch (IOException e) {
			// ignore
		}
	}

}
