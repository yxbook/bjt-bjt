package com.j4sc.bjt.system.server.rest.service.impl;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.bjt.system.rest.api.SystemApiSerivece;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.util.RandomUtil;
import com.j4sc.common.validator.LengthValidator;
import com.j4sc.common.validator.NotNullValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.j4sc.bjt.system.server.config.RabbitConfig.EMAIL_QUEUE;
import static com.j4sc.bjt.system.server.config.RabbitConfig.MESSAGE_QUEUE;
import static com.j4sc.bjt.system.server.config.RabbitConfig.PUSH_QUEUE;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/30 17:10
 * @Version: 1.0
 **/
@Service
@Transactional
@BaseService
@RequestMapping("api")
@RestController
@Api(tags = {"帮家团系统服务"}, description = "帮家团系统服务管理")
public class SystemApiSeriveceImpl implements SystemApiSerivece {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemApiSeriveceImpl.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation(value = "发送Email", notes = "发送Email")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
    })
    @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    @Override
    public BaseResult createAuthenticationToken(String username) {
        LOGGER.info(" 发送Email请求...");
        rabbitTemplate.convertAndSend(EMAIL_QUEUE, username);
        return new BaseResult(BaseResultEnum.SUCCESS, "");
    }

    @ApiOperation(value = "发送注册验证码短信", notes = "发送注册验证码短信")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tel", value = "电话", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @RequestMapping(value = "sendRegisterSMS", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public BaseResult sendRegisterSMS(@RequestBody Map<String, Object> params){
        LOGGER.info(" 发送短信...");
        //信息校验
        ComplexResult result = FluentValidator.checkAll()
                .on((String) params.get("tel"), new LengthValidator(11, 11, "电话"))
                .on((String) params.get("code"), new LengthValidator(6, 10, "验证码"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, result.getErrors().get(0).getErrorMsg());
        }
        params.put("templateid", "225285090");
        rabbitTemplate.convertAndSend(MESSAGE_QUEUE, params);
        return new BaseResult(BaseResultEnum.SUCCESS, "");
    }

    @ApiOperation(value = "生成Code", notes = "生成Code")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "对应用户", required = true, dataType = "String"),
            @ApiImplicitParam(name = "codeType", value = "验证码类型", required = false, dataType = "String"),
    })
    @Override
    public BaseResult generateCode(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "codeType", required = false) String codeType) {
        String code = RandomUtil.generateNumber(6);
        //5分钟时长
        redisTemplate.opsForValue().set(username + codeType, code, 60 * 5, TimeUnit.SECONDS);
        return new BaseResult(BaseResultEnum.SUCCESS, code);
    }

    @ApiOperation(value = "获取Code", notes = "获取Code")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "对应用户", required = true, dataType = "String"),
            @ApiImplicitParam(name = "codeType", value = "验证码类型", required = false, dataType = "String"),
    })
    @Override
    public BaseResult getCode(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "codeType", required = false) String codeType) {
        if (redisTemplate.hasKey(username + codeType)) {
            //取出验证码
            String code = redisTemplate.opsForValue().get(username + codeType);
            return new BaseResult(BaseResultEnum.SUCCESS, code);
        } else {
            return new BaseResult(BaseResultEnum.ERROR, "验证码不存在");
        }
    }

    @ApiOperation(value = "移除Code", notes = "移除Code")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "对应用户", required = true, dataType = "String"),
            @ApiImplicitParam(name = "codeType", value = "验证码类型", required = false, dataType = "String"),
    })
    @Override
    public BaseResult removeCode(@RequestParam(value = "username", required = true) String username,
                                 @RequestParam(value = "codeType", required = false) String codeType) {
        if (redisTemplate.hasKey(username + codeType)) {
            //移除验证码
            redisTemplate.delete(username + codeType);
            return new BaseResult(BaseResultEnum.SUCCESS, "移除验证码成功");
        } else {
            return new BaseResult(BaseResultEnum.SUCCESS, "移除验证码成功");
        }
    }
    @ApiOperation(value = "消息推送", notes = "消息推送")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", required = true, dataType = "String"),
            @ApiImplicitParam(name = "body", value = "消息体", dataType = "String"),
            @ApiImplicitParam(name = "phoneList", value = "电话列表", dataType = "String"),
            @ApiImplicitParam(name = "target", value = "目标操作", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "消息编号", dataType = "String"),
    })
    @RequestMapping(value = "sendToPushQueue", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public BaseResult sendToPushQueue(@RequestBody Map<String, Object> params) {
        rabbitTemplate.convertAndSend(PUSH_QUEUE, params);
        return new BaseResult(BaseResultEnum.SUCCESS, "");
    }

    @ApiOperation(value = "发送短信")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tel", value = "电话", required = true, dataType = "String"),
            @ApiImplicitParam(name = "operate", value = "操作为1表示同意，2表示拒绝", required = true, dataType = "String")
    })
    @RequestMapping(value = "sendMessage", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult sendMessage(@RequestBody Map<String, Object> params){
        LOGGER.info(" 发送短信...");
        //信息校验
        ComplexResult result = FluentValidator.checkAll()
                .on((String) params.get("tel"), new LengthValidator(11, 11, "电话"))
                .on((String) params.get("operate"), new NotNullValidator("操作类型"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, result.getErrors().get(0).getErrorMsg());
        }
        if ("1".equals(params.get("operate").toString())) {
            params.put("templateid", "279632942");//同意的模板编号
        }
        if ("2".equals(params.get("operate").toString())) {
            params.put("templateid", "279639021");//拒绝的模板编号
        }
        rabbitTemplate.convertAndSend(MESSAGE_QUEUE, params);
        return new BaseResult(BaseResultEnum.SUCCESS, "");
    }


}
