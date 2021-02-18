package com.cssrc.ibms.dp.util;
import java.io.*;

public class HtmlToPdfInterceptor extends Thread {
    private InputStream is;

    public HtmlToPdfInterceptor(InputStream is){
        this.is = is;
    }

    public void run(){
        try{
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line.toString()); //输出内容
            }
            //关闭文件流
            br.close();
            isr.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}