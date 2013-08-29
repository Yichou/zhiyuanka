package com.zhiyuanka.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.zhiyuanka.app.R;
import com.zhiyuanka.app.bean.Mbti;
import com.zhiyuanka.app.data.Globals;

public class RadarView extends View {

	public RadarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RadarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RadarView(Context context) {
		super(context);
		init();
	}
	
	Paint paint, paint2;
	DisplayMetrics metrics;
	
	private void init() {
		metrics = getResources().getDisplayMetrics();

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(1);
		paint.setTextSize(getPix(R.dimen.text_size));
//		paint.setTextSize(getResources().getDimension(R.dimen.text_size));

		paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint2.setStyle(Style.FILL);
		paint2.setStrokeWidth(2*metrics.scaledDensity);
		paint2.setShadowLayer(3, 3.0f, 3.0f, 0x80000000);
	}
	
	private int getPix(int res) {
		return getResources().getDimensionPixelSize(res);
	}
	
	static final float R2D = (float) (Math.PI/180.0f);

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		float midX = getWidth()/2;
//		float midY = getHeight()/2;
		r = midX - getPix(R.dimen.padding_left);
		
		rcos45 = (float) (r * Math.cos(45*R2D));
		rsin45 =  (float) (r *Math.sin(45*R2D));
		
		points = new float[][] {
				{-rcos45, -rcos45}, {0, -r}, {rcos45, -rcos45}, {r, 0},
				{rcos45, rcos45}, {0, r},
				{-rcos45, rcos45}, {-r, 0}
			};

		setMbti(Globals.mbti);
	}

	float r;
	float rcos45, rsin45;
	float[][] points;
	float[][] points2;
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int w = MeasureSpec.getSize(widthMeasureSpec);
		int h = MeasureSpec.getSize(heightMeasureSpec);
		
		float r = w/2 - getPix(R.dimen.padding_left);
		
		int real = w<h? w : h;
		int pad10 = getPix(R.dimen.padding_10);
		
		setMeasuredDimension(real, (int) ((r + paint.getTextSize() + pad10)*2 + pad10));
		
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	final String[] TITLES = {
		"感知(P)", "外向(E)", "感觉(S)", "情感(F)",
		"判断(J)", "内向(I)", "直觉(N)",
		"思考(T)"
	};

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		float midX = getWidth()/2;
		float midY = getHeight()/2;
		
		canvas.translate(midX, midY);

		paint.setColor(Color.GRAY);
		canvas.drawLine(0, r, 0, -r, paint);
		canvas.drawLine(rcos45, rcos45, -rcos45, -rcos45, paint);
		canvas.drawLine(r, 0, -r, 0, paint);
		canvas.drawLine(-rcos45, rcos45, rcos45, -rcos45, paint);
		
		int i;
		for(i=0; i<points.length-1; ++i){
			canvas.drawLine(points[i][0], points[i][1], points[i+1][0], points[i+1][1], paint);
		}
		canvas.drawLine(points[i][0], points[i][1], points[0][0], points[0][1], paint);
		String s = "0%";
		canvas.drawText(s, 0-paint.measureText(s)-3, -3, paint);
		
		for(i=0; i<points.length-1; ++i){
			canvas.drawLine(points[i][0]/2, points[i][1]/2, points[i+1][0]/2, points[i+1][1]/2, paint);
		}
		canvas.drawLine(points[i][0]/2, points[i][1]/2, points[0][0]/2, points[0][1]/2, paint);
		s = "50%";
		canvas.drawText(s, 0-paint.measureText(s)-3, -r/2-3, paint);
		
		if(points2 != null){
			paint2.setColor(0xff4376AD);
			for(i=0; i<points2.length-1; ++i){
				canvas.drawLine(points2[i][0], points2[i][1], points2[i+1][0], points2[i+1][1], paint2);
			}
			canvas.drawLine(points2[i][0], points2[i][1], points2[0][0], points2[0][1], paint2);
			
			for(i=0; i<points2.length; ++i){
				if(i == 4)
					paint2.setColor(Color.RED);
				canvas.drawCircle(points2[i][0], points2[i][1], 4*metrics.scaledDensity, paint2);
			}		
		}
		
		int pad10 = getPix(R.dimen.padding_10);
		
		paint.setColor(Color.BLACK);
		for(i=0; i<TITLES.length; ++i){
			if(i == 4)
				paint.setColor(Color.RED);
			
			float tw = paint.measureText(TITLES[i]);
			float th = paint.getTextSize();
			float x = points[i][0];
			float y = points[i][1];
			
			if(x < 0){
				x -= tw + pad10;
			}else if (x == 0) {
				x -= tw/2;
				y += y<0? -pad10 : pad10+th;
			}else {
				x += 10;
			}
			
			if (y == 0) {
				y += th/2;
			}
			
			canvas.drawText(TITLES[i], x, y, paint);
		}
	}
	
	Mbti mbti;
	
	public void setMbti(Mbti mbtia) {
		if(mbtia == null){
			this.mbti = new Mbti(40, 33, 20, 50);
		}else {
			this.mbti = mbtia;
		}
		
		points2 = new float[][]{
				{-mbti.getP()*rcos45/100f, -mbti.getP()*rsin45/100f},
				{0, -r*mbti.getE()/100f},
				{mbti.getS()*rcos45/100f, -mbti.getS()*rsin45/100f},
				{r*mbti.getF()/100f, 0},
				{mbti.getJ()*rcos45/100f, mbti.getJ()*rsin45/100f},
				{0, r*mbti.getI()/100f},
				{-mbti.getN()*rcos45/100f, mbti.getN()*rsin45/100f},
				{-r*mbti.getT()/100f, 0}
			};
		
		invalidate();
	}
}
