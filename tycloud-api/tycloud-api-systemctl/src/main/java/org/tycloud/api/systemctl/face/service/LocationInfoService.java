package org.tycloud.api.systemctl.face.service;

import com.baomidou.mybatisplus.service.IService;
import org.springframework.stereotype.Service;
import org.tycloud.api.systemctl.face.model.LocationInfoModel;
import org.tycloud.api.systemctl.face.orm.dao.LocationInfoMapper;
import org.tycloud.api.systemctl.face.orm.entity.LocationInfo;
import org.tycloud.core.rdbms.service.BaseService;

import java.util.List;

/**
 * Created by magintursh on 2017-06-21.
 */
@Service("locationInfoService")
public class LocationInfoService extends BaseService<LocationInfoModel,LocationInfo,LocationInfoMapper> implements IService<LocationInfo> {


    public static final String LOCATIONINFO="LOCATIONINFO";


    public List<LocationInfoModel> getByParent(String  parentCode) throws Exception
    {
       return this.queryForList(null,false,parentCode);
    }

    public LocationInfoModel getByCode(String  locationCode) throws Exception
    {
        LocationInfoModel model = new LocationInfoModel();
        model.setLocationCode(locationCode);
        return queryByModel(model);
    }
}
