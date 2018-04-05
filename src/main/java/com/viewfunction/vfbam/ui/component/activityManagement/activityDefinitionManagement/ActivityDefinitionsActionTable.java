package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityManagementConst;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentSelectedEvent;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ActivityDefinitionsActionTable extends Table{
    private UserClientInfo currentUserClientInfo;
    private String columnName_ActivityType ="columnName_ActivityType";
    private String columnName_RosterName ="columnName_RosterName";
    private String columnName_Status ="columnName_Status";
    private String columnName_ActivityTypeOperations ="columnName_ActivityTypeOperations";

    private IndexedContainer containerDataSource;

    public static String ACTIVITYDEFINITIONS_TYPE_ACTIVITYDEFINITION="ACTIVITYDEFINITIONS_TYPE_ACTIVITYDEFINITION";
    public static String ACTIVITYDEFINITIONS_TYPE_ROSTER="ACTIVITYDEFINITIONS_TYPE_ROSTER";

    private String activityDefinitionsType;
    private String activityDefinitionsQueryId;
    private boolean isActionMode;

    public ActivityDefinitionsActionTable(UserClientInfo currentUserClientInfo,String tableHeight,boolean isActionMode){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.isActionMode = isActionMode;
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
            this.containerDataSource.addContainerProperty(columnName_ActivityType, Button.class, null);
        }else{
            this.containerDataSource.addContainerProperty(columnName_ActivityType, String.class, null);
        }
        this.containerDataSource.addContainerProperty(columnName_RosterName, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_Status, String.class, null);
        if(this.isActionMode){
            this.containerDataSource.addContainerProperty(columnName_ActivityTypeOperations, ActivityDefinitionsTableRowActions.class, null);
            setRowHeaderMode(Table.RowHeaderMode.INDEX);
        }
        setContainerDataSource(this.containerDataSource);
        if(this.isActionMode){
            setColumnHeaders(new String[]{userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_TypePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_RosterNamePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_IsEnablePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Table_ListActionPropertyText")});
        }else{
            setColumnHeaders(new String[]{userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_TypePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_RosterNamePropertyText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_IsEnablePropertyText")});
        }

        setColumnAlignment(columnName_ActivityType, Table.Align.LEFT);
        setColumnAlignment(columnName_RosterName, Table.Align.LEFT);
        setColumnAlignment(columnName_Status, Table.Align.CENTER);
        setColumnWidth(columnName_Status, 200);
        if(this.isActionMode) {
            setColumnAlignment(columnName_ActivityTypeOperations, Table.Align.CENTER);
            setColumnWidth(columnName_ActivityTypeOperations, 160);
        }
    }

    @Override
    public void attach() {
        super.attach();
        if(getActivityDefinitionsType()==null){
            return;
        }
        loadDefinitionsData();
    }

    public void loadDefinitionsData(){
        this.clear();
        this.containerDataSource.removeAllItems();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        BusinessActivityDefinition[] activityTypes=null;
        if(getActivityDefinitionsType().equals(ACTIVITYDEFINITIONS_TYPE_ACTIVITYDEFINITION)){
            ActivitySpaceMetaInfoDAO spaceMetaInfoDAO=ActivitySpaceOperationUtil.getActivitySpaceMetaInfo(activitySpaceName,
                    new String[]{ActivitySpaceOperationUtil.ACTIVITYSPACE_METAINFOTYPE_ACTIVITYTYPE});
            activityTypes=spaceMetaInfoDAO.getBusinessActivityDefinitions();
        }
        if(getActivityDefinitionsType().equals(ACTIVITYDEFINITIONS_TYPE_ROSTER)){
            activityTypes=ActivitySpaceOperationUtil.getRosterContainsActivityDefinitions(activitySpaceName,getActivityDefinitionsQueryId());
        }
        if(activityTypes!=null){
            for(BusinessActivityDefinition currentActivityType:activityTypes){
                final String activityTypeName=currentActivityType.getActivityType();
                Item item = this.containerDataSource.addItem(activityTypeName);

                item.getItemProperty(columnName_RosterName).setValue(currentActivityType.getRosterName());
                item.getItemProperty(columnName_Status).setValue(""+currentActivityType.isEnabled());
                if(this.isActionMode){
                    Button switchToActivityTypeViewButton = new Button(activityTypeName);
                    switchToActivityTypeViewButton.setIcon(FontAwesome.HAND_O_RIGHT);
                    switchToActivityTypeViewButton.addStyleName("small");
                    //switchToActivityTypeViewButton.addStyleName("borderless");
                    //switchToActivityTypeViewButton.addStyleName("link");
                    switchToActivityTypeViewButton.addStyleName("quiet");
                    switchToActivityTypeViewButton.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            switchToActivityDefinitionDetailView(activityTypeName);
                        }
                    });
                    item.getItemProperty(columnName_ActivityType).setValue(switchToActivityTypeViewButton);
                    ActivityDefinitionsTableRowActions b = new ActivityDefinitionsTableRowActions(this.currentUserClientInfo,activityTypeName);
                    item.getItemProperty(columnName_ActivityTypeOperations).setValue(b);
                }else{
                    item.getItemProperty(columnName_ActivityType).setValue(activityTypeName);
                }
            }
       }
    }

    private void switchToActivityDefinitionDetailView(String activityType){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String componentType= ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION;
        ActivitySpaceComponentSelectedEvent activitySpaceComponentSelectedEvent=
                new ActivitySpaceComponentSelectedEvent(activitySpaceName,componentType,activityType);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentSelectedEvent);
    }

    public String getActivityDefinitionsType() {
        return activityDefinitionsType;
    }

    public void setActivityDefinitionsType(String activityDefinitionsType) {
        this.activityDefinitionsType = activityDefinitionsType;
    }

    public String getActivityDefinitionsQueryId() {
        return activityDefinitionsQueryId;
    }

    public void setActivityDefinitionsQueryId(String activityDefinitionsQueryId) {
        this.activityDefinitionsQueryId = activityDefinitionsQueryId;
    }
}
