package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ActivityExposedDataFieldEditor extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public static final String EDITMODE_UPDATE="EDITMODE_UPDATE";
    public static final String EDITMODE_NEW="EDITMODE_NEW";
    private String currentEditMode;
    private SectionActionsBar editDataFieldActionsBar;

    private FormLayout form;
    private HorizontalLayout footer;

    private ComboBox dataFieldDefinitions;
    private CheckBox isReadableCheck;
    private CheckBox isWritableCheck;
    private CheckBox isMandatoryCheck;
    private TextField dataFieldsDisplayForEdit;
    private Button updateButton;
    private Button resetButton;
    private Button addButton;

    private Window containerDialog;

    private ActivityExposedDataFieldsEditor relatedActivityDataFieldsEditor;
    private ActivityExposedDataFieldsActionTable relatedActivityExposedDataFieldsActionTable;
    private Map<String,ActivityDataFieldVO> activityDataFieldsInfoMap;

    private String currentDataFieldName;
    private String currentDataFieldDisplayName;
    private String currentDataFieldIsReadable;
    private String currentDataFieldIsWritable;
    private String currentDataFieldIsMandatory;

    public ActivityExposedDataFieldEditor(UserClientInfo currentUserClientInfo,String editMode){
        this.currentUserClientInfo=currentUserClientInfo;
        activityDataFieldsInfoMap=new HashMap<String,ActivityDataFieldVO>();
        setSpacing(true);
        setMargin(true);
        this.currentEditMode=editMode;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        if(EDITMODE_NEW.equals(this.currentEditMode)){
            MainSectionTitle addNewParticipantSectionTitle=new MainSectionTitle(userI18NProperties.
                    getProperty("ActivityManagement_Common_AddNewExposedFieldText"));
            addComponent(addNewParticipantSectionTitle);
        }else if(EDITMODE_UPDATE.equals(this.currentEditMode)){
            MainSectionTitle addNewParticipantSectionTitle=new MainSectionTitle(userI18NProperties.
                    getProperty("ActivityManagement_Common_UpdateExposedFieldText"));
            addComponent(addNewParticipantSectionTitle);
        }

        editDataFieldActionsBar=new SectionActionsBar(new Label(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_DefinitionText")+" : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(editDataFieldActionsBar);

        form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        dataFieldDefinitions = new ComboBox(userI18NProperties.
                getProperty("ActivityManagement_Common_DataFieldDefinitionText"));
        dataFieldDefinitions.setRequired(true);
        dataFieldDefinitions.setWidth("100%");
        dataFieldDefinitions.setTextInputAllowed(true);
        dataFieldDefinitions.setNullSelectionAllowed(false);
        dataFieldDefinitions.setPageLength(200);
        dataFieldDefinitions.setInputPrompt(userI18NProperties.
                getProperty("ActivityManagement_Common_SelectDataFieldDefinitionText"));
        dataFieldDefinitions.addValueChangeListener(new Property.ValueChangeListener(){
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if(valueChangeEvent.getProperty()!=null&&valueChangeEvent.getProperty().getValue()!=null){
                    String selectedDataField=valueChangeEvent.getProperty().getValue().toString();
                    setCheckboxStatus(selectedDataField);
                }
            }
        });
        form.addComponent(dataFieldDefinitions);

        dataFieldsDisplayForEdit = new TextField(userI18NProperties.
                getProperty("ActivityManagement_Common_DataFieldDefinitionText"));
        dataFieldsDisplayForEdit.setRequired(true);
        dataFieldsDisplayForEdit.setWidth("100%");
        form.addComponent(dataFieldsDisplayForEdit);

        HorizontalLayout checkboxRow = new HorizontalLayout();
        checkboxRow.setMargin(true);
        checkboxRow.setSpacing(true);
        form.addComponent(checkboxRow);

        isMandatoryCheck = new CheckBox(userI18NProperties.
                getProperty("ActivityManagement_Common_DataFieldShortProp_IsMandatoryText"), false);
        checkboxRow.addComponent(isMandatoryCheck);

        isReadableCheck = new CheckBox(userI18NProperties.
                getProperty("ActivityManagement_Common_DataFieldShortProp_IsReadableText"), false);
        checkboxRow.addComponent(isReadableCheck);

        isWritableCheck = new CheckBox(userI18NProperties.
                getProperty("ActivityManagement_Common_DataFieldShortProp_IsWritableText"), false);
        checkboxRow.addComponent(isWritableCheck);

        form.setReadOnly(true);

        footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        updateButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_UpdateButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do update current data field logic */
                updateCurrentDataField();
            }
        });
        updateButton.setIcon(FontAwesome.SAVE);
        updateButton.addStyleName("primary");

        resetButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_ResetButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                resetFieldData();
            }
        });
        resetButton.setIcon(FontAwesome.TIMES);

        addButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_AddDataFieldButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new data field logic */
                addNewDataField();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
    }

    @Override
    public void attach() {
        super.attach();
        if(relatedActivityDataFieldsEditor!=null){
            dataFieldDefinitions.clear();
            dataFieldDefinitions.removeAllItems();
            activityDataFieldsInfoMap.clear();
            List<ActivityDataFieldVO> activityTypeDataFieldList= relatedActivityDataFieldsEditor.getActivityDataFieldsList();
            for(ActivityDataFieldVO currentActivityDataFieldVO:activityTypeDataFieldList){
                String dataFieldCombinationStr=currentActivityDataFieldVO.getDataFieldName()+" ("+currentActivityDataFieldVO.getDataFieldDisplayName()+")";
                dataFieldDefinitions.addItem(dataFieldCombinationStr);
                activityDataFieldsInfoMap.put(dataFieldCombinationStr,currentActivityDataFieldVO);
            }
        }
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
            sectionActionBarLabel=new Label(userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeText")+" : <b>"+componentId+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            editDataFieldActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        footer.removeAllComponents();
        isReadableCheck.clear();
        isWritableCheck.clear();
        isMandatoryCheck.clear();
        if(this.currentEditMode.equals(EDITMODE_NEW)){
            //For add new data field
            dataFieldsDisplayForEdit.setVisible(false);
            footer.addComponent(addButton);
            dataFieldDefinitions.select(null);
            isReadableCheck.setValue(true);
            isWritableCheck.setValue(true);
        }else{
            //For edit existing data field
            dataFieldDefinitions.setVisible(false);
            String dateFieldComStr=currentDataFieldName+" ("+currentDataFieldDisplayName+")";
            dataFieldsDisplayForEdit.setValue(dateFieldComStr);
            dataFieldsDisplayForEdit.setReadOnly(true);
            isReadableCheck.setValue(Boolean.parseBoolean(currentDataFieldIsReadable));
            isWritableCheck.setValue(Boolean.parseBoolean(currentDataFieldIsWritable));
            isMandatoryCheck.setValue(Boolean.parseBoolean(currentDataFieldIsMandatory));
            setCheckboxStatus(dateFieldComStr);
            footer.addComponent(updateButton);
            footer.addComponent(resetButton);
            resetFieldData();
        }
    }

    private boolean updateCurrentDataField(){
        if(this.containerDialog!=null){
            this.containerDialog.close();
        }
        //execute callback logic
        if(this.relatedActivityExposedDataFieldsActionTable!=null){
            this.relatedActivityExposedDataFieldsActionTable.doUpdateExistDataField(currentDataFieldName, isReadableCheck.getValue(),
                    isWritableCheck.getValue(),isMandatoryCheck.getValue());
        }
        return true;
    }

    private void resetFieldData(){
        isReadableCheck.setValue(Boolean.parseBoolean(currentDataFieldIsReadable));
        isWritableCheck.setValue(Boolean.parseBoolean(currentDataFieldIsWritable));
        isMandatoryCheck.setValue(Boolean.parseBoolean(currentDataFieldIsMandatory));
    }

    private boolean addNewDataField(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        if(dataFieldDefinitions.getValue()==null){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Common_SelectDataFieldDefinitionText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return false;
        }else{
            final String dataFieldName=activityDataFieldsInfoMap.get(dataFieldDefinitions.getValue().toString()).getDataFieldName();
            this.relatedActivityDataFieldsEditor.getActivityExposedDataFieldByName(dataFieldName);
            if(this.relatedActivityDataFieldsEditor.getActivityExposedDataFieldByName(dataFieldName)!=null){
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_SelectedFieldExistText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
            }else{
                boolean isReadable=isReadableCheck.getValue();
                boolean isWritable=isWritableCheck.getValue();
                boolean isMandatory=isMandatoryCheck.getValue();
                if(this.containerDialog != null) {
                    this.containerDialog.close();
                }
                //execute callback logic
                if (this.relatedActivityDataFieldsEditor != null) {
                    this.relatedActivityDataFieldsEditor.addNewExposedDataField(dataFieldName,isReadable,isWritable,isMandatory);
                }
            }
        }
        return true;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setRelatedActivityDataFieldsEditor(ActivityExposedDataFieldsEditor relatedActivityDataFieldsEditor) {
        this.relatedActivityDataFieldsEditor = relatedActivityDataFieldsEditor;
    }

    public void setCurrentDataFieldDisplayName(String currentDataFieldDisplayName) {
        this.currentDataFieldDisplayName = currentDataFieldDisplayName;
    }

    public void setCurrentDataFieldName(String currentDataFieldName) {
        this.currentDataFieldName = currentDataFieldName;
    }

    public void setCurrentDataFieldIsReadable(String currentDataFieldIsReadable) {
        this.currentDataFieldIsReadable = currentDataFieldIsReadable;
    }

    public void setCurrentDataFieldIsWritable(String currentDataFieldIsWritable) {
        this.currentDataFieldIsWritable = currentDataFieldIsWritable;
    }

    public void setRelatedActivityExposedDataFieldsActionTable(ActivityExposedDataFieldsActionTable relatedActivityExposedDataFieldsActionTable) {
        this.relatedActivityExposedDataFieldsActionTable = relatedActivityExposedDataFieldsActionTable;
    }

    public void setCurrentDataFieldIsMandatory(String currentDataFieldIsMandatory) {
        this.currentDataFieldIsMandatory = currentDataFieldIsMandatory;
    }

    private void setCheckboxStatus(String selectedValue){
        ActivityDataFieldVO selectedActivityTypeDataFieldDefinition=activityDataFieldsInfoMap.get(selectedValue);
        if(selectedActivityTypeDataFieldDefinition!=null){
            boolean isGlobalMandatoryField=selectedActivityTypeDataFieldDefinition.isMandatoryField();
            if(isGlobalMandatoryField){
                if(EDITMODE_NEW.equals(this.currentEditMode)){
                    isReadableCheck.setValue(true);
                    isWritableCheck.setValue(true);
                    isMandatoryCheck.setValue(true);
                    isReadableCheck.setEnabled(false);
                    isWritableCheck.setEnabled(false);
                    isMandatoryCheck.setEnabled(false);
                }else if(EDITMODE_UPDATE.equals(this.currentEditMode)){
                    if(currentDataFieldIsMandatory.equals("true")){
                        isMandatoryCheck.setEnabled(false);
                    }
                    if(currentDataFieldIsReadable.equals("true")){
                        isReadableCheck.setEnabled(false);
                    }
                    if(currentDataFieldIsWritable.equals("true")){
                        isWritableCheck.setEnabled(false);
                    }
                }
            }else{
                if(EDITMODE_NEW.equals(this.currentEditMode)){
                    isReadableCheck.setValue(true);
                    isWritableCheck.setValue(true);
                    isMandatoryCheck.setValue(false);
                }
                isReadableCheck.setEnabled(true);
                isWritableCheck.setEnabled(true);
                isMandatoryCheck.setEnabled(true);
            }
        }
    }
}
