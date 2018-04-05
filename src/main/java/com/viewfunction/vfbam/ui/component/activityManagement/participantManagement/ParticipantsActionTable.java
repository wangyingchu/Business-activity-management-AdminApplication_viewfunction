package com.viewfunction.vfbam.ui.component.activityManagement.participantManagement;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityManagementConst;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentModifyEvent;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentSelectedEvent;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import com.viewfunction.activityEngine.security.Participant;

import java.util.Properties;

public class ParticipantsActionTable extends Table {
    private UserClientInfo currentUserClientInfo;
    private String columnName_ParticipanName="columnName_ParticipanName";
    private String columnName_ParticipanDisplayName="columnName_ParticipanDisplayName";
    private String columnName_ParticipanType="columnName_ParticipanType";
    private String columnName_ParticipanOperations="columnName_ParticipanOperations";

    private IndexedContainer containerDataSource;

    public static String PARTICIPANTS_TYPE_ROLE="PARTICIPANTS_TYPE_ROLE";
    public static String PARTICIPANTS_TYPE_PARTICIPANT="PARTICIPANTS_TYPE_PARTICIPANT";

    private String participantsType;
    private String participantsQueryId;
    private boolean isActionMode;
    private boolean allowRemoveOperation;

    public ParticipantsActionTable(UserClientInfo currentUserClientInfo,String tableHeight,boolean isActionMode,boolean allowRemoveOperation){
        this.currentUserClientInfo=currentUserClientInfo;
        this.isActionMode=isActionMode;
        this.allowRemoveOperation=allowRemoveOperation;
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
        if(this.isActionMode){
            this.containerDataSource.addContainerProperty(columnName_ParticipanName, Button.class, null);
        }else{
            this.containerDataSource.addContainerProperty(columnName_ParticipanName, String.class, null);
        }
        this.containerDataSource.addContainerProperty(columnName_ParticipanDisplayName, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_ParticipanType, String.class, null);
        if(this.isActionMode) {
            this.containerDataSource.addContainerProperty(columnName_ParticipanOperations, ParticipantsTableRowActions.class, null);
            setRowHeaderMode(RowHeaderMode.INDEX);
        }
        setContainerDataSource(this.containerDataSource);
        if(this.isActionMode) {
            setColumnHeaders(new String[]{ userI18NProperties.
                    getProperty("ActivityManagement_ParticipantsManagement_NamePropertyText"),
                    userI18NProperties.
                    getProperty("ActivityManagement_ParticipantsManagement_DisplayPropertyText"),
                    userI18NProperties.
                    getProperty("ActivityManagement_ParticipantsManagement_TypePropertyText"),
                    userI18NProperties.
                    getProperty("ActivityManagement_Table_ListActionPropertyText")});
        }else{
            setColumnHeaders(new String[]{ userI18NProperties.
                    getProperty("ActivityManagement_ParticipantsManagement_NamePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_ParticipantsManagement_DisplayPropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_ParticipantsManagement_TypePropertyText")});
        }
        setColumnAlignment(columnName_ParticipanName, Align.LEFT);
        setColumnAlignment(columnName_ParticipanDisplayName, Align.LEFT);
        setColumnAlignment(columnName_ParticipanType, Align.CENTER);
        setColumnWidth(columnName_ParticipanType, 200);
        if(this.isActionMode) {
            setColumnAlignment(columnName_ParticipanOperations, Align.CENTER);
            setColumnWidth(columnName_ParticipanOperations, 180);
        }
    }

    @Override
    public void attach() {
        super.attach();
        if(participantsType==null){
            return;
        }
        loadParticipantsData();
    }

