package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.common.ActivityStepDefinition;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.RoleVO;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.*;

import static com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil.getActivityTypeDefinedSteps;

public class ActivityStepsEditor extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;

    private List<ActivityStepVO> activityStepsList;
    private AddNewExposedActivityStepPanel addNewExposedActivityStepPanel;
    private List<ActivityStepVO> currentExposedActivityStepList;
    private ActivityStepItemsActionList activityStepItemsActionList;
    private List<ActivityDataFieldVO> activityDataFieldsList;
    private List<RoleVO> rolesList;

    public ActivityStepsEditor(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        SectionActionsBar activityStepsSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.SLIDERS.getHtml() + " "+
                userI18NProperties.
                        getProperty("ActivityManagement_ActivityTypeManagement_ActivityStepPropertiesText")+":", ContentMode.HTML));
        addComponent(activityStepsSectionActionsBar);
        SectionActionButton addNewActivityStepActionButton = new SectionActionButton();
        addNewActivityStepActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ExposeActivityStepButtonLabel"));
        addNewActivityStepActionButton.setIcon(FontAwesome.PLUS_SQUARE);
        activityStepsSectionActionsBar.addActionComponent(addNewActivityStepActionButton);

        addNewExposedActivityStepPanel=new AddNewExposedActivityStepPanel(this.currentUserClientInfo);
        addNewExposedActivityStepPanel.setRelatedActivityStepsEditor(this);
        addNewActivityStepActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(670.0f, Unit.PIXELS);
                window.setHeight(290.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(addNewExposedActivityStepPanel);
                addNewExposedActivityStepPanel.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });
        activityStepItemsActionList=new ActivityStepItemsActionList(this.currentUserClientInfo,null,true);
        activityStepItemsActionList.setContainerActivityStepsEditor(this);
        addComponent(activityStepItemsActionList);
        setActivityDefinitionStepData();
        activityStepItemsActionList.setCurrentExposedActivityStepList(this.currentExposedActivityStepList);
    }

    @Override
    public void attach() {
        super.attach();
    }

    private void setActivityDefinitionStepData(){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        BusinessActivityDefinition targetActivityType= ActivitySpaceOperationUtil.getActivityTypeDetailInfo(activitySpaceName, activityDefinitionType);
        ActivityStepDefinition[] definedStepsArray=getActivityTypeDefinedSteps(activitySpaceName, activityDefinitionType);
        Map<String,ActivityStepDefinition> definedStepInfoMap=new HashMap<>();
        List<ActivityStepVO> activityStepList=new ArrayList<ActivityStepVO>();
        for(ActivityStepDefinition currentActivityStepDefinition:definedStepsArray){
            ActivityStepVO currentActivityStepVO=new ActivityStepVO();
            currentActivityStepVO.setActivityStepName(currentActivityStepDefinition.getStepId());
            currentActivityStepVO.setActivityStepDisplayName(currentActivityStepDefinition.getStepName());
            activityStepList.add(currentActivityStepVO);
            definedStepInfoMap.put(currentActivityStepDefinition.getStepId(),currentActivityStepDefinition);
        }
        setActivityStepsList(activityStepList);
        currentExposedActivityStepList=new ArrayList<ActivityStepVO>();
        String[] exposedStepsArray=targetActivityType.getExposedSteps();
        Map<String,DataFieldDefinition[]> exposedStepDataFieldDefinitionMap=targetActivityType.getActivityStepsExposedDataField();
        Map<String,String> stepRoleMap=targetActivityType.getActivityStepRoleMap();
        if(exposedStepsArray!=null){
            for(String currentStep:exposedStepsArray){
                if(definedStepInfoMap.get(currentStep)!=null){
                    ActivityStepVO currentActivityStepVO=new ActivityStepVO();
                    currentActivityStepVO.setActivityStepName(currentStep);
                    currentActivityStepVO.setActivityStepDisplayName(definedStepInfoMap.get(currentStep).getStepName());
                    List<ActivityDataFieldVO> voExposedActivityDataFields=new ArrayList<ActivityDataFieldVO>();
                    currentActivityStepVO.setExposedActivityDataFields(voExposedActivityDataFields);
                    if(stepRoleMap.get(currentStep)!=null){
                        currentActivityStepVO.setRelatedRole(stepRoleMap.get(currentStep));
                    }
                    if(targetActivityType.getStepUserIdentityAttributeName(currentStep)!=null){
                        currentActivityStepVO.setUserIdentityAttribute(targetActivityType.getStepUserIdentityAttributeName(currentStep));
                    }
                    if(targetActivityType.getStepProcessVariableList(currentStep)!=null){
                        currentActivityStepVO.setStepProcessVariables(targetActivityType.getStepProcessVariableList(currentStep));
                    }
                    if(targetActivityType.getStepDecisionPointAttributeName(currentStep)!=null){
                        currentActivityStepVO.setDecisionPointAttribute(targetActivityType.getStepDecisionPointAttributeName(currentStep));
                    }
                    if(targetActivityType.getStepDecisionPointChoiseList(currentStep)!=null){
                        currentActivityStepVO.setDecisionPointChooseOption(targetActivityType.getStepDecisionPointChoiseList(currentStep));
                    }
                    DataFieldDefinition[] stepDataFieldDefinitionsArray=exposedStepDataFieldDefinitionMap.get(currentStep);
                    if(stepDataFieldDefinitionsArray!=null){
                        for(DataFieldDefinition currentDataFieldDefinition:stepDataFieldDefinitionsArray){
                            ActivityDataFieldVO activityDataFieldVO0=new ActivityDataFieldVO();
                            activityDataFieldVO0.setDataFieldName(currentDataFieldDefinition.getFieldName());
                            activityDataFieldVO0.setDataFieldDisplayName(currentDataFieldDefinition.getDisplayName());
                            activityDataFieldVO0.setArrayField(currentDataFieldDefinition.isArrayField());
                            activityDataFieldVO0.setWritableField(currentDataFieldDefinition.isWriteableField());
                            activityDataFieldVO0.setReadableField(currentDataFieldDefinition.isReadableField());
                            activityDataFieldVO0.setMandatoryField(currentDataFieldDefinition.isMandatoryField());
                            String dataType=ActivitySpaceOperationUtil.getDataFieldDefinitionTypeString(currentDataFieldDefinition.getFieldType());
                            activityDataFieldVO0.setDataType(dataType);
                            voExposedActivityDataFields.add(activityDataFieldVO0);
                        }
                    }
                    currentExposedActivityStepList.add(currentActivityStepVO);
                }
            }
        }
    }

    public void setActivityStepsList(List<ActivityStepVO> activityStepsList) {
        this.activityStepsList = activityStepsList;
        addNewExposedActivityStepPanel.setActivityStepsList(this.activityStepsList);
    }

    public void addNewExposedActivityStep(String activityStepName,String relatedRoleName){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        boolean addNewExposedStepResult=
                ActivitySpaceOperationUtil.addActivityTypeExposedStep(activitySpaceName,activityDefinitionType,activityStepName,relatedRoleName);
        if(addNewExposedStepResult){
            Notification resultNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_AddDataSuccessText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_ExposeActivityStepSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
            ActivityStepVO newActivityStepInfo=getActivityStepDefinitionByName(activityStepName);
            ActivityStepVO newStepVO=new ActivityStepVO();
            newStepVO.setActivityStepName(newActivityStepInfo.getActivityStepName());
            newStepVO.setActivityStepDisplayName(newActivityStepInfo.getActivityStepDisplayName());
            newStepVO.setRelatedRole(relatedRoleName);
            if(newActivityStepInfo!=null){
                getActivityStepItemsActionList().addActivityStep(newStepVO);
            }
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_ExposeActivityStepErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    public boolean removeExposedActivityStep(String activityStepName){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        return ActivitySpaceOperationUtil.removeActivityTypeExposedStep(activitySpaceName,activityDefinitionType,activityStepName);
    }

    private ActivityStepVO getActivityStepDefinitionByName(String activityStepName){
        for(ActivityStepVO activityStep :activityStepsList){
            if(activityStep.getActivityStepName().equals(activityStepName)){
                return activityStep;
            }
        }
        return null;
    }

    public ActivityStepVO getExistActivityStepByName(String activityStepName){
        for(ActivityStepVO activityStep :currentExposedActivityStepList){
            if(activityStep.getActivityStepName().equals(activityStepName)){
                return activityStep;
            }
        }
        return null;
    }

    public void setActivityDataFieldsList(List<ActivityDataFieldVO> activityDataFieldsList) {
        this.activityDataFieldsList = activityDataFieldsList;
    }

    public ActivityStepItemsActionList getActivityStepItemsActionList() {
        return activityStepItemsActionList;
    }

    public void setRolesList(List<RoleVO> rolesList) {
        this.rolesList = rolesList;
    }

    public List<RoleVO> getRolesList() {
        return rolesList;
    }

    public void refreshUIData(){
        //update data fields info in case data fields updated in Activity Definition Data Fields tab
        if(currentExposedActivityStepList!=null){
            for(ActivityStepVO currentActivityStep:currentExposedActivityStepList){
                List<ActivityDataFieldVO> stepExposedDataFieldList=currentActivityStep.getExposedActivityDataFields();
                if(stepExposedDataFieldList!=null){
                    for(ActivityDataFieldVO currentActivityDataFieldVO:stepExposedDataFieldList){
                        syncDataFieldInfo(currentActivityDataFieldVO);
                    }
                }
            }
        }
    }

    private void syncDataFieldInfo(ActivityDataFieldVO activityDataFieldToSync){
        if(activityDataFieldsList!=null){
            for(ActivityDataFieldVO currentActivityDataField:activityDataFieldsList){
                if(currentActivityDataField.getDataFieldName().equals(activityDataFieldToSync.getDataFieldName())){
                    activityDataFieldToSync.setDescription(currentActivityDataField.getDescription());
                    activityDataFieldToSync.setSystemField(currentActivityDataField.isSystemField());
                    activityDataFieldToSync.setArrayField(currentActivityDataField.isArrayField());
                    activityDataFieldToSync.setDataFieldDisplayName(currentActivityDataField.getDataFieldDisplayName());
                    activityDataFieldToSync.setDataType(currentActivityDataField.getDataType());
                    return;
                }
            }
        }
    }

    public void cleanActivityDefinitionProcessRelatedUIData(){
        currentExposedActivityStepList.clear();
        activityStepItemsActionList.renderActivityStepItems();
    }
}
