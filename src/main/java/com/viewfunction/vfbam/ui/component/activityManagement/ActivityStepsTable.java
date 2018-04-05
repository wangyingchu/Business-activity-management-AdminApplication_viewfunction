package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Table;
import com.viewfunction.activityEngine.activityView.common.ActivityStep;
import com.viewfunction.activityEngine.activityView.common.ParticipantTask;
import com.viewfunction.activityEngine.exception.ActivityEngineException;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

public class ActivityStepsTable extends Table {
    private UserClientInfo currentUserClientInfo;
    private String columnName_StepName ="columnName_StepName";
    private String columnName_ActivityType ="columnName_ActivityType";
    private String columnName_RoleName ="columnName_RoleName";
    private String columnName_CreateTime ="columnName_CreateTime";
    private String columnName_DueDate ="columnName_DueDate";

    private IndexedContainer containerDataSource;

    public static String ACTIVITYSTEPS_TYPE_PARTICIPANT ="ACTIVITYSTEPS_TYPE_PARTICIPANT";
    public static String ACTIVITYSTEPS_TYPE_ROLEQUEUE="ACTIVITYSTEPS_TYPE_ROLEQUEUE";
    private String activityStepType;
    private String activityStepQueryId;

    private SimpleDateFormat dataFormater;

    public ActivityStepsTable(UserClientInfo currentUserClientInfo, String tableHeight){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setWidth("100%");
        if(tableHeight!=null){
            setHeight(tableHeight);
        }else {
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
        //addStyleName("no-header");
        //addStyleName("compact");
        //addStyleName("small");

        this.dataFormater=new SimpleDateFormat("yyyy-MM-dd HH:mm");

        this.containerDataSource = new IndexedContainer();
        this.containerDataSource.addContainerProperty(columnName_StepName, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_ActivityType, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_RoleName, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_CreateTime, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_DueDate, String.class, null);
        this.setContainerDataSource(this.containerDataSource);

        //setRowHeaderMode(RowHeaderMode.INDEX);
        setColumnHeaders(new String[]{
                userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_StepText"),
                userI18NProperties.
                        getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeText"),
                userI18NProperties.
                        getProperty("ActivityManagement_RolesManagement_NamePropertyText"),
                userI18NProperties.
                        getProperty("ActivityManagement_Common_Activity_CreateTimeText"),
                userI18NProperties.
                        getProperty("ActivityManagement_Common_Activity_DueDateText")

        });
        setColumnAlignment(columnName_StepName, Align.LEFT);
        setColumnAlignment(columnName_ActivityType, Align.LEFT);
        setColumnAlignment(columnName_RoleName, Align.CENTER);
        setColumnWidth(columnName_RoleName, 200);
        setColumnAlignment(columnName_CreateTime, Align.CENTER);
        setColumnAlignment(columnName_DueDate, Align.CENTER);
    }

    @Override
    public void attach() {
        super.attach();
        if(activityStepType==null){
            return;
        }
        loadActivityStepsData();
    }

    public void loadActivityStepsData(){
        this.clear();
        this.containerDataSource.removeAllItems();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        if(activityStepType.equals(ACTIVITYSTEPS_TYPE_PARTICIPANT)){
            List<ParticipantTask> tasksList= ActivitySpaceOperationUtil.getParticipantWorkTasks(activitySpaceName, this.activityStepQueryId);
            if(tasksList!=null){
                for(ParticipantTask currentParticipantTask:tasksList){
                    String id = ""+currentParticipantTask.getActivityType()+
                                    currentParticipantTask.getActivityStep().getActivityId()+
                                    currentParticipantTask.getActivityStepName();
                    Item item = this.containerDataSource.addItem(id);
                    item.getItemProperty(columnName_StepName).setValue(currentParticipantTask.getActivityStepName());
                    item.getItemProperty(columnName_ActivityType).setValue(currentParticipantTask.getActivityType());
                    try {
                        item.getItemProperty(columnName_RoleName).setValue(currentParticipantTask.getRoleName());
                    }catch (ActivityEngineException e) {
                        e.printStackTrace();
                    }
                    if(currentParticipantTask.getCreateTime()!=null){
                        item.getItemProperty(columnName_CreateTime).setValue(this.dataFormater.format(currentParticipantTask.getCreateTime()));
                    }else{
                        item.getItemProperty(columnName_CreateTime).setValue("-");
                    }
                    if(currentParticipantTask.getDueDate()!=null){
                        item.getItemProperty(columnName_DueDate).setValue(this.dataFormater.format(currentParticipantTask.getDueDate()));
                    }else{
                        item.getItemProperty(columnName_DueDate).setValue("-");
                    }
                }
            }
        }
        if(activityStepType.equals(ACTIVITYSTEPS_TYPE_ROLEQUEUE)){
            List<ActivityStep> activityStepList=ActivitySpaceOperationUtil.getRoleQueueActivitySteps(activitySpaceName, this.activityStepQueryId);
            if(activityStepList!=null){
                for(ActivityStep currentActivityStep:activityStepList){

                    String id = ""+currentActivityStep.getActivityType()+
                            currentActivityStep.getActivityId()+
                            currentActivityStep.getActivityStepName();
                    Item item = this.containerDataSource.addItem(id);
                    item.getItemProperty(columnName_StepName).setValue(currentActivityStep.getActivityStepName());
                    item.getItemProperty(columnName_ActivityType).setValue(currentActivityStep.getActivityType());
                    try {
                        item.getItemProperty(columnName_RoleName).setValue(currentActivityStep.getRelatedRole().getRoleName());
                    }catch (ActivityEngineException e) {
                        e.printStackTrace();
                    }
                    if(currentActivityStep.getCreateTime()!=null){
                        item.getItemProperty(columnName_CreateTime).setValue(this.dataFormater.format(currentActivityStep.getCreateTime()));
                    }else{
                        item.getItemProperty(columnName_CreateTime).setValue("-");
                    }
                    if(currentActivityStep.getDueDate()!=null){
                        item.getItemProperty(columnName_DueDate).setValue(this.dataFormater.format(currentActivityStep.getDueDate()));
                    }else{
                        item.getItemProperty(columnName_DueDate).setValue("-");
                    }
                }
            }
        }
    }

    public void setActivityStepType(String activityStepType) {
        this.activityStepType = activityStepType;
    }

    public void setActivityStepQueryId(String activityStepQueryId) {
        this.activityStepQueryId = activityStepQueryId;
    }
}

