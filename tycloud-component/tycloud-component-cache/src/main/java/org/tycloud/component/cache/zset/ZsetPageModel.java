package org.tycloud.component.cache.zset;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yaohelang on 2018/7/10.
 */
public class ZsetPageModel<T> implements Serializable {

    private List<T> members;

    private Long size;

    public List<T> getMembers() {
        return members;
    }

    public void setMembers(List<T> members) {
        this.members = members;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
