package com.zhiyuanka.app.data;

import android.database.Cursor;

/**
 * 试题表
 * 
 * @author Yichou 2013-6-19
 *
 */
public final class Tab_Exams {
	public static final String NAME = "exams";

	public static final String F_TYPT = "type";
	public static final String F_TITLE = "title";
	public static final String F_ANS_A = "ans_a";
	public static final String F_ANS_B = "ans_b";
	public static final String F_TYPE_A = "type_a";
	public static final String F_TYPE_B = "type_b";

	public static final String F_IMGURL = "imgurl";

	final static String[] COLS = { F_TYPT, F_TITLE, F_ANS_A, F_ANS_B, F_TYPE_A, F_TYPE_B, F_IMGURL };
	
	private static int ci_title = -1;
	public static String getTitle(Cursor cursor) {
		if(ci_title == -1){
			ci_title = cursor.getColumnIndex(F_TITLE);
		}
		if(ci_title >= 0){
			return cursor.getString(ci_title);
		}
		
		return null;
	}

	private static int ci_ansA = -1;
	public static String getAnsA(Cursor cursor) {
		if(ci_ansA == -1){
			ci_ansA = cursor.getColumnIndex(F_ANS_A);
		}
		if(ci_ansA >= 0){
			return cursor.getString(ci_ansA);
		}
		
		return null;
	}
	
	private static int ci_ansB = -1;
	public static String getAnsB(Cursor cursor) {
		if(ci_ansB == -1){
			ci_ansB = cursor.getColumnIndex(F_ANS_B);
		}
		if(ci_ansB >= 0){
			return cursor.getString(ci_ansB);
		}
		
		return null;
	}

	private static int ci_typeA = -1;
	public static String getTypeA(Cursor cursor) {
		if(ci_typeA == -1){
			ci_typeA = cursor.getColumnIndex(F_TYPE_A);
		}
		if(ci_typeA >= 0){
			return cursor.getString(ci_typeA);
		}
		
		return null;
	}

	private static int ci_typeB = -1;
	public static String getTypeB(Cursor cursor) {
		if(ci_typeB == -1){
			ci_typeB = cursor.getColumnIndex(F_TYPE_B);
		}
		if(ci_typeB >= 0){
			return cursor.getString(ci_typeB);
		}
		
		return null;
	}
}
