package com.viewfunction.vfbam.ui.component.activityManagement.roleManagement;

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

public class RolesListManagementView extends VerticalLayout implements View {
    private UserClientInfo currentUserClientInfo;
    public RolesListManagementView(UserClientInfo currentUserClientInfo){
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
                getProperty("ActivityManagement_RolesManagement_InfoText"));
        viewContentContainer.addComponent(mainSectionTitle);
        // Roles List Section
        SectionActionsBar rolesListSectionActionsBar=new SectionActionsBar(new Label(FontAwesome.USERS.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_RolesManagement_ListText"), ContentMode.HTML));
        viewContentContainer.addComponent(rolesListSectionActionsBar);
        SectionActionButton addNewRoleActionButton = new SectionActionButton();
        addNewRoleActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_RolesManagement_AddNewButtonText"));
        addNewRoleActionButton.setIcon(FontAwesome.PLUS_SQUARE);
        rolesListSectionActionsBar.addActionComponent(addNewRoleActionButton);

        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        String tableHeightString=""+(browserWindowHeight-300)+"px";
        RolesActionTable rolesActionTable=new RolesActionTable(this.currentUserClientInfo,tableHeightString,true);
        rolesActionTable.setRolesQueryId(null);
        rolesActionTable.setRolesType(RolesActionTable.ROLES_TYPE_ROLE);
        viewContentContainer.addComponent(rolesActionTable);

        final AddNewRolePanel addNewRolePanel=new AddNewRolePanel(this.currentUserClientInfo);
        addNewRolePanel.setRelatedRolesTable(rolesActionTable);
        addNewRoleActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Unit.PIXELS);
                window.setHeight(390.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(addNewRolePanel);
                addNewRolePanel.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
