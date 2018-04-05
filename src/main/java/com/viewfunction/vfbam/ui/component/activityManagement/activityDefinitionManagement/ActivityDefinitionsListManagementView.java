package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;

import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.activityManagement.participantManagement.AddNewParticipantPanel;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ActivityDefinitionsListManagementView extends VerticalLayout implements View {
    private UserClientInfo currentUserClientInfo;
    public ActivityDefinitionsListManagementView(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setMargin(true);
        this.setSpacing(true);
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        VerticalLayout viewContentContainer = new VerticalLayout();
        viewContentContainer.setMargin(false);
        viewContentContainer.setSpacing(false);
        viewContentContainer.addStyleName("ui_appSubViewContainer");
        this.addComponent(viewContentContainer);
        // View Title
        MainSectionTitle mainSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_InfoText"));
        viewContentContainer.addComponent(mainSectionTitle);
        // Activity Definitions List Section
        SectionActionsBar definitionsListSectionActionsBar=new SectionActionsBar(new Label(FontAwesome.SHARE_ALT_SQUARE.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ListText"), ContentMode.HTML));
        viewContentContainer.addComponent(definitionsListSectionActionsBar);
        SectionActionButton addNewDefinitionActionButton = new SectionActionButton();
        addNewDefinitionActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AddNewActivityTypeButtonLabel"));
        addNewDefinitionActionButton.setIcon(FontAwesome.PLUS_SQUARE);
        definitionsListSectionActionsBar.addActionComponent(addNewDefinitionActionButton);
        final AddNewActivityTypePanel addNewActivityTypePanel=new AddNewActivityTypePanel(this.currentUserClientInfo);
        addNewDefinitionActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Unit.PIXELS);
                window.setHeight(300.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(addNewActivityTypePanel);
                addNewActivityTypePanel.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        String tableHeightString=""+(browserWindowHeight-300)+"px";
        ActivityDefinitionsActionTable activityDefinitionsActionTable=new ActivityDefinitionsActionTable(this.currentUserClientInfo,tableHeightString,true);
        activityDefinitionsActionTable.setActivityDefinitionsQueryId(null);
        activityDefinitionsActionTable.setActivityDefinitionsType(ActivityDefinitionsActionTable.ACTIVITYDEFINITIONS_TYPE_ACTIVITYDEFINITION);
        viewContentContainer.addComponent(activityDefinitionsActionTable);
        addNewActivityTypePanel.setRelatedActivityDefinitionsActionTable(activityDefinitionsActionTable);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
