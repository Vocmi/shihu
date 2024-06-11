package shihu.cache;

import java.util.concurrent.TimeUnit;

/**
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-06-08
 */
public class UserCacheKey extends CacheKey {
    public static final CacheKey USER_ID_BLOOM_FILTER = new CacheKey("user:bloomfilter:use_id:", null, null);

    public static final CacheKey LOGIN_TOKEN = new CacheKey("user:token:login:%s", 60 * 24 * 7L, TimeUnit.MINUTES);

    public static final CacheKey LIST = new CacheKey("user:list", 60 * 24 * 7L, TimeUnit.MINUTES);

    public static final CacheKey PAGE = new CacheKey("user:page:%s:%s", 60 * 24 * 7L, TimeUnit.MINUTES);

    public static final CacheKey EMAIL_CODE = new CacheKey("user:email:%s:%s", 5L, TimeUnit.MINUTES);
}