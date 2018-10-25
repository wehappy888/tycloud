package org.tycloud.component.cache.annotation;

import java.lang.annotation.*;

/**
 * Created by yaohelang on 2018/6/8.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ListCache {


    /**
     * 列表缓存key前缀
     * @return
     */
    String  keyPrefix() ;


    /**
     * 列表缓存是否需要刷新，默认是需要根据 指定的 entity来刷新缓存
     * @return
     */
    boolean needRefresh() default true;
}
