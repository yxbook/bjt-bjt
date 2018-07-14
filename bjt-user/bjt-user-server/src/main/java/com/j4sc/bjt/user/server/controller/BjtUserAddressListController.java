package com.j4sc.bjt.user.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.j4sc.bjt.user.dao.entity.BjtUserAddressList;
import com.j4sc.bjt.user.rest.api.BjtUserAddressListService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/10 15:28
 * @Version: 1.0
 **/
@RestController
@RequestMapping("api/addressList")
@ApiModel
@Api(tags = {"用户通讯录"}, description = "用户通讯录")
public class BjtUserAddressListController extends BaseController<BjtUserAddressList, BjtUserAddressListService> implements BaseApiService<BjtUserAddressList> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtUserAddressListController.class);

    @Autowired
    private BjtUserAddressListService userAddressListService;

    @ApiOperation(value = "查询通讯录列表", notes = "查询通讯录列表")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "select/AllAddressList", method = RequestMethod.GET)
    public BaseResult<List<BjtUserAddressList>> selectAllAddressList(@RequestParam("userId") String userId, @RequestParam("key") String key) {
        LOGGER.info("selectAllAddressList = > 查询通讯录列表userId:" + userId);
        Wrapper<BjtUserAddressList> entityWrapper = new EntityWrapper<BjtUserAddressList>().eq("user_id", userId).eq("status", 1);
        if (!"".equals(key) && key != null) {
            entityWrapper.like("slave_nickname", key);
        }
        entityWrapper.last("ORDER BY CONVERT(slave_nickname USING gbk) ASC");
        List<BjtUserAddressList> list = userAddressListService.selectList(entityWrapper);
        return new BaseResult(BaseResultEnum.SUCCESS, list);
    }
}
