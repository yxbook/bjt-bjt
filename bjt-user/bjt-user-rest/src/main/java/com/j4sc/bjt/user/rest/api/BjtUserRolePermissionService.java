package com.j4sc.bjt.user.rest.api;

import com.j4sc.common.base.BaseService;
import com.j4sc.bjt.user.dao.entity.BjtUserRolePermission;

import java.util.List;

/**
* @Description: BjtUserRolePermission Service接口
* @Author: LongRou
* @CreateDate: 2018/3/30.
* @Version: 1.0
**/
public interface BjtUserRolePermissionService extends BaseService<BjtUserRolePermission> {
    //更新权限
    boolean replacePermissionByRoleId(Integer id, List<Integer> permissionList);
}