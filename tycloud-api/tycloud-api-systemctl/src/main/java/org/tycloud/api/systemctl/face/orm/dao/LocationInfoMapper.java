package org.tycloud.api.systemctl.face.orm.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.tycloud.api.systemctl.face.orm.entity.LocationInfo;

import java.util.List;

/**
 * Created by magintursh on 2017-06-21.
 */
public interface LocationInfoMapper extends BaseMapper<LocationInfo>{

    @Select("select * from systemctl_location_info  t where t.PARENT_CODE = #{parentCode}")
    List<LocationInfo> queryByParentCode(String parentCode);

}
