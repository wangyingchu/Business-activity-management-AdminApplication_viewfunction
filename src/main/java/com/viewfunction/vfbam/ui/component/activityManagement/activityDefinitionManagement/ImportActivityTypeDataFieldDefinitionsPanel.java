package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityDataFieldsEditor;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.RuntimeEnvironmentUtil;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Created by wangychu on 6/19/17.
 */
public class ImportActivityTypeDataFieldDefinitionsPanel extends VerticalLayout implements Upload.Receiver{

    private UserClientInfo currentUserClientInfo;
    private Upload upload;
    private OptionGroup existedDataFieldHandleMethodsOptionGroup;
    private Window containerDialog;
    private String templateActivityTypeDataFieldDefinitionsFile;
    private String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();
    private String activitySpaceName;
    private String activityType;
    private ActivityDataFieldsEditor relatedActivityDataFieldsEditor;

    public ImportActivityTypeDataFieldDefinitionsPanel(UserClientInfo currentUserClientInfo,String activitySpaceName,String activityType){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        MainSectionTitle addNewPropertySectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ImportActivityTypeDataFieldDefinitionText"));
        addComponent(addNewPropertySectionTitle);

        this.activitySpaceName=activitySpaceName;
        this.activityType=activityType;

        Label sectionActionBarLabel=new Label(FontAwesome.DATABASE.getHtml()+" "+activitySpaceName + FontAwesome.ANGLE_RIGHT.getHtml()+" "+activityType, ContentMode.HTML);
        SectionActionsBar dataFieldActionsBar=new SectionActionsBar(sectionActionBarLabel);
        addComponent(dataFieldActionsBar);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        existedDataFieldHandleMethodsOptionGroup = new OptionGroup(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ExistDataFieldDefinitionHandleMethodText"));
        existedDataFieldHandleMethodsOptionGroup.addItem(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_IgnoreDataFieldDefinitionHandleMethodText"));
        existedDataFieldHandleMethodsOptionGroup.addItem(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ReplaceDataFieldDefinitionHandleMethodText"));
        existedDataFieldHandleMethodsOptionGroup.select(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_IgnoreDataFieldDefinitionHandleMethodText"));
        existedDataFieldHandleMethodsOptionGroup.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        form.addComponent(existedDataFieldHandleMethodsOptionGroup);

        upload = new Upload(null, this);
        //make analyzing start immediatedly when file is selected
        upload.setImmediate(true);
        upload.setButtonCaption(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_SelectDataFieldDefinitionFileText"));

        upload.addFailedListener(new Upload.FailedListener() {
            @Override
            public void uploadFailed(Upload.FailedEvent failedEvent) {
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("Global_Application_DataOperation_DataTransferErrorText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_ActivityTypeManagement_UploadDataFieldDefinitionFileErrorPart1Text")+" "+failedEvent.getFilename()+" "+userI18NProperties.
                        getProperty("ActivityManagement_Common_ErrorText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
            }
        });

        upload.addSucceededListener(new Upload.SucceededListener() {

            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                if("application/json".equals(event.getMIMEType())){
                    executeImportActivityTypeDataFieldDefinitions();
                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_ActivityTypeManagement_DataFieldDefinitionFileFormatErrorText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        });

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);
        footer.addComponent(upload);

        addComponent(form);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        templateActivityTypeDataFieldDefinitionsFile=tempFileDir+this.activitySpaceName+"_"+this.activityType+"_"+new Date().getTime()+filename;
        FileOutputStream fos = null;
        // Output stream to write to
        File file = new File(templateActivityTypeDataFieldDefinitionsFile);
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

    private void executeImportActivityTypeDataFieldDefinitions(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String methodSelectedValue=existedDataFieldHandleMethodsOptionGroup.getValue().toString();
        String handleMethod=null;
        if(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ReplaceDataFieldDefinitionHandleMethodText").equals(methodSelectedValue)){
            handleMethod=ActivitySpaceOperationUtil.ExistedDataHandleMethod_REPLACE;
        }
        if(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_IgnoreDataFieldDefinitionHandleMethodText").equals(methodSelectedValue)){
            handleMethod=ActivitySpaceOperationUtil.ExistedDataHandleMethod_IGNORE;
        }
        boolean importDataResult= ActivitySpaceOperationUtil.
                importActivityTypeDataFieldDefinitionsFromJsonFile(this.activitySpaceName,this.activityType,this.templateActivityTypeDataFieldDefinitionsFile,handleMethod);
        if(importDataResult){
            this.containerDialog.close();
            if(this.getRelatedActivityDataFieldsEditor()!=null){
                this.getRelatedActivityDataFieldsEditor().importActivityDataFieldsSuccessCallBack();
            }
            Notification resultNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_AddDataSuccessText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_ImportActivityTypeDataFieldDefinitionSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_ImportActivityTypeDataFieldDefinitionErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    public ActivityDataFieldsEditor getRelatedActivityDataFieldsEditor() {
        return relatedActivityDataFieldsEditor;
    }

    public void setRelatedActivityDataFieldsEditor(ActivityDataFieldsEditor relatedActivityDataFieldsEditor) {
        this.relatedActivityDataFieldsEditor = relatedActivityDataFieldsEditor;
    }
}
