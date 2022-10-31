package ar.unrn.tp.redis;

import redis.clients.jedis.Jedis;

public class RedisCache {
	private Jedis redis;

	public RedisCache(String localhost, int puerto) {
		this.redis = new Jedis(localhost, puerto);
		System.out.println("Connection Successful");
		System.out.println("The server is running " + redis.ping());
	}

	public void set(String key, String value) {
		try {
			this.redis.set(key, value);
		} finally {
			this.close();
		}
	}

	public String get(String key) {
		try {
			String result = this.redis.get(key);
			return result;
		} finally {
			this.close();
		}

	}

	public void del(String key) {
		try {
			this.redis.del(key);
		} finally {
			this.close();
		}
	}

	private void close() {
		this.redis.close();
	}
}
