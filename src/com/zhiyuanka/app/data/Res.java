package com.zhiyuanka.app.data;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.yichou.common.utils.LocalDataUtils;
import com.yichou.common.utils.UrlBitmapLoader;
import com.zhiyuanka.app.R;

public final class Res {
	public static Resources res;
	public static Drawable actbarBgDrawable;
	public static BitmapDrawable selunderDrawable;
	public static Bitmap lineBitmap;
	public static Bitmap defSchoolBitmap;
	private static boolean isInit = false;

	static {
		UrlBitmapLoader.setIconSize(75); // 设置图标大小
		LocalDataUtils.ICON_STORE_PATH += "/schoolicon"; // 设置图标路径
	}

	public static void init(Context context) {
		if (isInit)
			return;

		res = context.getResources();

		actbarBgDrawable = new ColorDrawable(res.getColor(R.color.actionbar_bg));
		lineBitmap = BitmapFactory.decodeResource(res, R.drawable.line);
		defSchoolBitmap = BitmapFactory.decodeResource(res,
				R.drawable.ic_school_default);
		selunderDrawable = new BitmapDrawable(res, lineBitmap);
		selunderDrawable.setTileModeX(TileMode.REPEAT);
		selunderDrawable.setAlpha(0xa0);

		selunderDrawable.setBounds(0, 0, getPix(60), getPix(3));

		MbtiDB.checkDB(context);

		isInit = true;
	}

	public static void recycle() {
		if (!isInit)
			return;

		isInit = false;
		lineBitmap.recycle();
		lineBitmap = null;
		defSchoolBitmap.recycle();
		defSchoolBitmap = null;
	}

	public static int getPix(int dp) {
		float scale = res.getDisplayMetrics().density;
		return (int) (scale * dp + 0.5);
	}
}
