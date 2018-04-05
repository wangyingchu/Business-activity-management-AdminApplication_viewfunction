package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class GeneralInfoView extends VerticalLayout implements View {
    private UserClientInfo currentUserClientInfo;
    public GeneralInfoView(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setMargin(true);
        this.setSpacing(true);
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        VerticalLayout viewContentContainer = new VerticalLayout();
        viewContentContainer.setMargin(false);
        viewContentContainer.setSpacing(false);
        viewContentContainer.addStyleName("ui_appSubViewContainer");
        this.addComponent(viewContentContainer);
        // View Title
        MainSectionTitle mainSectionTitle=new MainSectionTitle(userI18NProperties.getProperty("ActivityManagement_GeneralInfo_WelcomeTitle"));
        viewContentContainer.addComponent(mainSectionTitle);
        Label welcomeMessage = new Label( FontAwesome.SMILE_O.getHtml()+" "+userI18NProperties.getProperty("ActivityManagement_GeneralInfo_WelcomeText"), ContentMode.HTML);
        welcomeMessage.setStyleName("ui_appLightDarkMessage");
        viewContentContainer.addComponent(welcomeMessage);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
