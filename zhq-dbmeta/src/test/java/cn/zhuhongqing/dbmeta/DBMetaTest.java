package cn.zhuhongqing.dbmeta;

import org.junit.Ignore;
import org.junit.Test;

import cn.zhuhongqing.DBMeta;

/**
 * Test
 * 
 * @author HongQing.Zhu
 *
 */
public class DBMetaTest {

	public static DBMetaInfo createDBDbMetaInfo(String prop) {
		return DBMeta.createDBMetaInfo(prop);
	}

	@Test
	@Ignore
	public void showDefTables() {
		DBMetaInfo metaInfo = createDBDbMetaInfo("oracle.properties");
		metaInfo.getTables().forEach(t -> {
			System.out.println(t);
			t.getColumns().forEach(c -> System.out.println(c));
		});
	}

	@Test
	@Ignore
	public void showAppointTable() {
		DBMetaInfo metaInfo = createDBDbMetaInfo("postgresql.properties");
		metaInfo.getTables().forEach(t -> {
			System.out.println(t);
			t.getColumns().forEach(c -> System.out.println(c));
		});
	}

}
