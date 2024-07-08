package shihu.cache;

import java.util.concurrent.TimeUnit;

/**
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-06-08
 */
public class UserCacheKey extends CacheKey {
    public static final CacheKey TOKEN = new CacheKey("login:token:%s", 36000L, TimeUnit.MINUTES);
    public static final CacheKey EMAIL_CODE = new CacheKey("user:email:%s:%s", 5L, TimeUnit.MINUTES);
}