package com.zy.conf;

import java.io.File;

/**
 * @Author Zhanying
 * @Description 记忆上次打开软件的最后一次输入 TODO
 * @Date 2020/11/3 19:28
 * @Version 1.0
 */
public class ConfManager {
    private static final String proPath = "/resources/config.properties";
    private static volatile ConfManager instance;

    private ConfManager() {
        init();
    }

    public static ConfManager getInstance() {
        if (instance == null) {
            Class var0 = ConfManager.class;
            synchronized(ConfManager.class) {
                if (instance == null) {
                    instance = new ConfManager();
                }
            }
        }

        return instance;
    }

    private void init() {
        //判断一下配置文件，如果不存在则新建
        File file = new File(proPath);
        if(!file.exists()){
            try{
                setConf();
            }catch (Exception e) {

            }
        }
    }

    public void setConf(){

    }

}
