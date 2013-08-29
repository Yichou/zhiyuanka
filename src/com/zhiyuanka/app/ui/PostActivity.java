package com.zhiyuanka.app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.Toast;

/**
 * 封装了发送数据的 Activity
 * 
 * @author Yichou 2013-6-22
 *
 */
public abstract class PostActivity extends BaseActivity implements Callback {
	static final int MSG_POST_SUC = 1;
	static final int MSG_POST_FAIL = -1;
	protected static String LOADING_MSG = "正在提交数据...";

	protected Handler mHandler;
	protected ProgressDialog mProgressDialog;
	
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case 1:
			onPostSuc();
			break;
			
		case -1:
			onPostFail();
			break;

		default:
			return false;
		}
		
		mProgressDialog.dismiss();

		return true;
	}
	
	protected void post() {
		if(mProgressDialog.isShowing()) //正在加载
			return;
		
		mProgressDialog.show();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					onPost();
				} catch (Exception e) {
					e.printStackTrace();
					
					mHandler.sendEmptyMessage(MSG_POST_FAIL);
				}
			}
		}).start();
	}
	
	protected void notifyPostFail() {
		mHandler.sendEmptyMessage(MSG_POST_FAIL);
	}
	
	protected void notifyPostSuc() {
		mHandler.sendEmptyMessage(MSG_POST_SUC);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mHandler = new Handler(this);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage(LOADING_MSG);
	}

	/**
	 * 已转到UI线程
	 */
	protected void onPostFail() {
		Toast.makeText(this, "数据发送失败！", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 在独立线程运行
	 * 
	 * @throws Exception
	 */
	protected abstract void onPost() throws Exception;
	
	/**
	 * 已转到UI线程
	 */
	protected abstract void onPostSuc();
}
