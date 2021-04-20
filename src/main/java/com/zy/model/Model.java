package com.zy.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @Author Zhanying
 * @Description TODO
 * @Date 2020/11/9 21:15
 * @Version 1.0
 */
public interface Model {
    //计算属性

    /**
     * 单行计算
     * @param fieldName
     * @param para
     * @param map
     * @param row
     */
    void Compute(String fieldName, JSONObject para, Map<String, String> map, int row);

    String defaultPara();
}
