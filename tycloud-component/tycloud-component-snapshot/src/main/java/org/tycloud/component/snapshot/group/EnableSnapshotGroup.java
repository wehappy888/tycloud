package org.tycloud.component.snapshot.group;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableSnapshotGroup {
    String  groupName();

    String groupCode();

    boolean enableSave() default false;

}
