package com.j4sc.bjt.api.server.client;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.user.dao.entity.BjtUserEmailBox;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description: 用户发件箱
 * @Author: chengyz
 * @CreateDate: 2018/4/10 14:36
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-user-server", path = "api/emailBox")
public interface UserEmailBoxClient extends BaseApiService<BjtUserEmailBox> {

    @RequestMapping(value = "select/EmailBox", method = RequestMethod.GET)
    BaseResult<BjtUserEmailBox> selectEmailBox(@RequestParam("userId") String userId, @RequestParam("emailId") String emailId);

    @RequestMapping(value = "delete/EmailBox", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult deleteEmailBox(@RequestParam("userId") String userId, @RequestBody List<String> list);

    @RequestMapping(value = "select/PageEmailBox", method = RequestMethod.GET)
    BaseResult<Page<BjtUserEmailBox>> selectPageEmailBox(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "save/SendEmailBox", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveSendEmailBox(@RequestBody BjtUserEmailBox bjtUserEmailBox);

    @RequestMapping(value = "save/EmailBox", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveEmailBox(@RequestBody BjtUserEmailBox bjtUserEmailBox);
}
