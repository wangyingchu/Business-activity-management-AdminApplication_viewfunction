package com.viewfunction.vfbam.ui.component.activityManagement.roleManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement.RoleQueuesActionTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RoleRelatedRoleQueuesInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar relatedRoleQueueSectionActionsBar;
    private String roleName;

    public RoleRelatedRoleQueuesInfo(UserClientInfo currentUserClientInfo,String roleName){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.roleName=roleName;
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle participantsBelongsToRoleSectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RolesManagement_RoleRoleQueuesActionButtonsLabel"));
        addComponent(participantsBelongsToRoleSectionTitle);

        relatedRoleQueueSectionActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_RolesManagement_RoleText")+" : <b>"+this.roleName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(relatedRoleQueueSectionActionsBar);
        RoleQueuesActionTable roleQueuesActionTable=new RoleQueuesActionTable(this.currentUserClientInfo, "300px",false,false);
        roleQueuesActionTable.setRoleQueuesType(RoleQueuesActionTable.ROLEQUEUES_TYPE_ROLE);
        roleQueuesActionTable.setRoleQueuesQueryId(this.roleName);
        addComponent(roleQueuesActionTable);
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
                    getProperty("ActivityManagement_RolesManagement_RoleText")+" : <b>"+this.roleName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            relatedRoleQueueSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}
