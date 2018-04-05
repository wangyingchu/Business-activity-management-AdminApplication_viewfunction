package com.viewfunction.vfbam.ui;

import com.github.wolfie.blackboard.Blackboard;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.tests.themes.valo.ValoThemeSessionInitListener;
import com.vaadin.ui.UI;

import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.ApplicationBanner;
import com.viewfunction.vfbam.ui.component.ApplicationContent;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentModifyEvent;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentSelectedEvent;
import com.viewfunction.vfbam.ui.component.activityManagement.NewActivitySpaceCreatedEvent;
import com.viewfunction.vfbam.ui.component.login.ApplicationLoginForm;
import com.viewfunction.vfbam.ui.util.ApplicationI18nPerportyHandler;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import javax.servlet.ServletException;
import java.util.Properties;

//@Theme("tests-valo")
//@Theme("tests-valo-metro")
@Theme("viewfunction")
@Title("ViewFunction Business Activity Management Platform Administration")
@PreserveOnRefresh
public class VFBPM_AdminApplicationUI  extends UI {

    private String loginUserId;

    @VaadinServletConfiguration(productionMode = false, ui = VFBPM_AdminApplicationUI.class)
    public static class Servlet extends VaadinServlet {

        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();
            getService().addSessionInitListener(new ValoThemeSessionInitListener());
        }
    }
    @Override
    protected void init(final VaadinRequest request) {
        final WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        Properties i18NProperties= ApplicationI18nPerportyHandler.loadI18nProperties(webBrowser.getLocale().toLanguageTag());
        UserClientInfo currentUserClientInfo=new UserClientInfo();
        currentUserClientInfo.setUserI18NProperties(i18NProperties);
        currentUserClientInfo.setUserWebBrowserInfo(webBrowser);

        Blackboard BLACKBOARD = new Blackboard();
        BLACKBOARD.enableLogging();
        BLACKBOARD.register(ActivitySpaceComponentSelectedEvent.ActivitySpaceComponentSelectedListener.class,
                ActivitySpaceComponentSelectedEvent.class);
        BLACKBOARD.register(ActivitySpaceComponentModifyEvent.ActivitySpaceComponentModifiedListener.class,
                ActivitySpaceComponentModifyEvent.class);
        BLACKBOARD.register(NewActivitySpaceCreatedEvent.NewActivitySpaceCreatedListener.class,
                NewActivitySpaceCreatedEvent.class);
        currentUserClientInfo.setEventBlackBoard(BLACKBOARD);

        if (browserCantRenderFontsConsistently()) {
            getPage().getStyles().add(".v-app.v-app.v-app {font-family: Sans-Serif;}");
        }
        /*
        if (getPage().getWebBrowser().isIE()
                && getPage().getWebBrowser().getBrowserMajorVersion() == 9) {
            menu.setWidth("320px");
        }
        */
        Responsive.makeResponsive(this);
        getPage().setTitle(currentUserClientInfo.getUserI18NProperties().getProperty("Global_Application_Desc"));

        renderLoginUI(currentUserClientInfo);
    }

    public void renderLoginUI(UserClientInfo userClientInfo){
        ApplicationLoginForm loginForm=new ApplicationLoginForm(userClientInfo);
        loginForm.setContainerApplicationUI(this);
        setContent(loginForm);
    }

    public void renderOperationUI(UserClientInfo userClientInfo){
        VerticalLayout rootLayout = new VerticalLayout();
        // sure it's 100% sized, and remove unwanted margins
        rootLayout.setSizeFull();
        rootLayout.setMargin(false);

        ApplicationBanner applicationBanner=new ApplicationBanner(userClientInfo);
        applicationBanner.setContainerApplicationUI(this);
        rootLayout.addComponent(applicationBanner);

        ApplicationContent applicationContent=new ApplicationContent(userClientInfo);
        rootLayout.addComponent(applicationContent);
        rootLayout.setExpandRatio(applicationContent, 1.0F);
        setContent(rootLayout);
    }

    private boolean browserCantRenderFontsConsistently() {
        // PhantomJS renders font correctly about 50% of the time, so disable it to have consistent screenshots
        // https://github.com/ariya/phantomjs/issues/10592
        // IE8 also has randomness in its font rendering...
        return getPage().getWebBrowser().getBrowserApplication().contains("PhantomJS")|| (getPage().getWebBrowser()
                .isIE() && getPage().getWebBrowser().getBrowserMajorVersion() <= 9);
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }
}
