package com.viewfunction.vfbam.ui.component.activityManagement.roleManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.participantManagement.ParticipantsActionTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RoleContainsParticipantsInfo  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar containsParticipantsSectionActionsBar;
    private String roleName;

    public RoleContainsParticipantsInfo(UserClientInfo currentUserClientInfo,String roleName){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.roleName=roleName;
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle participantsBelongsToRoleSectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RolesManagement_RoleParticipantsActionButtonsLabel"));
        addComponent(participantsBelongsToRoleSectionTitle);

        containsParticipantsSectionActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_RolesManagement_RoleText")+" : <b>"+this.roleName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(containsParticipantsSectionActionsBar);

        ParticipantsActionTable participantsActionTable=new ParticipantsActionTable(this.currentUserClientInfo,"300px",false,false);
        participantsActionTable.setParticipantsQueryId(this.roleName);
        participantsActionTable.setParticipantsType(ParticipantsActionTable.PARTICIPANTS_TYPE_ROLE);
        addComponent(participantsActionTable);
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
            containsParticipantsSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}
