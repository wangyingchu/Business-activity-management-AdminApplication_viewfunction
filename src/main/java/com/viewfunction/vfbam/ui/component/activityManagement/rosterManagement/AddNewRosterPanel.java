package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;


public class AddNewRosterPanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private RosterEditor rosterEditor;
    private SectionActionsBar addNewRoleActionsBar;
    private RostersActionTable relatedRostersActionTable;
    public AddNewRosterPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        // Add New Role Section
        MainSectionTitle addNewParticipantSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_AddNewButtonText"));
        addComponent(addNewParticipantSectionTitle);
        addNewRoleActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_Common_ActivitySpaceText")+" <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(addNewRoleActionsBar);
        rosterEditor=new RosterEditor(this.currentUserClientInfo, RosterEditor.EDITMODE_NEW);
        rosterEditor.setContainerAddNewRosterPanel(this);
        addComponent(rosterEditor);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void addNewRosterFinishCallBack(String rosterName,String rosterDisplayName,String rosterDescription){
        if(this.relatedRostersActionTable !=null){
            this.relatedRostersActionTable.addRoster(rosterName, rosterDisplayName, rosterDescription);
        }
        //close dialog window
        if(this.containerDialog!=null){
            this.containerDialog.close();
        }
    }

    @Override
    public void attach() {
        super.attach();
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label activitySpaceNameLabel=new Label(userI18NProperties.
                    getProperty("ActivityManagement_Common_ActivitySpaceText")+" <b>"+activitySpaceName+"</b>" , ContentMode.HTML);
            addNewRoleActionsBar.resetSectionActionsBarContent(activitySpaceNameLabel);
        }
    }

    public void setRelatedRostersTable(RostersActionTable relatedRostersActionTable) {
        this.relatedRostersActionTable = relatedRostersActionTable;
    }

    public RostersActionTable getRostersActionTable(){
        return this.relatedRostersActionTable;
    }
}
