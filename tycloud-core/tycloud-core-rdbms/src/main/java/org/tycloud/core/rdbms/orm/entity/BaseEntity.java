package org.tycloud.core.rdbms.orm.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by magintursh on 2017-06-17.
 */
public class   BaseEntity<P extends Model> extends Model<P>{

    private static final long serialVersionUID = 5354351431289739L;


    @TableId(value = "SEQUENCE_NBR",type = IdType.ID_WORKER)
    protected Long sequenceNbr;

    @TableField("REC_DATE")
    protected Date recDate;

    @TableField("REC_USER_ID")
    protected String recUserId;

    @TableField("REC_STATUS")
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

    @Override
    protected Serializable pkVal() {
        return this.sequenceNbr;
    }
}
