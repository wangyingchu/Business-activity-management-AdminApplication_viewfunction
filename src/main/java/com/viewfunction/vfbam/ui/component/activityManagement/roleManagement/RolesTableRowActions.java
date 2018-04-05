package com.viewfunction.vfbam.ui.component.activityManagement.roleManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RolesTableRowActions extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private String roleName;
    public RolesTableRowActions(UserClientInfo currentUserClientInfo,String roleName){
        this.roleName=roleName;
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        Button showContainerdParticipantsButton = new Button();
        showContainerdParticipantsButton.setIcon(FontAwesome.USER);
        showContainerdParticipantsButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_RolesManagement_ContainsParticipantsText"));
        showContainerdParticipantsButton.addStyleName("small");
        showContainerdParticipantsButton.addStyleName("borderless");
        addComponent(showContainerdParticipantsButton);
        final RoleContainsParticipantsInfo roleContainsParticipantsInfo=new RoleContainsParticipantsInfo(this.currentUserClientInfo,this.roleName);
        showContainerdParticipantsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Sizeable.Unit.PIXELS);
                window.setHeight(490.0f, Sizeable.Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setContent(roleContainsParticipantsInfo);
                showContainerdParticipantsButton.setEnabled(false);
                window.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        showContainerdParticipantsButton.setEnabled(true);
                    }
                });
                UI.getCurrent().addWindow(window);
            }
        });

        Button showRelatedRoleQueuesButton = new Button();
        showRelatedRoleQueuesButton.setIcon(FontAwesome.ALIGN_JUSTIFY);
        showRelatedRoleQueuesButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_RolesManagement_RelatedRoleQueuesText"));
        showRelatedRoleQueuesButton.addStyleName("small");
        showRelatedRoleQueuesButton.addStyleName("borderless");
        addComponent(showRelatedRoleQueuesButton);
        final RoleRelatedRoleQueuesInfo roleRelatedRoleQueuesInfo=new RoleRelatedRoleQueuesInfo(this.currentUserClientInfo,this.roleName);
        showRelatedRoleQueuesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1000.0f, Unit.PIXELS);
                window.setHeight(490.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setContent(roleRelatedRoleQueuesInfo);
                showRelatedRoleQueuesButton.setEnabled(false);
                window.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        showRelatedRoleQueuesButton.setEnabled(true);
                    }
                });
                UI.getCurrent().addWindow(window);
            }
        });
    }
}
