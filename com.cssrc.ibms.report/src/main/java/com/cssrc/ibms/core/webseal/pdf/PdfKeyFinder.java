package com.cssrc.ibms.core.webseal.pdf;

import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/** 
* @ClassName: PdfKeyFinder 
* @Description: pdf关键字定位监听器
* @author zxg 
* @date 2016年12月5日 下午3:05:42 
*  
*/
public class PdfKeyFinder implements RenderListener {
	private String key;
	private String imgPath;
	
	private StringBuffer username = new StringBuffer();
	private List<String> usernames = new ArrayList<String>();

	private Rectangle2D.Float textRectangle = null;
	private List<KeyPosition> textRectangles = new ArrayList<KeyPosition>();
	private List<KeyPosition> positions = new ArrayList<KeyPosition>();

	public PdfKeyFinder(String username) {
		this.key = username;
	}
	public PdfKeyFinder(String username,String imgPath) {
		this.key = username;
		this.imgPath = imgPath;
	}

	public void setTexts(String username) {
		usernames.add(username);
	}

	public List<KeyPosition> getPositions() {
		return this.positions;
	}

	public void renderText(TextRenderInfo renderInfo) {
		String text = renderInfo.getText().toString();
		Rectangle2D.Float boundingRectange = renderInfo.getBaseline()
				.getBoundingRectange();
		if (text != null && !text.trim().equals("") && key.indexOf(text) > -1) {
			this.username.append(text);
			textRectangles.add(new KeyPosition(text, boundingRectange));
		} else if (text != null && !text.trim().equals("")) {
			this.username.delete(0, this.username.length());
			textRectangles.clear();
		}
		if (null != text && key.equals(this.username.toString())) {
			positions.add(new KeyPosition(textRectangles, key,imgPath));
		}
	}

	public float getLlx() {
		return this.textRectangle.x;
	}

	public float getLly() {
		return this.textRectangle.y;
	}

	public float getUrx() {
		return this.textRectangle.x + this.textRectangle.width;
	}

	public float getUry() {
		return this.textRectangle.y + this.textRectangle.height;
	}

	public float getWidth() {
		return this.textRectangle.width;
	}

	public float getHeight() {
		return this.textRectangle.height;
	}

	public void beginTextBlock() {
	}

	public void endTextBlock() {
	}

	public void renderImage(ImageRenderInfo renderInfo) {
	}

}
