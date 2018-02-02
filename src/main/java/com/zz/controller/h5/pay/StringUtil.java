package com.zz.controller.h5.pay;


import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 操作String的相关操作
 * @author liufei
 *
 */
public class StringUtil {
	/**
	 * 判断str1与str2两个字符串是否相等，相等为true，否则为false
	 * @param str1
	 * @param str2
	 * @return  true or false
	 */
	public static boolean equals(String str1,String str2) {
		boolean b = false ;
		if(str1==null&&str2==null) b = true ;
		else if((str1==null&&str2!=null)||(str1!=null&&str2==null)) {
			b = false ;
		}else if(str1.trim().equals(str2.trim())) {
			b = true ;
		}else {
			b = false ;
		}
		return b ;
	}
	/**
	 * 判断字符串是否为空，即判断字符串是否为null或者是""字符串等操作
	 * @param str
	 * @return 为空时返回true，否则返回false
	 */
	public static boolean isNull(String str) {
		boolean b = false ;
		if(str==null||str.trim().equals("")) {
			b = true ;
		}
		return b ;
	}
	/**
	 * 判断字符串str中是否含有subStr
	 * @param str
	 * @param subStr
	 * @return
	 */
	public static boolean isPart(String str,String subStr) {
		boolean b = false ;
		if(str==null||subStr==null) {
			b = false ;
		}else {
			int index = str.indexOf(subStr)  ;
			if(index>=0) b = true ;
		}
		return b ;
	}
	/**
	 * 字符的编码转换s
	 * @param str
	 * @param fromCharset
	 * @param toCharset
	 * @return
	 */
	public static String setCharset(String str,String fromCharset,String toCharset) {
		if(str == null) return null ;
		try {
			str = new String(str.getBytes(fromCharset),toCharset) ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str ;
	}
	/**
	 * 判断字符是否为数字,不包含小数点
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		boolean b = true ;
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57)
			return false;
		}
		return b ;
	}
	/**
	 * 判断是否为数字，包含小数点
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		boolean b = false ;
		if(StringUtil.isNull(str)) return b ;
		b = Pattern.matches("^(0|[1-9]{1}[0-9]{0,}+)(.[0-9]{1,}){0,1}+", str.toLowerCase());  
		return b ;
	}
	/**
	 * 判断是否为email格式
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		boolean b = false ;
		if(StringUtil.isNull(str)) return b ;
		b = Pattern.matches("^(\\w+@(\\w+|-){1,}\\.\\w+){1}", str.toLowerCase());  
		return b ;
	}
	/**
	 * 判断字符串是否是HTTP的格式
	 * @param str
	 * @return
	 */
	public static boolean isHTTPURL(String str) {
		boolean b = false ;
		if(StringUtil.isNull(str)) return b ;
		b = Pattern.matches("^(http|https)://\\w+((.\\w+)|/){1,}", str.toLowerCase());  
		return b ;
	}
	/**
	 * 判断字符串是否为日期格式，格式以type变量为准，如type="-"，则为yyyy-MM-dd的格式
	 * @param str
	 * @param type
	 * @return
	 */
	public static boolean isDateFormat(String str,String type) {
		boolean b = false ;
		if(StringUtil.isNull(str)) return b ;
		b = Pattern.matches("[1-9][0-9]{3}"+type+"(0[1-9]|1[0-2])"+type+"(0[1-9]|[1-2][0-9]|3[0-1])", str.toLowerCase());  
		return b ;
	}
	/**
	 * 生成随机数，当指定的参数小于0时默认为6位
	 * @param length
	 * @return
	 */
	public static String generateRandom(int length) {
		StringBuffer sb = new StringBuffer() ;
		if(length<=0) length=6 ;
		for(int i=0;i<length;i++){
			int random = (int)(Math.random()*10) ;
			sb.append(random) ;
		}
		return sb.toString() ;
	}
	/**
	 * 生成随机字符串数据
	 * @param length
	 * @return
	 */
	public static String generateRandomString(int length) {
		String arr[] = {
				"0","1","2","3","4","5","6","7","8","9"
				,"A","B","C","D","E","F","G","H","I","J"
				,"K","L","M","N","O","P","Q","R","S","T"
				,"U","V","W","X","Y","Z"
				,"a","b","c","d","e","f","g","h","i","j"
				,"k","l","m","n","o","p","q","r","s","t"
				,"u","v","w","x","y","z"
		} ;
		StringBuffer sb = new StringBuffer() ;
		int size = arr.length ;
		if(length<=0) length=6 ;
		for(int i=0;i<length;i++){
			int random = (int)(Math.random()*size) ;
			sb.append(arr[random]) ;
		}
		return sb.toString() ;
	}

	public static String generateOrderNo(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String date = sdf.format(new Date()); // 生成时间数字
		String sj = RandomUtil.randomUtil(4);// 四位随机数
		String orderno = date + sj;// 生成订单编号
		return orderno;
	}
	
	
	/**
	 * 根据时间秒生成随机字符串数据
	 * @param length
	 * @return
	 */
	public static String randomStringByTime(int length){
		StringBuffer sb = new StringBuffer(String.valueOf(System.currentTimeMillis())) ;
		String randomStr=generateRandomString(length);
		sb.append(randomStr);
		return sb.toString();
	}
	/**
	 * 随机生成字符串
	 * 
	 * @param len 长度
	 * @return
	 */
	public static String randomString(Integer len) {
		char[] chars = { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a',
				's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v',
				'b', 'n', 'm', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O',
				'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X',
				'C', 'V', 'B', 'N', 'M', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '0' };
		Random r = new Random();
		len = 0>=len?6:len;
		StringBuffer no = new StringBuffer();
		for (int i = 0; i < len; i++) {
			char c = chars[r.nextInt(chars.length)];
			no.append(c);
		}
		return no.toString();
	}
	/**
	 * 将字符串元转换为分
	 * @param cny
	 * @return
	 */
	public static long exchangeCNY2Fen(String cny) {
		if(cny==null||cny.trim().equals("")) {
			return -1l ;
		}else if(!StringUtil.isNumber(cny)) {
			return -2l ;
		}
		DecimalFormat format = new DecimalFormat("0.00") ;
		long fen = -1l;
		try {
			fen = (long)(format.parse(cny).floatValue()*100);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return fen ;
	}
	/**
	 * 将分转换为元
	 * @param fen
	 * @return
	 */
	public static double exangeFen2CNY(String fen) {
		if(StringUtil.isNull(fen)) {
			return -1 ;
		}else if(!StringUtil.isNumeric(fen)) {
			return -2 ;
		}
		double cny = Double.parseDouble(fen)/100 ;
		return cny ;
	}
	
	/**
	 * 获取银行卡号后四位
	 * @Title: bankCardHandle
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bankCard
	 * @return
	 */
	public static String bankCardHandle(String bankCard){
		if(bankCard == null || "".equals(bankCard.trim()) || bankCard.length()<=4){
			return bankCard;
		}
		return bankCard.substring(bankCard.length() - 4);
	}
	
	/**
	 * 验证手机号
	 * 
	 * @param Mobile
	 * @return
	 */
	public static boolean isMobile(String Mobile) {
		Pattern pattern = Pattern.
				compile(("^\\d{11}$")); 
		Matcher match = pattern.matcher(Mobile);
		return match.matches();
	}

	/**
	 * 校验ip合法性
	 * @Title: checkIp
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param ip
	 * @return
	 */
	public static boolean isIp(String ip){
		Pattern pattern = Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}"); 
		Matcher match = pattern.matcher(ip);
		return match.matches();
	}
	
	public static String hideString(String str,int startLen,int endLen){
		if(StringUtil.isNull(str)) return str;
		int len = str.length()-(startLen+endLen);
		if(len<0){
			return str;
		}
		String startStr = str.substring(0, startLen);
		String endStr = str.substring(str.length() - endLen);
		StringBuffer middle = new StringBuffer();
		for (int i = 0; i < len; i++) {
			middle.append("*");
		}
		String result = startStr + middle.toString() + endStr;
		return result;
	}
	
	/**
	 * 判断是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(Object str) {
		return (str == null || "".equals(str));
	}
	
	public static void main(String[] args) {
		/*System.out.println(StringUtil.isNumber("15.000"));
		System.out.println(StringUtil.isEmail("yunchan86@16-3com"));
		System.out.println(StringUtil.isHTTPURL("http://www.yacol.com/ss/"));
		System.out.println(isDateFormat("2011-01-25","-"));
		System.out.println(isPart("true:","true"));
		System.out.println(StringUtil.exangeFen2CNY("101"));
		System.out.println(StringUtil.exchangeCNY2Fen("1.23"));*/
//		System.out.println(hideString("12345678",3,4));
		
		Pattern pattern = Pattern.
				compile(("^\\d{11}$")); 
		Matcher match = pattern.matcher("15801168536");
		System.out.println(match.matches());
		
		System.out.println("随机数——"+randomStringByTime(10));
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("随机数——"+randomStringByTime(10));
		System.out.println("随机数——"+randomStringByTime(10));
	}
	
	/**
	 * formatString(String text) : 字符串格式化方法
	 */
	public static String formatString(String text) {
		return (text == null ? "" : text.trim());
	}
}
