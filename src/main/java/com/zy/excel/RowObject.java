package com.zy.excel;

import java.util.List;
import java.util.Map;

public class RowObject {
    private static int id_sum;//1.java中int的取值范围为-2147483648到+-2147483648。用来动态分配私有id;初始化默认是0
    private int id;//每一行会自动分配一个id
    //每一行各个字段的属性怎么加，因为字段是动态，map添加吧,感觉要用到泛型;统一用文本类型写入吧，这样比较简单，反正是excel,导入BI也是会自动识别类型的，不影响
    private Map<String,String> propertyMap;

    public RowObject(Map<String,String> propertyMap_){
        propertyMap = propertyMap_;//这里应该不用拷贝，原来的map用完之后，下一次循环又会重新引用新的对象
        id = id_sum;
        id_sum++;
    }

    public Map<String, String> getPropertMap() {
        return propertyMap;
    }

    //重置主键关联字段
    public void uniqueReset(List<RowObject> rowObjectList,String primaryField,String[] relatedFields){
        for(RowObject rowObject:rowObjectList){
            if(propertyMap.get(primaryField).equals(rowObject.propertyMap.get(primaryField))){//如果主键value相等，则重置相关的关联字段
                for(String relatedField:relatedFields){
                    propertyMap.put(relatedField,rowObject.propertyMap.get(relatedField));
                }
                return;
            }
        }
    }

    public String toRow(List<String> fieldNameList){
        String[] slist = new String[propertyMap.size()];
        //遍历map,这里顺序有点问题，传入一个列名列表
        for(int i=0; i<fieldNameList.size(); i++){
            slist[i] = propertyMap.get(fieldNameList.get(i));
        }
        /*
        for(Map.Entry<String,String> entry:propertyMap.entrySet()){
            slist[i] = entry.getValue();
            i++;
        }
         */
        return String.join(",",slist);
    }
}
