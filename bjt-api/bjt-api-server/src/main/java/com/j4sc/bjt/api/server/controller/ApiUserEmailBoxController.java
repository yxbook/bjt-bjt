package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.UserAddressListClient;
import com.j4sc.bjt.api.server.client.UserEmailBoxClient;
import com.j4sc.bjt.api.server.client.UserEmailBoxRecordClient;
import com.j4sc.bjt.user.dao.entity.BjtUserAddressList;
import com.j4sc.bjt.user.dao.entity.BjtUserEmailBox;
import com.j4sc.bjt.user.dao.entity.BjtUserEmailBoxRecord;
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
 * @Description: 用户邮箱相关服务
 * @Author: chengyz
 * @CreateDate: 2018/4/10 15:34
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/emailBox")
@Api(tags = {"帮家团用户邮箱相关服务"}, description = "帮家团用户邮箱相关服务 - 需授权")
public class ApiUserEmailBoxController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserEmailBoxController.class);

    @Autowired
    private UserEmailBoxClient userEmailBoxClient;

    @Autowired
    private UserEmailBoxRecordClient userEmailBoxRecordClient;

    @ApiOperation(value = "用户发邮件", notes = "用户发邮件")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收件人(用,分开)", name = "receiver", required = true),
            @ApiImplicitParam(value = "收件人编号(用,拼接的字符串)", name = "receiverId", required = true),
            @ApiImplicitParam(value = "主题", name = "title"),
            @ApiImplicitParam(value = "内容", name = "content")
    })
    @RequestMapping(value = "save/SendEmailBox", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveSendEmailBox(@RequestBody BjtUserEmailBox bjtUserEmailBox) {
        LOGGER.info("saveSendEmailBox = >用户发邮件");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .on(bjtUserEmailBox.getReceiver(), new NotNullValidator("收件人"))
                .on(bjtUserEmailBox.getReceiverId(), new NotNullValidator("收件人编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        bjtUserEmailBox.setSenderId(getUserId());
        return userEmailBoxClient.saveSendEmailBox(bjtUserEmailBox);
    }

    @ApiOperation(value = "用户保存邮件到草稿箱", notes = "用户保存邮件到草稿箱")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收件人(用,分开)", name = "receiver"),
            @ApiImplicitParam(value = "收件人编号(用,拼接的字符串)", name = "receiver_id"),
            @ApiImplicitParam(value = "主题", name = "title"),
            @ApiImplicitParam(value = "内容", name = "content"),
    })
    @RequestMapping(value = "save/EmailBox", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveEmailBox(@RequestBody BjtUserEmailBox bjtUserEmailBox) {
        LOGGER.info("saveEmailBox = >用户保存邮件到草稿箱");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        bjtUserEmailBox.setSenderId(getUserId());
        return userEmailBoxClient.saveEmailBox(bjtUserEmailBox);
    }

    @ApiOperation(value = "查看发件箱邮件详情", notes = "查看发件箱邮件详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "邮件编号", name = "emailId", required = true)
    })
    @RequestMapping(value = "select/EmailBox", method = RequestMethod.GET)
    public BaseResult<BjtUserEmailBox> selectEmailBox(@RequestParam("emailId") String emailId) {
        LOGGER.info("selectEmailBox = >查看发件箱邮件详情");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return userEmailBoxClient.selectEmailBox(getUserId(), emailId);
    }

    @ApiOperation(value = "删除发件箱邮件", notes = "删除发件箱邮件")
    @RequestMapping(value = "delete/EmailBox", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteEmailBox(@RequestBody List<String> list) {
        LOGGER.info("deleteEmailBox = >删除发件箱邮件");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .on(list.toString(), new NotNullValidator("邮件编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return userEmailBoxClient.deleteEmailBox(getUserId(), list);
    }

    @ApiOperation(value = "搜索发件箱邮件列表", notes = "搜索发件箱邮件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "关键词", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int")
    })
    @RequestMapping(value = "select/PageEmailBox", method = RequestMethod.GET)
    public BaseResult<Page<BjtUserEmailBox>> selectPageEmailBox(@RequestParam Map<String, Object> params) {
        LOGGER.info("selectPageEmailBox = >搜索发件箱邮件列表");
        params.put("userId", getUserId());
        return userEmailBoxClient.selectPageEmailBox(params);
    }

    /**********************************以上是发件箱的api，以下是收件箱的api*****************************************************************************************/

    @ApiOperation(value = "标记邮件", notes = "标记邮件")
    @RequestMapping(value = "update/EmailBoxRecordList", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateEmailBoxRecordList(@RequestBody List<String> list) {
        LOGGER.info("updateEmailBoxRecordList = > 标记邮件");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .on(list.toString(), new NotNullValidator("邮件编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return userEmailBoxRecordClient.updateEmailBoxRecordList(getUserId(), list);
    }

    @ApiOperation(value = "搜索收件箱邮件列表", notes = "搜索收件箱邮件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "关键词", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int")
    })
    @RequestMapping(value = "select/PageEmailBoxRecord", method = RequestMethod.GET)
    public BaseResult<Page<BjtUserEmailBoxRecord>> selectPageEmailBoxRecord(@RequestParam Map<String, Object> params) {
        LOGGER.info("selectPageEmailBoxRecord = >搜索收件箱邮件列表");
        params.put("userId", getUserId());
        return userEmailBoxRecordClient.selectPageEmailBoxRecord(params);
    }

    @ApiOperation(value = "获取收件箱未读邮件数量")
    @RequestMapping(value = "select/EmailBoxRecordCount", method = RequestMethod.GET)
    public BaseResult selectEmailBoxRecordCount() {
        LOGGER.info("selectEmailBoxRecordCount = >获取收件箱未读邮件数量");
        Map<String, Object> params = new HashMap<>();
        params.put("receiver_id", getUserId());
        params.put("email_status", 0);
        BaseResult<List<BjtUserEmailBoxRecord>> listBaseResult = userEmailBoxRecordClient.selectAll(params);
        if (listBaseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return listBaseResult;
        }
        if (listBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.SUCCESS, 0);
        }
        return new BaseResult(BaseResultEnum.SUCCESS, listBaseResult.getData().size());
    }

    @ApiOperation(value = "删除收件箱邮件", notes = "删除收件箱邮件")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "delete/EmailBoxRecord", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteEmailBoxRecord(@RequestBody List<String> list) {
        LOGGER.info("deleteEmailBoxRecord = >删除发件箱邮件");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .on(list.toString(), new NotNullValidator("邮件编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return userEmailBoxRecordClient.deleteEmailBoxRecord(getUserId(), list);
    }

    @ApiOperation(value = "查看收件箱邮件详情", notes = "查看收件箱邮件详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收件编号", name = "recordId", required = true)
    })
    @RequestMapping(value = "select/EmailBoxRecord", method = RequestMethod.GET)
    public BaseResult<BjtUserEmailBoxRecord> selectEmailBoxRecord(@RequestParam("recordId") String recordId) {
        LOGGER.info("selectEmailBoxRecord = >查看收件箱邮件详情");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .on(recordId, new NotNullValidator("收件编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return userEmailBoxRecordClient.selectEmailBoxRecord(getUserId(), recordId);
    }

}
