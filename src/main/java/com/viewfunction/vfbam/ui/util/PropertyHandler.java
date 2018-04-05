package com.viewfunction.vfbam.ui.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyHandler {

    private static Properties _properties;
    public static String BUILDIN_ADMINISTRATOR_ACCOUNTNAME="BUILDIN_ADMINISTRATOR_ACCOUNTNAME";
    public static String BUILDIN_ADMINISTRATOR_ACCOUNTPWD="BUILDIN_ADMINISTRATOR_ACCOUNTPWD";

    public static String getPropertyValue(String resourceFileName){
        _properties=new Properties();
        try {
            _properties.load(new FileInputStream(RuntimeEnvironmentUtil.getWebInfoDirLocation()+"AdminApplicationCfg.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _properties.getProperty(resourceFileName);
    }
}
