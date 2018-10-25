package org.tycloud.core.auth.face.orm.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.tycloud.core.auth.face.orm.entity.SsoSessions;

/**
 * <p>
  * 登录SESSION Mapper 接口
 * </p>
 *
 * @author magintursh
 * @since 2017-07-06
 */
public interface SsoSessionsMapper extends BaseMapper<SsoSessions> {



   /* @Delete("delete from auth_sso_sessions where LOGIN_ID=#{loginId}")
    void deleteByLoginId(String loginId);


    @Select("select * from auth_sso_sessions where TOKEN=#{token}")
    SsoSessions queryByToken(String token);
*/

}