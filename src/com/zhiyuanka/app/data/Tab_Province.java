package com.zhiyuanka.app.data;

import android.database.Cursor;

/**
 * 省份表
 * 
 * @author Yichou 2013-6-19
 * 
 */
public final class Tab_Province {
	public static final String NAME = "province";

	public static final String F_ID = "provinceID";
	public static final String F_NAME = "province";

	private static int ci_id = -1;

	public static String getId(Cursor cursor) {
		if (ci_id == -1) {
			ci_id = cursor.getColumnIndex(F_ID);
		}
		if (ci_id >= 0) {
			return cursor.getString(ci_id);
		}

		return null;
	}

	private static int ci_name = -1;

	public static String getName(Cursor cursor) {
		if (ci_name == -1) {
			ci_name = cursor.getColumnIndex(F_NAME);
		}
		if (ci_name >= 0) {
			return cursor.getString(ci_name);
		}

		return null;
	}
}
