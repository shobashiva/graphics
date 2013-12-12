package com.example.basketball;

public class GameObject {
	public enum Type { Ball, Plank, Camera };
	public Type type;
	public float x = 0;
	public float y = 0;
	public float z = 0;
	public float velX = 0;
	public float velY = 0;
	public float scaleX = 1;
	public float scaleY = 1;
	public float scaleZ = 1;
	
	public GameObject(Type type) {
		this.type = type;
	}
	
	void moveTo(float xV, float yV, float zV) {
		this.x = xV;
		this.y = yV;
		this.z = zV;
	}
	
	void scale(float x, float y, float z) {
		this.scaleX = x;
		this.scaleY = y;
		this.scaleZ = z;
	}
	
	void scale(float factor) {
		this.scaleX = factor;
		this.scaleY = factor;
		this.scaleZ = factor;
	}
	
	float boundStartX() {
		return x-scaleX/2;
	}
	
	float boundStopX() {
		return x+scaleX/2;
	}
	
	float boundStartY() {
		return y-scaleY/2;
	}
	
	float boundStopY() {
		return y+scaleY/2;
	}
	
	float boundStartZ() {
		return z-scaleZ/2;
	}
	
	float boundStopZ() {
		return z+scaleZ/2;
	}
}