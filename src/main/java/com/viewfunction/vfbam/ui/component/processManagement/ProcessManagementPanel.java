package com.viewfunction.vfbam.ui.component.processManagement;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ProcessManagementPanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;

    public ProcessManagementPanel(UserClientInfo currentUserClientInfo){
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
