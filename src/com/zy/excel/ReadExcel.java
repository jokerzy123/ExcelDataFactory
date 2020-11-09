package com.zy.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadExcel {
    public static void excel() throws Exception {
        //用流的方式先读取到你想要的excel的文件
        //FileInputStream fis=new FileInputStream(new File(System.getProperty("user.dir")+"/src/excel.xls"));
        FileInputStream fis = new FileInputStream(new File("C:\\Users\\24469\\Desktop\\BI培训打分问卷_Filter培训20191204_20200727095932.xls"));
        //解析excel
        POIFSFileSystem pSystem=new POIFSFileSystem(fis);
        //获取整个excel
        HSSFWorkbook hb=new HSSFWorkbook(pSystem);
        System.out.println(hb.getNumCellStyles());
        //获取第一个表单sheet
        HSSFSheet sheet=hb.getSheetAt(0);
        //获取第一行
        int firstrow=sheet.getFirstRowNum();
        //获取最后一行
        int lastrow=sheet.getLastRowNum();
        //获取表头
        Row row0=sheet.getRow(firstrow);
        //获取这一行的第一列
        int firstcell0=row0.getFirstCellNum();
        //获取这一行的最后一列
        int lastcell0=row0.getLastCellNum();
        List<String> headerList=new ArrayList<>();
        for (int j = firstcell0; j <lastcell0; j++) {
            //获取第j列
            Cell cell=row0.getCell(j);
            if (cell!=null) {
                System.out.print(cell+"\t");
                headerList.add(cell.toString());
            }
        }

        System.out.println();

        List<User> userList = new ArrayList<>();
        //循环行数依次获取列数
        for (int i = firstrow+1; i < lastrow+1; i++) {
            //获取哪一行i
            Row row=sheet.getRow(i);
            if (row!=null) {
                //获取这一行的第一列
                int firstcell=row.getFirstCellNum();
                //获取这一行的最后一列
                int lastcell=row.getLastCellNum();
                //创建一个集合，用处将每一行的每一列数据都存入集合中
                //List<String> list=new ArrayList<>();
                //String name = row.getCell(0).toString();
                Map<String,String> scoreMap = new HashMap<>();
                for (int j = firstcell; j <lastcell; j++) {
                    //获取第j列
                    Cell cell=row.getCell(j);
                    scoreMap.put(headerList.get(j),cell.toString());
                    /*
                    if (cell!=null) {
                        System.out.print(cell+"\t");
                        list.add(cell.toString());
                    }

                     */
                }
                User user = new User(scoreMap);
                userList.add(user);
                //System.out.println(user.getScore());
            }
        }
        System.out.println(User.getScoreList(userList));
        System.out.println(User.getScoreList1(userList,0.8,0.6));
        fis.close();
    }

    public static void main(String[] args){
        try {
            excel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
