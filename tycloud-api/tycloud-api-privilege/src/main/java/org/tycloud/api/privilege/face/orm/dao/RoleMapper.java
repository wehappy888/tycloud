package org.tycloud.api.privilege.face.orm.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.tycloud.api.privilege.face.orm.entity.Role;

import java.util.List;

/**
 * <p>
  * 角色表 Mapper 接口
 * </p>
 *
 * @author magintursh
 * @since 2017-08-18
 */
public interface RoleMapper extends BaseMapper<Role> {



    @Select("SELECT" +
            " r.*" +
            " FROM" +
            " privilege_role r" +
            " JOIN privilege_user_role u ON r.SEQUENCE_NBR = u.ROLE_SEQUENCE_NBR" +
            " WHERE" +
            " u.USER_ID          = #{userId}" +
            " AND u.AGENCY_CODE  = #{agencyCode}" +
            " AND r.AGENCY_CODE  = #{agencyCode}")
    List<Role> selectByUser(String userId, String agencyCode);


}