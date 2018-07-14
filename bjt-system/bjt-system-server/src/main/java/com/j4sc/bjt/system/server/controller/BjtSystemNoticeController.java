package com.j4sc.bjt.system.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.system.dao.entity.BjtSystemNotice;
import com.j4sc.bjt.system.rest.api.BjtSystemNoticeService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/4 9:54
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtSystemNotice")
public class BjtSystemNoticeController extends BaseController<BjtSystemNotice,BjtSystemNoticeService> implements BaseApiService<BjtSystemNotice> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtSystemNoticeController.class);

    @Autowired
    private BjtSystemNoticeService bjtSystemNoticeService;

    @ApiOperation(value = "获取系统公告", notes = "获取系统公告")
    @RequestMapping(value = "select/SysNoticeAll", method = RequestMethod.GET)
    public BaseResult<List<BjtSystemNotice>> selectSysNoticeAll() {
        LOGGER.info("获取系统公告....");
        List<BjtSystemNotice> systemNoticeList = bjtSystemNoticeService.selectList(new EntityWrapper<BjtSystemNotice>().orderBy("ctime", false));
        return new BaseResult(BaseResultEnum.SUCCESS, systemNoticeList);
    }
}
