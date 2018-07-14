package com.j4sc.bjt.carpark.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkApplyDetail;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkApplyDetailService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/12 10:43
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/BjtCarparkApplyDetail")
@ApiModel
@Api(tags = {"停车申请明细服务"}, description = "停车申请明细服务")
public class BjtCarparkApplyDetailController extends BaseController<BjtCarparkApplyDetail, BjtCarparkApplyDetailService> implements BaseApiService<BjtCarparkApplyDetail> {

    private static final Logger LOGGER= LoggerFactory.getLogger(BjtCarparkApplyDetailController.class);

    @Autowired
    private BjtCarparkApplyDetailService carparkApplyDetailService;

    @RequestMapping(value = "select/CarparkApplyDetail", method = RequestMethod.GET)
    public BaseResult selectCarparkApplyDetail(@RequestParam("userId")String userId, @RequestParam("carId")int carId, @RequestParam("spaceId")int spaceId){
        LOGGER.info("获取停车申请明细权限");
        BjtCarparkApplyDetail carparkApplyDetail = carparkApplyDetailService.selectOne(new EntityWrapper<BjtCarparkApplyDetail>()
                .eq("user_id", userId).eq("car_id", carId).eq("space_id", spaceId));
        return new BaseResult(BaseResultEnum.SUCCESS, carparkApplyDetail);
    }

    @RequestMapping(value = "select/ApplyDetailList", method = RequestMethod.GET)
    public BaseResult<List<BjtCarparkApplyDetail>> selectApplyDetailList(@RequestParam("userId")String userId){
        LOGGER.info("获取停车申请明细权限列表");
        List<BjtCarparkApplyDetail> list = carparkApplyDetailService.selectList(new EntityWrapper<BjtCarparkApplyDetail>()
                .eq("user_id", userId).andNew("status={0}", "2").or("status=4"));
        return new BaseResult(BaseResultEnum.SUCCESS, list);
    }

    @RequestMapping(value = "select/ListByUserIdList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult<List<BjtCarparkApplyDetail>> selectListByUserIdList(@RequestBody List<String> userIdList){
        LOGGER.info("获取停车申请明细权限列表");
        userIdList.add("-1");
        List<BjtCarparkApplyDetail> list = carparkApplyDetailService.selectList(new EntityWrapper<BjtCarparkApplyDetail>().in("user_id", userIdList));
        return new BaseResult(BaseResultEnum.SUCCESS, list);
    }

    @RequestMapping(value = "delete/CarparkDetailList", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteCarparkDetailList(@RequestParam("userId") String userId, @RequestBody List<String> list){
        LOGGER.info("deleteCarparkDetailList = > 删除停车权限列表");
        list.add("-1");
        List<BjtCarparkApplyDetail> detailList = carparkApplyDetailService.selectList(new EntityWrapper<BjtCarparkApplyDetail>().eq("user_id", userId).in("detail_id", list));
        if (detailList == null || detailList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, "停车权限列表为空");
        }
        List<Integer> idList = new ArrayList<>();
        detailList.forEach(v -> {
            idList.add(v.getDetailId());
        });
        if (carparkApplyDetailService.deleteBatchIds(idList)) {
            return new BaseResult(BaseResultEnum.SUCCESS, "删除成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "删除失败" );
    }
    @RequestMapping(value = "delete/DetailList", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteDetailList(@RequestParam("spaceId") String spaceId, @RequestBody List<String> list){
        LOGGER.info("deleteDetailList = > 删除停车权限列表");
        list.add("-1");
        List<BjtCarparkApplyDetail> detailList = carparkApplyDetailService.selectList(new EntityWrapper<BjtCarparkApplyDetail>().eq("space_id", spaceId).in("detail_id", list));
        if (detailList == null || detailList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, "停车权限列表为空");
        }
        List<Integer> idList = new ArrayList<>();
        detailList.forEach(v -> {
            idList.add(v.getDetailId());
        });
        if (carparkApplyDetailService.deleteBatchIds(idList)) {
            return new BaseResult(BaseResultEnum.SUCCESS, "删除成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "删除失败" );
    }
}
