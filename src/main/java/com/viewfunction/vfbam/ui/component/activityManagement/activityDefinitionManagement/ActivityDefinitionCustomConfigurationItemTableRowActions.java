package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

/**
 * Created by wangychu on 1/24/17.
 */
public class ActivityDefinitionCustomConfigurationItemTableRowActions extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private boolean canDeleteItem;
    private Button editConfigurationItemButton;
    private Button deleteConfigurationItemButton;
    private String configurationItemName;
    private String configurationItemType;
    private ActivityDefinitionCustomConfigurationItemTable containerActivityDefinitionCustomConfigurationItemTable;

    public ActivityDefinitionCustomConfigurationItemTableRowActions(UserClientInfo currentUserClientInfo, boolean canDeleteItem){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.canDeleteItem=canDeleteItem;

        editConfigurationItemButton = new Button();
        editConfigurationItemButton.setIcon(FontAwesome.EDIT);
        editConfigurationItemButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_EditConfigItemButtonLabel"));
        editConfigurationItemButton.addStyleName("small");
        editConfigurationItemButton.addStyleName("borderless");
        addComponent(editConfigurationItemButton);

        editConfigurationItemButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                doEditConfigurationItem();
            }
        });

        if(this.canDeleteItem){
            deleteConfigurationItemButton = new Button();
            deleteConfigurationItemButton.setIcon(FontAwesome.TRASH_O);
            deleteConfigurationItemButton.setDescription(userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_DeleteConfigItemButtonLabel"));
            deleteConfigurationItemButton.addStyleName("small");
            deleteConfigurationItemButton.addStyleName("borderless");
            addComponent(deleteConfigurationItemButton);

            deleteConfigurationItemButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    deleteConfigurationItem();
                }
            });
        }
    }

    private void doEditConfigurationItem(){
        final Window window = new Window();
        window.setResizable(false);
        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        int windowsHeight=screenHeight-300;
        window.setWidth(60, Unit.PERCENTAGE);
        window.setHeight(windowsHeight, Unit.PIXELS);
        window.setModal(true);
        window.center();
        ActivityDefinitionCustomConfigurationItemEditor configurationItemEditor=
                new ActivityDefinitionCustomConfigurationItemEditor(this.currentUserClientInfo,getConfigurationItemType(),getConfigurationItemName(),screenHeight-310);
        window.setContent(configurationItemEditor);
        UI.getCurrent().addWindow(window);
    }

    public String getConfigurationItemName() {
        return configurationItemName;
    }

    public void setConfigurationItemName(String configurationItemName) {
        this.configurationItemName = configurationItemName;
    }

    public String getConfigurationItemType() {
        return configurationItemType;
    }

    public void setConfigurationItemType(String configurationItemType) {
        this.configurationItemType = configurationItemType;
    }

    public void deleteConfigurationItem(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_Common_ConfirmDeleteConfigurationItemText")+
                " <b>"+getConfigurationItemName() +"</b>", ContentMode.HTML);
        final ConfirmDialog deleteConfigurationItemConfirmDialog = new ConfirmDialog();
        deleteConfigurationItemConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                deleteConfigurationItemConfirmDialog.close();
                getContainerActivityDefinitionCustomConfigurationItemTable().deleteConfigurationItem(getConfigurationItemName());
            }
        };
        deleteConfigurationItemConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(deleteConfigurationItemConfirmDialog);

    }

    public ActivityDefinitionCustomConfigurationItemTable getContainerActivityDefinitionCustomConfigurationItemTable() {
        return containerActivityDefinitionCustomConfigurationItemTable;
    }

    public void setContainerActivityDefinitionCustomConfigurationItemTable(ActivityDefinitionCustomConfigurationItemTable containerActivityDefinitionCustomConfigurationItemTable) {
        this.containerActivityDefinitionCustomConfigurationItemTable = containerActivityDefinitionCustomConfigurationItemTable;
    }
}
