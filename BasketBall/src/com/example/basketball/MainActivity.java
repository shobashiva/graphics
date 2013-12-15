package com.example.basketball;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	SurfaceView mGLView;
	
	ArrayList<GameObject> mRenderObjs = new ArrayList<GameObject>();
	
	Ball mBall;
	Camera mCamera = new Camera();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mCamera.moveTo(-5f, 5.0f, 0);
		mCamera.lookAt(0f, 5f, 0f);
		
		GameObject court = new GameObject(GameObject.Type.Plank);
		court.scale(42f,0.1f,50f);
		court.moveTo(-6f, -0.05f, 0);
		
		GameObject pole = new GameObject(GameObject.Type.Plank);
		pole.scale(.3f, 10f,.3f);
		pole.moveTo(22.5f, 5f, 0f);
		
		GameObject pole2 = new GameObject(GameObject.Type.Plank);
		pole2.scale(4f, .3f,.3f);
		pole2.moveTo(19f, 15f, 0f);
		
		GameObject backboard = new GameObject(GameObject.Type.Plank);
		backboard.scale(0.1f, 3.5f, 6f);
		backboard.moveTo(15.1f, 13f, 0f);
		
//		GameObject rim = new GameObject(GameObject.Type.Plank);
//		rim.scale(1f, 0.1f, 1f);
		GameObject rim = new Rim();
		rim.scale(1.6f, 0.1f, 1.6f);
		rim.moveTo(14.5f, 10f, 0f);
		
		mBall = new Ball(backboard,rim);
		mBall.scale(0.8f);
		mBall.moveTo(0f,0.4f,0f);
		
		mRenderObjs.add(pole);
		mRenderObjs.add(pole2);
		mRenderObjs.add(backboard);
		mRenderObjs.add(mBall);
		mRenderObjs.add(court);
		mRenderObjs.add(rim);
		
		mGLView = new SurfaceView(this,mCamera,mBall,mRenderObjs);
		setContentView(mGLView);
		
		Thread logicTask = new Thread() {
			@Override
			public void run() {
				
				try {
					while(true) {
						mBall.update(0.017f);
						mGLView.updateFrame();
						sleep(17);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
        };
        
        logicTask.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
