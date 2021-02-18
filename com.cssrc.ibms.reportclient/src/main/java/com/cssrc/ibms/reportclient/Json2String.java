package com.cssrc.ibms.reportclient;

import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fr.general.FArray;
import com.fr.script.AbstractFunction;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ResourceBundle;
import com.cssrc.ibms.reportutil.FileUtil;
import com.cssrc.ibms.util.Coder;
import com.cssrc.ibms.util.http.HttpClientUtil;
import com.fr.base.AbstractPainter;
import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.base.Style;
import com.fr.general.FRFont;

public class Json2String extends AbstractFunction
{
    
    
    public static String CHECK_ON = "/com/fr/web/images/checkon.gif";
    
    public static String CHECK_OFF = "/com/fr/web/images/checkoff.gif";
    
    public static String RADIO_ON = "/com/fr/web/images/radioon.gif";
    
    public static String RADIO_OFF = "/com/fr/web/images/radiooff.gif";
    
    public static String IMG_BASE64 = "data:image/png;base64,";
    
    private static final long serialVersionUID = 5750615746226800882L;
    @Override
    public Object run(Object[] json)
    {
        String str=json[0].toString();
        String key=json[1].toString();
        int fontSize=Integer.parseInt(json.length==3?json[2].toString():"16");

        try{
        	if(str.length() < 4){
        		return "";
        	}
            Map<String,String> map=JSONObject.parseObject(str, new TypeReference<Map<String,String>>(){});
            String val=map.get(key);
            if(val==null){
                return str;
            }else{
                if("opinion".equals(key)){
                    //return this.replaceHtml(val).replaceAll("\n", "");
                    FArray<HtmlElement> elements= this.parseHtml(val);
                    if(elements.length() < 1){
                    	return val;
                    }else{
                    	return new WidgetPaint(elements,fontSize);
                    }
                }else if(key!=null&&!"".equals(key)){
                    return val;
                }else{
                    return str;
                }
            }
            
        }catch(Exception e){
            return e.getMessage();
        }
       
    }
    /*// 第一个参数：控件类型，不区分大小写
    String type = "checkbox";
    // 第二个参数：控件按钮个数
    int num = 4;
    
    // 第三个参数：按钮组的值，哪些被选中
    String selection = "0110";
    // 第四个参数:可选参数，按钮组对应的显示值数组
    FArray<String> textArray = new FArray<String>();
    textArray.add("同意");
    textArray.add("同意");
    textArray.add("同意");
    textArray.add("同意");
    FArray result_ = new FArray();
    return result_.add(new WidgetPaint(type, num, selection, textArray));*/
    //return this.replaceHtml(opinion.trim());

    
    /**
     * 将审批意见全部解析成一张图片，图片包含checkbox 以及文本内容 这种形式能保证内容的展示，无法保证审批意见换行的格式
     * @param opinion
     * @return
     */
    public FArray<HtmlElement> parseHtml(String opinion)
    {
        FArray<HtmlElement> result = new FArray<HtmlElement>();
        Document doc = Jsoup.parse(opinion);
        String[] htmls= doc.getElementsByTag("body").html().split("\n");
        Elements elements = doc.getAllElements();
        for (Element element : elements)
        {
            if (element.tagName().equals("input"))
            {
                String etype = element.attr("type");
                if (etype.equals("checkbox") || etype.equals("radio"))
                {
                    if ("checked".equals(element.attr("checked")))
                    {
                        result.add(new HtmlElement(etype, 1, element.html().trim()));
                    }
                    else
                    {
                        result.add(new HtmlElement(etype, 0, element.html().trim()));
                    }
                }else if("text".equals(etype)){
                    result.add(new HtmlElement(etype, 0, element.val().trim()));
                }
            }
            else if (element.tagName().equals("t"))
            {
                result.add(new HtmlElement(element.tagName(), 0, element.html().trim()));
            }else if (element.tagName().equals("textarea"))
            {
                result.add(new HtmlElement(element.tagName(), 0, element.html().trim()));
            }
            else if (element.tagName().equals("label"))
            {
                result.add(new HtmlElement(element.tagName(), 0, element.html().trim()));
            }
            else if (element.tagName().equals("select"))
            {
                List<Node> childs = element.childNodes();
                for (Node child : childs)
                {
                    if ("selected".equals(child.attr("selected")))
                    {
                        result.add(new HtmlElement("text", 0, child.childNode(0).toString()));
                    }
                }
            }
            else if (element.tagName().equals("br"))
            {
                result.add(new HtmlElement(element.tagName(), 0, element.html().trim()));
            }
        }
        return result;
    }
    
    
    
