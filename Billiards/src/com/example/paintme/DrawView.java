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
	
	public class GameObject {
		protected float velocityX, velocityY, velocityZ;
		private float startX, stopX;
		private float startY, stopY;
		private float startZ, stopZ;
		protected float centerX, centerY, centerZ;
		private float length;
		
		
		public GameObject(){
			
		}
		
		public GameObject (float mstartX, float mstartY, float mstopX, float mstopY){

			startX = mstartX;
			startY = mstartY;
			stopX = mstopX;
			stopY = mstopY;
		}
		
		public GameObject(int radius){
			
		}
		
		public float getVelocityX(){
			return velocityX;
		}
		
		public float getVelocityY(){
			return velocityY;
		}
		
		public float getVelocityZ(){
			return velocityZ;
		}
		
	    public float getStartX(){
	    	return startX;
	    }
	    
	    public float getStopX(){
	    	return stopX;
	    }
	    
	    public float getStartY(){
	    	return startY;
	    }
	    
	    public float getStopY(){
	    	return stopY;
	    }
	    
	    public void setLength(float mLength){
	    	length = mLength;
	    }
	    
	    public float getLength(){
	    	return length;
	    }
	    
	    public void drawBoard(Canvas mCanvas){
			mCanvas.drawLine(startX,startY,stopX,stopY,fgColor);
		}
	    
//	    public void drawBall(Canvas mCanvas){
//			mCanvas.drawCircle(centerX, centerY, radius, fgColor);
//		}
	}
	
	public class Rim extends GameObject {

		public Rim (float mstartX, float mstartY, float mstopX, float mstopY){
			super(mstartX, mstartY, mstopX, mstopY);
		}

	}
	
	public class Backboard extends GameObject{

		public Backboard (float mstartX, float mstartY, float mstopX, float mstopY){
			super(mstartX, mstartY, mstopX, mstopY);
			centerY = mstartY;
		}
		
		public float getCenterY(){
			return centerY;
		}
	}
	
	
	public class BBall extends GameObject{

		private int radius = 25;
		
		public BBall(){
			super();
		}
		
		public void drawBall(Canvas mCanvas){
			mCanvas.drawCircle(centerX, centerY, radius, fgColor);
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
		
		public float updateX(){
			float newX;
			if(thrown){
				newX = ((float)0.01*velocityX) + centerX + (0.5f*gravity*timeInterval*timeInterval);
			}
			else{
				newX = ((float)0.01*velocityX) + centerX;
			}
			
			return newX;
		}
		
		public float updateY(){
			float newY = ((float)0.01*velocityY) + centerY;
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

			//Get the the position that the ball will be updated to - we use this to determine
			// if the ball is going to collide with anything.
			float updatedX = updateX();

			float updatedY = updateY();
			
			/*
			 * First check if the updated position of the ball would put it out of the bounds 
			 * of the court.  If it does, the velocities are adjusted accordingly in the
			 * 'else' statement.
			 * If the updated position would be in the court, first check if it collides with
			 * the backboard.  If it does, adjust the velocity accordingly, then adjusted the 
			 * updated position using the new velocity.  Do the same for the rim.  The logic 
			 * to see if a basket is made is in the logic for the rim.
			 */

			
			//case where ball is within the court
			if((updatedX > 10 && updatedX < (WIDTH_BOUND - 10)) && (updatedY > 10 && updatedY < (HEIGHT_BOUND - 10))){
				
				//check for collision with backboard
					if(updatedY < (mBackboard.getCenterY() + radius)){
						if((updatedX > (mBackboard.getStartX() - radius)) && (updatedX < (mBackboard.getStopX() + radius))){
							setVelocityY(-velocityY/2);
							updatedY = updateY();
						}
					}
				
				//check for collision with rim or basket
				if(mRim != null){
					if(updatedX > mRim.getStartX()){
						//if you've made a basket, stop the ball
						if((updatedY > (mRim.getStartY() + radius/2)) && updatedY < mRim.getStopY()){
							setVelocityY(0);
							setVelocityX(0);
							updatedY = updateY();
							updatedX = updateX();
						}
					}
					// else just collide and bounce off
					else if(updatedX == mRim.getStartX()){
						setVelocityY(-velocityY/2);
						updatedY = updateY();
					}
				}
				
				setX(updatedX);
				setY(updatedY);
				updateVelX();
				updateVelY();
			} 
			else{
				//the cases represented are in the 'Log' statements
				if(updatedX < radius){
					setVelocityX(-velocityX);
					Log.i(TAG, "x less than min");
				}
				if(updatedX > (WIDTH_BOUND - radius)){
					setVelocityX(-velocityX);
					Log.i(TAG, "x greater than max");

				}
				if(updatedY < radius){
					setVelocityY(-velocityY);
					Log.i(TAG, "y less than min");

				}
				if(updatedY > (HEIGHT_BOUND - radius)){
					setVelocityY(-velocityY);
					Log.i(TAG, "y greater than max");

				}
				
				// in this clause, at least one velocity has changed, so update position
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


	// this is a friction coefficient so the ball will stop moving eventually
	//may not be needed after initial testing
	Integer kA = 20;
	
	BBall mBall = null;
	Backboard mBackboard = new Backboard(150.0f, 200.0f, 400.0f, 200.0f);
	Rim mRim = new Rim(275f, 200f, 275f, 275f);

	
	boolean mTimeOn = false;
	Integer WIDTH_BOUND;
	Integer HEIGHT_BOUND;
	
	float gravity = 9.8f;
	float timeInterval = 2f;
	boolean thrown = false;
	
	/*
	 * This function is used to determine if the user's finger was anywhere near
	 * the ball when they did a fling.
	 */
	public boolean findBall(float xLess,float xMore, float velocityX, float velocityY){

			if(mBall.centerX <= xMore && mBall.centerX >= xLess){
				Log.i(TAG, "ball in motion");
				mBall.setVelocityX(velocityX);
				mBall.setVelocityY(velocityY);
				return true;
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
	
	//start updating the UI once the game begins
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

		Paint p = new Paint();
		p.setColor(Color.CYAN);
		
		if(MainActivity.clear){
			mBall = null;
			MainActivity.clear = false;
			Log.i(TAG, "clear");
		}
		
		mBackboard.drawBoard(canvas);
		mRim.drawBoard(canvas);
		
		if (mBall!= null) {
			p.setColor(Color.BLACK);

			mBall.drawBall(canvas);
			mBall.update(canvas);
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
		
		if(mBall != null){
		m1 = new PointF(e1.getX()-25, e1.getY()-25); 
		m2 = new PointF(e1.getX()+25, e1.getY()+25);
		// check to see if the fling event occurred near the ball
		findBall(m1.x,m2.x, velocityX, velocityY);
		
		}
		
		if(!thrown){
			thrown = true;
		}
		
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		/*
		 * If there is no ball, create a new Ball object, set the x and y
		 * coordinates for the center, and make sure that the thrown variable is false
		 * so that gravity doesn't turn on yet.  Gravity should not be on until the
		 * first onFling event near the ball.
		 */
		if(mBall == null){
			mBall = new BBall();
			mBall.setX(e.getX());
			mBall.setY(e.getY());
			thrown = false;
		}

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
