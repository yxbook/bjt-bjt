package com.j4sc.bjt.carpark.server.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.carpark.dao.entity.*;
import com.j4sc.bjt.carpark.rest.api.*;
import com.j4sc.bjt.carpark.server.client.SystemApiClient;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.util.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/12 10:43
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/BjtCarparkApply")
@ApiModel
@Api(tags = {"停车申请服务"}, description = "停车申请服务")
public class BjtCarparkApplyController extends BaseController<BjtCarparkApply, BjtCarparkApplyService> implements BaseApiService<BjtCarparkApply> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtCarparkApplyController.class);

    @Autowired
    private BjtCarparkApplyService carparkApplyService;
    @Autowired
    private BjtCarparkApplyDetailService carparkApplyDetailService;
    @Autowired
    private BjtCarparkCarService carparkCarService;
    @Autowired
    private BjtCarparkSpaceUserService carparkSpaceUserService;
    @Autowired
    private BjtCarparkSpaceService carparkSpaceService;
    @Autowired
    private SystemApiClient systemApiClient;
    @Autowired
    private BjtCarparkParkingLotService carparkParkingLotService;

    @ApiOperation(value = "公司管理员提交停车申请", notes = "公司管理员提交停车申请")
    @Transactional
    @RequestMapping(value = "save/CarparkApply", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCarparkApply(@RequestBody Map<String, Object> params) {
        LOGGER.info("公司管理员提交停车申请 = >saveCarparkApply");

        List<BjtCarparkApplyDetail> submitDetailList;
        try {
            submitDetailList = MapUtil.mapsToObjects((List<Map<String, Object>>) params.get("detailList"), BjtCarparkApplyDetail.class);
        } catch (InstantiationException e) {
            return new BaseResult(BaseResultEnum.ERROR, e);
        } catch (IllegalAccessException e) {
            return new BaseResult(BaseResultEnum.ERROR, e);
        }
        if (submitDetailList == null) {
            return new BaseResult(BaseResultEnum.ERROR, "停车申请明细为空");
        }
        List<Integer> idList = new ArrayList<>();
        submitDetailList.forEach(v -> {
            idList.add(v.getCarId());
        });
        idList.add(-1);
        List<BjtCarparkCar> carList = carparkCarService.selectList(new EntityWrapper<BjtCarparkCar>().in("car_id", idList));
        if (carList == null || carList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, "提交停车申请失败");
        }

        //检测是否已经有停车申请明细
        List<BjtCarparkApplyDetail> applyDetailList = carparkApplyDetailService.selectList(new EntityWrapper<BjtCarparkApplyDetail>()
                .in("car_id", idList).eq("space_id", params.get("spaceId")));
        if (applyDetailList != null && applyDetailList.size() > 0) {
            return new BaseResult(BaseResultEnum.ERROR, "不能重复提交申请");
        }
        BjtCarparkApply carparkApply = MapUtil.mapToBean(params, new BjtCarparkApply());
        System.out.println(params);
        carparkApply.setApplyTime(System.currentTimeMillis());
        carparkApply.setStatus(1);//类型:1、待审批，2、已通过，3、未通过
        if (!carparkApplyService.insert(carparkApply)) {
            return new BaseResult(BaseResultEnum.ERROR, "提交停车申请失败");
        }
        submitDetailList.forEach(v -> {
            v.setApplyId(carparkApply.getApplyId());
            v.setCtime(System.currentTimeMillis());
            v.setStatus(1);//状态:1、待审批；2、（已通过）但未缴费；3、未通过；4、正常
            v.setSpaceId(carparkApply.getSpaceId());
            v.setUserId(v.getUserId());
            v.setSpaceName(carparkApply.getSpaceName());
            v.setCarId(v.getCarId());
            carList.forEach(s -> {
                if (v.getCarId() == s.getCarId()) {
                    v.setResource(s.getResource());//给申请明细添加车辆图片
                }
            });
        });
        if (!carparkApplyDetailService.insertBatch(submitDetailList)) {
            return new BaseResult(BaseResultEnum.SUCCESS, "停车申请提交失败");
        }
        String detail = JSON.toJSONString(submitDetailList);
        carparkApply.setDetail(detail);//设置停车申请明细
        if (!carparkApplyService.updateById(carparkApply)) {
            return new BaseResult(BaseResultEnum.SUCCESS, "停车申请提交失败");
        }
        List<String> usernameList = new ArrayList<>();
        List<BjtCarparkSpaceUser> spaceUserList = carparkSpaceUserService.selectList(new EntityWrapper<BjtCarparkSpaceUser>().eq("space_id", carparkApply.getSpaceId()));
        spaceUserList.forEach(v -> {
            usernameList.add(v.getUsername());
        });
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", "停车申请");
        paramsMap.put("body", "停车申请待审批");
        paramsMap.put("target", "CarparkApply");
        paramsMap.put("id", carparkApply.getApplyId() + "");
        paramsMap.put("phoneList", usernameList);
        systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        return new BaseResult(BaseResultEnum.SUCCESS, "门禁申请提交成功");
    }

    //类型:1、待审批，2、已通过，3、未通过
    @ApiOperation(value = "停车申请审批", notes = "操作类型type：1同意,2拒绝")
    @RequestMapping(value = "update/CarparkApply", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public BaseResult updateCarparkApply(@RequestBody Map<String, Object> params) {
        LOGGER.info("停车申请审批 = >updateCarparkApply");
        BjtCarparkSpaceUser carparkSpaceUser = carparkSpaceUserService.selectOne(new EntityWrapper<BjtCarparkSpaceUser>().eq("user_id", params.get("spaceUserId")));
        if (carparkSpaceUser == null) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是停车场管理员");

        BjtCarparkSpace carparkSpace = carparkSpaceService.selectOne(new EntityWrapper<BjtCarparkSpace>().eq("space_id", carparkSpaceUser.getSpaceId()));
        if (carparkSpace == null) return new BaseResult(BaseResultEnum.ERROR, "停车场不存在");

        BjtCarparkApply carparkApply = carparkApplyService.selectOne(new EntityWrapper<BjtCarparkApply>().eq("apply_id", params.get("applyId")));
        if (carparkApply == null) return new BaseResult(BaseResultEnum.ERROR, "停车申请不存在");
        if ("1".equals(params.get("type").toString()) || "2".equals(params.get("type").toString())) {
            carparkApply.setApprover(params.get("approver") + "");
            carparkApply.setApproverId(params.get("approverId") + "");
            carparkApply.setApprovalTime(System.currentTimeMillis());
        }
        if (carparkApply.getStatus() == 2 || carparkApply.getStatus() == 3) {
            return new BaseResult(BaseResultEnum.ERROR, "当前停车申请已完成，不能进行审批");
        }
        //操作类型type：1、同意；2、拒绝
        if ("2".equals(params.get("type").toString()) && carparkApply.getStatus() == 1) {
            carparkApply.setStatus(3);
            if (params.get("opinion") != null && !"".equals(params.get("opinion"))) {
                carparkApply.setOpinion(params.get("opinion") + "");
            }
            List<BjtCarparkApplyDetail> detailList = carparkApplyDetailService.selectList(new EntityWrapper<BjtCarparkApplyDetail>().eq("apply_id", carparkApply.getApplyId()));
            if (detailList == null || detailList.size() < 1) {
                return new BaseResult(BaseResultEnum.ERROR, "无停车申请明细");
            }
            detailList.forEach(v->v.setStatus(3));
            carparkApply.setDetail(JSON.toJSONString(detailList));
            if (!carparkApplyService.updateById(carparkApply)) {
                return new BaseResult(BaseResultEnum.ERROR, "停车申请申请审批失败");
            }
            List<Integer> detailIdList = new ArrayList<>();
            detailList.forEach(v -> {
                detailIdList.add(v.getDetailId());
            });
            if (!carparkApplyDetailService.deleteBatchIds(detailIdList)) {
                return new BaseResult(BaseResultEnum.ERROR, "停车申请审批失败");
            }
            return new BaseResult(BaseResultEnum.SUCCESS, "停车申请审批成功");
        }
        //操作类型type：1、同意；2、拒绝
        if ("1".equals(params.get("type").toString()) && carparkApply.getStatus() == 1) {
            carparkApply.setStatus(2);//通过
            List<BjtCarparkApplyDetail> detailList = carparkApplyDetailService.selectList(new EntityWrapper<BjtCarparkApplyDetail>().eq("apply_id", carparkApply.getApplyId()));
            if (detailList == null || detailList.size() < 1) {
                return new BaseResult(BaseResultEnum.ERROR, "无停车申请明细");
            }
            //类型：1、固定,2、临时; 检查停车申请明细中是否有固定停车的明细
            List<BjtCarparkApplyDetail> list = detailList.stream().filter(x -> x.getType() == 1).collect(Collectors.toList());
            //停车申请明细中存在固定停车位的明细

            List<BjtCarparkApplyDetail> submitDetailList;
            List<BjtCarparkApplyDetail> submitList;
            try {
                submitDetailList = MapUtil.mapsToObjects((List<Map<String, Object>>) params.get("detailList"), BjtCarparkApplyDetail.class);
                submitList = submitDetailList.stream().filter(x -> x.getType() == 1).collect(Collectors.toList());
                if (submitList.size() != list.size()) {
                    return new BaseResult(BaseResultEnum.ERROR, "停车申请审批失败");
                }
                if (list.size() > 0) {
                    if (submitDetailList == null) {
                        return new BaseResult(BaseResultEnum.ERROR, "停车申请明细为空");
                    }
                    detailList.forEach(v -> {
                        if (v.getType() == 1) {
                            submitDetailList.forEach(s -> {
                                if (s.getDetailId() == v.getDetailId()) {
                                    v.setLotNumber(s.getLotNumber());
                                }
                                v.setTerminalTime(s.getTerminalTime());
                            });
                        }
                    });
                    List<String> lotNumberList = submitList.stream().map(BjtCarparkApplyDetail::getLotNumber).collect(Collectors.toList());
                    List<BjtCarparkParkingLot> lotList = carparkParkingLotService.selectList(new EntityWrapper<BjtCarparkParkingLot>()
                            .in("number", lotNumberList).eq("space_id", carparkApply.getSpaceId()));
                    lotList.forEach(v -> {
                        v.setStatus(2);//状态：1、空闲；2、使用中
                    });
                    if (!carparkParkingLotService.updateAllColumnBatchById(lotList)) {
                        return new BaseResult(BaseResultEnum.ERROR, "停车申请审批失败");
                    }
                }
            } catch (InstantiationException e) {
                return new BaseResult(BaseResultEnum.ERROR, e);
            } catch (IllegalAccessException e) {
                return new BaseResult(BaseResultEnum.ERROR, e);
            }

            detailList.forEach(v -> {
                v.setStatus(2);//状态:1、待审批；2、（已通过）但未缴费；3、未通过；4、正常
                v.setTerminalTime(submitDetailList.get(0).getTerminalTime());
            });
            if (!carparkApplyDetailService.updateAllColumnBatchById(detailList)) {
                return new BaseResult(BaseResultEnum.ERROR, "停车申请审批失败");
            }
            carparkApply.setDetail(JSON.toJSONString(detailList));
            if (!carparkApplyService.updateById(carparkApply)) {
                return new BaseResult(BaseResultEnum.ERROR, "停车申请审批失败");
            }
        }
        List<String> usernameList = new ArrayList<>();
        usernameList.add(carparkApply.getApplyUsername());//设置申请人账号（手机号）
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", "停车申请");
        if (carparkApply.getStatus() == 2) {
            paramsMap.put("body", "停车申请已通过");
        } else {
            paramsMap.put("body", "停车申请已拒绝");
        }

        paramsMap.put("target", "CarparkApply");
        paramsMap.put("id", carparkApply.getApplyId() + "");
        paramsMap.put("phoneList", usernameList);
        systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        return new BaseResult(BaseResultEnum.SUCCESS, "停车申请审批成功");
    }

    @ApiOperation(value = "查询停车申请记录列表")
    @RequestMapping(value = "select/CarparkApplyPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtCarparkApply>> selectCarparkApplyPage(@RequestParam Map<String, Object> params) {
        LOGGER.info("查询停车申请记录列表 = > ");
        Page<BjtCarparkApply> pageModel = new Page<>();
        Page<BjtCarparkApply> pageResult = new Page<>();
        if (params.get("size") != null) {
            pageModel.setSize(Integer.parseInt(params.get("size").toString()));
        } else {
            pageModel.setSize(20);
        }
        if (params.get("page") != null) {
            pageModel.setCurrent(Integer.parseInt(params.get("page").toString()));
        } else {
            pageModel.setCurrent(1);
        }
        //buildId大于0表示停车场管理者
        if (Integer.parseInt(params.get("spaceId").toString()) > 0) {
            //查询当前用户是否是停车场管理员
            BjtCarparkSpaceUser spaceUser = carparkSpaceUserService.selectOne(new EntityWrapper<BjtCarparkSpaceUser>()
                    .eq("user_id", params.get("userId")).eq("space_id", params.get("spaceId")));
            if (spaceUser != null) {
                pageResult = carparkApplyService.selectPage(pageModel, new EntityWrapper<BjtCarparkApply>().eq("space_id", params.get("spaceId")).orderBy("apply_time", false));
                return new BaseResult(BaseResultEnum.SUCCESS, pageResult);
            }
        }
        //buildId为-1表示非停车场管理者
        if (Integer.parseInt(params.get("spaceId").toString()) == -1) {
            pageResult = carparkApplyService.selectPage(pageModel, new EntityWrapper<BjtCarparkApply>()
                    .eq("applyer_id", params.get("userId")).orderBy("apply_time", false));
            return new BaseResult(BaseResultEnum.SUCCESS, pageResult);
        }

        return new BaseResult(BaseResultEnum.SUCCESS, pageResult);
    }
}
