package com.j4sc.bjt.carpark.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkSpace;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkSpaceUser;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkSpaceService;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkSpaceUserService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/8 17:01
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/BjtCarparkSpaceUser")
@Api(tags = {"邦家团停车场系统停车场管理员服务"}, description = "邦家团停车场系统停车场管理员服务")
public class BjtCarparkSpaceUserController extends BaseController<BjtCarparkSpaceUser, BjtCarparkSpaceUserService> implements BaseApiService<BjtCarparkSpaceUser> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtCarparkSpaceUserController.class);

    @Autowired
    private BjtCarparkSpaceUserService carparkSpaceUserService;
    @Autowired
    private BjtCarparkSpaceService carparkSpaceService;

    @RequestMapping(value = "delete/SpaceUser", method = RequestMethod.DELETE)
    public BaseResult deleteSpaceUser(@RequestParam("spaceId")String spaceId, @RequestParam("userId")String userId){
        BjtCarparkSpace carparkSpace = carparkSpaceService.selectOne(new EntityWrapper<BjtCarparkSpace>().eq("space_id", spaceId));
        if (carparkSpace == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前停车场不存在");
        }
        BjtCarparkSpaceUser carparkSpaceUser = carparkSpaceUserService.selectOne(new EntityWrapper<BjtCarparkSpaceUser>().eq("user_id", userId).eq("space_id", spaceId));
        if (carparkSpaceUser == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不是停车场管理员");
        }
        if (carparkSpaceUserService.deleteById(carparkSpaceUser.getSpaceUserId())) {
            return new BaseResult(BaseResultEnum.SUCCESS, "删除停车场管理员成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "删除失败");
    }
    @ApiOperation("查询停车场管理员")
    @RequestMapping(value = "select/SpaceUser", method = RequestMethod.GET)
    @Cacheable(value = "spaceUser", key = "#userId")
    public BaseResult<BjtCarparkSpaceUser> selectSpaceUser(@RequestParam("userId")String userId){
        BjtCarparkSpaceUser spaceUser = carparkSpaceUserService.selectOne(new EntityWrapper<BjtCarparkSpaceUser>().eq("user_id", userId));
        if (spaceUser == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不是停车场管理员");
        }
        return new BaseResult(BaseResultEnum.SUCCESS, spaceUser);
    }
}
