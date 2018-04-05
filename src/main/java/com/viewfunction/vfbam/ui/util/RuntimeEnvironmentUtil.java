package com.viewfunction.vfbam.ui.util;

public class RuntimeEnvironmentUtil {
    public static final String BINART_TEMP_FILE_DIR = "/BINARY_TEMP_FILES";
    public static final String WEB_INFO_DIR = "/WEB-INF";

    public static String getBinaryTempFileDirLocation() {
        return getApplicationRootPath() + BINART_TEMP_FILE_DIR + "/";
    }

    public static String getWebInfoDirLocation() {
        return getApplicationRootPath() + WEB_INFO_DIR + "/";
    }

    public static String getApplicationRootPath() {
        if (System.getProperty("catalina.home") != null) {
            // running under tomcat container, set application root location to /WEB-INF
            StringBuilder sb = new StringBuilder(RuntimeEnvironmentUtil.class.getResource("").toString());
            String OSType = System.getProperty("os.name");
            if (OSType.startsWith("Windows")) {
                sb.delete(0, 6); //this only work on windows system
            } else {
                sb.delete(0, 5);//this only work on Unix system
            }
            sb.reverse().delete(0, 48).reverse();
            return sb.toString().replaceAll("%20", " ");
        } else {
            //running under common standalone mode,set application root location to ./
            return "";
        }
    }
}
