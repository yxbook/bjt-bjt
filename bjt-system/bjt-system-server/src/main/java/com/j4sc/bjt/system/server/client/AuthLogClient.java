package com.j4sc.bjt.system.server.client;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.j4sc.auth.dao.entity.AuthLog;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/29 18:08
 * @Version: 1.0
 **/
@FeignClient(value = "j4sc-auth-server",path = "/AuthLog",fallback = AuthLogHystrix.class)
public interface AuthLogClient extends BaseApiService<AuthLog>{
    ////测试 可删除
}
