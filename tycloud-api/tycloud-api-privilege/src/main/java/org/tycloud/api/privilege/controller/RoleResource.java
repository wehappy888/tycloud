package org.tycloud.api.privilege.controller;

import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tycloud.api.privilege.face.model.RoleModel;
import org.tycloud.api.privilege.face.service.RoleService;
import org.tycloud.api.privilege.face.service.UserRoleService;
import org.tycloud.component.event.RestEventTrigger;
import org.tycloud.core.foundation.constans.CoreConstans;
import org.tycloud.core.foundation.constans.PropertyValueConstants;
import org.tycloud.core.foundation.context.RequestContext;
import org.tycloud.core.foundation.utils.Bean;
import org.tycloud.core.foundation.utils.StringUtil;
import org.tycloud.core.restful.doc.TycloudOperation;
import org.tycloud.core.restful.doc.TycloudResource;
import org.tycloud.core.restful.utils.APILevel;
import org.tycloud.core.restful.utils.ResponseHelper;
import org.tycloud.core.restful.utils.ResponseModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author magintursh
 * @since 2017-08-18
 */
@TycloudResource(module = "privilege",value = "role")
@RequestMapping(value = "/v1/privilege/role")
@Api(value = "privilege-角色管理")
@RestController
public class RoleResource {




    @Autowired
    RoleService roleService;

    @Autowired
    UserRoleService userRoleService;

    @TycloudOperation( ApiLevel = APILevel.AGENCY,needAuth = false)
    @ApiOperation(value = "分页查询角色信息")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseModel<Page> queryForPage (
            @RequestParam(value = "roleName", required = false) String roleName,
            @RequestParam(value ="current") int current,
            @RequestParam(value = "size") int size) throws Exception
    {
        Page page = new Page();
        page.setCurrent(current);
        page.setSize(size);

        String agencyCode = null;
        if(!CoreConstans.CODE_SUPER_ADMIN.equals(RequestContext.getAgencyCode()))
        {
            agencyCode = RequestContext.getAgencyCode();
        }
        return ResponseHelper.buildRespons(roleService.qeuryByName(page,roleName, agencyCode));
    }




    @TycloudOperation( ApiLevel = APILevel.AGENCY)
    @ApiOperation(value = "获取当前机构所有的角色")
    @RequestMapping(value = "/agency", method = RequestMethod.GET)
    public ResponseModel<List<RoleModel>> selectByAgency () throws Exception
    {
        return ResponseHelper.buildRespons(roleService.selectByAgency(RequestContext.getAgencyCode()));
    }


    //获取用户角色列表
    @TycloudOperation( ApiLevel = APILevel.AGENCY)
    @ApiOperation(value = "获取指定用的的角色列表")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseModel<List<RoleModel>> selectByUser (String userId) throws Exception
    {
        return ResponseHelper.buildRespons(userRoleService.selectRoleByUser(userId, RequestContext.getAgencyCode()));
    }






    @TycloudOperation( ApiLevel = APILevel.AGENCY)
    @ApiOperation(value = "查询单个角色信息")
    @RequestMapping(value = "/{sequenceNBR}", method = RequestMethod.GET)
    public ResponseModel<RoleModel> queryByCode(
            @PathVariable(value = "sequenceNBR") String sequenceNBR) throws Exception {
        return ResponseHelper.buildRespons(Bean.toModel(roleService.selectById(sequenceNBR),new RoleModel()));
    }

    @TycloudOperation( ApiLevel = APILevel.AGENCY)
    @ApiOperation(value="创建角色")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseModel<RoleModel> createRole(@RequestBody RoleModel roleModel) throws Exception
    {
        roleModel.setCreateTime(new Date());
        roleModel.setLockStatus(PropertyValueConstants.LOCK_STATUS_UNLOCK);
        roleModel.setCreateUserId(RequestContext.getExeUserId());
        roleModel.setAgencyCode(RequestContext.getAgencyCode());
        roleModel = roleService.createRole(roleModel);
        return ResponseHelper.buildRespons(roleModel);
    }

    @TycloudOperation( ApiLevel = APILevel.AGENCY)
    @ApiOperation(value="更新角色")
    @RequestMapping(value = "/{sequenceNBR}", method = RequestMethod.PUT)
    @RestEventTrigger("roleUpdateEventHandler")
    public ResponseModel<RoleModel> updateRole(@RequestBody RoleModel roleModel, @PathVariable Long sequenceNBR) throws Exception
    {
        roleModel.setSequenceNbr(sequenceNBR);
        return ResponseHelper.buildRespons(roleService.updateRole(roleModel));
    }



    @TycloudOperation( ApiLevel = APILevel.SUPERADMIN)
    @ApiOperation(value="删除角色")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseModel<Boolean> deleteMenu(@PathVariable Long   id) throws Exception
    {

        return ResponseHelper.buildRespons(roleService.deleteRole(id));
    }


	
}
