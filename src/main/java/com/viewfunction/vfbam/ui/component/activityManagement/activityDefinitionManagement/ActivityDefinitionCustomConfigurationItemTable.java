package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.viewfunction.activityEngine.activityView.common.CustomStructure;
import com.viewfunction.activityEngine.util.ActivitySpaceCommonConstant;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.List;
import java.util.Properties;

/**
 * Created by wangychu on 1/24/17.
 */
public class ActivityDefinitionCustomConfigurationItemTable extends Table {
    private UserClientInfo currentUserClientInfo;
    private String columnName_CustomConfigurationName ="columnName_CustomConfigurationName";
    private String columnName_CustomConfigurationOperations ="columnName_CustomConfigurationOperations";
    private IndexedContainer containerDataSource;
    private String configurationItemType;
    private boolean canDeleteItem;
    private CustomStructure parentContainerConfigurationItem;

    public ActivityDefinitionCustomConfigurationItemTable(UserClientInfo currentUserClientInfo,String tableHeight,String configurationItemDisplayName,boolean canDeleteItem){
        this.currentUserClientInfo=currentUserClientInfo;
        this.canDeleteItem=canDeleteItem;
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
        this.containerDataSource.addContainerProperty(columnName_CustomConfigurationName, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_CustomConfigurationOperations, ActivityDefinitionCustomConfigurationItemTableRowActions.class, null);
        setRowHeaderMode(Table.RowHeaderMode.INDEX);
        setContainerDataSource(this.containerDataSource);
        setColumnAlignment(columnName_CustomConfigurationName, Table.Align.LEFT);
        setColumnAlignment(columnName_CustomConfigurationOperations, Table.Align.CENTER);
        setColumnWidth(columnName_CustomConfigurationOperations, 160);
        if(configurationItemDisplayName!=null){
            setColumnHeaders(new String[]{configurationItemDisplayName,
                    userI18NProperties.
                            getProperty("ActivityManagement_Table_ListActionPropertyText")});
        }else{
            setColumnHeaders(new String[]{userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_ConfigItemNameText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Table_ListActionPropertyText")});
        }
    }

    @Override
    public void attach() {
        super.attach();
    }

    public void loadConfigurationItemsData(List<String> configurationItems){
        Properties userI18NProperties = this.currentUserClientInfo.getUserI18NProperties();
        this.clear();
        this.containerDataSource.removeAllItems();
        for(String currentConfigurationItem:configurationItems){
            Item item = this.containerDataSource.addItem(currentConfigurationItem);
            if(currentConfigurationItem.equals(ActivitySpaceCommonConstant.ActivityDefinition_launchPointLogicStepId)){
                item.getItemProperty(columnName_CustomConfigurationName).setValue(userI18NProperties.
                        getProperty("ActivityManagement_ActivityTypeManagement_LaunchPointText"));
            }else{
                item.getItemProperty(columnName_CustomConfigurationName).setValue(currentConfigurationItem);
            }
            ActivityDefinitionCustomConfigurationItemTableRowActions currentRowActions=
                    new ActivityDefinitionCustomConfigurationItemTableRowActions(this.currentUserClientInfo,this.canDeleteItem);
            currentRowActions.setConfigurationItemType(getConfigurationItemType());
            currentRowActions.setConfigurationItemName(currentConfigurationItem);
            currentRowActions.setContainerActivityDefinitionCustomConfigurationItemTable(this);
            item.getItemProperty(columnName_CustomConfigurationOperations).setValue(currentRowActions);
        }
    }

    public String getConfigurationItemType() {
        return configurationItemType;
    }

    public void setConfigurationItemType(String configurationItemType) {
        this.configurationItemType = configurationItemType;
    }

    public boolean checkConfigurationItemExistence(String itemName){
        Item item = this.containerDataSource.getItem(itemName);
        if(item==null){
            return false;
        }else{
            return true;
        }
    }

    public void addNewConfigurationItem(String configurationItemName,boolean canDeleteThisItem){
        Item item = this.containerDataSource.addItem(configurationItemName);
        item.getItemProperty(columnName_CustomConfigurationName).setValue(configurationItemName);
        ActivityDefinitionCustomConfigurationItemTableRowActions currentRowActions=
                new ActivityDefinitionCustomConfigurationItemTableRowActions(this.currentUserClientInfo,canDeleteThisItem);
        currentRowActions.setConfigurationItemType(getConfigurationItemType());
        currentRowActions.setConfigurationItemName(configurationItemName);
        currentRowActions.setContainerActivityDefinitionCustomConfigurationItemTable(this);
        item.getItemProperty(columnName_CustomConfigurationOperations).setValue(currentRowActions);
    }

    public void deleteConfigurationItem(String configurationItemName){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        if(getParentContainerConfigurationItem()!=null){
            boolean deleteConfigurationItemResult=ActivitySpaceOperationUtil.deleteSubCustomStructure(getParentContainerConfigurationItem(),configurationItemName);
            if(deleteConfigurationItemResult){
                this.containerDataSource.removeItem(configurationItemName);
                Notification resultNotification = new Notification(userI18NProperties.
                        getProperty("Global_Application_DataOperation_DeleteDataSuccessText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_DeleteConfigurationItemSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                resultNotification.setPosition(Position.MIDDLE_CENTER);
                resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                resultNotification.show(Page.getCurrent());
            }else{
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("ActivityManagement_Common_DeleteConfigurationItemErrorText"),
                        userI18NProperties.
                                getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
            }
        }
    }

    public CustomStructure getParentContainerConfigurationItem() {
        return parentContainerConfigurationItem;
    }

    public void setParentContainerConfigurationItem(CustomStructure parentContainerConfigurationItem) {
        this.parentContainerConfigurationItem = parentContainerConfigurationItem;
    }
}
