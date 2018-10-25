package org.tycloud.component.snapshot.group;


import org.tycloud.component.snapshot.BaseSnapshot;
import org.tycloud.core.rdbms.service.BaseService;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableSnapshotKey {


    Class<? extends BaseService> mainService();

    String mepperMethod();

    Class<?extends BaseSnapshot> snapshotEntity();






}
