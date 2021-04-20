package com.zy.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Author Zhanying
 * @Description
 * @Date 2021/1/21 20:36
 */
public class Test {
    public static void main(String[] args) {
        JSONObject jsonObject = JSON.parseObject("{\"pGroup\":\"性别\",\"value\":{\"女\":{\"begin\":1,\"end\":10},\"男\":{\"begin\":1,\"end\":10}},\"default\":{\"begin\":1,\"end\":10}}");
        System.out.println(jsonObject.toJSONString());
    }
}
