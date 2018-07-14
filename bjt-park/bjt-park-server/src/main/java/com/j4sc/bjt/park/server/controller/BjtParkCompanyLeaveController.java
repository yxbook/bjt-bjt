package com.j4sc.bjt.park.server.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.park.common.util.MapToEntityUtil;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyLeave;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyProgress;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyLeaveService;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyProgressService;
import com.j4sc.bjt.park.server.client.SystemApiClient;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 请假、外出、出差、加班申请的controller
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:06
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("BjtParkCompanyLeave")
public class BjtParkCompanyLeaveController extends BaseController<BjtParkCompanyLeave, BjtParkCompanyLeaveService> implements BaseApiService<BjtParkCompanyLeave> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkCompanySignController.class);

    @Autowired
    private BjtParkCompanyLeaveService parkCompanyLeaveService;
    @Autowired
    private BjtParkCompanyProgressService parkCompanyProgressService;
    @Autowired
    private SystemApiClient systemApiClient;

    @ApiOperation(value = "提交申请")
    @Transactional
    @RequestMapping(value = "save/CompanyLeave", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanyLeave(@RequestBody Map<String, Object> params) {
        LOGGER.info("提交申请 = > saveCompanyClock");
        BjtParkCompanyLeave bjtParkCompanyLeave = (BjtParkCompanyLeave) MapToEntityUtil.mapToEntity(params, BjtParkCompanyLeave.class);
        bjtParkCompanyLeave.setStatus(1);//申请进度：1、待审批、2、申请通过、3、未通过
        bjtParkCompanyLeave.setCtime(System.currentTimeMillis());
        if (!parkCompanyLeaveService.insert(bjtParkCompanyLeave)) return new BaseResult(BaseResultEnum.ERROR, "提交申请失败");

        BjtParkCompanyProgress parkCompanyProgress = (BjtParkCompanyProgress) MapToEntityUtil.mapToEntity((Map<String, Object>) params.get("progress"), BjtParkCompanyProgress.class);
        parkCompanyProgress.setCtime(System.currentTimeMillis());
        parkCompanyProgress.setLastTime(System.currentTimeMillis());
        parkCompanyProgress.setLastUserId(bjtParkCompanyLeave.getUserId());
        parkCompanyProgress.setLastUserRealname(bjtParkCompanyLeave.getApplyer());
        parkCompanyProgress.setLeaveId(bjtParkCompanyLeave.getLeaveId());
        parkCompanyProgress.setStatus(0);//处理状态：0表示待审批，1表示同意，2表示拒绝，3表示转批
        if (!parkCompanyProgressService.insert(parkCompanyProgress))
            return new BaseResult(BaseResultEnum.ERROR, "提交申请失败");
        List<BjtParkCompanyProgress> companyProgressList = new ArrayList<>();
        companyProgressList.add(parkCompanyProgress);
        String approval = JSON.toJSONString(companyProgressList);
        bjtParkCompanyLeave.setApprover(approval);
        if (!parkCompanyLeaveService.updateById(bjtParkCompanyLeave)) return new BaseResult(BaseResultEnum.ERROR, "提交申请失败");
        List<String> usernameList = new ArrayList<>();
        usernameList.add(parkCompanyProgress.getUsername());//设置审批人账号（手机号）
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", "请假申请");
        paramsMap.put("body", "请假申请待审批");
        paramsMap.put("target", "CompanyLeave");
        paramsMap.put("id", bjtParkCompanyLeave.getLeaveId()+"");
        paramsMap.put("phoneList", usernameList);
        systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        return new BaseResult(BaseResultEnum.SUCCESS, "提交申请成功");
    }

    @ApiOperation(value = "申请审批:1表示同意，2表示拒绝，3表示转批")
    @Transactional
    @RequestMapping(value = "update/CompanyLeave", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateCompanyLeave(@RequestBody Map<String, Object> params) {
        LOGGER.info("申请审批 = > updateCompanyLeave");
        if ("1".equals(params.get("type").toString()) || "2".equals(params.get("type").toString())) {
            return this.dataValidation(params);
        }
        if ("3".equals(params.get("type").toString())) {
            BjtParkCompanyLeave _bjtParkCompanyLeave = parkCompanyLeaveService.selectOne(new EntityWrapper<BjtParkCompanyLeave>().eq("leave_id", params.get("leaveId")));

            //当前处理人的进度
            BjtParkCompanyProgress _parkCompanyProgress = parkCompanyProgressService.selectOne(new EntityWrapper<BjtParkCompanyProgress>().eq("leave_id", params.get("leaveId")).eq("user_id", params.get("userId")).eq("status", 0));
            _parkCompanyProgress.setStatus(3);
            if (!parkCompanyProgressService.updateById(_parkCompanyProgress))
                return new BaseResult(BaseResultEnum.ERROR, "审批失败");
            if (params.get("progress") == null) return new BaseResult(BaseResultEnum.ERROR, "下一步审批信息为空");
            //下一步处理人的进度
            BjtParkCompanyProgress parkCompanyProgress = (BjtParkCompanyProgress) MapToEntityUtil.mapToEntity((Map<String, Object>) params.get("progress"), BjtParkCompanyProgress.class);
            parkCompanyProgress.setLeaveId(Integer.parseInt(params.get("leaveId").toString()));
            parkCompanyProgress.setLastUserRealname(params.get("userRealname")+"");
            parkCompanyProgress.setLastUserId(params.get("userId")+"");
            parkCompanyProgress.setLastTime(System.currentTimeMillis());
            parkCompanyProgress.setCtime(System.currentTimeMillis());
            parkCompanyProgress.setStatus(0);//1表示同意，2表示拒绝，3表示转批,0待审批
            if (!parkCompanyProgressService.insert(parkCompanyProgress))
                return new BaseResult(BaseResultEnum.ERROR, "审批失败");
            List<BjtParkCompanyProgress> list = parkCompanyProgressService.selectList(new EntityWrapper<BjtParkCompanyProgress>().eq("leave_id", params.get("leaveId")));
            String approval = JSON.toJSONString(list);
            _bjtParkCompanyLeave.setApprover(approval);
            if (!parkCompanyLeaveService.updateById(_bjtParkCompanyLeave))
                return new BaseResult(BaseResultEnum.ERROR, "审批失败");
            List<String> usernameList = new ArrayList<>();
            usernameList.add(parkCompanyProgress.getUsername());//设置审批人账号（手机号）
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("title", "请假申请");
            paramsMap.put("body", "请假审批");
            paramsMap.put("target", "CompanyLeave");
            paramsMap.put("id", _bjtParkCompanyLeave.getLeaveId()+"");
            paramsMap.put("phoneList", usernameList);
            systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
            return new BaseResult(BaseResultEnum.SUCCESS, "审批成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "审批失败");
    }

    @ApiOperation(value = "待审批事项和审批记录（status为1则是待审批事项，否则查询所有记录）")
    @RequestMapping(value = "select/CompanyLeavePage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkCompanyLeave>> selectCompanyLeavePage(@RequestParam Map<String, Object> params) {
        LOGGER.info("待审批事项和审批记录 = > selectCompanyLeavePage");
        Wrapper<BjtParkCompanyProgress> entityWrapper = Integer.parseInt(params.get("status").toString()) == 1 ? new EntityWrapper<BjtParkCompanyProgress>().eq("user_id", params.get("userId")).eq("status", 0) : new EntityWrapper<BjtParkCompanyProgress>().eq("user_id", params.get("userId")).orderBy("status");
        List<BjtParkCompanyProgress> companyProgressList = parkCompanyProgressService.selectList(entityWrapper);
        if (companyProgressList.size() < 1 || companyProgressList == null) return new BaseResult(BaseResultEnum.SUCCESS, null);
        List<Integer> companyLeaveIdList = new ArrayList<>();
        companyProgressList.forEach(v -> {
            companyLeaveIdList.add(v.getLeaveId());
        });
        companyLeaveIdList.add(-1);
        Page<BjtParkCompanyLeave> pageModel = new Page<>();
        pageModel.setCurrent(Integer.parseInt(params.get("page").toString()));
        pageModel.setSize(Integer.parseInt(params.get("size").toString()));
        Page<BjtParkCompanyLeave> pageLeave = parkCompanyLeaveService.selectPage(pageModel, new EntityWrapper<BjtParkCompanyLeave>().in("leave_id", companyLeaveIdList));
        return new BaseResult(BaseResultEnum.SUCCESS, pageLeave);
    }

    /**
     * 申请审批:type为1表示同意，2表示拒绝，3表示转批
     * @param params
     * @return
     */
    private BaseResult dataValidation(Map<String, Object> params){
        BjtParkCompanyLeave _bjtParkCompanyLeave = parkCompanyLeaveService.selectOne(new EntityWrapper<BjtParkCompanyLeave>().eq("leave_id", params.get("leaveId")));

        if ("1".equals(params.get("type").toString())) {
            _bjtParkCompanyLeave.setStatus(2);//申请进度：1、待审批、2、申请通过、3、未通过
        }
        if ("2".equals(params.get("type").toString())) {
            _bjtParkCompanyLeave.setStatus(3);//申请进度：1、待审批、2、申请通过、3、未通过
        }
        BjtParkCompanyProgress _parkCompanyProgress = parkCompanyProgressService.selectOne(new EntityWrapper<BjtParkCompanyProgress>().eq("leave_id", params.get("leaveId")).eq("user_id", params.get("userId")).eq("status", 0));
        _parkCompanyProgress.setStatus(Integer.parseInt(params.get("type").toString()));
        if (!parkCompanyProgressService.updateById(_parkCompanyProgress))
            return new BaseResult(BaseResultEnum.ERROR, "审批失败");
        List<BjtParkCompanyProgress> list = parkCompanyProgressService.selectList(new EntityWrapper<BjtParkCompanyProgress>().eq("leave_id", params.get("leaveId")));
        String approval = JSON.toJSONString(list);
        _bjtParkCompanyLeave.setApprover(approval);
        if (!parkCompanyLeaveService.updateById(_bjtParkCompanyLeave))
            return new BaseResult(BaseResultEnum.ERROR, "审批失败");
        List<String> usernameList = new ArrayList<>();
        usernameList.add(_bjtParkCompanyLeave.getUsername());//设置申请人账号（手机号）
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", "请假申请");
        if (_bjtParkCompanyLeave.getStatus() == 2) {
            paramsMap.put("body", "请假申请已通过");
        } else {
            paramsMap.put("body", "请假申请已拒绝");
        }
        paramsMap.put("target", "CompanyLeave");
        paramsMap.put("id", _bjtParkCompanyLeave.getLeaveId()+"");
        paramsMap.put("phoneList", usernameList);
        systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        return new BaseResult(BaseResultEnum.SUCCESS, "审批成功");
    }

}
