package com.zz.controller.h5.pay;


public class H5WxConstant {
	
	/**
     * oauth2接口URL
     */
    public static final String OAUTH2 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base#wechat_redirect";
    /**
     * 获取用户标识URL
     */
    public static final String USER_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    /**
     * 统一下单接口
     */
  	public static final String UNIFY_ORDER="https://api.mch.weixin.qq.com/pay/unifiedorder";
  	/**
     * oauth2接口URL
     */
    public static final String OAUTH2_INFO	 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo#wechat_redirect";
 
    public static final String USERINFO="https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
    /**
     * token接口URL
     */
    public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    /**
     * ticket接口URL
     */
    public static final String TICKET_TOKEN = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

}
