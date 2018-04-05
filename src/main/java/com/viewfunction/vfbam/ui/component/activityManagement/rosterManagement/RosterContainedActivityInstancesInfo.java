package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityInstancesActionTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RosterContainedActivityInstancesInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar containedActivityInstancesSectionActionsBar;
    private String rosterName;
    public RosterContainedActivityInstancesInfo(UserClientInfo currentUserClientInfo,String rosterName){
        this.currentUserClientInfo=currentUserClientInfo;
        this.rosterName=rosterName;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle containedInstancesSectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_RosterActivityInstanceText"));
        addComponent(containedInstancesSectionTitle);

        containedActivityInstancesSectionActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_RosterManagement_RosterText")+" : <b>"+this.rosterName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(containedActivityInstancesSectionActionsBar);

        ActivityInstancesActionTable activityInstancesActionTable=new ActivityInstancesActionTable(this.currentUserClientInfo,"300px",false);
        activityInstancesActionTable.setActivityInstancesQueryType(ActivityInstancesActionTable.ACTIVITYINSTANCES_TYPE_ROSTER);
        activityInstancesActionTable.setActivityInstancesQueryId(this.rosterName);
        addComponent(activityInstancesActionTable);
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
                    getProperty("ActivityManagement_RosterManagement_RosterText")+" : <b>"+this.rosterName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            containedActivityInstancesSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}
