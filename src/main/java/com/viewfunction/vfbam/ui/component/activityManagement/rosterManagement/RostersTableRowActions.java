package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RostersTableRowActions extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private String rosterName;
    private RostersActionTable containerRostersActionTable;
    public RostersTableRowActions(UserClientInfo currentUserClientInfo,String rosterName){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.rosterName=rosterName;
        Button showContainsDataFieldsButton = new Button();
        showContainsDataFieldsButton.setIcon(FontAwesome.SHARE_ALT_SQUARE);
        showContainsDataFieldsButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_RosterActivityTypeText"));
        showContainsDataFieldsButton.addStyleName("small");
        showContainsDataFieldsButton.addStyleName("borderless");
        addComponent(showContainsDataFieldsButton);
        final RosterContainsActivityDefinitionsInfo rosterContainsActivityDefinitionsInfo=new RosterContainsActivityDefinitionsInfo(this.currentUserClientInfo,this.rosterName);
        showContainsDataFieldsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1000.0f, Sizeable.Unit.PIXELS);
                window.setHeight(490.0f, Sizeable.Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setContent(rosterContainsActivityDefinitionsInfo);
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
                getProperty("ActivityManagement_RosterManagement_DataFilterText"));
        showContainsActivityStepsButton.addStyleName("small");
        showContainsActivityStepsButton.addStyleName("borderless");
        addComponent(showContainsActivityStepsButton);
        final RosterExposedDataFieldsInfo rosterExposedDataFieldsInfo=new RosterExposedDataFieldsInfo(this.currentUserClientInfo,this.rosterName);
        showContainsActivityStepsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1000.0f, Unit.PIXELS);
                window.setHeight(490.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setContent(rosterExposedDataFieldsInfo);
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

        Button startActivityButton = new Button();
        startActivityButton.setIcon(FontAwesome.INDENT);
        startActivityButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_FetchActivityInstanceButtonLabel"));
        startActivityButton.addStyleName("small");
        startActivityButton.addStyleName("borderless");
        addComponent(startActivityButton);
        final RosterContainedActivityInstancesInfo rosterContainedActivityInstancesInfo=new RosterContainedActivityInstancesInfo(this.currentUserClientInfo,this.rosterName);
        startActivityButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1000.0f, Unit.PIXELS);
                window.setHeight(490.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setContent(rosterContainedActivityInstancesInfo);
                startActivityButton.setEnabled(false);
                window.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        startActivityButton.setEnabled(true);
                    }
                });
                UI.getCurrent().addWindow(window);
            }
        });

        Label spaceDivLabel=new Label(" ");
        spaceDivLabel.setWidth("10px");
        addComponent(spaceDivLabel);

        Button removeCurrentRosterButton = new Button();
        removeCurrentRosterButton.setIcon(FontAwesome.TRASH_O);
        removeCurrentRosterButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_RemoveRosterText"));
        removeCurrentRosterButton.addStyleName("small");
        removeCurrentRosterButton.addStyleName("borderless");
        addComponent(removeCurrentRosterButton);
        final String rosterNameToRemove=this.rosterName;
        removeCurrentRosterButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {

                Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                        getProperty("ActivityManagement_RosterManagement_ConfirmRemoveRosterText")+
                        " <b>"+rosterNameToRemove +"</b>.", ContentMode.HTML);
                final ConfirmDialog removeRosterConfirmDialog = new ConfirmDialog();
                removeRosterConfirmDialog.setConfirmMessage(confirmMessage);
                Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        //close confirm dialog
                        removeRosterConfirmDialog.close();
                        //remove roster in roster table
                        if(getContainerRostersActionTable()!=null){
                            getContainerRostersActionTable().removeRoster(rosterNameToRemove);
                        }
                    }
                };
                removeRosterConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
                UI.getCurrent().addWindow(removeRosterConfirmDialog);
            }
        });
    }

    public RostersActionTable getContainerRostersActionTable() {
        return containerRostersActionTable;
    }

    public void setContainerRostersActionTable(RostersActionTable containerRostersActionTable) {
        this.containerRostersActionTable = containerRostersActionTable;
    }
}
