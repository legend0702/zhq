package cn.zhuhongqing;

import java.util.Collections;
import java.util.List;

import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;
import javax.xml.ws.WebServiceException;

import cn.zhuhongqing.module.Module;
import cn.zhuhongqing.websocket.WSConfig;

/**
 * <h3>Use to dynamic deploy WebSocketServer.</h3>
 * 
 * 
 * <pre>
 * <h3>Think about it:</h3>
 * WebSocket use {@link ServerEndpoint} to deploy a WebSocketServer.
 * I have a WebSocketServer just have to mapping URI.
 * Also a common WebSocketServer just need a URI.
 * OK,How can I do?Modify source code?
 * Dynamic deployment is a right way to do it.
 * </pre>
 * 
 * <h3>What should we do?</h3>
 * 
 * <pre>
 * Before all,you must have a Web Application Container which support WebSocket.
 * Open your app's web.xml add:
 * &lt;listener>
 * 	&lt;listener-class>cn.zhuhongqing.websocket.WebSocketContainerListener&lt;/listener-class>
 * &lt;/listener>
 * Use {@link #addWebSocketServer(WSConfig)} or {@link #addWebSocketServer(Class, String)} to add WebSocketServer. :)
 * </pre>
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class WebSocketDeploy extends Module {

	public static ServerContainer SERVER_CONTAINER;

	public static List<WSConfig> wsConfig = Collections.emptyList();

	public static ServerContainer getServerContainer() {
		return SERVER_CONTAINER;
	}

	public static void setWsConfig(List<WSConfig> wsConfig) {
		WebSocketDeploy.wsConfig = wsConfig;
	}

	/**
	 * @see ServerContainer#addEndpoint(javax.websocket.server.ServerEndpointConfig)
	 */

	public static void addWebSocketServer(WSConfig config) {
		try {
			SERVER_CONTAINER.addEndpoint(config.build());
		} catch (DeploymentException e) {
			throw new WebServiceException(e);
		}
	}

	public static void addWebSocketServer(
			Class<? extends Endpoint> endPointClass, String path) {
		addWebSocketServer(new WSConfig(endPointClass, path));
	}

	@Override
	protected void init() throws Exception {
		for (WSConfig config : wsConfig) {
			addWebSocketServer(config);
		}
	}

}