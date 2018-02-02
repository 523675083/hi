package com.zz.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zz.util.JedisSimpleClient;
import com.zz.util.JedisWorker;

import redis.clients.jedis.Jedis;

@Service
public class MemcachedManager {

	private JedisSimpleClient sessionMemcachedClient;

	@Autowired
	public void setSessionMemcachedClient(JedisSimpleClient sessionMemcachedClient) {
		this.sessionMemcachedClient = sessionMemcachedClient;
	}
	
	
	/**
	 * 获取缓存数据
	 * @param cityId
	 * @param lineType == sid
	 * @param marking 缓存的KEY
	 * @return
	 */
	public String getCache(final String key){
		String result = "";

		result = sessionMemcachedClient.execute(new JedisWorker<String>(){

			@Override
			public String work(Jedis jedis) {
				if(jedis.exists(key)){
					return jedis.get(key);
				}
				return "";
			}
		});

		return result;	
	}
	
	/**
	 * 设置缓存数据
	 * @param result
	 * @param cityId
	 * @param lineType == sid
	 * @param marking 缓存的KEY
	 */
	public void setCache(final String result, final String key, final int timeout){

		sessionMemcachedClient.execute(new JedisWorker<String>() {
			@Override
			public String work(Jedis jedis) {
				return jedis.setex(key, timeout, result);
			}
		}

		);
	}
	
}
