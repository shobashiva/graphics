package com.example.basketball;

import android.graphics.Canvas;
import android.util.Log;


public class Ball extends GameObject{

//	private final float WIDTH_BOUND = 100f;
//	private final float HEIGHT_BOUND = 100f;
//	
//	private boolean mThrown = false;
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
	
	public boolean inZBound(GameObject mObj, float updated, float padding){
		boolean inBounds = false;
//		Log.e("mZ", ""+updated);
		if(updated <  (mObj.boundStopZ() + padding)){
//			Log.e("mZ", "Z lt");
			if(updated > mObj.boundStartZ() - padding){
//				Log.e("mZ", "Z gt");
				inBounds = true;
			}
		}
		return inBounds;
	}
	
	public boolean inYBound(GameObject mObj, float updated, float padding){
		boolean inBounds = false;
		if(updated <  (mObj.boundStopY() + padding)){
			if(updated > mObj.boundStartY() + 0.5f){
//				Log.e("mY", "Y gt");
				inBounds = true;
			}
		}
		return inBounds;
	}
	
	public void collision(GameObject mObj){
		
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
	
	public float updateZ(float stepScale){
		return z + stepScale*(velZ);
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
	
	public void updateVelZ(){
		//if your addition of the friction co-efficient changes the sign set velocity to zero
		//
		if(y <= floorBound+0.03f) {
			velZ *= 0.95f; // floor friction
		} else {
			velZ *= 0.999f; // air resistance/drag
		}
		
		if(velZ < 0){
			if(velZ > 0){
				velZ = 0;
			}
		}
		else{
			if(velZ < 0){
				velZ = 0;
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
		float updatedZ = updateZ(stepScale);

		float addRadX = updatedX + radius;
//		float addRadY = updatedY + radius;

		// check if past rim
		// check if above rim
		// check if before backboard
		//check for collision with backboard
		//if up to the backboard and within the y bounds 
		//8f makes the bounds work

		if(addRadX > mRimRef.boundStartX()){
			if(inZBound(mRimRef, updatedZ, 0.8f)){
				if(inYBound(mRimRef, updatedY, 0.1f)){
					Log.e("basket", "basket");
				}
				else if(updatedY > mRimRef.boundStartY()){
					//collision with rim
					velX = (velX + mRimRef.velX)/2;
					updatedX = updateX(stepScale);
				}
			}
			
			//collision with backboard	
			if(addRadX > (mBackboardRef.boundStartX())){
				if(inYBound(mBackboardRef, updatedY, 8f)){
					if(inZBound(mBackboardRef, updatedZ, 5f)){
						velX = (velX + mBackboardRef.velX)/2;
						updatedX = updateX(stepScale);
					}
				}
			}
		}

		x = updatedX;
		y = updatedY;
		z = updatedZ;
		updateVelX();
		updateVelZ();
		updateVelY(stepScale);


	}
		
}
