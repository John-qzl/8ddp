package com.cssrc.ibms.core.util.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;

/**
 * 处理http接口
 * 
 * @author zxg
 *
 */
public class HttpClientUtil
{
    protected static Logger logger = Logger.getLogger("asset");
    
    /**
     * 功能描述: GET方式请求URL地址. <br>
     *
     * @param url 地址
     * @return 失败返回null
     * @throws Exception
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String httpGet(String url)
        throws Exception
    {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        
        HttpGet httpGet = new HttpGet(url);
        
        HttpResponse response;
        String responseString;
        try
        {
            response = client.execute(httpGet);
            responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
        // 如果是 200 OK 的话输出
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
        {
            return responseString;
        }
        return null;
    }
    
    /**
     * 获取文件流
     * 
     * @param url
     * @return
     * @throws Exception
     */
    public static byte[] httpGetByte(String url)
        throws Exception
    {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response;
        try
        {
            response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                byte[] by = EntityUtils.toByteArray(response.getEntity());
                return by;
            }
            else
            {
                return null;
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 功能描述: GET方式请求URL地址. <br>
     *
     * @param url 地址
     * @return 失败返回null
     * @throws Exception
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String httpPost(String url)
        throws Exception
    {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        
        HttpPost httpPost = new HttpPost(url);
        
        HttpResponse response;
        String responseString;
        try
        {
            response = client.execute(httpPost);
            EntityUtils.toByteArray(response.getEntity());
            
            responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        }
        catch (Exception e)
        {
            throw new Exception(e);
        }
        
        // 如果是 200 OK 的话输出
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
        {
            return responseString;
        }
        return null;
    }
    
    /**
     * 调用 API
     * 
     * @param parameters
     * @return
     * @throws Exception
     */
    public static JSONObject jsonPost(String url, Map<String, String> parameters)
        throws Exception
    {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;
        try
        {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            for (Map.Entry<String, String> entry : parameters.entrySet())
            {
                if (entry.getValue() != null)
                {
                    params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            // 添加参数
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            // 设置编码
            logger.info("send trade post request");
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info(" get status code:" + statusCode);
            if (statusCode != HttpStatus.SC_OK)
            {
                return null;
            }
            // Read the response body
            String body = EntityUtils.toString(response.getEntity());
            jsonObject = JSONArray.parseObject(body);
            logger.info("get return body:" + jsonObject.toString());
        }
        catch (IOException e)
        {
            // 网络错误
            throw new Exception(e);
        }
        finally
        {
            
        }
        return jsonObject;
    }
    
    public static JSONObject httpsPost(String url, int port, Map<String, String> map)
        throws Exception
    {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        JSONObject result = null;
        try
        {
            httpClient = new SSLClient(port);
            httpPost = new HttpPost(url);
            // 设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator<?> iterator = map.entrySet().iterator();
            while (iterator.hasNext())
            {
                Entry<String, String> elem = (Entry<String, String>)iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0)
            {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
                httpPost.setEntity(entity);
            }
            logger.info("send trade post request");
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info(" get status code:" + statusCode);
            if (statusCode != HttpStatus.SC_OK)
            {
                return null;
            }
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null)
            {
                String body = EntityUtils.toString(resEntity, "UTF-8");
                result = JSONArray.parseObject(body);
                logger.info("get return body:" + result.toString());
            }
            else
            {
                logger.info("response  entity is null");
            }
        }
        catch (Throwable e)
        {
            throw new Exception(e);
        }
        return result;
    }
    
    // 功能: postBody形式发送数据
    // @param urlPath 对方地址
    // @param json 要传送的数据
    // @return
    // @throws Exception
    public static String postBody(String urlPath, String json)
        throws Exception
    {
        try
        {
            // Configure and open a connection to the site you will send the request
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            // 设置doOutput属性为true表示将使用此urlConnection写入数据
            urlConnection.setDoOutput(true);
            // 定义待写入数据的内容类型，我们设置为application/x-www-form-urlencoded类型
            urlConnection.setRequestProperty("content-type", "text/html;charset=UTF-8");
            // 得到请求的输出流对象
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            // 把数据写入请求的Body
            out.write(json);
            out.flush();
            out.close();
            
            // 从服务器读取响应
            InputStream inputStream = urlConnection.getInputStream();
            String encoding = urlConnection.getContentEncoding();
            String body = IOUtils.toString(inputStream, encoding);
            if (urlConnection.getResponseCode() == 200)
            {
                return body;
            }
            else
            {
                throw new Exception(body);
            }
        }
        catch (IOException e)
        {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
    
}
