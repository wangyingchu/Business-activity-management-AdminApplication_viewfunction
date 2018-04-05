package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.List;
import java.util.Properties;

public class ActivityDataFieldsActionTable extends Table {

    private UserClientInfo currentUserClientInfo;
    private String columnName_DataFieldName ="columnName_DataFieldName";
    private String columnName_DataFieldDisplayName ="columnName_DataFieldDisplayName";
    private String columnName_DataFieldType ="columnName_DataFieldType";
    private String columnName_DataFieldDesc ="columnName_DataFieldDesc";
    private String columnName_isArrayDataField ="columnName_isArrayDataField";
    private String columnName_isSystemDataField ="columnName_isSystemDataField";
    private String columnName_isMandatoryDataField ="columnName_isMandatoryDataField";
    private String columnName_dataFieldOperations ="columnName_dataFieldOperations";

    private IndexedContainer containerDataSource;

    private String dataFieldsQueryId;
    private String dataFieldQueryType;

    public static final String DATAFIELDS_TYPE_ACTIVITYTYPE="DATAFIELDS_TYPE_ACTIVITYTYPE";
    public static final String DATAFIELDS_TYPE_ROSTER="DATAFIELDS_TYPE_ROSTER";
    public static final String DATAFIELDS_TYPE_ROLEQUEUE="DATAFIELDS_TYPE_ROLEQUEUE";

    private boolean isActionMode;
    private boolean isFilterMode;

    private List<ActivityDataFieldVO> activityDataFieldsList;
    private List<DataFieldDefinition> dataFieldDefinitionsList;
    private ActivityDataFieldsEditor relatedActivityDataFieldsEditor;

    public ActivityDataFieldsActionTable(UserClientInfo currentUserClientInfo, String tableHeight, boolean isActionMode,boolean isFilterMode){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.isActionMode=isActionMode;
        this.isFilterMode=isFilterMode;
        setWidth("100%");
        if(tableHeight!=null){
            setHeight(tableHeight);
        }else{
            setHeight("100%");
        }
        setPageLength(10);
        setColumnReorderingAllowed(false);
        setSelectable(true);
        setMultiSelect(false);
        setSortEnabled(true);
        addStyleName("no-vertical-lines");
        addStyleName("no-horizontal-lines");
        addStyleName("borderless");

        this.containerDataSource = new IndexedContainer();
        this.containerDataSource.addContainerProperty(columnName_DataFieldName, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_DataFieldDisplayName, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_DataFieldType, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_DataFieldDesc, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_isArrayDataField, String.class, null);
        if(!this.isFilterMode) {
            this.containerDataSource.addContainerProperty(columnName_isMandatoryDataField, String.class, null);
            this.containerDataSource.addContainerProperty(columnName_isSystemDataField, String.class, null);
        }
        if(this.isActionMode){
            this.containerDataSource.addContainerProperty(columnName_dataFieldOperations,DataFieldsTableRowActions.class, null);
        }
        setContainerDataSource(this.containerDataSource);
        if(this.isActionMode){
            setRowHeaderMode(RowHeaderMode.INDEX);
        }
        if(this.isActionMode){
            if(!this.isFilterMode) {
                setColumnHeaders(new String[]{
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldNameText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_DisplayNameText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_TypeText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_DescText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_IsArrayText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_IsMandatoryText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_IsSystemText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Table_ListActionPropertyText")
                });
            }else{
                setColumnHeaders(new String[]{
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldNameText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_DisplayNameText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_TypeText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_DescText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_IsArrayText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Table_ListActionPropertyText")
                });
            }
        }else{
            if(!this.isFilterMode) {
                setColumnHeaders(new String[]{
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldNameText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_DisplayNameText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_TypeText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_DescText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_IsArrayText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_IsMandatoryText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_IsSystemText")
                });
            }else{
                setColumnHeaders(new String[]{
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldNameText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_DisplayNameText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_TypeText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_DescText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DataFieldShortProp_IsArrayText")
                });
            }
        }
        setColumnAlignment(columnName_DataFieldName, Align.LEFT);
        setColumnAlignment(columnName_DataFieldDisplayName, Align.LEFT);
        setColumnAlignment(columnName_DataFieldType, Align.CENTER);
        setColumnWidth(columnName_DataFieldDesc, 100);
        setColumnWidth(columnName_DataFieldType, 100);
        setColumnAlignment(columnName_isArrayDataField, Align.CENTER);
        setColumnWidth(columnName_isArrayDataField, 110);
        if(!this.isFilterMode) {
            setColumnAlignment(columnName_isMandatoryDataField, Align.CENTER);
            setColumnWidth(columnName_isMandatoryDataField, 110);
            setColumnAlignment(columnName_isSystemDataField, Align.CENTER);
            setColumnWidth(columnName_isSystemDataField, 110);
        }
        if(this.isActionMode){
            setColumnAlignment(columnName_dataFieldOperations, Align.CENTER);
            setColumnWidth(columnName_dataFieldOperations, 100);
        }
    }

