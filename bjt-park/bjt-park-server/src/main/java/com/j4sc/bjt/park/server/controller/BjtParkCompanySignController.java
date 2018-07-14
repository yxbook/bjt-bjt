package com.j4sc.bjt.park.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanySign;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyUser;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 签到的controller
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:04
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("BjtParkCompanySign")
public class BjtParkCompanySignController extends BaseController<BjtParkCompanySign, BjtParkCompanySignService> implements BaseApiService<BjtParkCompanySign>{

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkCompanySignController.class);

    @Autowired
    private BjtParkCompanySignService parkCompanySignService;
    @Autowired
    private BjtParkCompanyUserService parkCompanyUserService;

    @ApiOperation(value = "公司管理员获取所有员工签到记录")
    @RequestMapping(value = "select/CompanySignAllList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkCompanySign>> selectCompanySignAllList(@RequestParam Map<String, Object> params){
        LOGGER.info("selectCompanySignAllList");
        BjtParkCompanyUser _companyAdminUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId")));
        if (_companyAdminUser == null) return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        if (_companyAdminUser.getType() != 2) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司管理员");
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
        List<BjtParkCompanySign> _list =  parkCompanySignService.selectList(entityWrapper);
        return new BaseResult(BaseResultEnum.SUCCESS, _list);
    }

    @ApiOperation(value = "获取单个员工签到记录")
    @RequestMapping(value = "select/CompanySignList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkCompanySign>> selectCompanySignList(@RequestParam Map<String, Object> params){
        LOGGER.info("selectCompanySignList");
        //管理员编号不是当前用户编号，则表示是公司管理员获取单个员工的打卡记录
        if (!params.get("adminUserId").equals(params.get("userId"))) {
            BjtParkCompanyUser _companyAdminUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("adminUserId")));
            if (_companyAdminUser == null) return new BaseResult(BaseResultEnum.ERROR, "非法操作");
            if (_companyAdminUser.getType() != 2) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司管理员");
        }
        Wrapper entityWrapper = new EntityWrapper<BjtParkCompanySign>()
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
        List<BjtParkCompanySign> _list =  parkCompanySignService.selectList(entityWrapper);
        return new BaseResult(BaseResultEnum.SUCCESS, _list);
    }
}
