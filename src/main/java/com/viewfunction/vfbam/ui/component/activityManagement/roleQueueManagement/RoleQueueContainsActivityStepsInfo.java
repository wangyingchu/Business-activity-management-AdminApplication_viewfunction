package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityStepsTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RoleQueueContainsActivityStepsInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private String roleQueueName;
    private SectionActionsBar workingTasksActionsBar;
    public RoleQueueContainsActivityStepsInfo(UserClientInfo currentUserClientInfo,String roleQueueName){
        this.currentUserClientInfo=currentUserClientInfo;
        this.roleQueueName=roleQueueName;
        setSpacing(true);
        setMargin(true);
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        SecondarySectionTitle roleQueueContainsActivityStepsSectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_RoleQueueActivityStepsText"));
        addComponent(roleQueueContainsActivityStepsSectionTitle);

        workingTasksActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_RoleQueuesManagement_RoleQueueText")+" : <b>"+this.roleQueueName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(workingTasksActionsBar);

        ActivityStepsTable activityStepsTable =new ActivityStepsTable(this.currentUserClientInfo,"300px");
        addComponent(activityStepsTable);
    }

    @Override
    public void attach() {
        super.attach();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String activitySpaceName="";
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        }
        Label sectionActionBarLabel= new Label(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_RoleQueueText")+" : <b>"+this.roleQueueName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
        workingTasksActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
    }
}