    @Override
    public void attach() {
        super.attach();
        if(getDataFieldQueryType()==null){
            return;
        }
        loadActivityDataFieldsData();
    }

    public void loadActivityDataFieldsData(){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        this.clear();
        this.containerDataSource.removeAllItems();
        if(this.dataFieldDefinitionsList!=null){
            //data field definitions already set by container
            for(DataFieldDefinition currentDataFieldDefinition:dataFieldDefinitionsList){
                String id = currentDataFieldDefinition.getFieldName();
                Item item = this.containerDataSource.addItem(id);
                item.getItemProperty(columnName_DataFieldName).setValue(currentDataFieldDefinition.getFieldName());
                item.getItemProperty(columnName_DataFieldDisplayName).setValue(currentDataFieldDefinition.getDisplayName());
                item.getItemProperty(columnName_DataFieldDesc).setValue(currentDataFieldDefinition.getDescription());
                item.getItemProperty(columnName_DataFieldType).
                        setValue(ActivitySpaceOperationUtil.getDataFieldDefinitionTypeString(currentDataFieldDefinition.getFieldType()));
                item.getItemProperty(columnName_isArrayDataField).setValue(""+currentDataFieldDefinition.isArrayField());
                if(!this.isFilterMode) {
                    item.getItemProperty(columnName_isMandatoryDataField).setValue(""+currentDataFieldDefinition.isMandatoryField());
                    item.getItemProperty(columnName_isSystemDataField).setValue(""+currentDataFieldDefinition.isSystemField());
                }
                if(this.isActionMode){
                    DataFieldsTableRowActions actionButtons = new DataFieldsTableRowActions(this.currentUserClientInfo,id);
                    actionButtons.setContainerActivityDataFieldsActionTable(this);
                    item.getItemProperty(columnName_dataFieldOperations).setValue(actionButtons);
                }
            }
        }else{
            //need query activityspace to get data field definitions
            DataFieldDefinition[] dataFieldDefinitions=
                    ActivitySpaceOperationUtil.getDataFieldDefinitions(activitySpaceName,getDataFieldsQueryId(),getDataFieldQueryType());
            if(dataFieldDefinitions!=null){
                for(DataFieldDefinition currentDataFieldDefinition:dataFieldDefinitions){
                    String id = currentDataFieldDefinition.getFieldName();
                    Item item = this.containerDataSource.addItem(id);
                    item.getItemProperty(columnName_DataFieldName).setValue(currentDataFieldDefinition.getFieldName());
                    item.getItemProperty(columnName_DataFieldDisplayName).setValue(currentDataFieldDefinition.getDisplayName());
                    item.getItemProperty(columnName_DataFieldDesc).setValue(currentDataFieldDefinition.getDescription());
                    item.getItemProperty(columnName_DataFieldType).
                            setValue(ActivitySpaceOperationUtil.getDataFieldDefinitionTypeString(currentDataFieldDefinition.getFieldType()));
                    item.getItemProperty(columnName_isArrayDataField).setValue(""+currentDataFieldDefinition.isArrayField());
                    if(!this.isFilterMode) {
                        item.getItemProperty(columnName_isMandatoryDataField).setValue(""+currentDataFieldDefinition.isMandatoryField());
                        item.getItemProperty(columnName_isSystemDataField).setValue(""+currentDataFieldDefinition.isSystemField());
                    }
                    if(this.isActionMode){
                        DataFieldsTableRowActions actionButtons = new DataFieldsTableRowActions(this.currentUserClientInfo,id);
                        actionButtons.setContainerActivityDataFieldsActionTable(this);
                        item.getItemProperty(columnName_dataFieldOperations).setValue(actionButtons);
                    }
                }
            }
        }
    }

