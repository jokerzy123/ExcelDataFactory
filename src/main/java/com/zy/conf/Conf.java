package com.zy.conf;

/**
 * @Author Zhanying
 * @Description TODO
 * @Date 2020/11/9 20:15
 * @Version 1.0
 */
public class Conf {
    private String uniquePara;//联合关联主键配置
    private String jsonPara;
    private String tables;//快捷保存

    public Conf() {}

    public String getJsonPara() {
        return jsonPara;
    }

    public void setJsonPara(String jsonPara) {
        this.jsonPara = jsonPara;
    }

    public String getTables() {
        return tables;
    }

    public void setTables(String tables) {
        this.tables = tables;
    }

    public String getUniquePara() {
        return uniquePara;
    }

    public void setUniquePara(String uniquePara) {
        this.uniquePara = uniquePara;
    }
}
