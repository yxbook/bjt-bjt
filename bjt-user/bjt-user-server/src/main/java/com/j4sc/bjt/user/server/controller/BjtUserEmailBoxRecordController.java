package com.j4sc.bjt.user.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.user.dao.entity.BjtUserEmailBox;
import com.j4sc.bjt.user.dao.entity.BjtUserEmailBoxRecord;
import com.j4sc.bjt.user.rest.api.BjtUserEmailBoxRecordService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/10 15:30
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("BjtUserEmailBoxRecord")
@Api(tags = {"收件箱服务"}, description = "收件箱服务")
public class BjtUserEmailBoxRecordController extends BaseController<BjtUserEmailBoxRecord, BjtUserEmailBoxRecordService> implements BaseApiService<BjtUserEmailBoxRecord> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtUserEmailBoxRecordController.class);

    @Autowired
    private BjtUserEmailBoxRecordService userEmailBoxRecordService;

    @ApiOperation(value = "删除收件箱邮件", notes = "删除收件箱邮件")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "delete/EmailBoxRecord", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteEmailBoxRecord(@RequestParam("userId")String userId, @RequestBody List<String> list) {
        LOGGER.info("deleteEmailBoxRecord = > 删除收件箱邮件");
        list.add("-1");
        List<BjtUserEmailBoxRecord> emailBoxRecordList = userEmailBoxRecordService.selectList(new EntityWrapper<BjtUserEmailBoxRecord>().eq("receiver_id", userId).in("record_id", list));
        if (emailBoxRecordList == null || emailBoxRecordList.size() < 1) return new BaseResult(BaseResultEnum.ERROR, "删除收件箱邮件失败");
        List<Integer> recordIdList = new ArrayList<>();
        emailBoxRecordList.forEach(v->{recordIdList.add(v.getRecordId());});
        if (userEmailBoxRecordService.deleteBatchIds(recordIdList)) return new BaseResult(BaseResultEnum.SUCCESS, "删除收件箱邮件成功");
        return new BaseResult(BaseResultEnum.ERROR, "删除收件箱邮件失败");
    }

    @ApiOperation(value = "收件人标记邮件为已读", notes = "收件人标记邮件为已读")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "update/EmailBoxRecordList", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateEmailBoxRecordList(@RequestParam("userId")String userId, @RequestBody List<String> list) {
        LOGGER.info("updateEmailBoxRecordList = > 收件人标记邮件为已读");
        list.add("-1");
        List<BjtUserEmailBoxRecord> emailBoxRecordList = userEmailBoxRecordService.selectList(new EntityWrapper<BjtUserEmailBoxRecord>().eq("receiver_id", userId).in("record_id", list));
        if (emailBoxRecordList == null || emailBoxRecordList.size() < 1) return new BaseResult(BaseResultEnum.ERROR, "标记收件箱邮件失败");
        if (emailBoxRecordList.size() > 0 && emailBoxRecordList != null) {
            emailBoxRecordList.forEach(v -> {
                v.setEmailStatus(1);//收件人标记为已读
                v.setReadDate(System.currentTimeMillis());//设置读取时间
            });
            if (userEmailBoxRecordService.insertOrUpdateAllColumnBatch(emailBoxRecordList))
                return new BaseResult(BaseResultEnum.SUCCESS, "标记邮件成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "标记邮件失败");
    }

    @ApiOperation(value = "收件箱(可搜索)", notes = "收件箱(可搜索)")
    @RequestMapping(value = "select/PageEmailBoxRecord", method = RequestMethod.GET)
    public BaseResult<Page<BjtUserEmailBoxRecord>> selectPageEmailBoxRecord(@RequestParam Map<String, Object> params) {
        LOGGER.info("selectPageEmailBoxRecord = > 收件箱");
        Page<BjtUserEmailBoxRecord> pageModel = new Page<>();
        pageModel.setCurrent(Integer.parseInt(params.get("page").toString()));
        pageModel.setSize(Integer.parseInt(params.get("size").toString()));
        Wrapper entityWrapper = new EntityWrapper<BjtUserEmailBoxRecord>().eq("receiver_id", params.get("userId")).orderBy("ctime", false);
        if (params.get("key") != null && !"".equals(params.get("key"))) {
            entityWrapper.like("title", params.get("key")+"");
        }
        Page<BjtUserEmailBoxRecord> pageData = userEmailBoxRecordService.selectPage(pageModel, entityWrapper);
        return new BaseResult(BaseResultEnum.SUCCESS, pageData);
    }

    @ApiOperation(value = "查看收件箱邮件详情", notes = "查看收件箱邮件详情")
    @RequestMapping(value = "select/EmailBoxRecord", method = RequestMethod.GET)
    public BaseResult<BjtUserEmailBoxRecord> selectEmailBoxRecord(@RequestParam("userId") String userId, @RequestParam("recordId") String recordId) {
        LOGGER.info("selectEmailBoxRecord = > 查看收件箱邮件详情");
        BjtUserEmailBoxRecord _bjtUserEmailBoxRecord = userEmailBoxRecordService.selectOne(new EntityWrapper<BjtUserEmailBoxRecord>().eq("record_id", recordId).eq("receiver_id", userId));
        return new BaseResult(BaseResultEnum.SUCCESS, _bjtUserEmailBoxRecord);
    }

}
