package com.example.basketball;

public class Camera extends GameObject {
	public Camera() {
		super(GameObject.Type.Camera);
	}
	
	float targetX;
	float targetY;
	float targetZ;
	
	void lookAt(GameObject obj) {
		targetX = obj.x;
		targetY = obj.y;
		targetZ = obj.z;
	}
	
	void lookAt(float x, float y, float z) {
		targetX = x;
		targetY = y;
		targetZ = z;
	}
}
