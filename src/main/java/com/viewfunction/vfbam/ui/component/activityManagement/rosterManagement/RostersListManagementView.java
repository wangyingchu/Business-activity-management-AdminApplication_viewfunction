package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

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

public class RostersListManagementView extends VerticalLayout implements View {
    private UserClientInfo currentUserClientInfo;
    public RostersListManagementView(UserClientInfo currentUserClientInfo){
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
                getProperty("ActivityManagement_RosterManagement_InfoText"));
        viewContentContainer.addComponent(mainSectionTitle);
        // Rosters List Section
        SectionActionsBar rostersListSectionActionsBar=new SectionActionsBar(new Label(FontAwesome.INBOX.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_ListText"), ContentMode.HTML));
        viewContentContainer.addComponent(rostersListSectionActionsBar);
        SectionActionButton addNewRosterActionButton = new SectionActionButton();
        addNewRosterActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_AddNewButtonText"));
        addNewRosterActionButton.setIcon(FontAwesome.PLUS_SQUARE);
        rostersListSectionActionsBar.addActionComponent(addNewRosterActionButton);

        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        String tableHeightString=""+(browserWindowHeight-300)+"px";
        RostersActionTable rostersActionTable =new RostersActionTable(this.currentUserClientInfo,tableHeightString);
        viewContentContainer.addComponent(rostersActionTable);

        final AddNewRosterPanel addNewRosterPanel=new AddNewRosterPanel(this.currentUserClientInfo);
        addNewRosterPanel.setRelatedRostersTable(rostersActionTable);
        addNewRosterActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Unit.PIXELS);
                window.setHeight(390.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(addNewRosterPanel);
                addNewRosterPanel.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
