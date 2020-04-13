package com.jzm.malltiny01.service.impl;

import com.jzm.common.entity.CommonResult;
import com.jzm.malltiny01.service.RedisService;
import com.jzm.malltiny01.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Random;

@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    @Autowired
    private RedisService redisService;
    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        try {
            Random random = new Random();
            for (int i = 0; i < 6; i++) {
                sb.append(random.nextInt(10));
            }
            //验证码绑定手机号并存储到redis
            redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb.toString(), AUTH_CODE_EXPIRE_SECONDS);
        }catch (Exception e){
            return CommonResult.failed("获取验证码失败");
        }
        return CommonResult.success(sb.toString(), "获取验证码成功");
    }


    //对输入的验证码进行校验
    @Override
    public CommonResult verifyAuthCode(String telephone, String authCode) {
        if (StringUtils.isEmpty(authCode)) {
            return CommonResult.failed("请输入验证码");
        }
        String key = REDIS_KEY_PREFIX_AUTH_CODE + telephone;
        String realAuthCode = redisService.get(key);
        boolean result = authCode.equals(realAuthCode);
        if (result) {
            redisService.delete(key);
            return CommonResult.success(null, "验证码校验成功");
        } else {
            return CommonResult.failed("验证码不正确");
        }
    }

}