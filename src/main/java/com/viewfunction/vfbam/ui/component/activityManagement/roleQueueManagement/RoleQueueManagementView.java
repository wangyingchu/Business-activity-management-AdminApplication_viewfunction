package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RoleQueueManagementView extends VerticalLayout implements View {
    private UserClientInfo currentUserClientInfo;
    private  VerticalLayout viewContentContainer;
    private String activitySpaceName;
    private String roleQueueName;
    public RoleQueueManagementView(UserClientInfo currentUserClientInfo){
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
            String newRoleQueueName=currentActivitySpaceComponentInfo.getComponentId();
            if(!newActivitySpaceName.equals(activitySpaceName)||!newRoleQueueName.equals(roleQueueName)){
                activitySpaceName=newActivitySpaceName;
                roleQueueName=newRoleQueueName;
                RoleQueueDetailInfo roleQueueDetailInfo=new RoleQueueDetailInfo(this.currentUserClientInfo);
                viewContentContainer.removeAllComponents();
                viewContentContainer.addComponent(roleQueueDetailInfo);
            }
        }
    }
}
