package com.j4sc.bjt.system.server.client;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.auth.dao.entity.AuthLog;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/19 9:48
 * @Version: 1.0
 **/
@Component
public class AuthLogHystrix implements AuthLogClient {
    @Override
    public BaseResult insert(AuthLog entity) {
        return null;
    }

    @Override
    public BaseResult deleteById(String id) {
        return null;
    }

    @Override
    public BaseResult updateById(AuthLog entity) {
        return null;
    }

    @Override
    public BaseResult<AuthLog> selectById(String id) {
        return null;
    }

    @Override
    public BaseResult<List<AuthLog>> selectAll(Map<String, Object> params) {
        return null;
    }

    @Override
    public BaseResult<Page<AuthLog>> selectPage(Map<String, Object> params) {
        return null;
    }
}
