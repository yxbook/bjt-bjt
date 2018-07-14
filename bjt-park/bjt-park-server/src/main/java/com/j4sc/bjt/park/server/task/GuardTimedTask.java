package com.j4sc.bjt.park.server.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildAgreement;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildAgreementRecord;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyUser;
import com.j4sc.bjt.park.rest.api.BjtParkBuildGuardService;
import com.j4sc.bjt.park.server.client.SystemApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/17 20:33
 * @Version: 1.0
 **/
@Component
public class GuardTimedTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuardTimedTask.class);
    @Autowired
    private BjtParkBuildGuardService guardService;
    @Autowired
    private SystemApiClient systemApiClient;
    /**
     * 扫描到期门禁权限(每天下午两点执行)
     *
     */
    @Scheduled(cron = "0 0 14 * * ?")
    public void expirePropertyAgreementTask() {
        List<BjtParkBuildGuard> guardList = guardService.selectList(new EntityWrapper<BjtParkBuildGuard>().eq("type", 1).le("end_time", System.currentTimeMillis()));
        LOGGER.info("扫描到期门禁权限::" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        if (guardList != null && guardList.size() > 0) {
            List<Integer> idList = guardList.stream().map(v->v.getGuardId()).collect(Collectors.toList());
            guardService.deleteBatchIds(idList);//删除门禁权限
            List<String> usernameList = guardList.stream().map(i -> i.getApplyUsername()).collect(Collectors.toList());
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("title", "门禁权限到期");
            paramsMap.put("body", "门禁权限到期");
            paramsMap.put("target", "BuildGuard");
            paramsMap.put("id", "-1");
            paramsMap.put("phoneList", usernameList);
            systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        }

    }
}
