package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.activityManagement.roleManagement.RolesActionTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RoleQueueRelatedRolesInfo  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar relatedRolesSectionActionsBar;
    private String roleQueueName;
    public RoleQueueRelatedRolesInfo(UserClientInfo currentUserClientInfo,String roleQueueName){
        this.currentUserClientInfo=currentUserClientInfo;
        this.roleQueueName=roleQueueName;
        setSpacing(true);
        setMargin(true);
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.roleQueueName=roleQueueName;
        SecondarySectionTitle relatedToRolesSectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_RoleQueueRolesActionButtonsLabel"));
        addComponent(relatedToRolesSectionTitle);

        relatedRolesSectionActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_RoleQueuesManagement_RoleQueueText")+" : <b>"+this.roleQueueName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(relatedRolesSectionActionsBar);

        RolesActionTable rolesSummaryTable=new RolesActionTable(this.currentUserClientInfo,"300px",false);
        rolesSummaryTable.setRolesQueryId(this.roleQueueName);
        rolesSummaryTable.setRolesType(RolesActionTable.ROLES_TYPE_ROLEQUEUE);
        addComponent(rolesSummaryTable);
    }

    @Override
    public void attach() {
        super.attach();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        if(currentActivitySpaceComponentInfo!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label sectionActionBarLabel=new Label(userI18NProperties.
                    getProperty("ActivityManagement_RoleQueuesManagement_RoleQueueText")+" : <b>"+this.roleQueueName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            relatedRolesSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}
