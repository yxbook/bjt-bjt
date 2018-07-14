package com.j4sc.bjt.park.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyClock;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanySign;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyUser;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyClockService;
import com.j4sc.bjt.park.rest.api.BjtParkCompanySignService;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyUserService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 打卡controller
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:00
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("BjtParkCompanyClock")
public class BjtParkCompanyClockController extends BaseController<BjtParkCompanyClock, BjtParkCompanyClockService> implements BaseApiService<BjtParkCompanyClock> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkCompanySignController.class);

    @Autowired
    private BjtParkCompanyClockService parkCompanyClockService;
    @Autowired
    private BjtParkCompanyUserService parkCompanyUserService;

    @ApiOperation(value = "公司管理员获取所有员工打卡记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司编号", required = true),
            @ApiImplicitParam(name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "公司编号"),
    })
    @RequestMapping(value = "select/CompanyClockAllList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkCompanyClock>> selectCompanyClockAllList(@RequestParam Map<String, Object> params){
        LOGGER.info("selectCompanyClockAllList");
        BjtParkCompanyUser _companyAdminUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId")));
        if (_companyAdminUser == null) return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        if (_companyAdminUser.getType() != 2 && _companyAdminUser.getType() != 3) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司管理员");
        List<BjtParkCompanyUser> companyUserList = parkCompanyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().eq("company_id", _companyAdminUser.getCompanyId()));
        List<String> userIdList = new ArrayList<>();
        companyUserList.forEach(v->{userIdList.add(v.getUserId());});//过滤公司员工用户编号
        userIdList.add("-1");
        Wrapper entityWrapper = new EntityWrapper<BjtParkCompanySign>().in("user_id", userIdList);
        if (params.get("beginTime")!= null && params.get("endTime") != null) {
            entityWrapper.between(true, "ctime", params.get("beginTime"), params.get("endTime"));
        }
        if (params.get("beginTime") != null && params.get("endTime") == null) {
            entityWrapper.ge("ctime",params.get("beginTime"));
        }
        if (params.get("beginTime") == null && params.get("endTime") != null) {
            entityWrapper.le("ctime", params.get("endTime"));
        }
        List<BjtParkCompanyClock> _list =  parkCompanyClockService.selectList(entityWrapper);
        return new BaseResult(BaseResultEnum.SUCCESS, _list);
    }

    @ApiOperation(value = "获取单个员工打卡记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
            @ApiImplicitParam(name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
    })
    @RequestMapping(value = "select/CompanyClockList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkCompanyClock>> selectCompanyClockList(@RequestParam Map<String, Object> params){
        LOGGER.info("selectCompanyClockList");
        //管理员编号不是当前用户编号，则表示是公司管理员获取单个员工的打卡记录
        if (!params.get("adminUserId").equals(params.get("userId"))) {
            BjtParkCompanyUser _companyAdminUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("adminUserId")));
            if (_companyAdminUser == null) return new BaseResult(BaseResultEnum.ERROR, "非法操作");
            if (_companyAdminUser.getType() != 2 && _companyAdminUser.getType() != 3) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司管理员");
        }
        Wrapper entityWrapper = new EntityWrapper<BjtParkCompanyClock>()
                .eq("user_id", params.get("userId"));
        if (params.get("beginTime")!= null && params.get("endTime") != null) {
            entityWrapper.between(true, "ctime", params.get("beginTime"), params.get("endTime"));
        }
        if (params.get("beginTime") != null && params.get("endTime") == null) {
            entityWrapper.ge("ctime",params.get("beginTime"));
        }
        if (params.get("beginTime") == null && params.get("endTime") != null) {
            entityWrapper.le("ctime", params.get("endTime"));
        }
        List<BjtParkCompanyClock> _list =  parkCompanyClockService.selectList(entityWrapper);
        return new BaseResult(BaseResultEnum.SUCCESS, _list);
    }
}
