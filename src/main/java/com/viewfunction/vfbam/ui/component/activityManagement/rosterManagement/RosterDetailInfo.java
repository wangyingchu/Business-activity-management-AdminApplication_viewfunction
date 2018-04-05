package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityDataFieldsEditor;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityInstancesActionTable;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityManagementConst;
import com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement.ActivityDefinitionsActionTable;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RosterDetailInfo  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public RosterDetailInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        setSpacing(false);
        setMargin(true);

        String activitySpaceName=currentActivitySpaceComponentInfo.getActivitySpaceName();
        String rosterName=currentActivitySpaceComponentInfo.getComponentId();
        Roster currentRoster= ActivitySpaceOperationUtil.getRosterByName(activitySpaceName,rosterName);

        TabSheet tabs=new TabSheet();
        addComponent(tabs);

        VerticalLayout rosterInfoLayout=new VerticalLayout();
        TabSheet.Tab roleInfoTab =tabs.addTab(rosterInfoLayout, userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_RosterInfoText"));
        roleInfoTab.setIcon(FontAwesome.INFO);
        MainSectionTitle mainSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_RosterInfoText"));
        rosterInfoLayout.addComponent(mainSectionTitle);
        RosterEditor rosterEditor=new RosterEditor(this.currentUserClientInfo,RosterEditor.EDITMODE_UPDATE);
        rosterEditor.setRoster(currentRoster);
        rosterInfoLayout.addComponent(rosterEditor);

        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        String tableHeightString=""+(browserWindowHeight-330)+"px";

        VerticalLayout containedActivityTypeLayout=new VerticalLayout();
        TabSheet.Tab containedActivityTypeTab =tabs.addTab(containedActivityTypeLayout, userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_RosterActivityTypeText"));
        containedActivityTypeTab.setIcon(FontAwesome.SHARE_ALT_SQUARE);
        MainSectionTitle containedActivityDefinitionSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_RosterActivityTypeText"));
        containedActivityTypeLayout.addComponent(containedActivityDefinitionSectionTitle);

        ActivityDefinitionsActionTable activityDefinitionsActionTable=new ActivityDefinitionsActionTable(this.currentUserClientInfo,tableHeightString,true);
        activityDefinitionsActionTable.setActivityDefinitionsQueryId(currentActivitySpaceComponentInfo.getComponentId());
        activityDefinitionsActionTable.setActivityDefinitionsType(ActivityDefinitionsActionTable.ACTIVITYDEFINITIONS_TYPE_ROSTER);
        SectionActionsBar containsActivityDefinitionsSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.SHARE_ALT_SQUARE.getHtml() + " "+" "+userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_RosterActivityTypeActionButtonsLabel"), ContentMode.HTML));
        containedActivityTypeLayout.addComponent(containsActivityDefinitionsSectionActionsBar);
        SectionActionButton modifyContainedActivityDefinitionActionButton = new SectionActionButton();
        modifyContainedActivityDefinitionActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_ModifyRelatedActivityTypeButtonLabel"));
        modifyContainedActivityDefinitionActionButton.setIcon(FontAwesome.COG);
        final RosterRelatedActivityDefinitionsSelector rosterRelatedActivityDefinitionsSelector=new RosterRelatedActivityDefinitionsSelector(this.currentUserClientInfo);
        rosterRelatedActivityDefinitionsSelector.setRoster(currentRoster);
        rosterRelatedActivityDefinitionsSelector.setRelatedActivityDefinitionsActionTable(activityDefinitionsActionTable);
        modifyContainedActivityDefinitionActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Unit.PIXELS);
                window.setHeight(490.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setContent(rosterRelatedActivityDefinitionsSelector);
                rosterRelatedActivityDefinitionsSelector.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });
        containsActivityDefinitionsSectionActionsBar.addActionComponent(modifyContainedActivityDefinitionActionButton);
        containedActivityTypeLayout.addComponent(activityDefinitionsActionTable);

        VerticalLayout exposedDataFieldsLayout=new VerticalLayout();
        TabSheet.Tab exposedDataFieldsTab =tabs.addTab(exposedDataFieldsLayout, userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_DataFilterText"));
        exposedDataFieldsTab.setIcon(FontAwesome.TH_LIST);
        MainSectionTitle exposedDataFieldsSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_DataFilterText"));
        exposedDataFieldsLayout.addComponent(exposedDataFieldsSectionTitle);
        ActivityDataFieldsEditor activityDataFieldsEditor=new ActivityDataFieldsEditor(this.currentUserClientInfo,
                ActivityManagementConst.COMPONENT_TYPE_ROSTER,currentActivitySpaceComponentInfo.getComponentId(),tableHeightString);
        exposedDataFieldsLayout.addComponent(activityDataFieldsEditor);

        VerticalLayout businessActivitiesLayout=new VerticalLayout();
        TabSheet.Tab businessActivitiesTab =tabs.addTab(businessActivitiesLayout, userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_ActivityInstanceText"));
        businessActivitiesTab.setIcon(FontAwesome.INDENT);
        MainSectionTitle runningBusinessActivitiesSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_ActivityInstanceText"));
        businessActivitiesLayout.addComponent(runningBusinessActivitiesSectionTitle);
        SectionActionsBar activitiesContainedInRosterSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.INDENT.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_RosterActivityInstanceActionButtonsLabel"), ContentMode.HTML));
        businessActivitiesLayout.addComponent(activitiesContainedInRosterSectionActionsBar);
        SectionActionButton fetchActivitiesActionButton = new SectionActionButton();
        fetchActivitiesActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_FetchActivityInstanceButtonLabel"));
        fetchActivitiesActionButton.setIcon(FontAwesome.DOWNLOAD);
        activitiesContainedInRosterSectionActionsBar.addActionComponent(fetchActivitiesActionButton);

        final ActivityInstancesActionTable activityInstancesActionTable =new ActivityInstancesActionTable(this.currentUserClientInfo,tableHeightString,true);
        activityInstancesActionTable.setActivityInstancesQueryId(currentActivitySpaceComponentInfo.getComponentId());
        activityInstancesActionTable.setActivityInstancesQueryType(ActivityInstancesActionTable.ACTIVITYINSTANCES_TYPE_ROSTER);
        activityInstancesActionTable.setRelatedRoster(currentRoster);
        businessActivitiesLayout.addComponent(activityInstancesActionTable);

        fetchActivitiesActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                activityInstancesActionTable.loadActivityInstancesData();
            }
        });
    }
}
