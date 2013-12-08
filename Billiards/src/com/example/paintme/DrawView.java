package com.example.paintme;

import java.util.ArrayList;
import java.util.Timer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class DrawView extends View implements OnGestureListener {

	public static final String TAG = "DrawView"; 
	
	
	public class PoolBall {
		
		private float centerX;
		private float centerY;
		private float velocityX;
		private float velocityY;
		
		public PoolBall(float xCoord, float yCoord){
			centerX = xCoord;
			Log.i("coord", "x is "+xCoord);
			centerY = yCoord;
			Log.i("coord", "y is "+yCoord);
			
		}
		
		public PoolBall(){
		}
		
		public void drawBall(Canvas mCanvas){
			mCanvas.drawCircle(centerX, centerY, 25, fgColor);
		}
		
		public void setX(float x){
			centerX = x;
			Log.i(TAG, "x is set");
		}
		
		public void setY(float y){
			centerY = y;
		}
		
		public void setVelocityX(float x){
			velocityX = x;
			Log.i(TAG, "vel x is " + velocityX);
		}
		
		public void setVelocityY(float y){
			velocityY = y;
		}
		
//		public void setSelected(boolean selected){
//			isSelected = selected;
//		}
		
		public float updateX(){
			float newX = ((float)0.01*velocityX*k) + centerX;
			return newX;
		}
		
		public float updateY(){
			float newY = ((float)0.01*velocityY*k) + centerY;
			return newY;
		}
		
		public void updateVelX(){
			
			if(velocityX < 0){
				setVelocityX(velocityX + kA);
				if(velocityX > 0){
					setVelocityX(0);
				}
			}
			else{
				setVelocityX((velocityX - kA));
				if(velocityX < 0){
					setVelocityX(0);
				}
			}
		}
		
		public void updateVelY(){
			//if your addition of the friction co-efficient changes the sign set velocity to zero
			if(velocityY < 0){
				setVelocityY(velocityY + kA);
				if(velocityY > 0){
					setVelocityY(0);
				}
			}
			else{
				setVelocityY((velocityY - kA));
				if(velocityY < 0){
					setVelocityY(0);
				}
			}
		}
		
		public void update(Canvas canvas){

			float updatedX = updateX();

			float updatedY = updateY();
			
			if((updatedX > 10 && updatedX < (WIDTH_BOUND - 10)) && (updatedY > 10 && updatedY < (HEIGHT_BOUND - 10))){
				if(findBallSpecific((updatedX - 35), (updatedX + 35),(updatedY - 35), (updatedY + 35), (velocityX/2), (velocityY/2))){
					setVelocityX((-velocityX/2));
					setVelocityY((-velocityY/2));
				}
				setX(updatedX);
				setY(updatedY);
				updateVelX();
				updateVelY();
			} 
			else{
				if(updatedX < 10){
					setVelocityX(-velocityX);
					Log.i(TAG, "x less than min");
				}
				if(updatedX > (WIDTH_BOUND - 10)){
					setVelocityX(-velocityX);
					Log.i(TAG, "x greater than max");

				}
				if(updatedY < 10){
					setVelocityY(-velocityY);
					Log.i(TAG, "y less than min");

				}
				if(updatedY > (HEIGHT_BOUND - 10)){
					setVelocityY(-velocityY);
					Log.i(TAG, "y greater than max");

				}
				
				updatedX = updateX();
				updatedY = updateY();
				updateVelX();
				updateVelY();
			}
			
			drawBall(canvas);
		}
			
	}
			
	private Paint bgColor;
	private Paint fgColor; 
	
	private PointF m1, m2;
	private GestureDetector gd; 
	
	public Button up;
	
	Integer k = 2;
	Integer kA = 20;
	
	PoolBall mBall = null;
	ArrayList<PoolBall> billiards = new ArrayList<PoolBall>();
	boolean mTimeOn = false;
	Integer WIDTH_BOUND;
	Integer HEIGHT_BOUND;
	
	public boolean findBall(float xLess,float xMore, float velocityX, float velocityY){

		for(int i = 0; i < billiards.size(); i++){
			mBall = billiards.get(i);
			if(mBall.centerX <= xMore && mBall.centerX >= xLess){
				Log.i(TAG, "ball in motion");
				mBall.setVelocityX(velocityX);
				mBall.setVelocityY(velocityY);
				return true;
			}
			
		}
		return false;
	}
	
	public boolean findBallSpecific(float xLess,float xMore,float yLess, float yMore, float velocityX, float velocityY){

		for(int i = 0; i < billiards.size(); i++){
			mBall = billiards.get(i);
			if((mBall.centerX <= xMore && mBall.centerX >= xLess)&&(mBall.centerY <= yMore && mBall.centerY >= yLess)){
//				if(mBall.centerY <= yMore && mBall.centerY >= yLess){
				Log.i(TAG, "ball in motion");
				mBall.setVelocityX(velocityX);
				mBall.setVelocityY(velocityY);
				return true;
//				}
			}
			
		}
		return false;
	}

	
	public DrawView(Context context) {
		super(context);
	}

	public DrawView(Context c, AttributeSet attrs) {
		super(c, attrs);
		
		bgColor = new Paint();
		bgColor.setColor(0xff267312);
		
		fgColor = new Paint();
		fgColor.setARGB(255, 255, 128, 0);
		

		gd = new GestureDetector(c, this);  


	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
		if (mTimeOn == false) {
			mUIUpdater.startUpdates();
			mTimeOn = true;
		}


		
		return gd.onTouchEvent(event);  
		
		
	}
	
	UIUpdater mUIUpdater = new UIUpdater(new Runnable() {
	    @Override 
	    public void run() {
	      	invalidate();
	    }
	});

	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawPaint(bgColor);
		WIDTH_BOUND = canvas.getWidth();
		HEIGHT_BOUND = canvas.getHeight();
		
		canvas.drawLine(213.0f, 314.0f, 442.0f, 314.0f, fgColor);

		Paint p = new Paint();
		p.setColor(Color.CYAN);
		
		if(MainActivity.clear){
			billiards.clear();
			MainActivity.clear = false;
			Log.i(TAG, "clear");
		}
		
		if(MainActivity.mK > 0){
			k++;
			MainActivity.mK = 0;
		}
		
		if(MainActivity.mK < 0){
			k--;
			MainActivity.mK = 0;
		}
		
		
		for(int i = 0; i < billiards.size(); i++){
		
		mBall = billiards.get(i);
		if (mBall!= null) {
			p.setColor(Color.BLACK);

			mBall.drawBall(canvas);
			mBall.update(canvas);

		
		}
		}

		
	}

	@Override
	public boolean onDown(MotionEvent e) {
		Log.i(TAG, "touch down: (" + e.getX() + "," + e.getY() + ")"); 
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(billiards.size() > 0){
		m1 = new PointF(e1.getX()-25, e1.getY()-25); 
		m2 = new PointF(e1.getX()+25, e1.getY()+25);
		findBall(m1.x,m2.x, velocityX, velocityY);
		}

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Log.i(TAG, "long press: (" + e.getX() + "," + e.getY() + ")");
		Log.i(TAG, mBall+"");
		m1 = new PointF(e.getX()-10, e.getY()-10); 
		m2 = new PointF(e.getX()+10, e.getY()+10); 
		billiards.add(new PoolBall());
		int index = billiards.size()-1;
		billiards.get(index).setX(e.getX());
		billiards.get(index).setY(e.getY());
		invalidate(); 
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		Log.i(TAG, "touch scroll: (" + distanceX + "," + distanceY + ")"); 
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		
		return true;
	}
	
}
