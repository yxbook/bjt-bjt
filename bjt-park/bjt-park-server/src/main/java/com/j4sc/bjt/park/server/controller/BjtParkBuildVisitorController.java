package com.j4sc.bjt.park.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.park.dao.entity.*;
import com.j4sc.bjt.park.rest.api.*;
import com.j4sc.bjt.park.server.client.SystemApiClient;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.util.AESUtil;
import com.j4sc.common.util.MapUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/2 17:18
 * @Version: 1.0
 **/
@RestController
@ApiModel
@Transactional
@RequestMapping("BjtParkBuildVisitor")
public class BjtParkBuildVisitorController extends BaseController<BjtParkBuildVisitor, BjtParkBuildVisitorService> implements BaseApiService<BjtParkBuildVisitor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildVisitorController.class);

    @Autowired
    private BjtParkBuildVisitorService parkBuildVisitorService;
    @Autowired
    private BjtParkBuildUserService parkBuildUserService;
    @Autowired
    private BjtParkBuildGuardService parkBuildGuardService;
    @Autowired
    private SystemApiClient systemApiClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation(value = "提交访客申请 类型：1、访客记录；2、邀请记录")
    @RequestMapping(value = "add/BuildVisitor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addBuildVisitor(@RequestBody Map<String, Object> params) {
        List<BjtParkBuildGuard> buildGuardList = parkBuildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().eq("user_id", params.get("interviewUserId")).eq("type", 1));
        if (buildGuardList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, "被访者无门禁权限");
        }
        boolean flag = buildGuardList.stream().anyMatch(v -> v.getType().equals(1));
        //权限  1.正常 0.申请中 -1.禁止
        if (flag) {
            BjtParkBuildVisitor buildVisitor = MapUtil.mapToBean(params, new BjtParkBuildVisitor());
            if (buildVisitor.getBuildId() == null) {
                buildVisitor.setBuildId(buildGuardList.get(0).getBuildId());
            }
            buildVisitor.setCtime(System.currentTimeMillis());
            buildVisitor.setUseCount(0);//初始使用次数
            if (parkBuildVisitorService.insert(buildVisitor)) {
                List<String> list = new ArrayList<>();
                if (buildVisitor.getStatus() == 1) {
                    list.add(buildVisitor.getInterviewPhone());
                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put("title", "访客申请");
                    paramsMap.put("body", buildVisitor.getVisitorName()+"申请拜访");
                    paramsMap.put("target", "BuildVisitor");
                    paramsMap.put("id", buildVisitor.getVisitorId() + "");
                    paramsMap.put("phoneList", list);
                    systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
                    return new BaseResult(BaseResultEnum.SUCCESS, "提交访客申请成功");
                }
                if (buildVisitor.getStatus() == 2) {
                    list.add(buildVisitor.getVisitorPhone());
                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put("title", "邀请访问");
                    paramsMap.put("body", buildVisitor.getInterviewName()+"邀请访问");
                    paramsMap.put("target", "BuildVisitor");
                    paramsMap.put("id", buildVisitor.getVisitorId() + "");
                    paramsMap.put("phoneList", list);
                    systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
                    return new BaseResult(BaseResultEnum.SUCCESS, "邀请访问成功");
                }
            }
        }
        return new BaseResult(BaseResultEnum.ERROR, "操作失败");
    }

    /**
     * type为2表示同意、为3表示拒绝
     */
    @ApiOperation(value = "处理访客申请")
    @RequestMapping(value = "update/BuildVisitor", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateBuildVisitor(@RequestBody Map<String, Object> params) {
        LOGGER.info("处理访客申请");
        BjtParkBuildVisitor bjtParkBuildVisitor = parkBuildVisitorService.selectOne(new EntityWrapper<BjtParkBuildVisitor>()
                .eq("visitor_id", params.get("visitorId")));
        if (bjtParkBuildVisitor == null) {
            return new BaseResult(BaseResultEnum.ERROR, "访客记录为空");
        }
        if (Integer.parseInt(params.get("buildId").toString()) == -1) {
            if (!bjtParkBuildVisitor.getInterviewUserId().equals(params.get("userId").toString())) {
                return new BaseResult(BaseResultEnum.ERROR, "非法操作");
            }
        }
        if (Integer.parseInt(params.get("buildId").toString()) > 0) {
            BjtParkBuildUser bjtParkBuildUser = parkBuildUserService.selectOne(new EntityWrapper<BjtParkBuildUser>().eq("build_id", params.get("buildId")).eq("user_id", params.get("userId")));
            if (bjtParkBuildUser == null) {
                return new BaseResult(BaseResultEnum.ERROR, "非法操作");
            }
        }
        if (bjtParkBuildVisitor.getStatus() != 1) {
            return new BaseResult(BaseResultEnum.ERROR, "不能进行其他操作");
        }
        Map<String, Object> sendMap = new HashMap<>();
        if (bjtParkBuildVisitor.getStatus() == 1) {
            if ((int) params.get("type") == 2) {
                //将被访者信息存入Redis中
                redisTemplate.opsForValue().set(bjtParkBuildVisitor.getVisitorId().toString() + bjtParkBuildVisitor.getVisitorPhone(), bjtParkBuildVisitor.getInterviewUserId(), 3600 * 6, TimeUnit.SECONDS);
                bjtParkBuildVisitor.setStatus(2);//状态:1、待处理；2、已通过；3、未通过
                sendMap.put("tel", bjtParkBuildVisitor.getVisitorPhone());
                sendMap.put("operate", "1");
                sendMap.put("code", "");
            }
            if ((int) params.get("type") == 3) {
                bjtParkBuildVisitor.setStatus(3);//状态:1、待处理；2、已通过；3、未通过
                sendMap.put("tel", bjtParkBuildVisitor.getVisitorPhone());
                sendMap.put("operate", "2");
                sendMap.put("code", "");
            }
            if (!parkBuildVisitorService.updateAllColumnById(bjtParkBuildVisitor)) {
                return new BaseResult(BaseResultEnum.ERROR, "操作失败");
            }
        }
        systemApiClient.sendMessage(sendMap);//发送短信消息到消息队列
        return new BaseResult(BaseResultEnum.SUCCESS, "操作成功");
    }

    @ApiOperation(value = "查询访客列表")
    @RequestMapping(value = "select/PageBuildVisitor", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildVisitor>> selectBuildVisitorPage(@RequestParam Map<String, Object> params) {
        Page<BjtParkBuildVisitor> pageModel = new Page<>();
        Page<BjtParkBuildVisitor> pageResult;
        pageModel.setCurrent(Integer.parseInt(params.get("page").toString()));
        pageModel.setSize(Integer.parseInt(params.get("size").toString()));
        pageResult = parkBuildVisitorService.selectPage(pageModel, new EntityWrapper<BjtParkBuildVisitor>().eq("interview_user_id", params.get("userId")).eq("type", params.get("type")).orderBy("ctime", false));
        return new BaseResult(BaseResultEnum.SUCCESS, pageResult);
    }

    @ApiOperation("查询访客最近的访客记录")
    @RequestMapping(value = "select/BuildVisitorLatest", method = RequestMethod.GET)
    public BaseResult<BjtParkBuildVisitor> selectBuildVisitorLatest(@RequestParam("phone") String phone) throws Exception {
        BjtParkBuildVisitor buildVisitor = parkBuildVisitorService.selectOne(new EntityWrapper<BjtParkBuildVisitor>().eq("visitor_phone", phone).orderBy("ctime", false).last("limit 1"));
        if (null != buildVisitor && buildVisitor.getStatus() == 2) {
            Map<String, String> map = new HashMap<>();
            map.put("key", buildVisitor.getVisitorId().toString() + buildVisitor.getVisitorPhone());
            map.put("type", "2");
            buildVisitor.setRemark(URLEncoder.encode(AESUtil.aesEncode(JSONObject.toJSONString(map)), "utf-8"));
        }
        return new BaseResult(BaseResultEnum.SUCCESS, buildVisitor);
    }

    @ApiOperation("删除访客记录")
    @RequestMapping(value = "delete/BuildVisitor", method = RequestMethod.DELETE)
    public BaseResult deleteBuildVisitor(@RequestParam("type") String type,@RequestParam("who")String who, @RequestBody List<String> list) {
        List<BjtParkBuildVisitor> visitorList = parkBuildVisitorService.selectList(new EntityWrapper<BjtParkBuildVisitor>().in("visitor_id", list));
        if (visitorList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, "被邀请记录不存在");
        }
        //类型：1、访客记录；
        if (type.equals("1")) {
            visitorList.forEach(v->{v.setInterviewDelete(2);});//默认为1，表示不删除，为2表示已删除
            if (!parkBuildVisitorService.updateAllColumnBatchById(visitorList)) {
                return new BaseResult(BaseResultEnum.ERROR, "删除失败");
            }
        }
        //2、邀请记录
        if (type.equals("2")) {
            //who为1表示邀请者，为2表示被邀请者
            if ("1".equals(who)) {
                visitorList.forEach(v->{v.setInterviewDelete(2);});//默认为1，表示不删除，为2表示已删除
                if (!parkBuildVisitorService.updateAllColumnBatchById(visitorList)) {
                    return new BaseResult(BaseResultEnum.ERROR, "删除失败");
                }
            }
            if ("2".equals(who)) {
                visitorList.forEach(v->{v.setVisitorDelete(2);});//默认为1，表示不删除，为2表示已删除
                if (!parkBuildVisitorService.updateAllColumnBatchById(visitorList)) {
                    return new BaseResult(BaseResultEnum.ERROR, "删除失败");
                }
            }
        }
        return new BaseResult(BaseResultEnum.SUCCESS, "删除成功");
    }

}
