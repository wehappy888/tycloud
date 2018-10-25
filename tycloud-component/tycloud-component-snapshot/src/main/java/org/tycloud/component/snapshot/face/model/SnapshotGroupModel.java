package org.tycloud.component.snapshot.face.model;

import org.tycloud.core.rdbms.model.BaseModel;

/**
 * <p>
 * 快照分组信息
 * </p>
 *
 * @author magintursh
 * @since 2017-08-25
 */
public class SnapshotGroupModel extends BaseModel {

    private static final long serialVersionUID = 48943165169464L;


    /**
     * 快照分组类型编号
     */
	private String groupCode;
    /**
     * 快照分组名称
     */
	private String groupName;
    /**
     * 快照主键
     */
	private Long  snapshotSequenceNbr;
    /**
     * 快照类型（系统默认按快照对象分类）
     */
	private String snapshotType;
    /**
     * 是否可为空
     */
	private String nullable;
    /**
     * 业务标识code
     */
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

	public Long getSnapshotSequenceNbr() {
		return snapshotSequenceNbr;
	}

	public void setSnapshotSequenceNbr(Long snapshotSequenceNbr) {
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
