package com.viewfunction.vfbam.ui.component;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.VFBPM_AdminApplicationUI;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ApplicationBanner extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private VFBPM_AdminApplicationUI containerApplicationUI;
    private  Label loginUserName;
    public ApplicationBanner(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setHeight("55px");
        setWidth("100%");
        setSpacing(true);
        this.setStyleName("ui_appBanner");

        HorizontalLayout leftElementContainer=new HorizontalLayout();
        HorizontalLayout rightElementContainer=new HorizontalLayout();
        this.addComponent(leftElementContainer);
        this.addComponent(rightElementContainer);
        this.setComponentAlignment(leftElementContainer, Alignment.MIDDLE_LEFT);
        this.setComponentAlignment(rightElementContainer, Alignment.MIDDLE_RIGHT);

        Image applicationLogo = new Image();
        applicationLogo.setSource(new ThemeResource("imgs/vflogo.png"));
        leftElementContainer. addComponent(applicationLogo);

        Label applicationTitle = new Label(userI18NProperties.getProperty("Global_Application_Title"));
        applicationTitle.addStyleName("ui_appTitle");
        leftElementContainer. addComponent(applicationTitle);

        loginUserName = new Label( FontAwesome.USER.getHtml() + "-",ContentMode.HTML);
        rightElementContainer. addComponent(loginUserName);
        rightElementContainer.setComponentAlignment(loginUserName, Alignment.MIDDLE_CENTER);

        Button signOutButton = new Button(userI18NProperties.getProperty("Global_Application_SignOut"));
        signOutButton.setIcon(FontAwesome.SIGN_OUT);
        signOutButton.addStyleName("borderless-colored");
        rightElementContainer.addComponent(signOutButton);

        signOutButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if(getContainerApplicationUI()!=null){
                    getContainerApplicationUI().renderLoginUI(currentUserClientInfo);
                }
                UI.getCurrent().getSession().close();
                UI.getCurrent().getPage().reload();
            }
        });

        Label spaceDivLabel1=new Label("|");
        rightElementContainer. addComponent(spaceDivLabel1);
        rightElementContainer.setComponentAlignment(spaceDivLabel1, Alignment.MIDDLE_CENTER);

        Button viewfunctionDescButton = new Button(userI18NProperties.getProperty("Global_Application_ViewfunctionDesc"));
        viewfunctionDescButton.setIcon(FontAwesome.INFO_CIRCLE);
        viewfunctionDescButton.addStyleName("borderless");
        rightElementContainer. addComponent(viewfunctionDescButton);
        viewfunctionDescButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window("www.viewfunction.com");
                window.setWidth(1000.0f, Unit.PIXELS);
                window.setHeight(800.0f, Unit.PIXELS);
                window.center();
                BrowserFrame viewfunctionWebPage = new BrowserFrame("www.viewfunction.com", new ExternalResource(
                        "http://www.viewfunction.com"));
                viewfunctionWebPage.setSizeFull();
                window.setContent(viewfunctionWebPage);
                UI.getCurrent().addWindow(window);
            }
        });
        Label supportLabel2=new Label(" ");
        rightElementContainer. addComponent(supportLabel2);
    }

    @Override
    public void attach() {
        super.attach();
        loginUserName.setValue(FontAwesome.USER.getHtml() +" "+ getContainerApplicationUI().getLoginUserId());
    }

    public VFBPM_AdminApplicationUI getContainerApplicationUI() {
        return containerApplicationUI;
    }

    public void setContainerApplicationUI(VFBPM_AdminApplicationUI containerApplicationUI) {
        this.containerApplicationUI = containerApplicationUI;
    }
}
