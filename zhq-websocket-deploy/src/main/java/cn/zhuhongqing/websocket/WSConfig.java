package cn.zhuhongqing.websocket;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Builder;

/**
 * Defined WebScoket Config.
 * 
 * @see ServerEndpoint
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public final class WSConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private Class<?> endPointClass;

	private String path;

	private List<String> subprotocols = Collections.emptyList();

	private List<Class<? extends Decoder>> decoders = Collections.emptyList();

	private List<Class<? extends Encoder>> encoders = Collections.emptyList();

	public WSConfig() {
	}

	public WSConfig(Class<?> endPointClass, String path) {
		this.endPointClass = endPointClass;
		this.path = path;
	}

	public Class<?> getEndPointClass() {
		return endPointClass;
	}

	public void setEndPointClass(Class<?> endPointClass) {
		this.endPointClass = endPointClass;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getSubprotocols() {
		return subprotocols;
	}

	public void setSubprotocols(List<String> subprotocols) {
		this.subprotocols = subprotocols;
	}

	public List<Class<? extends Decoder>> getDecoders() {
		return decoders;
	}

	public void setDecoders(List<Class<? extends Decoder>> decoders) {
		this.decoders = decoders;
	}

	public List<Class<? extends Encoder>> getEncoders() {
		return encoders;
	}

	public void setEncoders(List<Class<? extends Encoder>> encoders) {
		this.encoders = encoders;
	}

	public ServerEndpointConfig build() {
		return Builder.create(endPointClass, path).subprotocols(subprotocols)
				.encoders(encoders).decoders(decoders).build();
	}

}
