package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityExposedDataFieldsEditor;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ActivityStepExposedDataFieldsEditor  extends VerticalLayout {

    private SectionActionsBar editDataFieldActionsBar;
    private ActivityExposedDataFieldsEditor activityExposedDataFieldsEditor;
    private UserClientInfo currentUserClientInfo;
    private ActivityStepVO currentActivityStep;
    private List<ActivityDataFieldVO> activityDataFieldsList;
    private ActivityStepItem relatedActivityStepItem;
    private Window containerDialog;

    public ActivityStepExposedDataFieldsEditor(UserClientInfo currentUserClientInfo,ActivityStepVO currentActivityStep,List<ActivityDataFieldVO> activityDataFieldsList) {
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.activityDataFieldsList = activityDataFieldsList;
        setSpacing(true);
        setMargin(true);
        this.currentActivityStep=currentActivityStep;

        MainSectionTitle updateStepExposedDataFieldsSectionTitle = new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_UpdateStepDataFieldsText"));
        addComponent(updateStepExposedDataFieldsSectionTitle);
        editDataFieldActionsBar=new SectionActionsBar(new Label(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_DefinitionText")+" : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(editDataFieldActionsBar);

        activityExposedDataFieldsEditor=new ActivityExposedDataFieldsEditor(this.currentUserClientInfo,
                ActivityExposedDataFieldsEditor.DATAFIELDS_TYPE_ACTIYITYSTEP,this.currentActivityStep.getActivityStepName(),this.currentActivityStep);
        activityExposedDataFieldsEditor.setActivityDataFieldsList(this.activityDataFieldsList);
        addComponent(activityExposedDataFieldsEditor);

        HorizontalLayout buttonsBarLayout=new HorizontalLayout();
        addComponent(buttonsBarLayout);
        this.setComponentAlignment(buttonsBarLayout, Alignment.MIDDLE_CENTER);

        Button confirmButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_ConfirmChangeButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateExposedActivityDataFields();
            }
        });
        confirmButton.setIcon(FontAwesome.CHECK);
        confirmButton.addStyleName("primary");
        buttonsBarLayout.addComponent(confirmButton);

        HorizontalLayout divLayout=new HorizontalLayout();
        divLayout.setWidth("15px");
        buttonsBarLayout.addComponent(divLayout);

        Button cancelButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_ResetButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                resetExposedActivityDataFields();
            }
        });
        cancelButton.setIcon(FontAwesome.TIMES);
        buttonsBarLayout.addComponent(cancelButton);
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
            editDataFieldActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        activityExposedDataFieldsEditor.setExposedDataFields(this.currentActivityStep.getExposedActivityDataFields());
    }

    private void resetExposedActivityDataFields(){
        activityExposedDataFieldsEditor.setExposedDataFields(this.currentActivityStep.getExposedActivityDataFields());
    }

    private void updateExposedActivityDataFields(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String currentActivitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        final String activityType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        final String activityStep=this.currentActivityStep.getActivityStepName();

        final ActivityStepExposedDataFieldsEditor self=this;
        Label confirmMessage = new Label(FontAwesome.INFO.getHtml() +" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ConfirmUpdateStepDataFieldsText")+
                " <b>" + activityType + "</b>.", ContentMode.HTML);
        final ConfirmDialog updateDataFieldConfirmDialog = new ConfirmDialog();
        updateDataFieldConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                updateDataFieldConfirmDialog.close();
                if(self.containerDialog!=null){
                    self.containerDialog.close();
                }
                List<ActivityDataFieldVO> exposedActivityDataFieldsList=activityExposedDataFieldsEditor.getExposedDataFields();
                boolean setStepDataFieldDefinitionsResult=ActivitySpaceOperationUtil.
                        setActivityTypeExposedStepDataFieldDefinitions(currentActivitySpaceName, activityType, activityStep, exposedActivityDataFieldsList);
                if(setStepDataFieldDefinitionsResult){
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_ActivityTypeManagement_UpdateStepDataFieldsSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    self.currentActivityStep.setExposedActivityDataFields(exposedActivityDataFieldsList);
                    self.relatedActivityStepItem.loadExposedActivityDataFields();
                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_UpdateStepDataFieldsErrorText"),
                            userI18NProperties.
                                    getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        updateDataFieldConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(updateDataFieldConfirmDialog);
    }

    public void setRelatedActivityStepItem(ActivityStepItem relatedActivityStepItem) {
        this.relatedActivityStepItem = relatedActivityStepItem;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    private boolean isExistDatafield(String fieldName){
        List<ActivityDataFieldVO> exposedDataFieldsList=this.currentActivityStep.getExposedActivityDataFields();
        if(exposedDataFieldsList==null){
            return false;
        }else{
            for(ActivityDataFieldVO currentDataField:exposedDataFieldsList){
                if(fieldName.equals(currentDataField.getDataFieldName())){
                    return true;
                }
            }
            return false;
        }
    }
}
