package com.j4sc.bjt.park.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyClockRule;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyUser;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyClockRuleService;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyUserService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description: 打卡规则controller
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:08
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("api/companyClockRule")
public class BjtParkCompanyClockRuleController extends BaseController<BjtParkCompanyClockRule, BjtParkCompanyClockRuleService> implements BaseApiService<BjtParkCompanyClockRule> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkCompanyClockRuleController.class);

    @Autowired
    private BjtParkCompanyClockRuleService companyClockRuleService;
    @Autowired
    private BjtParkCompanyUserService companyUserService;

    @ApiOperation(value = "添加打卡规则")
    @RequestMapping(value = "save/CompanyClockRule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanyClockRule(@RequestBody Map<String, Object> params) {
        LOGGER.info("新增打卡规则...");
        BjtParkCompanyUser _companyAdminUser = companyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId")));
        if (_companyAdminUser == null) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司员工");
        if (_companyAdminUser.getType() != 2) new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司管理员");
        BjtParkCompanyClockRule clockRule = new BjtParkCompanyClockRule();
        clockRule.setAddress(params.get("address") + "");
        clockRule.setCompanyId(_companyAdminUser.getCompanyId());
        clockRule.setCtime(System.currentTimeMillis());
        clockRule.setInTime(params.get("inTime") + "");
        clockRule.setOutTime(params.get("outTime") + "");
        clockRule.setLatitude(params.get("latitude") + "");
        clockRule.setLongitude(params.get("longitude") + "");
        clockRule.setWorkDay(params.get("workDay") + "");
        if (companyClockRuleService.insertAllColumn(clockRule)) return new BaseResult(BaseResultEnum.SUCCESS, "添加成功");
        return new BaseResult(BaseResultEnum.ERROR, "添加失败");
    }

    @ApiOperation(value = "删除打卡规则")
    @RequestMapping(value = "delete/CompanyClockRule", method = RequestMethod.DELETE)
    public BaseResult deleteCompanyClockRule(@RequestParam("userId")String userId) {
        BjtParkCompanyUser _companyAdminUser = companyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", userId));
        if (_companyAdminUser == null) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司员工");
        if (_companyAdminUser.getType() != 2) new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司管理员");
        BjtParkCompanyClockRule _clockRule = companyClockRuleService.selectOne(new EntityWrapper<BjtParkCompanyClockRule>().eq("company_id", _companyAdminUser.getCompanyId()));
        if (_clockRule == null) return new BaseResult(BaseResultEnum.ERROR, "打卡规则不存在");
        if (companyClockRuleService.delete(new EntityWrapper<BjtParkCompanyClockRule>().eq("rule_id", _clockRule.getRuleId()).eq("company_id", _clockRule.getCompanyId())))
            return new BaseResult(BaseResultEnum.SUCCESS, "删除成功");
        return new BaseResult(BaseResultEnum.ERROR, "删除失败");
    }

    @RequestMapping(value = "select/CompanyClockRule", method = RequestMethod.GET)
    public BaseResult<BjtParkCompanyClockRule> selectCompanyClockRule(@RequestParam("userId")String userId) {
        BjtParkCompanyUser _companyUser = companyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", userId));
        if (_companyUser == null) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司员工");
        BjtParkCompanyClockRule _clockRule = companyClockRuleService.selectOne(new EntityWrapper<BjtParkCompanyClockRule>().eq("company_id", _companyUser.getCompanyId()));
        if (_clockRule == null) return new BaseResult(BaseResultEnum.ERROR, "打卡规则不存在");
        return new BaseResult(BaseResultEnum.SUCCESS, _clockRule);
    }
}
