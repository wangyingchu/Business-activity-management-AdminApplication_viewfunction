package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RosterEditor extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public static final String EDITMODE_UPDATE="EDITMODE_UPDATE";
    public static final String EDITMODE_NEW="EDITMODE_NEW";
    private String currentEditMode;

    private final FormLayout form;
    private HorizontalLayout footer;
    private TextField rosterName;
    private TextField rosterDisplayName;
    private TextArea rosterDescription;

    private Button updateButton;
    private Button cancelButton;
    private Button addButton;

    private String currentRosterDisplayName;
    private String currentRosterDescription;

    private Roster roster;
    private AddNewRosterPanel containerAddNewRosterPanel;

    public RosterEditor(UserClientInfo currentUserClientInfo,String editorType){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.currentEditMode = editorType;
        form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        rosterName = new TextField(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_NamePropertyText"));
        rosterName.setRequired(true);
        form.addComponent(rosterName);

        rosterDisplayName = new TextField(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_DisplayNamePropertyText"));
        rosterDisplayName.setRequired(true);
        rosterDisplayName.setWidth("50%");
        form.addComponent(rosterDisplayName);

        rosterDescription = new TextArea(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_DescriptionPropertyText"));
        rosterDescription.setWidth("50%");
        rosterDescription.setRows(3);
        form.addComponent(rosterDescription);

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
                    rosterDisplayName.setReadOnly(false);
                    rosterDescription.setReadOnly(false);
                    form.removeStyleName("light");
                    event.getButton().setCaption(userI18NProperties.
                            getProperty("ActivityManagement_Common_SaveButtonLabel"));
                    event.getButton().setIcon(FontAwesome.SAVE);
                    event.getButton().addStyleName("primary");
                    footer.addComponent(cancelButton);
                } else {
                    /* Do update roster logic */
                    updateCurrentRoster();
                }
            }
        });
        updateButton.setIcon(FontAwesome.HAND_O_RIGHT);

        cancelButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_CancelButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                rosterDisplayName.setValue(currentRosterDisplayName);
                rosterDescription.setValue(currentRosterDescription);
                form.setReadOnly(true);
                rosterDisplayName.setReadOnly(true);
                rosterDescription.setReadOnly(true);
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
                getProperty("ActivityManagement_RosterManagement_AddRosterButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new roster logic */
                addNewRoster();
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
        rosterName.setReadOnly(false);
        rosterDisplayName.setReadOnly(false);
        rosterDescription.setReadOnly(false);
        if(!this.currentEditMode.equals(EDITMODE_NEW)){
            //For edit existing role
            footer.addComponent(updateButton);
            if (currentActivitySpaceComponentInfo != null){
                rosterName.setValue(currentActivitySpaceComponentInfo.getComponentId());
                if(this.roster!=null){
                    rosterDisplayName.setValue(this.roster.getDisplayName());
                    rosterDescription.setValue(this.roster.getDescription());
                }
                currentRosterDisplayName=rosterDisplayName.getValue();
                currentRosterDescription=rosterDescription.getValue();
            }
            rosterName.setReadOnly(true);
            rosterDisplayName.setReadOnly(true);
            rosterDescription.setReadOnly(true);
        } else{
            //For add new roster
            footer.addComponent(addButton);
            rosterName.setValue("");
            rosterDisplayName.setValue("");
            rosterDescription.setValue("");
        }
    }

    private boolean addNewRoster(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String rosterNameStr=rosterName.getValue();
        final String rosterDisplayNameStr=rosterDisplayName.getValue();
        final String rosterDescriptionStr=rosterDescription.getValue();
        if(rosterNameStr.equals("")||rosterDisplayNameStr.equals("")){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_PleaseInputAllFieldText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return false;
        }else{
            boolean alreadyExist=containerAddNewRosterPanel.getRostersActionTable().checkRosterExistence(rosterNameStr);
            if(alreadyExist){
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_RosterManagement_RosterExistErrorText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return false;
            }
            //do add new logic
            Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                    getProperty("ActivityManagement_RosterManagement_ConfirmAddRosterText")+
                    " <b>"+rosterNameStr +"</b>.", ContentMode.HTML);
            final ConfirmDialog addRosterConfirmDialog = new ConfirmDialog();
            addRosterConfirmDialog.setConfirmMessage(confirmMessage);
            final RosterEditor self=this;
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_AddDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_RosterManagement_AddRosterSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    //close confirm dialog
                    addRosterConfirmDialog.close();
                    //execute callback logic
                    if(self.containerAddNewRosterPanel!=null){
                        self.containerAddNewRosterPanel.addNewRosterFinishCallBack(
                                rosterNameStr, rosterDisplayNameStr, rosterDescriptionStr
                        );
                    }
                }
            };
            addRosterConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(addRosterConfirmDialog);
        }
        return true;
    }

    private boolean updateCurrentRoster(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        final String rosterNameStr=rosterName.getValue();
        final String rosterDisplayNameStr=rosterDisplayName.getValue();
        final String rosterDescriptionStr=rosterDescription.getValue();
        if(rosterDisplayNameStr.equals("")){
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
                    getProperty("ActivityManagement_RosterManagement_ConfirmUpdateRosterPart1Text")+
                    " <b>"+rosterNameStr +"</b>"+userI18NProperties.
                    getProperty("ActivityManagement_RosterManagement_ConfirmUpdateRosterPart2Text"), ContentMode.HTML);
            final ConfirmDialog updateRosterConfirmDialog = new ConfirmDialog();
            updateRosterConfirmDialog.setConfirmMessage(confirmMessage);
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    updateRosterConfirmDialog.close();
                    boolean updateRosterResult=
                            ActivitySpaceOperationUtil.updateRoster(activitySpaceName,rosterNameStr,rosterDisplayNameStr,rosterDescriptionStr);
                    if(updateRosterResult){
                        Notification resultNotification = new Notification(userI18NProperties.
                                getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                                userI18NProperties.
                                        getProperty("ActivityManagement_RosterManagement_UpdateRosterSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());

                        currentRosterDisplayName=rosterDisplayName.getValue();
                        currentRosterDescription=rosterDescription.getValue();
                        form.setReadOnly(true);
                        rosterDisplayName.setReadOnly(true);
                        rosterDescription.setReadOnly(true);
                        form.addStyleName("light");
                        updateButton.setCaption(userI18NProperties.
                                getProperty("ActivityManagement_Common_UpdateButtonLabel"));
                        updateButton.removeStyleName("primary");
                        updateButton.setIcon(FontAwesome.HAND_O_RIGHT);
                        footer.removeComponent(cancelButton);
                    }else{
                        Notification errorNotification = new Notification(userI18NProperties.
                                getProperty("ActivityManagement_RosterManagement_UpdateRosterErrorText"),
                                userI18NProperties.
                                        getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }
                }
            };
            updateRosterConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(updateRosterConfirmDialog);
        }
        return true;
    }

    public void setContainerAddNewRosterPanel(AddNewRosterPanel containerAddNewRosterPanel) {
        this.containerAddNewRosterPanel = containerAddNewRosterPanel;
    }

    public void setRoster(Roster roster) {
        this.roster = roster;
    }
}
