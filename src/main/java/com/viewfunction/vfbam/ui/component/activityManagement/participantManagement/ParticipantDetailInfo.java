package com.viewfunction.vfbam.ui.component.activityManagement.participantManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityStepsTable;
import com.viewfunction.vfbam.ui.component.activityManagement.roleManagement.RolesActionTable;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ParticipantDetailInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public ParticipantDetailInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        setSpacing(false);
        setMargin(true);

        String activitySpaceName=currentActivitySpaceComponentInfo.getActivitySpaceName();
        String participantName=currentActivitySpaceComponentInfo.getComponentId();
        Participant currentParticipant=ActivitySpaceOperationUtil.getParticipantByName(activitySpaceName, participantName);

        TabSheet tabs=new TabSheet();
        addComponent(tabs);
        VerticalLayout participantInfoLayout=new VerticalLayout();
        TabSheet.Tab participantInfoTab =tabs.addTab(participantInfoLayout, userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_ParticipantInfoText"));
        participantInfoTab.setIcon(FontAwesome.INFO);

        MainSectionTitle mainSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_ParticipantInfoText"));
        participantInfoLayout.addComponent(mainSectionTitle);
        //Participant info editor
        ParticipantEditor participantEditor =new ParticipantEditor(this.currentUserClientInfo,ParticipantEditor.EDITMODE_UPDATE);
        participantEditor.setParticipant(currentParticipant);
        participantInfoLayout.addComponent(participantEditor);

        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        String tableHeightString=""+(browserWindowHeight-330)+"px";

        VerticalLayout belongsToRolesLayout=new VerticalLayout();
        TabSheet.Tab belongsToRolesTab = tabs.addTab(belongsToRolesLayout, userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_ParticipantRolesText"));
        belongsToRolesTab.setIcon(FontAwesome.USERS);
        // Participant's Roles Info Section
        MainSectionTitle belongsToRolesSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_ParticipantRolesText"));
        belongsToRolesLayout.addComponent(belongsToRolesSectionTitle);
        SectionActionsBar belongsToRolesSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.USERS.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_ParticipantRolesActionButtonsLabel"), ContentMode.HTML));
        belongsToRolesLayout.addComponent(belongsToRolesSectionActionsBar);
        SectionActionButton modifyBelongedRolesActionButton = new SectionActionButton();
        modifyBelongedRolesActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_ModifyParticipantRoleButtonLabel"));
        modifyBelongedRolesActionButton.setIcon(FontAwesome.COG);
        belongsToRolesSectionActionsBar.addActionComponent(modifyBelongedRolesActionButton);

        RolesActionTable rolesActionTable=new RolesActionTable(this.currentUserClientInfo,tableHeightString,true);
        rolesActionTable.setRolesQueryId(currentActivitySpaceComponentInfo.getComponentId());
        rolesActionTable.setRolesType(RolesActionTable.ROLES_TYPE_PARTICIPANT);
        belongsToRolesLayout.addComponent(rolesActionTable);

        final ParticipantBelongedRolesSelector participantBelongedRolesSelector=new ParticipantBelongedRolesSelector(this.currentUserClientInfo);
        participantBelongedRolesSelector.setParticipant(currentParticipant);
        participantBelongedRolesSelector.setRelatedRolesActionTable(rolesActionTable);
        modifyBelongedRolesActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1000.0f, Unit.PIXELS);
                window.setHeight(490.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(participantBelongedRolesSelector);
                participantBelongedRolesSelector.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        VerticalLayout participantTasksLayout=new VerticalLayout();
        TabSheet.Tab participantTasksTab = tabs.addTab(participantTasksLayout, userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_ParticipantTasksText"));
        participantTasksTab.setIcon(FontAwesome.TASKS);
        // Participant's Working Tasks Section
        MainSectionTitle workingTasksSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_ParticipantTasksText"));
        participantTasksLayout.addComponent(workingTasksSectionTitle);
        SectionActionsBar  workingTasksSectionActionsBar=new SectionActionsBar(new Label(FontAwesome.TASKS.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_ParticipantTasksActionButtonsLabel"), ContentMode.HTML));
        participantTasksLayout.addComponent(workingTasksSectionActionsBar);
        SectionActionButton fetchParticipantTasksActionButton = new SectionActionButton();
        fetchParticipantTasksActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_FetchParticipantTasksButtonLabel"));
        fetchParticipantTasksActionButton.setIcon(FontAwesome.DOWNLOAD);
        workingTasksSectionActionsBar.addActionComponent(fetchParticipantTasksActionButton);

        final ActivityStepsTable activityStepsTable =new ActivityStepsTable(this.currentUserClientInfo,tableHeightString);
        activityStepsTable.setActivityStepQueryId(currentActivitySpaceComponentInfo.getComponentId());
        activityStepsTable.setActivityStepType(ActivityStepsTable.ACTIVITYSTEPS_TYPE_PARTICIPANT);
        participantTasksLayout.addComponent(activityStepsTable);

        fetchParticipantTasksActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                activityStepsTable.loadActivityStepsData();
            }
        });
    }
}