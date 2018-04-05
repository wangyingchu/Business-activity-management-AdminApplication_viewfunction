package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.RoleVO;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.*;

public class ActivityStepProcessVariablesEditor extends VerticalLayout {
    private SectionActionsBar updateStepVariablesActionsBar;

    private UserClientInfo currentUserClientInfo;
    private ActivityStepVO currentActivityStep;
    private List<RoleVO> rolesList;
    private Window containerDialog;
    private TwinColSelect stepActivityVariablesSelect;
    private FormLayout form;
    private HorizontalLayout footer;
    private ComboBox relatedRoles;
    private TextField stepUserIdentityAttribute;
    private Map<String,RoleVO> rolesInfoMap;
    private Map<String,ActivityDataFieldVO> activityDataFieldsInfoMap;
    private String currentStepRole;
    private String currentStepUserIdentityAttribute;
    private String[] currentStepProcessVariablesList;

    public ActivityStepProcessVariablesEditor(UserClientInfo currentUserClientInfo,ActivityStepVO currentActivityStep,
                                              List<RoleVO> rolesList) {
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        this.currentActivityStep=currentActivityStep;
        this.rolesList = rolesList;

        rolesInfoMap=new HashMap<String, RoleVO>();
        activityDataFieldsInfoMap=new HashMap<String,ActivityDataFieldVO>();

        MainSectionTitle updateStepProcessVariablesSectionTitle = new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_UpdateStepProcessPropertiesText"));
        addComponent(updateStepProcessVariablesSectionTitle);
        updateStepVariablesActionsBar=new SectionActionsBar(new Label(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_DefinitionText")+" : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(updateStepVariablesActionsBar);

        form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        relatedRoles = new ComboBox(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepRoleText"));
        relatedRoles.setRequired(false);
        relatedRoles.setWidth("100%");
        relatedRoles.setTextInputAllowed(false);
        relatedRoles.setNullSelectionAllowed(true);
        relatedRoles.setInputPrompt(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_PleaseSelectStepRoleText"));
        form.addComponent(relatedRoles);

        stepUserIdentityAttribute = new TextField(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepUserIDAttributeNameText"));
        stepUserIdentityAttribute.setRequired(false);
        stepUserIdentityAttribute.setWidth("100%");
        form.addComponent(stepUserIdentityAttribute);

        stepActivityVariablesSelect = new TwinColSelect(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepProcessVariableListText"));
        stepActivityVariablesSelect.setLeftColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AvailableStepProcessVariablesText"));
        stepActivityVariablesSelect.setRightColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_SelectedStepProcessVariablesText"));
        stepActivityVariablesSelect.setNewItemsAllowed(false);
        stepActivityVariablesSelect.setWidth("700px");
        stepActivityVariablesSelect.setHeight("170px");
        addComponent(stepActivityVariablesSelect);
        stepActivityVariablesSelect.addStyleName("ui_appElementMiddleMargin");
        form.addComponent(stepActivityVariablesSelect);

        footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button confirmButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_ConfirmChangeButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateActivityStepProcessVariables();
            }
        });
        confirmButton.setIcon(FontAwesome.CHECK);
        confirmButton.addStyleName("primary");
        footer.addComponent(confirmButton);

        Button cancelButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_ResetButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setProcessVariablesValue();
            }
        });
        cancelButton.setIcon(FontAwesome.TIMES);
        footer.addComponent(cancelButton);
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
            updateStepVariablesActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        relatedRoles.clear();
        relatedRoles.removeAllItems();
        if(rolesList!=null){
            for(RoleVO currentRoleVO:rolesList){
                String roleCombinationStr=currentRoleVO.getRoleName()+" ("+currentRoleVO.getRoleDisplayName()+")";
                relatedRoles.addItem(roleCombinationStr);
                rolesInfoMap.put(roleCombinationStr, currentRoleVO);
            }
        }
        stepActivityVariablesSelect.clear();
        List<ActivityDataFieldVO> exposedActivityDataFieldsList=this.currentActivityStep.getExposedActivityDataFields();

        stepActivityVariablesSelect.clear();
        stepActivityVariablesSelect.removeAllItems();
        if(exposedActivityDataFieldsList!=null){
            for(ActivityDataFieldVO currentActivityDataFieldVO:exposedActivityDataFieldsList){
                String dataFieldCombinationStr=currentActivityDataFieldVO.getDataFieldName()+" ("+currentActivityDataFieldVO.getDataFieldDisplayName()+")";
                stepActivityVariablesSelect.addItem(dataFieldCombinationStr);
                activityDataFieldsInfoMap.put(dataFieldCombinationStr,currentActivityDataFieldVO);
            }
        }
        setProcessVariablesValue();
    }

