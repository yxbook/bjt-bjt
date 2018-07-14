package com.j4sc.bjt.api.common.constant;

/**
 * @Description: 身份证校验
 * @Author: chengyz
 * @CreateDate: 2018/5/2 10:56
 * @Version: 1.0
 **/

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class IdcardValidation {
    public static final String APP_ID = "11322502";
    public static final String API_KEY = "Qlv5lR1dpzZtv7txE2xLoCBW";
    public static final String SECRET_KEY = "B8gg5bZiZ2mQSscomIOSIYyMd1nP5TEY";

    @Value("${imagePath}")
    private String path;

    public static boolean validation(String idCardSide, String filePath) {
        Long beginTime = System.currentTimeMillis();
        System.out.println(beginTime);
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");
        // 参数为本地图片路径
        JSONObject res = client.idcard(filePath, idCardSide, options);
        Map<String, Object> resultMap = res.toMap();
        Long endTime = System.currentTimeMillis();
        System.out.println("百度校验时长"+(endTime-beginTime));
        if ("normal".equals(resultMap.get("image_status"))) {
            return true;
        } else {
            return false;
        }
    }

}
