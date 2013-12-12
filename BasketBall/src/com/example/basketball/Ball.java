package com.example.basketball;

import android.graphics.Canvas;
import android.util.Log;


public class Ball extends GameObject{

	private final float WIDTH_BOUND = 100f;
	private final float HEIGHT_BOUND = 100f;
	
	private boolean mThrown = false;
	private float gravity = 9.8f;
	private float radius = 0.4f;

	private GameObject mBackboardRef = null;
	private GameObject mRimRef = null;
	private float floorBound = 1f;
	
	public Ball(GameObject rim, GameObject backboard) {
		super(GameObject.Type.Ball);
		mBackboardRef = backboard;
		mRimRef = rim;
	}
	
	public float updateY(float stepScale){
		float nextY = y + stepScale*(velY);
		if(nextY < floorBound) {
			velY *= -1*0.85f;
		}
		return Math.max(floorBound,nextY);
	}
	
	
	public float updateX(float stepScale){
		return x + stepScale*(velX);
	}
	
	public void updateVelX(){
		//if your addition of the friction co-efficient changes the sign set velocity to zero
		//
		if(y <= floorBound+0.03f) {
			velX *= 0.95f; // floor friction
		} else {
			velX *= 0.999f; // air resistance/drag
		}
		if(velX < 0){
			if(velX > 0){
				velX = 0;
			}
		}
		else{
			if(velX < 0){
				velX = 0;
			}
		}
	}
	
	public void updateVelY(float stepScale){
		//if your addition of the friction co-efficient changes the sign set velocity to zero
		
		velY -= gravity*stepScale;
		velY *= 0.999f; // air resistance/drag
		
		if(velY < 0){
			if(velY > 0){
				velY = 0;
			}
		}
		else{
			if(velY < 0){
				velY = 0;
			}
		}
	}
	
	public void update(float stepScale){

		//Get the the position that the ball will be updated to - we use this to determine
		// if the ball is going to collide with anything.
		float updatedX = updateX(stepScale);
		float updatedY = updateY(stepScale);
		
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
		//if((updatedX > 10 && updatedX < (WIDTH_BOUND - 10)) && (updatedY > 10 && updatedY < (HEIGHT_BOUND - 10))){
			
			//check for collision with backboard
				/*if(updatedX < (mBackboardRef.x + radius)){
					if((updatedY > (mBackboardRef.y - radius)) && (updatedY < (mBackboardRef.boundStopY() + radius))){
						x = -velX/2;
						updatedX = updateX(stepScale);
					}
				}*/
			
			//check for collision with rim or basket
			//if(mRimRef != null){
				/*if(updatedY > mRimRef.boundStartY()){
					//if you've made a basket, stop the ball
					if((updatedX > (mRimRef.x + radius/2)) && updatedX < mRimRef.x){
						velX = velY = 0;
						updatedY = updateY(stepScale);
						updatedX = updateX(stepScale);
					}
				}*/
				// else just collide and bounce off
				/*else if(updatedY == mRimRef.boundStartY()){
					velX = (-velX/2);
					updatedX = updateX();
				}*/
			//}
			
			x = updatedX;
			y = updatedY;
			updateVelX();
			updateVelY(stepScale);
		/*} 
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
		}*/
		
	}
		
}
