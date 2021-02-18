package com.cssrc.ibms.core.util.redis;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.SortingParams;
import redis.clients.util.SafeEncoder;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;

/**
 * 
 * <p>
 * Redis客户端访问
 * </p>
 * 
 * @author xye
 * @创建时间：2014年10月31日
 * @version： V1.0
 */
public class RedisClient
{
    private static Logger  logger=Logger.getLogger(RedisClient.class);
    public static JedisPool jedisPool; // 池化管理jedis链接池
    private final int expire = 60000;
    public static final String confbean="pluginproperties";

    static
    {
        
        // 读取相关的配置
        int redisuse = Integer.parseInt(AppConfigUtil.get(confbean, "plugin.redis.use"));
        if(redisuse==1) {
            int maxActive = Integer.parseInt(AppConfigUtil.get(confbean, "redis.pool.maxActive"));
            int maxIdle = Integer.parseInt(AppConfigUtil.get(confbean, "redis.pool.maxIdle"));
            int maxWait = Integer.parseInt(AppConfigUtil.get(confbean, "redis.pool.maxWait"));
            
            String ip = AppConfigUtil.get(confbean, "redis.host");
            int port = Integer.parseInt(AppConfigUtil.get(confbean, "redis.port"));
            String password =AppConfigUtil.get(confbean, "redis.pass");
            
            JedisPoolConfig config = new JedisPoolConfig();
            // 设置最大连接数
            config.setMaxTotal(maxActive);
            // 设置最大空闲数
            config.setMaxIdle(maxIdle);
            // 设置超时时间
            config.setMaxWaitMillis(maxWait);
            
            // 初始化连接池
            if (password != null && !"".equals(password))
            {
                jedisPool = new JedisPool(config, ip, port, maxActive, password);
            }
            else
            {
                jedisPool = new JedisPool(config, ip, port,maxActive);
            }
        }
       
    }
    
