package org.tycloud.core.rdbms.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.boot.jackson.JsonComponent;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by magintursh on 2017-06-17.
 */
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 457432767545432436L;


    protected Long  sequenceNbr;

    @JsonIgnore
    protected Date recDate;

    @JsonIgnore
    protected String recUserId;

    @JsonIgnore
    protected String recStatus;


    public Long getSequenceNbr() {
        return sequenceNbr;
    }

    public void setSequenceNbr(Long sequenceNbr) {
        this.sequenceNbr = sequenceNbr;
    }

    public Date getRecDate() {
        return recDate;
    }

    public void setRecDate(Date recDate) {
        this.recDate = recDate;
    }

    public String getRecUserId() {
        return recUserId;
    }

    public void setRecUserId(String recUserId) {
        this.recUserId = recUserId;
    }

    public String getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(String recStatus) {
        this.recStatus = recStatus;
    }
}
