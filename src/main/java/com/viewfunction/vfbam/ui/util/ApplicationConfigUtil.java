package com.viewfunction.vfbam.ui.util;

public class ApplicationConfigUtil {

    public static boolean verifyUserLoginInfo(String userName,String userPWD){
        String adminAccountName=PropertyHandler.getPropertyValue(PropertyHandler.BUILDIN_ADMINISTRATOR_ACCOUNTNAME);
        String adminAccountPWD=PropertyHandler.getPropertyValue(PropertyHandler.BUILDIN_ADMINISTRATOR_ACCOUNTPWD);
        if(adminAccountName==null||adminAccountPWD==null){
            return true;
        }else{
            if(adminAccountName.equals(userName)&&adminAccountPWD.equals(userPWD)){
                return true;
            }else{
                return false;
            }
        }
    }
}
