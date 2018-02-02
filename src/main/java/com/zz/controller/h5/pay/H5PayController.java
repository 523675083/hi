package com.zz.controller.h5.pay;




import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zz.controller.h5.WechatContent;
import com.zz.controller.h5.WechatToken;
import com.zz.service.MemcachedManager;
//import com.zz.service.RedisCacheUtil;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;

@Controller
@RequestMapping("/h5Pay")
public class H5PayController {
	
	
	private String appid;
	private String appsecret;
	private String mch_id;
	private String key;
	private String server_name;
	private String notify_url;
	//@Autowired
	//private RedisCacheUtil<Object> redisClient;
	@Autowired
	private MemcachedManager memcachedManager;
	
	
	

	public H5PayController() {
		 ResourceBundle res = ResourceBundle.getBundle("H5Wx");
		 appid=res.getString("appid");
		 appsecret=res.getString("appsecret");
		 mch_id=res.getString("mch_id");
		 key=res.getString("key");
		 server_name=res.getString("server_name");
		 notify_url=res.getString("notify_url");
	}
	 
	 @RequestMapping(value="/shouquan")
	 public String shouquan(HttpServletRequest request,HttpServletResponse response,Model model,
			 @RequestParam(value="openid",required=true) String openid){
		 model.addAttribute("openid", openid);
		 return "shouquanResult";
	 }
	
	/**
	 * 获取支付二维码链接
	 */
	@RequestMapping(value="/h5WxPay",method=RequestMethod.GET)
	public ModelAndView h5WxPay(HttpServletRequest request, HttpServletResponse response, String openid,
			@RequestParam(value="orderId",required=true) String orderId,
			@RequestParam(value="productType",required=true) String productType,
			@RequestParam(value="userName",required=true) String userName

			) throws Exception{
//		if(StringUtil.isEmpty(openid)){
//			log.info("微信支付获取openid失败");
//			return ;
//		}
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, String> params=new HashMap<String,String>();
		String isSuccess="0";
		String info="";
			params.put("trade_type", "NATIVE");
		    params.put("spbill_create_ip", H5WxUtils.localIp());
		    
		    params.put("total_fee","1");
		    params.put("appid",appid);
		    params.put("mch_id",mch_id);
		    params.put("nonce_str", StringUtil.generateRandom(20));//随机数
		    //params.put("body", order.getPRODUCT_NAME());//产品名
		    params.put("body", "盈科跟团游");//产品名
		    SimpleDateFormat sdFormat=new SimpleDateFormat("yyyyMMddHHmmss");
		    params.put("out_trade_no",sdFormat.format(new Date())+"111");//订单号
		    params.put("notify_url",notify_url);
		    //可作为参数 订单的产品类型
		    params.put("attach", productType);
			//map.put("openId", openid);
			String qrUrl=H5WxUtils.getPayURL(params,key);
			map.put("payMoney", "0.01");
			if(StringUtil.isEmpty(qrUrl)){
				isSuccess="1";
				info="生成支付二维码失败";
			}else{
				map.put("qrUrl", qrUrl);
			}
		map.put("isSuccess", isSuccess);
		map.put("info", info);
		map.put("orderId", orderId);
		map.put("productType", productType);
		map.put("userName", userName);
		return new ModelAndView("packagewx/wxPay").addAllObjects(map);
		
	}
	

	
	/**
     * 获取微信用户OpenId
     */
    @ResponseBody
    @RequestMapping(value = "/getWechatOpenId")
    public void getWechatOpenId(String code, String targetUrl, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if (StringUtil.isEmpty(code)) {
            String codeUrl = getCodeUrl(targetUrl, request);
            response.sendRedirect(codeUrl);
        } else {
        	String accessTokenUrl = String.format(H5WxConstant.USER_TOKEN,appid,appsecret, code);
        	JSONObject jsonObject=HttpUtil.sendGet(accessTokenUrl, "utf-8");
        	 if (null != jsonObject && jsonObject.containsKey("access_token")) {
        		 if (jsonObject.get("scope").toString().equals("snsapi_base")) {
                     String openid=jsonObject.getString("openid");
                     if(StringUtil.isEmpty(openid)){
                    
                     }else{
                    	 if(targetUrl.indexOf("?") == -1) {
                    		 targetUrl = targetUrl + "?" + "openid="+openid;
                    	    } else {
                    	     targetUrl = targetUrl + "&" + "openid="+openid;
                    	    }
                     }
                   
                 }else if(jsonObject.get("scope").toString().equals("snsapi_userinfo")){
                	String access_token=jsonObject.getString("access_token");
                    String openid=jsonObject.getString("openid");
                    String getUserinfoUrl=String.format(H5WxConstant.USERINFO, access_token,openid);
                    JSONObject jsonUserinfo=HttpUtil.sendGet(getUserinfoUrl, "utf-8");
                    if(targetUrl.indexOf("?") == -1) {
               		 targetUrl = targetUrl + "?" + "openid="+openid;
               	    } else {
               	     targetUrl = targetUrl + "&" + "openid="+openid;
               	    }
                 }
        	 }
            response.sendRedirect(targetUrl.replace("^", "&"));
        }
    } 
    
