package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.component.common.*;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ActivityStepDecisionPointEditor  extends VerticalLayout {
    private SectionActionsBar updateStepDecisionPointActionsBar;

    private UserClientInfo currentUserClientInfo;
    private ActivityStepVO currentActivityStep;
    private Window containerDialog;
    private FormLayout form;
    private HorizontalLayout footer;
    private TextField decisionPointAttribute;
    private SectionActionButton addDecisionOptionActionButton;
    private PropertyValuesActionTable propertyValuesActionTable;
    public ActivityStepDecisionPointEditor(UserClientInfo currentUserClientInfo,ActivityStepVO currentActivityStep) {
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        this.currentActivityStep=currentActivityStep;

        MainSectionTitle updateStepDecisionPointSectionTitle = new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_UpdateStepDecisionPointsPropertiesText"));
        addComponent(updateStepDecisionPointSectionTitle);
        updateStepDecisionPointActionsBar=new SectionActionsBar(new Label(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_DefinitionText")+" : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(updateStepDecisionPointActionsBar);

        form = new FormLayout();
        form.setMargin(false);
        form.setSpacing(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);
        decisionPointAttribute = new TextField(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepDecisionAttributeNameText"));
        decisionPointAttribute.setRequired(false);
        decisionPointAttribute.setWidth("100%");
        decisionPointAttribute.setInputPrompt(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_InputDecisionAttributeNamePromptText"));
        form.addComponent(decisionPointAttribute);

        SectionActionsBar launchDecisionPointSectionActionsBar=new SectionActionsBar(new Label(FontAwesome.LIST.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepDecisionOptionsText"), ContentMode.HTML));
        addComponent(launchDecisionPointSectionActionsBar);
        addDecisionOptionActionButton = new SectionActionButton();
        addDecisionOptionActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepDecisionPointAddOptionButtonLabel"));
        addDecisionOptionActionButton.setIcon(FontAwesome.PLUS_SQUARE);
        launchDecisionPointSectionActionsBar.addActionComponent(addDecisionOptionActionButton);
        propertyValuesActionTable=new PropertyValuesActionTable(this.currentUserClientInfo,"180",userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepDecisionPointOptionsText"),userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepDecisionPointChooseOptionsText"),true,true);
        addComponent(propertyValuesActionTable);

        addDecisionOptionActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                propertyValuesActionTable.addPropertyValue();
            }
        });

        footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        addComponent(footer);

        Button confirmButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_ConfirmChangeButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateDecisionPointProperties();
            }
        });
        confirmButton.setIcon(FontAwesome.CHECK);
        confirmButton.addStyleName("primary");
        footer.addComponent(confirmButton);

        Button cancelButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_ResetButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setDecisionPointValue();
            }
        });
        cancelButton.setIcon(FontAwesome.TIMES);
        footer.addComponent(cancelButton);
        this.setComponentAlignment(footer, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void attach() {
        super.attach();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        String componentType=currentActivitySpaceComponentInfo.getComponentType();
        String componentId=currentActivitySpaceComponentInfo.getComponentId();
        if(componentType==null){
            return;
        }else{
            String activitySpaceName="";
            Label sectionActionBarLabel=null;
            if(currentActivitySpaceComponentInfo!=null){
                activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            }
            String activityStepCombinationStr=currentActivityStep.getActivityStepName()+" ("+currentActivityStep.getActivityStepDisplayName() + ")";
            sectionActionBarLabel=new Label(userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_StepText")+" : <b>"+activityStepCombinationStr+"</b> &nbsp;&nbsp;<br/>"+userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeText")+" : "+componentId+" &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            updateStepDecisionPointActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        setDecisionPointValue();
    }

    private void setDecisionPointValue(){
        decisionPointAttribute.setValue("");
        propertyValuesActionTable.setPropertyValues(null);
        String attributeName=this.currentActivityStep.getDecisionPointAttribute();
        String[] chooseOption=this.currentActivityStep.getDecisionPointChooseOption();
        if(attributeName!=null){
            decisionPointAttribute.setValue(attributeName);
        }
        if(chooseOption!=null){
            propertyValuesActionTable.setPropertyValues(chooseOption);
        }
    }

    private void updateDecisionPointProperties(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        final String activityType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        final String activityStepName=currentActivityStep.getActivityStepName();
        final String newDecisionPointAttribute=decisionPointAttribute.getValue();
        final String[] newChooseOptions=propertyValuesActionTable.getPropertyValues();
        if(newDecisionPointAttribute==null||newDecisionPointAttribute.equals("")) {
            if(newChooseOptions==null||newChooseOptions.length==0){}else{
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_ActivityTypeManagement_PleaseInputStepDecisionAttributeText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
        }
        if(newChooseOptions==null||newChooseOptions.length==0) {
            if(newDecisionPointAttribute==null||newDecisionPointAttribute.equals("")) {}else{
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_ActivityTypeManagement_PleaseInputStepDecisionOptionAttributeText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
        }

        final ActivityStepDecisionPointEditor self=this;
        Label confirmMessage = new Label(FontAwesome.INFO.getHtml() +" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ConfirmUpdateStepDecisionPropertiesText")+
                " <b>" + currentActivityStep.getActivityStepName() + "</b>.", ContentMode.HTML);
        final ConfirmDialog updateDecisionPointPropertiesConfirmDialog = new ConfirmDialog();
        updateDecisionPointPropertiesConfirmDialog.setConfirmMessage(confirmMessage);
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                updateDecisionPointPropertiesConfirmDialog.close();
                if(self.containerDialog!=null){
                    self.containerDialog.close();
                }
                boolean setDecisionPointPropResult=ActivitySpaceOperationUtil.setActivityTypeStepDecisionPointProperties(activitySpaceName, activityType, activityStepName, newDecisionPointAttribute, newChooseOptions);
                if(setDecisionPointPropResult){
                    self.currentActivityStep.setDecisionPointAttribute(newDecisionPointAttribute);
                    self.currentActivityStep.setDecisionPointChooseOption(newChooseOptions);
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_ActivityTypeManagement_UpdateStepDecisionPropertiesSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_UpdateStepDecisionPropertiesErrorText"),
                            userI18NProperties.
                                    getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        updateDecisionPointPropertiesConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(updateDecisionPointPropertiesConfirmDialog);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }
}
