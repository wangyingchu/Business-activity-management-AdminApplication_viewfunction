package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.RoleVO;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.List;
import java.util.Properties;

public class ActivityStepItemsActionList extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private boolean isActionMode;
    private List<ActivityStepVO> currentExposedActivityStepList;
    private List<ActivityDataFieldVO> activityDataFieldsList;
    private VerticalLayout listContainerLayout;
    private List<RoleVO> rolesList;
    private ActivityStepsEditor containerActivityStepsEditor;
    public ActivityStepItemsActionList(UserClientInfo currentUserClientInfo,String listHeight,boolean isActionMode){
        this.currentUserClientInfo=currentUserClientInfo;
        this.isActionMode=isActionMode;

        Panel containerPanel=new Panel();
        containerPanel.addStyleName("borderless");
        if(listHeight!=null){
            containerPanel.setHeight(listHeight);
        }
        addComponent(containerPanel);
        listContainerLayout=new VerticalLayout();
        containerPanel.setContent(listContainerLayout);
    }

    @Override
    public void attach() {
        super.attach();
        renderActivityStepItems();
    }

    public void renderActivityStepItems(){
        listContainerLayout.removeAllComponents();
        if(currentExposedActivityStepList!=null){
            for(ActivityStepVO activityStepVO:currentExposedActivityStepList){
                ActivityStepItem activityStepItem=new ActivityStepItem(this.currentUserClientInfo,activityStepVO,activityDataFieldsList,rolesList,isActionMode);
                activityStepItem.setContainerActivityStepItemsActionList(this);
                listContainerLayout.addComponent(activityStepItem);
            }
        }
    }

    public void setCurrentExposedActivityStepList(List<ActivityStepVO> currentExposedActivityStepList) {
        this.currentExposedActivityStepList = currentExposedActivityStepList;
    }

    public void addActivityStep(ActivityStepVO newActivityStep){
        ActivityStepItem activityStepItem=new ActivityStepItem(this.currentUserClientInfo,newActivityStep,activityDataFieldsList,rolesList,isActionMode);
        this.currentExposedActivityStepList.add(newActivityStep);
        activityStepItem.setContainerActivityStepItemsActionList(this);
        listContainerLayout.addComponent(activityStepItem);
    }

    public void setActivityDataFieldsList(List<ActivityDataFieldVO> activityDataFieldsList) {
        this.activityDataFieldsList = activityDataFieldsList;
    }

    public void deleteStepItem(ActivityStepVO targetActivityStepVO,ActivityStepItem targetActivityStepItem){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String stepName=targetActivityStepVO.getActivityStepName();
        boolean removeExposedStepResult=this.containerActivityStepsEditor.removeExposedActivityStep(stepName);
        if(removeExposedStepResult) {
            Notification resultNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DeleteDataSuccessText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_RemoveStepSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
            this.currentExposedActivityStepList.remove(targetActivityStepVO);
            listContainerLayout.removeComponent(targetActivityStepItem);
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_RemoveStepErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    public void setRolesList(List<RoleVO> rolesList) {
        this.rolesList = rolesList;
    }

    public void setContainerActivityStepsEditor(ActivityStepsEditor containerActivityStepsEditor) {
        this.containerActivityStepsEditor = containerActivityStepsEditor;
    }
}
