package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityManagementConst;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentModifyEvent;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentSelectedEvent;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RoleQueuesActionTable extends Table {
    private UserClientInfo currentUserClientInfo;
    private String columnName_RoleQueueName ="columnName_RoleQueueName";
    private String columnName_RoleQueueDisplayName ="columnName_RoleQueueDisplayName";
    private String columnName_RoleQueueDescription ="columnName_RoleQueueDescription";
    private String columnName_RoleQueueOperations ="columnName_RoleQueueOperations";

    private IndexedContainer containerDataSource;

    public static String ROLEQUEUES_TYPE_ROLE="ROLEQUEUES_TYPE_ROLE";
    public static String ROLEQUEUES_TYPE_ROLEQUEUE="ROLEQUEUES_TYPE_ROLEQUEUE";

    private String roleQueuesType;
    private String roleQueuesQueryId;

    private boolean isActionMode;
    private boolean allowRemoveOperation;
    public RoleQueuesActionTable(UserClientInfo currentUserClientInfo,String tableHeight,boolean isActionMode,boolean allowRemoveOperation){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.isActionMode=isActionMode;
        this.allowRemoveOperation=allowRemoveOperation;
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
        if(isActionMode){
            this.containerDataSource.addContainerProperty(columnName_RoleQueueName, Button.class, null);
        }else{
            this.containerDataSource.addContainerProperty(columnName_RoleQueueName, String.class, null);
        }
        this.containerDataSource.addContainerProperty(columnName_RoleQueueDisplayName, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_RoleQueueDescription, String.class, null);
        if(isActionMode) {
            this.containerDataSource.addContainerProperty(columnName_RoleQueueOperations, RoleQueuesTableRowActions.class, null);
            setRowHeaderMode(Table.RowHeaderMode.INDEX);
        }
        setContainerDataSource(this.containerDataSource);
        if(isActionMode){
            setColumnHeaders(new String[]{userI18NProperties.
                    getProperty("ActivityManagement_RoleQueuesManagement_NamePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_RoleQueuesManagement_DisplayNamePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_RoleQueuesManagement_DescriptionPropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Table_ListActionPropertyText")});
        }else{
            setColumnHeaders(new String[]{userI18NProperties.
                    getProperty("ActivityManagement_RoleQueuesManagement_NamePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_RoleQueuesManagement_DisplayNamePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_RoleQueuesManagement_DescriptionPropertyText")});
        }
        setColumnAlignment(columnName_RoleQueueName, Table.Align.LEFT);
        setColumnAlignment(columnName_RoleQueueDisplayName, Table.Align.LEFT);
        setColumnAlignment(columnName_RoleQueueDescription, Table.Align.LEFT);
        if(isActionMode) {
            setColumnAlignment(columnName_RoleQueueOperations, Table.Align.CENTER);
            setColumnWidth(columnName_RoleQueueOperations, 200);
        }
    }

    @Override
    public void attach() {
        super.attach();
        if(roleQueuesType==null){
            return;
        }
        loadRoleQueuesData();
    }

    public void loadRoleQueuesData(){
        this.clear();
        this.containerDataSource.removeAllItems();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        RoleQueue[] roleQueues=null;
        if(roleQueuesType.equals(ROLEQUEUES_TYPE_ROLEQUEUE)){
            ActivitySpaceMetaInfoDAO activitySpaceMetaInfoDAO=
                    ActivitySpaceOperationUtil.getActivitySpaceMetaInfo(activitySpaceName, new String[]{ActivitySpaceOperationUtil.ACTIVITYSPACE_METAINFOTYPE_ROLEQUEUE});
            roleQueues=activitySpaceMetaInfoDAO.getRoleQueues();
        }
        if(roleQueuesType.equals(ROLEQUEUES_TYPE_ROLE)){
            roleQueues=ActivitySpaceOperationUtil.getRoleQueuesByRoleName(activitySpaceName, roleQueuesQueryId);
        }
        if(roleQueues!=null){
            for(RoleQueue currentRoleQueue:roleQueues){
                final String roleQueueName=currentRoleQueue.getQueueName();
                Item item = this.containerDataSource.addItem(roleQueueName);
                item.getItemProperty(columnName_RoleQueueDisplayName).setValue(currentRoleQueue.getDisplayName());
                item.getItemProperty(columnName_RoleQueueDescription).setValue(currentRoleQueue.getDescription());
                if(isActionMode){
                    Button switchToRoleQueueViewButton = new Button(roleQueueName);
                    switchToRoleQueueViewButton.setIcon(FontAwesome.HAND_O_RIGHT);
                    switchToRoleQueueViewButton.addStyleName("small");
                    //switchToRoleQueueViewButton.addStyleName("borderless");
                    //switchToRoleQueueViewButton.addStyleName("link");
                    switchToRoleQueueViewButton.addStyleName("quiet");
                    switchToRoleQueueViewButton.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            switchToRoleQueueDetailView(roleQueueName);
                        }
                    });
                    item.getItemProperty(columnName_RoleQueueName).setValue(switchToRoleQueueViewButton);
                    RoleQueuesTableRowActions actions = new RoleQueuesTableRowActions(this.currentUserClientInfo,roleQueueName,this.allowRemoveOperation);
                    actions.setContainerRoleQueuesActionTable(this);
                    item.getItemProperty(columnName_RoleQueueOperations).setValue(actions);
                }else{
                    item.getItemProperty(columnName_RoleQueueName).setValue(roleQueueName);
                }
            }
        }
    }

    private void switchToRoleQueueDetailView(String roleQueueId){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String componentType= ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE;
        ActivitySpaceComponentSelectedEvent activitySpaceComponentSelectedEvent=
                new ActivitySpaceComponentSelectedEvent(activitySpaceName,componentType,roleQueueId);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentSelectedEvent);
    }

    public void addRoleQueue(final String roleQueueName,String roleQueueDisplayName,String roleQueueDescription){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        boolean addRoleQueueResult=ActivitySpaceOperationUtil.addNewRoleQueue(activitySpaceName,roleQueueName,roleQueueDisplayName,roleQueueDescription);
        if(addRoleQueueResult){
            String id = roleQueueName;
            Item item = this.containerDataSource.addItem(id);

            Button switchToRoleQueueViewButton = new Button(roleQueueName);
            switchToRoleQueueViewButton.setIcon(FontAwesome.HAND_O_RIGHT);
            switchToRoleQueueViewButton.addStyleName("small");
            //switchToRoleQueueViewButton.addStyleName("borderless");
            //switchToRoleQueueViewButton.addStyleName("link");
            switchToRoleQueueViewButton.addStyleName("quiet");
            switchToRoleQueueViewButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    switchToRoleQueueDetailView(roleQueueName);
                }
            });
            item.getItemProperty(columnName_RoleQueueName).setValue(switchToRoleQueueViewButton);
            item.getItemProperty(columnName_RoleQueueDisplayName).setValue(roleQueueDisplayName);
            RoleQueuesTableRowActions actions = new RoleQueuesTableRowActions(this.currentUserClientInfo,roleQueueName,this.allowRemoveOperation);
            actions.setContainerRoleQueuesActionTable(this);
            item.getItemProperty(columnName_RoleQueueOperations).setValue(actions);
            item.getItemProperty(columnName_RoleQueueDescription).setValue(roleQueueDescription);
            // board added role queue  success message
            broadcastAddedRoleQueueEvent(id);
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_RoleQueuesManagement_AddRoleQueueErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    public void setRoleQueuesType(String roleQueuesType) {
        this.roleQueuesType = roleQueuesType;
    }

    public void setRoleQueuesQueryId(String roleQueuesQueryId) {
        this.roleQueuesQueryId = roleQueuesQueryId;
    }

    public void removeRoleQueue(final String roleQueueName){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        boolean removeRoleQueueResult=ActivitySpaceOperationUtil.removeRoleQueue(activitySpaceName,roleQueueName);
        if(removeRoleQueueResult){
            Notification resultNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DeleteDataSuccessText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_RoleQueuesManagement_RemoveRoleQueueSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
            //broadcast role queue remove message
            broadcastRemovedRoleQueueEvent(roleQueueName);
            Item itemToRemove=this.containerDataSource.getItem(roleQueueName);
            if(itemToRemove!=null){
                this.containerDataSource.removeItem(roleQueueName);
            }
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_RoleQueuesManagement_RemoveRoleQueueErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    private void broadcastAddedRoleQueueEvent(String roleQueueName){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String componentType= ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE;
        ActivitySpaceComponentModifyEvent activitySpaceComponentModifyEvent=
                new ActivitySpaceComponentModifyEvent(activitySpaceName,componentType,roleQueueName,
                        ActivitySpaceComponentModifyEvent.MODIFYTYPE_ADD);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentModifyEvent);
    }

    private void broadcastRemovedRoleQueueEvent(String roleQueueName){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String componentType= ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE;
        ActivitySpaceComponentModifyEvent activitySpaceComponentModifyEvent=
                new ActivitySpaceComponentModifyEvent(activitySpaceName,componentType,roleQueueName,
                        ActivitySpaceComponentModifyEvent.MODIFYTYPE_REMOVE);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentModifyEvent);
    }

    public boolean checkRoleQueueExistence(String roleQueueName){
        Item targetItem =containerDataSource.getItem(roleQueueName);
        if(targetItem!=null){
            return true;
        }else{
            return false;
        }
    }
}
