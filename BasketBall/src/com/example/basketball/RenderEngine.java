package com.example.basketball;


import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

//a renderer that can draw and manipulate objects
@SuppressLint("NewApi")
public class RenderEngine implements GLSurfaceView.Renderer {
	
	private Cube mCube; 
	private Sphere mSphere;
	
	private float mZoomDist = 5;
	
	private final float[] mVMatrix = new float[16];
	private final float[] mProjMatrix = new float[16];
	private final float[] mVPMatrix = new float [16];
	//private final float[] mCommonTransformation = new float [16];
	
	// Set up the view's OpenGL ES environment
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
	    GLES20.glDepthFunc(GLES20.GL_LEQUAL);
	    GLES20.glDepthMask(true);
	    
		// initialize basic shapes
		mCube = new Cube(); 
		
		float[] color1 = new float[]{0.88f, 0.12f, 0.12f, 0.6f};
		mSphere = new Sphere(1.0f, 20, 40, color1, -2f, 1f);
	}
	
	ArrayList<GameObject> mRenderObjectRef;
	Camera mCameraRef;
	
	public RenderEngine(Camera camera, ArrayList<GameObject> renderObjs) {
		mRenderObjectRef = renderObjs;
		mCameraRef = camera;
	}
	
	// Called for each redraw of the view
	@Override
	public void onDrawFrame(GL10 gl) {
		// Set the background frame color
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		GLES20.glClearDepthf(1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		Matrix.setLookAtM(mVMatrix, 0, 
				mCameraRef.x, mCameraRef.y, mCameraRef.z, 
				mCameraRef.targetX, mCameraRef.targetY, mCameraRef.targetZ, 
				0f, 1.0f, 0.0f);
		
		// Calculate the projection and view transformation
		// tempPV = Proj*View; 
		
		
		// mCommonTransformation = RotX*RotY
		//Matrix.setRotateM(mRotationMatrix_X,  0,  mAngleX,  0,  1.0f,  0);
		//Matrix.setRotateM(mRotationMatrix_Y,  0,  mAngleY,  1.0f,  0,  0);
		//Matrix.multiplyMM(mCommonTransformation,  0,  mRotationMatrix_Y, 0, mRotationMatrix_X,  0);

		Matrix.multiplyMM(mVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
		
		for(GameObject obj : mRenderObjectRef) {
			switch(obj.type) {
			case Plank:
				drawCube(obj.scaleX, obj.scaleY, obj.scaleZ, obj.x, obj.y, obj.z);
				break;
			case Ball:
				drawSphere(obj.scaleX, obj.scaleY, obj.scaleZ, obj.x, obj.y, obj.z);
				break;
			}
		}
	}
	
	public void drawSphere(float sx, float sy, float sz, float tx, float ty, float tz) {
		float[] tt = new float[16];
		float[] sphereTransformation = new float[16]; 
		float[] sphereMVP = new float[16];
		float[] sphereMV = new float[16];
		float[] sphereNormal = new float[16];
		// sphereTransformation = temp2[RotX*RotY] * scale * translate
		//System.arraycopy(mCommonTransformation, 0, sphereTransformation, 0, mCommonTransformation.length);
		Matrix.setIdentityM(sphereTransformation, 0);
		Matrix.translateM(sphereTransformation, 0, tx, ty, tz);
		Matrix.scaleM(sphereTransformation, 0, sx, sy, sz);
		Matrix.multiplyMM(sphereMVP, 0, mVPMatrix, 0, sphereTransformation, 0);
		// normal mat = transpose(inv(modelview)); 
		Matrix.multiplyMM(sphereMV, 0, mVMatrix, 0, sphereTransformation, 0);
		Matrix.invertM(tt, 0, sphereMV, 0);
		Matrix.transposeM(sphereNormal, 0, tt, 0);
		mSphere.draw(sphereMVP, sphereNormal, sphereMV);
	}
	
	public void drawCube(float sx, float sy, float sz, float tx, float ty, float tz) {
		float [] tt = new float[16];
		float [] temp = new float[16];
		float [] cubeTransformation = new float[16]; 
		float [] cubeMVP = new float[16];
		float [] cubeNormal = new float[16];
		//System.arraycopy(mCommonTransformation, 0, cubeTransformation, 0, mCommonTransformation.length);
		Matrix.setIdentityM(cubeTransformation, 0);
		Matrix.translateM(cubeTransformation, 0, tx, ty, tz);
		Matrix.scaleM(cubeTransformation, 0, sx, sy, sz);
		Matrix.multiplyMM(cubeMVP, 0, mVPMatrix, 0, cubeTransformation, 0);
		// normal mat = transpose(inv(modelview)); 
		Matrix.multiplyMM(temp, 0, mVMatrix, 0, cubeTransformation, 0);
		Matrix.invertM(tt, 0, temp, 0);
		Matrix.transposeM(cubeNormal, 0, tt, 0);
		mCube.draw(cubeMVP, cubeNormal);
	}

	// Called if the geometry of the view changes
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// set view port to the windows's width and height
		GLES20.glViewport(0,  0,  width,  height);
		
		float ratio = (float) width / height;
		
		// this projection matrix is applied to object coordinates
		// in the onDrawFrame() method
		
		// if you have android API level 14+ you can use this
		Matrix.perspectiveM(mProjMatrix, 0, 75.0f, ratio, 1.0f, 150.0f);
		
		//Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 7);
	}
	
	// compile GLSL code prior to using it in OpenGL ES environment
	public static int loadShader(int type, String shaderCode) {
		
		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);
		
		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		
		return shader;
	}

}