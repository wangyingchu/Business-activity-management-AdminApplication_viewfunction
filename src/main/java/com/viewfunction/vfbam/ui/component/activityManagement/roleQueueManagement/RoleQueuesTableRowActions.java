package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RoleQueuesTableRowActions extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private String roleQueueName;
    private boolean allowRemoveOperation;
    private RoleQueuesActionTable containerRoleQueuesActionTable;
    public RoleQueuesTableRowActions(UserClientInfo currentUserClientInfo,String roleQueueName,boolean allowRemoveOperation){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.roleQueueName=roleQueueName;
        this.allowRemoveOperation=allowRemoveOperation;
        Button showContainsDataFieldsButton = new Button();
        showContainsDataFieldsButton.setIcon(FontAwesome.USERS);
        showContainsDataFieldsButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_RelatedRolesText"));
        showContainsDataFieldsButton.addStyleName("small");
        showContainsDataFieldsButton.addStyleName("borderless");
        addComponent(showContainsDataFieldsButton);
        final RoleQueueRelatedRolesInfo roleQueueRelatedRolesInfo=new RoleQueueRelatedRolesInfo(this.currentUserClientInfo,this.roleQueueName);
        showContainsDataFieldsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(800.0f, Sizeable.Unit.PIXELS);
                window.setHeight(490.0f, Sizeable.Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setContent(roleQueueRelatedRolesInfo);
                showContainsDataFieldsButton.setEnabled(false);
                window.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        showContainsDataFieldsButton.setEnabled(true);
                    }
                });
                UI.getCurrent().addWindow(window);
            }
        });

        Button showContainsActivityStepsButton = new Button();
        showContainsActivityStepsButton.setIcon(FontAwesome.TH_LIST);
        showContainsActivityStepsButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_DataFilterText"));
        showContainsActivityStepsButton.addStyleName("small");
        showContainsActivityStepsButton.addStyleName("borderless");
        addComponent(showContainsActivityStepsButton);
        final RoleQueueExposedDataFieldsInfo roleQueueExposedDataFieldsInfo=new RoleQueueExposedDataFieldsInfo(this.currentUserClientInfo,this.roleQueueName);
        showContainsActivityStepsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1000.0f, Sizeable.Unit.PIXELS);
                window.setHeight(490.0f, Sizeable.Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setContent(roleQueueExposedDataFieldsInfo);
                showContainsActivityStepsButton.setEnabled(false);
                window.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        showContainsActivityStepsButton.setEnabled(true);
                    }
                });
                UI.getCurrent().addWindow(window);
            }
        });

        Button roleQueueContainsActivityStepsInfoButton = new Button();
        roleQueueContainsActivityStepsInfoButton.setIcon(FontAwesome.SLIDERS);
        roleQueueContainsActivityStepsInfoButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_ContainsActivityStepsText"));
        roleQueueContainsActivityStepsInfoButton.addStyleName("small");
        roleQueueContainsActivityStepsInfoButton.addStyleName("borderless");
        addComponent(roleQueueContainsActivityStepsInfoButton);
        final RoleQueueContainsActivityStepsInfo roleQueueContainsActivityStepsInfo=new RoleQueueContainsActivityStepsInfo(this.currentUserClientInfo,this.roleQueueName);
        roleQueueContainsActivityStepsInfoButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1200.0f, Sizeable.Unit.PIXELS);
                window.setHeight(490.0f, Sizeable.Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setContent(roleQueueContainsActivityStepsInfo);
                roleQueueContainsActivityStepsInfoButton.setEnabled(false);
                window.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        roleQueueContainsActivityStepsInfoButton.setEnabled(true);
                    }
                });
                UI.getCurrent().addWindow(window);
            }
        });

        Label spaceDivLabel=new Label(" ");
        spaceDivLabel.setWidth("10px");
        addComponent(spaceDivLabel);

        Button removeCurrentRoleQueueButton = new Button();
        removeCurrentRoleQueueButton.setIcon(FontAwesome.TRASH_O);
        removeCurrentRoleQueueButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_RemoveRoleQueueText"));
        removeCurrentRoleQueueButton.addStyleName("small");
        removeCurrentRoleQueueButton.addStyleName("borderless");
        addComponent(removeCurrentRoleQueueButton);
        final String roleQueueNameToRemove=this.roleQueueName;
        removeCurrentRoleQueueButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {

                Label confirmMessage = new Label(FontAwesome.INFO.getHtml() +" "+userI18NProperties.
                        getProperty("ActivityManagement_RoleQueuesManagement_ConfirmDeleteRoleQueueText")+
                        " <b>" + roleQueueNameToRemove + "</b>.", ContentMode.HTML);
                final ConfirmDialog removeRosterConfirmDialog = new ConfirmDialog();
                removeRosterConfirmDialog.setConfirmMessage(confirmMessage);
                Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        //close confirm dialog
                        removeRosterConfirmDialog.close();
                        //remove roster in roster table
                        if (getContainerRoleQueuesActionTable() != null) {
                            getContainerRoleQueuesActionTable().removeRoleQueue(roleQueueNameToRemove);
                        }
                    }
                };
                removeRosterConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
                UI.getCurrent().addWindow(removeRosterConfirmDialog);
            }
        });
        if(!this.allowRemoveOperation){
            removeCurrentRoleQueueButton.setEnabled(false);
        }
    }

    public RoleQueuesActionTable getContainerRoleQueuesActionTable() {
        return containerRoleQueuesActionTable;
    }

    public void setContainerRoleQueuesActionTable(RoleQueuesActionTable containerRoleQueuesActionTable) {
        this.containerRoleQueuesActionTable = containerRoleQueuesActionTable;
    }
}
