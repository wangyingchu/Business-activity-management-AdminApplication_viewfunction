package com.viewfunction.vfbam.ui.component.activityManagement.participantManagement;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ParticipantsListManagementView extends VerticalLayout implements View {
    private UserClientInfo currentUserClientInfo;
    public ParticipantsListManagementView(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setMargin(true);
        this.setSpacing(true);
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        VerticalLayout viewContentContainer = new VerticalLayout();
        viewContentContainer.setMargin(false);
        viewContentContainer.setSpacing(false);
        viewContentContainer.addStyleName("ui_appSubViewContainer");
        this.addComponent(viewContentContainer);
        // View Title
        MainSectionTitle mainSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_InfoText"));
        viewContentContainer.addComponent(mainSectionTitle);
        // Participants List Section
        SectionActionsBar participantsListSectionActionsBar=new SectionActionsBar(new Label(FontAwesome.USER.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_ListText"), ContentMode.HTML));
        viewContentContainer.addComponent(participantsListSectionActionsBar);
        SectionActionButton addNewParticipantActionButton = new SectionActionButton();
        addNewParticipantActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_AddNewButtonText"));
        addNewParticipantActionButton.setIcon(FontAwesome.PLUS_SQUARE);
        participantsListSectionActionsBar.addActionComponent(addNewParticipantActionButton);

        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        String tableHeightString=""+(browserWindowHeight-300)+"px";
        ParticipantsActionTable participantsActionTable=new ParticipantsActionTable(this.currentUserClientInfo,tableHeightString,true,true);
        participantsActionTable.setParticipantsQueryId(null);
        participantsActionTable.setParticipantsType(ParticipantsActionTable.PARTICIPANTS_TYPE_PARTICIPANT);
        viewContentContainer.addComponent(participantsActionTable);

        final AddNewParticipantPanel addNewParticipantPanel=new AddNewParticipantPanel(this.currentUserClientInfo);
        addNewParticipantPanel.setRelatedParticipantsTable(participantsActionTable);
        addNewParticipantActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Unit.PIXELS);
                window.setHeight(350.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(addNewParticipantPanel);
                addNewParticipantPanel.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
