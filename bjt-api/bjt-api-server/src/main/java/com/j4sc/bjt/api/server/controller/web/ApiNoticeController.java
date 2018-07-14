package com.j4sc.bjt.api.server.controller.web;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkBuildNoticeClient;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildNotice;
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

import java.util.Date;
import java.util.Map;


/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/3 11:29
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "admin/notice")
@Api(tags = {"帮家团楼宇公告服务"}, description = "帮家团楼宇公告服务 - WEB授权")
public class ApiNoticeController extends BaseJwtController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiNoticeController.class);
    @Autowired
    ParkBuildNoticeClient parkBuildNoticeClient;

    /*
    * 楼宇级别公告
    * 
    * */

    @ApiOperation(value = "新建楼宇公告",notes = "新建楼宇公告")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "add/Notice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addNotice(@RequestBody BjtParkBuildNotice bjtParkBuildNotice){
        LOGGER.info(" 新建楼宇公告...");
        bjtParkBuildNotice.setCtime(new Date().getTime());
        bjtParkBuildNotice.setUserId(getUserId());
        bjtParkBuildNotice.setBuildId(getBuildId());
        return parkBuildNoticeClient.saveBuildNotice(bjtParkBuildNotice);
    }
    @ApiOperation(value = "修改楼宇公告",notes = "修改楼宇公告")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "update/Notice", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateNotice(@RequestBody BjtParkBuildNotice bjtParkBuildNotice){
        LOGGER.info(" 修改楼宇公告...");
        bjtParkBuildNotice.setCtime(new Date().getTime());
        bjtParkBuildNotice.setUserId(getUserId());
        bjtParkBuildNotice.setBuildId(getBuildId());
        BaseResult baseResult = parkBuildNoticeClient.updateById(bjtParkBuildNotice);
        return baseResult;
    }
    @ApiOperation(value = "删除楼宇公告",notes = "删除楼宇公告")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "delete/Notice", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteNotice(@RequestBody Map<String, Object> params){
        String bNoticeId = (String) params.get("bNoticeId");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(bNoticeId+"", new NotNullValidator("ID"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult noticeResult =parkBuildNoticeClient.selectById(bNoticeId);
        if (noticeResult.getData()!=null && ((BjtParkBuildNotice)noticeResult.getData()).getBuildId() == getBuildId()){
            BaseResult baseResult = parkBuildNoticeClient.deleteById(bNoticeId);
            return baseResult;
        }
        return new BaseResult(BaseResultEnum.ERROR,"非法操作");
    }
    @ApiOperation(value = "根据ID查询楼宇公告",notes = "根据ID查询楼宇公告")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "楼宇公告编号", name = "bNoticeId", required = true)
    })
    @RequestMapping(value = "select/Notice", method = RequestMethod.GET)
    public BaseResult selectNotice(@RequestParam("bNoticeId") String bNoticeId){
        LOGGER.info(" Chaxun 楼宇...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(bNoticeId+"", new NotNullValidator("楼宇公告编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult baseResult = parkBuildNoticeClient.selectById(bNoticeId);
        return baseResult;
    }

    @ApiOperation(value = "查询楼宇公告",notes = "查询楼宇公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", required = false, dataType = "int"),
    })
    @RequestMapping(value = "select/PageNotice", method = RequestMethod.GET)
    public BaseResult selectPageNotice(@RequestParam Map<String, Object> params){
        LOGGER.info(" PAGE查询楼宇...");
        params.put("build_id",getBuildId());
        BaseResult baseResult = parkBuildNoticeClient.selectPage(params);
        return baseResult;
    }


}
