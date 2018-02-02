package com.zz.controller.h5.pay;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public final class Tools {  
  
    public static final String inputStream2String(InputStream in) throws UnsupportedEncodingException, IOException{  
        if(in == null)  
            return "";  
          
        StringBuffer out = new StringBuffer();  
        byte[] b = new byte[4096];  
        for (int n; (n = in.read(b)) != -1;) {  
            out.append(new String(b, 0, n, "UTF-8"));  
        }  
        return out.toString();  
    }  
      
  
}  