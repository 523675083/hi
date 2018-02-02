package com.zz.controller.h5.pay;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class H5WxUtils {
	public static final DefaultHttpClient httpclient = (DefaultHttpClient)HttpUtils.getSSLInstance(new DefaultHttpClient());
	

	public static String getPayURL(Map<String,String> params,String key){
	    //生成签名
		String sign = createSign(params,key);
	    //log.info("生成签名的参数：{}",params);
	    //log.info("签名：{}",sign);
	    params.put("sign", sign);
	    
		String requestXML=createWxScanRequestXML(params);
		//统一下单接口
		String wxScanPayURL = H5WxConstant.UNIFY_ORDER;
		Map<String, String> return_url = getCodeUrl(wxScanPayURL, requestXML);
		String return_code=return_url.get("return_code");
		if(StringUtil.isEmpty(return_code)||!"SUCCESS".equals(return_code)){
			//log.info("====h5微信支付错误return_code="+return_code+"===========================");
			return "";
		}else{
			//String return_msg =return_url.get("return_msg");
				String result_code=return_url.get("result_code");
				if(result_code==null){
					result_code="";
				}
				if("SUCCESS".equals(result_code)&&"SUCCESS".equals(return_code)){
//					String prepay_id=return_url.get("prepay_id");
//					return "prepay_id="+prepay_id;
					String code_url=return_url.get("code_url");
					return code_url;
				}else{
					String err_code=return_url.get("err_code");
					//log.info("===h5微信支付错误=err_code="+err_code+"===================================");
				}
		}
		return "";
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
      //log.error("获取本机Ip失败", e);
    }
    return ip;
  }

/**
 * 获取签名
 * @return
 */
public static String createSign(Map<String,String> params,String key){
	
	ArrayList<String> list = new ArrayList<String>();
    for(Map.Entry<String,String> entry:params.entrySet()){
        if(entry.getValue()!=""){
            list.add(entry.getKey() + "=" + entry.getValue() + "&");
        }
    }
    int size = list.size();
    String [] arrayToSort = list.toArray(new String[size]);
    Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < size; i ++) {
        sb.append(arrayToSort[i]);
    }
    String result = sb.toString();
    result += "key=" + key;
    String sign = MD5Utils.sign(result, "", "UTF-8").toUpperCase();
    return sign;
}



/**
 * 组装报文
 * @param paramMap
 * @return
 */
 public static String createWxScanRequestXML(Map<String, String> paramMap)
  {
    StringBuffer returnXML = new StringBuffer();
    
    returnXML.append("<xml><appid>");
    returnXML.append((String)paramMap.get("appid"));
    returnXML.append("</appid>");
    if (StringUtils.isNotEmpty((String)paramMap.get("attach")))
    {
      returnXML.append("<attach>");
      returnXML.append((String)paramMap.get("attach"));
      returnXML.append("</attach>");
    }
    
    returnXML.append("<body>");
    returnXML.append((String)paramMap.get("body"));
    returnXML.append("</body>");
    
    returnXML.append("<mch_id>");
    returnXML.append((String)paramMap.get("mch_id"));
    returnXML.append("</mch_id>");
    
    returnXML.append("<nonce_str>");
    returnXML.append((String)paramMap.get("nonce_str"));
    returnXML.append("</nonce_str>");
    
    returnXML.append("<notify_url>");
    returnXML.append((String)paramMap.get("notify_url"));
    returnXML.append("</notify_url>");
    
//    returnXML.append("<openid>");
//    returnXML.append((String)paramMap.get("openid"));
//    returnXML.append("</openid>");
    
    returnXML.append("<out_trade_no>");
    returnXML.append((String)paramMap.get("out_trade_no"));
    returnXML.append("</out_trade_no>");
    
    returnXML.append("<spbill_create_ip>");
    returnXML.append((String)paramMap.get("spbill_create_ip"));
    returnXML.append("</spbill_create_ip>");
    
    returnXML.append("<total_fee>");
    returnXML.append((String)paramMap.get("total_fee"));
    returnXML.append("</total_fee>");
    
    returnXML.append("<trade_type>");
    returnXML.append((String)paramMap.get("trade_type"));
    returnXML.append("</trade_type>");
    
    returnXML.append("<sign>");
    returnXML.append((String)paramMap.get("sign"));
    returnXML.append("</sign></xml>");
    
    return returnXML.toString();
  }
 
 
 public static Map<String, String> getCodeUrl(String url, String xmlParam)
 {
	    Map<String, String> returnMap = null;
	    
	    DefaultHttpClient client = new DefaultHttpClient();
	    client.getParams().setParameter("http.protocol.allow-circular-redirects", Boolean.valueOf(true));
	    HttpPost httpost = HttpUtils.getPostMethod(url);
	    try
	    {
	      httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
	      HttpResponse response = httpclient.execute(httpost);
	      String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
	      returnMap = SimpleXMLParseUtil.doXMLParse(jsonStr);
	    }
	    catch (Exception e)
	    {
	      returnMap.put("return_code", "FAIL");
	      returnMap.put("return_msg", e.getMessage());
	    }
	    return returnMap;
 }
 
 /** 
  * map转成xml 
  *  
  * @param arr 
  * @return 顺序有问题 
  */  
 public static String ArrayToXml(Map<String, Object> parm, boolean isAddCDATA) {  
     StringBuffer strbuff = new StringBuffer("<xml>");  
     if (parm != null && !parm.isEmpty()) {  
         for (Entry<String, Object> entry : parm.entrySet()) {  
             strbuff.append("<").append(entry.getKey()).append(">");  
             if (isAddCDATA) {  
                 strbuff.append("<![CDATA[");  
                 if (!StringUtil.isEmpty(entry.getValue())) {  
                     strbuff.append(entry.getValue());  
                     }  
                 strbuff.append("]]>");  
             } else {  
                 if (!StringUtil.isEmpty(entry.getValue())) {  
                     strbuff.append(entry.getValue());  
                     }  
                 }  
             strbuff.append("</").append(entry.getKey()).append(">");  
         }  
     }  
     return strbuff.append("</xml>").toString();  
 } 
 
 public static void main(String[] args) {
	Map<String,Object> map = new HashMap<String, Object>();  
     map.put("return_code", "SUCCESS");  
     map.put("return_msg", "OK"); 
     String aString=ArrayToXml(map,true);
     System.out.println(aString);
}
 
//转换十六进制
	public static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