    /**
     * 获取页面授权的回调URL
     */
    private String getCodeUrl(String targetUrl, HttpServletRequest request) throws UnsupportedEncodingException {
        // 获取页面授权的回调URL
        String redirectUrl="";
        try {
        	redirectUrl=URLEncoder.encode("http://" + server_name + "/h5Pay/getWechatOpenId.do?targetUrl=" + targetUrl, "utf-8");
            String str=String.format(H5WxConstant.OAUTH2_INFO,appid,redirectUrl);
            System.out.println(str);
            return str;
        } catch (Exception ex) {
           // log.info("获取微信授权错误：" + ex.getMessage());
        }
        return null;
    }
    
  
    /**
    * 微信支付异步回调方法
    * @param request
    * @param response
    * @throws Exception
    */
    @RequestMapping("/wxPayNotify")  
    public void appPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {  
    	 String resXml="";
        // "<xml><appid><![CDATA[wxb4dc385f953b356e]]></appid><bank_type><![CDATA[CCB_CREDIT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1228442802]]></mch_id><nonce_str><![CDATA[1002477130]]></nonce_str><openid><![CDATA[o-HREuJzRr3moMvv990VdfnQ8x4k]]></openid><out_trade_no><![CDATA[1000000000051249]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[1269E03E43F2B8C388A414EDAE185CEE]]></sign><time_end><![CDATA[20150324100405]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1009530574201503240036299496]]></transaction_id></xml>";  
       // log.info("====h5进入微信支付回调方法================================");
    	response.setCharacterEncoding("UTF-8");  
        response.setContentType("text/xml");  
        ServletInputStream in = request.getInputStream();  
        String xmlMsg = Tools.inputStream2String(in);  
  
        Map<String, String> map =SimpleXMLParseUtil.doXMLParse(xmlMsg); 
        String return_code = map.get("return_code");  
        String return_msg = map.get("return_msg");
        if(StringUtil.isEmpty(return_code)||!"SUCCESS".equals(return_code)){
        	//log.error("==h5微信支付异步回调方法错误，通信失败==return_msg="+return_msg+"========================");
        	 resXml="<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        	 response.getWriter().write(resXml);  
        	 return;
        }
        //订单产品类型
        String attach=map.get("attach");
        //获取订单号
        String orderNo=map.get("out_trade_no");
        orderNo=orderNo.substring(14);
        Map<String, Object> param=new HashMap<String,Object>();
        param.put("ORDER_NO", orderNo);
//        map = new HashMap<String, String>();  
//        map.put("return_code", "SUCCESS");  
//        map.put("return_msg", "OK");  
  
        // 响应xml  
        //String resXml = H5WxUtils.ArrayToXml(map, true);  
        resXml="<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        //log.info("=========h5响应微信支付通知成功xml=resXml="+resXml+"===========================");
        response.getWriter().write(resXml);  
    } 
//    
//    /**
//	 * 生成二维码
//	 */
//	@RequestMapping(value = "/qrCode.htm", method = { RequestMethod.GET, RequestMethod.POST })
//	public ModelAndView qrCode(String url, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		ServletOutputStream out = response.getOutputStream();
//		try {
//			Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
//			int width = 200;
//			int height = 200;
//			// 内容所使用编码
//			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//			//hints.put(1, 1);
//			BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
//			BufferedImage twoDimensionalCode = MatrixToImageWriter.toBufferedImage(bitMatrix);
//			// 设置浏览器不缓存本页
//			response.setDateHeader("Expires", 0);
//			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
//			response.addHeader("Cache-Control", "post-check=0, pre-check=0");
//			response.setHeader("Pragma", "no-cache");
//			ImageIO.write(twoDimensionalCode, "JPEG", out);
//			out.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			out.close();
//		}
//		return null;
//	}
	/**
	 * base64加密
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("restriction")
	public static String encryptBASE64(byte[] key) throws Exception {
	 return (new BASE64Encoder()).encodeBuffer(key);
    
	}
	
	/**
     * 获取签名
     */
    public  WechatContent sign(String url) {
        WechatContent content = null;
        WechatToken token = getAccessToken(); // 获取token
        if (!StringUtil.isEmpty(token.getAccessToken())) { // 获取ticket
            WechatToken ticket = getApiTicket(token.getAccessToken(), false);
            if (!StringUtil.isEmpty(ticket.getTicket())) {
                content = new WechatContent();
                String nonce_str = UUID.randomUUID().toString();
                String timestamp = Long.toString(System.currentTimeMillis() / 1000);
                String string1;
                String signature = "";
                string1 = "jsap" + "i_ticket=" + ticket.getTicket() + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
                try {
                    MessageDigest crypt = MessageDigest.getInstance("SHA-1");
                    crypt.reset();
                    crypt.update(string1.getBytes("UTF-8"));
                    signature = H5WxUtils.byteToHex(crypt.digest());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                content.setAppId(appid);
                content.setJsapiTicket(ticket.getTicket());
                content.setNonceStr(nonce_str);
                content.setTimestamp(timestamp);
                content.setSignature(signature);
                content.setUrl(url);
            }
        }
        return content;
    }
	
	 /**
     * 获取Token
     */
    public  WechatToken getAccessToken() {
        WechatToken token = new WechatToken();
        String tokenStr="";
        //tokenStr = redisClient.getCacheObject("WECHAT_TOKEN_KEY");
        tokenStr = memcachedManager.getCache("WECHAT_TOKEN_KEY");
        if (StringUtil.isEmpty(tokenStr)) {
            String tockenUrl = String.format(H5WxConstant.ACCESS_TOKEN, appid, appsecret);
            JSONObject jsonObject=HttpUtil.sendGet(tockenUrl, "utf-8");
            if (null != jsonObject && !jsonObject.containsKey("errcode")) {
                try {
                    token = new WechatToken();
                    token.setAccessToken(jsonObject.getString("access_token"));
                    token.setExpiresIn(jsonObject.getInt("expires_in"));
                    //redisClient.setCacheObject("WECHAT_TOKEN_KEY", token.getAccessToken());
                    memcachedManager.setCache(token.getAccessToken(),"WECHAT_TOKEN_KEY", 7200);
                } catch (JSONException e) {
                    token = null;// 获取token失败
                }
            } else if (null != jsonObject) {
                token = new WechatToken();
                token.setErrcode(jsonObject.getInt("errcode"));
            }
        }else{
        	token.setAccessToken(tokenStr);
        }
        return token;
    }
    
    /**
     * 获取Ticket
     */
    public  WechatToken getApiTicket(String tokenStr, boolean update) {
        WechatToken token = new WechatToken();
        String tokenTic="";
        //tokenTic =  redisClient.getCacheObject("WECHAT_TICKET_KEY");
        tokenTic =  memcachedManager.getCache("WECHAT_TICKET_KEY");
        if (update) {
            token = null;
        }
        if (token == null || StringUtil.isEmpty(tokenTic)) {
            String tockenUrl = String.format(H5WxConstant.TICKET_TOKEN, tokenStr);
            JSONObject jsonObject =HttpUtil.sendGet(tockenUrl, "utf-8");
            if (null != jsonObject && jsonObject.containsKey("ticket")) {
                try {
                    token = new WechatToken();
                    token.setTicket(jsonObject.getString("ticket"));
                    token.setExpiresIn(jsonObject.getInt("expires_in"));
                    //redisClient.setCacheObject("WECHAT_TICKET_KEY", token.getTicket());
                    memcachedManager.setCache(token.getTicket(), "WECHAT_TICKET_KEY", 7200);
                } catch (JSONException e) {
                    token = null;
                }
            } else if (null != jsonObject) {
                token = new WechatToken();
                token.setErrcode(jsonObject.getInt("errcode"));
            }
        }else{
        	token.setTicket(tokenTic);
        }
        return token;
    }
    
    @ResponseBody
    @RequestMapping("/getWxConfigInfo")
    public JSONObject getWxConfigInfo(HttpServletRequest request,HttpServletResponse response){
    	JSONObject jsonObject=null;
    	String url=request.getParameter("url");
    	WechatContent wxContent =sign(url);
    	jsonObject = JSONObject.fromObject(wxContent);
    	return jsonObject;
    }
}
