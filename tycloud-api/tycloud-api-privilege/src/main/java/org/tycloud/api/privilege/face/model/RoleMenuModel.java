package org.tycloud.api.privilege.face.model;

import org.tycloud.core.rdbms.model.BaseModel;

import java.util.Date;

/**
 * <p>
 * 角色与菜单关系表
 * </p>
 *
 * @author magintursh
 * @since 2017-08-18
 */
public class RoleMenuModel extends BaseModel {

    private static final long serialVersionUID = 28573735432548L;

    /**
     * 机构编号
     */
	private String agencyCode;
    /**
     * 角色ID
     */
	private String roleSequenceNbr;
    /**
     * 菜单ID
     */
	private String menuSequenceNbr;
	private String createUserId;
	private Date createTime;



	public String getAgencyCode() {
		return agencyCode;
	}

	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}

	public String getRoleSequenceNbr() {
		return roleSequenceNbr;
	}

	public void setRoleSequenceNbr(String roleSequenceNbr) {
		this.roleSequenceNbr = roleSequenceNbr;
	}

	public String getMenuSequenceNbr() {
		return menuSequenceNbr;
	}

	public void setMenuSequenceNbr(String menuSequenceNbr) {
		this.menuSequenceNbr = menuSequenceNbr;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


}
