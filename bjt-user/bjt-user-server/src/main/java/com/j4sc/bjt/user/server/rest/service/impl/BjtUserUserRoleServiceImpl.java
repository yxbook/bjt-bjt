package com.j4sc.bjt.user.server.rest.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.user.dao.entity.BjtUserRole;
import com.j4sc.bjt.user.dao.mapper.BjtUserRoleMapper;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.user.dao.mapper.BjtUserUserRoleMapper;
import com.j4sc.bjt.user.dao.entity.BjtUserUserRole;
import com.j4sc.bjt.user.rest.api.BjtUserUserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* @Description: BjtUserUserRole Service实现
* @Author: LongRou
* @CreateDate: 2018/3/30.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtUserUserRoleServiceImpl extends BaseServiceImpl<BjtUserUserRoleMapper, BjtUserUserRole> implements BjtUserUserRoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtUserUserRoleServiceImpl.class);
    @Override
    public boolean replaceRoleByBU(String userId, String buildId, List<Integer> list) {
        Integer[] roles = list.stream().filter(v->v!=1).distinct().toArray(Integer[]::new);
        baseMapper.delete(new EntityWrapper<BjtUserUserRole>().eq("user_id",userId)
                .eq("build_id",buildId).and("role_id != 1"));
        BjtUserUserRole bjtUserUserRole = new BjtUserUserRole();
        Arrays.asList(roles).forEach(v ->{
            bjtUserUserRole.setBuildId(Integer.parseInt(buildId));
            bjtUserUserRole.setUserId(userId);
            bjtUserUserRole.setRoleId(v);
            baseMapper.insert(bjtUserUserRole);
        });
        return true;
    }
}