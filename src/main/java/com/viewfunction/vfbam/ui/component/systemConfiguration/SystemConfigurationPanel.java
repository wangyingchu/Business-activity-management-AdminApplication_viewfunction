package com.viewfunction.vfbam.ui.component.systemConfiguration;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class SystemConfigurationPanel  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;

    public SystemConfigurationPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();

        VerticalLayout content0 = new VerticalLayout();
        content0.setMargin(true);
        content0.setSpacing(true);
        // content.setHeight("500px");
        content0.setSizeFull();
        content0.addComponent(new Label("Content for tab "));
        this.addComponent(content0);

    }
}
