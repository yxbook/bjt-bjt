package com.j4sc.bjt.user.rest.api;

import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseService;
import com.j4sc.bjt.user.dao.entity.BjtUserUserRole;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
* @Description: BjtUserUserRole Service接口
* @Author: LongRou
* @CreateDate: 2018/3/30.
* @Version: 1.0
**/
public interface BjtUserUserRoleService extends BaseService<BjtUserUserRole> {
    //更改用户权限
    boolean replaceRoleByBU(String userId,String buildId,List<Integer> list);
}