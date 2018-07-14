package com.j4sc.bjt.user.common;

import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/3/30 0030 上午 11:46
 * @Version: 1.0
 **/
public class UserNameValidation {

    /**
     * 验证用户名，支持中英文（包括全角字符）、数字、下划线和减号 （全角及汉字算两位）,长度为4-20位,中文按二位计数
     *
     * @param userName
     * @return
     */

    public static boolean validateUserName(String userName) {
        Pattern p = Pattern.compile("[a-zA-Z]{1}[a-zA-Z0-9_]{1,15}");
        Matcher m = p.matcher(userName);
        System.out.println(m.matches());
        return m.matches();
    }

    public static void main(String[] args){
        System.out.println(validateUserName("fdsafd张广宁"));
    }
}