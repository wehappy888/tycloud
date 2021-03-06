package org.tycloud.api.privilege.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tycloud.core.restful.doc.TycloudResource;

/**
 * <p>
 * 角色与菜单关系表 前端控制器
 * </p>
 *
 * @author magintursh
 * @since 2017-08-18
 */
@RestController
@TycloudResource(module = "privilege",value = "agencymenu")
@RequestMapping(value = "/v1/privilege/agencymenu")
@Api(value = "privilege-机构菜单权限")
public class AgencyMenuResource {
	
}
