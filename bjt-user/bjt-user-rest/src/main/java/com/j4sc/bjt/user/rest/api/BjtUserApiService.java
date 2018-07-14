package com.j4sc.bjt.user.rest.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.user.dao.entity.*;
import com.j4sc.common.base.BaseResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/3/29 0029 下午 3:03
 * @Version: 1.0
 **/
public interface BjtUserApiService {
    @RequestMapping(
            value = {"/login"},
            method = {RequestMethod.POST},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    BaseResult<BjtUserUser> login(@RequestBody BjtUserUser bjtUserUser);

    @RequestMapping(
            value = {"/updateUser"},
            method = {RequestMethod.POST},
            consumes = MediaType.APPLICATION_JSON_VALUE

    )
    BaseResult<BjtUserUser> updateUser(@RequestBody BjtUserUser bjtUserUser);

    @RequestMapping(
            value = {"/saveUser"},
            method = {RequestMethod.POST},
            consumes = MediaType.APPLICATION_JSON_VALUE

    )
    BaseResult<BjtUserUser> saveUser(@RequestBody BjtUserUser bjtUserUser);

    @RequestMapping(
            value = {"/updatePassword"},
            method = {RequestMethod.POST},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    BaseResult updatePassword(@RequestBody Map<String, Object> params);

    @RequestMapping(
            value = {"/findUserByUserId"},
            method = {RequestMethod.GET})
    BaseResult<BjtUserUser> findUserByUserId(@RequestParam("userId") String userId);

    @RequestMapping(
            value = {"/selectUserList"},
            method = {RequestMethod.GET})
    BaseResult<List<BjtUserUser>> selectUserList(@RequestParam("username") String username);

    @RequestMapping(
            value = {"/findUserByPhone"},
            method = {RequestMethod.GET})
    BaseResult findUserByPhone(@RequestParam("phone") String phone);

    @RequestMapping(
            value = {"/findUserByUserTel"},
            method = {RequestMethod.GET})
    BaseResult<BjtUserUser> findUserByUserTel(@RequestParam("phone") String phone);
    @RequestMapping(
            value = {"/findUserByUsername"},
            method = {RequestMethod.GET})
    BaseResult<BjtUserUser> findUserByUsername(@RequestParam("username") String username);

    @RequestMapping(
            value = {"/deleteUsers"},
            method = {RequestMethod.DELETE})
    BaseResult deleteUsers(@RequestParam("userIds") String userIds);

    @RequestMapping(
            value = {"/findListByIdList"},
            method = {RequestMethod.POST},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    BaseResult<List<BjtUserUser>>  findListByIdList(@RequestBody List<String>  list);

    /*===================================================================*/
    /**
     * 获取所有权限
     *
     * @return
     */
    @RequestMapping(value = "/getAllPermission", method = RequestMethod.GET)
    BaseResult<List<BjtUserPermission>> getAllPermission();
    /**
     * 根据用户id获取所拥有的权限(用户和角色权限合集)
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/selectPermissionByUserId", method = RequestMethod.GET)
    BaseResult<List<BjtUserPermission>> selectPermissionByUserId(@RequestParam("userId") String userId,@RequestParam("buildId") String buildId);

    /**
     * 根据用户id获取所拥有的权限(用户和角色权限合集)
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/selectPermissionByUserIdByCache", method = RequestMethod.GET)
    BaseResult<List<BjtUserPermission>> selectPermissionByUserIdByCache(@RequestParam("userId") String userId,@RequestParam("buildId") String buildId);
    /**
     * 根据用户id获取所属的角色
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/selectBjtUserRoleByhUserId", method = RequestMethod.GET)
    BaseResult<List<BjtUserRole>> selectBjtUserRoleByUserId(@RequestParam("userId") String userId,@RequestParam("buildId") String buildId);

    /**
     * 根据用户id获取所属的角色
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/selectBjtUserRoleByUserIdByCache", method = RequestMethod.GET)
    BaseResult<List<BjtUserRole>> selectBjtUserRoleByUserIdByCache(@RequestParam("userId") String userId,@RequestParam("buildId") String buildId);

    /**
     * 根据角色id获取所拥有的权限
     *
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/selectBjtUserRolePermisstionByBjtUserRoleId", method = RequestMethod.GET)
    BaseResult<List<BjtUserRolePermission>> selectBjtUserRolePermisstionByBjtUserRoleId(@RequestParam("roleId") String roleId);
    /**
     * 根据角色id 楼宇ID获取所拥有的权限
     */
    @RequestMapping(
            value = {"/findUserBuildInfoByBU"},
            method = {RequestMethod.POST},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    BaseResult<List<Map<String, Object>>> findUserBuildInfoByBU(@RequestBody Map<String, Object> params);
    /**
     * 根据角色id 楼宇ID 移除权限和角色
     */
    @RequestMapping(
            value = {"/removeRolePermissionByBU"},
            method = {RequestMethod.DELETE},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    BaseResult removeRolePermissionByBU(@RequestBody Map<String, Object> params);
    /*===================================================================*/
    @RequestMapping(
            value = {"/saveFeedBack"},
            method = {RequestMethod.POST},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    BaseResult saveFeedBack(@RequestBody BjtUserFeedback bjtUserFeedback);

    @RequestMapping(
            value = {"/findWallet"},
            method = {RequestMethod.GET}
    )
    BaseResult<BjtUserWallet> findWallet(@RequestParam("userId") String userId);

}
