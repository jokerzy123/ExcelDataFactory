package com.zy.model;

import com.alibaba.fastjson.JSONObject;
import com.zy.datafunction.DataFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author Zhanying
 * @Description
 * @Date 2021/1/21 19:48
 */
public class RandomListWeight implements Model{
    @Override
    public void Compute(String fieldName, JSONObject para, Map<String, String> map, int row) {
        List<String> dataList = new ArrayList<>();
        List<Integer> weightList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : para.entrySet()) {
            dataList.add(entry.getKey());
            weightList.add((Integer) entry.getValue());
            //System.out.println("key值="+entry.getKey());
            //System.out.println("对应key值的value="+entry.getValue());
        }
        map.put(fieldName, DataFunction.weightChoice(dataList,weightList));
    }

    @Override
    public String defaultPara() {
        return "{\"男\":2,\"女\":1}";
    }
}
