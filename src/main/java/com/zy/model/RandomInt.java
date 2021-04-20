package com.zy.model;

import com.alibaba.fastjson.JSONObject;
import com.zy.datafunction.DataFunction;

import java.util.Map;

/**
 * @Author Zhanying
 * @Description
 * @Date 2021/1/21 19:37
 */
public class RandomInt implements Model{
    @Override
    public void Compute(String fieldName, JSONObject para, Map<String, String> map, int row) {
        map.put(fieldName, String.valueOf(DataFunction.random(para.getLong("begin"),para.getLong("end"))));
    }

    @Override
    public String defaultPara() {
        return "{\"begin\":0,\"end\":100}";
    }
}
