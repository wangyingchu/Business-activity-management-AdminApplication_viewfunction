package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RosterManagementView extends VerticalLayout implements View {
    private UserClientInfo currentUserClientInfo;
    private  VerticalLayout viewContentContainer;
    private String activitySpaceName;
    private String rosterName;
    public RosterManagementView(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setMargin(false);
        this.setSpacing(false);
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        viewContentContainer = new VerticalLayout();
        viewContentContainer.setMargin(false);
        viewContentContainer.setSpacing(false);
        viewContentContainer.addStyleName("ui_appSubViewContainer");
        this.addComponent(viewContentContainer);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        if(currentActivitySpaceComponentInfo!=null){
            String newActivitySpaceName=currentActivitySpaceComponentInfo.getActivitySpaceName();
            String newRosterName=currentActivitySpaceComponentInfo.getComponentId();
            if(!newActivitySpaceName.equals(activitySpaceName)||!newRosterName.equals(rosterName)){
                activitySpaceName=newActivitySpaceName;
                rosterName=newRosterName;
                RosterDetailInfo rosterDetailInfo=new RosterDetailInfo(this.currentUserClientInfo);
                viewContentContainer.removeAllComponents();
                viewContentContainer.addComponent(rosterDetailInfo);
            }
        }
    }
}