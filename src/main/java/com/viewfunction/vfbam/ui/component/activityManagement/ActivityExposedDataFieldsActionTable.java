package com.viewfunction.vfbam.ui.component.activityManagement;


import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ActivityExposedDataFieldsActionTable extends Table {

    private UserClientInfo currentUserClientInfo;
    private String columnName_DataFieldName ="columnName_DataFieldName";
    private String columnName_DataFieldDisplayName ="columnName_DataFieldDisplayName";
    private String columnName_DataFieldType ="columnName_DataFieldType";
    private String columnName_isMandatoryDataField ="columnName_isMandatoryDataField";
    private String columnName_isReadableDataField ="columnName_isReadableDataField";
    private String columnName_isWritableDataField ="columnName_isWritableDataField";
    private String columnName_dataFieldOperations ="columnName_dataFieldOperations";

    private IndexedContainer containerDataSource;

    private String dataFieldsQueryId;
    private String dataFieldQueryType;
    private ActivityStepVO activityStep;
    private ActivityExposedDataFieldsEditor relatedActivityDataFieldsEditor;

    public ActivityExposedDataFieldsActionTable(UserClientInfo currentUserClientInfo, String tableHeight){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
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
        this.containerDataSource.addContainerProperty(columnName_isMandatoryDataField, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_isReadableDataField, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_isWritableDataField, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_dataFieldOperations, DataFieldsTableRowActions.class, null);
        setContainerDataSource(this.containerDataSource);
        setRowHeaderMode(RowHeaderMode.INDEX);
        setColumnHeaders(new String[]{
                userI18NProperties.
                        getProperty("ActivityManagement_Common_DataFieldNameText"),
                userI18NProperties.
                        getProperty("ActivityManagement_Common_DataFieldShortProp_DisplayNameText"),
                userI18NProperties.
                        getProperty("ActivityManagement_Common_DataFieldShortProp_TypeText"),
                userI18NProperties.
                        getProperty("ActivityManagement_Common_DataFieldShortProp_IsMandatoryText"),
                userI18NProperties.
                        getProperty("ActivityManagement_Common_DataFieldShortProp_IsReadableText"),
                userI18NProperties.
                        getProperty("ActivityManagement_Common_DataFieldShortProp_IsWritableText"),
                userI18NProperties.
                        getProperty("ActivityManagement_Table_ListActionPropertyText")
        });

        setColumnAlignment(columnName_DataFieldName, Align.LEFT);
        setColumnAlignment(columnName_DataFieldDisplayName, Align.LEFT);
        setColumnAlignment(columnName_DataFieldType, Align.CENTER);
        setColumnWidth(columnName_DataFieldType, 100);
        setColumnAlignment(columnName_isMandatoryDataField, Align.CENTER);
        setColumnWidth(columnName_isMandatoryDataField, 110);
        setColumnAlignment(columnName_isReadableDataField, Align.CENTER);
        setColumnWidth(columnName_isReadableDataField, 110);
        setColumnAlignment(columnName_isWritableDataField, Align.CENTER);
        setColumnWidth(columnName_isWritableDataField, 110);
        setColumnAlignment(columnName_dataFieldOperations, Align.CENTER);
        setColumnWidth(columnName_dataFieldOperations, 100);
    }

    @Override
    public void attach() {
        super.attach();
        if(getDataFieldQueryType()==null){
            return;
        }
        clearActivityDataData();
    }

    public void clearActivityDataData(){
        this.clear();
        this.containerDataSource.removeAllItems();
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

    public List<ActivityDataFieldVO> getExposedDataFields(){
        List<ActivityDataFieldVO> exposedDataFields=new ArrayList<ActivityDataFieldVO>();
        List itemIds=this.containerDataSource.getItemIds();
        for(Object itemId:itemIds){
            Item currentItem=this.containerDataSource.getItem(itemId);
            if(currentItem!=null){
                String dataFieldName=currentItem.getItemProperty(columnName_DataFieldName).getValue().toString();
                String isReadableStr=currentItem.getItemProperty(columnName_isReadableDataField).getValue().toString();
                String isWritableStr=currentItem.getItemProperty(columnName_isWritableDataField).getValue().toString();
                String isMandatoryStr=currentItem.getItemProperty(columnName_isMandatoryDataField).getValue().toString();
                ActivityDataFieldVO currentActivityDataFieldVO=new ActivityDataFieldVO();
                currentActivityDataFieldVO.setDataFieldName(dataFieldName);
                currentActivityDataFieldVO.setReadableField(Boolean.parseBoolean(isReadableStr));
                currentActivityDataFieldVO.setWritableField(Boolean.parseBoolean(isWritableStr));
                currentActivityDataFieldVO.setMandatoryField(Boolean.parseBoolean(isMandatoryStr));
                exposedDataFields.add(currentActivityDataFieldVO);
            }
        }
        return exposedDataFields;
    }

    public void addNewDataField(String dataFieldName,String dataFieldDisplayName,String dataFieldType,
                                boolean isMandatory,boolean isReadable,boolean isWritable){
        String id = dataFieldName;
        Item item = this.containerDataSource.addItem(id);
        item.getItemProperty(columnName_DataFieldName).setValue(dataFieldName);
        item.getItemProperty(columnName_DataFieldDisplayName).setValue(dataFieldDisplayName);
        item.getItemProperty(columnName_DataFieldType).setValue(dataFieldType);
        item.getItemProperty(columnName_isMandatoryDataField).setValue("" + isMandatory);
        item.getItemProperty(columnName_isReadableDataField).setValue(""+isReadable);
        item.getItemProperty(columnName_isWritableDataField).setValue(""+isWritable);
        DataFieldsTableRowActions actionButtons = new DataFieldsTableRowActions(this.currentUserClientInfo,id);
        actionButtons.setContainerActivityExposedDataFieldsActionTable(this);
        item.getItemProperty(columnName_dataFieldOperations).setValue(actionButtons);
    }

    public void doUpdateExistDataField(String dataFieldName,boolean isReadable,boolean isWritable,boolean isMandatory){
        Item itemToUpdate=this.containerDataSource.getItem(dataFieldName);
        if(itemToUpdate!=null){
            itemToUpdate.getItemProperty(columnName_isReadableDataField).setValue(""+isReadable);
            itemToUpdate.getItemProperty(columnName_isWritableDataField).setValue(""+isWritable);
            itemToUpdate.getItemProperty(columnName_isMandatoryDataField).setValue(""+isMandatory);
        }
    }

    public void updateDataField(String dataFieldName){
        Item itemToUpdate=this.containerDataSource.getItem(dataFieldName);
        if(itemToUpdate!=null){
            String dataFieldNameStr=itemToUpdate.getItemProperty(columnName_DataFieldName).getValue().toString();
            String dataFieldDisplayNameStr=itemToUpdate.getItemProperty(columnName_DataFieldDisplayName).getValue().toString();
            String isReadableStr=itemToUpdate.getItemProperty(columnName_isReadableDataField).getValue().toString();
            String isWritableStr=itemToUpdate.getItemProperty(columnName_isWritableDataField).getValue().toString();
            String isMandatoryStr=itemToUpdate.getItemProperty(columnName_isMandatoryDataField).getValue().toString();

            ActivityExposedDataFieldEditor  activityExposedDataFieldEditor=new ActivityExposedDataFieldEditor(this.currentUserClientInfo,ActivityExposedDataFieldEditor.EDITMODE_UPDATE);
            activityExposedDataFieldEditor.setRelatedActivityExposedDataFieldsActionTable(this);
            activityExposedDataFieldEditor.setCurrentDataFieldName(dataFieldNameStr);
            activityExposedDataFieldEditor.setCurrentDataFieldDisplayName(dataFieldDisplayNameStr);
            activityExposedDataFieldEditor.setCurrentDataFieldIsReadable(isReadableStr);
            activityExposedDataFieldEditor.setCurrentDataFieldIsWritable(isWritableStr);
            activityExposedDataFieldEditor.setCurrentDataFieldIsMandatory(isMandatoryStr);
            activityExposedDataFieldEditor.setRelatedActivityDataFieldsEditor(this.relatedActivityDataFieldsEditor);
            Window window = new Window();
            window.setWidth(670.0f, Unit.PIXELS);
            window.setHeight(300.0f, Unit.PIXELS);
            window.center();
            window.setModal(true);
            window.setContent(activityExposedDataFieldEditor);
            activityExposedDataFieldEditor.setContainerDialog(window);
            UI.getCurrent().addWindow(window);
        }
    }

    public void deleteExistDataField(final String dataFieldName) {
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        if(this.activityStep!=null){
            String[] stepProcessVariables=this.activityStep.getStepProcessVariables();
            if(stepProcessVariables!=null) {
                for (String currentProcessVariable : stepProcessVariables) {
                    if(currentProcessVariable.equals(dataFieldName)){
                        Notification errorNotification = new Notification(userI18NProperties.
                                getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                                userI18NProperties.
                                        getProperty("ActivityManagement_Common_FieldUsedInProcessVariablesText"), Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                        return;
                    }
                }
            }
        }
        Item itemToDelete = this.containerDataSource.getItem(dataFieldName);
        if (itemToDelete != null) {
            this.removeItem(dataFieldName);
        }
    }

    public void enableTableEdit(){
        List itemIds=this.containerDataSource.getItemIds();
        for(Object itemId:itemIds){
            Item currentItem=this.containerDataSource.getItem(itemId);
            if(currentItem!=null){
                DataFieldsTableRowActions actions=(DataFieldsTableRowActions)currentItem.getItemProperty(columnName_dataFieldOperations).getValue();
                actions.enableEditActions(true);
            }
        }
    }

    public void disableTableEdit(){
        List itemIds=this.containerDataSource.getItemIds();
        for(Object itemId:itemIds){
            Item currentItem=this.containerDataSource.getItem(itemId);
            if(currentItem!=null){
                DataFieldsTableRowActions actions=(DataFieldsTableRowActions)currentItem.getItemProperty(columnName_dataFieldOperations).getValue();
                actions.enableEditActions(false);
            }
        }
    }

    public void setActivityStep(ActivityStepVO activityStep) {
        this.activityStep = activityStep;
    }

    public void setRelatedActivityDataFieldsEditor(ActivityExposedDataFieldsEditor relatedActivityDataFieldsEditor) {
        this.relatedActivityDataFieldsEditor = relatedActivityDataFieldsEditor;
    }
}