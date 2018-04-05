package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.viewfunction.activityEngine.activityView.common.ActivityStepDefinition;
import com.viewfunction.activityEngine.activityView.common.CustomStructure;
import com.viewfunction.activityEngine.util.ActivitySpaceCommonConstant;
import com.viewfunction.vfbam.ui.component.common.*;
import com.viewfunction.vfbam.ui.util.UserClientInfo;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;

import java.util.*;

/**
 * Created by wangychu on 1/24/17.
 */
public class ActivityAdditionalConfigurationEditor extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private FormLayout stepProcessEditorForm;
    private HorizontalLayout stepProcessEditorOperationButtonsLayout;
    private Button updateProcessEditorsButton;
    private Button cancelProcessEditorsButton;
    private Button saveProcessEditorsButton;
    private Map<String,TextField> stepProcessEditorTextFieldMap;
    private Map<String,String> stepProcessEditorsMap;
    public static final String ConfigurationItemType_StepConfig="ConfigurationItemType_StepConfig";
    public static final String ConfigurationItemType_GlobalConfig="ConfigurationItemType_GlobalConfig";
    private ActivityDefinitionCustomConfigurationItemTable stepsConfigurationItemTable;
    private ActivityDefinitionCustomConfigurationItemTable globalConfigurationItemTable;
    private CustomStructure activityTypeCustomConfigItemRootStructure;
    //public static final String _launchPointStepNameId="Launch_Point_Logic_Step";

    public ActivityAdditionalConfigurationEditor(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        Properties userI18NProperties = this.currentUserClientInfo.getUserI18NProperties();
        stepProcessEditorTextFieldMap=new HashMap<>();
        SecondarySectionTitle processEditorSecondarySectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepProcessEditorsText"));
        addComponent(processEditorSecondarySectionTitle);
        stepProcessEditorForm = new FormLayout();
        stepProcessEditorForm.setMargin(false);
        stepProcessEditorForm.setWidth("100%");
        stepProcessEditorForm.addStyleName("light");
        addComponent(stepProcessEditorForm);

        HorizontalLayout processEditorButtonsLayout = new HorizontalLayout();
        addComponent(processEditorButtonsLayout);
        HorizontalLayout processEditorButtonsSpacingDivLayout= new HorizontalLayout();
        processEditorButtonsSpacingDivLayout.setWidth(200,Unit.PIXELS);
        processEditorButtonsLayout.addComponent(processEditorButtonsSpacingDivLayout);

        stepProcessEditorOperationButtonsLayout = new HorizontalLayout();
        stepProcessEditorOperationButtonsLayout.setMargin(new MarginInfo(true, false, true, false));
        stepProcessEditorOperationButtonsLayout.setSpacing(true);
        stepProcessEditorOperationButtonsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        processEditorButtonsLayout.addComponent(stepProcessEditorOperationButtonsLayout);

        updateProcessEditorsButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_UpdateButtonLabel"));
        updateProcessEditorsButton.setIcon(FontAwesome.HAND_O_RIGHT);
        updateProcessEditorsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setEditStepProcessEditorsFieldEnableStatus(true);
                stepProcessEditorOperationButtonsLayout.removeAllComponents();
                stepProcessEditorOperationButtonsLayout.addComponent(saveProcessEditorsButton);
                stepProcessEditorOperationButtonsLayout.addComponent(cancelProcessEditorsButton);
            }
        });
        stepProcessEditorOperationButtonsLayout.addComponent(updateProcessEditorsButton);

        cancelProcessEditorsButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_CancelButtonLabel"));
        cancelProcessEditorsButton.setIcon(FontAwesome.TIMES);
        cancelProcessEditorsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setCurrentStepEditorValues();
                setEditStepProcessEditorsFieldEnableStatus(false);
                stepProcessEditorOperationButtonsLayout.removeAllComponents();
                stepProcessEditorOperationButtonsLayout.addComponent(updateProcessEditorsButton);
            }
        });

        saveProcessEditorsButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_SaveButtonLabel"));
        saveProcessEditorsButton.setIcon(FontAwesome.SAVE);
        saveProcessEditorsButton.addStyleName("primary");
        saveProcessEditorsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateStepProcessEditorsInfo();
            }
        });

        VerticalLayout globalSpacingDiv0Layout=new VerticalLayout();
        addComponent(globalSpacingDiv0Layout);

        SecondarySectionTitle stepCustomConfigInfoSecondarySectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepsConfigurationItemText"));
        addComponent(stepCustomConfigInfoSecondarySectionTitle);
        SectionActionsBar activityStepsCustomConfigInfoSectionActionsBar=new SectionActionsBar(new Label( VaadinIcons.FORM.getHtml() + " "+
                userI18NProperties.
                        getProperty("ActivityManagement_ActivityTypeManagement_StepConfigurationInfoText")+":", ContentMode.HTML));
        addComponent(activityStepsCustomConfigInfoSectionActionsBar);

        stepsConfigurationItemTable=new ActivityDefinitionCustomConfigurationItemTable(this.currentUserClientInfo,"250px",userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepNameText"),false);
        stepsConfigurationItemTable.setConfigurationItemType(ConfigurationItemType_StepConfig);
        addComponent(stepsConfigurationItemTable);

        VerticalLayout globalSpacingDiv1Layout=new VerticalLayout();
        addComponent(globalSpacingDiv1Layout);

        SecondarySectionTitle activityGlobalCustomConfigInfoSecondarySectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_GlobalConfigurationItemText"));
        addComponent(activityGlobalCustomConfigInfoSecondarySectionTitle);
        SectionActionsBar activityGlobalCustomConfigInfoSectionActionsBar=new SectionActionsBar(new Label( VaadinIcons.FORM.getHtml() + " "+
                userI18NProperties.
                        getProperty("ActivityManagement_ActivityTypeManagement_GlobalConfigurationInfoText")+":", ContentMode.HTML));
        addComponent(activityGlobalCustomConfigInfoSectionActionsBar);
        SectionActionButton addNewActivityTypeConfigurationItemButton = new SectionActionButton();
        addNewActivityTypeConfigurationItemButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AddConfigItemButtonLabel"));
        addNewActivityTypeConfigurationItemButton.setIcon(FontAwesome.PLUS_SQUARE);
        activityGlobalCustomConfigInfoSectionActionsBar.addActionComponent(addNewActivityTypeConfigurationItemButton);
        addNewActivityTypeConfigurationItemButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                addNewActivityTypeConfigurationItem();
            }
        });

        globalConfigurationItemTable=new ActivityDefinitionCustomConfigurationItemTable(this.currentUserClientInfo,"250px",null,true);
        globalConfigurationItemTable.setConfigurationItemType(ConfigurationItemType_GlobalConfig);
        addComponent(globalConfigurationItemTable);
    }

    @Override
    public void attach() {
        super.attach();
        displayAdditionalConfigurationContent();
    }

    private void displayAdditionalConfigurationContent(){
        Properties userI18NProperties = this.currentUserClientInfo.getUserI18NProperties();
        stepProcessEditorForm.removeAllComponents();
        stepProcessEditorTextFieldMap.clear();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        stepProcessEditorsMap=
                ActivitySpaceOperationUtil.getActivityTypeStepProcessEditors(activitySpaceName,activityDefinitionType);
        List<String> stepConfigurationItemsList=new ArrayList<>();
        String launchPointProcessEditorStr=userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_LaunchPointProcessEditorText");
        TextField launchPointStepProcessEditorTextField = new TextField(launchPointProcessEditorStr);
        launchPointStepProcessEditorTextField.setWidth("100%");
        launchPointStepProcessEditorTextField.setRequired(false);
        if(stepProcessEditorsMap!=null){
            String currentStepEditor=stepProcessEditorsMap.get(ActivitySpaceCommonConstant.ActivityDefinition_launchPointStepProcessEditor);
            if(currentStepEditor!=null){
                launchPointStepProcessEditorTextField.setValue(currentStepEditor);
            }
        }
        launchPointStepProcessEditorTextField.setEnabled(false);
        stepProcessEditorForm.addComponent(launchPointStepProcessEditorTextField);
        stepProcessEditorTextFieldMap.put(ActivitySpaceCommonConstant.ActivityDefinition_launchPointStepProcessEditor,launchPointStepProcessEditorTextField);

        stepConfigurationItemsList.add(ActivitySpaceCommonConstant.ActivityDefinition_launchPointLogicStepId);
        ActivityStepDefinition[] definedStepsArray=ActivitySpaceOperationUtil.getActivityTypeDefinedSteps(activitySpaceName, activityDefinitionType);
        for(ActivityStepDefinition currentActivityStepDefinition:definedStepsArray){
            String activityStepCombinationStr=currentActivityStepDefinition.getStepId()+" ("+currentActivityStepDefinition.getStepName()+") "+userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_ProcessEditorText");
            TextField currentActivityStepProcessEditor = new TextField(activityStepCombinationStr);
            if(stepProcessEditorsMap!=null){
                String currentStepEditor=stepProcessEditorsMap.get(currentActivityStepDefinition.getStepId());
                if(currentStepEditor!=null){
                    currentActivityStepProcessEditor.setValue(currentStepEditor);
                }
            }
            currentActivityStepProcessEditor.setWidth("100%");
            currentActivityStepProcessEditor.setRequired(false);
            currentActivityStepProcessEditor.setEnabled(false);
            stepProcessEditorForm.addComponent(currentActivityStepProcessEditor);
            stepProcessEditorTextFieldMap.put(currentActivityStepDefinition.getStepId(),currentActivityStepProcessEditor);
            stepConfigurationItemsList.add(currentActivityStepDefinition.getStepId());
        }

        HorizontalLayout spacingDivLayout = new HorizontalLayout();
        spacingDivLayout.setHeight(5,Unit.PIXELS);
        stepProcessEditorForm.addComponent(spacingDivLayout);
        stepsConfigurationItemTable.loadConfigurationItemsData(stepConfigurationItemsList);

        activityTypeCustomConfigItemRootStructure=ActivitySpaceOperationUtil.getActivityDefinitionRootCustomConfigItem(activitySpaceName,activityDefinitionType);
        if(activityTypeCustomConfigItemRootStructure!=null){
            List<CustomStructure> subStructuresList=ActivitySpaceOperationUtil.getSubCustomConfigItemsList(activityTypeCustomConfigItemRootStructure);
            List<String> activityTypeConfigurationItemNamesList=new ArrayList<>();
            if(activityTypeConfigurationItemNamesList!=null){
                for(CustomStructure currentCustomStructure:subStructuresList){
                    activityTypeConfigurationItemNamesList.add(currentCustomStructure.getStructureName());
                }
            }
            globalConfigurationItemTable.setParentContainerConfigurationItem(activityTypeCustomConfigItemRootStructure);
            globalConfigurationItemTable.loadConfigurationItemsData(activityTypeConfigurationItemNamesList);
        }
    }

    private void setEditStepProcessEditorsFieldEnableStatus(boolean enableStatus){
        if(enableStatus){
            stepProcessEditorForm.removeStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        }else{
            stepProcessEditorForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        }
        Collection<TextField> editorCollection=stepProcessEditorTextFieldMap.values();
        Iterator<TextField> fieldIterator=editorCollection.iterator();
        while(fieldIterator.hasNext()){
            fieldIterator.next().setEnabled(enableStatus);
        }
    }

    private void updateStepProcessEditorsInfo(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ConfirmUpdateActivityTypePart1Text")+
                " <b>"+activityDefinitionType +"</b>"+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ConfigUpdateStepProcessEditorsInfoPart2Text"), ContentMode.HTML);
        final ConfirmDialog updateStepProcessEditorsInfoConfirmDialog = new ConfirmDialog();
        updateStepProcessEditorsInfoConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                updateStepProcessEditorsInfoConfirmDialog.close();

                boolean updateResult=doUpdateStepProcessEditorsInfo();
                if(updateResult){
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_ActivityTypeManagement_UpdateStepProcessEditorsInfoSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    setEditStepProcessEditorsFieldEnableStatus(false);
                    stepProcessEditorOperationButtonsLayout.removeAllComponents();
                    stepProcessEditorOperationButtonsLayout.addComponent(updateProcessEditorsButton);
                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_UpdateStepProcessEditorsInfoErrorText"),
                            userI18NProperties.
                                    getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        updateStepProcessEditorsInfoConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(updateStepProcessEditorsInfoConfirmDialog);
    }

    private boolean doUpdateStepProcessEditorsInfo(){
        final String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        final String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        Map<String,String> newStepProcessEditorsMap=new HashMap<>();
        Set<String> stepNameSet=stepProcessEditorTextFieldMap.keySet();
        Iterator<String> stepNameIterator=stepNameSet.iterator();
        while(stepNameIterator.hasNext()){
            String currentStepName=stepNameIterator.next();
            TextField currentStepEditorTextField=stepProcessEditorTextFieldMap.get(currentStepName);
            if(currentStepEditorTextField!=null&&currentStepEditorTextField.getValue()!=null&&!currentStepEditorTextField.getValue().equals("")){
                newStepProcessEditorsMap.put(currentStepName,currentStepEditorTextField.getValue());
            }
        }
        boolean updateResult=ActivitySpaceOperationUtil.updateActivityTypeStepProcessEditors(activitySpaceName,activityDefinitionType,newStepProcessEditorsMap);
        if(updateResult) {
            stepProcessEditorsMap = newStepProcessEditorsMap;
        }
        return updateResult;
    }

    private void setCurrentStepEditorValues(){
        Set<String> stepNameSet=stepProcessEditorTextFieldMap.keySet();
        Iterator<String> stepNameIterator=stepNameSet.iterator();
        while(stepNameIterator.hasNext()){
            String currentStepName=stepNameIterator.next();
            TextField currentStepEditorTextField=stepProcessEditorTextFieldMap.get(currentStepName);
            String currentStepEditorValue=stepProcessEditorsMap.get(currentStepName);
            if(currentStepEditorValue!=null){
                currentStepEditorTextField.setValue(currentStepEditorValue);
            }else{
                currentStepEditorTextField.setValue("");
            }
        }
    }

    private void addNewActivityTypeConfigurationItem(){
        AddNewActivityDefinitionGlobalConfigurationItemPanel addNewActivityDefinitionGlobalConfigurationItemPanel=
                new AddNewActivityDefinitionGlobalConfigurationItemPanel(this.currentUserClientInfo);
        addNewActivityDefinitionGlobalConfigurationItemPanel.setRelatedActivityDefinitionCustomConfigurationItemTable(globalConfigurationItemTable);
        addNewActivityDefinitionGlobalConfigurationItemPanel.setContainerActivityAdditionalConfigurationEditor(this);
        final Window window = new Window();
        window.setWidth(550.0f, Unit.PIXELS);
        window.setHeight(200.0f, Unit.PIXELS);
        window.setModal(true);
        window.setResizable(false);
        window.center();
        window.setContent(addNewActivityDefinitionGlobalConfigurationItemPanel);
        addNewActivityDefinitionGlobalConfigurationItemPanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    public boolean addNewActivityTypeGlobalConfigurationItem(String itemName){
        CustomStructure newAddedStructure=ActivitySpaceOperationUtil.addSubCustomStructure(activityTypeCustomConfigItemRootStructure, itemName);
        if(newAddedStructure!=null&&newAddedStructure.getStructureName().equals(itemName)){
            globalConfigurationItemTable.addNewConfigurationItem(itemName,true);
            return true;
        }else{
            return false;
        }
    }
}