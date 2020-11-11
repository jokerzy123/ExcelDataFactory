package com.zy.main;

import com.zy.excel.GenerateExcel;
import com.zy.excel.RowObject;
import com.zy.datafunction.*;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args){
        //map的内容放到主函数里面设计
        //设置字段列表，即表头
        String[] list = {"边防检查站", "姓名", "身份证号", "性别", "国籍", "交通工具", "出入境", "日期", "是否携带违规品", "违规品类型"};
        int rowNum = 100;//设置表的行数
        //设置表
        //List<String> fieldNameList = Arrays.asList(list);//这种方式不能增删，只能查改，所以要增删的时候不要用
        List<String> fieldNameList = Arrays.stream(list).collect(Collectors.toList());//这种方式就可以增删了
        System.out.println(fieldNameList);

        List<RowObject> rowObjectList = new ArrayList<>();//行元素列表
        for(int i=0; i<rowNum; i++){
            Map<String,String> map = new HashMap<>();
            map.put("国籍",DataFunction.weightChoice(Arrays.asList(new String[]{"中国公民", "外国公民", "外国边民"}), Arrays.asList(new Integer[] {5,4,1})));
            map.put("B","4612784678");
            map.put("C","2457823678");
            map.put("D","2123");
            //添加完属性后，传给RowObject对象
            RowObject rowObject = new RowObject(map);
            //System.out.println(rowObject.getPropertMap().get("A"));
            rowObjectList.add(rowObject);
        }

        GenerateExcel.createExcel("D:\\test.xlsx", fieldNameList, rowObjectList);

        /*
        map = new HashMap<>();
        map.put("A","127849327");
        map.put("B","4612784678");
        map.put("C","2457823678");
        map.put("D","412678461");
        System.out.println(rowObject.getPropertMap().get("D"));//这样就会找不到了。ok，没啥问题。
        Integer a = 3;
        Integer b = a;
        a = 4;
        System.out.println(b);//b 的值并没有改变，这里需要区别一下，对象引用

         */
    }
}
