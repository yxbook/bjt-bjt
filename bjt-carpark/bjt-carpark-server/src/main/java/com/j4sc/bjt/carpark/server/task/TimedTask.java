package com.j4sc.bjt.carpark.server.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkApplyDetail;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkPayment;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkApplyDetailService;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkPaymentService;
import com.j4sc.bjt.carpark.server.client.SystemApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/27 14:14
 * @Version: 1.0
 **/
@Component
public class TimedTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimedTask.class);

    @Autowired
    private BjtCarparkPaymentService carparkPaymentService;
    @Autowired
    private BjtCarparkApplyDetailService carparkApplyDetailService;

    /**
     * 扫描停车权限，权限状态为:1、待审批；2、（已通过）但未缴费；3、未通过；4、正常
     * 1、查看当前是否有失效日期小于当前日期的权限列表，若有，则查询所有停车预交费订单，
     * （1）若预交费订单列表为空，则直接将所有过期的权限设置为2；
     * （2）若预交费订单列表不为空，则循环失效的权限列表，内嵌预交费订单列表循环，
     * 若有满足条件的权限和预交费订单，则从预交费订单列表中取出该车辆、该用户、该停车场对应的缴费信息是否有多条，
     * 有则取出最接近当前时间的一条，则将预交费订单中的生效日期和失效日期复制到权限列表中，并将状态设置为正常状态
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void carparkPayment() {
        LOGGER.info("扫描停车缴费权限：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        List<BjtCarparkApplyDetail> detailList = carparkApplyDetailService.selectList(new EntityWrapper<BjtCarparkApplyDetail>().le("end_time", System.currentTimeMillis()).eq("status", 4));
        if (detailList != null && detailList.size() > 0) {
            List<BjtCarparkPayment> paymentList = carparkPaymentService.selectList(new EntityWrapper<BjtCarparkPayment>().ge("end_time", System.currentTimeMillis()));
            if (paymentList == null || paymentList.size() < 1) {
                detailList.forEach(v -> {
                    v.setStatus(2);
                });
                carparkApplyDetailService.insertOrUpdateAllColumnBatch(detailList);
            }
            if (paymentList != null && paymentList.size() > 0) {
                detailList.forEach(v -> {
                    List<BjtCarparkPayment> templateList = new ArrayList<>();
                    paymentList.forEach(i -> {
                        if (v.getCarId() == i.getCarId() && v.getSpaceId() == i.getSpaceId() && v.getUserId().equals(i.getUserId())) {
                            templateList.add(i);
                        }
                    });
                    //通过开始时间从小到大排序取最小的
                    BjtCarparkPayment payment = templateList.stream().sorted(Comparator.comparing(BjtCarparkPayment::getBeginTime)).collect(Collectors.toList()).get(0);

                    if (payment != null) {
                        //取出最小的缴费信息
                        v.setBeginTime(payment.getBeginTime());
                        v.setEndTime(payment.getEndTime());
                        v.setStatus(4);
                    }
                });
                carparkApplyDetailService.insertOrUpdateAllColumnBatch(detailList);
            }
        }
    }

    /**
     * 查询权限终止日期是否小于当前时间，若小于，则移除
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void carparkDetail() {
        LOGGER.info("扫描停车权限并移除：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        List<BjtCarparkApplyDetail> detailList = carparkApplyDetailService.selectList(new EntityWrapper<BjtCarparkApplyDetail>().le("terminal_time", System.currentTimeMillis()));
        if (detailList != null && detailList.size() > 0) {
            List<Integer> idList = detailList.stream().map(v->v.getDetailId()).collect(Collectors.toList());//取出停车权限明细编号
            carparkApplyDetailService.deleteBatchIds(idList);//删除停车权限
        }
    }
}
