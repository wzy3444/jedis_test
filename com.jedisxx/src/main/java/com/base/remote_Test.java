package com.base;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class remote_Test {
	public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.142.129",6379);
		jedis.auth("123456");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("hello","world");
		jsonObject.put("name","wzy");

		Transaction multi = jedis.multi();
		String result = jsonObject.toJSONString();
		System.out.println(result);

		try {
			multi.set("user1",result);
			multi.set("user2",result);
			multi.exec();//执行事务
		}catch (Exception e){
			multi.discard(); //放弃事务
			e.printStackTrace();
		}finally {
			System.out.println(jedis.get("user1"));
			System.out.println(jedis.get("user2"));
            jedis.flushDB();
		}
		jedis.close();//关闭连接

	}
}
