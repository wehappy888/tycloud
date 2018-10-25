package org.tycloud.component.cache.enumeration;

public enum CacheType {


    /**
     * 不可清理的缓存
     */
    INERASABLE(-1),

    /**
     * 可被清理的缓存
     */
    ERASABLE(1*60*60),
    MAPPER_CACHE(1*60*60),
    ENTITY_CACHE(2*60*60),
    MANUALLY_CACHE(2*60*60);





    private long defaultExpire;//默认时间（秒）



    CacheType(long  defaultExpire)
    {
        this.defaultExpire = defaultExpire;
    }


    public long getDefaultExpire() {
        return defaultExpire;
    }
}
