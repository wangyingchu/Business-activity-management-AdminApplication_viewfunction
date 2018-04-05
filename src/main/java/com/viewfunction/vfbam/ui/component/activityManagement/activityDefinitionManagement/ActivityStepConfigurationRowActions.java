package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.RoleVO;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.List;
import java.util.Properties;

public class ActivityStepConfigurationRowActions extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private ActivityStepVO currentActivityStep;
    private List<ActivityDataFieldVO> activityDataFieldsList;
    private ActivityStepExposedDataFieldsEditor activityStepExposedDataFieldsEditor;
    private ActivityStepItem relatedActivityStepItem;
    public ActivityStepConfigurationRowActions(UserClientInfo currentUserClientInfo,ActivityStepVO currentActivityStep,List<ActivityDataFieldVO> activityDataFieldsList,List<RoleVO> rolesList){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.currentActivityStep= currentActivityStep;
        this.activityDataFieldsList = activityDataFieldsList;
        Button showContainsDataFieldsButton = new Button();
        showContainsDataFieldsButton.setIcon(FontAwesome.TH_LIST);
        showContainsDataFieldsButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepDataFieldsText"));
        showContainsDataFieldsButton.addStyleName("small");
        showContainsDataFieldsButton.addStyleName("borderless");
        addComponent(showContainsDataFieldsButton);
        activityStepExposedDataFieldsEditor=
                new ActivityStepExposedDataFieldsEditor(this.currentUserClientInfo,this.currentActivityStep,this.activityDataFieldsList);
        showContainsDataFieldsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1000.0f, Sizeable.Unit.PIXELS);
                window.setHeight(520.0f, Sizeable.Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(activityStepExposedDataFieldsEditor);
                activityStepExposedDataFieldsEditor.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        Label spaceDivLabel=new Label("|");
        addComponent(spaceDivLabel);

        Button showActivityStepPropertiesButton = new Button();
        showActivityStepPropertiesButton.setIcon(FontAwesome.BARS);
        showActivityStepPropertiesButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepProcessPropertiesText"));
        showActivityStepPropertiesButton.addStyleName("small");
        showActivityStepPropertiesButton.addStyleName("borderless");
        addComponent(showActivityStepPropertiesButton);
        final ActivityStepProcessVariablesEditor activityStepProcessVariablesEditor=
                new ActivityStepProcessVariablesEditor(this.currentUserClientInfo,this.currentActivityStep,rolesList);
        showActivityStepPropertiesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1000.0f, Sizeable.Unit.PIXELS);
                window.setHeight(520.0f, Sizeable.Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(activityStepProcessVariablesEditor);
                activityStepProcessVariablesEditor.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        Button showStepDecisionPointsPropertiesButton = new Button();
        showStepDecisionPointsPropertiesButton.setIcon(FontAwesome.COG);
        showStepDecisionPointsPropertiesButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepDecisionPointsPropertiesText"));
        showStepDecisionPointsPropertiesButton.addStyleName("small");
        showStepDecisionPointsPropertiesButton.addStyleName("borderless");
        addComponent(showStepDecisionPointsPropertiesButton);
        final ActivityStepDecisionPointEditor activityStepDecisionPointEditor=new ActivityStepDecisionPointEditor(this.currentUserClientInfo,this.currentActivityStep);
        showStepDecisionPointsPropertiesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Sizeable.Unit.PIXELS);
                window.setHeight(540.0f, Sizeable.Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(activityStepDecisionPointEditor);
                activityStepDecisionPointEditor.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        Button removeCurrentStepButton = new Button();
        removeCurrentStepButton.setIcon(FontAwesome.TRASH_O);
        removeCurrentStepButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_DontExposeStepText"));
        removeCurrentStepButton.addStyleName("small");
        removeCurrentStepButton.addStyleName("borderless");
        addComponent(removeCurrentStepButton);
        removeCurrentStepButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                removeCurrentStepFromExposedStepList();
            }
        });
    }

    public ActivityStepExposedDataFieldsEditor getActivityStepExposedDataFieldsEditor() {
        return activityStepExposedDataFieldsEditor;
    }

    public void removeCurrentStepFromExposedStepList(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final ActivityStepItem activityStepItem=this.relatedActivityStepItem;
        Label confirmMessage = new Label(FontAwesome.INFO.getHtml() +" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ConfirmDontExposeStepText")+
                " <b>" + currentActivityStep.getActivityStepName() + "</b>.", ContentMode.HTML);
        final ConfirmDialog deleteStepExposeConfirmDialog = new ConfirmDialog();
        deleteStepExposeConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                activityStepItem.removeCurrentActivityStepFromExposedStepList();
                deleteStepExposeConfirmDialog.close();
            }
        };
        deleteStepExposeConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(deleteStepExposeConfirmDialog);
    }

    public void setRelatedActivityStepItem(ActivityStepItem relatedActivityStepItem) {
        this.relatedActivityStepItem = relatedActivityStepItem;
    }
}
