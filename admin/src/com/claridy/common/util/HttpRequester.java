package com.claridy.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * HTTP請求對象
 * 
 * @author YYmmiinngg
 */
public class HttpRequester {
	private String defaultContentEncoding;

	public HttpRequester() {
		this.defaultContentEncoding = Charset.defaultCharset().name();
	}

	/**
	 * 發送GET請求
	 * 
	 * @param urlString URL地址
	 * @return 響應物件
	 * @throws IOException
	 */
	public String sendGet(String urlString) throws IOException {
		return this.send(urlString, "GET", null, null);
	}

	/**
	 * 發送GET請求
	 * 
	 * @param urlString URL地址
	 * @param params 參數集合
	 * @return 響應物件
	 * @throws IOException
	 */
	public String sendGet(String urlString, Map<String, String> params)
			throws IOException {
		return this.send(urlString, "GET", params, null);
	}

	/**
	 * 發送GET請求
	 * 
	 * @param urlString URL地址
	 * @param params 參數集合
	 * @param propertys 請求屬性
	 * @return 響應物件
	 * @throws IOException
	 */
	public String sendGet(String urlString, Map<String, String> params,
			Map<String, String> propertys) throws IOException {
		return this.send(urlString, "GET", params, propertys);
	}

	/**
	 * 發送POST請求
	 * 
	 * @param urlString URL地址
	 * @return 響應物件
	 * @throws IOException
	 */
	public String sendPost(String urlString) throws IOException {
		return this.send(urlString, "POST", null, null);
	}

	/**
	 * 發送POST請求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            參數集合
	 * @return 響應物件
	 * @throws IOException
	 */
	public String sendPost(String urlString, Map<String, String> params)
			throws IOException {
		return this.send(urlString, "POST", params, null);
	}

	/**
	 * 發送POST請求
	 * 
	 * @param urlString URL地址
	 * @param params 參數集合
	 * @param propertys 請求屬性
	 * @return 響應物件
	 * @throws IOException
	 */
	public String sendPost(String urlString, Map<String, String> params,
			Map<String, String> propertys) throws IOException {
		return this.send(urlString, "POST", params, propertys);
	}

	/**
	 * 發送HTTP請求
	 * 
	 * @param urlString
	 * @return 響映對象
	 * @throws IOException
	 */
	private String send(String urlString, String method,
			Map<String, String> parameters, Map<String, String> propertys)
			throws IOException {
		if(urlString != null && !"".equals(urlString)){
			HttpURLConnection urlConnection = null;
	
			if (method.equalsIgnoreCase("GET") && parameters != null) {
				StringBuffer param = new StringBuffer();
				int i = 0;
				for (String key : parameters.keySet()) {
					if (i == 0)
						param.append("?");
					else
						param.append("&");
					param.append(key).append("=").append(parameters.get(key));
					i++;
				}
				urlString += param;
			}
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
	
			urlConnection.setRequestMethod(method);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.addRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
	
			if (propertys != null)
				for (String key : propertys.keySet()) {
					urlConnection.addRequestProperty(key, propertys.get(key));
				}
	
			if (method.equalsIgnoreCase("POST") && parameters != null) {
				StringBuffer param = new StringBuffer();
				for (String key : parameters.keySet()) {
					param.append("&");
					param.append(key).append("=").append(parameters.get(key));
				}
				urlConnection.getOutputStream().write(param.toString().getBytes("UTF-8"));
				urlConnection.getOutputStream().flush();
				urlConnection.getOutputStream().close();
			}
	
			return this.makeContent(urlConnection);
		}else{
			return "URL is empty";
		}
	}

	/**
	 * 得到回應物件
	 * 
	 * @param urlConnection
	 * @return 響應物件
	 * @throws IOException
	 */
	private String makeContent(HttpURLConnection urlConnection)
			throws IOException {
		try {
			InputStream in = urlConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in));
			Vector<String> contentCollection = new Vector<String>();
			StringBuffer temp = new StringBuffer();
			String line = bufferedReader.readLine();
			while (line != null) {
				contentCollection.add(line);
				temp.append(line).append("\r\n");
				line = bufferedReader.readLine();
			}
			bufferedReader.close();

			String ecod = urlConnection.getContentEncoding();
			if (ecod == null)
				ecod = this.defaultContentEncoding;
			String content = new String(temp.toString().getBytes(), ecod);
			return content;
		} catch (IOException e) {
			throw e;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

	/**
	 * 默認的回應字元集
	 */
	public String getDefaultContentEncoding() {
		return this.defaultContentEncoding;
	}

	/**
	 * 設置默認的回應字元集
	 */
	public void setDefaultContentEncoding(String defaultContentEncoding) {
		this.defaultContentEncoding = defaultContentEncoding;
	}
	
	public static void main(String[] args) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("url", "www.google.com");
			params.put("title", "建國測試008");
			params.put("owner", "TESTPORTAL003");
			HttpRequester request = new HttpRequester();
			//request.defaultContentEncoding = "UTF-8";
			String hr = request.sendPost("http://203.64.154.30/AdminPortal/addTodolist",params);
			System.out.println("============: "+hr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
