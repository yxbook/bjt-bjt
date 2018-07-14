package com.j4sc.bjt.park.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGate;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGateRecord;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildVisitor;
import com.j4sc.bjt.park.rest.api.BjtParkBuildGateRecordService;
import com.j4sc.bjt.park.rest.api.BjtParkBuildGateService;
import com.j4sc.bjt.park.rest.api.BjtParkBuildGuardService;
import com.j4sc.bjt.park.rest.api.BjtParkBuildVisitorService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.ApiModel;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018 2018/4/9 11:30
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("BjtParkBuildGate")
public class BjtParkBuildGateController extends BaseController<BjtParkBuildGate, BjtParkBuildGateService> implements BaseApiService<BjtParkBuildGate> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildGateController.class);

    @Autowired
    private BjtParkBuildGuardService parkBuildGuardService;
    @Autowired
    private BjtParkBuildGateService parkBuildGateService;
    @Autowired
    private BjtParkBuildGateRecordService parkBuildGateRecordService;
    @Autowired
    private BjtParkBuildVisitorService parkBuildVisitorService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * deviceIdentifier 设备编号
     * control 1开门 0关门
     * userId 用户编号
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "open/Door", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult openDoor(@RequestBody Map<String, Object> params) {
        Map<String, Object> openMap = new HashedMap();
        openMap.put("code", 0);
        if (!redisTemplate.hasKey(params.get("userId").toString())) {
            return new BaseResult(BaseResultEnum.ERROR, JSONObject.toJSONString(openMap));
        }
        List<BjtParkBuildGuard> buildGuardList = parkBuildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().eq("user_id", params.get("userId")).eq("type", 1));
        if (buildGuardList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, JSONObject.toJSONString(openMap));
        }
        List<Integer> buildIdList = buildGuardList.stream().map(v -> v.getBuildId()).collect(Collectors.toList());
        List<BjtParkBuildGate> gateList = parkBuildGateService.selectList(new EntityWrapper<BjtParkBuildGate>().in("build_id", buildIdList));
        if (gateList == null || gateList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, JSONObject.toJSONString(openMap));
        }
        boolean flag = gateList.stream().anyMatch(v -> v.getDeviceIdentifier().equals(params.get("deviceIdentifier")));//检验用户门禁权限对应楼宇下的闸机设备编号中是否含有当前设备编号
        if (flag) {
            this.addGateRecord(buildGuardList.get(0), params.get("deviceIdentifier") + "", "");
            openMap.put("code", 1);
            return new BaseResult(BaseResultEnum.SUCCESS, JSONObject.toJSONString(openMap));
        }
        return new BaseResult(BaseResultEnum.ERROR, JSONObject.toJSONString(openMap));
    }

    /**
     * deviceIdentifier 设备编号
     * control 1开门 0关门
     * userId 用户编号
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "open/VisitorDoor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult openVisitorDoor(@RequestBody Map<String, Object> params) {
        Map<String, Object> openMap = new HashedMap();
        openMap.put("code", 0);
        List<BjtParkBuildGuard> buildGuardList = parkBuildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().eq("user_id", params.get("userId")).eq("type", 1));
        if (buildGuardList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, JSONObject.toJSONString(openMap));
        }
        List<Integer> buildIdList = buildGuardList.stream().map(v -> v.getBuildId()).collect(Collectors.toList());
        List<BjtParkBuildGate> gateList = parkBuildGateService.selectList(new EntityWrapper<BjtParkBuildGate>().in("build_id", buildIdList));
        if (gateList == null || gateList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, JSONObject.toJSONString(openMap));
        }
        boolean flag = gateList.stream().anyMatch(v -> v.getDeviceIdentifier().equals(params.get("deviceIdentifier")));//检验用户门禁权限对应楼宇下的闸机设备编号中是否含有当前设备编号
        if (flag) {
            String key = params.get("key").toString();
            String visitorId = key.substring(0, key.length() - 11);
            String phone = key.substring(key.length() - 11, key.length());
            BjtParkBuildVisitor buildVisitor = parkBuildVisitorService.selectOne(new EntityWrapper<BjtParkBuildVisitor>().eq("visitor_id", visitorId).eq("visitor_phone", phone));
            if (buildVisitor == null) {
                return new BaseResult(BaseResultEnum.ERROR, JSONObject.toJSONString(openMap));
            }
            if (buildVisitor.getUseCount() >= 2) {
                redisTemplate.delete(key);//移除key value
                return new BaseResult(BaseResultEnum.ERROR, JSONObject.toJSONString(openMap));
            }
            buildVisitor.setUseCount(buildVisitor.getUseCount() + 1);
            if (!parkBuildVisitorService.updateById(buildVisitor)) {
                return new BaseResult(BaseResultEnum.ERROR, JSONObject.toJSONString(openMap));
            }
            if (buildVisitor.getUseCount() >= 2) {
                redisTemplate.delete(key);//移除key value
            }
            String remark = buildVisitor.getVisitorName() + "(" + buildVisitor.getVisitorPhone() + ")";
            this.addGateRecord(buildGuardList.get(0), params.get("deviceIdentifier") + "", remark);
            openMap.put("code", 1);
            return new BaseResult(BaseResultEnum.SUCCESS, JSONObject.toJSONString(openMap));
        }
        return new BaseResult(BaseResultEnum.ERROR, JSONObject.toJSONString(openMap));
    }

    /**
     * 添加闸机记录
     *
     * @param buildGuard 门禁权限
     * @param deviceId   设备编号
     * @return
     */
    private BaseResult addGateRecord(BjtParkBuildGuard buildGuard, String deviceId, String remark) {
        BjtParkBuildGate gate = parkBuildGateService.selectOne(new EntityWrapper<BjtParkBuildGate>().eq("device_identifier", deviceId));
        BjtParkBuildGateRecord gateRecord = new BjtParkBuildGateRecord();
        gateRecord.setCompanyId(buildGuard.getCompanyId());
        gateRecord.setCompanyName(buildGuard.getCompanyName());
        gateRecord.setCtime(System.currentTimeMillis());
        gateRecord.setDeviceIdentifier(deviceId);
        gateRecord.setRealname(buildGuard.getUserRealname());
        gateRecord.setUsername(buildGuard.getUsername());
        gateRecord.setBuildId(gate.getBuildId());
        gateRecord.setTime(System.currentTimeMillis());
        gateRecord.setRemark(remark);
        if (parkBuildGateRecordService.insertAllColumn(gateRecord)) {
            return new BaseResult(BaseResultEnum.SUCCESS, "操作成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "操作失败");
    }

}
