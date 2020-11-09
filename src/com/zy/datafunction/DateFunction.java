package com.zy.datafunction;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFunction {
    //日期函数处理
    public static void main(String[] args) {

        for (int i=0;i<30;i++){
            Date date = randomDate("2019-01-01","2019-01-31");
            //SimpleDateFormat.format把时间转换成字符串
            System.out.println(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));
        }
    }

    //工具类，实现各种数据模型生成，数据转换的工作
    //开始日期到结束日期之间，返回随机时间
    public static Date randomDate(String beginDate, String endDate){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);//SimpleDateFormat.parse把字符串转换成时间
            Date end = format.parse(endDate);

            if(start.getTime() >= end.getTime()){
                return null;
            }
            long date = random(start.getTime(),end.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long random(long begin,long end){
        long rtn = begin + (long)(Math.random() * (end - begin));
        if(rtn == begin || rtn == end){
            return random(begin,end);
        }
        return rtn;
    }
}
