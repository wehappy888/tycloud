package org.tycloud.component.cache.pipeline;

import org.tycloud.core.foundation.enumeration.StorageMode;

import java.util.List;

/**
 * Created by yaohelang on 2018/7/2.
 */
public class OperationForModelsDetail extends PipelineOperation{

    private String propertyName;

    public OperationForModelsDetail(String hkey, String mkey, StorageMode storageMode, String propertyName) {
        super(hkey, mkey, storageMode);
        this.propertyName = propertyName;
    }

    public OperationForModelsDetail(String hkey, String mkey, StorageMode storageMode, ResultCallBack resultCallBack, String propertyName) {
        super(hkey, mkey, storageMode, resultCallBack);
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
