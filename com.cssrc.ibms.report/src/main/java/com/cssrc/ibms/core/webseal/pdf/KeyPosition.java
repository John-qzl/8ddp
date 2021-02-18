package com.cssrc.ibms.core.webseal.pdf;

import java.awt.geom.Rectangle2D;
import java.util.List;

/** 
* @ClassName: KeyPosition 
* @Description: pdf关键字定位坐标结果
* @author zxg 
* @date 2016年12月5日 下午3:05:07 
*  
*/
public class KeyPosition {
	private float x;
	private float y;
	private String text;
	private float image_width;
	private float image_height;
	private String imgPath;

	public KeyPosition(String text,Rectangle2D.Float f) {
		this.text=text;
		this.x=f.x;
		this.y=f.y;
		this.image_width=f.width;
		this.image_height=f.height;
	}

	public KeyPosition() {
	}

	public KeyPosition(List<KeyPosition> textRectangles, String text,String imgPath) {
		float stratX=textRectangles.get(0).getX();
		float stratY=textRectangles.get(0).getY();
		float endX=textRectangles.get(textRectangles.size()-1).getX();
		float endY=textRectangles.get(textRectangles.size()-1).getY();
		this.text=text;
		this.x=stratX;
		this.y=stratY;
		this.image_width=endX-stratX;
		if(endY-stratY>0){
			this.image_height=endY-stratY;
		}else{
			this.image_height=endX-stratX;
		}
		this.imgPath=imgPath;
	}

	public float getX() {
		return this.x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return this.y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public float getImage_width() {
		return this.image_width;
	}

	public void setImage_width(float image_width) {
		this.image_width = image_width;
	}

	public float getImage_height() {
		return this.image_height;
	}

	public void setImage_height(float image_height) {
		this.image_height = image_height;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	
}
