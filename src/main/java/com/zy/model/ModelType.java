package com.zy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhanying
 * @Description
 * @Date 2021/1/21 19:21
 */
public enum ModelType {
    Accumulation("累加id"),RandomInt("随机整数"),RandomTime("随机时间"),RandomListWeight("随机列表权重"),GroupRandomListWeight("分组随机列表权重"),GroupRandomInt("分组随机整数"),Formula("明细公式(待开发)");

    private final String name;
    private ModelType(String name) {
        this.name = name;
    }

    public static String[] getModelNames() {
        ModelType[] modelTypes = ModelType.values();
        List<String> list = new ArrayList<>();
        list.add("--请选择--");
        for(ModelType modelType : modelTypes) {
            list.add(modelType.name);
        }
        return list.toArray(new String[0]);
    }

    public static ModelType getModelType(String name) {
        ModelType[] modelTypes = ModelType.values();
        for(ModelType modelType : modelTypes) {
            if(modelType.name.equals(name)) {
                return modelType;
            }
        }
        return null;
    }

    public static Model getModel(ModelType modelType) {
        if(modelType == null) {
            return null;
        }
        switch (modelType) {
            case Accumulation: {
                return new Accumulation();
            }
            case RandomInt: {
                return new RandomInt();
            }
            case RandomTime: {
                return new RanDomTime();
            }
            case RandomListWeight: {
                return new RandomListWeight();
            }
            case GroupRandomListWeight: {
                return new GroupRandomListWeight();
            }
            case GroupRandomInt: {
                return new GroupRandomInt();
            }
            default: {
                return null;
            }
        }
    }
}
