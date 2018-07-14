package com.j4sc.bjt.api.server.client;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.user.dao.entity.BjtUserEmailBox;
import com.j4sc.bjt.user.dao.entity.BjtUserEmailBoxRecord;
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
 * @Description: 用户收件箱
 * @Author: chengyz
 * @CreateDate: 2018/4/10 14:36
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-user-server",path = "/BjtUserEmailBoxRecord")
public interface UserEmailBoxRecordClient extends BaseApiService<BjtUserEmailBoxRecord> {

    @RequestMapping(value = "select/EmailBoxRecord", method = RequestMethod.GET)
    BaseResult<BjtUserEmailBoxRecord> selectEmailBoxRecord(@RequestParam("userId") String userId, @RequestParam("recordId")String recordId);

    @RequestMapping(value = "delete/EmailBoxRecord", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult deleteEmailBoxRecord(@RequestParam("userId")String userId,@RequestBody List<String> list);

    @RequestMapping(value = "update/EmailBoxRecordList", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateEmailBoxRecordList(@RequestParam("userId")String userId, @RequestBody List<String> list);

    @RequestMapping(value = "select/PageEmailBoxRecord", method = RequestMethod.GET)
    BaseResult<Page<BjtUserEmailBoxRecord>> selectPageEmailBoxRecord(@RequestParam Map<String, Object> params);

}
