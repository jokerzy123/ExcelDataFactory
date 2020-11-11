package com.zy;

/**
 * @Author Zhanying
 * @Description 字段描述 TODO
 * @Date 2020/11/9 20:47
 * @Version 1.0
 */
public class Field {
    private String fieldName;
    private String modelType;
    private String jsonPara;

    public Field() {}

    public Field(String filedName, String modelType, String jsonPara) {
        this.fieldName = filedName;
        this.modelType = modelType;
        this.jsonPara = jsonPara;
    }

    public String getJsonPara() {
        return jsonPara;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getModelType() {
        return modelType;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setJsonPara(String jsonPara) {
        this.jsonPara = jsonPara;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }
}
