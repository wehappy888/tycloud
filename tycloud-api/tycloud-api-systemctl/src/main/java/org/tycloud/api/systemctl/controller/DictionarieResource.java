package org.tycloud.api.systemctl.controller;

import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tycloud.api.systemctl.face.model.DictionarieModel;
import org.tycloud.api.systemctl.face.service.DictionarieService;
import org.tycloud.core.foundation.utils.ValidationUtil;
import org.tycloud.core.restful.doc.TycloudOperation;
import org.tycloud.core.restful.doc.TycloudResource;
import org.tycloud.core.restful.utils.APILevel;
import org.tycloud.core.restful.utils.ResponseHelper;
import org.tycloud.core.restful.utils.ResponseModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by magintursh on 2017-05-03.
 */
@TycloudResource(module = "systemctl",value = "DictionarieResource")
@RequestMapping(value = "/v1/systemctl/dictionary")
@Api(value = "systemctl-字典管理")
@RestController
public class DictionarieResource {
    private final Logger logger = Logger.getLogger(DictionarieResource.class);


    @Autowired
    DictionarieService dictionarieService;

    @TycloudOperation( ApiLevel = APILevel.ALL)
    @ApiOperation(value="创建字典")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseModel<DictionarieModel> createDictionary(@RequestBody DictionarieModel dictionaryModel) throws Exception
    {
        dictionaryModel = dictionarieService.createDict(dictionaryModel);
        return ResponseHelper.buildRespons(dictionaryModel);
    }


    @TycloudOperation(ApiLevel = APILevel.AGENCY)
    @ApiOperation(value="删除字典")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseModel<Boolean> deleteDictionary(@RequestBody String[] ids) throws Exception
    {
        List<Long> seqs = new ArrayList<>();
        for(int i = 0;i<ids.length ;i++)
        {
            seqs.add(Long.parseLong(ids[i]));
        }
        return ResponseHelper.buildRespons(dictionarieService.deleteDicts(seqs));
    }


    @TycloudOperation( ApiLevel = APILevel.AGENCY)
    @ApiOperation(value="更新字典")
    @RequestMapping(value = "/{sequenceNbr}", method = RequestMethod.PUT)
    public ResponseModel<DictionarieModel> updateDictionary(
            @RequestBody DictionarieModel dictionaryModel,
            @PathVariable(value="sequenceNbr") String sequenceNbr) throws Exception {
        dictionaryModel.setDictCode(sequenceNbr);
        return ResponseHelper.buildRespons(dictionarieService.updateDict(dictionaryModel));
    }


    @TycloudOperation( ApiLevel = APILevel.ALL)
    @RequestMapping(value = "/{dictCode}", method = RequestMethod.GET)
    public ResponseModel<DictionarieModel> queryByCode(
            @PathVariable(value = "dictCode") String dictCode) throws Exception {
        return ResponseHelper.buildRespons(dictionarieService.queryByCode("SUPER_ADMIN",dictCode));
    }


    @TycloudOperation( ApiLevel = APILevel.ALL)
    @ApiOperation(value = "分页查询字典信息")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseModel<Page> queryForPage(
            @RequestParam(value = "agencyCode", required = false) String agencyCode,
            @RequestParam(value = "buType", required = false) String buType,
            @RequestParam(value = "dictAlias", required = false) String dictAlias,
            @RequestParam(value = "dictName", required = false) String dictName,
            @RequestParam(value = "dictCode", required = false) String dictCode,
            @RequestParam(value ="current") int current,
            @RequestParam(value = "size") int size) throws Exception
    {
        Page page = new Page();
        page.setCurrent(current);
        page.setSize(size);
        return ResponseHelper.buildRespons(dictionarieService.queryDictPage(page,agencyCode,buType,dictAlias,dictName,dictCode));
    }


    @TycloudOperation(ApiLevel = APILevel.ALL)
    @ApiOperation(value = "检查字典编号是否可用")
    @RequestMapping(value = "/{dictCode}/available", method = RequestMethod.GET)
    public ResponseModel<Boolean> isDictionaryCodeAvailable(
            @PathVariable(value = "dictCode") String dictCode) throws Exception {
        return ResponseHelper.buildRespons(ValidationUtil.isEmpty(dictionarieService.queryByCode("SUPER_ADMIN",dictCode)));
    }

    @TycloudOperation(ApiLevel = APILevel.ALL,needAuth = false)
    @ApiOperation(value = "测试接口")
    @RequestMapping(value = "/string/teststring", method = RequestMethod.GET)
    public String isDictionaryCodeAvailable() throws Exception {
        return "success";
    }



}