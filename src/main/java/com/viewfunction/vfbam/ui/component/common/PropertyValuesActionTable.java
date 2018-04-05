package com.viewfunction.vfbam.ui.component.common;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertyValuesActionTable extends Table {
    private UserClientInfo currentUserClientInfo;
    private String columnName_propertyValue="columnName_propertyValue";
    private String columnName_propertyOperations="columnName_propertyOperations";
    private boolean allowEditOperation;
    private boolean allowRemoveOperation;
    private String[] currentPropertyValues;
    private List<String> currentPropertyValuesList;
    private boolean displayDeleteValueConfirmMessage=false;

    private String propertyEditorDisplayName;
    private IndexedContainer containerDataSource;
    public PropertyValuesActionTable(UserClientInfo currentUserClientInfo,String tableHeight,String propertyValueDisplayName,String propertyEditorDisplayName,boolean allowEditOperation,boolean allowRemoveOperation){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.propertyEditorDisplayName=propertyEditorDisplayName;
        this.allowEditOperation=allowEditOperation;
        this.allowRemoveOperation=allowRemoveOperation;
        this.currentPropertyValuesList=new ArrayList<String>();
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
        addStyleName("compact");
        addStyleName("small");

        this.containerDataSource = new IndexedContainer();
        this.containerDataSource.addContainerProperty(columnName_propertyValue, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_propertyOperations, PropertyValueTableRowActions.class, null);
        setContainerDataSource(this.containerDataSource);
        setColumnHeaders(new String[]{propertyValueDisplayName, userI18NProperties.
                getProperty("ActivityManagement_Table_ListActionPropertyText")});
        setRowHeaderMode(RowHeaderMode.INDEX);
        setColumnAlignment(columnName_propertyValue, Align.LEFT);
        setColumnAlignment(columnName_propertyOperations, Align.CENTER);
        setColumnWidth(columnName_propertyOperations, 100);
    }

    @Override
    public void attach() {
        super.attach();
        loadPropertyValuesData();
    }

    public void setPropertyValues(String[] propertyValuesArray){
        this.currentPropertyValues=propertyValuesArray;
        loadPropertyValuesData();
    }

    private void loadPropertyValuesData(){
        this.clear();
        this.containerDataSource.removeAllItems();
        if(this.currentPropertyValues!=null){
            for(String currentValue:this.currentPropertyValues){
                Item item = containerDataSource.addItem(currentValue);
                item.getItemProperty(columnName_propertyValue).setValue(currentValue);
                PropertyValueTableRowActions b = new PropertyValueTableRowActions(this.currentUserClientInfo,currentValue,this.allowEditOperation,this.allowRemoveOperation);
                b.setContainerPropertyValuesActionTable(this);
                item.getItemProperty(columnName_propertyOperations).setValue(b);
                this.currentPropertyValuesList.add(currentValue);
            }
        }
    }

    public String[] getPropertyValues(){
        List itemIds=this.containerDataSource.getItemIds();
        String[] propertyValues=new String[itemIds.size()];
        for(int i=0;i<itemIds.size();i++){
            propertyValues[i]=itemIds.get(i).toString();
        }
        return propertyValues;
    }

    public void enableTableFullEdit(){
        List itemIds=this.containerDataSource.getItemIds();
        for(Object itemId:itemIds){
            Item currentItem=this.containerDataSource.getItem(itemId);
            if(currentItem!=null){
                PropertyValueTableRowActions actions=(PropertyValueTableRowActions)currentItem.getItemProperty(columnName_propertyOperations).getValue();
                actions.enableEditAction(true);
                actions.enableDeleteAction(true);
            }
        }
    }

    public void disableTableFullEdit(){
        List itemIds=this.containerDataSource.getItemIds();
        for(Object itemId:itemIds){
            Item currentItem=this.containerDataSource.getItem(itemId);
            if(currentItem!=null){
                PropertyValueTableRowActions actions=(PropertyValueTableRowActions)currentItem.getItemProperty(columnName_propertyOperations).getValue();
                actions.enableEditAction(false);
                actions.enableDeleteAction(false);
            }
        }
    }

    public void updatePropertyValue(Object itemId){
        final PropertyValueEditor propertyValueEditor=new PropertyValueEditor(this.currentUserClientInfo,PropertyValueEditor.EDITMODE_UPDATE,this.propertyEditorDisplayName);
        propertyValueEditor.setRelatedPropertyValuesActionTable(this);
        propertyValueEditor.setCurrentPropertyValue(itemId.toString());
        Window window = new Window();
        window.setWidth(600.0f, Unit.PIXELS);
        window.setHeight(250.0f, Unit.PIXELS);
        window.center();
        window.setModal(true);
        window.setContent(propertyValueEditor);
        propertyValueEditor.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    public void deletePropertyValue(final Object itemId){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        if(this.displayDeleteValueConfirmMessage){
            Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                    getProperty("ActivityManagement_Common_ConfirmDeletePropertyValueText")+
                    " <b>"+itemId.toString() +"</b>.", ContentMode.HTML);
            final ConfirmDialog deletePropertyValueConfirmDialog = new ConfirmDialog();
            deletePropertyValueConfirmDialog.setConfirmMessage(confirmMessage);
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    doDeleteExistPropertyValue(itemId.toString());
                    //close confirm dialog
                    deletePropertyValueConfirmDialog.close();
                }
            };
            deletePropertyValueConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(deletePropertyValueConfirmDialog);
        }else{
            doDeleteExistPropertyValue(itemId.toString());
        }
    }

    public void addPropertyValue(){
        final PropertyValueEditor propertyValueEditor=new PropertyValueEditor(this.currentUserClientInfo,PropertyValueEditor.EDITMODE_NEW,this.propertyEditorDisplayName);
        propertyValueEditor.setRelatedPropertyValuesActionTable(this);
        Window window = new Window();
        window.setWidth(600.0f, Unit.PIXELS);
        window.setHeight(250.0f, Unit.PIXELS);
        window.center();
        window.setModal(true);
        window.setContent(propertyValueEditor);
        propertyValueEditor.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    public void doAddNewPropertyValue(String newPropertyValue){
        Item item = containerDataSource.addItem(newPropertyValue);
        item.getItemProperty(columnName_propertyValue).setValue(newPropertyValue);
        PropertyValueTableRowActions b = new PropertyValueTableRowActions(this.currentUserClientInfo,newPropertyValue,true,true);
        b.setContainerPropertyValuesActionTable(this);
        item.getItemProperty(columnName_propertyOperations).setValue(b);
        this.currentPropertyValuesList.add(newPropertyValue);
    }

    public void doUpdateExistPropertyValue(String originalValue,String newValue){
        containerDataSource.removeItem(originalValue);
        this.currentPropertyValuesList.remove(originalValue);
        Item item = containerDataSource.addItem(newValue);
        item.getItemProperty(columnName_propertyValue).setValue(newValue);
        PropertyValueTableRowActions b = new PropertyValueTableRowActions(this.currentUserClientInfo,newValue,true,true);
        b.setContainerPropertyValuesActionTable(this);
        item.getItemProperty(columnName_propertyOperations).setValue(b);
        this.currentPropertyValuesList.add(newValue);
    }

    public void doDeleteExistPropertyValue(String propertyValue){
        containerDataSource.removeItem(propertyValue);
        this.currentPropertyValuesList.remove(propertyValue);
    }

    public boolean checkValueExistance(String value){
        if(this.currentPropertyValuesList.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public void setDisplayDeleteValueConfirmMessage(boolean displayDeleteValueConfirmMessage) {
        this.displayDeleteValueConfirmMessage = displayDeleteValueConfirmMessage;
    }
}
