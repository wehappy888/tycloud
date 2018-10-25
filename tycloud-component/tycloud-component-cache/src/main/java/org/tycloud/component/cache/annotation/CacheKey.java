package org.tycloud.component.cache.annotation;


import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheKey {

    String DEFAULT_PROPERTY = "sequenceNbr";

    String keyProperty()    default "";

    String [] keyPrefix() ;
    int[]     order();
}
