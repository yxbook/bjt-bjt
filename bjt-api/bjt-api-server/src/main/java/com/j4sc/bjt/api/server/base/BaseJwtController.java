package com.j4sc.bjt.api.server.base;

import com.j4sc.auth.common.jwt.JwtInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/2 14:20
 *
 * @Version: 1.0
 **/
public class BaseJwtController {

    @Autowired
    @Lazy
    protected HttpServletRequest request;
    public String getUserId() {
        return request.getHeader("userId");
    }
    public int getBuildId() {
        if (!"".equals(request.getHeader("buildId")) && request.getHeader("buildId") != null) {
            return Integer.parseInt(request.getHeader("buildId"));
        }
        return -1;
    }
}
