package com.zhiyuanka.app.data;

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

import com.yichou.common.CrashHandler;
import com.zhiyuanka.app.bean.Mbti;

public final class Globals {
	public static final String PATH = "http://zhiyuanka.com";
	
	public static MbtiDB dbUtil;
	public static Mbti mbti;
	public static int uid;
	public static int rid;
	/**
	 * 生源地
	 */
	public static String province;
	/**
	 * 文/理科
	 */
	public static String wenli;
	/**
	 * 批次
	 */
	public static String pici;
	
	public static Cursor peovinceCursor;
	
	
	public static String retJson;
	public static JSONObject mbtiJsonObj;
	public static Context applicationContext;
	private static boolean isInit = false;
	
	
	public static void init(Context context) {
		if(isInit)
			return;
		
		dbUtil = new MbtiDB(context);
		mbti = new Mbti(40, 33, 20, 50);
		applicationContext = context.getApplicationContext();
		CrashHandler.init(context);
		peovinceCursor = Globals.dbUtil.getProvinces();
		isInit = true;
	}
	
	public static void recyle() {
		if(!isInit)
			return;
		
		isInit = false;
		peovinceCursor.close();
		peovinceCursor = null;
		dbUtil.close();
		dbUtil = null;
	}
}
