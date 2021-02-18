package com.cssrc.ibms.reportclient.MoneyUtil;

import com.fr.script.AbstractFunction;

public class MoneyFomat extends AbstractFunction
{
    private static final long serialVersionUID = -6558663614442375417L;
    
    public final static String[] CN_NUMBER={"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
    
    @Override
    public Object run(Object[] param)
    {
        //"1021312324".charAt("1021312324".length()-3);
        if(param.length<2){
            return "参数不匹配";
        }else{
            String money=param[0].toString();
            Integer point=Integer.parseInt(param[1].toString());
            char pv;
            if(point<0){
                String intv=money.substring(money.indexOf(".") + 1, money.length());
                try{
                    pv=intv.charAt(Math.abs(point)-1);
                }catch(Exception e){
                    return CN_NUMBER[0];
                }
            }else{
            	int length = money.indexOf(".");
            	if(length == -1){
            		length = money.length();
            	}
                String intv=money.substring(0, length);
                if(intv.length()>=point){
                    pv=intv.charAt(intv.length()-point);
                }else{
                    return CN_NUMBER[0];
                }
            }
            return CN_NUMBER[Integer.valueOf(String.valueOf(pv))];
        }
        
    }
  
    
    public static void main(String[] args){
        MoneyFomat mf=new MoneyFomat();
        String[] parmas=new String[2];
        String money="1021312324";
        for(int i=money.length(),p=1;i>0;i--){
            parmas[0]=money;
            parmas[1]=String.valueOf(p);
            System.out.println(mf.run(parmas));
            p++;
        }
        parmas[0]=money;
        parmas[1]=String.valueOf(100);
        System.out.println(mf.run(parmas));
        
        parmas[0]=money;
        parmas[1]=String.valueOf(-1);
        System.out.println(mf.run(parmas));
        
    }
    
}
