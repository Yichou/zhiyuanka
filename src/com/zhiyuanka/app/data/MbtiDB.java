package com.zhiyuanka.app.data;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yichou.common.utils.FileUtils;

public class MbtiDB extends SQLiteOpenHelper {
	private static final String DB_NAME = "ccard.db";

	private SQLiteDatabase db; // 数据库操作实例

	
	public MbtiDB(Context context) {
		super(context, DB_NAME, null, 1);

		this.db = getReadableDatabase();
	}
	
	public static void checkDB(Context context){
		File file = context.getDatabasePath(DB_NAME); //返回文件路径，databases 文件夹不会自己创建
		if(!file.exists()){ //释放数据库
			FileUtils.assetToFile(context, DB_NAME, file);
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public Cursor getExams() {
		return db.query(Tab_Exams.NAME,
				null, null, null, null, null, null);
	}
	
	public Cursor getProvinces() {
		return db.query(Tab_Province.NAME,
				null, null, null, null, null, null);
	}
}
