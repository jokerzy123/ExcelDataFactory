package com.zy.excel;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class GenerateExcel {
    //工具类
    public static void main(String[] args){

    }

    //传入RowObject列表，这里方法名开头应该小写
    // TODO 换成SXSSFWorkbook
    public static void createExcel(String filePath, List<String> fieldNameList,List<RowObject> rowObjectList){
        try{
            //创建SXSSFWorkbook对象
            SXSSFWorkbook wb = new SXSSFWorkbook();
            //创建SXSSFSheet对象
            SXSSFSheet sheet = wb.createSheet("sheet0");
            //创建SXSSFRow对象，先创建一个行对象（传入行标），然后在行对象里面创建一个单元格对象，根据列标指定单元格
            int rownum = 0;
            SXSSFRow row = sheet.createRow(rownum);
            //表头写入excel
            for(int i=0; i<fieldNameList.size(); i++){
                //创建SXSSFCell对象
                SXSSFCell cell=row.createCell(i);
                cell.setCellValue(fieldNameList.get(i));
            }

            //遍历List<RowObject>
            for(RowObject rowObject : rowObjectList){
                rownum ++;
                //获取行元素的map
                row = sheet.createRow(rownum);
                Map<String,String> propertyMap = rowObject.getPropertMap();
                for(int i=0; i<fieldNameList.size(); i++){
                    SXSSFCell cell=row.createCell(i);
                    cell.setCellValue(propertyMap.get(fieldNameList.get(i)));
                }
            }
            //输出Excel文件
            FileOutputStream output=new FileOutputStream(filePath);
            wb.write(output);
            output.flush();

            //最好是最后调用一下流的关闭方法，虽然JAVA虚拟机的回收机制会自动关闭
            wb.close();
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
