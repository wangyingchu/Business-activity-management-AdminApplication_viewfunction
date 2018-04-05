package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.RuntimeEnvironmentUtil;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class UpdateActivityTypePanel extends VerticalLayout implements Upload.Receiver {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private SectionActionsBar addNewActivityTypeActionsBar;
    private Upload upload;
    private final static String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();
    private final static String definitionFileMIMEType="text/xml";

    private String templateDefinitionFileName;
    private String activitySpaceName;

    private TextField activityTypeName;
    private TextField activityTypeBPMNFile;
    private String activityType;

    private HorizontalLayout footer;
    private Button addButton;
    private Button cancelButton;
    private ActivityDefinitionBPMNEditor relatedActivityDefinitionBPMNEditor;

    public UpdateActivityTypePanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        // Update Current Activity Definition Section
        MainSectionTitle addNewActivityTypeSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_UpdateActivityTypeButtonLabel"));
        addComponent(addNewActivityTypeSectionTitle);
        addNewActivityTypeActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_Common_ActivitySpaceText")+" <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(addNewActivityTypeActionsBar);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        activityTypeName = new TextField(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeText"));
        activityTypeName.setRequired(true);
        form.addComponent(activityTypeName);

        activityTypeBPMNFile = new TextField(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_BPMNFileText"));
        activityTypeBPMNFile.setRequired(true);
        form.addComponent(activityTypeBPMNFile);

        activityTypeName.setEnabled(false);
        activityTypeBPMNFile.setEnabled(false);

        form.setReadOnly(true);

        footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        addButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_UpdateActivityTypeDefineButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateCurrentActivityType();
            }
        });
        addButton.setIcon(FontAwesome.UPLOAD);
        addButton.addStyleName("primary");

        cancelButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_CancelButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                activityTypeName.setEnabled(false);
                activityTypeName.setValue("");
                activityTypeBPMNFile.setValue("");
                footer.addComponent(upload);
                footer.removeComponent(addButton);
                footer.removeComponent(cancelButton);
                File uploadedFile = new File(templateDefinitionFileName);
                if(uploadedFile!=null){
                    uploadedFile.delete();
                }
            }
        });
        cancelButton.setIcon(FontAwesome.TIMES);

        upload = new Upload(null, this);
        // make analyzing start immediatedly when file is selected
        upload.setImmediate(true);
        upload.setButtonCaption(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_UploadBPMNFileButtonLabel"));
        /*
        upload.addStartedListener(new Upload.StartedListener() {
            @Override
            public void uploadStarted(Upload.StartedEvent startedEvent) {
                System.out.println("---Start---------------------------");
                System.out.println(startedEvent.getFilename());
                System.out.println(startedEvent.getContentLength());
                System.out.println(startedEvent.getMIMEType());
            }
        });
        upload.addFinishedListener(new Upload.FinishedListener() {
            @Override
            public void uploadFinished(Upload.FinishedEvent finishedEvent) {
                System.out.println("---Finish---------------------------");
                System.out.println(finishedEvent.getFilename());
                System.out.println(finishedEvent.getLength());
                System.out.println(finishedEvent.getMIMEType());
            }
        });
        */
        upload.addFailedListener(new Upload.FailedListener() {
            @Override
            public void uploadFailed(Upload.FailedEvent failedEvent) {
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("ActivityManagement_ActivityTypeManagement_UploadBPMNFileErrorText"),
                        userI18NProperties.
                                getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
            }
        });

        upload.addSucceededListener(new Upload.SucceededListener() {
            @Override
            public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
                if(!succeededEvent.getMIMEType().equals(definitionFileMIMEType)){
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_BPMNFileFormatErrorText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_ActivityTypeManagement_ShouldBeBPMNFormatErrorText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                    File wrongFile = new File(templateDefinitionFileName);
                    if(wrongFile!=null){
                        wrongFile.delete();
                    }
                }else{
                    renderUpdateActivityTypeUI(succeededEvent.getFilename());
                }
            }
        });
        footer.addComponent(upload);
    }

    private void renderUpdateActivityTypeUI(String fileName){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        int idx=fileName.indexOf(".");
        String activityType=fileName.substring(0,idx);
        String[] existingSpacesArray=ActivitySpaceOperationUtil.listActivitySpaces();
        for(String currentSpaceName:existingSpacesArray){
            String activitySpaceNamePrefix=currentSpaceName+"_";
            if(activityType.startsWith(activitySpaceNamePrefix)){
                if(!currentSpaceName.equals(activitySpaceName)){
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_DefinitionFileContentErrorText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_ActivityTypeManagement_DefinitionFileContentErrorDescText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                    File wrongFile = new File(templateDefinitionFileName);
                    if(wrongFile!=null){
                        wrongFile.delete();
                    }
                    return;
                }else{
                    activityType=activityType.replaceFirst(activitySpaceNamePrefix,"");
                }
            }
        }
        if(!activityType.equals(this.activityType)){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_DefinitionFileContentErrorText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_DefinitionTypeNotMatchErrorDescText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            File wrongFile = new File(templateDefinitionFileName);
            if(wrongFile!=null){
                wrongFile.delete();
            }
            return;
        }
        activityTypeName.setValue(activityType);
        activityTypeBPMNFile.setValue(fileName);
        activityTypeName.setEnabled(true);
        footer.removeComponent(upload);
        footer.addComponent(addButton);
        footer.addComponent(cancelButton);
    }

    private void updateCurrentActivityType(){
        //do update logic
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ConfirmUpdateDefinitionPart1Text")+
                " <b>"+ this.activityType +"</b>"+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ConfirmUpdateDefinitionPart2Text"), ContentMode.HTML);
        final ConfirmDialog addActivityTypeConfirmDialog = new ConfirmDialog();
        addActivityTypeConfirmDialog.setConfirmMessage(confirmMessage);
        final UpdateActivityTypePanel self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addActivityTypeConfirmDialog.close();
                //execute business logic
                boolean addNewActivityTypeResult=
                        ActivitySpaceOperationUtil.updateActivityTypeProcessDefinition(self.activitySpaceName, self.activityType, self.templateDefinitionFileName);
                if(addNewActivityTypeResult){
                    self.relatedActivityDefinitionBPMNEditor.refreshActivityDefinitionBPMNUI();
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_ActivityTypeManagement_UpdateProcessInfoSuccess"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    self.containerDialog.close();
                    self.relatedActivityDefinitionBPMNEditor.cleanActivityDefinitionProcessRelatedUIData();
                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_UpdateProcessInfoError"),
                            userI18NProperties.
                                    getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        addActivityTypeConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addActivityTypeConfirmDialog);
    }

    @Override
    public void attach() {
        super.attach();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label activitySpaceNameLabel=new Label(userI18NProperties.
                    getProperty("ActivityManagement_Common_ActivitySpaceText")+" <b>"+activitySpaceName+"</b>" , ContentMode.HTML);
            addNewActivityTypeActionsBar.resetSectionActionsBarContent(activitySpaceNameLabel);
        }
        activityTypeName.setEnabled(false);
        activityTypeName.setValue(this.activityType);
        activityTypeBPMNFile.setValue("");
        footer.addComponent(upload);
        footer.removeComponent(addButton);
        footer.removeComponent(cancelButton);
        if(templateDefinitionFileName!=null){
            File uploadedFile = new File(templateDefinitionFileName);
            if(uploadedFile!=null){
                uploadedFile.delete();
            }
        }
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    public OutputStream receiveUpload(String filename, String MIMEType) {
        String templateDefinationFilePerfix=this.activitySpaceName+"_";
        templateDefinitionFileName=tempFileDir+templateDefinationFilePerfix+filename;
        FileOutputStream fos = null;
        // Output stream to write to
        File file = new File(templateDefinitionFileName);
        try {
            // Open the file for writing.
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            // Error while opening the file. Not reported here.
            e.printStackTrace();
            return null;
        }
        return fos;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public void setRelatedActivityDefinitionBPMNEditor(ActivityDefinitionBPMNEditor relatedActivityDefinitionBPMNEditor) {
        this.relatedActivityDefinitionBPMNEditor = relatedActivityDefinitionBPMNEditor;
    }
}
