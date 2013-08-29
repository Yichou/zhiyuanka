package com.zhiyuanka.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;
import com.zhiyuanka.app.R;
import com.zhiyuanka.app.common.Settings;
import com.zhiyuanka.app.data.Globals;
import com.zhiyuanka.app.data.Res;

public class WelcomeActivity extends SherlockActivity {
	ImageView imageView1, imageView2;
	FrameLayout layout;
	int curImg = R.drawable.img3;

	
	private void start2() {
		Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.translate);
		anim2.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				layout.removeViewAt(1);
				layout.addView(imageView2, 0);
				imageView2.setImageResource(curImg++);
				if (curImg <= R.drawable.img5)
					start1();
				else
					jump();
			}
		});

		imageView2.startAnimation(anim2);
	}

	private void start1() {
		Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.translate);
		anim1.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				layout.removeViewAt(1);
				layout.addView(imageView1, 0);
				imageView1.setImageResource(curImg++);
				if (curImg <= R.drawable.img5)
					start2();
				else {
					jump();
				}
			}
		});

		imageView1.startAnimation(anim1);
	}

	private void jump() {
		Settings.firstRun = false;
		Settings.save(this);

		startActivity(new Intent(this, HomeActivity.class));
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Res.init(this);
		Globals.init(this);
		Settings.read(this);

		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window. FEATURE_NO_TITLE); //去掉标题栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

		if (!Settings.firstRun) {
			startActivity(new Intent(this, HomeActivity.class));
			finish();
			return;
		}

		setContentView(R.layout.activity_welcome);

		layout = (FrameLayout) findViewById(R.id.root);
		imageView1 = (ImageView) findViewById(R.id.imageView1); // 上层
		imageView2 = (ImageView) findViewById(R.id.imageView2);

		start1();
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// if (event.getAction() == MotionEvent.ACTION_DOWN) {
	// startActivity(new Intent(this, HomeActivity.class));
	// finish();
	// return true;
	// } else {
	// return super.onTouchEvent(event);
	// }
	// }
}