    public void loadParticipantsData(){
        this.clear();
        this.containerDataSource.removeAllItems();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        Participant[] participants=null;
        if(participantsType.equals(PARTICIPANTS_TYPE_PARTICIPANT)){
            ActivitySpaceMetaInfoDAO activitySpaceMetaInfoDAO=
                    ActivitySpaceOperationUtil.getActivitySpaceMetaInfo(activitySpaceName, new String[]{ActivitySpaceOperationUtil.ACTIVITYSPACE_METAINFOTYPE_PARTICIPANT});
            participants=activitySpaceMetaInfoDAO.getParticipants();
        }else if(participantsType.equals(PARTICIPANTS_TYPE_ROLE)){
            participants=ActivitySpaceOperationUtil.getParticipantsByRoleName(activitySpaceName,participantsQueryId);
        }
        if(participants!=null){
            for(Participant currentParticipant:participants) {
                final String participantName = currentParticipant.getParticipantName();
                Item item = containerDataSource.addItem(participantName);
                item.getItemProperty(columnName_ParticipanDisplayName).setValue(currentParticipant.getDisplayName());
                if(currentParticipant.isGroup()){
                    item.getItemProperty(columnName_ParticipanType).setValue("Group");
                }else{
                    item.getItemProperty(columnName_ParticipanType).setValue("User");
                }
                if(this.isActionMode) {
                    Button switchToParticipantViewButton = new Button(participantName);
                    switchToParticipantViewButton.setIcon(FontAwesome.HAND_O_RIGHT);
                    switchToParticipantViewButton.addStyleName("small");
                    //switchToParticipantViewButton.addStyleName("borderless");
                    //switchToParticipantViewButton.addStyleName("link");
                    switchToParticipantViewButton.addStyleName("quiet");
                    switchToParticipantViewButton.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            switchToParticipantDetailView(participantName);
                        }
                    });
                    item.getItemProperty(columnName_ParticipanName).setValue(switchToParticipantViewButton);
                    ParticipantsTableRowActions b = new ParticipantsTableRowActions(this.currentUserClientInfo,participantName,this.allowRemoveOperation);
                    item.getItemProperty(columnName_ParticipanOperations).setValue(b);
                }else{
                    item.getItemProperty(columnName_ParticipanName).setValue(participantName);
                }
            }
        }
    }

    private void switchToParticipantDetailView(String participantId){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String componentType= ActivityManagementConst.COMPONENT_TYPE_PARTICIPANT;
        ActivitySpaceComponentSelectedEvent activitySpaceComponentSelectedEvent=
                new ActivitySpaceComponentSelectedEvent(activitySpaceName,componentType,participantId);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentSelectedEvent);
    }

    public void addParticipant(final String participantName,String participantDisplayName,String participantType){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        boolean addParticipantResult=ActivitySpaceOperationUtil.addNewParticipant(activitySpaceName,participantName,participantDisplayName,participantType);
        if(addParticipantResult){
            Notification resultNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_AddDataSuccessText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_ParticipantsManagement_AddParticipantSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
            String id = participantName;
            Item item = containerDataSource.addItem(id);
            Button switchToParticipantViewButton = new Button(participantName);
            switchToParticipantViewButton.setIcon(FontAwesome.HAND_O_RIGHT);
            switchToParticipantViewButton.addStyleName("small");
            switchToParticipantViewButton.addStyleName("quiet");
            switchToParticipantViewButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    switchToParticipantDetailView(participantName);
                }
            });
            item.getItemProperty(columnName_ParticipanName).setValue(switchToParticipantViewButton);
            item.getItemProperty(columnName_ParticipanDisplayName).setValue(participantDisplayName);
            ParticipantsTableRowActions b = new ParticipantsTableRowActions(this.currentUserClientInfo,participantName,this.allowRemoveOperation);
            item.getItemProperty(columnName_ParticipanOperations).setValue(b);
            item.getItemProperty(columnName_ParticipanType).setValue(participantType);
            // board added participant success message
            broadcastAddedParticipantEvent(participantName);
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_ParticipantsManagement_AddParticipantErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    private void broadcastAddedParticipantEvent(String participantId){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String componentType= ActivityManagementConst.COMPONENT_TYPE_PARTICIPANT;
        ActivitySpaceComponentModifyEvent activitySpaceComponentModifyEvent=
                new ActivitySpaceComponentModifyEvent(activitySpaceName,componentType,participantId,
                        ActivitySpaceComponentModifyEvent.MODIFYTYPE_ADD);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentModifyEvent);
    }

    public void setParticipantsType(String participantsType) {
        this.participantsType = participantsType;
    }

    public void setParticipantsQueryId(String participantsQueryId) {
        this.participantsQueryId = participantsQueryId;
    }

    public boolean checkParticipantExistence(String participantName){
        Item targetItem =containerDataSource.getItem(participantName);
        if(targetItem!=null){
            return true;
        }else{
            return false;
        }
    }
}
