package com.zy.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @Author Zhanying
 * @Description
 * @Date 2021/1/21 18:49
 */
public class Accumulation implements Model{
    @Override
    public void Compute(String fieldName, JSONObject para, Map<String, String> map, int row) {
        map.put(fieldName, String.valueOf(para.getLong("begin")+row));
    }

    @Override
    public String defaultPara() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("begin", 0);
        return jsonObject.toJSONString();
    }
}
