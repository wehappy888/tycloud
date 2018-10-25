package org.tycloud.core.rdbms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by yaohelang on 2018/5/20.
 */
@ConfigurationProperties(prefix = "mybatisplus")
public class MybatisplusProperties {

    private String dbType = "mysql";
    private int     idType = 2;

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }
}
