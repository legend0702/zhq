package cn.zhuhongqing.dbmeta;

import org.junit.Test;

import cn.zhuhongqing.DBMeta;
import cn.zhuhongqing.dbmeta.struct.Table;

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
	public void showDefTables() {
		DBMetaInfo metaInfo = createDBDbMetaInfo("oracle.properties");
		metaInfo.getTables().forEach(t -> {
			System.out.println(t);
			t.getColumns().forEach(c -> System.out.println(c));
		});
	}

	@Test
	public void showAppointTable() {
		DBMetaInfo metaInfo = createDBDbMetaInfo("postgresql.properties");
		Table t = metaInfo.getTable("stock_day_deal");
		System.out.println(t);
		t.getColumns().forEach(c -> System.out.println(c));
	}

}