    /**
     * 设置过期时间
     *
     * @param key
     * @param seconds
     */
    public void expire(String key, int seconds)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return ;
        }
        if (seconds <= 0)
        {
            return;
        }
        Jedis jedis = jedisPool.getResource();
        jedis.expire(key, seconds);
        jedisPool.returnResource(jedis);
    }
    
    /**
     * 设置默认过期时间
     *
     * @param key
     */
    public void expire(String key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return ;
        }
        expire(key, expire);
    }
    
    // *******************************************Strings************************
    /**
     * 向缓存中设置byte[]内容
     * 
     * @param key key
     * @param value value
     * @return
     * @throws Exception
     */
    public static boolean set(byte[] key, byte[] value, int seconds)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return false;
        }
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, seconds);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
     * 向缓存中设置字符串内容
     * 
     * @param key key
     * @param value value
     * @return
     * @throws Exception
     */
    public static boolean set(String key, String value)
        throws Exception
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return false;
        }
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
     * @param key
     * @param seconds 过期时间 秒
     * @param value
     * @return
     * @throws Exception
     */
    public static boolean setex(String key, int seconds, String value)
        throws Exception
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return false;
        }
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            jedis.setex(key, seconds, value);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
     * 向缓存中设置对象
     * 
     * @param key
     * @param value
     * @return
     */
    public static boolean set(String key, Object value)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return false;
        }
        Jedis jedis = null;
        try
        {
            String objectJson = JSON.toJSONString(value);
            jedis = jedisPool.getResource();
            jedis.set(key, objectJson);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
     * 删除缓存中得对象，根据key
     * 
     * @param key
     * @return
     */
    public static boolean del(String key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return false;
        }
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            jedis.del(key);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
     * 根据key 获取内容
     * 
     * @param key
     * @return
     */
    public static Object get(String key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return false;
        }
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            Object value = jedis.get(key);
            return value;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
     * 根据key 获取内容
     * 
     * @param key
     * @return
     */
    public static Object get(byte[] key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return false;
        }
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            Object value = jedis.get(key);
            return value;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
     * 根据key 获取内容
     * 
     * @param key
     * @return
     */
    public static byte[] getByte(byte[] key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        }
        finally
        {
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
     * 根据key 获取对象
     * 
     * @param key
     * @return
     */
    public static <T> T get(String key, Class<T> clazz)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            if (value != null)
            {
                return JSON.parseObject(value, clazz);
            }
            else
            {
                return null;
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            jedisPool.returnResource(jedis);
        }
    }
    
    public static List<String> mget(String... keys)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        List<String> list = jedis.mget(keys);
        jedisPool.returnResource(jedis);
        return list;
    }
    
    // *******************************************Hash************************
    /**
     * 从hash中删除指定的存储
     * 
     * @param String key
     * @param String fieid 存储的名字
     * @return 状态码，1成功，0失败
     * */
    public static long hdel(String key, String fieid)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        Jedis jedis = jedisPool.getResource();
        long s = jedis.hdel(key, fieid);
        jedisPool.returnResource(jedis);
        return s;
    }
    
    public static long hdel(String key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        Jedis jedis = jedisPool.getResource();
        long s = jedis.del(key);
        jedisPool.returnResource(jedis);
        return s;
    }
    
    /**
     * 测试hash中指定的存储是否存在
     * 
     * @param String key
     * @param String fieid 存储的名字
     * @return 1存在，0不存在
     * */
    public static boolean hexists(String key, String fieid)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return false;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        boolean s = jedis.hexists(key, fieid);
        jedisPool.returnResource(jedis);
        return s;
    }
    
    /**
     * 返回hash中指定存储位置的值
     *
     * @param String key
     * @param String fieid 存储的名字
     * @return 存储对应的值
     * */
    public static String hget(String key, String fieid)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        String s = jedis.hget(key, fieid);
        jedisPool.returnResource(jedis);
        return s;
    }
    
    public static byte[] hget(byte[] key, byte[] fieid)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        byte[] s = jedis.hget(key, fieid);
        jedisPool.returnResource(jedis);
        return s;
    }
    
    /**
     * 以Map的形式返回hash中的存储和值
     * 
     * @param String key
     * @return Map<Strinig,String>
     * */
    public static Map<String, String> hgetAll(String key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        Map<String, String> map = jedis.hgetAll(key);
        jedisPool.returnResource(jedis);
        return map;
    }
    
    /**
     * 添加一个对应关系
     * 
     * @param String key
     * @param String fieid
     * @param String value
     * @return 状态码 1成功，0失败，fieid已存在将更新，也返回0
     * **/
    public static long hset(String key, String fieid, String value)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        Jedis jedis = jedisPool.getResource();
        long s = jedis.hset(key, fieid, value);
        jedisPool.returnResource(jedis);
        return s;
    }
    
    public static long hset(String key, String fieid, byte[] value)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        Jedis jedis = jedisPool.getResource();
        long s = jedis.hset(key.getBytes(), fieid.getBytes(), value);
        jedisPool.returnResource(jedis);
        return s;
    }
    
    /**
     * 添加对应关系，只有在fieid不存在时才执行
     * 
     * @param String key
     * @param String fieid
     * @param String value
     * @return 状态码 1成功，0失败fieid已存
     * **/
    public static long hsetnx(String key, String fieid, String value)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        Jedis jedis = jedisPool.getResource();
        long s = jedis.hsetnx(key, fieid, value);
        jedisPool.returnResource(jedis);
        return s;
    }
    
    /**
     * 获取hash中value的集合
     *
     * @param String key
     * @return List<String>
     * */
    public static List<String> hvals(String key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        List<String> list = jedis.hvals(key);
        jedisPool.returnResource(jedis);
        return list;
    }
    
    /**
     * 在指定的存储位置加上指定的数字，存储位置的值必须可转为数字类型
     *
     * @param String key
     * @param String fieid 存储位置
     * @param String long value 要增加的值,可以是负数
     * @return 增加指定数字后，存储位置的值
     * */
    public static long hincrby(String key, String fieid, long value)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        Jedis jedis = jedisPool.getResource();
        long s = jedis.hincrBy(key, fieid, value);
        jedisPool.returnResource(jedis);
        return s;
    }
    
    /**
     * 返回指定hash中的所有存储名字,类似Map中的keySet方法
     *
     * @param String key
     * @return Set<String> 存储名称的集合
     * */
    public static Set<String> hkeys(String key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        Set<String> set = jedis.hkeys(key);
        jedisPool.returnResource(jedis);
        return set;
    }
    
    /**
     * 获取hash中存储的个数，类似Map中size方法
     *
     * @param String key
     * @return long 存储的个数
     * */
    public static long hlen(String key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        long len = jedis.hlen(key);
        jedisPool.returnResource(jedis);
        return len;
    }
    
    /**
     * 根据多个key，获取对应的value，返回List,如果指定的key不存在,List对应位置为null
     *
     * @param String key
     * @param String ... fieids 存储位置
     * @return List<String>
     * */
    public static List<String> hmget(String key, String... fieids)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        List<String> list = jedis.hmget(key, fieids);
        jedisPool.returnResource(jedis);
        return list;
    }
    
    public static List<byte[]> hmget(byte[] key, byte[]... fieids)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        List<byte[]> list = jedis.hmget(key, fieids);
        jedisPool.returnResource(jedis);
        return list;
    }
    
    /**
     * 添加对应关系，如果对应关系已存在，则覆盖
     *
     * @param Strin key
     * @param Map <String,String> 对应关系
     * @return 状态，成功返回OK
     * */
    public static String hmset(String key, Map<String, String> map)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        Jedis jedis = jedisPool.getResource();
        String s = jedis.hmset(key, map);
        jedisPool.returnResource(jedis);
        return s;
    }
    
    /**
     * 添加对应关系，如果对应关系已存在，则覆盖
     *
     * @param Strin key
     * @param Map <String,String> 对应关系
     * @return 状态，成功返回OK
     * */
    public static String hmset(byte[] key, Map<byte[], byte[]> map)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        Jedis jedis = jedisPool.getResource();
        String s = jedis.hmset(key, map);
        jedisPool.returnResource(jedis);
        return s;
    }
    
    // *******************************************keys相关****************************
    /**
     * 清空所有key
     */
    public static String flushAll()
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        Jedis jedis = jedisPool.getResource();
        String stata = jedis.flushAll();
        jedisPool.returnResource(jedis);
        return stata;
    }
    
    /**
     * 更改key
     *
     * @param String oldkey
     * @param String newkey
     * @return 状态码
     * */
    public static String rename(String oldkey, String newkey)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        return rename(SafeEncoder.encode(oldkey), SafeEncoder.encode(newkey));
    }
    
    /**
     * 更改key,仅当新key不存在时才执行
     *
     * @param String oldkey
     * @param String newkey
     * @return 状态码
     * */
    public static long renamenx(String oldkey, String newkey)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        Jedis jedis = jedisPool.getResource();
        long status = jedis.renamenx(oldkey, newkey);
        jedisPool.returnResource(jedis);
        return status;
    }
    
    /**
     * 更改key
     *
     * @param String oldkey
     * @param String newkey
     * @return 状态码
     * */
    public static String rename(byte[] oldkey, byte[] newkey)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        Jedis jedis = jedisPool.getResource();
        String status = jedis.rename(oldkey, newkey);
        jedisPool.returnResource(jedis);
        return status;
    }
    
    /**
     * 设置key的过期时间，以秒为单位
     *
     * @param String key
     * @param 时间 ,已秒为单位
     * @return 影响的记录数
     * */
    public static long expired(String key, int seconds)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        Jedis jedis = jedisPool.getResource();
        long count = jedis.expire(key, seconds);
        jedisPool.returnResource(jedis);
        return count;
    }
    
    /**
     * 设置key的过期时间,它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。
     *
     * @param String key
     * @param 时间 ,已秒为单位
     * @return 影响的记录数
     * */
    public static long expireAt(String key, long timestamp)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        Jedis jedis = jedisPool.getResource();
        long count = jedis.expireAt(key, timestamp);
        jedisPool.returnResource(jedis);
        return count;
    }
    
    /**
     * 查询key的过期时间
     *
     * @param String key
     * @return 以秒为单位的时间表示
     * */
    public static long ttl(String key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        long len = jedis.ttl(key);
        jedisPool.returnResource(jedis);
        return len;
    }
    
    /**
     * 取消对key过期时间的设置
     *
     * @param key
     * @return 影响的记录数
     * */
    public static long persist(String key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        Jedis jedis = jedisPool.getResource();
        long count = jedis.persist(key);
        jedisPool.returnResource(jedis);
        return count;
    }
    
    /**
     * 删除keys对应的记录,可以是多个key
     *
     * @param String ... keys
     * @return 删除的记录数
     * */
    public static long del(String... keys)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        Jedis jedis = jedisPool.getResource();
        long count = jedis.del(keys);
        jedisPool.returnResource(jedis);
        return count;
    }
    
    /**
     * 删除keys对应的记录,可以是多个key
     *
     * @param String ... keys
     * @return 删除的记录数
     * */
    public static long del(byte[]... keys)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return 0l;
        }
        Jedis jedis = jedisPool.getResource();
        long count = jedis.del(keys);
        jedisPool.returnResource(jedis);
        return count;
    }
    
    /**
     * 判断key是否存在
     *
     * @param String key
     * @return boolean
     * */
    public static boolean exists(String key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return false;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        boolean exis = jedis.exists(key);
        jedisPool.returnResource(jedis);
        return exis;
    }
    
    /**
     * 对List,Set,SortSet进行排序,如果集合数据较大应避免使用这个方法
     *
     * @param String key
     * @return List<String> 集合的全部记录
     * **/
    public static List<String> sort(String key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        List<String> list = jedis.sort(key);
        jedisPool.returnResource(jedis);
        return list;
    }
    
    /**
     * 对List,Set,SortSet进行排序或limit
     *
     * @param String key
     * @param SortingParams parame 定义排序类型或limit的起止位置.
     * @return List<String> 全部或部分记录
     * **/
    public static List<String> sort(String key, SortingParams parame)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        List<String> list = jedis.sort(key, parame);
        jedisPool.returnResource(jedis);
        return list;
    }
    
    /**
     * 返回指定key存储的类型
     *
     * @param String key
     * @return String string|list|set|zset|hash
     * **/
    public static String type(String key)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        // ShardedJedis sjedis = getShardedJedis();
        Jedis jedis = jedisPool.getResource();
        String type = jedis.type(key);
        jedisPool.returnResource(jedis);
        return type;
    }
    
    /**
     * 查找所有匹配给定的模式的键
     *
     * @param String key的表达式,*表示多个，？表示一个
     * */
    public static Set<String> keys(String pattern)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        Jedis jedis = jedisPool.getResource();
        Set<String> set = jedis.keys(pattern);
        jedisPool.returnResource(jedis);
        return set;
    }
    
    // *******************************************SortSet****************************
    
    /**
     * 根据key 获取对象
     * 
     * @param key
     * @return
     */
    public static <T> T getFromSession(HttpServletRequest request, String key, Class<T> clazz)
    {
        if(jedisPool==null) {
            logger.warn(" jedisPool is null......");
            return null;
        }
        try
        {
            String value = (String)request.getSession().getAttribute(key);
            return JSON.parseObject(value, clazz);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
}