    /**
     * 将审批意见中的checkbox 以及 radio 替换成图片，图片以base64为字节码展示 保证审批意见的格式，无法保证图片展示
     * 
     * @param opinion
     * @return
     */
    public String replaceHtml(String opinion)
    {
        Document doc = Jsoup.parse(opinion);
        Elements checks = doc.select("input[type=checkbox]");
        for (Element ch : checks)
        {
            String checked = ch.attr("checked");
            int bit = "checked".equals(checked) ? 1 : 0;
            Element img = doc.createElement("img");
            // img.attr("src", IMG_BASE64 + this.readImg("checkbox", bit));
            img.attr("src", this.getImg("checkbox", bit));
            ch.replaceWith(img);
        }
        Elements radios = doc.select("input[type=radio]");
        for (Element radio : radios)
        {
            String checked = radio.attr("checked");
            int bit = "checked".equals(checked) ? 1 : 0;
            Element img = doc.createElement("img");
            // img.attr("src", IMG_BASE64 + this.readImg("radio", bit));
            img.attr("src", this.getImg("radio", bit));
            
            radio.replaceWith(img);
        }
        Elements selects = doc.select("select");
        for (Element select : selects)
        {
            List<Node> options = select.childNodes();
            for (Node option : options)
            {
                if ("selected".equals(option.attr("selected")))
                {
                    Element text = doc.createElement("text");
                    text.html(option.childNode(0).toString().trim());
                    select.replaceWith(text);
                }
            }
            
        }
        return doc.select("body").html().trim();
    }
    
    /**
     * 读取图片base64位字节码
     * 
     * @param type
     * @param bit
     * @return
     */
    public String readImg(String type, int bit)
    {
        String OFF = CHECK_OFF;
        String ON = CHECK_ON;
        if ("radio".equals(type))
        {
            OFF = RADIO_OFF;
            ON = RADIO_ON;
        }
        String[] checkOFFON = {OFF, ON};
        BaseUtils.readResource(checkOFFON[bit]);
        byte[] b = FileUtil.readByte(BaseUtils.readResource(checkOFFON[bit]));
        try
        {
            return Coder.encryptBASE64(b);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取图片映射路径
     * 
     * @param type
     * @param bit
     * @return
     */
    public String getImg(String type, int bit)
    {
        String reportServer = this.getReportServer();
        
        String CHECK_ON = reportServer + "/image/sys_checkbox_no.png";
        
        String CHECK_OFF = reportServer + "/image/sys_checkbox_yes.png";
        
        String RADIO_ON = reportServer + "/image/sys_radiobtn_no.png";
        
        String RADIO_OFF = reportServer + "/image/sys_radiobtn_yes.png";
        String OFF = CHECK_OFF;
        String ON = CHECK_ON;
        if ("radio".equals(type))
        {
            OFF = RADIO_OFF;
            ON = RADIO_ON;
        }
        String[] checkOFFON = {OFF, ON};
        return checkOFFON[bit];
    }
    
    /**
     * 获取报表服务器地址
     * 
     * @return
     */
    public String getReportServer()
    {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("ibms-server");
        String reportServer = resourceBundle.getString("ibms.reportserver.url");
        return reportServer;
    }
    
    /*public static class WidgetPaint extends AbstractPainter {
        public static String CHECK_ON = "/com/fr/web/images/checkon.gif";
        public static String CHECK_OFF = "/com/fr/web/images/checkoff.gif";
        public static String RADIO_ON = "/com/fr/web/images/radioon.gif";
        public static String RADIO_OFF = "/com/fr/web/images/radiooff.gif";
        public static FRFont DEFUALT_FONT = FRFont.getInstance();
        public static FontMetrics FontMetrics = GraphHelper
                .getFontMetrics(DEFUALT_FONT);
        private String type;
        private int num;
        private String selection;
        private FArray textArray;
        {
            DEFUALT_FONT = DEFUALT_FONT.applyForeground(Color.BLACK);
        }
        public WidgetPaint(String type, int num, String selection,
                FArray textArray) {
            this.type = type;
            this.num = num;
            this.selection = selection;
            this.textArray = textArray;
        }

        private String resolveText(int i) {
            if (i < this.textArray.length()) {
                return this.textArray.elementAt(i).toString();
            }
            return null;
        }
        public void paint(Graphics g, int width, int height, int resolution,
                Style style) {
            String OFF = CHECK_OFF;
            String ON = CHECK_ON;
            if ("radiobutton".equals(type)) {
                OFF = RADIO_OFF;
                ON = RADIO_ON;
            }
            Image[] checkOFFON = { BaseUtils.readImage(OFF),
                    BaseUtils.readImage(ON) };
            int[] imgWidths = { checkOFFON[0].getWidth(null),
                    checkOFFON[1].getWidth(null) };
            int[] imgHeights = { checkOFFON[0].getHeight(null),
                    checkOFFON[1].getHeight(null) };
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(FRFont.getInstance());
            g2d.setPaint(Color.BLACK);
            int x = 2;
            int y = (height - imgHeights[0]) / 2;
            String select = selection;
            for (int i = 0; i < num; i++) {
                int bit = Integer.parseInt(select.substring(i, i + 1));
                g2d.drawImage(checkOFFON[bit], x, y, imgWidths[bit],
                        imgHeights[bit], null);
                x += imgWidths[bit] + 2;
                String text = resolveText(i);
                g2d.setBackground(Color.BLACK);
                g2d.drawString(text, (float) x, (float) (y + FontMetrics
                        .getAscent()));
                x += FontMetrics.stringWidth(text) + 2;
            }
        }
    }*/
}
