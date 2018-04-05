package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityDataFieldsEditor;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityManagementConst;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityStepsTable;
import com.viewfunction.vfbam.ui.component.activityManagement.roleManagement.RolesActionTable;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RoleQueueDetailInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public RoleQueueDetailInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        setSpacing(false);
        setMargin(true);

        String activitySpaceName=currentActivitySpaceComponentInfo.getActivitySpaceName();
        String roleQueueName=currentActivitySpaceComponentInfo.getComponentId();
        RoleQueue currentRoleQueue= ActivitySpaceOperationUtil.getRoleQueueByName(activitySpaceName,roleQueueName);

        TabSheet tabs=new TabSheet();
        addComponent(tabs);
        VerticalLayout roleQueueInfoLayout=new VerticalLayout();
        TabSheet.Tab roleQueueInfoTab =tabs.addTab(roleQueueInfoLayout, userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_RoleInfoText"));
        roleQueueInfoTab.setIcon(FontAwesome.INFO);
        MainSectionTitle mainSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_RoleInfoText"));
        roleQueueInfoLayout.addComponent(mainSectionTitle);
        RoleQueueEditor roleQueueEditor=new RoleQueueEditor(this.currentUserClientInfo,RoleQueueEditor.EDITMODE_UPDATE);
        roleQueueEditor.setRoleQueue(currentRoleQueue);
        roleQueueInfoLayout.addComponent(roleQueueEditor);

        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        String tableHeightString=""+(browserWindowHeight-330)+"px";

        VerticalLayout relatedRolesLayout=new VerticalLayout();
        TabSheet.Tab relatedRolesTab =tabs.addTab(relatedRolesLayout, userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_RelatedRolesText"));
        relatedRolesTab.setIcon(FontAwesome.USERS);
        MainSectionTitle relatedRolesSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_RelatedRolesText"));
        relatedRolesLayout.addComponent(relatedRolesSectionTitle);
        SectionActionsBar relatedRolesSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.USERS.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_RelatedRolesActionButtonsLabel"), ContentMode.HTML));
        relatedRolesLayout.addComponent(relatedRolesSectionActionsBar);
        SectionActionButton modifyRelatedRolesActionButton = new SectionActionButton();
        modifyRelatedRolesActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_ModifyRelatedRolesButtonLabel"));
        modifyRelatedRolesActionButton.setIcon(FontAwesome.COG);
        final RoleQueueRelatedRolesSelector roleQueueRelatedRolesSelector=new RoleQueueRelatedRolesSelector(this.currentUserClientInfo);
        roleQueueRelatedRolesSelector.setRoleQueue(currentRoleQueue);
        modifyRelatedRolesActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(900.0f, Unit.PIXELS);
                window.setHeight(490.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setContent(roleQueueRelatedRolesSelector);
                roleQueueRelatedRolesSelector.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });
        relatedRolesSectionActionsBar.addActionComponent(modifyRelatedRolesActionButton);

        RolesActionTable rolesActionTable=new RolesActionTable(this.currentUserClientInfo,tableHeightString,true);
        rolesActionTable.setRolesQueryId(currentActivitySpaceComponentInfo.getComponentId());
        rolesActionTable.setRolesType(RolesActionTable.ROLES_TYPE_ROLEQUEUE);
        relatedRolesLayout.addComponent(rolesActionTable);
        roleQueueRelatedRolesSelector.setRelatedRolesActionTable(rolesActionTable);

        VerticalLayout exposedDataFieldsLayout=new VerticalLayout();
        TabSheet.Tab exposedDataFieldsTab =tabs.addTab(exposedDataFieldsLayout, userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_DataFilterText"));
        exposedDataFieldsTab.setIcon(FontAwesome.TH_LIST);
        MainSectionTitle exposedDataFieldsSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_DataFilterText"));
        exposedDataFieldsLayout.addComponent(exposedDataFieldsSectionTitle);
        ActivityDataFieldsEditor activityDataFieldsEditor=new ActivityDataFieldsEditor(this.currentUserClientInfo,
                ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE,currentActivitySpaceComponentInfo.getComponentId(),tableHeightString);
        exposedDataFieldsLayout.addComponent(activityDataFieldsEditor);

        VerticalLayout activityStepsLayout=new VerticalLayout();
        TabSheet.Tab activityStepsTab =tabs.addTab(activityStepsLayout, userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_ContainsActivityStepsText"));
        activityStepsTab.setIcon(FontAwesome.SLIDERS);
        MainSectionTitle activityStepsSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_ContainsActivityStepsText"));
        activityStepsLayout.addComponent(activityStepsSectionTitle);

        SectionActionsBar workingActivityStepsSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.SLIDERS.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_RoleWorkingActivityStepText"), ContentMode.HTML));
        activityStepsLayout.addComponent(workingActivityStepsSectionActionsBar);
        SectionActionButton fetchActivityStepsActionButton = new SectionActionButton();
        fetchActivityStepsActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_FetchStepsButtonLabel"));
        fetchActivityStepsActionButton.setIcon(FontAwesome.DOWNLOAD);
        workingActivityStepsSectionActionsBar.addActionComponent(fetchActivityStepsActionButton);
        final ActivityStepsTable activityStepsTable =new ActivityStepsTable(this.currentUserClientInfo,tableHeightString);
        activityStepsTable.setActivityStepType(ActivityStepsTable.ACTIVITYSTEPS_TYPE_ROLEQUEUE);
        activityStepsTable.setActivityStepQueryId(roleQueueName);
        activityStepsLayout.addComponent(activityStepsTable);

        fetchActivityStepsActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                activityStepsTable.loadActivityStepsData();
            }
        });
    }
}
