package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Table;
import com.viewfunction.activityEngine.activityBureau.BusinessActivity;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.activityEngine.exception.ActivityEngineException;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.List;
import java.util.Properties;

public class ActivityInstancesActionTable extends Table {
    private UserClientInfo currentUserClientInfo;
    private String columnName_ActivityId ="columnName_ActivityId";
    private String columnName_ActivityType ="columnName_ActivityType";
    private boolean isActionMode;
    private IndexedContainer containerDataSource;
    private String activityInstancesQueryId;
    private String activityInstancesQueryType;
    private Roster relatedRoster;

    public static final String ACTIVITYINSTANCES_TYPE_ROSTER="ACTIVITYINSTANCES_TYPE_ROSTER";

    public ActivityInstancesActionTable(UserClientInfo currentUserClientInfo, String tableHeight,boolean isActionMode){
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
        this.containerDataSource.addContainerProperty(columnName_ActivityId, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_ActivityType, String.class, null);
        setContainerDataSource(this.containerDataSource);
        if(this.isActionMode){
            setRowHeaderMode(RowHeaderMode.INDEX);
        }
        setColumnHeaders(new String[]{ userI18NProperties.
                getProperty("ActivityManagement_Common_ActivityIdText"),
                userI18NProperties.
                        getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeText") });
        setColumnAlignment(columnName_ActivityId, Align.LEFT);
        setColumnAlignment(columnName_ActivityType, Align.LEFT);
    }

    @Override
    public void attach() {
        super.attach();
        if(getActivityInstancesQueryType()==null){
            return;
        }
        loadActivityInstancesData();
    }

    public void loadActivityInstancesData(){
        this.clear();
        this.containerDataSource.removeAllItems();
        if(this.relatedRoster!=null){
            List<BusinessActivity> activityInstanceList=ActivitySpaceOperationUtil.getActivityInstancesByRoster(this.relatedRoster);
            if(activityInstanceList!=null){
               for(BusinessActivity currentBusinessActivity:activityInstanceList){
                   String id = currentBusinessActivity.getActivityId();
                   Item item = this.containerDataSource.addItem(id);
                   item.getItemProperty(columnName_ActivityId).setValue(id);
                   try {
                       item.getItemProperty(columnName_ActivityType).setValue(currentBusinessActivity.getActivityDefinition().getActivityType());
                   }catch(ActivityEngineException e) {
                       e.printStackTrace();
                   }
               }
            }
        }
    }

    public void setActivityInstancesQueryId(String activityInstancesQueryId) {
        this.activityInstancesQueryId = activityInstancesQueryId;
    }

    public String getActivityInstancesQueryType() {
        return activityInstancesQueryType;
    }

    public void setActivityInstancesQueryType(String activityInstancesQueryType) {
        this.activityInstancesQueryType = activityInstancesQueryType;
    }

    public void setRelatedRoster(Roster relatedRoster) {
        this.relatedRoster = relatedRoster;
    }
}
