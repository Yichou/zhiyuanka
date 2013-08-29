package com.zhiyuanka.app.common;

import java.io.InputStream;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 通用工具类
 * 
 * @author Yichou
 *
 */
public final class Utils {

	public static String stream2string(InputStream is) throws Exception {
		byte[] buf = new byte[is.available()];
		is.read(buf);
		return new String(buf);
	}
	
	public static String getPhoneNumber(Context context) {
		TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
		return mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
	}
}
