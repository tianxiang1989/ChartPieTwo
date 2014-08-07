package com.example.chartpie;

/**
 * 坐标bean
 * @author liuxiuquan
 * 2014年8月7日
 */
public class PieBean {
	private float posX;//x坐标
	private float posY;//y坐标

	public PieBean(float posX,float posY){
		this.posX=posX;
		this.posY=posY;
	}
	
	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

}
