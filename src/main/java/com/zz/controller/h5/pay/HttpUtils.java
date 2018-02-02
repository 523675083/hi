package com.zz.controller.h5.pay;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;

import net.sf.json.JSONObject;

/**
 *
 * <p>Title: </p>
 * <p>Description: http utils </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author LiLu
 * @version 1.0
 */
public class HttpUtils {

 
  private static final String URL_PARAM_CONNECT_FLAG = "&";
  private static final int SIZE 	= 1024 * 1024;
  private static Log log = LogFactory.getLog(HttpUtils.class);
  
  private HttpUtils() {
  }

  /**
   * GET METHOD
   * @param strUrl String
   * @param map Map
   * @throws IOException
   * @return List
   */
  public static List URLGet(String strUrl, Map map) throws IOException {
    String strtTotalURL = "";
    List result = new ArrayList();
    if(strtTotalURL.indexOf("?") == -1) {
      strtTotalURL = strUrl + "?" + getUrl(map);
    } else {
      strtTotalURL = strUrl + "&" + getUrl(map);
    }
    log.debug("strtTotalURL:" + strtTotalURL);
    System.out.println("strtTotalURL="+strtTotalURL);
    URL url = new URL(strtTotalURL);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setUseCaches(false);
    con.setFollowRedirects(true);
    BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()),SIZE);
    while (true) {
      String line = in.readLine();
      if (line == null) {
        break;
      }
      else {
    	  result.add(line);
      }
    }
    in.close();
    return (result);
  }

  /**
   * POST METHOD
   * @param strUrl String
   * @param content Map
   * @throws IOException
   * @return List
   */
  public static List URLPost(String strUrl, Map map) throws IOException {

    String content = "";
    content = getUrl(map);
    String totalURL = null;
    if(strUrl.indexOf("?") == -1) {
      totalURL = strUrl + "?" + content;
    } else {
      totalURL = strUrl + "&" + content;
    }

	System.out.println("totalURL : " + totalURL);

    URL url = new URL(strUrl);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setDoInput(true);
    con.setDoOutput(true);
    con.setAllowUserInteraction(false);
    con.setUseCaches(false);
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=GBK");
    BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(con.
        getOutputStream()));
    bout.write(content);
    bout.flush();
    bout.close();
    BufferedReader bin = new BufferedReader(new InputStreamReader(con.
        getInputStream()),SIZE);
    List result = new ArrayList(); 
    while (true) {
      String line = bin.readLine();
      if (line == null) {
        break;
      }
      else {
    	  result.add(line);
      }
    }
    return (result);
  }

  /**
   * ���URL
   * @param map Map
   * @return String
   */
  private static String getUrl(Map map) {
    if (null == map || map.keySet().size() == 0) {
      return ("");
    }
    StringBuffer url = new StringBuffer();
    Set keys = map.keySet();
    for (Iterator i = keys.iterator(); i.hasNext(); ) {
      String key = String.valueOf(i.next());
      if (map.containsKey(key)) {
    	 Object val = map.get(key);
    	 String str = val!=null?val.toString():"";
    	 try {
			str = URLEncoder.encode(str, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        url.append(key).append("=").append(str).
            append(URL_PARAM_CONNECT_FLAG);
      }
    }
    String strURL = "";
    strURL = url.toString();
    if (URL_PARAM_CONNECT_FLAG.equals("" + strURL.charAt(strURL.length() - 1))) {
      strURL = strURL.substring(0, strURL.length() - 1);
    }
    return (strURL);
  }
  
  public static HttpPost getPostMethod(String url)
  {
    HttpPost pmethod = new HttpPost(url);
    pmethod.addHeader("Connection", "keep-alive");
    pmethod.addHeader("Accept", "*/*");
    pmethod.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    pmethod.addHeader("Host", "api.mch.weixin.qq.com");
    pmethod.addHeader("X-Requested-With", "XMLHttpRequest");
    pmethod.addHeader("Cache-Control", "max-age=0");
    pmethod.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
    return pmethod;
  }

  public static HttpClient getSSLInstance(HttpClient httpClient)
  {
    ClientConnectionManager ccm = httpClient.getConnectionManager();
    SchemeRegistry sr = ccm.getSchemeRegistry();
    sr.register(new Scheme("https", MySSLSocketFactory.getInstance(), 443));
    httpClient = new DefaultHttpClient(ccm, httpClient.getParams());
    return httpClient;
  }
  
  /**
	 * 获取本机的ip
	 * @return
	 */
	public static String localIp()
	  {
	    String ip = null;
	    try
	    {
	      Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
	      while (allNetInterfaces.hasMoreElements())
	      {
	        NetworkInterface netInterface = (NetworkInterface)allNetInterfaces
	          .nextElement();

	        List<InterfaceAddress> InterfaceAddress = netInterface
	          .getInterfaceAddresses();
	        for (InterfaceAddress add : InterfaceAddress) {
	          InetAddress Ip = add.getAddress();
	          if ((Ip != null) && ((Ip instanceof Inet4Address)))
	            ip = Ip.getHostAddress();
	        }
	      }
	    }
	    catch (SocketException e) {
	      log.error("获取本机Ip失败", e);
	    }
	    return ip;
	  }
	
	
  
}