    public String getDataFieldsQueryId() {
        return dataFieldsQueryId;
    }

    public void setDataFieldsQueryId(String dataFieldsQueryId) {
        this.dataFieldsQueryId = dataFieldsQueryId;
    }

    public String getDataFieldQueryType() {
        return dataFieldQueryType;
    }

    public void setDataFieldQueryType(String dataFieldQueryType) {
        this.dataFieldQueryType = dataFieldQueryType;
    }

    public void addNewDataField(String dataFieldName,String dataFieldDisplayName,String dataFieldType,
                                String dataFieldDesc,boolean isArray,boolean isMandatory,boolean isSystem){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        boolean addDataFieldDefinitionResult=ActivitySpaceOperationUtil.addDataFieldDefinition(activitySpaceName, getDataFieldsQueryId(), getDataFieldQueryType(), dataFieldName, dataFieldDisplayName, dataFieldType,
                dataFieldDesc, isArray, isMandatory, isSystem);
        if(addDataFieldDefinitionResult){
            Notification resultNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_AddDataSuccessText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Common_AddDataFieldDefineSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
            String id = dataFieldName;
            Item item = this.containerDataSource.addItem(id);
            item.getItemProperty(columnName_DataFieldName).setValue(dataFieldName);
            item.getItemProperty(columnName_DataFieldDisplayName).setValue(dataFieldDisplayName);
            item.getItemProperty(columnName_DataFieldType).setValue(dataFieldType);
            item.getItemProperty(columnName_DataFieldDesc).setValue(dataFieldDesc);
            item.getItemProperty(columnName_isArrayDataField).setValue(""+isArray);
            if(!this.isFilterMode) {
                item.getItemProperty(columnName_isMandatoryDataField).setValue("" + isMandatory);
                item.getItemProperty(columnName_isSystemDataField).setValue("" + isSystem);
            }
            if(this.isActionMode){
                DataFieldsTableRowActions actionButtons = new DataFieldsTableRowActions(this.currentUserClientInfo,id);
                actionButtons.setContainerActivityDataFieldsActionTable(this);
                item.getItemProperty(columnName_dataFieldOperations).setValue(actionButtons);
            }
            ActivityDataFieldVO newActivityDataFieldVO=new ActivityDataFieldVO();
            newActivityDataFieldVO.setDataFieldName(dataFieldName);
            newActivityDataFieldVO.setDataFieldDisplayName(dataFieldDisplayName);
            newActivityDataFieldVO.setDataType(dataFieldType);
            newActivityDataFieldVO.setDescription(dataFieldDesc);
            newActivityDataFieldVO.setMandatoryField(isMandatory);
            newActivityDataFieldVO.setSystemField(isSystem);
            newActivityDataFieldVO.setArrayField(isArray);
            if(activityDataFieldsList!=null){
                activityDataFieldsList.add(newActivityDataFieldVO);
            }
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_Common_AddDataFieldDefineErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    public void updateExistDataField(String dataFieldName,String dataFieldDisplayName,String dataFieldType,
                                     String dataFieldDesc,boolean isArray,boolean isMandatory,boolean isSystem){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        boolean updateDataFieldDefinitionResult=ActivitySpaceOperationUtil.updateDataFieldDefinition(activitySpaceName, getDataFieldsQueryId(), getDataFieldQueryType(), dataFieldName, dataFieldDisplayName, dataFieldType,
                dataFieldDesc, isArray, isMandatory, isSystem);
        if(updateDataFieldDefinitionResult){
            Notification resultNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Common_UpdateDataFieldDefineSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());

            if(getRelatedActivityDataFieldsEditor()!=null){
                getRelatedActivityDataFieldsEditor().updateActivityDataFieldSuccessCallBack();
            }

            Item itemToUpdate=this.containerDataSource.getItem(dataFieldName);
            if(itemToUpdate!=null){
                itemToUpdate.getItemProperty(columnName_DataFieldName).setValue(dataFieldName);
                itemToUpdate.getItemProperty(columnName_DataFieldDisplayName).setValue(dataFieldDisplayName);
                itemToUpdate.getItemProperty(columnName_DataFieldType).setValue(dataFieldType);
                itemToUpdate.getItemProperty(columnName_DataFieldDesc).setValue(dataFieldDesc);
                itemToUpdate.getItemProperty(columnName_isArrayDataField).setValue("" + isArray);
                if(!this.isFilterMode) {
                    itemToUpdate.getItemProperty(columnName_isMandatoryDataField).setValue("" + isMandatory);
                    itemToUpdate.getItemProperty(columnName_isSystemDataField).setValue("" + isSystem);
                }
            }

            if(activityDataFieldsList!=null){
                for(ActivityDataFieldVO currentActivityDataFieldVO:activityDataFieldsList){
                    if(dataFieldName.equals(currentActivityDataFieldVO.getDataFieldName())){
                        currentActivityDataFieldVO.setDataFieldDisplayName(dataFieldDisplayName);
                        currentActivityDataFieldVO.setDataType(dataFieldType);
                        currentActivityDataFieldVO.setDescription(dataFieldDesc);
                        currentActivityDataFieldVO.setMandatoryField(isMandatory);
                        currentActivityDataFieldVO.setSystemField(isSystem);
                        currentActivityDataFieldVO.setArrayField(isArray);
                        return;
                    }
                }
            }
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_Common_UpdateDataFieldDefineErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    public void deleteExistDataField(final String dataFieldName){
        Item itemToDelete=this.containerDataSource.getItem(dataFieldName);
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        //need check whether data field is activityType field and is used in activity
        if(DATAFIELDS_TYPE_ACTIVITYTYPE.equals(getDataFieldQueryType())){
            boolean isCurrentDataFieldUsed=
            ActivitySpaceOperationUtil.isDataFieldDefinitionUsedInActivity(activitySpaceName,getDataFieldsQueryId(),dataFieldName);
            if(isCurrentDataFieldUsed){
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("ActivityManagement_Common_DataFieldIsUsedText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_PleaseRemoveUseBeforeDeleteDataFieldText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
        }
        if(itemToDelete!=null){
            //do delete exist logic
            Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                    getProperty("ActivityManagement_Common_ConfirmDeleteDataFieldText")+
                    " <b>"+dataFieldName +"</b>.", ContentMode.HTML);
            final ConfirmDialog deleteDataFieldConfirmDialog = new ConfirmDialog();
            deleteDataFieldConfirmDialog.setConfirmMessage(confirmMessage);
            final ActivityDataFieldsActionTable self=this;
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    deleteDataFieldConfirmDialog.close();

                    boolean deleteDataFieldDefinitionResult=ActivitySpaceOperationUtil
                            .deleteDataFieldDefinition(activitySpaceName, getDataFieldsQueryId(), getDataFieldQueryType(), dataFieldName);
                    if(deleteDataFieldDefinitionResult){
                        Notification resultNotification = new Notification(userI18NProperties.
                                getProperty("Global_Application_DataOperation_DeleteDataSuccessText"),
                                userI18NProperties.
                                        getProperty("ActivityManagement_Common_DeleteDataFieldDefineSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());

                        self.removeItem(dataFieldName);

                        // need remove from the dataFieldList
                        if(self.dataFieldDefinitionsList!=null){
                            for(int i=0;i<self.dataFieldDefinitionsList.size();i++){
                                DataFieldDefinition currentDataFieldDefinition= dataFieldDefinitionsList.get(i);
                                if(currentDataFieldDefinition.getFieldName().equals(dataFieldName)){
                                    self.dataFieldDefinitionsList.remove(i);
                                    break;
                                }
                            }
                        }
                        if(activityDataFieldsList!=null){
                            for(int i=0;i<self.activityDataFieldsList.size();i++){
                                ActivityDataFieldVO currentActivityDataFieldVO= activityDataFieldsList.get(i);
                                if(currentActivityDataFieldVO.getDataFieldName().equals(dataFieldName)){
                                    self.activityDataFieldsList.remove(i);
                                    break;
                                }
                            }
                        }
                        if(getRelatedActivityDataFieldsEditor()!=null){
                            getRelatedActivityDataFieldsEditor().deleteActivityDataFieldSuccessCallBack();
                        }
                    }else{
                        Notification errorNotification = new Notification(userI18NProperties.
                                getProperty("Global_Application_DataOperation_DeleteDataErrorText"),
                                userI18NProperties.
                                        getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }
                }
            };
            deleteDataFieldConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(deleteDataFieldConfirmDialog);
        }
    }

    public void updateDataField(String dataFieldName){
        Item itemToUpdate = this.containerDataSource.getItem(dataFieldName);
        if(itemToUpdate!=null){
            if(getDataFieldQueryType()!=null){
                ActivityDataFieldEditor activityDataFieldEditor=null;
                if(getDataFieldQueryType().equals(DATAFIELDS_TYPE_ACTIVITYTYPE)){
                    activityDataFieldEditor=new ActivityDataFieldEditor(this.currentUserClientInfo,ActivityDataFieldEditor.EDITMODE_UPDATE,false);
                    activityDataFieldEditor.setComponentType(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION);
                }
                if(getDataFieldQueryType().equals(DATAFIELDS_TYPE_ROSTER)){
                    activityDataFieldEditor=new ActivityDataFieldEditor(this.currentUserClientInfo,ActivityDataFieldEditor.EDITMODE_UPDATE,true);
                    activityDataFieldEditor.setComponentType(ActivityManagementConst.COMPONENT_TYPE_ROSTER);
                }
                if(getDataFieldQueryType().equals(DATAFIELDS_TYPE_ROLEQUEUE)){
                    activityDataFieldEditor=new ActivityDataFieldEditor(this.currentUserClientInfo,ActivityDataFieldEditor.EDITMODE_UPDATE,true);
                    activityDataFieldEditor.setComponentType(ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE);
                }
                if(activityDataFieldEditor!=null) {
                    activityDataFieldEditor.setComponentID(getDataFieldsQueryId());
                    activityDataFieldEditor.setRelatedActivityDataFieldsActionTable(this);
                }
                String currentDataFieldName=itemToUpdate.getItemProperty(columnName_DataFieldName).getValue().toString();
                activityDataFieldEditor.setCurrentDataFieldName(currentDataFieldName);
                String currentDataFieldDisplayName=itemToUpdate.getItemProperty(columnName_DataFieldDisplayName).getValue().toString();
                activityDataFieldEditor.setCurrentDataFieldDisplayName(currentDataFieldDisplayName);
                String currentDataFieldType=itemToUpdate.getItemProperty(columnName_DataFieldType).getValue().toString();
                activityDataFieldEditor.setCurrentDataFieldType(currentDataFieldType);
                String currentDataFieldDesc=itemToUpdate.getItemProperty(columnName_DataFieldDesc).getValue().toString();
                activityDataFieldEditor.setCurrentDataFieldDesc(currentDataFieldDesc);
                String currentDataFieldIsArray=itemToUpdate.getItemProperty(columnName_isArrayDataField).getValue().toString();
                activityDataFieldEditor.setCurrentDataFieldIsArray(currentDataFieldIsArray);

                if(!this.isFilterMode) {
                    String currentDataFieldIsMandatory = itemToUpdate.getItemProperty(columnName_isMandatoryDataField).getValue().toString();
                    activityDataFieldEditor.setCurrentDataFieldIsMandatory(currentDataFieldIsMandatory);
                    String currentDataFieldIsSystem = itemToUpdate.getItemProperty(columnName_isSystemDataField).getValue().toString();
                    activityDataFieldEditor.setCurrentDataFieldIsSystem(currentDataFieldIsSystem);
                }

                Window window = new Window();
                window.setWidth(670.0f, Unit.PIXELS);
                window.setHeight(450.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(activityDataFieldEditor);
                activityDataFieldEditor.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        }
    }

    public void setActivityDataFieldsList(List<ActivityDataFieldVO> activityDataFieldsList) {
        this.activityDataFieldsList = activityDataFieldsList;
    }

    public void setDataFieldDefinitionsList(List<DataFieldDefinition> dataFieldDefinitionsList) {
        this.dataFieldDefinitionsList = dataFieldDefinitionsList;
    }

    public boolean dataFieldNotExistCheck(String dataFieldName){
        Object existItem=this.containerDataSource.getItem(dataFieldName);
        if(existItem==null){
            return true;
        }else{
            return false;
        }
    }

    public ActivityDataFieldsEditor getRelatedActivityDataFieldsEditor() {
        return relatedActivityDataFieldsEditor;
    }

    public void setRelatedActivityDataFieldsEditor(ActivityDataFieldsEditor relatedActivityDataFieldsEditor) {
        this.relatedActivityDataFieldsEditor = relatedActivityDataFieldsEditor;
    }
}
