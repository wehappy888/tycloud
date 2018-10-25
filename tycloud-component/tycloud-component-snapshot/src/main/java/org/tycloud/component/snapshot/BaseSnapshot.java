package org.tycloud.component.snapshot;

import java.util.Date;

/** 
 * 
 * <pre>
 *  Tyrest
 *  File: BaseSnapshot.java
 * 
 *  Tyrest, Inc.
 *  Copyright (C): 2016
 * 
 *  Description:
 *  TODO
 * 
 *  Notes:
 *  $Id: BaseSnapshot.java  Tyrest\magintrursh $ 
 * 
 *  Revision History
 *  &lt;Date&gt;,			&lt;Who&gt;,			&lt;What&gt;
 *  2016年11月17日		magintrursh		Initial.
 *
 * </pre>
 */
public interface BaseSnapshot
{



	 Long getMasterSequenceNbr();
	 void setMasterSequenceNbr(Long  masterSequenceNbr);
	 Date getMasterRecDate();
	 void setMasterRecDate(Date masterRecDate);
	Long getSequenceNbr();
	 void setSequenceNbr(Long sequenceNBR);
}

/*
*$Log: av-env.bat,v $
*/