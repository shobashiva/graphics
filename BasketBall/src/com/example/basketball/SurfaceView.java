package com.example.basketball;

import java.util.ArrayList;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

// a view that can draw and manipulate objects using OpenGL ES 2.0
public class SurfaceView extends GLSurfaceView implements GestureDetector.OnGestureListener {
	
	private RenderEngine mRenderer;
	private ArrayList<GameObject> mRenderObjectRef;
	private Camera mCameraRef;
	private Ball mBallRef;
	
	GestureDetector mGesture;
	
	class Position {
		Position(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		float x,y,z;
	}
	
	int mCamPosIndex = 0;
	
	private ArrayList<Position> cameraPositions = new ArrayList<Position>();
	
	public SurfaceView(Context context, Camera camera, Ball ball, ArrayList<GameObject> renderObjs) {
		super(context);

		mGesture = new GestureDetector(context,this);
		
		cameraPositions.add(new Position(-20, 5.5f, 0));
		cameraPositions.add(new Position(10f, 8f, 25f));
		cameraPositions.add(new Position(-30f, 10f, 15f));
		
		mCameraRef = camera;
		mRenderObjectRef = renderObjs;
		mBallRef = ball;
		
		// Create and OpenGL ES 2.0 context
		setEGLContextClientVersion(2);
		setEGLConfigChooser(8,8,8,8,16,0);

		// Set the Renderer for drawing on the GLSurfaceView
		mRenderer = new RenderEngine(camera,renderObjs);
		setRenderer(mRenderer);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mGesture.onTouchEvent(event))
			return true;
		
		return super.onTouchEvent(event);
	}
	
	public void updateFrame() {
		requestRender();
	}

	@Override
	public boolean onDown(MotionEvent e) {
		
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		mBallRef.x = 0f;
		mBallRef.y = 5.5f;
		float magnitude = (float) Math.sqrt(Math.abs(velocityX*velocityX) + Math.abs(velocityY*velocityY))*0.005f;
		
		// angle = 2/3PI radians = 60 degrees
		float angleRadians = (float) ((2/3f)*Math.PI);
		
		// using magnitude of fling
		mBallRef.velX = (float) -(Math.cos(angleRadians) * magnitude);
		mBallRef.velY = (float) (Math.sin(angleRadians) * magnitude);
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		mCamPosIndex++;
		if(mCamPosIndex > cameraPositions.size()-1)
			mCamPosIndex = 0;
		
		Position p = cameraPositions.get(mCamPosIndex);
		mCameraRef.moveTo(p.x, p.y, p.z);
		
		Log.i("cameratouch","moving to " + p.x + ", " + p.y + ", " + p.z);
		
		requestRender();
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}
