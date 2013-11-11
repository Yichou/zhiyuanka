package com.zhiyuanka.app.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.yichou.common.utils.LocalDataUtils;
import com.yichou.common.utils.UrlBitmapLoader;
import com.yichou.common.utils.UrlBitmapLoader.IBitmapHolder;
import com.zhiyuanka.app.data.Res;

/**
 * 学校实体类
 * 
 * @author Yichou 2013-6-22
 * 
 */
public final class School implements IBitmapHolder {
	public Bitmap iconBitmap;
	
	public String id;
	public String name;
	public String province;
	public String iconUrl;
	public String type;
	public String property1;
	public String property2;
	public String url;
	
	/*
	"10": {
		"school_name": "\u5317\u4eac\u5927\u5b66", 
		"school_icon": "http://gkcx.eol.cn/upload/200904291511410_thumb.jpg", 
		"school_url": "http://www.pku.edu.cn", 
		"school_id": "31"
		"point_low": "--", 
		"point_height": "657", 
		"point_average": "657", 
		"school_prov": "\u5317\u4eac", 
		"school_type": "\u666e\u901a\u672c\u79d1", 
		"school_property1": "985\u5de5\u7a0b", 
		"school_property2": "211\u5de5\u7a0b", 
		"point_id": "2346", 
		"from_prov": "\u5e7f\u4e1c", 
		"specialty_category": "\u6587\u79d1\u8bd5\u9a8c\u73ed\u7c7b", 
		"year": "2012", 
		"level": "\u7b2c\u4e00\u6279", 
		"stu_type": "\u6587\u79d1", 
	}, 
	*/
	
	public ArrayList<Specialty> specialtyList;
	
	public School() {
		specialtyList = new ArrayList<Specialty>();
	}
	
	Context mContext;
	
	public void getIcon(Context context, ImageView imageView) {
		if(iconBitmap != null){
			imageView.setImageBitmap(iconBitmap);
			return;
		}
		
//		System.out.println("icon url=" + iconUrl);
		
		if(iconUrl == null || !iconUrl.startsWith("http:")){
			iconBitmap = Res.defSchoolBitmap;
			imageView.setImageBitmap(iconBitmap);
			return;
		}
		
		iconBitmap = LocalDataUtils.getBitmap(context, iconUrl);

		if (iconBitmap == null){
			mContext = context;
			imageView.setImageBitmap(Res.defSchoolBitmap); //先给一个图标占着
			UrlBitmapLoader.loadBitmap(context, imageView, iconUrl, this);
		} else {
			iconBitmap = UrlBitmapLoader.fitDpi(context.getResources(), iconBitmap);
			imageView.setImageBitmap(iconBitmap);
		}
	}

	@Override
	public void setBitmap(Bitmap bitmap) {
		this.iconBitmap = bitmap;
		
		//图片本地化
		LocalDataUtils.toLocal(mContext, iconUrl, iconBitmap);
	}
	
	private String optProperty(String src) {
		if(src == null || src.length() < 3)
			return " ";
		
		return src.substring(0, 3) + " ";
	}
	
	/**
	 * 解析基本信息
	 */
	public void parseInfo(JSONObject obj) throws JSONException {
		this.id = obj.getString("school_id");
		this.name = obj.getString("school_name");
		this.province = obj.getString("school_prov");
		this.iconUrl = obj.getString("school_icon");
		this.type = obj.getString("school_type");
		this.property1 = optProperty(obj.getString("school_property1"));
		this.property2 = optProperty(obj.getString("school_property2"));
		this.url = obj.getString("school_url");
	}
	
	/**
	 * 解析专业列表
	 */
	public void parseSpecialties(JSONObject obj) throws JSONException {
		if(specialtyList.size() > 0)
			return;
		
		//解析专业列表
		JSONArray array2 = obj.getJSONArray("specialties");
		for (int j = 0; j < array2.length(); j++) {
			JSONObject obj2 = array2.getJSONObject(j);
			Specialty specialty = new Specialty();

			specialty.id = obj2.getString("id");
//			specialty.specialty_category = obj2.getString("specialty_category");
			specialty.from_prov = obj2.getString("from_prov");
			specialty.level = obj2.getString("level");
			specialty.point_average = obj2.getString("point_average");
			specialty.point_height = obj2.getString("point_height");
			specialty.point_low = obj2.getString("point_low");
			specialty.year = obj2.getString("year");
			specialty.stu_type = obj2.getString("stu_type");
			specialty.name = obj2.optString("spe_name");

			this.specialtyList.add(specialty);
		}
	}
	
	public void recyle() {
		if(iconBitmap != null){
			iconBitmap.recycle();
			iconBitmap = null;
		}
	}
}
