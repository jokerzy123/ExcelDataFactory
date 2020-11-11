package com.zy.datafunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataFunction {
    //返回long型随机数，传入开始和结束范围;长整型数据
    //Math.random()方法，生成的是一个小于1的数
    public static long random(long begin,long end){
        long rtn = begin + (long)(Math.random() * (end - begin));
        //不包含结束和开始，这里应该不太适用
        /*
        if(rtn == begin || rtn == end){
            return random(begin,end);
        }

         */
        return rtn;
    }

    //列表，权重
    public static < E > E weightChoice(List<E> dataList, List<Integer> weightList){
        List<Integer> list = new ArrayList<>();
        for(int i=0; i<dataList.size(); i++){
            for(int j=0; j<weightList.get(i); j++){
                list.add(i);
            }
        }

        return (E) dataList.get(list.get((int) random(0,list.size())));
    }

    public static void main(String[] args){
        String[] dataList = {"A","B","C"};
        Integer[] weightList = {1,2,1};
        for(int i=0; i<20; i++){
            System.out.println(weightChoice(Arrays.asList(dataList),Arrays.asList(weightList)));
        }
    }
}
