package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class AddNewRoleQueuePanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private RoleQueueEditor roleQueueEditor;
    private SectionActionsBar addNewRoleQueueActionsBar;
    private RoleQueuesActionTable relatedRoleQueuesTable;
    public AddNewRoleQueuePanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        // Add New Role Queue Section
        MainSectionTitle addNewParticipantSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_AddNewButtonText"));
        addComponent(addNewParticipantSectionTitle);
        addNewRoleQueueActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_Common_ActivitySpaceText")+" <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(addNewRoleQueueActionsBar);
        roleQueueEditor=new RoleQueueEditor(this.currentUserClientInfo, RoleQueueEditor.EDITMODE_NEW);
        roleQueueEditor.setContainerAddNewRoleQueuePanel(this);
        addComponent(roleQueueEditor);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    public void attach() {
        super.attach();
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label activitySpaceNameLabel=new Label(userI18NProperties.
                    getProperty("ActivityManagement_Common_ActivitySpaceText")+" : <b>"+activitySpaceName+"</b>" , ContentMode.HTML);
            addNewRoleQueueActionsBar.resetSectionActionsBarContent(activitySpaceNameLabel);
        }
    }

    public void setRelatedRoleQueuesTable(RoleQueuesActionTable relatedRoleQueuesTable) {
        this.relatedRoleQueuesTable = relatedRoleQueuesTable;
    }

    public RoleQueuesActionTable getRelatedRoleQueuesTable() {
        return this.relatedRoleQueuesTable;
    }

    public void addNewRoleQueueFinishCallBack(String roleQueueName,String roleQueueDisplayName,String roleQueueDescription){
        if(this.relatedRoleQueuesTable!=null){
            this.relatedRoleQueuesTable.addRoleQueue(roleQueueName, roleQueueDisplayName, roleQueueDescription);
        }
        //close dialog window
        if(this.containerDialog!=null){
            this.containerDialog.close();
        }
    }
}
