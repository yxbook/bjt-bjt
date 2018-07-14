package com.j4sc.bjt.user.server.client;

import com.j4sc.auth.dao.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/3/29 0029 下午 3:51
 * @Version: 1.0
 **/
@Component
public class J4scAuthClientHystrix implements J4scAuthClient{
    @Override
    public String login(AuthUser authUser) throws Exception {
        return null;
    }

    @Override
    public List<AuthPermission> getAllPermission() {
        return null;
    }

    @Override
    public List<AuthPermission> selectAuthPermissionByAuthUserId(String integer) {
        return null;
    }

    @Override
    public List<AuthPermission> selectAuthPermissionByAuthUserIdByCache(String integer) {
        return null;
    }

    @Override
    public List<AuthRole> selectAuthRoleByAuthUserId(String integer) {
        return null;
    }

    @Override
    public List<AuthRole> selectAuthRoleByAuthUserIdByCache(String integer) {
        return null;
    }

    @Override
    public List<AuthRolePermission> selectAuthRolePermisstionByAuthRoleId(String integer) {
        return null;
    }

    @Override
    public List<AuthUserPermission> selectAuthUserPermissionByAuthUserId(String integer) {
        return null;
    }

/*
    public List<AuthPermission> selectAuthPermissionByAuthUserId(String s) {
        return null;
    }
*/


    @Override
    public List<AuthSystem> selectAuthSystemByExample(AuthSystem authSystem) {
        return null;
    }

    @Override
    public List<AuthOrganization> selectAuthOrganizationByExample(AuthOrganization authOrganization) {
        return null;
    }

    @Override
    public AuthUser selectAuthUserByUsername(String s) {
        return null;
    }

    @Override
    public boolean insertAuthLogSelective(AuthLog authLog) {
        return false;
    }

    public List<AuthPermission> getAllPermissionBySystemId(int i) {
        return null;
    }
}
