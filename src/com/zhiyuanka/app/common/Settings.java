package com.zhiyuanka.app.common;

import com.zhiyuanka.app.data.Globals;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 
 * 管理设置
 * 
 * @author Yichou 2013-6-29
 *
 */
public final class Settings {
	private static final String COMMON_SAVE_FILE_NAME = "common";
	private static final String TESTRET_SAVE_FILE_NAME = "testret";

	public static boolean firstRun;
	public static boolean hasSucTest;
	
	public static void read(Context context) {
		SharedPreferences sp = context.getSharedPreferences(COMMON_SAVE_FILE_NAME, Context.MODE_PRIVATE);
		
		firstRun = sp.getBoolean("firstRun", true);
		hasSucTest = sp.getBoolean("hasSucTest", false);
	}
	
	public static void save(Context context) {
		SharedPreferences sp = context.getSharedPreferences(COMMON_SAVE_FILE_NAME, Context.MODE_PRIVATE);
		Editor e = sp.edit();
		
		e.putBoolean("firstRun", firstRun);
		e.putBoolean("hasSucTest", hasSucTest);
		
		e.commit();
	}
	
	public static void readTestRet(Context context) {
		SharedPreferences sp = context.getSharedPreferences(TESTRET_SAVE_FILE_NAME, Context.MODE_PRIVATE);
		
		Globals.mbti.set(sp.getInt("p", 0), 
				sp.getInt("e", 0),
				sp.getInt("s", 0),
				sp.getInt("f", 0));
		
		Globals.wenli = sp.getString("wenli", "理科");
		Globals.pici = sp.getString("pici", "第一批");
		Globals.province = sp.getString("province", "浙江省");
		Globals.uid = sp.getInt("uid", 0);
	}
	
	public static void saveTestRet(Context context) {
		SharedPreferences sp = context.getSharedPreferences(TESTRET_SAVE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		
		editor.putInt("p", Globals.mbti.getP());
		editor.putInt("e", Globals.mbti.getE());
		editor.putInt("s", Globals.mbti.getS());
		editor.putInt("f", Globals.mbti.getF());
		editor.putString("wenli", Globals.wenli);
		editor.putString("pici", Globals.pici);
		editor.putString("province", Globals.province);
		editor.putInt("uid", Globals.uid);
		
		editor.commit();
	}
	
	
}
