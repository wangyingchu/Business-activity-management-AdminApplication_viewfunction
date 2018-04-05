package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RoleQueueEditor  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public static final String EDITMODE_UPDATE="EDITMODE_UPDATE";
    public static final String EDITMODE_NEW="EDITMODE_NEW";
    private String currentEditMode;

    private final FormLayout form;
    private HorizontalLayout footer;
    private TextField roleQueueName;
    private TextField roleQueueDisplayName;
    private TextArea roleQueueDescription;

    private Button updateButton;
    private Button cancelButton;
    private Button addButton;

    private String currentRoleQueueDisplayName;
    private String currentRoleQueueDescription;

    private AddNewRoleQueuePanel containerAddNewRoleQueuePanel;
    private RoleQueue roleQueue;

    public RoleQueueEditor(UserClientInfo currentUserClientInfo,String editorType){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.currentUserClientInfo=currentUserClientInfo;
        this.currentEditMode = editorType;
        form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        roleQueueName = new TextField(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_NamePropertyText"));
        roleQueueName.setRequired(true);
        form.addComponent(roleQueueName);

        roleQueueDisplayName = new TextField(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_DisplayNamePropertyText"));
        roleQueueDisplayName.setRequired(true);
        roleQueueDisplayName.setWidth("50%");
        form.addComponent(roleQueueDisplayName);

        roleQueueDescription = new TextArea(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_DescriptionPropertyText"));
        roleQueueDescription.setWidth("50%");
        roleQueueDescription.setRows(3);
        form.addComponent(roleQueueDescription);

        form.setReadOnly(true);

        footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        updateButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_UpdateButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean readOnly = form.isReadOnly();
                if (readOnly) {
                    form.setReadOnly(false);
                    roleQueueDisplayName.setReadOnly(false);
                    roleQueueDescription.setReadOnly(false);
                    form.removeStyleName("light");
                    event.getButton().setCaption(userI18NProperties.
                            getProperty("ActivityManagement_Common_SaveButtonLabel"));
                    event.getButton().setIcon(FontAwesome.SAVE);
                    event.getButton().addStyleName("primary");
                    footer.addComponent(cancelButton);
                } else {
                    /* Do update role Queue logic */
                    updateCurrentRoleQueue();
                }
            }
        });
        updateButton.setIcon(FontAwesome.HAND_O_RIGHT);

        cancelButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_CancelButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                roleQueueDisplayName.setValue(currentRoleQueueDisplayName);
                roleQueueDescription.setValue(currentRoleQueueDescription);
                form.setReadOnly(true);
                roleQueueDisplayName.setReadOnly(true);
                roleQueueDescription.setReadOnly(true);
                form.addStyleName("light");
                updateButton.setCaption(userI18NProperties.
                        getProperty("ActivityManagement_Common_UpdateButtonLabel"));
                updateButton.removeStyleName("primary");
                updateButton.setIcon(FontAwesome.HAND_O_RIGHT);
                footer.removeComponent(cancelButton);
            }
        });
        cancelButton.setIcon(FontAwesome.TIMES);

        addButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_AddRoleQueueButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new role queue logic */
                addNewRoleQueue();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
    }

    @Override
    public void attach() {
        super.attach();
        //reset form element value
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        footer.removeAllComponents();
        roleQueueName.setReadOnly(false);
        roleQueueDisplayName.setReadOnly(false);
        roleQueueDescription.setReadOnly(false);
        if(!this.currentEditMode.equals(EDITMODE_NEW)){
            //For edit existing role queue
            footer.addComponent(updateButton);
            if (currentActivitySpaceComponentInfo != null){
                roleQueueName.setValue(currentActivitySpaceComponentInfo.getComponentId());
                if(this.roleQueue!=null){
                    roleQueueDisplayName.setValue(this.roleQueue.getDisplayName());
                    roleQueueDescription.setValue(this.roleQueue.getDescription());
                }
                currentRoleQueueDisplayName=roleQueueDisplayName.getValue();
                currentRoleQueueDescription=roleQueueDescription.getValue();
            }
            roleQueueName.setReadOnly(true);
            roleQueueDisplayName.setReadOnly(true);
            roleQueueDescription.setReadOnly(true);
        }else {
            //For add new role queue
            footer.addComponent(addButton);
            roleQueueName.setValue("");
            roleQueueDisplayName.setValue("");
            roleQueueDescription.setValue("");
        }
    }

    private boolean addNewRoleQueue(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String roleQueueNameStr=roleQueueName.getValue();
        final String roleQueueDisplayNameStr=roleQueueDisplayName.getValue();
        final String roleQueueDescriptionStr=roleQueueDescription.getValue();
        if(roleQueueNameStr.equals("")||roleQueueDisplayNameStr.equals("")){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_PleaseInputAllFieldText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return false;
        }else{
            boolean alreadyExist=containerAddNewRoleQueuePanel.getRelatedRoleQueuesTable().checkRoleQueueExistence(roleQueueNameStr);
            if(alreadyExist){
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_RoleQueuesManagement_RoleQueueExistErrorText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return false;
            }
            //do add new logic
            Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                    getProperty("ActivityManagement_RoleQueuesManagement_ConfirmAddRoleQueueText")+
                    " <b>"+roleQueueNameStr +"</b>.", ContentMode.HTML);
            final ConfirmDialog addRoleQueueConfirmDialog = new ConfirmDialog();
            addRoleQueueConfirmDialog.setConfirmMessage(confirmMessage);
            final RoleQueueEditor self=this;
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_AddDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_RoleQueuesManagement_AddRoleQueueSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    //close confirm dialog
                    addRoleQueueConfirmDialog.close();
                    //execute callback logic
                    if(self.containerAddNewRoleQueuePanel!=null){
                        self.containerAddNewRoleQueuePanel.addNewRoleQueueFinishCallBack(
                                roleQueueNameStr, roleQueueDisplayNameStr, roleQueueDescriptionStr
                        );
                    }
                }
            };
            addRoleQueueConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(addRoleQueueConfirmDialog);
        }
        return true;
    }

    private boolean updateCurrentRoleQueue(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        final String roleQueueNameStr=roleQueueName.getValue();
        final String roleQueueDisplayNameStr=roleQueueDisplayName.getValue();
        final String roleQueueDescriptionStr=roleQueueDescription.getValue();
        if(roleQueueDisplayNameStr.equals("")){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_PleaseInputAllFieldText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return false;
        }else{
            //do update logic
            Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                    getProperty("ActivityManagement_RoleQueuesManagement_ConfirmUpdateRoleQueuePart1Text")+
                    " <b>"+roleQueueNameStr +"</b>"+userI18NProperties.
                    getProperty("ActivityManagement_RoleQueuesManagement_ConfirmUpdateRoleQueuePart2Text"), ContentMode.HTML);
            final ConfirmDialog updateRoleQueueConfirmDialog = new ConfirmDialog();
            updateRoleQueueConfirmDialog.setConfirmMessage(confirmMessage);
            final RoleQueueEditor self=this;
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    updateRoleQueueConfirmDialog.close();
                    boolean updateRoleQueueResult=
                            ActivitySpaceOperationUtil.updateRoleQueue(activitySpaceName,roleQueueNameStr,roleQueueDisplayNameStr,roleQueueDescriptionStr);
                    if(updateRoleQueueResult){
                        Notification resultNotification = new Notification(userI18NProperties.
                                getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                                userI18NProperties.
                                        getProperty("ActivityManagement_RoleQueuesManagement_UpdateRoleQueueSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());

                        //Need update current role queue's info for edit current queue's info directly by click browser tree item again
                        self.roleQueue=ActivitySpaceOperationUtil.getRoleQueueByName(activitySpaceName,roleQueueNameStr);
                        currentRoleQueueDisplayName=roleQueueDisplayName.getValue();
                        currentRoleQueueDescription=roleQueueDescription.getValue();

                        form.setReadOnly(true);
                        roleQueueDisplayName.setReadOnly(true);
                        roleQueueDescription.setReadOnly(true);
                        form.addStyleName("light");
                        updateButton.setCaption(userI18NProperties.
                                getProperty("ActivityManagement_Common_UpdateButtonLabel"));
                        updateButton.removeStyleName("primary");
                        updateButton.setIcon(FontAwesome.HAND_O_RIGHT);
                        footer.removeComponent(cancelButton);
                    }else{
                        Notification errorNotification = new Notification(userI18NProperties.
                                getProperty("ActivityManagement_RoleQueuesManagement_UpdateRoleQueueErrorText"),
                                userI18NProperties.
                                        getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }
                }
            };
            updateRoleQueueConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(updateRoleQueueConfirmDialog);
        }
        return true;
    }

    public void setContainerAddNewRoleQueuePanel(AddNewRoleQueuePanel containerAddNewRoleQueuePanel) {
        this.containerAddNewRoleQueuePanel = containerAddNewRoleQueuePanel;
    }

    public void setRoleQueue(RoleQueue roleQueue) {
        this.roleQueue = roleQueue;
    }
}
