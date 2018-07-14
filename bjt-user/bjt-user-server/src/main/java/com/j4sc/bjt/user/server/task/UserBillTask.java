package com.j4sc.bjt.user.server.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.user.dao.entity.BjtUserBill;
import com.j4sc.bjt.user.rest.api.BjtUserBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/17 20:33
 * @Version: 1.0
 **/
@Component
public class UserBillTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBillTask.class);
    @Autowired
    private BjtUserBillService userBillService;

    /**
     * 扫描用户账单(每天凌晨三点执行)
     * 状态（0逾期、1已缴纳、2未缴纳等）
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void expireBill() {
        LOGGER.info("扫描用户账单:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        List<BjtUserBill> userBillList = userBillService.selectList(new EntityWrapper<BjtUserBill>().eq("status", 2));
        if (userBillList.size() > 0) {
            List<BjtUserBill> billList = userBillList.stream().filter(v->v.getCutOffTime() < System.currentTimeMillis()).collect(Collectors.toList());
            billList.forEach(v->v.setStatus(0));
            if (billList.size() > 0) {
                userBillService.updateAllColumnBatchById(billList);//批量更新用户账单状态
            }
        }
    }
}
