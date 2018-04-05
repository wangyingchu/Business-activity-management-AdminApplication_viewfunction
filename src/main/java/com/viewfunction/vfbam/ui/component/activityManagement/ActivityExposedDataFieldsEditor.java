package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ActivityExposedDataFieldsEditor extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private ActivityExposedDataFieldsActionTable activityExposedDataFieldsActionTable;
    private ActivityExposedDataFieldEditor activityExposedDataFieldEditor;
    private String dataFieldsQueryId;
    private String dataFieldQueryType;
    private List<ActivityDataFieldVO> activityDataFieldsList;
    public static final String DATAFIELDS_TYPE_STARTPOINT="DATAFIELDS_TYPE_STARTPOINT";
    public static final String DATAFIELDS_TYPE_ACTIYITYSTEP="DATAFIELDS_TYPE_ACTIYITYSTEP";
    private ActivityStepVO activityStep;
    private SectionActionButton addNewExposedDataFieldActionButton;
    public ActivityExposedDataFieldsEditor(UserClientInfo currentUserClientInfo,
                                           String dataFieldQueryType,String dataFieldsQueryId,ActivityStepVO activityStep){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.dataFieldQueryType=dataFieldQueryType;
        this.dataFieldsQueryId=dataFieldsQueryId;
        this.activityStep=activityStep;

        SectionActionsBar dataFieldsSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.TH_LIST.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_Common_ExposedFieldText"), ContentMode.HTML));
        addComponent(dataFieldsSectionActionsBar);
        addNewExposedDataFieldActionButton = new SectionActionButton();
        addNewExposedDataFieldActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_Common_AddExposedFieldText"));
        addNewExposedDataFieldActionButton.setIcon(FontAwesome.PLUS_SQUARE);

        activityExposedDataFieldEditor=new ActivityExposedDataFieldEditor(this.currentUserClientInfo,ActivityExposedDataFieldEditor.EDITMODE_NEW);
        activityExposedDataFieldEditor.setRelatedActivityDataFieldsEditor(this);

        addNewExposedDataFieldActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(670.0f, Unit.PIXELS);
                window.setHeight(300.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(activityExposedDataFieldEditor);
                activityExposedDataFieldEditor.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        dataFieldsSectionActionsBar.addActionComponent(addNewExposedDataFieldActionButton);
        if(DATAFIELDS_TYPE_STARTPOINT.equals(dataFieldQueryType)){
            activityExposedDataFieldsActionTable=new ActivityExposedDataFieldsActionTable(this.currentUserClientInfo,"150");
            activityExposedDataFieldsActionTable.setRelatedActivityDataFieldsEditor(this);
        }else{
            activityExposedDataFieldsActionTable=new ActivityExposedDataFieldsActionTable(this.currentUserClientInfo,"230");
            activityExposedDataFieldsActionTable.setRelatedActivityDataFieldsEditor(this);
        }
        activityExposedDataFieldsActionTable.setDataFieldQueryType(this.getDataFieldQueryType());
        activityExposedDataFieldsActionTable.setDataFieldsQueryId(this.getDataFieldsQueryId());
        activityExposedDataFieldsActionTable.setActivityStep(this.activityStep);
        addComponent(activityExposedDataFieldsActionTable);
    }

    public void addNewExposedDataField(String dataFieldName,boolean isReadable,boolean isWritable,boolean isMandatory){
        ActivityDataFieldVO newActivityDataFieldVO=getActivityDataFieldByName(dataFieldName);
        activityExposedDataFieldsActionTable.addNewDataField(dataFieldName,
                newActivityDataFieldVO.getDataFieldDisplayName(), newActivityDataFieldVO.getDataType(),
                isMandatory, isReadable, isWritable);
    }

    public String getDataFieldsQueryId() {
        return dataFieldsQueryId;
    }

    public String getDataFieldQueryType() {
        return dataFieldQueryType;
    }

    public void disableEdit(){
        this.addNewExposedDataFieldActionButton.setEnabled(false);
        this.activityExposedDataFieldsActionTable.disableTableEdit();
    }

    public void enableEdit(){
        this.addNewExposedDataFieldActionButton.setEnabled(true);
        this.activityExposedDataFieldsActionTable.enableTableEdit();
    }

    public List<ActivityDataFieldVO> getActivityDataFieldsList() {
        return activityDataFieldsList;
    }

    public void setActivityDataFieldsList(List<ActivityDataFieldVO> activityDataFieldsList) {
        this.activityDataFieldsList = activityDataFieldsList;
    }

    public void setExposedDataFields(List<ActivityDataFieldVO> exposedDataFields){
        activityExposedDataFieldsActionTable.clearActivityDataData();
        if(exposedDataFields!=null){
            for(ActivityDataFieldVO currentDataFieldVO:exposedDataFields){
                activityExposedDataFieldsActionTable.addNewDataField(currentDataFieldVO.getDataFieldName(),
                        currentDataFieldVO.getDataFieldDisplayName(), currentDataFieldVO.getDataType(),
                        currentDataFieldVO.isMandatoryField(), currentDataFieldVO.isReadableField(), currentDataFieldVO.isWritableField());
            }
        }
    }

    private ActivityDataFieldVO getActivityDataFieldByName(String dataFieldName){
        if(activityDataFieldsList!=null){
            for(ActivityDataFieldVO currentActivityDataFieldVO:activityDataFieldsList){
                if(currentActivityDataFieldVO.getDataFieldName().equals(dataFieldName)){
                    return currentActivityDataFieldVO;
                }
            }
        }
        return null;
    }

    public ActivityDataFieldVO getActivityExposedDataFieldByName(String dataFieldName){
        List<ActivityDataFieldVO> currentExposedDataFields=getExposedDataFields();
        if(currentExposedDataFields!=null){
            for(ActivityDataFieldVO currentActivityDataFieldVO:currentExposedDataFields){
                if(currentActivityDataFieldVO.getDataFieldName().equals(dataFieldName)){
                    return currentActivityDataFieldVO;
                }
            }
        }
        return null;
    }

    public List<ActivityDataFieldVO> getExposedDataFields(){
        List<ActivityDataFieldVO> currentExposedDataFieldList= activityExposedDataFieldsActionTable.getExposedDataFields();
        List<ActivityDataFieldVO> exposedDataFieldList=new ArrayList<ActivityDataFieldVO>();
        for(ActivityDataFieldVO currentActivityDataFieldVO:currentExposedDataFieldList){
            ActivityDataFieldVO activityTypeDataField=getActivityDataFieldByName(currentActivityDataFieldVO.getDataFieldName());
            if(activityTypeDataField!=null){
                ActivityDataFieldVO exposedDataField=new ActivityDataFieldVO();
                exposedDataField.setDataFieldName(activityTypeDataField.getDataFieldName());
                exposedDataField.setDataFieldDisplayName(activityTypeDataField.getDataFieldDisplayName());
                exposedDataField.setDescription(activityTypeDataField.getDescription());
                exposedDataField.setDataType(activityTypeDataField.getDataType());
                exposedDataField.setArrayField(activityTypeDataField.isArrayField());
                exposedDataField.setSystemField(activityTypeDataField.isSystemField());
                exposedDataField.setMandatoryField(currentActivityDataFieldVO.isMandatoryField());
                exposedDataField.setReadableField(currentActivityDataFieldVO.isReadableField());
                exposedDataField.setWritableField(currentActivityDataFieldVO.isWritableField());
                exposedDataFieldList.add(exposedDataField);
            }
        }
        return exposedDataFieldList;
    }
}
