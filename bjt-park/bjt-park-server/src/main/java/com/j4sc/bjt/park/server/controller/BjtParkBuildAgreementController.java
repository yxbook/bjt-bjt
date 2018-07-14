package com.j4sc.bjt.park.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildAgreement;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildAgreementRecord;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildUser;
import com.j4sc.bjt.park.rest.api.BjtParkBuildAgreementRecordService;
import com.j4sc.bjt.park.rest.api.BjtParkBuildAgreementService;
import com.j4sc.bjt.park.rest.api.BjtParkBuildUserService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/11 12:21
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("ParkBuildAgreement")
public class BjtParkBuildAgreementController extends BaseController<BjtParkBuildAgreement, BjtParkBuildAgreementService> implements BaseApiService<BjtParkBuildAgreement> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildAgreementController.class);

    @Autowired
    private BjtParkBuildAgreementService agreementService;
    @Autowired
    private BjtParkBuildUserService parkBuildUserService;
    @Autowired
    private BjtParkBuildAgreementRecordService agreementRecordService;
    /**
     * 合同历史记录中：operate操作：1、合同续签；2、合同中止；3、合同到期
     * @param params
     * @return
     */
    @ApiOperation("合同续签")
    @RequestMapping(path = "add/RenewalAgreement", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addRenewalArgeement(@RequestBody Map<String,Object> params){
        LOGGER.info("合同续签");
        return null;
    }
    @ApiOperation("合同终止")
    @RequestMapping(path = "update/StopAgreement", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateStopAgreement(@RequestBody Map<String, Object> params){
        BjtParkBuildUser buildUser = parkBuildUserService.selectOne(new EntityWrapper<BjtParkBuildUser>().eq("user_id", params.get("userId").toString()));
        if (buildUser == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不是楼宇管理员");
        }
        BjtParkBuildAgreement agreement = agreementService.selectOne(new EntityWrapper<BjtParkBuildAgreement>().eq("agreement_id", params.get("agreementId")));
        if (agreement == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前合同不存在");
        }
        if (buildUser.getBuildId() != agreement.getBuildId()) {
            return new BaseResult(BaseResultEnum.ERROR, "不能操作该合同");
        }
        if (agreementRecordService.insertOrUpdateAllColumn(constructAgreementRecord(agreement, 2))){
            if (agreementService.deleteById(agreement.getAgreementId())) {
                return new BaseResult(BaseResultEnum.SUCCESS, "操作成功");
            }
        }
        return new BaseResult(BaseResultEnum.ERROR, "操作失败");
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
