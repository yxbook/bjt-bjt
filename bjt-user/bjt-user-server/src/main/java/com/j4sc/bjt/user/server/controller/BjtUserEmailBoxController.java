package com.j4sc.bjt.user.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.user.dao.entity.BjtUserEmailBox;
import com.j4sc.bjt.user.dao.entity.BjtUserEmailBoxRecord;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.bjt.user.rest.api.BjtUserEmailBoxRecordService;
import com.j4sc.bjt.user.rest.api.BjtUserEmailBoxService;
import com.j4sc.bjt.user.rest.api.BjtUserUserService;
import com.j4sc.bjt.user.server.client.SystemApiClient;
import com.j4sc.bjt.user.server.util.SendToPushQueue;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/10 15:30
 * @Version: 1.0
 **/
@Api(tags = {"发件箱服务"}, description = "发件箱服务")
@RestController
@ApiModel
@RequestMapping("api/emailBox")
public class BjtUserEmailBoxController extends BaseController<BjtUserEmailBox, BjtUserEmailBoxService> implements BaseApiService<BjtUserEmailBox> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtUserEmailBoxController.class);

    @Autowired
    private BjtUserEmailBoxService userEmailBoxService;
    @Autowired
    private BjtUserEmailBoxRecordService userEmailBoxRecordService;
    @Autowired
    private BjtUserUserService userService;
    @Autowired
    private SystemApiClient systemApiClient;

    @ApiOperation(value = "删除发件箱邮件", notes = "删除发件箱邮件")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "delete/EmailBox", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteEmailBox(@RequestParam("userId") String userId, @RequestBody List<String> list) {
        LOGGER.info("deleteEmailBox = > 删除发件箱邮件");
        list.add("-1");
        List<BjtUserEmailBox> listEmailBox = userEmailBoxService.selectList(new EntityWrapper<BjtUserEmailBox>().eq("sender_id", userId).in("email_id", list));
        if (listEmailBox == null || listEmailBox.size() < 1) return new BaseResult(BaseResultEnum.ERROR, "删除发件箱邮件失败");
        List<Integer> idList = new ArrayList<>();
        listEmailBox.forEach(v -> {
            idList.add(v.getEmailId());
        });
        if (userEmailBoxService.deleteBatchIds(idList)) return new BaseResult(BaseResultEnum.SUCCESS, "删除发件箱邮件成功");
        return new BaseResult(BaseResultEnum.ERROR, "删除发件箱邮件失败");
    }

    @ApiOperation(value = "搜索发件箱邮件", notes = "搜索发件箱邮件")
    @RequestMapping(value = "select/PageEmailBox", method = RequestMethod.GET)
    public BaseResult<Page<BjtUserEmailBox>> selectPageEmailBox(@RequestParam Map<String, Object> params) {
        LOGGER.info("selectPageEmailBox = > 搜索邮件");
        Page<BjtUserEmailBox> pageModel = new Page<>();
        pageModel.setCurrent(Integer.parseInt(params.get("page").toString()));
        pageModel.setSize(Integer.parseInt(params.get("size").toString()));
        Wrapper entityWrapper = new EntityWrapper<BjtUserEmailBoxRecord>().eq("sender_id", params.get("userId")).orderBy("ctime", false);
        if (params.get("key") != null && !"".equals(params.get("key"))) {
            entityWrapper.like("title", params.get("key") + "");
        }
        Page<BjtUserEmailBox> pageData = userEmailBoxService.selectPage(pageModel, entityWrapper);
        return new BaseResult(BaseResultEnum.SUCCESS, pageData);
    }

    @ApiOperation(value = "发送邮件", notes = "发送邮件")
    @Transactional
    @RequestMapping(value = "save/SendEmailBox", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveSendEmailBox(@RequestBody BjtUserEmailBox bjtUserEmailBox) {
        LOGGER.info("saveSendEmailBox = > 发送邮件");
        bjtUserEmailBox.setReceiverId(bjtUserEmailBox.getReceiverId()+",1");
        List<String> receiverIdList = Arrays.asList(bjtUserEmailBox.getReceiverId().split(","));//取出收件人ID
        java.util.Arrays.asList(receiverIdList);
        List<BjtUserEmailBoxRecord> emailBoxRecordList = new ArrayList<>();
        bjtUserEmailBox.setBoxType(2);//1：草稿箱、2：发件箱、3：垃圾箱
        bjtUserEmailBox.setCtime(System.currentTimeMillis());
        bjtUserEmailBox.setSendDate(System.currentTimeMillis());
        bjtUserEmailBox.setSendType(0);//0为普通, 1为急件

        List<BjtUserUser> listUser = userService.selectList(new EntityWrapper<BjtUserUser>().in("user_id", receiverIdList));
        if (userEmailBoxService.insertOrUpdate(bjtUserEmailBox)) {
            listUser.forEach(v -> {
                BjtUserEmailBoxRecord emailBoxRecord = new BjtUserEmailBoxRecord();
                emailBoxRecord.setBoxType(1);//1：收件箱、2：垃圾箱
                emailBoxRecord.setEmailStatus(0);//0：未读、1：已读、2：回复、3：转发、4：全部转发
                emailBoxRecord.setTitle(bjtUserEmailBox.getTitle());
                emailBoxRecord.setContent(bjtUserEmailBox.getContent());
                emailBoxRecord.setCopyer(bjtUserEmailBox.getCopyer());
                emailBoxRecord.setCopyerId(bjtUserEmailBox.getCopyerId());
                emailBoxRecord.setReceiverId(v.getUserId());
                emailBoxRecord.setReceiverUsername(v.getUsername());
                emailBoxRecord.setResource(bjtUserEmailBox.getResource());
                emailBoxRecord.setSender(bjtUserEmailBox.getSender());
                emailBoxRecord.setSenderId(bjtUserEmailBox.getSenderId());
                emailBoxRecord.setSendDate(System.currentTimeMillis());
                emailBoxRecord.setSendType(bjtUserEmailBox.getSendType());//0为普通，1为急件
                emailBoxRecord.setEmailId(bjtUserEmailBox.getEmailId());
                emailBoxRecord.setCtime(System.currentTimeMillis());
                emailBoxRecord.setRemark(bjtUserEmailBox.getRemark());
                emailBoxRecord.setSenderAvatar(bjtUserEmailBox.getSenderAvatar());
                emailBoxRecord.setSenderUsername(bjtUserEmailBox.getSenderUsername());
                emailBoxRecordList.add(emailBoxRecord);
            });
            userEmailBoxRecordService.insertBatch(emailBoxRecordList);//批量添加发送记录
        }
        emailBoxRecordList.forEach(v -> {
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("title", "新邮件");
            paramsMap.put("body", "新邮件提醒");
            paramsMap.put("target", "EmailBox");
            paramsMap.put("id",v.getRecordId()+"");
            List<String> phoneList = new ArrayList<>();
            phoneList.add(v.getReceiverUsername());
            paramsMap.put("phoneList", phoneList);
            systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        });
        return new BaseResult(BaseResultEnum.SUCCESS, "发送邮件成功");
    }

    @ApiOperation(value = "保存邮件", notes = "保存邮件")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "save/EmailBox", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveEmailBox(@RequestBody BjtUserEmailBox bjtUserEmailBox) {
        LOGGER.info("saveEmailBox = > 保存邮件");
        bjtUserEmailBox.setBoxType(1);//1：草稿箱、2：发件箱、3：垃圾箱
        if (userEmailBoxService.insert(bjtUserEmailBox)) return new BaseResult(BaseResultEnum.SUCCESS, "保存邮件成功");
        return new BaseResult(BaseResultEnum.ERROR, "保存邮件失败");
    }

    @ApiOperation(value = "查看邮件详情", notes = "查看邮件详情")
    @RequestMapping(value = "select/EmailBox", method = RequestMethod.GET)
    public BaseResult<BjtUserEmailBox> selectEmailBox(@RequestParam("userId") String
                                                              userId, @RequestParam("emailId") String emailId) {
        LOGGER.info("selectEmailBox = > 查看邮件详情");
        BjtUserEmailBox _bjtUserEmailBox = userEmailBoxService.selectOne(new EntityWrapper<BjtUserEmailBox>().eq("email_id", emailId).eq("sender_id", userId));
        return new BaseResult(BaseResultEnum.SUCCESS, _bjtUserEmailBox);
    }

}
