package com.j4sc.bjt.park.server.rest.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuild;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildUser;
import com.j4sc.bjt.park.rest.api.*;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Calendar;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/3 11:14
 * @Version: 1.0
 **/
@Service
@Transactional
@BaseService
@RequestMapping("api")
@RestController
@ApiIgnore
public class BjtParkApiServiceImpl implements BjtParkApiService{

    @Autowired
    private BjtParkBuildService parkBuildService;
    @Autowired
    private BjtParkBuildUserService parkBuildUserService;
    @Autowired
    private BjtParkBuildGuardService parkBuildGuardService;

    @Override
    public BaseResult addBuildUser(int buildId,String userId,String username, String realname) {
        //检查用户是否已经是管理者
        BjtParkBuildUser _bjtParkBuildUser = parkBuildUserService.selectOne(new EntityWrapper<BjtParkBuildUser>().eq("user_id",userId).eq("build_id",buildId));
        if (_bjtParkBuildUser!=null)return new BaseResult(BaseResultEnum.ERROR,"用户已经是'"+_bjtParkBuildUser.getBuildId()+"'楼宇管理者");
        BjtParkBuild _bjtParkBuild = parkBuildService.selectById(buildId);
        if (_bjtParkBuild==null)return new BaseResult(BaseResultEnum.ERROR,"楼宇不存在");

        BjtParkBuildUser parkBuildUser = new BjtParkBuildUser();
        parkBuildUser.setBuildId(buildId);
        parkBuildUser.setUserId(userId);
        parkBuildUser.setRealname(realname);
        parkBuildUser.setUsername(username);
        if (!parkBuildUserService.insert(parkBuildUser)) {
            return new BaseResult(BaseResultEnum.ERROR, "新增失败");
        }
        //给楼宇管理员添加门禁权限
        BjtParkBuildGuard guard = new BjtParkBuildGuard();
        guard.setCtime(System.currentTimeMillis());
        guard.setBuildId(buildId);
        guard.setBuildName(_bjtParkBuild.getName());
        guard.setUsername(username);
        guard.setUserRealname(realname);
        guard.setUserId(userId);
        guard.setType(1);//权限  1.正常 0.申请中 -1.禁止
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)+1);
        guard.setEndTime(curr.getTimeInMillis());
        if (parkBuildGuardService.insertAllColumn(guard)) {
            return new BaseResult(BaseResultEnum.SUCCESS, "新增成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "新增失败");
    }



}
