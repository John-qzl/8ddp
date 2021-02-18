package com.cssrc.ibms.util.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *	处理http接口
 * @author zxg
 *
 */
public class HttpClientUtil {

	/**
	 * 功能描述: GET方式请求URL地址. <br>
	 *
	 * @param url
	 *            地址
	 * @return 失败返回null
	 * @throws Exception
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static String httpGet(String url) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.content-charset",
				"UTF-8");

		HttpGet httpGet = new HttpGet(url);

		HttpResponse response;
		String responseString;
		try {
			response = client.execute(httpGet);
			responseString = EntityUtils
					.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// 如果是 200 OK 的话输出
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return responseString;
		}
		return null;
	}
	
	
	/**
	 * 获取文件流
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static byte[] httpGetByte(String url) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.content-charset",
				"UTF-8");
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				byte[] by=EntityUtils.toByteArray(response.getEntity());
				return by;
			}else{
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 功能描述: GET方式请求URL地址. <br>
	 *
	 * @param url
	 *            地址
	 * @return 失败返回null
	 * @throws Exception
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static String httpPost(String url) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.content-charset",
				"UTF-8");

		HttpPost httpPost = new HttpPost(url);

		HttpResponse response;
		String responseString;
		try {
			response = client.execute(httpPost);
			EntityUtils.toByteArray(response.getEntity());
			
			responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			throw new Exception(e);
		}

		// 如果是 200 OK 的话输出
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return responseString;
		}
		return null;
	}

}
