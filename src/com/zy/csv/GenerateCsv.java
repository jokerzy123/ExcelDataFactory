package com.zy.csv;

import com.zy.excel.RowObject;


import java.io.*;
import java.util.List;

public class GenerateCsv {
    public static void main(String[] args){
        //测试下数据生成
    }
    public static void createCsv(String filePath, List<String> fieldNameList, List<RowObject> rowObjectList){
        String[] headArr = fieldNameList.toArray(new String[fieldNameList.size()]);
        File csvFile = null;
        BufferedWriter csvWriter = null;
        try{
            csvFile = new File(filePath);
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();
            // GB2312使正确读取分隔符",";这里BufferedWriter的用法值得学习一下
            csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 1024);
            csvWriter.write(String.join(",", headArr));
            csvWriter.newLine();
            for(RowObject rowObject:rowObjectList){
                csvWriter.write(rowObject.toRow(fieldNameList));
                csvWriter.newLine();
            }
            csvWriter.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
