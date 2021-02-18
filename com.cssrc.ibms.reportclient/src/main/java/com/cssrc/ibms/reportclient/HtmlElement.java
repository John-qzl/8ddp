package com.cssrc.ibms.reportclient;

public class HtmlElement
{

    private String type;
    
    private String text;
    
    private int selected;
    
    public HtmlElement(String type, int selected, String text)
    {
        this.type = type;
        this.text = text;
        this.selected = selected;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public int getSelected()
    {
        return selected;
    }

    public void setSelected(int selected)
    {
        this.selected = selected;
    }
    

}
