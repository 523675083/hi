package com.zz.controller.h5;
/**
 * 接口凭证
 * 
 * 
 */
public class WechatToken implements java.io.Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4215137277384647451L;
	/**
	 * 
	 */
	
	private String accessToken;// 接口访问凭证
	private int expiresIn;// 凭证有效期，单位：秒
	private String ticket;// 凭证
	private Integer errcode;// 错误编码

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

}