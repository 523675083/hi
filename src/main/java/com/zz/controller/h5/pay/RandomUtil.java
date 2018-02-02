package com.zz.controller.h5.pay;


import java.util.Random;

public class RandomUtil {
	public static final String allChar = "01234567890123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String upperLetterChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
   public static String randomUtil(int digit){
		long randomNum=(long)(Math.random()*(Math.pow(10,digit)-1*Math.pow(10,digit-1))+1*Math.pow(10,digit-1));
	    String typeRandomNum=String.valueOf(randomNum);
	    return typeRandomNum;
   }
   
   public static String getUniqNo(){
	   	StringBuffer sb = new StringBuffer();
		Random random = new Random();
		int n=4,m=5;
		while(n>0){
			sb.append( upperLetterChar.charAt( random.nextInt( upperLetterChar.length() ) ) );
			n--;
		}
		sb.append("_");
		while(m>0){
			sb.append( allChar.charAt( random.nextInt( allChar.length() ) ) );
			m--;
		}
		return sb.toString();
  }
   
   public static void main(String[] args) {
	System.out.println(getUniqNo());
}
}
