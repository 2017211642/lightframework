package org.framework.util;

import com.alibaba.fastjson.JSONObject;
import org.framework.bean.ResponeBody;

public class ResponeUtil {
    public static String toJSONString(ResponeBody responeBody){
        return JSONObject.toJSONString(responeBody);
    }
}
