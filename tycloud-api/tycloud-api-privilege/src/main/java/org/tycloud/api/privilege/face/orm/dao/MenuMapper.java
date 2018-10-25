package org.tycloud.api.privilege.face.orm.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.tycloud.api.privilege.face.orm.entity.Menu;

import java.util.List;

/**
 * <p>
  * 菜单管理 Mapper 接口
 * </p>
 *
 * @author magintursh
 * @since 2017-08-18
 */
public interface MenuMapper extends BaseMapper<Menu> {


    List<Menu> selectByRole(@Param("roleSequenceNbr") String roleSequenceNbr,
                            @Param("agencyCode") String agencyCode);


    List<Menu> selectByUser(@Param("userId") String userId,
                            @Param("agencyCode") String agencyCode);



}