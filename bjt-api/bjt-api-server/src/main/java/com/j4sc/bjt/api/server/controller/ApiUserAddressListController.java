package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.UserAddressListClient;
import com.j4sc.bjt.api.server.client.UserManageClient;
import com.j4sc.bjt.user.dao.entity.BjtUserAddressList;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.validator.NotNullValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 用户通讯录服务
 * @Author: chengyz
 * @CreateDate: 2018/4/10 15:34
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/addressList")
@Api(tags = {"帮家团用户通讯录服务"}, description = "帮家团用户通讯录服务 - 需授权")
public class ApiUserAddressListController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserAddressListController.class);

    @Autowired
    private UserAddressListClient userAddressListClient;
    @Autowired
    private UserManageClient userManageClient;

    @ApiOperation(value = "用户添加联系人到通讯录", notes = "用户添加联系人到通讯录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "被添加者的用户编号", name = "slaveUserId", required = true),
            @ApiImplicitParam(value = "被添加者的昵称", name = "slaveNickname"),
            @ApiImplicitParam(value = "被添加者用户名", name = "slaveUsername")
    })
    @RequestMapping(value = "save/AddressList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveAddressList(@RequestBody BjtUserAddressList bjtUserAddressList) {
        LOGGER.info("saveAddressList = >用户添加联系人到通讯录");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .on(bjtUserAddressList.getSlaveUserId(), new NotNullValidator("被添加者的用户编号"))
                .on(bjtUserAddressList.getSlaveNickname(), new NotNullValidator("被添加者的昵称"))
                .on(bjtUserAddressList.getSlaveUsername(), new NotNullValidator("被添加者用户名"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", getUserId());
        params.put("slave_user_id", bjtUserAddressList.getSlaveUserId());
        BaseResult<List<BjtUserAddressList>> listBaseResult = userAddressListClient.selectAll(params);
        if (listBaseResult.getData().size() > 0 && listBaseResult.status == BaseResultEnum.SUCCESS.getStatus())
            return new BaseResult(BaseResultEnum.ERROR, "不能重复添加联系人");
        bjtUserAddressList.setUserId(getUserId());
        bjtUserAddressList.setStatus(1);//状态: 1是正常、2是删除、3是黑名单
        return userAddressListClient.insert(bjtUserAddressList);
    }

    @ApiOperation(value = "通过用户账号获取用户信息", notes = "exist为0表示不是通讯录联系人，1表示是通讯录联系人")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名", name = "username", required = true)
    })
    @RequestMapping(value = "select/AddressListUser", method = RequestMethod.GET)
    public BaseResult<Map<String, String>> selectBjtUserUser(@RequestParam("username") String username) {
        LOGGER.info("selectBjtUserUser = >");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(username, new NotNullValidator("用户名")).doValidate().result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUsername(username);
        if (userBaseResult.getData() == null || userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return new BaseResult(BaseResultEnum.ERROR, "获取失败");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", getUserId());
        map.put("slave_user_id", userBaseResult.getData().getUserId());
        BaseResult<List<BjtUserAddressList>> addressListBaseResult = userAddressListClient.selectAll(map);
        if (addressListBaseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return new BaseResult(BaseResultEnum.ERROR, "获取失败");
        }
        Map<String, String> resultMap = new HashMap<>();
        if (addressListBaseResult.getData().size() > 0) {
            resultMap.put("exist", "1");//1表示是通讯录联系人
        } else {
            resultMap.put("exist", "0");//0表示不是通讯录联系人
        }
        resultMap.put("slaveNickname", userBaseResult.getData().getNickname());
        resultMap.put("slaveUsername", userBaseResult.getData().getUsername());
        resultMap.put("slaveUserId", userBaseResult.getData().getUserId());
        resultMap.put("avatar", userBaseResult.getData().getAvatar());

        return new BaseResult(BaseResultEnum.SUCCESS, resultMap);
    }

    @ApiOperation(value = "用户修改联系人", notes = "用户修改联系人")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "被添加者的用户编号", name = "slaveUserId", required = true),
            @ApiImplicitParam(value = "通讯录编号", name = "addressListId", required = true),
            @ApiImplicitParam(value = "被添加者的昵称", name = "slaveNickname"),
            @ApiImplicitParam(value = "被添加者用户名", name = "slaveUsername")
    })
    @RequestMapping(value = "update/AddressList", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateAddressList(@RequestBody BjtUserAddressList bjtUserAddressList) {
        LOGGER.info("updateAddressList = >用户修改联系人");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .on(String.valueOf(bjtUserAddressList.getAddressListId()), new NotNullValidator("通讯录编号"))
                .on(bjtUserAddressList.getSlaveUserId(), new NotNullValidator("被添加者的用户编号"))
                .on(bjtUserAddressList.getSlaveNickname(), new NotNullValidator("被添加者的昵称"))
                .on(bjtUserAddressList.getSlaveUsername(), new NotNullValidator("被添加者用户名"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserAddressList> addressListBaseResult = userAddressListClient.selectById(String.valueOf(bjtUserAddressList.getAddressListId()));
        if (addressListBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus() || addressListBaseResult.getData() == null)
            return addressListBaseResult;
        if (!addressListBaseResult.getData().getUserId().equals(getUserId()))
            return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        bjtUserAddressList.setUserId(getUserId());
        return userAddressListClient.updateById(bjtUserAddressList);
    }

    @ApiOperation(value = "用户删除联系人", notes = "用户删除联系人")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "通讯录编号", name = "addressListId", required = true),
    })
    @RequestMapping(value = "delete/AddressList", method = RequestMethod.DELETE)
    public BaseResult deleteAddressList(@RequestParam("addressListId") String addressListId) {
        LOGGER.info("deleteAddressList = >用户删除联系人");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(addressListId, new NotNullValidator("通讯录编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserAddressList> addressListBaseResult = userAddressListClient.selectById(addressListId);
        if (addressListBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {return addressListBaseResult;}
        if (addressListBaseResult.getData() == null) {return new BaseResult(BaseResultEnum.ERROR, "当前联系人不存在");}
        if (!addressListBaseResult.getData().getUserId().equals(getUserId()))
            return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        return userAddressListClient.deleteById(addressListId);
    }

    @ApiOperation(value = "用户获取通讯录列表", notes = "用户获取通讯录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "关键词", name = "key")
    })
    @RequestMapping(value = "select/AllAddressList", method = RequestMethod.GET)
    public BaseResult<List<BjtUserAddressList>> selectAllAddressList(@RequestParam(name = "key", defaultValue = "") String key) {
        LOGGER.info("selectAllAddressList = >用户获取通讯录列表");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return userAddressListClient.selectAllAddressList(getUserId(), key);
    }

    @ApiOperation(value = "用户获取通讯录详情", notes = "用户获取通讯录详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "通讯录编号", name = "addressListId", required = true),
    })
    @RequestMapping(value = "select/AddressList", method = RequestMethod.GET)
    public BaseResult<BjtUserAddressList> selectAddressList(@RequestParam("addressListId") String addressListId) {
        LOGGER.info("selectAddressList = >用户获取通讯录详情");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .on(addressListId, new NotNullValidator("通讯录编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return userAddressListClient.selectById(addressListId);
    }

}
