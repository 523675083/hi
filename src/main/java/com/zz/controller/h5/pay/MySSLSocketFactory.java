package com.zz.controller.h5.pay;


import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class MySSLSocketFactory extends SSLSocketFactory
{
  //private static Logger logger = LoggerFactory.getLogger(SSLSocketFactory.class);

  private static MySSLSocketFactory mySSLSocketFactory = null;

  private static SSLContext createSContext()
  {
    SSLContext sslcontext = null;
    try {
      sslcontext = SSLContext.getInstance("SSL");
    } catch (NoSuchAlgorithmException e) {
      //logger.error("NoSuchAlgorithmException", e);
    }
    try {
      if (sslcontext != null)
        sslcontext.init(null, new TrustManager[] { new TrustAnyTrustManager() }, null);
    }
    catch (KeyManagementException e) {
      //logger.error("KeyManagementException", e);
      return null;
    }
    return sslcontext;
  }

  private MySSLSocketFactory(SSLContext sslContext)
  {
    super(sslContext);
    setHostnameVerifier(ALLOW_ALL_HOSTNAME_VERIFIER);
  }

  public static MySSLSocketFactory getInstance() {
    if (mySSLSocketFactory != null) {
      return mySSLSocketFactory;
    }
    return MySSLSocketFactory.mySSLSocketFactory = new MySSLSocketFactory(createSContext());
  }
}
