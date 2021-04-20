package com.zy.model;

import com.alibaba.fastjson.JSONObject;
import com.zy.datafunction.DateFunction;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @Author Zhanying
 * @Description
 * @Date 2021/1/21 19:44
 */
public class RanDomTime implements Model{
    @Override
    public void Compute(String fieldName, JSONObject para, Map<String, String> map, int row) {
        map.put(fieldName, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(DateFunction.randomDate(para.getString("beginDate"),para.getString("endDate"))));
    }

    @Override
    public String defaultPara() {
        return "{\"beginDate\":\"2020-01-01\",\"endDate\":\"2020-03-01\"}";
    }
}
