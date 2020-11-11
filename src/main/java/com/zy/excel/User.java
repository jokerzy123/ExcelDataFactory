package com.zy.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private static Map<String,Double> headerMap = new HashMap<>();
    static {
        headerMap.put("培训深度及晦涩性", 0.15);
        headerMap.put("内容完整度", 0.15);
        headerMap.put("能力提升度", 0.15);
        headerMap.put("实际应用度", 0.15);
        headerMap.put("培训氛围", 0.1);
        headerMap.put("培训时长", 0.1);
    }
    private static Map<Integer,Double> score_Map = new HashMap<>();
    static {
        score_Map.put(1, 1.0);
        score_Map.put(2, 0.8);
        score_Map.put(3, 0.6);
        score_Map.put(4, 0.4);
    }
    private String name;
    private Map<String,String> scoreMap;//字段值
    private Map<String,Double> lastscoreMap = new HashMap<>();//最终得分

    public User(Map<String,String> scoreMap_){
        name = scoreMap_.get("姓名");
        scoreMap = scoreMap_;
        getLastScore();//转换最终得分
    }

    private void getLastScore(){
        for(Map.Entry<String,Double> entry:headerMap.entrySet()){
            String key = entry.getKey();
            Double weight = entry.getValue();
            Double score = tranScore(scoreMap.get(key));
            lastscoreMap.put(key, score);
        }
    }

    public static Double getScoreList1(List<User> userList,Double score0,Double score1){
        Double lastScore = 0.0;
        int num = userList.size();
        for(Map.Entry<String,Double> entry:headerMap.entrySet()){
            Double a = 0.0;
            for(User user:userList){
                a += user.lastscoreMap.get(entry.getKey());
            }
            a = a*entry.getValue()/num;
            lastScore += a;

        }
        lastScore += score0*0.15+score1*0.05;
        return lastScore;
    }

    public static String getScoreList(List<User> userList){
        String s = "";
        int num = userList.size();
        for(Map.Entry<String,Double> entry:headerMap.entrySet()){
            Double a = 0.0;
            for(User user:userList){
                a += user.lastscoreMap.get(entry.getKey());
            }
            a = a/num;
            s += entry.getKey()+":"+a.toString()+"\t"+(a*entry.getValue())+"\n";
        }
        return s;
    }

    public String toString(){
        for(Map.Entry<String,Double> entry:headerMap.entrySet()){
            String key = entry.getKey();
            Double weight = entry.getValue();
            Double score = tranScore(scoreMap.get(key))*weight;
        }
        return name;
    }

    private Double tranScore(String s){
        //获取第一个数字
        String score = s.substring(0,1);
        return score_Map.get(Integer.valueOf(score));
    }

    public static void main(String[] args){
        String s = "1、asfdasf";
        System.out.println(s.substring(0,1));
    }
}
