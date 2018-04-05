package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement.ActivityDefinitionsActionTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RosterContainsActivityDefinitionsInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar containedActivityDefinitionsSectionActionsBar;
    private String rosterName;
    public RosterContainsActivityDefinitionsInfo(UserClientInfo currentUserClientInfo,String rosterName){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.rosterName=rosterName;
        setSpacing(true);
        setMargin(true);
        // Participant's Roles Info Section
        SecondarySectionTitle belongsToRolesSectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_RosterActivityDefinitionText"));
        addComponent(belongsToRolesSectionTitle);

        containedActivityDefinitionsSectionActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_RosterManagement_RosterText")+" : <b>"+this.rosterName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(containedActivityDefinitionsSectionActionsBar);

        ActivityDefinitionsActionTable activityDefinitionsActionTable=new ActivityDefinitionsActionTable(this.currentUserClientInfo,"300px",false);
        activityDefinitionsActionTable.setActivityDefinitionsQueryId(this.rosterName);
        activityDefinitionsActionTable.setActivityDefinitionsType(ActivityDefinitionsActionTable.ACTIVITYDEFINITIONS_TYPE_ROSTER);
        addComponent(activityDefinitionsActionTable);
    }

    @Override
    public void attach() {
        super.attach();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        if(currentActivitySpaceComponentInfo!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label sectionActionBarLabel=new Label(userI18NProperties.
                    getProperty("ActivityManagement_RosterManagement_RosterText")+" : <b>"+this.rosterName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            containedActivityDefinitionsSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}
