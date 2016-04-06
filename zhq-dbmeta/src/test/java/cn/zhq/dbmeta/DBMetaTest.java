package cn.zhq.dbmeta;

import cn.zhq.dbmeta.struct.Table;

/**
 * Test
 * 
 * @author HongQing.Zhu
 *
 */
public class DBMetaTest {

	public static DBMetaInfo createDBDbMetaInfo() {
		return DBMeta.createDBMetaInfo("postgresql.properties");
		// return DBMeta.createDBMetaInfo("oracle.properties");
	}

	// @Test
	public void showDefTables() {
		DBMetaInfo metaInfo = createDBDbMetaInfo();
		metaInfo.getTables().forEach(t -> {
			System.out.println(t);
			t.getColumns().forEach(c -> System.out.println(c));
		});
	}

	// @Test
	public void showAppointTable() {
		DBMetaInfo metaInfo = createDBDbMetaInfo();
		Table t = metaInfo.getTable("stock_day_deal");
		System.out.println(t);
		t.getColumns().forEach(c -> System.out.println(c));
	}

}
