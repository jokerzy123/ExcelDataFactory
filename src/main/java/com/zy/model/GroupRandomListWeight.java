package com.zy.model;

import com.alibaba.fastjson.JSONObject;
import com.zy.datafunction.DataFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author Zhanying
 * @Description
 * @Date 2021/1/21 19:49
 */
public class GroupRandomListWeight implements Model{
    @Override
    public void Compute(String fieldName, JSONObject para, Map<String, String> map, int row) {
        String pGroup = para.getString("pGroup");
        JSONObject value = para.getJSONObject("value");

        String pValue = map.get(pGroup);//当前依赖字段的值
        if(value.containsKey(pValue)){
            JSONObject jsonObject = value.getJSONObject(pValue);
            List<String> dataList = new ArrayList<>();
            List<Integer> weightList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                dataList.add(entry.getKey());
                weightList.add((Integer) entry.getValue());
                //System.out.println("key值="+entry.getKey());
                //System.out.println("对应key值的value="+entry.getValue());
            }
            map.put(fieldName, DataFunction.weightChoice(dataList,weightList));
        } else {
            if(para.containsKey("default")) {
                JSONObject defaultValue = para.getJSONObject("default");
                List<String> dataList = new ArrayList<>();
                List<Integer> weightList = new ArrayList<>();
                for (Map.Entry<String, Object> entry : defaultValue.entrySet()) {
                    dataList.add(entry.getKey());
                    weightList.add((Integer) entry.getValue());
                    //System.out.println("key值="+entry.getKey());
                    //System.out.println("对应key值的value="+entry.getValue());
                }
                map.put(fieldName, DataFunction.weightChoice(dataList,weightList));
            } else {
                map.put(fieldName, "");
            }
        }
    }

    @Override
    public String defaultPara() {
        return "{\"pGroup\":\"性别\",\"value\":{\"女\":{\"y1\":3,\"x1\":1},\"男\":{\"x\":1,\"y\":2}},\"default\":{\"x2\":1,\"y2\":2}}";
    }
}
