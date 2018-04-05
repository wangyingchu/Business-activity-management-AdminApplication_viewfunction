package com.viewfunction.vfbam.ui.component.activityManagement.activitySpaceManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.NewActivitySpaceCreatedEvent;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class AddNewActivitySpacePanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private TextField activitySpaceName;

    public AddNewActivitySpacePanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        // Add New Activity Space Section
        MainSectionTitle addNewActivitySpaceSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivitySpaceManagement_CreateSpace_AddSpaceText"));
        addComponent(addNewActivitySpaceSectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        activitySpaceName = new TextField(userI18NProperties.
                getProperty("ActivitySpaceManagement_CreateSpace_SpaceNameText"));
        activitySpaceName.setRequired(true);
        form.addComponent(activitySpaceName);

        form.setReadOnly(true);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button(userI18NProperties.
                getProperty("ActivitySpaceManagement_CreateSpace_AddSpaceText"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new activity space logic */
                addNewActivitySpace();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
    }

    private void addNewActivitySpace(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String activitySpaceNameStr=activitySpaceName.getValue();
        if(activitySpaceNameStr==null||activitySpaceNameStr.trim().equals("")){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("ActivitySpaceManagement_CreateSpace_PleaseInputNameText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isExistActivitySpaceName= ActivitySpaceOperationUtil.checkActivitySpaceExistance(activitySpaceNameStr);
        if(isExistActivitySpaceName){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("ActivitySpaceManagement_CreateSpace_NameCheckErrorPart1Text")+" "+activitySpaceNameStr+" "+userI18NProperties.
                            getProperty("ActivitySpaceManagement_CreateSpace_NameCheckErrorPart2Text"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        //do add new logic
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                getProperty("ActivitySpaceManagement_CreateSpace_ConfirmAddSpaceText")+
                " <b>"+activitySpaceNameStr +"</b>.", ContentMode.HTML);
        final ConfirmDialog addActivitySpaceConfirmDialog = new ConfirmDialog();
        addActivitySpaceConfirmDialog.setConfirmMessage(confirmMessage);

        final AddNewActivitySpacePanel self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addActivitySpaceConfirmDialog.close();
                boolean createActivitySpaceResult=ActivitySpaceOperationUtil.createActivitySpace(activitySpaceNameStr);
                if(createActivitySpaceResult){
                    self.containerDialog.close();
                    NewActivitySpaceCreatedEvent newActivitySpaceCreatedEvent=new NewActivitySpaceCreatedEvent(activitySpaceNameStr);
                    self.currentUserClientInfo.getEventBlackBoard().fire(newActivitySpaceCreatedEvent);
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_AddDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivitySpaceManagement_CreateSpace_AddSpaceSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("ActivitySpaceManagement_CreateSpace_AddSpaceErrorText"),
                            userI18NProperties.
                                    getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };

        addActivitySpaceConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addActivitySpaceConfirmDialog);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }
}
