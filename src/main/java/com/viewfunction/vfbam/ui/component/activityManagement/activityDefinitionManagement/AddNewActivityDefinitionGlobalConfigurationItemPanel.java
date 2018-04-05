package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.activityManagement.NewActivitySpaceCreatedEvent;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

/**
 * Created by wangychu on 2/8/17.
 */
public class AddNewActivityDefinitionGlobalConfigurationItemPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private TextField configurationItemName;
    private ActivityDefinitionCustomConfigurationItemTable relatedActivityDefinitionCustomConfigurationItemTable;
    private ActivityAdditionalConfigurationEditor containerActivityAdditionalConfigurationEditor;

    public AddNewActivityDefinitionGlobalConfigurationItemPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        MainSectionTitle addNewConfigurationItemSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AddConfigItemButtonLabel"));
        addComponent(addNewConfigurationItemSectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        configurationItemName = new TextField(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ConfigItemNameText"));
        configurationItemName.setRequired(true);
        form.addComponent(configurationItemName);

        form.setReadOnly(true);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_AddNewConfigurationItemText"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new activity space logic */
                addNewConfigurationItem();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
    }

    private void addNewConfigurationItem(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String newConfigurationItemNameStr= configurationItemName.getValue();
        if(newConfigurationItemNameStr==null||newConfigurationItemNameStr.trim().equals("")){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Common_PleaseInputConfigItemNameText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        boolean isExistConfigurationItem=false;
        if(getRelatedActivityDefinitionCustomConfigurationItemTable()!=null){
            isExistConfigurationItem=getRelatedActivityDefinitionCustomConfigurationItemTable().checkConfigurationItemExistence(newConfigurationItemNameStr);
        }
        if(isExistConfigurationItem){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Common_ConfigItemExistText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        //do add new logic
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_Common_ConfirmAddNewConfigItemText")+" "+
                " <b>"+newConfigurationItemNameStr +"</b>.", ContentMode.HTML);
        final ConfirmDialog addConfigItemConfirmDialog = new ConfirmDialog();
        addConfigItemConfirmDialog.setConfirmMessage(confirmMessage);

        final AddNewActivityDefinitionGlobalConfigurationItemPanel self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addConfigItemConfirmDialog.close();
                boolean addGlobalConfigItemResult=getContainerActivityAdditionalConfigurationEditor().
                        addNewActivityTypeGlobalConfigurationItem(newConfigurationItemNameStr);
                if(addGlobalConfigItemResult){
                    self.containerDialog.close();
                    NewActivitySpaceCreatedEvent newActivitySpaceCreatedEvent=new NewActivitySpaceCreatedEvent(newConfigurationItemNameStr);
                    self.currentUserClientInfo.getEventBlackBoard().fire(newActivitySpaceCreatedEvent);
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_AddDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_Common_AddNewActivityTypeConfigItemSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("ActivityManagement_Common_AddNewActivityTypeConfigItemErrorText"),
                            userI18NProperties.
                                    getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        addConfigItemConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addConfigItemConfirmDialog);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public ActivityDefinitionCustomConfigurationItemTable getRelatedActivityDefinitionCustomConfigurationItemTable() {
        return relatedActivityDefinitionCustomConfigurationItemTable;
    }

    public void setRelatedActivityDefinitionCustomConfigurationItemTable(ActivityDefinitionCustomConfigurationItemTable relatedActivityDefinitionCustomConfigurationItemTable) {
        this.relatedActivityDefinitionCustomConfigurationItemTable = relatedActivityDefinitionCustomConfigurationItemTable;
    }

    public ActivityAdditionalConfigurationEditor getContainerActivityAdditionalConfigurationEditor() {
        return containerActivityAdditionalConfigurationEditor;
    }

    public void setContainerActivityAdditionalConfigurationEditor(ActivityAdditionalConfigurationEditor containerActivityAdditionalConfigurationEditor) {
        this.containerActivityAdditionalConfigurationEditor = containerActivityAdditionalConfigurationEditor;
    }
}
