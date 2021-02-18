package com.cssrc.ibms.reportclient;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import com.fr.base.AbstractPainter;
import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.base.Style;
import com.fr.general.FArray;
import com.fr.general.FRFont;
import com.fr.stable.StringUtils;

public class WidgetPaint extends AbstractPainter
{
    private static final long serialVersionUID = 5867600745896402001L;

    public static String CHECK_ON = "/com/fr/web/images/checkon.gif";
    
    public static String CHECK_OFF = "/com/fr/web/images/checkoff.gif";
    
    public static String RADIO_ON = "/com/fr/web/images/radioon.gif";
    
    public static String RADIO_OFF = "/com/fr/web/images/radiooff.gif";
    
    //字体
    public FRFont defaultFont;
    public FontMetrics fontMetrics;
    public int fontSize;
    
    private int offx;
    private int offy;
    
    FArray<HtmlElement> htmlElements;
    
    public WidgetPaint(FArray<HtmlElement> htmlElements,int fontSize)
    {
        this.htmlElements = htmlElements;
        this.offx=2;
        this.fontSize=fontSize;
        this.defaultFont=FRFont.getInstance(new Font("宋体",Font.PLAIN,fontSize));
        this.defaultFont = defaultFont.applyForeground(Color.BLACK);
        this.fontMetrics = GraphHelper.getFontMetrics(defaultFont);
        this.offy=4;
    }
    
    public void paint(Graphics g, int width, int height, int resolution, Style style)
    {
        
        Graphics2D g2d = (Graphics2D)g;
        g2d.setFont(FRFont.getInstance());
        g2d.setPaint(Color.BLACK);
        //坐标
        Float p=new Float(offx,offy);
        //td宽高，实现自动换行
        Float td=new Float(width,height);

        for (HtmlElement htmlElement:htmlElements.toList())
        {
            p.x+=offx;
           // System.out.println("------x:"+p.x+"--------:"+htmlElement.getText());
            if(htmlElement.getType().equals("checkbox")||"radio".equals(htmlElement.getType())){
                p=this.drawImage(g2d, htmlElement, p, td);
                p=this.drawString(g2d,htmlElement,p, td);
            }else{
                p=drawString(g2d,htmlElement,p, td);
            }
        }
        
    }
    
    public Float drawString(Graphics2D g2d,HtmlElement htmlElement,Float p,Float td){
    	
    	if("br".equals(htmlElement.getType())){
             p.y+=offy+fontMetrics.getAscent();
             p.x=offx;
             g2d.drawString("", (float)p.x, (float)(p.y + fontMetrics.getAscent()-2));
    	}else{
    		String text = htmlElement.getText();
    		char[] texts=text.toCharArray();
    		g2d.setFont(new Font("宋体",Font.PLAIN,fontSize));
    		for(char c:texts){
    			g2d.setBackground(Color.BLACK);
    			int c_w=fontMetrics.stringWidth(String.valueOf(c));
    			//超出表格宽自动换行
    			if(p.x + c_w+offx>td.x){
    				p.y+=offy+fontMetrics.getAscent();
    				p.x=offx;
    			}
    			g2d.drawString(String.valueOf(c), (float)p.x, (float)(p.y + fontMetrics.getAscent()-2));
    			p.x += c_w;
    		}
    	}
        return p;
    }
    public Float drawImage(Graphics2D g2d,HtmlElement htmlElement,Float p,Float td){
        String OFF = null;
        String ON = null;
        if("checkbox".equals(htmlElement.getType())){
            OFF = CHECK_OFF;
            ON = CHECK_ON;
        }else if ("radio".equals(htmlElement.getType()))
        {
            OFF = RADIO_OFF;
            ON = RADIO_ON;
        }
        Image[] checkOFFON = {BaseUtils.readImage(OFF), BaseUtils.readImage(ON)};
        int[] imgWidths = {checkOFFON[0].getWidth(null), checkOFFON[1].getWidth(null)};
        int[] imgHeights = {checkOFFON[0].getHeight(null), checkOFFON[1].getHeight(null)};
        int bit = htmlElement.getSelected();
        //超出表格宽自动换行
        if(p.x+imgWidths[bit]+offx>td.x){
            p.y+=offy+fontMetrics.getAscent();
            p.x=offx;
        }
        g2d.drawImage(checkOFFON[bit], p.x, p.y, imgWidths[bit], imgHeights[bit], null);
        p.x+= imgWidths[bit];
        return p;
    }
    
    public class Float{
        int x;
        int y;
        public Float(int x,int y){
            this.x=x;
            this.y=y;
        }
    }
    
}
