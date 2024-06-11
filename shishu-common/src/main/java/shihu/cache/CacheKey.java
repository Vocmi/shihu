package shihu.cache;

import java.util.concurrent.TimeUnit;

/**
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-06-08
 */
public class CacheKey {
    public String prefix;

    public Long timeout;

    public TimeUnit unit;

    public CacheKey() {
    }

    public CacheKey(String prefix, Long timeout, TimeUnit unit) {
        this.prefix = prefix;
        this.timeout = timeout;
        this.unit = unit;
    }

    public String getKey(Object... param) {
        return String.format(prefix, param);
    }
}
