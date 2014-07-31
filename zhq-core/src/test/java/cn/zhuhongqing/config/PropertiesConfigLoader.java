package cn.zhuhongqing.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import cn.zhuhongqing.exception.UtilsException;
import cn.zhuhongqing.module.Module;

/**
 * Properties configLoader.
 * 
 * @author HongQing.Zhu
 * 
 */

public class PropertiesConfigLoader implements ConfigLoader {

	@Override
	public Config load(String path) {
		Prop prop = new Prop();
		prop.load(ClassLoader.getSystemResourceAsStream(path));
		return prop;
	}

	class Prop extends AbstractConfig {

		Properties prop = new Properties();

		void load(InputStream inputStream) {
			try {
				prop.load(inputStream);
			} catch (IOException e) {
				throw new UtilsException(e);
			}
		}

		public String get(String name) {
			return prop.getProperty(name);
		}

		@Override
		void addConfig(Config config) {
			Properties p = ((Prop) config).prop;
			Iterator<String> pItr = p.stringPropertyNames().iterator();
			while (pItr.hasNext()) {
				String key = pItr.next();
				prop.put(key, p.get(key));
			}
		}

		@Override
		public <T extends Module> T toModule(Class<T> clazz) {
			return null;
		}
	}

}
