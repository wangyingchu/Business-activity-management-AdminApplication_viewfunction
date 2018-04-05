package com.viewfunction.vfbam.ui.util;

import com.github.wolfie.blackboard.Blackboard;
import com.vaadin.server.WebBrowser;

import java.io.Serializable;
import java.util.Properties;

public class UserClientInfo implements Serializable {
    private static final long serialVersionUID = 3930971222892866428L;
    private Properties userI18NProperties;
    private WebBrowser userWebBrowserInfo;
    private Blackboard eventBlackBoard;
    private ActivitySpaceManagementMeteInfo activitySpaceManagementMeteInfo;
    /*
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(webBrowser.getAddress());
        System.out.println(webBrowser.getBrowserApplication());
        System.out.println(webBrowser.getBrowserMajorVersion());
        System.out.println(webBrowser.getBrowserMinorVersion());
        System.out.println(webBrowser.getCurrentDate());
        System.out.println(webBrowser.getDSTSavings());
        System.out.println(webBrowser.getRawTimezoneOffset());
        System.out.println(webBrowser.getScreenHeight());
        System.out.println(webBrowser.getScreenWidth());
        System.out.println(webBrowser.getTimezoneOffset());
        System.out.println(webBrowser.getLocale().getCountry());
        System.out.println(webBrowser.getLocale().getDisplayCountry());
        System.out.println(webBrowser.getLocale().getDisplayLanguage());
        System.out.println(webBrowser.getLocale().getDisplayName());
        System.out.println(webBrowser.getLocale().getDisplayVariant());
        System.out.println(webBrowser.getLocale().getISO3Country());
        System.out.println(webBrowser.getLocale().getISO3Language());
        System.out.println(webBrowser.getLocale().getLanguage());
        System.out.println(webBrowser.getLocale().getVariant());
        System.out.println(webBrowser.getLocale().getDisplayScript());
        System.out.println(webBrowser.getLocale().toLanguageTag());
        System.out.println(webBrowser.getLocale().getLanguage());
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
     */

    public Properties getUserI18NProperties() {
        return userI18NProperties;
    }

    public void setUserI18NProperties(Properties userI18NProperties) {
        this.userI18NProperties = userI18NProperties;
    }

    public WebBrowser getUserWebBrowserInfo() {
        return userWebBrowserInfo;
    }

    public void setUserWebBrowserInfo(WebBrowser userWebBrowserInfo) {
        this.userWebBrowserInfo = userWebBrowserInfo;
    }

    public Blackboard getEventBlackBoard() {
        return eventBlackBoard;
    }

    public void setEventBlackBoard(Blackboard eventBlackBoard) {
        this.eventBlackBoard = eventBlackBoard;
    }

    public ActivitySpaceManagementMeteInfo getActivitySpaceManagementMeteInfo() {
        return activitySpaceManagementMeteInfo;
    }

    public void setActivitySpaceManagementMeteInfo(ActivitySpaceManagementMeteInfo activitySpaceManagementMeteInfo) {
        this.activitySpaceManagementMeteInfo = activitySpaceManagementMeteInfo;
    }
}
