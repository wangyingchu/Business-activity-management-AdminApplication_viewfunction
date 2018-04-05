package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement.ImportActivityTypeDataFieldDefinitionsPanel;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;
import java.io.File;

import java.util.Properties;

public class ActivityDataFieldsEditor extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private  ActivityDataFieldsActionTable activityDataFieldsActionTable;
    private ActivityDataFieldEditor activityDataFieldEditor;
    private String componentType;
    private String componentId;
    private FileDownloader activityDataFieldsDownloader;
    private File activityDataFieldsFile;
    private SectionActionButton exportDataFieldActionButton;

    public ActivityDataFieldsEditor(UserClientInfo currentUserClientInfo,String componentType,String componentId,String tableHeight){
        this.currentUserClientInfo=currentUserClientInfo;
        this.componentType=componentType;
        this.componentId=componentId;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        SectionActionsBar dataFieldsSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.TH_LIST.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_DataFieldsDefineText"), ContentMode.HTML));
        addComponent(dataFieldsSectionActionsBar);
        SectionActionButton addNewDataFieldActionButton = new SectionActionButton();
        addNewDataFieldActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_Common_AddDataFieldText"));
        addNewDataFieldActionButton.setIcon(FontAwesome.PLUS_SQUARE);

        if(this.componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION)){
            activityDataFieldEditor=new ActivityDataFieldEditor(this.currentUserClientInfo,ActivityDataFieldEditor.EDITMODE_NEW,false);
        }
        if(this.componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROSTER)){
            activityDataFieldEditor=new ActivityDataFieldEditor(this.currentUserClientInfo,ActivityDataFieldEditor.EDITMODE_NEW,true);
        }
        if(this.componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE)){
            activityDataFieldEditor=new ActivityDataFieldEditor(this.currentUserClientInfo,ActivityDataFieldEditor.EDITMODE_NEW,true);
        }

        if(activityDataFieldEditor!=null){
            activityDataFieldEditor.setRelatedActivityDataFieldsEditor(this);
            activityDataFieldEditor.setComponentType(this.componentType);
            activityDataFieldEditor.setComponentID(this.componentId);
        }

        addNewDataFieldActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(670.0f, Unit.PIXELS);
                window.setHeight(450.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(activityDataFieldEditor);
                activityDataFieldEditor.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        dataFieldsSectionActionsBar.addActionComponent(addNewDataFieldActionButton);
        if(this.componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION)){
            setActivityDataFieldsActionTable(new ActivityDataFieldsActionTable(this.currentUserClientInfo,tableHeight,true,false));
            getActivityDataFieldsActionTable().setDataFieldQueryType(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ACTIVITYTYPE);
            getActivityDataFieldsActionTable().setRelatedActivityDataFieldsEditor(this);
        }
        if(this.componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROSTER)){
            setActivityDataFieldsActionTable(new ActivityDataFieldsActionTable(this.currentUserClientInfo,tableHeight,true,true));
            getActivityDataFieldsActionTable().setDataFieldQueryType(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROSTER);
        }
        if(this.componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE)){
            setActivityDataFieldsActionTable(new ActivityDataFieldsActionTable(this.currentUserClientInfo,tableHeight,true,true));
            getActivityDataFieldsActionTable().setDataFieldQueryType(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROLEQUEUE);
        }

        if(this.componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION)) {
            SectionActionButton importDataFieldActionButton = new SectionActionButton();
            importDataFieldActionButton.setCaption(userI18NProperties.
                    getProperty("ActivityManagement_Common_ImportDataFieldText"));
            importDataFieldActionButton.setIcon(FontAwesome.DOWNLOAD);
            importDataFieldActionButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    importDataFields();
                }
            });
            dataFieldsSectionActionsBar.addActionComponent(importDataFieldActionButton);

            this.exportDataFieldActionButton = new SectionActionButton();
            this.exportDataFieldActionButton.setCaption(userI18NProperties.
                    getProperty("ActivityManagement_Common_ExportDataFieldText"));
            this.exportDataFieldActionButton.setIcon(FontAwesome.UPLOAD);
            dataFieldsSectionActionsBar.addActionComponent(exportDataFieldActionButton);
            setupFileDownloader();
        }

        if(getActivityDataFieldsActionTable() !=null){
            getActivityDataFieldsActionTable().setDataFieldsQueryId(this.componentId);
        }
        addComponent(getActivityDataFieldsActionTable());
    }

    public void addNewDataFieldFinishCallBack(String dataFieldName,String dataFieldDisplayName,String dataFieldType,
                                              String dataFieldDesc,boolean isArray,boolean isMandatory,boolean isSystem){
        getActivityDataFieldsActionTable().addNewDataField(dataFieldName, dataFieldDisplayName, dataFieldType, dataFieldDesc
                , isArray, isMandatory, isSystem);
        setupFileDownloader();
    }

    public ActivityDataFieldsActionTable getActivityDataFieldsActionTable() {
        return activityDataFieldsActionTable;
    }

    private void setActivityDataFieldsActionTable(ActivityDataFieldsActionTable activityDataFieldsActionTable) {
        this.activityDataFieldsActionTable = activityDataFieldsActionTable;
    }

    public boolean dataFieldNotExistCheck(String dataFieldName){
        return  this.activityDataFieldsActionTable.dataFieldNotExistCheck(dataFieldName);
    }

    private void importDataFields(){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String activityType= this.componentId;
        ImportActivityTypeDataFieldDefinitionsPanel importActivityTypeDataFieldDefinitionsPanel=
                new ImportActivityTypeDataFieldDefinitionsPanel(this.currentUserClientInfo,activitySpaceName,activityType);
        final Window window = new Window();
        window.setHeight(270.0f, Unit.PIXELS);
        window.setWidth(600.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(importActivityTypeDataFieldDefinitionsPanel);
        importActivityTypeDataFieldDefinitionsPanel.setContainerDialog(window);
        importActivityTypeDataFieldDefinitionsPanel.setRelatedActivityDataFieldsEditor(this);
        UI.getCurrent().addWindow(window);
    }

    private void setupFileDownloader(){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String activityType= this.componentId;
        this.activityDataFieldsFile= ActivitySpaceOperationUtil.generateActivityTypeDataFieldsJsonFile(activitySpaceName,activityType);
        Resource res = new FileResource(this.activityDataFieldsFile);
        if(this.activityDataFieldsDownloader==null){
            this.activityDataFieldsDownloader = new FileDownloader(res);
            this.activityDataFieldsDownloader.extend(this.exportDataFieldActionButton);
        }else{
            this.activityDataFieldsDownloader.setFileDownloadResource(res);
        }
    }

    public void importActivityDataFieldsSuccessCallBack(){
        if(getActivityDataFieldsActionTable()!=null){
            getActivityDataFieldsActionTable().setDataFieldDefinitionsList(null);
            getActivityDataFieldsActionTable().loadActivityDataFieldsData();
        }
        setupFileDownloader();
    }

    public void deleteActivityDataFieldSuccessCallBack(){
        setupFileDownloader();
    }

    public void updateActivityDataFieldSuccessCallBack(){
        setupFileDownloader();
    }
}

