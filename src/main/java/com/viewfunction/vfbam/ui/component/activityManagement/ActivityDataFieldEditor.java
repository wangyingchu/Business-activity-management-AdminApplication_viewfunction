package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;

import com.vaadin.ui.*;

import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.ApplicationConstant;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ActivityDataFieldEditor  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public static final String EDITMODE_UPDATE="EDITMODE_UPDATE";
    public static final String EDITMODE_NEW="EDITMODE_NEW";
    private String currentEditMode;
    private SectionActionsBar editDataFieldActionsBar;
    private String componentType;
    private String componentID;

    private FormLayout form;
    private HorizontalLayout footer;
    private TextField dataFieldNameName;
    private TextField dataFieldDisplayName;
    private ComboBox dataFieldType;
    private TextArea dataFieldDescription;
    private CheckBox arrayFieldCheck;
    private CheckBox mandatoryFieldCheck;
    private CheckBox systemFieldCheck;

    private Button updateButton;
    private Button resetButton;
    private Button addButton;

    private Window containerDialog;

    private ActivityDataFieldsEditor relatedActivityDataFieldsEditor;
    private ActivityDataFieldsActionTable relatedActivityDataFieldsActionTable;

    private String currentDataFieldName;
    private String currentDataFieldDisplayName;
    private String currentDataFieldType;
    private String currentDataFieldIsArray;
    private String currentDataFieldIsMandatory;
    private String currentDataFieldIsSystem;
    private String currentDataFieldDesc;

    private boolean isFilterMode;

    public ActivityDataFieldEditor(UserClientInfo currentUserClientInfo,String editMode,boolean isFilterMode){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        this.currentEditMode=editMode;
        this.isFilterMode=isFilterMode;
        if(EDITMODE_NEW.equals(this.currentEditMode)){
            MainSectionTitle addNewParticipantSectionTitle=new MainSectionTitle(userI18NProperties.
                    getProperty("ActivityManagement_Common_AddDataFieldText"));
            addComponent(addNewParticipantSectionTitle);
        }else if(EDITMODE_UPDATE.equals(this.currentEditMode)){
            MainSectionTitle addNewParticipantSectionTitle=new MainSectionTitle(userI18NProperties.
                    getProperty("ActivityManagement_Common_UpdateDataFieldText"));
            addComponent(addNewParticipantSectionTitle);
        }

        editDataFieldActionsBar=new SectionActionsBar(new Label(userI18NProperties.
                getProperty("ActivityManagement_Common_ActivitySpaceText")+" <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(editDataFieldActionsBar);

        form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        dataFieldNameName = new TextField(userI18NProperties.
                getProperty("ActivityManagement_Common_DataFieldNameText"));
        dataFieldNameName.setRequired(true);
        dataFieldNameName.setWidth("100%");
        form.addComponent(dataFieldNameName);

        dataFieldDisplayName = new TextField(userI18NProperties.
                getProperty("ActivityManagement_Common_DataFieldDisplayNameText"));
        dataFieldDisplayName.setWidth("100%");
        dataFieldDisplayName.setRequired(true);
        form.addComponent(dataFieldDisplayName);

        dataFieldType = new ComboBox(userI18NProperties.
                getProperty("ActivityManagement_Common_DataFieldTypeNameText"));
        dataFieldType.setRequired(true);
        dataFieldType.setWidth("100%");
        dataFieldType.setTextInputAllowed(false);
        dataFieldType.setNullSelectionAllowed(false);
        dataFieldType.setInputPrompt(userI18NProperties.
                        getProperty("ActivityManagement_Common_PleaseSelectDataTypeText"));
        dataFieldType.addItem(ApplicationConstant.DataFieldType_STRING);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_BINARY);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_BOOLEAN);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_DATE);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_DECIMAL);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_DOUBLE);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_LONG);
        form.addComponent(dataFieldType);

        dataFieldDescription = new TextArea(userI18NProperties.
                getProperty("ActivityManagement_Common_DataFieldDescText"));
        dataFieldDescription.setRequired(true);
        dataFieldDescription.setWidth("100%");
        dataFieldDescription.setRows(2);
        form.addComponent(dataFieldDescription);

        HorizontalLayout checkboxRow = new HorizontalLayout();
        checkboxRow.setMargin(true);
        checkboxRow.setSpacing(true);
        form.addComponent(checkboxRow);

        arrayFieldCheck = new CheckBox(userI18NProperties.
                getProperty("ActivityManagement_Common_ArrayDataFieldText"), false);
        checkboxRow.addComponent(arrayFieldCheck);

        mandatoryFieldCheck = new CheckBox(userI18NProperties.
                getProperty("ActivityManagement_Common_MandatoryDataFieldText"), false);
        checkboxRow.addComponent(mandatoryFieldCheck);

        systemFieldCheck = new CheckBox(userI18NProperties.
                getProperty("ActivityManagement_Common_SystemDataFieldText"), false);
        checkboxRow.addComponent(systemFieldCheck);
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

        if(this.isFilterMode){
            mandatoryFieldCheck.setVisible(false);
            systemFieldCheck.setVisible(false);
        }
    }

    @Override
    public void attach() {
        super.attach();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        if(getComponentType()==null){
            return;
        }else{
            String activitySpaceName="";
            Label sectionActionBarLabel=null;
            if(currentActivitySpaceComponentInfo!=null){
                activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            }
            if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE)){
                sectionActionBarLabel=new Label(userI18NProperties.
                        getProperty("ActivityManagement_RoleQueuesManagement_RoleQueueText")+" : <b>"+getComponentID()+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            }
            if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROSTER)){
                sectionActionBarLabel=new Label(userI18NProperties.
                        getProperty("ActivityManagement_RosterManagement_RosterText")+" : <b>"+getComponentID()+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            }
            if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION)){
                sectionActionBarLabel=new Label(userI18NProperties.
                        getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeText")+" : <b>"+getComponentID()+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            }
            editDataFieldActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        footer.removeAllComponents();

        if(this.currentEditMode.equals(EDITMODE_NEW)){
            //For add new data field
            footer.addComponent(addButton);
            dataFieldNameName.setValue("");
            dataFieldDisplayName.setValue("");
            dataFieldType.select(null);
            dataFieldDescription.setValue("");
            arrayFieldCheck.clear();
            mandatoryFieldCheck.clear();
            systemFieldCheck.clear();
        }else{
            //For edit existing data field
            footer.addComponent(updateButton);
            footer.addComponent(resetButton);
            dataFieldNameName.setReadOnly(false);
            if(currentDataFieldName!=null){
                dataFieldNameName.setValue(currentDataFieldName);
            }
            dataFieldNameName.setReadOnly(true);
            resetFieldData();
        }
    }

    private boolean updateCurrentDataField(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String dataFieldNameStr=dataFieldNameName.getValue();
        final String dataFieldDisplayNameStr=dataFieldDisplayName.getValue();
        final Object dataFieldTypeObj=dataFieldType.getValue();
        final String dataFieldDescStr=dataFieldDescription.getValue();
        final boolean isArray=arrayFieldCheck.getValue();
        final boolean isMandatory=mandatoryFieldCheck.getValue();
        final boolean isSystem=systemFieldCheck.getValue();
        if(dataFieldNameStr.equals("")||dataFieldDisplayNameStr.equals("")||
                dataFieldTypeObj==null||dataFieldDescStr.equals("")){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_PleaseInputAllFieldText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return false;
        }else{
            Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                    getProperty("ActivityManagement_Common_ConfirmUpdateDataFieldPart1Text")+
                    " <b>"+dataFieldNameStr +"</b>"+userI18NProperties.
                    getProperty("ActivityManagement_Common_ConfirmUpdateDataFieldPart2Text"), ContentMode.HTML);
            final ConfirmDialog updateDataFieldConfirmDialog = new ConfirmDialog();
            updateDataFieldConfirmDialog.setConfirmMessage(confirmMessage);
            final ActivityDataFieldEditor self=this;
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_Common_UpdateDataFieldSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    //close confirm dialog
                    updateDataFieldConfirmDialog.close();
                    if(self.containerDialog!=null){
                        self.containerDialog.close();
                    }
                    //execute callback logic
                    if(self.relatedActivityDataFieldsActionTable!=null){
                        self.relatedActivityDataFieldsActionTable.updateExistDataField(dataFieldNameStr, dataFieldDisplayNameStr,
                                         dataFieldTypeObj.toString(),dataFieldDescStr,isArray,isMandatory,isSystem);
                    }
                }
            };
            updateDataFieldConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(updateDataFieldConfirmDialog);
        }
        return true;
    }

    private void resetFieldData(){
        if(currentDataFieldName!=null){
            dataFieldNameName.setValue(currentDataFieldName);
        }
        dataFieldNameName.setReadOnly(true);
        if(currentDataFieldDisplayName!=null){
            dataFieldDisplayName.setValue(currentDataFieldDisplayName);
        }
        if(currentDataFieldDesc!=null){
            dataFieldDescription.setValue(currentDataFieldDesc);
        }
        if(currentDataFieldType!=null){
            dataFieldType.select(currentDataFieldType);
        }
        if(currentDataFieldIsArray!=null){
            if(currentDataFieldIsArray.equals("true")){
                arrayFieldCheck.setValue(true);
            }else{
                arrayFieldCheck.setValue(false);
            }
        }
        if(currentDataFieldIsMandatory!=null){
            if(currentDataFieldIsMandatory.equals("true")){
                mandatoryFieldCheck.setValue(true);
            }else{
                mandatoryFieldCheck.setValue(false);
            }
        }
        if(currentDataFieldIsSystem!=null){
            if(currentDataFieldIsSystem.equals("true")){
                systemFieldCheck.setValue(true);
            }else{
                systemFieldCheck.setValue(false);
            }
        }
    }

    private boolean addNewDataField(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String dataFieldNameStr=dataFieldNameName.getValue();
        final String dataFieldDisplayNameStr=dataFieldDisplayName.getValue();
        final Object dataFieldTypeObj=dataFieldType.getValue();
        final String dataFieldDescStr=dataFieldDescription.getValue();
        final boolean isArray=arrayFieldCheck.getValue();
        final boolean isMandatory=mandatoryFieldCheck.getValue();
        final boolean isSystem=systemFieldCheck.getValue();
        if(dataFieldNameStr.equals("")||dataFieldDisplayNameStr.equals("")||
                dataFieldTypeObj==null||dataFieldDescStr.equals("")){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_PleaseInputAllFieldText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return false;
        }else{
            boolean dataFieldNotExistCheck=this.relatedActivityDataFieldsEditor.dataFieldNotExistCheck(dataFieldNameStr);
            if(!dataFieldNotExistCheck){
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldExistText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return false;

            }
            //do add new logic
            Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                    getProperty("ActivityManagement_Common_ConfirmAddDataFieldText")+
                    " <b>"+dataFieldNameStr +"</b>.", ContentMode.HTML);
            final ConfirmDialog addDataFieldConfirmDialog = new ConfirmDialog();
            addDataFieldConfirmDialog.setConfirmMessage(confirmMessage);
            final ActivityDataFieldEditor self=this;
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_AddDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_Common_AddDataFieldSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    //close confirm dialog
                    addDataFieldConfirmDialog.close();
                    if(self.containerDialog!=null){
                        self.containerDialog.close();
                    }
                    //execute callback logic
                    if(self.relatedActivityDataFieldsEditor!=null){
                        self.relatedActivityDataFieldsEditor.addNewDataFieldFinishCallBack(dataFieldNameStr,dataFieldDisplayNameStr,
                                dataFieldTypeObj.toString(),dataFieldDescStr,isArray,isMandatory,isSystem);
                    }
                }
            };
            addDataFieldConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(addDataFieldConfirmDialog);
        }
        return true;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getComponentID() {
        return componentID;
    }

    public void setComponentID(String componentID) {
        this.componentID = componentID;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setRelatedActivityDataFieldsEditor(ActivityDataFieldsEditor relatedActivityDataFieldsEditor) {
        this.relatedActivityDataFieldsEditor = relatedActivityDataFieldsEditor;
    }

    public void setRelatedActivityDataFieldsActionTable(ActivityDataFieldsActionTable relatedActivityDataFieldsActionTable) {
        this.relatedActivityDataFieldsActionTable = relatedActivityDataFieldsActionTable;
    }

    public void setCurrentDataFieldName(String currentDataFieldName) {
        this.currentDataFieldName = currentDataFieldName;
    }

    public void setCurrentDataFieldDisplayName(String currentDataFieldDisplayName) {
        this.currentDataFieldDisplayName = currentDataFieldDisplayName;
    }

    public void setCurrentDataFieldType(String currentDataFieldType) {
        this.currentDataFieldType = currentDataFieldType;
    }

    public void setCurrentDataFieldIsArray(String currentDataFieldIsArray) {
        this.currentDataFieldIsArray = currentDataFieldIsArray;
    }

    public void setCurrentDataFieldIsMandatory(String currentDataFieldIsMandatory) {
        this.currentDataFieldIsMandatory = currentDataFieldIsMandatory;
    }

    public void setCurrentDataFieldIsSystem(String currentDataFieldIsSystem) {
        this.currentDataFieldIsSystem = currentDataFieldIsSystem;
    }

    public void setCurrentDataFieldDesc(String currentDataFieldDesc) {
        this.currentDataFieldDesc = currentDataFieldDesc;
    }
}
