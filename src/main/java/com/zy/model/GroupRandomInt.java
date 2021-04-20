package com.zy.model;

import com.alibaba.fastjson.JSONObject;
import com.zy.datafunction.DataFunction;

import java.util.Map;

/**
 * @Author Zhanying
 * @Description
 * @Date 2021/1/21 20:10
 */
public class GroupRandomInt implements Model{
    @Override
    public void Compute(String fieldName, JSONObject para, Map<String, String> map, int row) {
        String pGroup = para.getString("pGroup");
        JSONObject value = para.getJSONObject("value");

        String pValue = map.get(pGroup);//当前依赖字段的值
        if(value.containsKey(pValue)){
            JSONObject jsonObject = value.getJSONObject(pValue);
            map.put(fieldName, String.valueOf(DataFunction.random(jsonObject.getLong("begin"),jsonObject.getLong("end"))));
        } else {
            if(para.containsKey("default")) {
                JSONObject defaultValue = para.getJSONObject("default");
                map.put(fieldName, String.valueOf(DataFunction.random(defaultValue.getLong("begin"),defaultValue.getLong("end"))));
            } else {
                map.put(fieldName, "");
            }
        }
    }

    @Override
    public String defaultPara() {
        return "{\"pGroup\":\"性别\",\"value\":{\"女\":{\"begin\":1,\"end\":10},\"男\":{\"begin\":1,\"end\":10}},\"default\":{\"begin\":1,\"end\":10}}";
    }
}
