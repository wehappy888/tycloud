package org.tycloud.component.cache.annotation;


import org.tycloud.core.foundation.enumeration.StorageMode;

import java.lang.annotation.*;


/***
 * 注解于entity类，通过AOP自动缓存相应的entity对象
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EntityCache {

    String [] keyPrefix()  default {};//实体的缓存key前缀，每个实体可以有多个
    StorageMode storageMode() default StorageMode.HASH;
    long    expire() default 1*60*60L;//过期时间（秒）默认一小时

    boolean cacheOnly() default false;//只保存于缓存

    String [] removeListCache() default {};//实体缓存更新以后需要刷新的列表缓存

}
