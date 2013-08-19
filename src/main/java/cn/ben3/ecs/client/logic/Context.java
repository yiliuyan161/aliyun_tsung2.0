package cn.ben3.ecs.client.logic;

import cn.ben3.ecs.client.Main;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class Context {
    public static Properties conf;
    public static String prop(String key){
        String value=null;
        if(conf==null){
            loadProps();
        }
        value = conf.getProperty(key);

        return value;
    }
    public static void loadProps(){
        conf = new Properties();
        String path=System.getProperty("user.dir");

        try {
            conf.load(new FileInputStream(path+"/config.properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void storeProps(){
        String path=System.getProperty("user.dir");
        try {
            File file = new File(path+"/config.properties");
            System.out.println(path);
            if(!file.exists()){
                file.createNewFile();
            }
            conf.setProperty("hello", "world");
            conf.store(new FileOutputStream(file),"配置文件");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
