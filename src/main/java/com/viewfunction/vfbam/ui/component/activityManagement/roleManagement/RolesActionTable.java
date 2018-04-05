package com.viewfunction.vfbam.ui.component.activityManagement.roleManagement;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityManagementConst;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentModifyEvent;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentSelectedEvent;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RolesActionTable extends Table {
    private UserClientInfo currentUserClientInfo;
    private String columnName_RoleName ="columnName_RoleName";
    private String columnName_RoleDisplayName ="columnName_RoleDisplayName";
    private String columnName_RoleDescription ="columnName_RoleDescription";
    private String columnName_RoleOperations ="columnName_RoleOperations";

    private IndexedContainer containerDataSource;

    public static String ROLES_TYPE_PARTICIPANT ="ROLESUMMARY_TYPE_PARTICIPANT";
    public static String ROLES_TYPE_ROLEQUEUE="ROLESSUMMARY_TYPE_ROLEQUEUE";
    public static String ROLES_TYPE_ROLE="ROLES_TYPE_ROLE";
    private String rolesType;
    private String rolesQueryId;

    private boolean isActionMode;

    public RolesActionTable(UserClientInfo currentUserClientInfo,String tableHeight,boolean isActionMode){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.isActionMode=isActionMode;
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
            this.containerDataSource.addContainerProperty(columnName_RoleName, Button.class, null);
        }else{
            this.containerDataSource.addContainerProperty(columnName_RoleName, String.class, null);
        }
        this.containerDataSource.addContainerProperty(columnName_RoleDisplayName, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_RoleDescription, String.class, null);
        if(this.isActionMode){
            this.containerDataSource.addContainerProperty(columnName_RoleOperations, RolesTableRowActions.class, null);
            setRowHeaderMode(RowHeaderMode.INDEX);
        }
        setContainerDataSource(this.containerDataSource);
        if(this.isActionMode){
            setColumnHeaders(new String[]{userI18NProperties.
                    getProperty("ActivityManagement_RolesManagement_NamePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_RolesManagement_DisplayNamePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_RolesManagement_DescriptionPropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Table_ListActionPropertyText")});
        }else{
            setColumnHeaders(new String[]{userI18NProperties.
                    getProperty("ActivityManagement_RolesManagement_NamePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_RolesManagement_DisplayNamePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_RolesManagement_DescriptionPropertyText")});
        }

        setColumnAlignment(columnName_RoleName, Align.LEFT);
        setColumnAlignment(columnName_RoleDisplayName, Align.LEFT);
        setColumnAlignment(columnName_RoleDescription, Align.LEFT);
        if(this.isActionMode){
            setColumnAlignment(columnName_RoleOperations, Align.CENTER);
            setColumnWidth(columnName_RoleOperations, 160);
        }
    }

    @Override
    public void attach() {
        super.attach();
        if(rolesType==null){
            return;
        }
        loadRolesData();
    }

    public void loadRolesData(){
        this.clear();
        this.containerDataSource.removeAllItems();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        Role[] roles=null;
        if(rolesType.equals(ROLES_TYPE_ROLE)){
            ActivitySpaceMetaInfoDAO activitySpaceMetaInfoDAO=
                    ActivitySpaceOperationUtil.getActivitySpaceMetaInfo(activitySpaceName, new String[]{ActivitySpaceOperationUtil.ACTIVITYSPACE_METAINFOTYPE_ROLE});
            roles=activitySpaceMetaInfoDAO.getRoles();
        }
        if(rolesType.equals(ROLES_TYPE_ROLEQUEUE)){
            roles=ActivitySpaceOperationUtil.getRolesByRoleQueueName(activitySpaceName,rolesQueryId);
        }
        if(rolesType.equals(ROLES_TYPE_PARTICIPANT)){
            roles=ActivitySpaceOperationUtil.getRolesByParticipantName(activitySpaceName,rolesQueryId);
        }
        if(roles!=null){
            for(Role currentRole:roles){
                final String roleName=currentRole.getRoleName();
                Item item = this.containerDataSource.addItem(roleName);
                if(this.isActionMode){
                    Button switchToRoleViewButton = new Button(roleName);
                    switchToRoleViewButton.setIcon(FontAwesome.HAND_O_RIGHT);
                    switchToRoleViewButton.addStyleName("small");
                    //switchToParticipantViewButton.addStyleName("borderless");
                    //switchToParticipantViewButton.addStyleName("link");
                    switchToRoleViewButton.addStyleName("quiet");
                    switchToRoleViewButton.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            switchToRoleDetailView(roleName);
                        }
                    });
                    item.getItemProperty(columnName_RoleName).setValue(switchToRoleViewButton);
                    RolesTableRowActions b = new RolesTableRowActions(this.currentUserClientInfo,roleName);
                    item.getItemProperty(columnName_RoleOperations).setValue(b);
                }else{
                    item.getItemProperty(columnName_RoleName).setValue(roleName);
                }
                item.getItemProperty(columnName_RoleDisplayName).setValue(currentRole.getDisplayName());
                item.getItemProperty(columnName_RoleDescription).setValue(currentRole.getDescription());
            }
        }
    }

    private void switchToRoleDetailView(String roleId){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String componentType= ActivityManagementConst.COMPONENT_TYPE_ROLE;
        ActivitySpaceComponentSelectedEvent activitySpaceComponentSelectedEvent=
                new ActivitySpaceComponentSelectedEvent(activitySpaceName,componentType,roleId);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentSelectedEvent);
    }

    public void addRole(final String roleName,String roleDisplayName,String roleDescription){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        boolean addRoleResult=ActivitySpaceOperationUtil.addNewRole(activitySpaceName,roleName,roleDisplayName,roleDescription);
        if(addRoleResult){
            String id = roleName;
            Item item = this.containerDataSource.addItem(id);
            Button switchToRoleViewButton = new Button(roleName);
            switchToRoleViewButton.setIcon(FontAwesome.HAND_O_RIGHT);
            switchToRoleViewButton.addStyleName("small");
            //switchToParticipantViewButton.addStyleName("borderless");
            //switchToParticipantViewButton.addStyleName("link");
            switchToRoleViewButton.addStyleName("quiet");
            switchToRoleViewButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    switchToRoleDetailView(roleName);
                }
            });
            item.getItemProperty(columnName_RoleName).setValue(switchToRoleViewButton);
            item.getItemProperty(columnName_RoleDisplayName).setValue(roleDisplayName);
            RolesTableRowActions b = new RolesTableRowActions(this.currentUserClientInfo,roleName);
            item.getItemProperty(columnName_RoleOperations).setValue(b);
            item.getItemProperty(columnName_RoleDescription).setValue(roleDescription);
            // board added role success message
            broadcastAddedRoleEvent(id);
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_RolesManagement_AddRoleErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    public void setRolesType(String rolesType) {
        this.rolesType = rolesType;
    }

    public void setRolesQueryId(String rolesQueryId) {
        this.rolesQueryId = rolesQueryId;
    }

    private void broadcastAddedRoleEvent(String roleId){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String componentType= ActivityManagementConst.COMPONENT_TYPE_ROLE;
        ActivitySpaceComponentModifyEvent activitySpaceComponentModifyEvent=
                new ActivitySpaceComponentModifyEvent(activitySpaceName,componentType,roleId,
                        ActivitySpaceComponentModifyEvent.MODIFYTYPE_ADD);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentModifyEvent);
    }

    public boolean checkRoleExistence(String roleName){
        Item targetItem =containerDataSource.getItem(roleName);
        if(targetItem!=null){
            return true;
        }else{
            return false;
        }
    }
}
