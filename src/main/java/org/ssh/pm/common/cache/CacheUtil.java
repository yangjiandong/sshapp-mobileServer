package org.ssh.pm.common.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springside.modules.utils.spring.SpringContextHolder;

public class CacheUtil {

    //cacheName
    public static final String HZK = "hzk";
    public static final String COMMON = "common";
    public static final String INCOMECATEGORY = "inComeCategory";
    public static final String COSTCATEGORY = "costCategory";

    /**
     * 设置缓存
     */
    public static void setCache(String cacheName, String key, Object value) {
        CacheManager manager = (CacheManager) SpringContextHolder.getBean(
                "ehcacheManager");

        if (manager.getCache(cacheName) == null) {
            manager.addCache(cacheName);
        }

        Cache cache = manager.getCache(cacheName);
        cache.put(new Element(key, value));

        // manager.shutdown();
    }

    /**
     * 获取缓存的值
     */
    public static Object getCache(String cacheName, String key) {
        CacheManager manager = (CacheManager) SpringContextHolder.getBean(
                "ehcacheManager");

        Cache cache = manager.getCache(cacheName);

        if (cache == null) {
            return null;
        }

        Element element = cache.get(key);

        // manager.shutdown();
        if (element == null) {
            return null;
        } else {
            return element.getObjectValue();
        }
    }

    /**
     * 清除缓存
     */
    public static void removeCache(String cacheName, String key) {
        CacheManager manager = (CacheManager) SpringContextHolder.getBean(
                "ehcacheManager");

        Cache cache = manager.getCache(cacheName);

        if (cache != null) {
            cache.remove(key);
        }
    }

    public static void removeCache(String cacheName){
        CacheManager manager = (CacheManager) SpringContextHolder.getBean(
        "ehcacheManager");

        Cache cache = manager.getCache(cacheName);
        if (cache != null) {
            cache.removeAll();
        }
    }
}
