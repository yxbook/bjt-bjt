package com.j4sc.bjt.api.server.controller.web;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.*;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkParkingLot;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkSpace;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkSpaceUser;
import com.j4sc.bjt.park.dao.entity.BjtParkBuild;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildUser;
import com.j4sc.bjt.system.dao.entity.BjtSystemAppCarousel;
import com.j4sc.bjt.system.dao.entity.BjtSystemNotice;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.bjt.user.dao.entity.BjtUserUserRole;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.validator.LengthValidator;
import com.j4sc.common.validator.NotNullValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/3 10:37
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "system")
@Api(tags = {"帮家团系统初始化服务"}, description = "帮家团系统初始化服务 - 特殊授权")
@Transactional
public class ApiSystemController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSystemController.class);
    @Autowired
    ParkApiClient parkApiClient;
    @Autowired
    ParkBuildClient parkBuildClient;
    @Autowired
    ParkBuildUserClient parkBuildUserClient;
    @Autowired
    UserManageClient userManageClient;
    @Autowired
    SystemNoticeClient systemNoticeClient;
    @Autowired
    UserUserRoleClient userUserRoleClient;
    @Autowired
    CarparkSpaceUserClient carparkSpaceUserClient;
    @Autowired
    CarparkSpaceClient carparkSpaceClient;
    @Autowired
    CarparkParkingLotClient carparkParkingLotClient;
    @Autowired
    SystemAppCarouselClient systemAppCarouselClient;
    @Autowired
    ParkBuildGuardClient parkBuildGuardClient;
    @Autowired
    SystemApiClient systemApiClient;
    @Autowired
    UserUserClient userUserClient;

    @ApiOperation(value = "新建楼宇",notes = "新建楼宇")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "build/add/Build", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addBuild(@RequestBody BjtParkBuild bjtParkBuild){
        LOGGER.info(" 新建楼宇...");
        bjtParkBuild.setCtime(new Date().getTime());
        bjtParkBuild.setCode(getCode());//设置邀请码
        BaseResult baseResult = parkBuildClient.insert(bjtParkBuild);
        return baseResult;
    }
    /**
     * 生成邀请码
     * @return
     */
    private String getCode() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0,10).toUpperCase();
    }

    @ApiOperation(value = "修改楼宇信息",notes = "修改楼宇信息")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "build/update/Build", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateBuild(@RequestBody BjtParkBuild bjtParkBuild){
        LOGGER.info(" 修改楼宇...");
        bjtParkBuild.setUtime(System.currentTimeMillis());
        BaseResult baseResult = parkBuildClient.updateById(bjtParkBuild);
        return baseResult;
    }
    @ApiOperation(value = "删除楼宇信息",notes = "删除楼宇信息")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "build/delete/Build", method = RequestMethod.DELETE)
    public BaseResult<String> deleteBuild(String id){
        LOGGER.info(" 修改楼宇... 多级删除 暂未实现");
        //BaseResult baseResult = parkBuildClient.deleteById(id);
        BaseResult<String> baseResult = new BaseResult<>(BaseResultEnum.ERROR,"不可删除，请先清理下级属性");
        return baseResult;
    }
    @ApiOperation(value = "根据ID查询楼宇信息",notes = "根据ID查询楼宇信息")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "build/select/Build", method = RequestMethod.GET)
    public BaseResult<BjtParkBuild> selectBuild(String id){
        LOGGER.info(" Chaxun 楼宇...");
        BaseResult<BjtParkBuild> baseResult = parkBuildClient.selectById(id);
        return baseResult;
    }

    @ApiOperation(value = "根据楼宇ID获取楼宇管理者",notes = "根据楼宇ID获取楼宇管理者")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "build/select/BuildUser", method = RequestMethod.GET)
    public BaseResult<List<Map<String, Object>>> selectBuildUser(@RequestParam("buildId") String buildId){
        LOGGER.info(" 查询楼宇管理者...");
        BaseResult buildResult = parkBuildClient.selectBuildUserList(Integer.parseInt(buildId));
        if (buildResult.getStatus() != BaseResultEnum.SUCCESS.getStatus())return buildResult;
        List<BjtParkBuildUser> buildUserList = (List<BjtParkBuildUser>)buildResult.getData();

        if (buildUserList == null || buildUserList.size() < 1) return buildResult;

        List<String> list = new ArrayList<>();
        buildUserList.forEach(v ->{
            list.add(v.getUserId());
        });
        Map<String,Object> map = new HashMap();
        map.put("buildId",buildId);
        map.put("list",list);
        return userManageClient.findUserBuildInfoByBU(map);
    }

    @ApiOperation(value = "查询楼宇信息",notes = "查询楼宇信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", required = false, dataType = "int"),
    })
    @RequestMapping(value = "build/select/PageBuild", method = RequestMethod.GET)
    public BaseResult<com.baomidou.mybatisplus.plugins.Page<BjtParkBuild>> selectPageBuild(@RequestParam Map<String, Object> params){
        LOGGER.info(" PAGE查询楼宇...");
        BaseResult<com.baomidou.mybatisplus.plugins.Page<BjtParkBuild>> baseResult = parkBuildClient.selectPage(params);
        return baseResult;
    }
    /*
    * 楼宇管理者API
    *
    *
    * */
    @ApiOperation(value = "新建楼宇总管理信息",notes = "新建楼宇总管理信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "buildId", value = "楼宇ID", required = true),
            @ApiImplicitParam(name = "username", value = "用户账号", required = true),
    })
    @RequestMapping(value = "build/add/BuildUser", method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addBuildUser(@RequestBody Map<String, Object> params){
        LOGGER.info(" 新建楼宇管理者..." + params);
        Integer buildId = Integer.parseInt((String)params.get("buildId"));
        String username = (String)params.get("username");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(username, new LengthValidator(1, 20, "用户账号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        //检查用户是否存在
        BaseResult<BjtUserUser> userResult = userManageClient.findUserByUsername(username);
        if (userResult.getStatus() != BaseResultEnum.SUCCESS.getStatus())return userResult;
        if (userResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "用户不存在");
        }
        if (null == userResult.getData().getRealname() || "".equals(userResult.getData().getRealname())) {
            return new BaseResult(BaseResultEnum.ERROR, "用户姓名为空");
        }
        BjtUserUser user = userResult.data;
        BaseResult baseResult = parkApiClient.addBuildUser(buildId,user.getUserId(),user.getUsername(), user.getRealname());
        if (baseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus())return baseResult;
        BjtUserUserRole bjtUserUserRole = new BjtUserUserRole();
        bjtUserUserRole.setBuildId(buildId);
        bjtUserUserRole.setRoleId(1);
        bjtUserUserRole.setUserId(user.getUserId());
        BaseResult roleResult = userUserRoleClient.insert(bjtUserUserRole);
        return roleResult;
    }
    @ApiOperation(value = "删除楼宇管理者",notes = "删除楼宇管理者")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "buildId", value = "楼宇ID", required = true, paramType = "body"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "body"),
    })
    @RequestMapping(value = "build/delete/BuildUser", method = RequestMethod.DELETE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult<String> deleteBuildUser(@RequestBody Map<String, Object> params){
        LOGGER.info(" 删除楼宇管理者" + params);
        String buildId = (String) params.get("buildId");
        String userId = (String) params.get("userId");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(buildId, new NotNullValidator("楼宇"))
                .on(userId, new NotNullValidator("用户"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        params.put("build_id",params.get("buildId"));
        params.put("user_id",params.get("userId"));
        params.remove("buildId");
        params.remove("userId");
        BaseResult<List<BjtParkBuildUser>> baseResult = parkBuildUserClient.selectAll(params);
        List<BjtParkBuildUser> list = baseResult.getData();

        if (list.size()==0)return new BaseResult<>(BaseResultEnum.ERROR,"错误的请求");
        userManageClient.removeRolePermissionByBU(params);
        BaseResult<List<BjtParkBuildGuard>> guardBaseResult = parkBuildGuardClient.selectAll(params);
        if (guardBaseResult.status == BaseResultEnum.SUCCESS.getStatus() && baseResult.getData() != null) {
            parkBuildGuardClient.deleteById(guardBaseResult.getData().get(0).getGuardId().toString());
        }
        List<String> usernameList = new ArrayList<>();
        usernameList.add(guardBaseResult.getData().get(0).getUsername());
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", "移除楼宇管理员权限");
        paramsMap.put("body", "移除楼宇管理员权限");
        paramsMap.put("target", "");
        paramsMap.put("id", "-1");
        paramsMap.put("phoneList", usernameList);
        systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        return parkBuildUserClient.deleteById(list.get(0).getBuildUserId() + "");
    }

    /*
    * 系统公告API
    *
    * */

    @ApiOperation(value = "发布系统公告",notes = "发布系统公告")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "notice/add/Notice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addNotice(@RequestBody BjtSystemNotice bjtSystemNotice){
        LOGGER.info(" 发布系统公告...");
        bjtSystemNotice.setUserId(getUserId());
        bjtSystemNotice.setCtime(System.currentTimeMillis());
        BaseResult baseResult = systemNoticeClient.insert(bjtSystemNotice);
        Map<String,Object> params = new HashMap<>();
        params.put("locked", 0);//状态(0:正常,1:锁定)
        BaseResult<List<BjtUserUser>> userListResult = userUserClient.selectAll(params);
        if (userListResult.status == BaseResultEnum.SUCCESS.getStatus() && userListResult.getData() != null &&userListResult.getData().size() > 0) {
            List<String> phoneList = userListResult.getData().stream().map(v->v.getUsername()).collect(Collectors.toList());
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("title", "系统公告");
            paramsMap.put("body", bjtSystemNotice.getContent());
            paramsMap.put("target", "SystemNotice");
            paramsMap.put("id", "");
            paramsMap.put("phoneList", phoneList);
            systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        }
        return baseResult;
    }
    @ApiOperation(value = "修改系统公告",notes = "修改系统公告")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "notice/update/Notice", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateNotice(@RequestBody BjtSystemNotice bjtSystemNotic){
        LOGGER.info(" 修改系统公告...");
        bjtSystemNotic.setCtime(new Date().getTime());
        BaseResult baseResult = systemNoticeClient.updateById(bjtSystemNotic);
        return baseResult;
    }
    @ApiOperation(value = "删除系统公告",notes = "删除系统公告")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "notice/delete/Notice", method = RequestMethod.DELETE)
    public BaseResult deleteNotice(@RequestParam("id") String id){
        LOGGER.info(" 删除系统公告");
        BaseResult baseResult = systemNoticeClient.deleteById(id);
        return baseResult;
    }
    @ApiOperation(value = "根据ID查询系统公告",notes = "根据ID查询系统公告")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "notice/select/Notice", method = RequestMethod.GET)
    public BaseResult<BjtSystemNotice> selectNotice(@RequestParam("id")String id){
        LOGGER.info(" Chaxun 系统公告...");
        BaseResult<BjtSystemNotice> baseResult = systemNoticeClient.selectById(id);
        return baseResult;
    }

    @ApiOperation(value = "查询系统公告",notes = "查询系统公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
    })
    @RequestMapping(value = "notice/select/PageNotice", method = RequestMethod.GET)
    public BaseResult<com.baomidou.mybatisplus.plugins.Page<BjtSystemNotice>> selectPageNotice(@RequestParam Map<String, Object> params){
        LOGGER.info(" PAGE查询系统公告...");
        BaseResult<com.baomidou.mybatisplus.plugins.Page<BjtSystemNotice>> baseResult = systemNoticeClient.selectPage(params);
        return baseResult;
    }

    @ApiOperation(value = "查询停车场管理员", notes = "查询停车场管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spaceId", value = "停车场编号", required = true)
    })
    @RequestMapping(value = "carpark/select/SpaceUserList", method = RequestMethod.GET)
    public BaseResult selectSpaceUserList(@RequestParam("spaceId")String spaceId) {
        LOGGER.info(" 查询停车场管理员");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(spaceId, new NotNullValidator("停车场编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        Map<String,Object> params = new HashMap<>();
        params.put("space_id", spaceId);
        BaseResult spaceUserResult = carparkSpaceUserClient.selectAll(params);
        return spaceUserResult;
    }
    @ApiOperation(value = "增加停车场管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spaceId", value = "停车场编号", dataType = "int", required = true),
            @ApiImplicitParam(name = "username", value = "用户名", required = true)
    })
    @RequestMapping(value = "carpark/add/SpaceUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addSpaceUser(@RequestBody Map<String, Object> params) {
        LOGGER.info(" 增加停车场管理员");
        String username = params.get("username")+"";
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(username, new NotNullValidator("用户名"))
                .on(params.get("spaceId")+"", new NotNullValidator("停车场编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userResult = userManageClient.findUserByUsername(username);
        if (userResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) return userResult;
        if (userResult.getData() == null) return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        if (userResult.getData().getLocked() != 0) return new BaseResult(BaseResultEnum.ERROR, "当前用户已被锁定");
        if (userResult.getData().getRealname() == null || "".equals(userResult.getData().getRealname())) return new BaseResult(BaseResultEnum.ERROR, "用户姓名为空");
        BaseResult<BjtCarparkSpace> spaceBaseResult = carparkSpaceClient.selectById(params.get("spaceId").toString());
        if (spaceBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return spaceBaseResult;
        }
        if (spaceBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "停车场信息为空");
        }
        //一个人只能管理一个停车场
        Map<String, Object> _params = new HashMap<>();
        _params.put("user_id", userResult.getData().getUserId());
        BaseResult<List<BjtCarparkSpaceUser>> spaceUserListResult = carparkSpaceUserClient.selectAll(_params);
        if (spaceUserListResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) return spaceUserListResult;
        if (spaceUserListResult.getData() != null && spaceUserListResult.getData().size() > 0)
            return new BaseResult(BaseResultEnum.ERROR, "当前用户已是停车场管理员");
        if (spaceUserListResult.getData().size() == 0) {
            BjtCarparkSpaceUser carparkSpaceUser = new BjtCarparkSpaceUser();
            carparkSpaceUser.setSpaceId(Integer.parseInt(params.get("spaceId").toString()));
            carparkSpaceUser.setUserId(userResult.getData().getUserId());
            carparkSpaceUser.setRealname(userResult.getData().getRealname());
            carparkSpaceUser.setUsername(userResult.getData().getUsername());
            carparkSpaceUser.setSpaceName(spaceBaseResult.getData().getName());
            return carparkSpaceUserClient.insert(carparkSpaceUser);
        }
        return new BaseResult(BaseResultEnum.ERROR, "添加停车场管理员失败");
    }
    @ApiOperation(value = "删除停车场管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spaceId", value = "停车场编号", dataType = "int", required = true),
            @ApiImplicitParam(name = "userId", value = "用户编号")
    })
    @RequestMapping(value = "carpark/delete/SpaceUser", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteSpaceUser(@RequestBody Map<String, Object> params) {
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("userId").toString(), new NotNullValidator("用户编号"))
                .on(params.get("spaceId").toString(), new NotNullValidator("停车场编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return carparkSpaceUserClient.deleteSpaceUser(params.get("spaceId").toString(), params.get("userId").toString());
    }
    @ApiOperation("添加停车场信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "名称", required = true),
            @ApiImplicitParam(name = "address",value = "地址", required = true),
            @ApiImplicitParam(name = "longitude",value = "经度", required = true),
            @ApiImplicitParam(name = "latitude",value = "纬度", required = true),
            @ApiImplicitParam(name = "number",value = "车位数量", required = true),
            @ApiImplicitParam(name = "contact",value = "联系人姓名", required = true),
            @ApiImplicitParam(name = "contactWay",value = "联系电话", required = true),
            @ApiImplicitParam(name = "open",value = "是否对外开放类型：1、是；2、否"),
            @ApiImplicitParam(name = "monthFee",value = "月卡单价"),
            @ApiImplicitParam(name = "yearFee",value = "年卡单价"),
            @ApiImplicitParam(name = "hourFee",value = "每小时费用"),
            @ApiImplicitParam(name = "temporaryNumber",value = "临时停车位总数量"),
            @ApiImplicitParam(name = "fixedNumber",value = "固定停车位总数量"),
            @ApiImplicitParam(name = "fixed",value = "剩余固定停车位数量"),
    })
    @RequestMapping(value = "carpark/add/CarparkSpace", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCarparkSpace(@RequestBody BjtCarparkSpace carparkSpace){
        LOGGER.info("添加停车场信息 = >saveCarparkApply");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(carparkSpace.getName(), new NotNullValidator("停车场名称"))
                .on(carparkSpace.getAddress(), new NotNullValidator("地址"))
                .on(carparkSpace.getContact(), new NotNullValidator("联系人姓名"))
                .on(carparkSpace.getContactWay(), new NotNullValidator("联系电话"))
                .on(carparkSpace.getLongitude(), new NotNullValidator("经度"))
                .on(carparkSpace.getLatitude(), new NotNullValidator("纬度"))
                .on(carparkSpace.getNumber()+"", new NotNullValidator("车位数量"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        carparkSpace.setCtime(System.currentTimeMillis());
        return carparkSpaceClient.insert(carparkSpace);
    }

    @ApiOperation("修改停车场信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "名称", required = true),
            @ApiImplicitParam(name = "address",value = "地址", required = true),
            @ApiImplicitParam(name = "longitude",value = "经度", required = true),
            @ApiImplicitParam(name = "latitude",value = "纬度", required = true),
            @ApiImplicitParam(name = "number",value = "车位数量", required = true),
            @ApiImplicitParam(name = "contact",value = "联系人姓名", required = true),
            @ApiImplicitParam(name = "contactWay",value = "联系电话", required = true),
            @ApiImplicitParam(name = "open",value = "是否对外开放类型：1、是；2、否"),
            @ApiImplicitParam(name = "monthFee",value = "月卡单价"),
            @ApiImplicitParam(name = "yearFee",value = "年卡单价"),
            @ApiImplicitParam(name = "hourFee",value = "每小时费用"),
            @ApiImplicitParam(name = "temporaryNumber",value = "临时停车位总数量"),
            @ApiImplicitParam(name = "fixedNumber",value = "固定停车位总数量"),
            @ApiImplicitParam(name = "fixed",value = "剩余固定停车位数量"),
    })
    @RequestMapping(value = "carpark/update/CarparkSpace", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateCarparkSpace(@RequestBody BjtCarparkSpace carparkSpace){
        LOGGER.info("修改停车场信息 = >updateCarparkSpace");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(carparkSpace.getSpaceId()+"", new NotNullValidator("停车场编号"))
                .on(carparkSpace.getName(), new NotNullValidator("停车场名称"))
                .on(carparkSpace.getAddress(), new NotNullValidator("地址"))
                .on(carparkSpace.getLongitude(), new NotNullValidator("经度"))
                .on(carparkSpace.getLatitude(), new NotNullValidator("纬度"))
                .on(carparkSpace.getNumber()+"", new NotNullValidator("车位数量"))
                .on(carparkSpace.getOpen()+"", new NotNullValidator("是否对外开放类型"))
                .on(carparkSpace.getMonthFee()+"", new NotNullValidator("月卡单价"))
                .on(carparkSpace.getYearFee()+"", new NotNullValidator("年卡单价"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return carparkSpaceClient.updateById(carparkSpace);
    }
    @ApiOperation(value = "获取停车位列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
            @ApiImplicitParam(name = "key", value = "关键词"),
            @ApiImplicitParam(name = "spaceId", value = "停车场编号")
    })
    @RequestMapping(value = "carpark/select/CarparkParkingLotPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtCarparkParkingLot>> selectCarparkParkingLotPage(@RequestParam Map<String,Object> params){
        LOGGER.info("获取停车位列表");
        return carparkParkingLotClient.selectPage(params);
    }

    @ApiOperation(value = "获取停车场列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
            @ApiImplicitParam(name = "key", value = "关键词")
    })
    @RequestMapping(value = "carpark/select/CarparkSpacePage", method = RequestMethod.GET)
    public BaseResult<Page<BjtCarparkSpace>> selectCarparkSpacePage(@RequestParam Map<String,Object> params){
        LOGGER.info("获取停车场列表");
        return carparkSpaceClient.selectCarparkSpacePageAdmin(params);
    }

    @ApiOperation(value = "获取停车场详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spaceId", value = "停车场编号", dataType = "String")
    })
    @RequestMapping(value = "carpark/select/CarparkSpace", method = RequestMethod.GET)
    public BaseResult<BjtCarparkSpace> selectCarparkSpacePage(@RequestParam("spaceId")String spaceId){
        LOGGER.info("获取停车场详情");
        return carparkSpaceClient.selectById(spaceId);
    }
    @ApiOperation(value = "删除停车场",notes = "删除停车场")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spaceId", value = "停车场编号", dataType = "String")
    })
    @RequestMapping(value = "carpark/delete/CarparkSpace", method = RequestMethod.DELETE)
    public BaseResult deleteCarparkSpace(@RequestParam("spaceId") String spaceId){
        LOGGER.info(" 删除停车场");
        BaseResult<BjtCarparkSpace> spaceBaseResult = carparkSpaceClient.selectById(spaceId);
        if (spaceBaseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return spaceBaseResult;
        }
        if (spaceBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "停车场信息不存在");
        }
        BaseResult baseResult = carparkSpaceClient.deleteById(spaceId);
        return baseResult;
    }

    @ApiOperation("添加APP首页轮播图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imagePath",value = "图片路径", required = true),
            @ApiImplicitParam(name = "orderValue",value = "显示顺序", required = true),
            @ApiImplicitParam(name = "link",value = "跳转链接"),
            @ApiImplicitParam(name = "remark",value = "备注"),
            @ApiImplicitParam(name = "name",value = "图片名称", required = true),
            @ApiImplicitParam(name = "showStartTime",value = "投放在APP首页的开始时间"),
            @ApiImplicitParam(name = "showEndTime",value = "投放在APP首页的结束时间"),
    })
    @RequestMapping(value = "carousel/add/AppCarousel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addAppCarousel(@RequestBody Map<String,Object> params){
        LOGGER.info("添加APP首页轮播图 = >addAppCarousel");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("imagePath").toString(), new NotNullValidator("图片路径"))
                .on(params.get("orderValue").toString(), new NotNullValidator("显示顺序"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BjtSystemAppCarousel carousel = new BjtSystemAppCarousel();
        carousel.setImagePath(params.get("imagePath").toString());
        carousel.setOrderValue(Integer.parseInt(params.get("orderValue").toString()));
        if (params.get("name") != null && !"".equals(params.get("name").toString())) {
            carousel.setName(params.get("name").toString());
        }
        if (params.get("link") != null && !"".equals(params.get("link").toString())) {
            carousel.setLink(params.get("link").toString());
        }
        if (params.get("remark") != null && !"".equals(params.get("remark").toString())) {
            carousel.setRemark(params.get("remark").toString());
        }
        if (null != params.get("showStartTime") && !"".equals(params.get("showStartTime").toString())) {
            carousel.setShowStartTime(Long.valueOf(params.get("showStartTime").toString()));
        }
        if (null != params.get("showEndTime") && !"".equals(params.get("showEndTime").toString())) {
            carousel.setShowEndTime(Long.valueOf(params.get("showEndTime").toString()));
        }
        carousel.setStatus(2);//状态：1为禁用、2为启用
        carousel.setCtime(System.currentTimeMillis());
        return systemAppCarouselClient.insert(carousel);
    }
    @ApiOperation("修改APP首页轮播图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imagePath",value = "图片路径", required = true),
            @ApiImplicitParam(name = "orderValue",value = "显示顺序", required = true),
            @ApiImplicitParam(name = "status",value = "状态：1为禁用、2为启用"),
            @ApiImplicitParam(name = "link",value = "跳转链接"),
            @ApiImplicitParam(name = "remark",value = "备注"),
            @ApiImplicitParam(name = "name",value = "图片名称", required = true),
            @ApiImplicitParam(name = "showStartTime",value = "投放在APP首页的开始时间"),
            @ApiImplicitParam(name = "showEndTime",value = "投放在APP首页的结束时间"),
    })
    @RequestMapping(value = "carousel/update/AppCarousel", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateAppCarousel(@RequestBody BjtSystemAppCarousel appCarousel){
        LOGGER.info("修改APP首页轮播图 = >updateAppCarousel");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(appCarousel.getImagePath(), new NotNullValidator("图片路径"))
                .on(appCarousel.getOrderValue().toString(), new NotNullValidator("显示顺序"))
                .on(appCarousel.getStatus().toString(), new NotNullValidator("状态"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return systemAppCarouselClient.updateById(appCarousel);
    }
    @ApiOperation(value = "获取轮播图列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
    })
    @RequestMapping(value = "carousel/select/CarouselPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtSystemAppCarousel>> selectCarouselPage(@RequestParam Map<String,Object> params){
        LOGGER.info("获取轮播图列表");
        return systemAppCarouselClient.selectPage(params);
    }
    @ApiOperation(value = "删除轮播图",notes = "删除轮播图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carouselId", value = "轮播图编号")
    })
    @RequestMapping(value = "carousel/delete/AppCarousel", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResult deleteAppCarousel(@RequestBody Map<String,Object> params){
        LOGGER.info(" 删除轮播图");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("carouselId").toString(), new NotNullValidator("轮播图编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return systemAppCarouselClient.deleteById(params.get("carouselId").toString());
    }
}
