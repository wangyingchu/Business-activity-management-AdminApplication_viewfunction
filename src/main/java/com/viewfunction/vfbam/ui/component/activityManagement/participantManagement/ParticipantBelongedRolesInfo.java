package com.viewfunction.vfbam.ui.component.activityManagement.participantManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.activityManagement.roleManagement.RolesActionTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ParticipantBelongedRolesInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar belongsToRolesSectionActionsBar;
    private String participantName;
    public ParticipantBelongedRolesInfo(UserClientInfo currentUserClientInfo,String participantName){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.participantName=participantName;
        setSpacing(true);
        setMargin(true);
        // Participant's Roles Info Section
        SecondarySectionTitle belongsToRolesSectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_ParticipantRolesActionButtonsLabel"));
        addComponent(belongsToRolesSectionTitle);

        belongsToRolesSectionActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_ParticipantsManagement_ParticipantText")+" : <b>"+this.participantName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(belongsToRolesSectionActionsBar);

        RolesActionTable rolesSummaryTable=new RolesActionTable(this.currentUserClientInfo,"300px",false);
        rolesSummaryTable.setRolesQueryId(this.participantName);
        rolesSummaryTable.setRolesType(RolesActionTable.ROLES_TYPE_PARTICIPANT);
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
                    getProperty("ActivityManagement_ParticipantsManagement_ParticipantText")+" : <b>"+this.participantName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            belongsToRolesSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}
