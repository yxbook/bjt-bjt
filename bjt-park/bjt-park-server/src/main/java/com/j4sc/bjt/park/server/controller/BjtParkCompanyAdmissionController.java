package com.j4sc.bjt.park.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.park.common.util.MapToEntityUtil;
import com.j4sc.bjt.park.dao.entity.*;
import com.j4sc.bjt.park.rest.api.*;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.j4sc.bjt.park.common.util.MapToEntityUtil.mapToEntity;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:14
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtParkCompanyAdmission")
public class BjtParkCompanyAdmissionController extends BaseController<BjtParkCompanyAdmission, BjtParkCompanyAdmissionService> implements BaseApiService<BjtParkCompanyAdmission>{
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkCompanyAdmissionController.class);

    @Autowired
    private BjtParkCompanyUserService parkCompanyUserService;
    @Autowired
    private BjtParkCompanyAdmissionService parkCompanyAdmissionService;
    @Autowired
    private BjtParkBuildHouseService parkBuildHouseService;
    @Autowired
    private BjtParkCompanyService parkCompanyService;
    @Autowired
    private BjtParkBuildAgreementService parkBuildAgreementService;
    @Autowired
    private BjtParkBuildGuardService parkBuildGuardService;

    @ApiOperation(value = "查询公司入驻信息", notes = "查询公司入驻信息")
    @RequestMapping(value = "select/CompanyAdmission", method = RequestMethod.GET)
    public BaseResult selectCompanyAdmission(@RequestParam("userId")String userId) {
        LOGGER.info("查询公司入驻信息...");
        BjtParkCompanyUser _companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", userId));
        if (_companyUser == null) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司员工");
        BjtParkCompanyAdmission _companyAdmission = parkCompanyAdmissionService.selectOne(new EntityWrapper<BjtParkCompanyAdmission>()
                .eq("apply_user_id", userId).eq("company_id", _companyUser.getCompanyId()));
        return new BaseResult(BaseResultEnum.SUCCESS, _companyAdmission);
    }
    /**
     * 状态（0表示拒绝、1表示通过且只有物业合同、2表示通过且有物业合同和租赁合同）
     * @param params
     *  approvalUserId, companyId, wyAgreement, state, zlAgreement, buildId 参数
     * @return
     */
    @ApiOperation(value = "公司入驻审批")
    @Transactional
    @RequestMapping(value = "update/CompanyAdmission", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateCompanyAdmission(@RequestBody Map<String, Object> params){
        LOGGER.info("公司入驻审批....");

        BjtParkCompany _parkCompany = parkCompanyService.selectOne(new EntityWrapper<BjtParkCompany>().eq("company_id", params.get("companyId")));
        if (_parkCompany == null) return new BaseResult(BaseResultEnum.ERROR, "公司信息有误");

        BjtParkCompanyAdmission _companyAdmission = parkCompanyAdmissionService.selectById(params.get("companyId")+"");
        if (_companyAdmission == null) return new BaseResult(BaseResultEnum.ERROR, "申请记录不存在");
        if (_companyAdmission.getStatus() != 1) return new BaseResult(BaseResultEnum.ERROR, "申请信息有误");
        _companyAdmission.setApprovalUserId(params.get("approvalUserId")+"");
        _companyAdmission.setApprovalTime(System.currentTimeMillis());
        //拒绝时对物业合同和租赁合同不做任何操作
        if ("0".equals(params.get("state").toString())){
            if (params.get("opinion") == null) return new BaseResult(BaseResultEnum.ERROR, "请填写审批意见");
            _companyAdmission.setStatus(3);//状态：1、待审批；2、已通过、3未通过
            _companyAdmission.setOpinion(params.get("opinion")+"");//拒绝时要填写审批意见
            _companyAdmission.setRemark(params.get("remark")+"");
            if (parkCompanyAdmissionService.insertOrUpdate(_companyAdmission)) return new BaseResult(BaseResultEnum.SUCCESS, "审批成功");
            return new BaseResult(BaseResultEnum.ERROR, "审批失败");
        }
        BjtParkBuildAgreement parkBuildWyAgreement = (BjtParkBuildAgreement) mapToEntity((Map<String, Object>) params.get("wyAgreement"), BjtParkBuildAgreement.class);
        String[] houseNumberArr = parkBuildWyAgreement.getDoorPlate().split(",");
        if (houseNumberArr.length < 1) {
            return new BaseResult(BaseResultEnum.ERROR, "未分配房屋");
        }
        List<String> houseNumberList = Arrays.asList(houseNumberArr);
        List<BjtParkBuildHouse> houseList = parkBuildHouseService.selectList(new EntityWrapper<BjtParkBuildHouse>().eq("build_id", params.get("buildId")
        ).in("house_number", houseNumberList));
        if (houseList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, "房屋信息有误");
        }
        //物业合同
        if ("1".equals(params.get("state").toString()) || "2".equals(params.get("state").toString())) {
            parkBuildWyAgreement = this.calculatePrice(parkBuildWyAgreement);
            parkBuildWyAgreement.setType(1);//合同类型：1、物业合同；2、租赁合同
            parkBuildWyAgreement.setCompanyId(_parkCompany.getCompanyId());//设置公司编号
            parkBuildWyAgreement.setBuildId(_parkCompany.getBuildId());
            parkBuildWyAgreement.setBuildName(_parkCompany.getBuildName());
            if (!parkBuildAgreementService.insert(parkBuildWyAgreement))
                return new BaseResult(BaseResultEnum.ERROR, "添加物业合同失败");
        }
        //租赁合同
        if ("2".equals(params.get("state").toString())){
            BjtParkBuildAgreement parkBuildZlAgreement = (BjtParkBuildAgreement) MapToEntityUtil.mapToEntity((Map<String, Object>) params.get("zlAgreement"), BjtParkBuildAgreement.class);
            parkBuildZlAgreement = this.calculatePrice(parkBuildZlAgreement);
            parkBuildZlAgreement.setType(2);//合同类型：1、物业合同；2、租赁合同
            parkBuildZlAgreement.setCompanyId(_parkCompany.getCompanyId());//设置公司编号
            parkBuildZlAgreement.setBuildId(_parkCompany.getBuildId());
            parkBuildZlAgreement.setBuildName(_parkCompany.getBuildName());
            String agrName = parkBuildZlAgreement.getName();
            Integer agrId = parkBuildZlAgreement.getAgreementId();
            if (!parkBuildAgreementService.insert(parkBuildZlAgreement) ) return new BaseResult(BaseResultEnum.ERROR, "添加租赁合同失败");
            houseList.forEach(v->{
                v.setLeaseAgreementId(agrId);
                v.setLeaseAgreementName(agrName);
            });
        }
        List<String> floorList = houseList.stream().map(v->v.getFloor().toString()).collect(Collectors.toList());//取出所在楼层
        //更新公司信息中的房屋信息
        _parkCompany.setDoorPlate(parkBuildWyAgreement.getDoorPlate());//从物业合同中获取房间号信息
        _parkCompany.setFloor(floorList.toString().replace("[", "").replaceAll("]", ""));
        if (!parkCompanyService.updateById(_parkCompany)) return new BaseResult(BaseResultEnum.ERROR, "审批失败");
        //更新房屋信息中的公司信息
        houseList.forEach(v->{
            v.setCompany(_parkCompany.getName());
            v.setCompanyId(_parkCompany.getCompanyId());
        });
        if (!parkBuildHouseService.updateAllColumnBatchById(houseList)) return new BaseResult(BaseResultEnum.ERROR, "审批失败");
        //更新入驻申请状态
        _companyAdmission.setStatus(2);//状态：1、待审批；2、已通过、3未通过
        if (parkCompanyAdmissionService.insertOrUpdate(_companyAdmission)) return new BaseResult(BaseResultEnum.SUCCESS, "审批成功");
        return new BaseResult(BaseResultEnum.ERROR, "审批失败");
    }

    @ApiOperation("删除公司入驻信息")
    @RequestMapping(value = "delete/CompanyAdmission", method = RequestMethod.DELETE)
    public BaseResult deleteAdmission(@RequestParam("companyId")String companyId, @RequestParam("buildId")String buildId){
        BjtParkCompanyAdmission admission = parkCompanyAdmissionService.selectOne(new EntityWrapper<BjtParkCompanyAdmission>().eq("company_id", companyId).eq("build_id", buildId));
        if (admission == null) {
            return new BaseResult(BaseResultEnum.ERROR, "公司入驻信息为空");
        }
        BjtParkCompany company = parkCompanyService.selectOne(new EntityWrapper<BjtParkCompany>().eq("company_id", companyId));
        if (company == null) {
            return new BaseResult(BaseResultEnum.ERROR, "公司信息为空");
        }
        List<BjtParkBuildGuard> guardList = parkBuildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().eq("company_id", admission.getCompanyId()));
        if (guardList.size() > 0) {
            List<Integer> guardIdList = guardList.stream().map(v->v.getGuardId()).collect(Collectors.toList());
            if (!parkBuildGuardService.deleteBatchIds(guardIdList)) {
                return new BaseResult(BaseResultEnum.ERROR, "删除公司入驻信息失败");
            }
        }
        List<BjtParkBuildHouse> houseList = parkBuildHouseService.selectList(new EntityWrapper<BjtParkBuildHouse>().eq("company_id", admission.getCompanyId()).eq("build_id", buildId));
        if (houseList.size() > 0) {
            houseList.forEach(v->{
                v.setCompanyId(null);
                v.setLeaseAgreementName("");
                v.setLeaseAgreementId(null);
                v.setCompany("");
            });
            if (!parkBuildHouseService.updateAllColumnBatchById(houseList)) {
                return new BaseResult(BaseResultEnum.ERROR, "删除公司入驻信息失败");
            }
        }
        if (!parkCompanyAdmissionService.deleteById(admission.getCompanyId())) {
            return new BaseResult(BaseResultEnum.ERROR, "删除失败");
        }
        //修改公司信息
        company.setBuildName("");
        company.setBuildId(null);
        company.setDoorPlate("");
        company.setCode("");
        company.setFloor("");
        if (parkCompanyService.updateAllColumnById(company)) {
            return new BaseResult(BaseResultEnum.SUCCESS, "删除公司入驻信息成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "删除公司入驻信息失败");
    }

    /**
     * 计算价格
     * @param parkBuildAgreement
     * @return
     */
    private BjtParkBuildAgreement calculatePrice(BjtParkBuildAgreement parkBuildAgreement){
        parkBuildAgreement.setTotalPrice(parkBuildAgreement.getUnitPrice() * parkBuildAgreement.getArea());
        parkBuildAgreement.setCtime(System.currentTimeMillis());
        if (parkBuildAgreement.getPayment() == 1) {
            parkBuildAgreement.setPayFee(parkBuildAgreement.getTotalPrice()+"");
        }
        if (parkBuildAgreement.getPayment() == 2) {
            parkBuildAgreement.setPayFee(String.valueOf(parkBuildAgreement.getTotalPrice() * 3));
        }
        if (parkBuildAgreement.getPayment() == 3) {
            parkBuildAgreement.setPayFee(String.valueOf(parkBuildAgreement.getTotalPrice() * 12));
        }
        return parkBuildAgreement;
    }

}

