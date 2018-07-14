package com.j4sc.bjt.user.rest.api;

import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.common.base.BaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
* @Description: BjtUserUser Service接口
* @Author: LongRou
* @CreateDate: 2018/3/29.
* @Version: 1.0
**/
public interface BjtUserUserService extends BaseService<BjtUserUser> {
}