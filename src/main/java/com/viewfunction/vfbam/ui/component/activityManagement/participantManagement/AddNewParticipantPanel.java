package com.viewfunction.vfbam.ui.component.activityManagement.participantManagement;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class AddNewParticipantPanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private ParticipantEditor participantEditor;
    private SectionActionsBar addNewParticipantActionsBar;
    private ParticipantsActionTable relatedParticipantsTable;

    public AddNewParticipantPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        // Add New Participant Section
        MainSectionTitle addNewParticipantSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_AddNewButtonText"));
        addComponent(addNewParticipantSectionTitle);
        addNewParticipantActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_Common_ActivitySpaceText")+" <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(addNewParticipantActionsBar);
        participantEditor=new ParticipantEditor(this.currentUserClientInfo,ParticipantEditor.EDITMODE_NEW);
        participantEditor.setContainerAddNewParticipantPanel(this);
        addComponent(participantEditor);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setRelatedParticipantsTable(ParticipantsActionTable relatedParticipantsTable) {
        this.relatedParticipantsTable = relatedParticipantsTable;
    }

    public ParticipantsActionTable getRelatedParticipantsTable() {
        return this.relatedParticipantsTable;
    }

    public void addNewParticipantFinishCallBack(String participantName,String participantDisplayName,String participantType){
        if(this.relatedParticipantsTable!=null){
            this.relatedParticipantsTable.addParticipant(participantName,participantDisplayName,participantType);
        }
        //close dialog window
        if(this.containerDialog!=null){
            this.containerDialog.close();
        }
    }

    @Override
    public void attach() {
        super.attach();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label activitySpaceNameLabel=new Label(userI18NProperties.
                    getProperty("ActivityManagement_Common_ActivitySpaceText")+" <b>"+activitySpaceName+"</b>" , ContentMode.HTML);
            addNewParticipantActionsBar.resetSectionActionsBarContent(activitySpaceNameLabel);
        }
    }
}
