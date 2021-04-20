package com.zy.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Iterator;

/**
 * @Author Zhanying
 * @Description
 * 表格转繁体测试通过
 * @Date 2021/2/24 18:33
 */
public class FileUtils {
    /**
     * 表格文件，转繁体
     * 存在问题：内存里应该是只存了100条数据
     * @param wb
     */
    public static SXSSFWorkbook transWorkbook(SXSSFWorkbook wb) throws IOException {
        long startTime = System.currentTimeMillis();
        //LogUtils.info("trans workbook start ");

        //InputStream in = workbookConvertorStream(wb);
        //SXSSFWorkbook wb1 = wb;//new SXSSFWorkbook(new XSSFWorkbook(in));
        SXSSFWorkbook wb1 = wb;

        Iterator<Sheet> sheetIterator = wb1.sheetIterator();
        int i = 0;
        while(sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            transSheet(sheet);
            //sheet名称修改
            String name = sheet.getSheetName();
            String name_ = "新sheet";//ZhConverterUtil.convertToTraditional(name);
            //System.out.println("sheet标题" + name + "->" + name_);
            wb1.setSheetName(i, name_);
            i++;
        }

        long consume = System.currentTimeMillis() - startTime;
        //LogUtils.info("trans workbook end cost: " + consume);

        return wb1;
    }

    public static XSSFWorkbook transWorkbook(XSSFWorkbook wb) throws IOException {
        long startTime = System.currentTimeMillis();
        //LogUtils.info("trans workbook start ");

        //InputStream in = workbookConvertorStream(wb);
        //SXSSFWorkbook wb1 = wb;//new SXSSFWorkbook(new XSSFWorkbook(in));
        XSSFWorkbook wb1 = wb;

        Iterator<Sheet> sheetIterator = wb1.sheetIterator();
        int i = 0;
        while(sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            transSheet(sheet);
            //sheet名称修改
            String name = sheet.getSheetName();
            String name_ = "新sheet值";//ZhConverterUtil.convertToTraditional(name);
            //System.out.println("sheet标题" + name + "->" + name_);
            wb1.setSheetName(i, name_);
            i++;
        }

        long consume = System.currentTimeMillis() - startTime;
        //LogUtils.info("trans workbook end cost: " + consume);

        return wb1;
    }

    /**
     * 处理sheet
     * @param sheet
     */
    private static void transSheet(Sheet sheet){

        for (Row row : sheet) {
            for (Cell cell : row) {
                String s = null;
                try {
                    s = cell.getStringCellValue();
                } catch (Exception ignore) {

                }
                if (s != null) {
                    String s_ = "新单元格值";//ZhConverterUtil.convertToTraditional(s);
                    cell.setCellValue(s_);
                    System.out.println("单元格" + s + "->" + s_);
                }
            }
        }
    }

    private static void writeWbToFile(SXSSFWorkbook wb, String filePath) {
        FileOutputStream output = null;
        try {
            output=new FileOutputStream(filePath);
            wb.write(output);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(output != null) {
                try {
                    output.flush();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void writeWbToFile(Workbook wb, String filePath) {
        FileOutputStream output = null;
        try {
            output=new FileOutputStream(filePath);
            wb.write(output);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(output != null) {
                try {
                    output.flush();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * SXSSFWorkbook转InputStream
     * @param workbook
     * @return
     */
    public static InputStream workbookConvertorStream(SXSSFWorkbook workbook) {
        InputStream in = null;
        try{
            //临时缓冲区
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //创建临时文件
            workbook.write(out);
            byte [] bookByteAry = out.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return in;
    }

    public static void main(String[] args) throws IOException {
        /*
        String original = "生命不息，奋斗不止";
        String result = ZhConverterUtil.convertToTraditional(original);
        System.out.println(result);

         */

        File file = new File("D:\\二开学习\\ExcelDataFactory\\exe4j\\繁体化测试1.xlsx");
        FileInputStream inputStream =  new FileInputStream(file);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
        inputStream.close();
        //SXSSFWorkbook workbook = new SXSSFWorkbook();
        //SXSSFWorkbook workbook1 = transWorkbook(workbook);
        writeWbToFile(transWorkbook(new SXSSFWorkbook(xssfWorkbook)), "D:\\二开学习\\ExcelDataFactory\\exe4j\\繁体化测试1_new.xlsx");

    }
}
