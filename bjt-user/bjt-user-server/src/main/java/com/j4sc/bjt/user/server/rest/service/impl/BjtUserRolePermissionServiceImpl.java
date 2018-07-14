package com.j4sc.bjt.user.server.rest.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.user.dao.mapper.BjtUserRolePermissionMapper;
import com.j4sc.bjt.user.dao.entity.BjtUserRolePermission;
import com.j4sc.bjt.user.rest.api.BjtUserRolePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
* @Description: BjtUserRolePermission Service实现
* @Author: LongRou
* @CreateDate: 2018/3/30.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
@RestController
@RequestMapping("BjtUserRolePermission")
@ApiIgnore
public class BjtUserRolePermissionServiceImpl extends BaseServiceImpl<BjtUserRolePermissionMapper, BjtUserRolePermission> implements BjtUserRolePermissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtUserRolePermissionServiceImpl.class);

    //根据角色ID替换权限
    @Override
    public boolean replacePermissionByRoleId(Integer id, List<Integer> permissionList) {
        baseMapper.delete(new EntityWrapper<BjtUserRolePermission>().eq("role_id",id));
        BjtUserRolePermission authRolePermission = new BjtUserRolePermission();
        authRolePermission.setRoleId(id);
        permissionList.forEach(item ->{
            authRolePermission.setPermissionId(item);
            baseMapper.insert(authRolePermission);
        });
        return true;
    }
}