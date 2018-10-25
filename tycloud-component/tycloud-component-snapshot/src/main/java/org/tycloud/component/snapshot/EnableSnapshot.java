package org.tycloud.component.snapshot;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.tycloud.core.rdbms.orm.entity.BaseEntity;

import java.lang.annotation.*;

/** 
 * 
 * <pre>
 *  Tyrest
 *  File: EnableSnapshot.java
 * 
 *  Tyrest, Inc.
 *  Copyright (C): 2016
 * 
 *  Description:
 *  Description:开启实体快照的注解
 * 
 *  Notes:
 *  $Id: EnableSnapshot.java  Tyrest\magintrursh $ 
 * 
 *  Revision History
 *  &lt;Date&gt;,			&lt;Who&gt;,			&lt;What&gt;
 *  2016年11月17日		magintrursh		Initial.
 *
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableSnapshot {
	Class<? extends BaseSnapshot> snapshotClass();

	Class<? extends BaseMapper> mapper();
}

/*
*$Log: av-env.bat,v $
*/