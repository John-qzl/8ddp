package com.cssrc.ibms.core.flow.status;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "statu")
@XmlAccessorType(XmlAccessType.NONE)
public class FlowStatus
{
    @XmlAttribute
    private int key;
    @XmlAttribute
    private String text;
    @XmlAttribute
    private String color;
    @XmlAttribute
    private String textColor;
    @XmlAttribute
    private boolean showbar;
    @XmlAttribute
    private String desc;

    public int getKey()
    {
        return key;
    }

    public void setKey(int key)
    {
        this.key = key;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getTextColor()
    {
        return textColor;
    }

    public void setTextColor(String textColor)
    {
        this.textColor = textColor;
    }

    public boolean isShowbar()
    {
        return showbar;
    }

    public void setShowbar(boolean showbar)
    {
        this.showbar = showbar;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getTextHtml()
    {
        return "<font color='"+this.textColor+"'>"+this.text+"</font>";
    }
    
    
}
