package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;

import com.vaadin.ui.Label;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Panel;

import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.RoleVO;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.List;
import java.util.Properties;

public class ActivityStepItem extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private ActivityStepVO currentActivityStep;
    private List<ActivityDataFieldVO> activityDataFieldsList;
    private List<RoleVO> rolesList;
    private HorizontalLayout dataFieldInfoContainerLayout;
    private ActivityStepItemsActionList containerActivityStepItemsActionList;
    private ActivityStepConfigurationRowActions activityStepConfigurationRowActions;
    @Override
    public void attach() {
        super.attach();
    }

    public ActivityStepItem(UserClientInfo currentUserClientInfo,ActivityStepVO currentActivityStep,List<ActivityDataFieldVO> activityDataFieldsList,List<RoleVO> rolesList,boolean isEditable){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.currentActivityStep=currentActivityStep;
        this.activityDataFieldsList=activityDataFieldsList;
        this.rolesList=rolesList;
        setWidth("100%");
        addStyleName("ui_appElementDiv");
        String activityStepComStr=this.currentActivityStep.getActivityStepName()+" ("+this.currentActivityStep.getActivityStepDisplayName()+")";
        Label stepNameLabel=new Label(FontAwesome.ANGLE_RIGHT.getHtml() + " <b>"+activityStepComStr+"</b>", ContentMode.HTML);
        HorizontalLayout stepActionSectionLayout=new HorizontalLayout();
        stepActionSectionLayout.addStyleName("ui_appSectionFadeDiv");
        stepActionSectionLayout.setWidth("100%");
        stepActionSectionLayout.addComponent(stepNameLabel);
        stepActionSectionLayout.setExpandRatio(stepNameLabel, 1);
        if(isEditable) {
            activityStepConfigurationRowActions =
                    new ActivityStepConfigurationRowActions(this.currentUserClientInfo,this.currentActivityStep,this.activityDataFieldsList,this.rolesList);
            activityStepConfigurationRowActions.getActivityStepExposedDataFieldsEditor().setRelatedActivityStepItem(this);
            activityStepConfigurationRowActions.setRelatedActivityStepItem(this);
            stepActionSectionLayout.addComponent(activityStepConfigurationRowActions);
        }
        addComponent(stepActionSectionLayout);

        Panel dataFieldContainerPanel=new Panel();
        dataFieldContainerPanel.setCaption(userI18NProperties.
                getProperty("ActivityManagement_Common_DataFieldsLabel"));
        dataFieldContainerPanel.setIcon(FontAwesome.TH_LIST);
        dataFieldContainerPanel.addStyleName("borderless");
        //dataFieldContainerPanel.addStyleName("scroll-divider");
        dataFieldInfoContainerLayout=new HorizontalLayout();
        dataFieldContainerPanel.setContent(dataFieldInfoContainerLayout);
        addComponent(dataFieldContainerPanel);
        loadExposedActivityDataFields();
    }

    private ActivityDataFieldVO getActivityDataFieldVOByDataFieldName(String fieldName){
        if(this.activityDataFieldsList!=null){
         for(ActivityDataFieldVO activityDataFieldVO:this.activityDataFieldsList) {
             if(fieldName.equals(activityDataFieldVO.getDataFieldName())){
                 return activityDataFieldVO;
             }
         }
        }
        return null;
    }

    public void loadExposedActivityDataFields(){
        dataFieldInfoContainerLayout.removeAllComponents();;
        List<ActivityDataFieldVO> exposedActivityDataFieldList=this.currentActivityStep.getExposedActivityDataFields();
        if(exposedActivityDataFieldList!=null){
            for(ActivityDataFieldVO currentActivityDataFieldVO:exposedActivityDataFieldList){
                String currentDataFieldName=currentActivityDataFieldVO.getDataFieldName();
                Label dataField=new Label(FontAwesome.SQUARE_O.getHtml() + "  "+currentDataFieldName, ContentMode.HTML);
                dataFieldInfoContainerLayout.addComponent(dataField);
                dataField.addStyleName("ui_appElementTag");
                ActivityDataFieldVO targetActivityDataFieldVO=getActivityDataFieldVOByDataFieldName(currentDataFieldName);
                if(targetActivityDataFieldVO!=null){
                    dataField.setDescription(targetActivityDataFieldVO.getDescription());
                }
            }
        }
    }

    public void removeCurrentActivityStepFromExposedStepList(){
        this.containerActivityStepItemsActionList.deleteStepItem(this.currentActivityStep,this);
    }

    public void setContainerActivityStepItemsActionList(ActivityStepItemsActionList containerActivityStepItemsActionList) {
        this.containerActivityStepItemsActionList = containerActivityStepItemsActionList;
    }
}
