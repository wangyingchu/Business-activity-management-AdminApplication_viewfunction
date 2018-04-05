package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityExposedDataFieldsEditor;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ParticipantVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.RoleVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.RosterVO;
import com.viewfunction.vfbam.ui.component.common.*;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.*;

public class ActivityDefinitionEditor extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private FormLayout basicPropertyForm;
    private FormLayout decisionPointForm;
    private TextField activityType;
    private ComboBox relatedRosterChooseList;
    private OptionGroup isEnabled;
    private TextField launchUserIdentityAttribute;
    private TwinColSelect allowedStartActivityRolesSelect;
    private TwinColSelect allowedStartActivityParticipantsSelect;
    private TwinColSelect startActivityVariablesSelect;
    private TextField launchDecisionPointAttribute;
    private SectionActionButton addDecisionOptionActionButton;
    private PropertyValuesActionTable propertyValuesActionTable;
    private ActivityExposedDataFieldsEditor activityExposedDataFieldsEditor;
    private TextArea activityTypeDescription;
    private TwinColSelect businessCategoriesSelect;

    private String currentActivityType;
    private String currentRoster;
    private String currentIsEnabled;
    private String currentStartUserIdentityAttr;
    private String[] currentAllowedStartActivityRoleList;
    private String[] currentAllowedStartActivityParticipantList;
    private String[] currentStartActivityVariableList;
    private String currentLaunchDecisionPointAttr;
    private String[] currentLaunchDecisionPointChooseOption;
    private HorizontalLayout operationButtonsLayout;
    private List<ActivityDataFieldVO> currentStartPointExposedDataFieldList;
    private String currentActivityTypeDescription;
    private String[] currentActivityTypeBusinessCategories;

    private Button updateButton;
    private Button cancelButton;
    private Button saveButton;

    private List<ParticipantVO> participantsList;
    private Map<String,ParticipantVO> participantsInfoMap;

    private List<RoleVO> rolesList;
    private Map<String,RoleVO> rolesInfoMap;

    private List<RosterVO> rostersList;
    private Map<String,RosterVO> rostersInfoMap;

    private List<ActivityDataFieldVO> activityDataFieldsList;
    private Map<String,ActivityDataFieldVO> activityDataFieldsInfoMap;

    private String[] businessCategories;

    private boolean isReadMode;

    public ActivityDefinitionEditor(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.isReadMode=true;
        participantsInfoMap=new HashMap<String,ParticipantVO>();
        rolesInfoMap=new HashMap<String,RoleVO>();
        rostersInfoMap=new HashMap<String,RosterVO>();
        activityDataFieldsInfoMap=new HashMap<String,ActivityDataFieldVO>();

        basicPropertyForm = new FormLayout();
        basicPropertyForm.setMargin(false);
        basicPropertyForm.setWidth("100%");
        basicPropertyForm.addStyleName("light");
        addComponent(basicPropertyForm);

        setActivityDefinitionData();

        activityType = new TextField(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_TypePropertyText"));
        activityType.setWidth("100%");
        activityType.setRequired(true);
        basicPropertyForm.addComponent(activityType);

        activityTypeDescription = new TextArea(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_TypeDescPropertyText"));
        activityTypeDescription.setWidth("100%");
        activityTypeDescription.setRows(2);
        basicPropertyForm.addComponent(activityTypeDescription);

        relatedRosterChooseList = new ComboBox(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_TypeRosterPropertyText"));
        relatedRosterChooseList.setWidth("100%");
        relatedRosterChooseList.setTextInputAllowed(false);
        relatedRosterChooseList.setNullSelectionAllowed(true);
        relatedRosterChooseList.setInputPrompt(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_SelectRosterText"));
        basicPropertyForm.addComponent(relatedRosterChooseList);

        isEnabled = new OptionGroup(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_IsEnablePropertyText"));
        isEnabled.addItem("true");
        isEnabled.addItem("false");
        isEnabled.addStyleName("horizontal");
        basicPropertyForm.addComponent(isEnabled);

        launchUserIdentityAttribute = new TextField(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StarterIDAttributePropertyText"));
        launchUserIdentityAttribute.setRequired(false);
        launchUserIdentityAttribute.setWidth("100%");
        basicPropertyForm.addComponent(launchUserIdentityAttribute);

        allowedStartActivityRolesSelect = new TwinColSelect(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AllowedStartActivityRolesText"));
        allowedStartActivityRolesSelect.setLeftColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AvailableStartActivityRolesLabel"));
        allowedStartActivityRolesSelect.setRightColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AllowedStartActivityRolesLabel"));
        allowedStartActivityRolesSelect.setNewItemsAllowed(false);
        allowedStartActivityRolesSelect.setWidth("80%");
        allowedStartActivityRolesSelect.setHeight("150px");
        addComponent(allowedStartActivityRolesSelect);
        allowedStartActivityRolesSelect.addStyleName("ui_appElementMiddleMargin");
        basicPropertyForm.addComponent(allowedStartActivityRolesSelect);

        allowedStartActivityParticipantsSelect = new TwinColSelect(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AllowedStartActivityParticipantsText"));
        allowedStartActivityParticipantsSelect.setLeftColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AvailableStartActivityParticipantsLabel"));
        allowedStartActivityParticipantsSelect.setRightColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AllowedStartActivityParticipantsLabel"));
        allowedStartActivityParticipantsSelect.setNewItemsAllowed(false);
        allowedStartActivityParticipantsSelect.setWidth("80%");
        allowedStartActivityParticipantsSelect.setHeight("150px");
        addComponent(allowedStartActivityParticipantsSelect);
        allowedStartActivityParticipantsSelect.addStyleName("ui_appElementMiddleMargin");
        basicPropertyForm.addComponent(allowedStartActivityParticipantsSelect);

        startActivityVariablesSelect = new TwinColSelect(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StartActivityVariablesText"));
        startActivityVariablesSelect.setLeftColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AvailableStartActivityVariablesLabel"));
        startActivityVariablesSelect.setRightColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AllowedStartActivityVariablesLabel"));
        startActivityVariablesSelect.setNewItemsAllowed(false);
        startActivityVariablesSelect.setWidth("80%");
        startActivityVariablesSelect.setHeight("150px");
        addComponent(startActivityVariablesSelect);
        startActivityVariablesSelect.addStyleName("ui_appElementMiddleMargin");
        basicPropertyForm.addComponent(startActivityVariablesSelect);

        businessCategoriesSelect = new TwinColSelect(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ActivityCategoriesText"));
        businessCategoriesSelect.setLeftColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AvailableCategoriesLabel"));
        businessCategoriesSelect.setRightColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_CurrentCategoriesLabel"));
        businessCategoriesSelect.setNewItemsAllowed(false);
        businessCategoriesSelect.setWidth("80%");
        businessCategoriesSelect.setHeight("150px");
        addComponent(businessCategoriesSelect);
        businessCategoriesSelect.addStyleName("ui_appElementMiddleMargin");
        basicPropertyForm.addComponent(businessCategoriesSelect);

        SecondarySectionTitle secondarySectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_LaunchDecisionPointAttributeText"));
        addComponent(secondarySectionTitle);

        decisionPointForm = new FormLayout();
        decisionPointForm.setMargin(false);
        decisionPointForm.setSpacing(false);
        decisionPointForm.setWidth("100%");
        decisionPointForm.addStyleName("light");
        addComponent(decisionPointForm);

        launchDecisionPointAttribute = new TextField(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_LaunchDecisionPointAttributeNameLabel"));
        launchDecisionPointAttribute.setInputPrompt(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_InputDecisionAttributeNamePromptText"));
        launchDecisionPointAttribute.setRequired(false);
        launchDecisionPointAttribute.setWidth("100%");
        decisionPointForm.addComponent(launchDecisionPointAttribute);

        SectionActionsBar launchDecisionPointSectionActionsBar=new SectionActionsBar(new Label(FontAwesome.LIST.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_LaunchDecisionPointChooseOptionsLabel"), ContentMode.HTML));
        addComponent(launchDecisionPointSectionActionsBar);
        addDecisionOptionActionButton = new SectionActionButton();
        addDecisionOptionActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_LaunchDecisionPointAddOptionButtonLabel"));
        addDecisionOptionActionButton.setIcon(FontAwesome.PLUS_SQUARE);
        launchDecisionPointSectionActionsBar.addActionComponent(addDecisionOptionActionButton);
        propertyValuesActionTable=new PropertyValuesActionTable(this.currentUserClientInfo,"150",userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_LaunchDecisionPointOptionsText"),userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_LaunchDecisionPointChooseOptionsLabel"),false,false);
        addComponent(propertyValuesActionTable);

        addDecisionOptionActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                propertyValuesActionTable.addPropertyValue();
            }
        });

        SecondarySectionTitle launchPointExposedDataFieldsSectionTitle2=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_LaunchDecisionPointDataFieldsText"));
        addComponent(launchPointExposedDataFieldsSectionTitle2);

        activityExposedDataFieldsEditor=new ActivityExposedDataFieldsEditor(this.currentUserClientInfo,ActivityExposedDataFieldsEditor.DATAFIELDS_TYPE_STARTPOINT,null,null);
        addComponent(activityExposedDataFieldsEditor);

        final FormLayout editorActionOperationButtonsForm = new FormLayout();
        editorActionOperationButtonsForm.setMargin(false);
        editorActionOperationButtonsForm.setSpacing(false);
        editorActionOperationButtonsForm.setWidth("100%");
        editorActionOperationButtonsForm.addStyleName("light");

        operationButtonsLayout = new HorizontalLayout();
        operationButtonsLayout.setMargin(new MarginInfo(true, false, true, false));
        operationButtonsLayout.setSpacing(true);
        operationButtonsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        editorActionOperationButtonsForm.addComponent(operationButtonsLayout);

        updateButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_UpdateButtonLabel"));
        updateButton.setIcon(FontAwesome.HAND_O_RIGHT);

        cancelButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_CancelButtonLabel"));
        cancelButton.setIcon(FontAwesome.TIMES);

        saveButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_SaveButtonLabel"));
        saveButton.setIcon(FontAwesome.SAVE);
        saveButton.addStyleName("primary");

        updateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                isReadMode=false;
                enableEdit();
                operationButtonsLayout.removeAllComponents();
                operationButtonsLayout.addComponent(saveButton);
                operationButtonsLayout.addComponent(cancelButton);
            }
        });

        cancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                isReadMode=true;
                setCurrentEditorValues();
                disableEdit();
                operationButtonsLayout.removeAllComponents();
                operationButtonsLayout.addComponent(updateButton);
            }
        });

        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                saveActivityDefinitionData();
            }
        });

        operationButtonsLayout.addComponent(updateButton);
        addComponent(editorActionOperationButtonsForm);

        VerticalLayout spacingDivLayout=new VerticalLayout();
        spacingDivLayout.setHeight(40,Unit.PIXELS);
        addComponent(spacingDivLayout);
    }

    @Override
    public void attach() {
        super.attach();
        loadActivityTypeGlobalData();
        setCurrentEditorValues();
        disableEdit();
    }

    private void setActivityDefinitionData(){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        BusinessActivityDefinition targetActivityType= ActivitySpaceOperationUtil.getActivityTypeDetailInfo(activitySpaceName, activityDefinitionType);

        currentActivityType=targetActivityType.getActivityType();
        currentActivityTypeDescription=targetActivityType.getActivityDescription();
        currentActivityTypeBusinessCategories=targetActivityType.getActivityCategories();
        currentRoster=targetActivityType.getRosterName();
        currentIsEnabled=""+targetActivityType.isEnabled();
        currentStartUserIdentityAttr= targetActivityType.getLaunchUserIdentityAttributeName();
        currentAllowedStartActivityRoleList=targetActivityType.getActivityLaunchRoles();
        currentAllowedStartActivityParticipantList=targetActivityType.getActivityLaunchParticipants();
        currentStartActivityVariableList=targetActivityType.getLaunchProcessVariableList();
        currentLaunchDecisionPointAttr=targetActivityType.getLaunchDecisionPointAttributeName();
        currentLaunchDecisionPointChooseOption=targetActivityType.getLaunchDecisionPointChoiseList();

        currentStartPointExposedDataFieldList=new ArrayList<ActivityDataFieldVO>();
        try {
            DataFieldDefinition[] launchPointDataFields=targetActivityType.getLaunchPointExposedDataFields();
            if(launchPointDataFields!=null){
                for(DataFieldDefinition currentDataFieldDefinition:launchPointDataFields){
                    ActivityDataFieldVO currentActivityDataFieldVO=new ActivityDataFieldVO();
                    currentActivityDataFieldVO.setDataFieldDisplayName(currentDataFieldDefinition.getDisplayName());
                    currentActivityDataFieldVO.setDescription(currentDataFieldDefinition.getDescription());
                    currentActivityDataFieldVO.setDataFieldName(currentDataFieldDefinition.getFieldName());
                    String dataFieldType=ActivitySpaceOperationUtil.getDataFieldDefinitionTypeString(currentDataFieldDefinition.getFieldType());
                    currentActivityDataFieldVO.setDataType(dataFieldType);
                    currentActivityDataFieldVO.setArrayField(currentDataFieldDefinition.isArrayField());
                    currentActivityDataFieldVO.setReadableField(currentDataFieldDefinition.isReadableField());
                    currentActivityDataFieldVO.setWritableField(currentDataFieldDefinition.isWriteableField());
                    currentActivityDataFieldVO.setMandatoryField(currentDataFieldDefinition.isMandatoryField());
                    currentStartPointExposedDataFieldList.add(currentActivityDataFieldVO);
                }
            }
        } catch (ActivityEngineDataException e) {
            e.printStackTrace();
        }
    }

    private void loadActivityTypeGlobalData(){
        if(participantsList!=null){
            for(ParticipantVO currentParticipantVO:participantsList){
                String participantCombinationStr=currentParticipantVO.getParticipantName()+" ("+currentParticipantVO.getParticipantDisplayName()+")";
                allowedStartActivityParticipantsSelect.addItem(participantCombinationStr);
                participantsInfoMap.put(participantCombinationStr, currentParticipantVO);
            }
        }
        if(rolesList!=null){
            for(RoleVO currentRoleVO:rolesList){
                String roleCombinationStr=currentRoleVO.getRoleName()+" ("+currentRoleVO.getRoleDisplayName()+")";
                allowedStartActivityRolesSelect.addItem(roleCombinationStr);
                rolesInfoMap.put(roleCombinationStr,currentRoleVO);
            }
        }
        if(rostersList!=null){
            for(RosterVO currentRosterVO:rostersList){
                String rosterCombinationStr=currentRosterVO.getRosterName()+" ("+currentRosterVO.getRosterDisplayName()+")";
                relatedRosterChooseList.addItem(rosterCombinationStr);
                rostersInfoMap.put(rosterCombinationStr,currentRosterVO);
            }
        }
        if(activityDataFieldsList!=null){
            for(ActivityDataFieldVO currentActivityDataFieldVO:activityDataFieldsList){
                String dataFieldCombinationStr=currentActivityDataFieldVO.getDataFieldName()+" ("+currentActivityDataFieldVO.getDataFieldDisplayName()+")";
                startActivityVariablesSelect.addItem(dataFieldCombinationStr);
                activityDataFieldsInfoMap.put(dataFieldCombinationStr,currentActivityDataFieldVO);
            }
        }
        if(businessCategories!=null){
            for(String currentCategory:businessCategories){
                businessCategoriesSelect.addItem(currentCategory);
            }
        }
    }

    private void setCurrentEditorValues() {
        activityType.setValue(currentActivityType);
        if(currentRoster!=null){
            String rosterCombinationStr=getRosterCombinationStrByName(currentRoster);
            if(rosterCombinationStr!=null){
                relatedRosterChooseList.select(rosterCombinationStr);
            }
        }else{
            relatedRosterChooseList.select(null);
        }
        if(currentIsEnabled!=null){
            isEnabled.select(currentIsEnabled);
        }
        if(currentStartUserIdentityAttr!=null){
            launchUserIdentityAttribute.setValue(currentStartUserIdentityAttr);
        }
        if(currentAllowedStartActivityRoleList!=null){
            allowedStartActivityRolesSelect.clear();
            for(String currentValue:currentAllowedStartActivityRoleList){
                String roleCombinationStr=getRoleCombinationStrByName(currentValue);
                if(roleCombinationStr!=null){
                    allowedStartActivityRolesSelect.select(roleCombinationStr);
                }
            }
        }
        if(currentAllowedStartActivityParticipantList!=null){
            allowedStartActivityParticipantsSelect.clear();
            for(String currentValue:currentAllowedStartActivityParticipantList){
                String participantCombinationStr=getParticipantCombinationStrByName(currentValue);
                if(participantCombinationStr!=null){
                    allowedStartActivityParticipantsSelect.select(participantCombinationStr);
                }
            }
        }
        if(currentStartActivityVariableList!=null){
            startActivityVariablesSelect.clear();
            for(String currentValue:currentStartActivityVariableList){
                String activityDataFieldCombinationStr=getActivityDataFieldCombinationStrByName(currentValue);
                if(activityDataFieldCombinationStr!=null){
                    startActivityVariablesSelect.select(activityDataFieldCombinationStr);
                }
            }
        }
        if(currentLaunchDecisionPointAttr != null) {
            launchDecisionPointAttribute.setValue(currentLaunchDecisionPointAttr);
        }
        if(currentLaunchDecisionPointChooseOption!=null) {
            propertyValuesActionTable.setPropertyValues(currentLaunchDecisionPointChooseOption);
        }
        if(currentStartPointExposedDataFieldList!=null){
            activityExposedDataFieldsEditor.setExposedDataFields(currentStartPointExposedDataFieldList);
        }
        if(currentActivityTypeDescription !=null){
            activityTypeDescription.setValue(currentActivityTypeDescription);
        }
        if(currentActivityTypeBusinessCategories!=null){
            businessCategoriesSelect.clear();
            for(String currentValue:currentActivityTypeBusinessCategories){
                businessCategoriesSelect.select(currentValue);
            }
        }
    }

    private String getParticipantCombinationStrByName(String particpantName){
        if(participantsList!=null){
            for(ParticipantVO currentParticipantVO:participantsList){
                if(currentParticipantVO.getParticipantName().equals(particpantName)){
                    return currentParticipantVO.getParticipantName()+" ("+currentParticipantVO.getParticipantDisplayName()+")";
                }
            }
        }
        return null;
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

    private String getRosterCombinationStrByName(String rosterName){
        if(rostersList!=null){
            for(RosterVO currentRosterVO:rostersList){
                if(currentRosterVO.getRosterName().equals(rosterName)){
                    return currentRosterVO.getRosterName()+" ("+currentRosterVO.getRosterDisplayName()+")";
                }
            }
        }
        return null;
    }

    private String getActivityDataFieldCombinationStrByName(String dataFieldName){
        if(activityDataFieldsList!=null){
            for(ActivityDataFieldVO currentActivityDataFieldVO:activityDataFieldsList){
                if(currentActivityDataFieldVO.getDataFieldName().equals(dataFieldName)){
                    return currentActivityDataFieldVO.getDataFieldName()+" ("+currentActivityDataFieldVO.getDataFieldDisplayName()+")";
                }
            }
        }
        return null;
    }

    private void disableEdit(){
        basicPropertyForm.addStyleName("light");
        decisionPointForm.addStyleName("light");
        activityType.setEnabled(false);
        relatedRosterChooseList.setEnabled(false);
        isEnabled.setEnabled(false);
        launchUserIdentityAttribute.setEnabled(false);
        allowedStartActivityRolesSelect.setEnabled(false);
        allowedStartActivityParticipantsSelect.setEnabled(false);
        startActivityVariablesSelect.setEnabled(false);
        launchDecisionPointAttribute.setEnabled(false);
        addDecisionOptionActionButton.setEnabled(false);
        propertyValuesActionTable.disableTableFullEdit();
        getActivityExposedDataFieldsEditor().disableEdit();
        activityTypeDescription.setEnabled(false);
        businessCategoriesSelect.setEnabled(false);
    }

    private void enableEdit(){
        basicPropertyForm.removeStyleName("light");
        decisionPointForm.removeStyleName("light");
        relatedRosterChooseList.setEnabled(true);
        isEnabled.setEnabled(true);
        launchUserIdentityAttribute.setEnabled(true);
        startActivityVariablesSelect.setEnabled(true);
        allowedStartActivityRolesSelect.setEnabled(true);
        allowedStartActivityParticipantsSelect.setEnabled(true);
        launchDecisionPointAttribute.setEnabled(true);
        addDecisionOptionActionButton.setEnabled(true);
        propertyValuesActionTable.enableTableFullEdit();
        getActivityExposedDataFieldsEditor().enableEdit();
        activityTypeDescription.setEnabled(true);
        businessCategoriesSelect.setEnabled(true);
    }

    public void refreshUIData(){
        //update data fields info in case data fields updated in Activity Definition Data Fields tab
        startActivityVariablesSelect.removeAllItems();
        if(activityDataFieldsList!=null){
            activityDataFieldsInfoMap.clear();
            startActivityVariablesSelect.removeAllItems();
            for(ActivityDataFieldVO currentActivityDataFieldVO:activityDataFieldsList){
                String dataFieldCombinationStr=currentActivityDataFieldVO.getDataFieldName()+" ("+currentActivityDataFieldVO.getDataFieldDisplayName()+")";
                startActivityVariablesSelect.addItem(dataFieldCombinationStr);
                activityDataFieldsInfoMap.put(dataFieldCombinationStr,currentActivityDataFieldVO);
            }
        }
        if(currentStartActivityVariableList!=null){
            startActivityVariablesSelect.clear();
            for(String currentValue:currentStartActivityVariableList){
                String activityDataFieldCombinationStr=getActivityDataFieldCombinationStrByName(currentValue);
                if(activityDataFieldCombinationStr!=null){
                    startActivityVariablesSelect.select(activityDataFieldCombinationStr);
                }
            }
        }
        if(isReadMode){
            startActivityVariablesSelect.setEnabled(false);
        }else{
            startActivityVariablesSelect.setEnabled(true);
        }
        if(currentStartPointExposedDataFieldList!=null){
            for(ActivityDataFieldVO currentActivityDataFieldVO:currentStartPointExposedDataFieldList){
                syncDataFieldInfo(currentActivityDataFieldVO);
            }
            activityExposedDataFieldsEditor.setExposedDataFields(currentStartPointExposedDataFieldList);
            if(isReadMode){
                activityExposedDataFieldsEditor.disableEdit();
            }else{
                activityExposedDataFieldsEditor.enableEdit();
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
                    activityDataFieldToSync.setMandatoryField(currentActivityDataField.isMandatoryField());
                    activityDataFieldToSync.setDataFieldDisplayName(currentActivityDataField.getDataFieldDisplayName());
                    activityDataFieldToSync.setDataType(currentActivityDataField.getDataType());
                    return;
                }
            }
        }
    }

    private void saveActivityDefinitionData(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        final String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ConfirmUpdateActivityTypePart1Text")+
                " <b>"+activityDefinitionType +"</b>"+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ConfirmUpdateActivityTypePart2Text"), ContentMode.HTML);
        final ConfirmDialog updatePropertyValueConfirmDialog = new ConfirmDialog();
        updatePropertyValueConfirmDialog.setConfirmMessage(confirmMessage);
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //do update change
                if(relatedRosterChooseList.getValue()!=null){
                    String rosterName=rostersInfoMap.get(relatedRosterChooseList.getValue().toString()).getRosterName();
                    currentRoster=rosterName;
                }else{
                    currentRoster=null;
                }

                currentIsEnabled=isEnabled.getValue().toString();
                currentStartUserIdentityAttr=launchUserIdentityAttribute.getValue();

                Set allowedStartActivityRolesSet=(Set)allowedStartActivityRolesSelect.getValue();
                String[] strRoleArray = new String[allowedStartActivityRolesSet.size()];
                String[] roleCombinationStrArray=(String[]) allowedStartActivityRolesSet.toArray(strRoleArray);
                currentAllowedStartActivityRoleList = new String[roleCombinationStrArray.length];
                for(int i=0;i<roleCombinationStrArray.length;i++){
                    String roleName=rolesInfoMap.get(roleCombinationStrArray[i]).getRoleName();
                    currentAllowedStartActivityRoleList[i]=roleName;
                }

                Set allowedStartActivityParticipantsSet=(Set)allowedStartActivityParticipantsSelect.getValue();
                String[] strParticipantArray=new String[allowedStartActivityParticipantsSet.size()];
                String[] participantCombinationStrArray=(String[]) allowedStartActivityParticipantsSet.toArray(strParticipantArray);
                currentAllowedStartActivityParticipantList = new String[participantCombinationStrArray.length];
                for(int i=0;i<participantCombinationStrArray.length;i++){
                    String participantName=participantsInfoMap.get(participantCombinationStrArray[i]).getParticipantName();
                    currentAllowedStartActivityParticipantList[i]=participantName;
                }

                Set startActivityVariablesSet=(Set)startActivityVariablesSelect.getValue();
                String[] strActivityDataArray = new String[startActivityVariablesSet.size()];
                String[] startActivityVariableCombinationStrArray=(String[]) startActivityVariablesSet.toArray(strActivityDataArray);
                currentStartActivityVariableList=new String[startActivityVariableCombinationStrArray.length];
                for(int i=0;i<startActivityVariableCombinationStrArray.length;i++){
                    String dataFieldName=activityDataFieldsInfoMap.get(startActivityVariableCombinationStrArray[i]).getDataFieldName();
                    currentStartActivityVariableList[i]=dataFieldName;
                }

                currentLaunchDecisionPointAttr=launchDecisionPointAttribute.getValue();
                currentLaunchDecisionPointChooseOption=propertyValuesActionTable.getPropertyValues();
                currentStartPointExposedDataFieldList=activityExposedDataFieldsEditor.getExposedDataFields();
                currentActivityTypeDescription=activityTypeDescription.getValue();

                Set activityTypeBusinessCategorySet=(Set)businessCategoriesSelect.getValue();
                String[] activityTypeBusinessCategoryArray=new String[activityTypeBusinessCategorySet.size()];
                activityTypeBusinessCategorySet.toArray(activityTypeBusinessCategoryArray);
                currentActivityTypeBusinessCategories=activityTypeBusinessCategoryArray;

                //close confirm dialog
                updatePropertyValueConfirmDialog.close();

                boolean updateActivityTypePropertiesResult=ActivitySpaceOperationUtil.setActivityTypeDefinitionProperties(
                        activitySpaceName,activityDefinitionType,currentActivityTypeDescription,currentRoster,
                        Boolean.parseBoolean(currentIsEnabled),currentStartUserIdentityAttr,currentAllowedStartActivityRoleList,
                        currentAllowedStartActivityParticipantList,  currentStartActivityVariableList,
                        currentActivityTypeBusinessCategories,currentLaunchDecisionPointAttr,
                        currentLaunchDecisionPointChooseOption,currentStartPointExposedDataFieldList);

                if(updateActivityTypePropertiesResult){
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_ActivityTypeManagement_UpdateActivityTypeSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    setCurrentEditorValues();
                    disableEdit();
                    operationButtonsLayout.removeAllComponents();
                    operationButtonsLayout.addComponent(updateButton);
                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_UpdateActivityTypeErrorText"),
                            userI18NProperties.
                                    getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        updatePropertyValueConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(updatePropertyValueConfirmDialog);
    }

    public void setParticipantsList(List<ParticipantVO> participantsList) {
        this.participantsList = participantsList;
    }

    public void setRolesList(List<RoleVO> rolesList) {
        this.rolesList = rolesList;
    }

    public void setRostersList(List<RosterVO> rostersList) {
        this.rostersList = rostersList;
    }

    public void setActivityDataFieldsList(List<ActivityDataFieldVO> activityDataFieldsList) {
        this.activityDataFieldsList = activityDataFieldsList;
    }

    public ActivityExposedDataFieldsEditor getActivityExposedDataFieldsEditor() {
        return activityExposedDataFieldsEditor;
    }

    public void setBusinessCategories(String[] businessCategories) {
        this.businessCategories = businessCategories;
    }

    public void cleanActivityDefinitionProcessRelatedUIData(){
        currentStartUserIdentityAttr=null;
        launchUserIdentityAttribute.setValue("");

        currentStartActivityVariableList=new String[]{};
        startActivityVariablesSelect.clear();

        currentLaunchDecisionPointAttr = null;
        launchDecisionPointAttribute.setValue("");

        currentLaunchDecisionPointChooseOption=new String[]{};
        propertyValuesActionTable.setPropertyValues(currentLaunchDecisionPointChooseOption);
    }
}
