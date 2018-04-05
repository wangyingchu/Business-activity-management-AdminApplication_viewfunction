package com.viewfunction.vfbam.ui.component.login;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.viewfunction.vfbam.ui.util.ApplicationConfigUtil;
import com.viewfunction.vfbam.ui.util.UserClientInfo;
import com.viewfunction.vfbam.ui.VFBPM_AdminApplicationUI;

import java.util.Properties;

/**
 * Created by wangychu on 1/14/17.
 */
public class ApplicationLoginForm extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    public static final ThemeResource Login_picture_cn = new ThemeResource("../viewfunction/imgs/loginPic_cn.jpg");
    public static final ThemeResource Login_picture = new ThemeResource("../viewfunction/imgs/loginPic.jpg");
    public static final ThemeResource viewfunctionLogo_picture = new ThemeResource("../viewfunction/imgs/vflogo.png");
    private TextField adminUserNameField;
    private PasswordField passwordField;
    private VFBPM_AdminApplicationUI containerApplicationUI;

    public ApplicationLoginForm(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo = currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        String languageTag=webBrowser.getLocale().toLanguageTag();

        int screenHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        int headerSpaceHeight=(int)(screenHeight*0.2);
        VerticalLayout loginFormLayout = new VerticalLayout();
        VerticalLayout heightSpaceDivLayout_0 = new VerticalLayout();
        heightSpaceDivLayout_0.setHeight(headerSpaceHeight,Unit.PIXELS);
        loginFormLayout.addComponent(heightSpaceDivLayout_0);
        addComponent(loginFormLayout);

        HorizontalLayout formLayout=new HorizontalLayout();
        formLayout.setMargin(true);

        //set login picture
        Embedded loginPicEmbedded=null;
        if( "zh-CN".equals(languageTag)){
            loginPicEmbedded=new Embedded(null, Login_picture_cn);

        }else{
            loginPicEmbedded=new Embedded(null, Login_picture);
        }
        formLayout.addComponent(loginPicEmbedded);
        //set login spacing div
        HorizontalLayout spaceDivLayout=new HorizontalLayout();
        spaceDivLayout.setWidth("20px");
        formLayout.addComponent(spaceDivLayout);

        //set login form input fields
        VerticalLayout loginFormFieldLayout = new VerticalLayout();
        formLayout.addComponent(loginFormFieldLayout);

        VerticalLayout heightSpaceDivLayout_1 = new VerticalLayout();
        heightSpaceDivLayout_1.setHeight(10,Unit.PIXELS);
        loginFormFieldLayout.addComponent(heightSpaceDivLayout_1);

        Embedded viewfunctionLogoEmbedded=new Embedded(null, viewfunctionLogo_picture);
        loginFormFieldLayout.addComponent(viewfunctionLogoEmbedded);

        HorizontalLayout applicationLabelContainerLayout=new HorizontalLayout();
        HorizontalLayout applicationLabelSpacingLayout=new HorizontalLayout();
        applicationLabelSpacingLayout.setWidth(20,Unit.PIXELS);
        applicationLabelContainerLayout.addComponent(applicationLabelSpacingLayout);

        Label applicationLabel=new Label(userI18NProperties.getProperty("Application_LoginForm_ApplicationLabel"));
        applicationLabel.addStyleName(ValoTheme.LABEL_HUGE);
        applicationLabel.addStyleName(ValoTheme.LABEL_COLORED);
        applicationLabel.addStyleName(ValoTheme.LABEL_BOLD);
        applicationLabelContainerLayout.addComponent(applicationLabel);

        loginFormFieldLayout.addComponent(applicationLabelContainerLayout);

        FormLayout userLoginForm=new FormLayout();
        userLoginForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        adminUserNameField = new TextField(userI18NProperties.getProperty("Application_LoginForm_AdminName"));
        adminUserNameField.setIcon(FontAwesome.USER);
        adminUserNameField.setWidth("220px");
        userLoginForm.addComponent(adminUserNameField);

        passwordField = new PasswordField(userI18NProperties.getProperty("Application_LoginForm_AdminPWD"));
        passwordField.setIcon(FontAwesome.LOCK);
        passwordField.setWidth("220px");
        userLoginForm.addComponent(passwordField);

        HorizontalLayout loginFormBottomLayout=new HorizontalLayout();
        loginFormBottomLayout.setHeight(30,Unit.PIXELS);
        userLoginForm.addComponent(loginFormBottomLayout);

        loginFormFieldLayout.addComponent(userLoginForm);

        Button loginButton=new Button(userI18NProperties.getProperty("Application_LoginForm_LoginButtonLabel"));
        loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        loginButton.setIcon(FontAwesome.SIGN_IN);

        loginButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                doUserLogin(userI18NProperties);
            }
        });
        HorizontalLayout loginButtonContainerLayout=new HorizontalLayout();

        HorizontalLayout loginButtonSpacingLayout=new HorizontalLayout();
        loginButtonSpacingLayout.setWidth(300,Unit.PIXELS);
        loginButtonContainerLayout.addComponent(loginButtonSpacingLayout);
        loginButtonContainerLayout.addComponent(loginButton);
        loginFormFieldLayout.addComponent(loginButtonContainerLayout);

        Panel loginPanel=new Panel();
        loginPanel.setWidth(980,Unit.PIXELS);
        loginPanel.setContent(formLayout);

        HorizontalLayout loginFormPanelContainerLayout=new HorizontalLayout();
        loginFormPanelContainerLayout.setWidth(100,Unit.PERCENTAGE);
        loginFormPanelContainerLayout.addComponent(loginPanel);
        loginFormPanelContainerLayout.setComponentAlignment(loginPanel,Alignment.MIDDLE_CENTER);

        this.addComponent(loginFormPanelContainerLayout);
    }

    private void doUserLogin(Properties userI18NProperties){
        String userName=adminUserNameField.getValue();
        String userPWD=passwordField.getValue();
        if(userName.equals("")){
            Notification.show(userI18NProperties.getProperty("Application_LoginForm_inputLoginInfoPrompt"),Notification.Type.TRAY_NOTIFICATION);
            return;
        }else{
            if(this.getContainerApplicationUI()!=null){
                boolean loginUnfoVerifyResult= ApplicationConfigUtil.verifyUserLoginInfo(userName,userPWD);
                if(loginUnfoVerifyResult){
                    this.getContainerApplicationUI().setLoginUserId(userName);
                    this.getContainerApplicationUI().renderOperationUI(this.currentUserClientInfo);
                }else{
                    Notification errorNotification = new Notification(userI18NProperties.getProperty("ActivityManagement_Common_ErrorText"),
                            userI18NProperties.getProperty("Application_LoginForm_verifyUserLoginInfoErrorText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        }
    }

    public VFBPM_AdminApplicationUI getContainerApplicationUI() {
        return containerApplicationUI;
    }

    public void setContainerApplicationUI(VFBPM_AdminApplicationUI containerApplicationUI) {
        this.containerApplicationUI = containerApplicationUI;
    }
}
