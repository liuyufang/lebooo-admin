package com.lebooo.admin.service.lebooo;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.mapper.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient工具类.
 * @author liuwei
 */
public class SimpleHttpClient {
	protected static Logger logger = LoggerFactory.getLogger(SimpleHttpClient.class);

	protected HttpClient httpClient = null;

	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

	/**
	 * 发送信息默认使用UTF-8编码.
	 */
	protected String urlEncoding = "UTF-8";

	/**
	 * 构造函数.
	 */
	public SimpleHttpClient() {
		buildHttpClient();
	}

	/**
	 * @param urlEncoding 发送信息使用的编码,如果不设置则默认使用UTF-8.
	 */
	public SimpleHttpClient(String urlEncoding) {
		this.urlEncoding = urlEncoding;
		buildHttpClient();
	}

	/**
	 * 创建线程安全的HttpClient.
	 */
	protected void buildHttpClient() {
		// Create and initialize HTTP parameters
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 100);
		ConnManagerParams.setTimeout(params, 3000);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		// Create and initialize scheme registry 
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		// Create an HttpClient with the ThreadSafeClientConnManager.
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
		httpClient = new DefaultHttpClient(cm, params);
	}

	@SuppressWarnings("cast")
	public <T> T get(String url, Class<T> clazz) {
		return (T) jsonMapper.fromJson(get(url), clazz);
	}

	public String get(String url, Map<String, Object> params) {
		if (params == null || params.entrySet().size() == 0) {
			return get(url);
		}
		String urlWithParams = urlParams(url, params);
		return get(urlWithParams);
	}

	/**
	 * GET.
	 */
	public String get(String url) {
		HttpGet httpGet = new HttpGet(url);
		HttpContext context = new BasicHttpContext();
		try {
			HttpResponse response = httpClient.execute(httpGet, context);
			checkStatus(response);
			HttpEntity httpEntiry = response.getEntity();
			return getResponseText(httpEntiry);
		} catch (Exception e) {
			logger.error("fetch remote content" + url + "  error", e);
			httpGet.abort();
			return null;
		}
	}

	/**
	 * @param url
	 * @param data 发送的数据，可以为null
	 * @return Response Body，出现异常时返回 null.
	 */
	public String postPlainText(String url, String data) {
		HttpPost httpPost = new HttpPost(url);
		HttpContext context = new BasicHttpContext();
		try {
			if (data != null) {
				HttpEntity stringEntity = new StringEntity(data, urlEncoding);
				httpPost.setEntity(stringEntity);
			}
			HttpResponse response = httpClient.execute(httpPost, context);
			checkStatus(response);
			HttpEntity httpEntiry = response.getEntity();
			return getResponseText(httpEntiry);
		} catch (Exception e) {
			logger.error("fetch remote content" + url + "  error", e);
			httpPost.abort();
			return null;
		}
	}

	/**
	 * @param url
	 * @param params 键: 字符串，值: 数字、字符串、布尔值、数组、List 
	 * @return Response Body，响应 4xx 或 5xx 时返回 null.
	 */
	public String postForm(String url, Map<String, Object> params) {
		HttpPost httpPost = new HttpPost(url);
		HttpContext context = new BasicHttpContext();

		try {
			if (params != null && params.entrySet().size() > 0) {
				List<NameValuePair> nameValuePairList = toNameValuePairList(params);
				HttpEntity requestHttpEntity = new UrlEncodedFormEntity(nameValuePairList, urlEncoding);
				httpPost.setEntity(requestHttpEntity);
			}
			HttpResponse response = httpClient.execute(httpPost, context);
			checkStatus(response);
			HttpEntity responseHttpEntiry = response.getEntity();
			String responseText = getResponseText(responseHttpEntiry);
			return responseText;

		} catch (Exception e) {
			logger.error("fetch remote content " + url + "  error", e);
			httpPost.abort();
			return null;
		}
	}

	/**
	 * 检查响应状态代码,如果是4xx或5xx则抛出异常.
	 */
	protected void checkStatus(HttpResponse response) throws IllegalStateException, IOException {
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		// 4xx,5xx抛出异常
		if (statusCode >= 400 && statusCode < 600) {
			if (statusCode >= 400 && statusCode < 500) {
				throw new RuntimeException(statusCode + ":" + getResponseText(response.getEntity()));
			}
			if (statusCode >= 500 && statusCode < 600) {
				throw new RuntimeException(statusCode + ":" + getResponseText(response.getEntity()));
			}
		}
	}

	//-- 工具函数 --//
	/**
	 * 将参数Map转化为键值对(NameValuePair)数组.
	 * 参数格式:
	 * key: 字符串
	 * value: 基本数据类型, List, 或基本数据类型数组. 
	 */
	@SuppressWarnings("rawtypes")
	protected List<NameValuePair> toNameValuePairList(Map<String, Object> map) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		if (map != null && map.entrySet().size() > 0) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				Object v = entry.getValue();

				if (key != null && v != null) {
					// 多值
					if (v instanceof List || v instanceof Object[]) {
						Object[] values;
						if (v instanceof List) {
							values = ((List) v).toArray();
						} else {
							values = (Object[]) v;
						}
						for (int i = 0; i < values.length; i++) {
							Object value = values[i];
							if (value != null) {
								params.add(new BasicNameValuePair(key, value.toString()));
							}
						}
						// 一个值
					} else {
						params.add(new BasicNameValuePair(key, v.toString()));
					}
				}
			}// end for map.entrySet()
		}

		return params;
	}

	/**
	 * 将参数Map转化为查询字符串.
	 * 参数格式:
	 * key: 字符串
	 * value: 基本数据类型, 枚举类型, List, 或基本数据类型数组. 
	 */
	@SuppressWarnings("rawtypes")
	String urlParams(String url, Map<String, Object> params) {
		if (params == null || params.entrySet().size() == 0) {
			return url;
		}
		StringBuilder sb = new StringBuilder();
		// 将Map转成查询字符串.
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();
			Object v = entry.getValue();
			if (key != null && v != null) {
				if (v instanceof List || v instanceof Object[]) {
					Object[] values;
					if (v instanceof List) {
						values = ((List) v).toArray();
					} else {
						values = (Object[]) v;
					}
					for (Object value : values) {
						if (value != null) {
							sb.append(urlEncode(key) + "=" + urlEncode(value.toString()) + "&");
						}
					}
				} else {
					sb.append(urlEncode(key) + "=" + urlEncode(v.toString()) + "&");
				}
			}
		}
		// 去掉结尾的"&"
		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '&') {
			sb.deleteCharAt(sb.length() - 1);
		}

		if (sb.length() == 0) {
			return url;
		}

		String result;
		if (url.contains("?")) {
			result = url + "&" + sb.toString();
		} else {
			result = url + "?" + sb.toString();
		}
		return result;
	}

	/**
	 * 从HttpEntity中获取内容,自动探测编码.
	 * @throws java.io.IOException
	 * @throws IllegalStateException 
	 */
	protected String getResponseText(HttpEntity entity) throws IllegalStateException, IOException {
		if (entity == null) {
			throw new IllegalArgumentException("HttpEntity can not be null.");
		}
		InputStream input = entity.getContent();
		String content = CharsetDetector.getContentAndClose(input);
		return content;
	}

	/**
	 * URL 编码. 
	 */
	protected String urlEncode(String input) {
		try {
			return URLEncoder.encode(input, urlEncoding);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding Exception", e);
		}
	}

	/**
	 * 销毁,释放资源.
	 */
	public void destroy() {
		if (httpClient != null) {
			httpClient.getConnectionManager().shutdown();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		destroy();
	}

    public static void main(String[] args) {
        SimpleHttpClient client = new SimpleHttpClient();
        String html = client.get("http://www.baidu.com");
        System.out.println(html);
    }
}
