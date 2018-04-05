package com.viewfunction.vfbam.ui.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ApplicationI18nPerportyHandler {
    private static String web_inf_Path=ApplicationI18nPerportyHandler.class.getResource("/").getPath();
    private static String[] supportedLocal=new String[]{"zh-CN"};

    public static Properties loadI18nProperties(String localString){
        Properties i18NProperties=new Properties();
        String i18NResourcePostfix="";
        for(int i=0;i<supportedLocal.length;i++){
            if(supportedLocal[i].equals(localString)){
                i18NResourcePostfix="_"+localString;
            }
        }
        try {
            i18NProperties.load(new FileInputStream(web_inf_Path+"applicationText"+i18NResourcePostfix+".properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return i18NProperties;
    }
}
