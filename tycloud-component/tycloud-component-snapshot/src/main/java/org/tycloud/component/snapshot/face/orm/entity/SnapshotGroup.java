package org.tycloud.component.snapshot.face.orm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import org.tycloud.core.rdbms.orm.entity.BaseEntity;

/**
 * <p>
 * 快照分组信息
 * </p>
 *
 * @author magintursh
 * @since 2017-08-25
 */
@TableName("snapshot_group")
public class SnapshotGroup extends BaseEntity<SnapshotGroup> {

    private static final long serialVersionUID = 156415131694898L;


    /**
     * 快照分组类型编号
     */
	@TableField("GROUP_CODE")
	private String groupCode;
    /**
     * 快照分组名称
     */
	@TableField("GROUP_NAME")
	private String groupName;
    /**
     * 快照主键
     */
	@TableField("SNAPSHOT_SEQUENCE_NBR")
	private String snapshotSequenceNbr;
    /**
     * 快照类型（系统默认按快照对象分类）
     */
	@TableField("SNAPSHOT_TYPE")
	private String snapshotType;
    /**
     * 是否可为空
     */
	@TableField("NULLABLE")
	private String nullable;
    /**
     * 业务标识code
     */
	@TableField("BUSINESS_CODE")
	private String businessCode;

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getSnapshotSequenceNbr() {
		return snapshotSequenceNbr;
	}

	public void setSnapshotSequenceNbr(String  snapshotSequenceNbr) {
		this.snapshotSequenceNbr = snapshotSequenceNbr;
	}

	public String getSnapshotType() {
		return snapshotType;
	}

	public void setSnapshotType(String snapshotType) {
		this.snapshotType = snapshotType;
	}

	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

}
