package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ActivityDefinitionExposedStepsInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar activityTypeStepsSectionActionsBar;
    private String activityType;

    public ActivityDefinitionExposedStepsInfo(UserClientInfo currentUserClientInfo,String activityType){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.activityType=activityType;
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle activityStepsSectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepPropertiesText"));
        addComponent(activityStepsSectionTitle);
        activityTypeStepsSectionActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeText")+" : <b>"+this.activityType+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(activityTypeStepsSectionActionsBar);
        ActivityStepItemsActionList activityStepItemsActionList=new ActivityStepItemsActionList(this.currentUserClientInfo,"340px",false);
        addComponent(activityStepItemsActionList);

    }

    @Override
    public void attach() {
        super.attach();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        if(currentActivitySpaceComponentInfo!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label sectionActionBarLabel=new Label(userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeText")+" : <b>"+this.activityType+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            activityTypeStepsSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}
