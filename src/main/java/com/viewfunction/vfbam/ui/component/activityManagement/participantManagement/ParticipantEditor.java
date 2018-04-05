package com.viewfunction.vfbam.ui.component.activityManagement.participantManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ParticipantEditor extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public static final String EDITMODE_UPDATE="EDITMODE_UPDATE";
    public static final String EDITMODE_NEW="EDITMODE_NEW";
    private String currentEditMode;

    private TextField participantName;
    private TextField participantDisplayName;
    private OptionGroup participantType;

    private Button updateButton;
    private Button cancelButton;
    private Button addButton;
    private HorizontalLayout footer;
    private final FormLayout form;

    private String currentParticipantDisplayName;
    private String currentParticipantType;

    private AddNewParticipantPanel containerAddNewParticipantPanel;
    private Participant participant;

    public ParticipantEditor(UserClientInfo currentUserClientInfo,String editorType){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.currentEditMode = editorType;
        form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        participantName = new TextField(userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_NamePropertyText"));
        participantName.setRequired(true);
        form.addComponent(participantName);

        participantDisplayName = new TextField(userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_DisplayPropertyText"));
        participantDisplayName.setWidth("50%");
        participantDisplayName.setRequired(true);
        form.addComponent(participantDisplayName);

        participantType = new OptionGroup(userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_TypePropertyText"));
        participantType.addItem("User");
        participantType.addItem("Group");
        participantType.select("User");
        participantType.addStyleName("horizontal");
        form.addComponent(participantType);

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
                    participantDisplayName.setReadOnly(false);
                    participantType.setReadOnly(false);
                    form.removeStyleName("light");
                    event.getButton().setCaption(userI18NProperties.
                            getProperty("ActivityManagement_Common_SaveButtonLabel"));
                    event.getButton().setIcon(FontAwesome.SAVE);
                    event.getButton().addStyleName("primary");
                    footer.addComponent(cancelButton);
                } else {
                    /* Do update participant logic */
                    updateCurrentParticipant();
                }
            }
        });
        updateButton.setIcon(FontAwesome.HAND_O_RIGHT);

        cancelButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_CancelButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                participantDisplayName.setValue(currentParticipantDisplayName);
                participantType.select(currentParticipantType);
                form.setReadOnly(true);
                participantDisplayName.setReadOnly(true);
                participantType.setReadOnly(true);
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
                getProperty("ActivityManagement_ParticipantsManagement_AddParticipantButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new participant logic */
                addNewParticipant();
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
        participantName.setReadOnly(false);
        participantDisplayName.setReadOnly(false);
        participantType.setReadOnly(false);
        if(!this.currentEditMode.equals(EDITMODE_NEW)){
            //For edit existing participant
            footer.addComponent(updateButton);
            if (currentActivitySpaceComponentInfo != null){
                if(this.participant!=null){
                    participantName.setValue(this.participant.getParticipantName());
                    participantDisplayName.setValue(this.participant.getDisplayName());
                    if(this.participant.isGroup()){
                        participantType.select("Group");
                    }else{
                        participantType.select("User");
                    }
                    currentParticipantDisplayName=participantDisplayName.getValue();
                    currentParticipantType=participantType.getValue().toString();
                }
            }
            participantName.setReadOnly(true);
            participantDisplayName.setReadOnly(true);
            participantType.setReadOnly(true);
        }else{
            //For add new participant
            footer.addComponent(addButton);
            participantName.setValue("");
            participantDisplayName.setValue("");
            participantType.select("User");
        }
    }

    private boolean addNewParticipant(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        final String participantNameStr=participantName.getValue();
        final String participantDisplayNameStr=participantDisplayName.getValue();
        final String participantTypeStr=participantType.getValue().toString();

        if(participantNameStr.equals("")||participantDisplayNameStr.equals("")){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_PleaseInputAllFieldText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return false;
        }else{
            boolean alreadyExist=this.containerAddNewParticipantPanel.getRelatedParticipantsTable().checkParticipantExistence(participantNameStr);
            if(alreadyExist){
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_ParticipantsManagement_ParticipantExistErrorText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return false;
            }
            //do add new logic
            Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                    getProperty("ActivityManagement_ParticipantsManagement_ConfirmAddParticipantText")+
                    " <b>"+participantNameStr +"</b>.", ContentMode.HTML);
            final ConfirmDialog addParticipantConfirmDialog = new ConfirmDialog();
            addParticipantConfirmDialog.setConfirmMessage(confirmMessage);
            final ParticipantEditor self=this;
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    addParticipantConfirmDialog.close();
                    //execute callback logic
                    if(self.containerAddNewParticipantPanel!=null){
                        self.containerAddNewParticipantPanel.addNewParticipantFinishCallBack(
                                participantNameStr,participantDisplayNameStr,participantTypeStr
                        );
                    }
                }
            };
            addParticipantConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(addParticipantConfirmDialog);
        }
        return true;
    }

    private boolean updateCurrentParticipant(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String participantNameStr=participantName.getValue();
        final String participantDisplayNameStr=participantDisplayName.getValue();
        final String participantTypeStr=participantType.getValue().toString();

        if(participantDisplayNameStr.equals("")){
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
                    getProperty("ActivityManagement_ParticipantsManagement_ConfirmUpdateParticipantPart1Text")+
                    " <b>"+participantNameStr +"</b>"+userI18NProperties.
                    getProperty("ActivityManagement_ParticipantsManagement_ConfirmUpdateParticipantPart2Text"), ContentMode.HTML);
            final ConfirmDialog updateParticipantConfirmDialog = new ConfirmDialog();
            updateParticipantConfirmDialog.setConfirmMessage(confirmMessage);
            final ParticipantEditor self=this;
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    updateParticipantConfirmDialog.close();
                    self.participant.setDisplayName(participantDisplayNameStr);
                    boolean updateParticipantResult=ActivitySpaceOperationUtil.updateParticipant(self.participant,participantTypeStr);
                    if(updateParticipantResult){
                        Notification resultNotification = new Notification(userI18NProperties.
                                getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                                userI18NProperties.
                                        getProperty("ActivityManagement_ParticipantsManagement_UpdateParticipantInfoSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());
                        currentParticipantDisplayName=participantDisplayName.getValue();
                        currentParticipantType=participantType.getValue().toString();
                        form.setReadOnly(true);
                        participantDisplayName.setReadOnly(true);
                        participantType.setReadOnly(true);
                        form.addStyleName("light");
                        updateButton.setCaption(userI18NProperties.
                                getProperty("ActivityManagement_Common_UpdateButtonLabel"));
                        updateButton.removeStyleName("primary");
                        updateButton.setIcon(FontAwesome.HAND_O_RIGHT);
                        footer.removeComponent(cancelButton);
                    }else{
                        Notification errorNotification = new Notification(userI18NProperties.
                                getProperty("ActivityManagement_ParticipantsManagement_UpdateParticipantInfoErrorText"),
                                userI18NProperties.
                                        getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }
                }
            };
            updateParticipantConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(updateParticipantConfirmDialog);
        }
        return true;
    }

    public void setContainerAddNewParticipantPanel(AddNewParticipantPanel containerAddNewParticipantPanel) {
        this.containerAddNewParticipantPanel = containerAddNewParticipantPanel;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
}