    private void setProcessVariablesValue(){
        currentStepRole=currentActivityStep.getRelatedRole();
        if(currentStepRole!=null){
            String currentRelatedRoleCombinationStr=getRoleCombinationStrByName(currentStepRole);
            relatedRoles.select(currentRelatedRoleCombinationStr);
        }else{
            relatedRoles.select(null);
        }
        currentStepUserIdentityAttribute=currentActivityStep.getUserIdentityAttribute();
        if(currentStepUserIdentityAttribute!=null){
            stepUserIdentityAttribute.setValue(currentStepUserIdentityAttribute);
        }else{
            stepUserIdentityAttribute.setValue("");
        }
        currentStepProcessVariablesList=currentActivityStep.getStepProcessVariables();
        if(currentStepProcessVariablesList!=null){
            stepActivityVariablesSelect.clear();
            for(String currentValue:currentStepProcessVariablesList){
                String activityDataFieldCombinationStr=getActivityDataFieldCombinationStrByName(currentValue);
                if(activityDataFieldCombinationStr!=null){
                    stepActivityVariablesSelect.select(activityDataFieldCombinationStr);
                }
            }
        }else{
            stepActivityVariablesSelect.clear();
        }
    }

    private String getRoleCombinationStrByName(String roleName){
        if(rolesList!=null){
            for(RoleVO currentRoleVO:rolesList){
                if(currentRoleVO.getRoleName().equals(roleName)){
                    return currentRoleVO.getRoleName()+" ("+currentRoleVO.getRoleDisplayName()+")";
                }
            }
        }
        return null;
    }

    private String getActivityDataFieldCombinationStrByName(String dataFieldName){
        if(this.currentActivityStep.getExposedActivityDataFields()!=null){
            for(ActivityDataFieldVO currentActivityDataFieldVO:this.currentActivityStep.getExposedActivityDataFields()){
                if(currentActivityDataFieldVO.getDataFieldName().equals(dataFieldName)){
                    return currentActivityDataFieldVO.getDataFieldName()+" ("+currentActivityDataFieldVO.getDataFieldDisplayName()+")";
                }
            }
        }
        return null;
    }

    public void updateActivityStepProcessVariables(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        final String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        final String activityStep=currentActivityStep.getActivityStepName();
        if(relatedRoles.getValue()!=null){
            String roleName=rolesInfoMap.get(relatedRoles.getValue().toString()).getRoleName();
            currentStepRole=roleName;
        }else{
            currentStepRole=null;
        }
        if(!stepUserIdentityAttribute.getValue().equals("")){
            currentStepUserIdentityAttribute=stepUserIdentityAttribute.getValue();
        }else{
            currentStepUserIdentityAttribute=null;
        }
        Set startActivityVariablesSet=(Set)stepActivityVariablesSelect.getValue();
        String[] strActivityDataArray = new String[startActivityVariablesSet.size()];
        String[] startActivityVariableCombinationStrArray=(String[]) startActivityVariablesSet.toArray(strActivityDataArray);
        currentStepProcessVariablesList=new String[startActivityVariableCombinationStrArray.length];
        for(int i=0;i<startActivityVariableCombinationStrArray.length;i++){
            String dataFieldName=activityDataFieldsInfoMap.get(startActivityVariableCombinationStrArray[i]).getDataFieldName();
            currentStepProcessVariablesList[i]=dataFieldName;
        }

        final ActivityStepProcessVariablesEditor self=this;
        Label confirmMessage = new Label(FontAwesome.INFO.getHtml() +" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ConfirmUpdateStepProcessPropertiesText")+
                " <b>" + currentActivityStep.getActivityStepName() + "</b>.", ContentMode.HTML);
        final ConfirmDialog updateProcessVariablesConfirmDialog = new ConfirmDialog();
        updateProcessVariablesConfirmDialog.setConfirmMessage(confirmMessage);
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                updateProcessVariablesConfirmDialog.close();
                if(self.containerDialog!=null){
                    self.containerDialog.close();
                }
                boolean setStepInfoResult=ActivitySpaceOperationUtil.
                        setActivityTypeExposedStepInfo(activitySpaceName, activityDefinitionType, activityStep,
                                currentStepRole, currentStepUserIdentityAttribute, currentStepProcessVariablesList);
                if(setStepInfoResult){
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_ActivityTypeManagement_UpdateStepProcessPropertiesSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    self.currentActivityStep.setRelatedRole(currentStepRole);
                    self.currentActivityStep.setUserIdentityAttribute(currentStepUserIdentityAttribute);
                    self.currentActivityStep.setStepProcessVariables(currentStepProcessVariablesList);
                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_UpdateStepProcessPropertiesErrorText"),
                            userI18NProperties.
                                    getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        updateProcessVariablesConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(updateProcessVariablesConfirmDialog);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }
}
