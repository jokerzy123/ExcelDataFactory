package com.zy;

/**
 * @Author Zhanying
 * @Description 字段描述 TODO
 * @Date 2020/11/9 20:47
 * @Version 1.0
 */
public class Field {
    private String filedName;
    private String modelType;
    private String jsonPara;

    public Field() {}

    public Field(String filedName, String modelType, String jsonPara) {
        this.filedName = filedName;
        this.modelType = modelType;
        this.jsonPara = jsonPara;
    }

    public String getJsonPara() {
        return jsonPara;
    }

    public String getFiledName() {
        return filedName;
    }

    public String getModelType() {
        return modelType;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public void setJsonPara(String jsonPara) {
        this.jsonPara = jsonPara;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }
}
