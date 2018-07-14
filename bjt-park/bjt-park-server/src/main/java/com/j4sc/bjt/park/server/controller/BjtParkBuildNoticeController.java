package com.j4sc.bjt.park.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildNotice;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildUser;
import com.j4sc.bjt.park.dao.entity.BjtParkCompany;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyUser;
import com.j4sc.bjt.park.rest.api.BjtParkBuildNoticeService;
import com.j4sc.bjt.park.rest.api.BjtParkBuildUserService;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyService;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyUserService;
import com.j4sc.bjt.park.server.client.SystemApiClient;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/3 11:30
 * @Version: 1.0
 **/
@ApiModel
@RestController
@RequestMapping("BjtParkBuildNotice")
public class BjtParkBuildNoticeController extends BaseController<BjtParkBuildNotice, BjtParkBuildNoticeService> implements BaseApiService<BjtParkBuildNotice> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildNoticeController.class);

    @Autowired
    private BjtParkBuildNoticeService bjtParkBuildNoticeService;
    @Autowired
    private BjtParkCompanyService bjtParkCompanyService;
    @Autowired
    private BjtParkCompanyUserService bjtParkCompanyUserService;
    @Autowired
    private SystemApiClient systemApiClient;
    @Autowired
    private BjtParkBuildUserService bjtParkBuildUserService;

    @ApiOperation(value = "获取楼宇公告", notes = "获取楼宇公告")
    @RequestMapping(value = "select/BuildNoticeAll", method = RequestMethod.GET)
    public BaseResult<List<BjtParkBuildNotice>> selectBuildNoticeAll(@RequestParam("userId") String userId) {
        LOGGER.info("获取楼宇公告...");
        List<BjtParkBuildUser> buildUserList = bjtParkBuildUserService.selectList(new EntityWrapper<BjtParkBuildUser>().eq("user_id", userId));
        //当前用户不是楼宇管理员
        if (buildUserList.size() < 1) {
            BjtParkCompanyUser companyUser = bjtParkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", userId));
            if (null == companyUser) {
                return new BaseResult(BaseResultEnum.SUCCESS, null);
            }
            BjtParkCompany company = bjtParkCompanyService.selectOne(new EntityWrapper<BjtParkCompany>().eq("company_id", companyUser.getCompanyId()));
            if (null == company) {
                return new BaseResult(BaseResultEnum.SUCCESS, null);
            }
            List<BjtParkBuildNotice> buildNoticeList = bjtParkBuildNoticeService.selectList(new EntityWrapper<BjtParkBuildNotice>().eq("build_id", company.getBuildId()).orderBy("ctime", false));
            return new BaseResult(BaseResultEnum.SUCCESS, buildNoticeList);
        }
        //当前用户是楼宇管理员
        List<Integer> buildIdList = buildUserList.stream().map(v->v.getBuildId()).collect(Collectors.toList());
        List<BjtParkBuildNotice> buildNoticeList = bjtParkBuildNoticeService.selectList(new EntityWrapper<BjtParkBuildNotice>().in("build_id", buildIdList).orderBy("ctime", false));
        return new BaseResult(BaseResultEnum.SUCCESS, buildNoticeList);

    }

    @RequestMapping(value = "save/BuildNotice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveBuildNotice(@RequestBody BjtParkBuildNotice bjtParkBuildNotice) {
        LOGGER.info("新建楼宇公告...");
        if (!bjtParkBuildNoticeService.insert(bjtParkBuildNotice)) {
            return new BaseResult(BaseResultEnum.ERROR, "新建楼宇公告失败");
        }
        List<BjtParkCompany> companyList = bjtParkCompanyService.selectList(new EntityWrapper<BjtParkCompany>().eq("build_id", bjtParkBuildNotice.getBuildId()));
        if (companyList == null || companyList.size() < 1) {
            return new BaseResult(BaseResultEnum.SUCCESS, "新建楼宇公告成功");
        }
        List<Integer> companyIdList = new ArrayList<>();
        companyList.forEach(v -> {
            companyIdList.add(v.getCompanyId());
        });
        companyIdList.add(-1);
        List<BjtParkCompanyUser> companyUserList = bjtParkCompanyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().in("company_id", companyIdList));
        List<String> usernameList = new ArrayList<>();
        companyUserList.forEach(v -> {
            usernameList.add(v.getPhone());
        });
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", "楼宇公告");
        paramsMap.put("body", "楼宇公告");
        paramsMap.put("target", "BuildNotice");
        paramsMap.put("id", bjtParkBuildNotice.getbNoticeId()+ "");
        paramsMap.put("phoneList", usernameList);
        systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        return new BaseResult(BaseResultEnum.SUCCESS, "新建楼宇公告成功");
    }
}
