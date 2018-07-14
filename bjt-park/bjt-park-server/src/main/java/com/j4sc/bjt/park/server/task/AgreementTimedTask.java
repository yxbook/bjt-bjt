package com.j4sc.bjt.park.server.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.park.common.util.BjtUserBill;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildAgreement;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildAgreementRecord;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyUser;
import com.j4sc.bjt.park.rest.api.BjtParkBuildAgreementRecordService;
import com.j4sc.bjt.park.rest.api.BjtParkBuildAgreementService;
import com.j4sc.bjt.park.rest.api.BjtParkBuildGuardService;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyUserService;
import com.j4sc.bjt.park.server.client.SystemApiClient;
import com.j4sc.bjt.park.server.client.UserBillClient;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/27 14:14
 * @Version: 1.0
 **/
@Component
public class AgreementTimedTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgreementTimedTask.class);

    @Autowired
    private BjtParkBuildAgreementService agreementService;
    @Autowired
    private BjtParkBuildAgreementRecordService agreementRecordService;
    @Autowired
    private BjtParkCompanyUserService companyUserService;
    @Autowired
    private BjtParkBuildGuardService buildGuardService;
    @Autowired
    private SystemApiClient systemApiClient;
    @Autowired
    private UserBillClient userBillClient;

    /**
     * 扫描物业合同生成账单(每天早上十点半执行)
     * 合同类型type：1、物业合同；2、租赁合同
     */
    @Scheduled(cron = "0 30 10 * * ?")
    public void expirePropertyAgreementTask() {
        List<BjtParkBuildAgreement> list = agreementService.selectList(new EntityWrapper<BjtParkBuildAgreement>().eq("open", 1).eq("type", 1).le("end_time", System.currentTimeMillis()));
        LOGGER.info("扫描到期物业合同::" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        if (list != null && list.size() > 0) {
            List<BjtParkBuildAgreementRecord> agreementRecordNewList = new ArrayList<>();
            list.forEach(v -> {
                agreementRecordNewList.add(constructAgreementRecord(v, 3));
            });
            List<Integer> companyIdList = list.stream().map(i -> i.getType()).collect(Collectors.toList());
            //查询所有公司的员工
            companyIdList.add(-1);
            List<BjtParkCompanyUser> companyUserList = companyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().in("company_id", companyIdList));
            List<String> userIdList = companyUserList.stream().map(i -> i.getUserId()).collect(Collectors.toList());//取出用户编号
            userIdList.add("-1");
            List<BjtParkBuildGuard> guardList = buildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().in("user_id", userIdList));
            guardList.forEach(v -> {
                v.setType(-1);//权限  1.正常 0.申请中 -1.禁止
            });
            buildGuardService.updateAllColumnBatchById(guardList);//禁用门禁权限
            agreementRecordService.insertOrUpdateAllColumnBatch(agreementRecordNewList);//批量添加移除的合同到历史合同记录中
            List<Integer> agreementIdList = list.stream().map(i -> i.getAgreementId()).collect(Collectors.toList());
            agreementService.deleteBatchIds(agreementIdList);//批量删除过期的合同

            List<BjtParkCompanyUser> companyAdminUserList = companyUserList.stream().filter(i -> i.getType() == 3).collect(Collectors.toList());//取出公司管理员

            List<String> usernameList = companyAdminUserList.stream().map(i -> i.getPhone()).collect(Collectors.toList());
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("title", "物业合同");
            paramsMap.put("body", "物业合同已到期");
            paramsMap.put("target", "PropertyAgreement");
            paramsMap.put("id", "-1");
            paramsMap.put("phoneList", usernameList);
            systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        }

    }

    /**
     * 扫描物业合同生成账单(每天早上十一点执行)
     * 合同类型type：1、物业合同；2、租赁合同
     */
    @Scheduled(cron = "0 0 11 * * ?")
    public void propertyAgreementTask() {
        LOGGER.info("扫描逾期缴费物业合同::" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        //查询过期的物业合同
        List<BjtParkBuildAgreement> list = agreementService.selectList(new EntityWrapper<BjtParkBuildAgreement>()
                .eq("open", 1).eq("type", 1));
        if (list != null && list.size() > 0) {
            List<BjtParkBuildAgreement> greatSevenList = new ArrayList<>();//逾期7天未缴费
            List<BjtParkBuildAgreement> smallSevenList = new ArrayList<>();//距离缴费日期小于等于7天
            List<BjtParkBuildAgreement> greatThanOneList = new ArrayList<>();//逾期1天
            list.forEach(v -> {
                if (System.currentTimeMillis() - v.getNextTime() > 86400 * 7 * 1000) {
                    greatSevenList.add(v);
                }
                if (v.getNextTime() - System.currentTimeMillis() <= 86400 * 7 * 1000) {
                    smallSevenList.add(v);
                }
                if (System.currentTimeMillis() - v.getNextTime() <= 86400 * 1 * 1000) {
                    greatThanOneList.add(v);
                }
            });
            if (greatThanOneList.size() > 0) {
                List<Integer> companyIdList = greatThanOneList.stream().map(i -> i.getType()).collect(Collectors.toList());
                //查询公司管理员
                List<BjtParkCompanyUser> companyAdminUserList = companyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().in("company_id", companyIdList).eq("type", 3));
                List<String> usernameList = companyAdminUserList.stream().map(i -> i.getPhone()).collect(Collectors.toList());
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("title", "物业合同");
                paramsMap.put("body", "物业合同已逾期");
                paramsMap.put("target", "");
                paramsMap.put("id", "-1");
                paramsMap.put("phoneList", usernameList);
                systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
            }
            if (smallSevenList.size() > 0) {
                List<Integer> companyIdList = smallSevenList.stream().map(i -> i.getCompanyId()).collect(Collectors.toList());
                //查询公司管理员
                List<BjtParkCompanyUser> companyAdminUserList = companyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().in("company_id", companyIdList).eq("type", 3));
                smallSevenList.forEach(v -> {
                    List<BjtParkCompanyUser> userList = companyAdminUserList.stream().filter(s -> v.getCompanyId() == s.getCompanyId()).collect(Collectors.toList());
                    BjtUserBill userBill = new BjtUserBill();
                    userBill.setCtime(System.currentTimeMillis());
                    userBill.setMoney(new BigDecimal(v.getPayFee().toString()));
                    userBill.setCutOffTime(v.getNextTime());//支付截止时间为下次缴费时间
                    userBill.setStatus(2);//状态（0逾期、1已缴纳、2未缴纳等）
                    userBill.setType(2);//账单类型（1租金、2物业费等）
                    userBill.setAgreementId(v.getAgreementId());
                    //取公司管理员中的一个
                    if (userList.size() > 0) {
                        userBill.setUserId(userList.get(0).getUserId());
                        userBill.setUsername(userList.get(0).getPhone());
                        userBill.setCompanyId(userList.get(0).getCompanyId());
                        userBill.setCompanyName(userList.get(0).getCompanyName());
                    }
                    userBill.setHouseNumber(v.getDoorPlate());
                    userBill.setBuildId(v.getBuildId());
                    userBillClient.insert(userBill);
                    List<String> phoneList = new ArrayList<>();
                    phoneList.add(userBill.getUsername());
                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put("title", "物业账单");
                    paramsMap.put("body", "物业账单已生成");
                    paramsMap.put("target", "");
                    paramsMap.put("id", "-1");
                    paramsMap.put("phoneList", phoneList);
                    systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
                });

            }
            if (greatSevenList.size() > 0) {
                List<BjtParkBuildAgreementRecord> agreementRecordNewList = new ArrayList<>();
                list.forEach(v -> {
                    agreementRecordNewList.add(constructAgreementRecord(v, 3));//操作：1、合同续签；2、合同中止；3、合同到期
                });
                List<Integer> companyIdList = list.stream().map(i -> i.getType()).collect(Collectors.toList());
                //查询所有公司的员工
                companyIdList.add(-1);
                List<BjtParkCompanyUser> companyUserList = companyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().in("company_id", companyIdList));
                List<String> userIdList = companyUserList.stream().map(i -> i.getUserId()).collect(Collectors.toList());//取出用户编号
                userIdList.add("-1");
                List<BjtParkBuildGuard> guardList = buildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().in("user_id", userIdList));
                guardList.forEach(v -> {
                    v.setType(-1);//权限  1.正常 0.申请中 -1.禁止
                });
                buildGuardService.updateAllColumnBatchById(guardList);//禁用门禁权限
                agreementRecordService.insertOrUpdateAllColumnBatch(agreementRecordNewList);//批量添加移除的合同到历史合同记录中
                List<Integer> agreementIdList = list.stream().map(i -> i.getAgreementId()).collect(Collectors.toList());
                agreementService.deleteBatchIds(agreementIdList);//批量删除过期的合同

                List<BjtParkCompanyUser> companyAdminUserList = companyUserList.stream().filter(i -> i.getType() == 3).collect(Collectors.toList());//取出公司管理员

                List<String> usernameList = companyAdminUserList.stream().map(i -> i.getPhone()).collect(Collectors.toList());
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("title", "物业合同");
                paramsMap.put("body", "物业合同已逾期");
                paramsMap.put("target", "");
                paramsMap.put("id", "-1");
                paramsMap.put("phoneList", usernameList);
                systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
            }
        }
    }

    /**
     * 是否开启缴费(1、开启；2、关闭)',
     * 扫描租赁合同生成账单(每天早上十点执行)
     * 合同类型type：1、物业合同；2、租赁合同
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void expireLeaseAgreementTask() {
        LOGGER.info("扫描到期租赁合同::" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        //查询过期的租赁合同
        List<BjtParkBuildAgreement> list = agreementService.selectList(new EntityWrapper<BjtParkBuildAgreement>()
                .eq("type", 2).eq("open", 1).le("end_time", System.currentTimeMillis()));
        if (list != null && list.size() > 0) {
            List<BjtParkBuildAgreementRecord> agreementRecordNewList = new ArrayList<>();
            list.forEach(v -> {
                agreementRecordNewList.add(constructAgreementRecord(v, 3));
            });
            List<Integer> companyIdList = list.stream().map(i -> i.getType()).collect(Collectors.toList());
            agreementRecordService.insertOrUpdateAllColumnBatch(agreementRecordNewList);//批量添加移除的合同到历史合同记录中
            List<Integer> agreementIdList = list.stream().map(i -> i.getAgreementId()).collect(Collectors.toList());
            agreementService.deleteBatchIds(agreementIdList);//批量删除过期的合同
            //查询公司管理员
            List<BjtParkCompanyUser> companyAdminUserList = companyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().in("company_id", companyIdList).eq("type", 3));

            List<String> usernameList = companyAdminUserList.stream().map(i -> i.getPhone()).collect(Collectors.toList());
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("title", "租赁合同");
            paramsMap.put("body", "租赁合同已到期");
            paramsMap.put("target", "");
            paramsMap.put("id", "-1");
            paramsMap.put("phoneList", usernameList);
            systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        }
    }

    /**
     * 扫描租赁合同生成账单(每天早上十点半执行)
     * 合同类型type：1、物业合同；2、租赁合同
     */
    @Scheduled(cron = "0 30 10 * * ?")
    public void overdueLeaseAgreementTask() {
        LOGGER.info("扫描逾期缴费租赁合同::" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        //查询过期的租赁合同
        List<BjtParkBuildAgreement> list = agreementService.selectList(new EntityWrapper<BjtParkBuildAgreement>()
                .eq("open", 1).eq("type", 2));
        if (list != null && list.size() > 0) {
            List<BjtParkBuildAgreement> greatSevenList = new ArrayList<>();//逾期7天未缴费
            List<BjtParkBuildAgreement> smallSevenList = new ArrayList<>();//距离缴费日期小于等于7天
            List<BjtParkBuildAgreement> greatThanOneList = new ArrayList<>();//逾期1天
            list.forEach(v -> {
                if (System.currentTimeMillis() - v.getNextTime() > 86400 * 7 * 1000) {
                    greatSevenList.add(v);
                }
                if (v.getNextTime() - System.currentTimeMillis() <= 86400 * 7 * 1000) {
                    smallSevenList.add(v);
                }
                if (System.currentTimeMillis() - v.getNextTime() <= 86400 * 1 * 1000) {
                    greatThanOneList.add(v);
                }
            });
            //逾期1天
            if (greatThanOneList.size() > 0) {
                List<Integer> companyIdList = greatThanOneList.stream().map(i -> i.getType()).collect(Collectors.toList());
                //查询公司管理员
                List<BjtParkCompanyUser> companyAdminUserList = companyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().in("company_id", companyIdList).eq("type", 3));
                List<String> usernameList = companyAdminUserList.stream().map(i -> i.getPhone()).collect(Collectors.toList());
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("title", "租赁合同");
                paramsMap.put("body", "租赁合同已逾期");
                paramsMap.put("target", "");
                paramsMap.put("id", "-1");
                paramsMap.put("phoneList", usernameList);
                systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
            }
            //距离缴费日期小于等于7天
            if (smallSevenList.size() > 0) {
                List<Integer> companyIdList = smallSevenList.stream().map(i -> i.getCompanyId()).collect(Collectors.toList());
                //查询公司管理员
                List<BjtParkCompanyUser> companyAdminUserList = companyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().in("company_id", companyIdList).eq("type", 3));
                smallSevenList.forEach(v -> {
                    List<BjtParkCompanyUser> userList = companyAdminUserList.stream().filter(s -> v.getCompanyId() == s.getCompanyId()).collect(Collectors.toList());
                    BjtUserBill userBill = new BjtUserBill();
                    userBill.setCtime(System.currentTimeMillis());
                    userBill.setMoney(new BigDecimal(v.getPayFee().toString()));
                    userBill.setCutOffTime(v.getNextTime());//支付截止时间为下次缴费时间
                    userBill.setStatus(1);//状态（0逾期、1已缴纳、2未缴纳等）
                    userBill.setType(1);//账单类型（1租金、2停车、3物业费等）
                    userBill.setAgreementId(v.getAgreementId());
                    //取公司管理员中的一个
                    if (userList.size() > 0) {
                        userBill.setUserId(userList.get(0).getUserId());
                        userBill.setUsername(userList.get(0).getPhone());
                    }
                    BaseResult baseResult = userBillClient.insert(userBill);
                    if (baseResult.status == BaseResultEnum.SUCCESS.getStatus()) {

                    }
                    List<String> phoneList = new ArrayList<>();
                    phoneList.add(userBill.getUsername());
                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put("title", "房租账单");
                    paramsMap.put("body", "房租账单已生成");
                    paramsMap.put("target", "");
                    paramsMap.put("id", "-1");
                    paramsMap.put("phoneList", phoneList);
                    systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
                });

            }
            //逾期7天未缴费
            if (greatSevenList.size() > 0) {
                List<Integer> companyIdList = greatSevenList.stream().map(i -> i.getCompanyId()).collect(Collectors.toList());
                //查询公司管理员
                List<BjtParkCompanyUser> companyAdminUserList = companyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().in("company_id", companyIdList).eq("type", 3));
                List<String> phoneList = companyAdminUserList.stream().map(v -> v.getPhone()).collect(Collectors.toList());
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("title", "租赁合同");
                paramsMap.put("body", "租赁合同已逾期");
                paramsMap.put("target", "");
                paramsMap.put("id", "-1");
                paramsMap.put("phoneList", phoneList);
                systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
            }
        }
    }

    //构造合同历史记录
    private BjtParkBuildAgreementRecord constructAgreementRecord(BjtParkBuildAgreement v, int operate) {
        BjtParkBuildAgreementRecord record = new BjtParkBuildAgreementRecord();
        record.setAgreementId(v.getAgreementId());
        record.setArea(v.getArea());
        record.setBeginTime(v.getBeginTime());
        record.setEndTime(v.getEndTime());
        record.setCompanyId(v.getCompanyId());
        record.setContract(v.getContract());
        record.setCtime(v.getCtime());
        record.setDeposit(v.getDeposit());
        record.setDoorPlate(v.getDoorPlate());
        record.setFirstParty(v.getFirstParty());
        record.setNumber(v.getNumber());
        record.setOpen(v.getOpen());
        record.setName(v.getName());
        record.setPayFee(v.getPayFee());
        record.setTotalPrice(v.getTotalPrice());
        record.setNextTime(v.getNextTime());
        record.setOperate(operate);//操作：1、合同续签；2、合同中止；3、合同到期
        record.setResource(v.getResource());
        record.setPayment(v.getPayment());
        record.setSecendParty(v.getSecendParty());
        record.setType(v.getType());
        record.setUnitPrice(v.getUnitPrice());
        record.setBuildId(v.getBuildId());
        record.setBuildName(v.getBuildName());
        return record;
    }

}
