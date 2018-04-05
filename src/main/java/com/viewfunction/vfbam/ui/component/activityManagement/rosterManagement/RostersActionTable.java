package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityManagementConst;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentModifyEvent;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentSelectedEvent;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RostersActionTable extends Table {
    private UserClientInfo currentUserClientInfo;
    private String columnName_RosterName ="columnName_RosterName";
    private String columnName_RosterDisplayName ="columnName_RosterDisplayName";
    private String columnName_RosterDescription ="columnName_RosterDescription";
    private String columnName_RosterOperations ="columnName_RosterOperations";

    private IndexedContainer containerDataSource;

    public RostersActionTable(UserClientInfo currentUserClientInfo, String tableHeight){
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

        this.containerDataSource= new IndexedContainer();
        this.containerDataSource.addContainerProperty(columnName_RosterName, Button.class, null);
        this.containerDataSource.addContainerProperty(columnName_RosterDisplayName, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_RosterDescription, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_RosterOperations, RostersTableRowActions.class, null);
        setContainerDataSource(this.containerDataSource);

        setRowHeaderMode(Table.RowHeaderMode.INDEX);
        setColumnHeaders(new String[]{
                userI18NProperties.
                        getProperty("ActivityManagement_RosterManagement_NamePropertyText"),
                userI18NProperties.
                        getProperty("ActivityManagement_RosterManagement_DisplayNamePropertyText"),
                userI18NProperties.
                        getProperty("ActivityManagement_RosterManagement_DescriptionPropertyText"),
                userI18NProperties.
                        getProperty("ActivityManagement_Table_ListActionPropertyText")

        });
        setColumnAlignment(columnName_RosterName, Table.Align.LEFT);
        //setColumnWidth(columnName_RosterName, 200);
        setColumnAlignment(columnName_RosterDisplayName, Table.Align.LEFT);
        //setColumnWidth(columnName_RosterDisplayName, 200);
        setColumnAlignment(columnName_RosterDescription, Table.Align.CENTER);
        //setColumnWidth(columnName_RosterDescription, 200);
        setColumnAlignment(columnName_RosterOperations, Table.Align.CENTER);
        setColumnWidth(columnName_RosterOperations, 200);
    }

    @Override
    public void attach() {
        super.attach();
        loadRostersData();
    }

    private void loadRostersData(){
        this.clear();
        this.containerDataSource.removeAllItems();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        ActivitySpaceMetaInfoDAO activitySpaceMetaInfoDAO=
                ActivitySpaceOperationUtil.getActivitySpaceMetaInfo(activitySpaceName, new String[]{ActivitySpaceOperationUtil.ACTIVITYSPACE_METAINFOTYPE_ROSTER});
        Roster[] rosters=activitySpaceMetaInfoDAO.getRosters();
        if(rosters!=null){
            for(Roster currentRoster:rosters){
                final String rosterName=currentRoster.getRosterName();
                Item item = this.containerDataSource.addItem(rosterName);
                Button switchToRosterViewButton = new Button(rosterName);
                switchToRosterViewButton.setIcon(FontAwesome.HAND_O_RIGHT);
                switchToRosterViewButton.addStyleName("small");
                //switchToRosterViewButton.addStyleName("borderless");
                //switchToRosterViewButton.addStyleName("link");
                switchToRosterViewButton.addStyleName("quiet");
                switchToRosterViewButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        switchToRosterDetailView(rosterName);
                    }
                });
                item.getItemProperty(columnName_RosterName).setValue(switchToRosterViewButton);
                item.getItemProperty(columnName_RosterDisplayName).setValue(currentRoster.getDisplayName());
                RostersTableRowActions actions = new RostersTableRowActions(this.currentUserClientInfo,rosterName);
                actions.setContainerRostersActionTable(this);
                item.getItemProperty(columnName_RosterOperations).setValue(actions);
                item.getItemProperty(columnName_RosterDescription).setValue(currentRoster.getDescription());
            }
        }
    }

    private void switchToRosterDetailView(String rosterId){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String componentType= ActivityManagementConst.COMPONENT_TYPE_ROSTER;
        ActivitySpaceComponentSelectedEvent activitySpaceComponentSelectedEvent=
                new ActivitySpaceComponentSelectedEvent(activitySpaceName,componentType,rosterId);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentSelectedEvent);
    }

    public void addRoster(final String rosterName,String rosterDisplayName,String rosterDescription){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        boolean addRosterResult=ActivitySpaceOperationUtil.addNewRoster(activitySpaceName,rosterName,rosterDisplayName,rosterDescription);
        if(addRosterResult){
            String id = rosterName;
            Item item = this.containerDataSource.addItem(id);
            Button switchToRosterViewButton = new Button(rosterName);
            switchToRosterViewButton.setIcon(FontAwesome.HAND_O_RIGHT);
            switchToRosterViewButton.addStyleName("small");
            //switchToRosterViewButton.addStyleName("borderless");
            //switchToRosterViewButton.addStyleName("link");
            switchToRosterViewButton.addStyleName("quiet");
            switchToRosterViewButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    switchToRosterDetailView(rosterName);
                }
            });
            item.getItemProperty(columnName_RosterName).setValue(switchToRosterViewButton);
            item.getItemProperty(columnName_RosterDisplayName).setValue(rosterDisplayName);
            RostersTableRowActions actions = new RostersTableRowActions(this.currentUserClientInfo,rosterName);
            actions.setContainerRostersActionTable(this);
            item.getItemProperty(columnName_RosterOperations).setValue(actions);
            item.getItemProperty(columnName_RosterDescription).setValue(rosterDescription);
            // board added roster success message
            broadcastAddedRosterEvent(id);
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_RosterManagement_AddRosterErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    public void removeRoster(String rosterName){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        boolean removeRosterResult=ActivitySpaceOperationUtil.removeRoster(activitySpaceName,rosterName);
        if(removeRosterResult){

            Notification resultNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DeleteDataSuccessText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_RosterManagement_DeleteRosterSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());

            //broadcast role queue remove message
            broadcastRemovedRosterEvent(rosterName);
            Item itemToRemove=this.containerDataSource.getItem(rosterName);
            if(itemToRemove!=null){
                this.containerDataSource.removeItem(rosterName);
            }
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_RosterManagement_DeleteRosterErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    private void broadcastAddedRosterEvent(String roleId){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String componentType= ActivityManagementConst.COMPONENT_TYPE_ROSTER;
        ActivitySpaceComponentModifyEvent activitySpaceComponentModifyEvent=
                new ActivitySpaceComponentModifyEvent(activitySpaceName,componentType,roleId,
                        ActivitySpaceComponentModifyEvent.MODIFYTYPE_ADD);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentModifyEvent);
    }

    private void broadcastRemovedRosterEvent(String rostedName){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String componentType= ActivityManagementConst.COMPONENT_TYPE_ROSTER;
        ActivitySpaceComponentModifyEvent activitySpaceComponentModifyEvent=
                new ActivitySpaceComponentModifyEvent(activitySpaceName,componentType,rostedName,
                        ActivitySpaceComponentModifyEvent.MODIFYTYPE_REMOVE);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentModifyEvent);
    }

    public boolean checkRosterExistence(String rosterName){
        Item targetItem =containerDataSource.getItem(rosterName);
        if(targetItem!=null){
            return true;
        }else{
            return false;
        }
    }
}
