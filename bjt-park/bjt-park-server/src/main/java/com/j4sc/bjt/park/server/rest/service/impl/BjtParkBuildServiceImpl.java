package com.j4sc.bjt.park.server.rest.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildUser;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkBuildMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuild;
import com.j4sc.bjt.park.rest.api.BjtParkBuildService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @Description: BjtParkBuild Service实现
* @Author: LongRou
* @CreateDate: 2018/4/3.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkBuildServiceImpl extends BaseServiceImpl<BjtParkBuildMapper, BjtParkBuild> implements BjtParkBuildService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildServiceImpl.class);

    @Override
    public String getTest() {
        return null;
    }

    @Override
    public BaseResult<List<BjtParkBuild>> getBuildListByIdList(List<Integer> buildIdList) {
       if (buildIdList.size() == 0)return  new BaseResult(BaseResultEnum.SUCCESS,new ArrayList<>());
        return new BaseResult(BaseResultEnum.SUCCESS,baseMapper.selectList(new EntityWrapper<BjtParkBuild>()
                .in("build_id",buildIdList)));
    }
}