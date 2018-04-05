package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ActivityManagementPanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public ActivityManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        String windowsHeight=""+(screenHeight-230)+"px";
        this.setHeight(windowsHeight);

        HorizontalSplitPanel activityManagementSplitPanel = new HorizontalSplitPanel();
        activityManagementSplitPanel.setSizeFull();
        activityManagementSplitPanel.setSplitPosition(250, Unit.PIXELS);

        ActivityObjectBrowser activityObjectBrowser=new ActivityObjectBrowser(currentUserClientInfo);
        activityManagementSplitPanel.setFirstComponent(activityObjectBrowser);

        ActivityObjectDetail activityObjectDetail=new ActivityObjectDetail(currentUserClientInfo);
        activityManagementSplitPanel.setSecondComponent(activityObjectDetail);
        this.addComponent(activityManagementSplitPanel);
        this.setExpandRatio(activityManagementSplitPanel, 1.0F);
    }
}
