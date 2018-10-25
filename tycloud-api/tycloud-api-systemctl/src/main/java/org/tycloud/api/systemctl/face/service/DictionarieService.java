package org.tycloud.api.systemctl.face.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.stereotype.Component;
import org.tycloud.api.systemctl.face.model.DictionarieModel;
import org.tycloud.api.systemctl.face.orm.dao.DictionarieMapper;
import org.tycloud.api.systemctl.face.orm.entity.Dictionarie;
import org.tycloud.core.rdbms.annotation.Operate;
import org.tycloud.core.rdbms.annotation.Operator;
import org.tycloud.core.rdbms.service.BaseService;

import java.util.List;

/**
 * <p>
 * 系统字典表 服务类
 * </p>
 *
 * @author magintursh
 * @since 2017-06-27
 */
@Component
public class DictionarieService extends BaseService<DictionarieModel,Dictionarie,DictionarieMapper> implements IService<Dictionarie> {


    public static final String CACHEKEY_DICTIONARY="DICTIONARY";



    public DictionarieModel createDict(DictionarieModel model)throws Exception
    {
        return this.createWithModel(model);
    }

    public boolean deleteDicts(List<Long > seqs)throws Exception
    {
        for(Long seq:seqs)
            this.deleteBySeq(seq);
        return true;
    }

    public DictionarieModel updateDict(DictionarieModel model)throws Exception
    {
        return this.updateWithModel(model);
    }


    public DictionarieModel queryByCode(String agencyCode,String dictCode) throws Exception
    {
        return this.queryModelByParams(agencyCode,dictCode);
    }


    public Page<DictionarieModel> queryDictPage(Page page, String agencyCode ,
                                               String buType,
                                               @Operate(value = Operator.LIKE) String dictAlias,
                                               @Operate(value = Operator.LIKE) String dictName,
                                               String dictCode) throws Exception
    {
        return this.queryForPage(page,null,false,
                agencyCode,buType,dictAlias,dictName,dictCode);
    }



    public List<DictionarieModel> queryDictList(String agencyCode ,
                                               String buType,
                                               @Operate(value = Operator.LIKE) String dictAlias,
                                               @Operate(value = Operator.LIKE) String dictName,
                                               String dictCode) throws Exception
    {
        return this.queryForList(null,false,
                agencyCode,buType,dictAlias,dictName,dictCode);

    }


}
