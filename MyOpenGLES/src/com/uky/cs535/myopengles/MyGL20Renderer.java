package com.uky.cs535.myopengles;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

//a renderer that can draw and manipulate objects
public class MyGL20Renderer implements GLSurfaceView.Renderer {
	
	private Triangle mTriangle;
	private Square mSquare;
	
	public volatile float mAngle; // use volatile because we modify it in other classes
	
	private final float[] mVMatrix = new float[16];
	private final float[] mProjMatrix = new float[16];
	private final float[] mMVPMatrix = new float[16];
	private final float[] mRotationMatrix = new float[16];
	
	// Set up the view's OpenGL ES environment
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set the background frame color
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		
		// initialize a triangle
		mTriangle = new Triangle();
		// initialize a square
		mSquare = new Square();
	}

	// Called for each redraw of the view
	@Override
	public void onDrawFrame(GL10 gl) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
		// Set the camera position (View matrix)
		Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		
		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
		
		mSquare.draw(mMVPMatrix);
		
		// Create a rotation transformation for the triangle
//		long time = SystemClock.uptimeMillis() % 4000L;
//		float angle = 0.090f * ((int) time);
		// instead of rotate automatically, we use mAngle
		Matrix.setRotateM(mRotationMatrix,  0,  mAngle,  0,  0,  -1.0f);

		// Combine the rotation matrix with the projection and camera view
		Matrix.multiplyMM(mMVPMatrix,  0,  mRotationMatrix,  0,  mMVPMatrix,  0);
		
		// Draw shape
		mTriangle.draw(mMVPMatrix);
	}

	// Called if the geometry of the view changes
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// set view port to the windows's width and height
		GLES20.glViewport(0,  0,  width,  height);
		
		float ratio = (float) width / height;
		
		// this projection matrix is applied to object coordinates
		// in the onDrawFrame() method
		Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
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