package com.j4sc.bjt.carpark.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkSpace;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkSpaceService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/18 17:01
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/BjtCarparkSpace")
@ApiModel
@Api(tags = {"邦家团停车场系统停车场信息服务"}, description = "邦家团停车场系统停车场信息服务")
public class BjtCarparkSpaceController extends BaseController<BjtCarparkSpace, BjtCarparkSpaceService> implements BaseApiService<BjtCarparkSpace> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtCarparkSpaceController.class);

    @Autowired
    private BjtCarparkSpaceService carparkSpaceService;

    @ApiOperation("获取停车场列表（停车申请时）")
    @RequestMapping(value = "select/CarparkSpacePage", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> selectCarparkSpacePage(@RequestParam Map<String, Object> params) {
        Map<String, Object> resultMap = new HashMap<>();
        Page<BjtCarparkSpace> pageModel = new Page<>();
        pageModel.setSize(Integer.parseInt(params.get("size").toString()));
        pageModel.setCurrent(Integer.parseInt(params.get("page").toString()));
        Wrapper entityWrapper = new EntityWrapper<BjtCarparkSpace>();
        if (params.get("key") != null) {
            entityWrapper.like("name", params.get("key")+"");
        }
        //spaceId为-1则表示当前楼宇无对应的停车场
        if (Integer.parseInt(params.get("spaceId").toString()) == -1) {
            entityWrapper.last("ORDER BY CONVERT(name USING gbk) ASC");
        } else {
            if (Integer.parseInt(params.get("page").toString()) == 1) {
                BjtCarparkSpace carparkSpace = carparkSpaceService.selectById(Integer.parseInt(params.get("spaceId").toString()));
                if (carparkSpace == null) {
                    resultMap.put("space", null);
                } else {
                    entityWrapper.notIn("space_id", carparkSpace.getSpaceId()).last("ORDER BY CONVERT(name USING gbk) ASC");
                }
            } else {
                entityWrapper.notIn("space_id", Integer.parseInt(params.get("spaceId").toString())).last("ORDER BY CONVERT(name USING gbk) ASC");
            }
        }
        Page<BjtCarparkSpace> pageResult = carparkSpaceService.selectPage(pageModel, entityWrapper);
        resultMap.put("pageData", pageResult);
        return new BaseResult(BaseResultEnum.SUCCESS, resultMap);
    }
    @ApiOperation("获取停车场列表（后台管理）")
    @RequestMapping(value = "select/CarparkSpacePageAdmin", method = RequestMethod.GET)
    public BaseResult<Page<BjtCarparkSpace>> selectCarparkSpacePageAdmin(@RequestParam Map<String, Object> params) {
        Page<BjtCarparkSpace> pageModel = new Page<>();
        pageModel.setSize(Integer.parseInt(params.get("size").toString()));
        pageModel.setCurrent(Integer.parseInt(params.get("page").toString()));
        Wrapper<BjtCarparkSpace> entityWrapper = new EntityWrapper();
        if (params.get("key") != null && !"".equals(params.get("key"))) {
            entityWrapper.like("name", params.get("key")+"");
        }
        Page<BjtCarparkSpace> pageResult = carparkSpaceService.selectPage(pageModel, entityWrapper);
        return new BaseResult(BaseResultEnum.SUCCESS, pageResult);
    }
}
