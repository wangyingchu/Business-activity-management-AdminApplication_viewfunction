package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.viewfunction.activityEngine.activityView.common.CustomAttribute;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.util.ApplicationConstant;
import com.viewfunction.vfbam.ui.util.UserClientInfo;
import javax.jcr.PropertyType;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

/**
 * Created by wangychu on 2/1/17.
 */
public class CustomAttributeItem extends HorizontalLayout{

    private UserClientInfo currentUserClientInfo;
    private CustomAttribute customAttribute;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
    private CustomAttributeItemsActionList containerCustomAttributeItemsList;
    private Label attributeValueDisplayLabel;

    public CustomAttributeItem(UserClientInfo currentUserClientInfo,CustomAttribute customAttribute){
        this.currentUserClientInfo=currentUserClientInfo;
        this.customAttribute=customAttribute;
        this.setWidth(100,Unit.PERCENTAGE);
        this.addStyleName("ui_appSectionDiv");
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        HorizontalLayout attributeDataContainerLayout=new HorizontalLayout();
        addComponent(attributeDataContainerLayout);
        setExpandRatio(attributeDataContainerLayout, 1);

        HorizontalLayout attributeInfoContainerLayout=new HorizontalLayout();
        attributeDataContainerLayout.addComponent(attributeInfoContainerLayout);

        Label attributeNameLabel=new Label(FontAwesome.CIRCLE_THIN.getHtml()+"&nbsp;&nbsp;"+this.customAttribute.getAttributeName(), ContentMode.HTML);
        attributeNameLabel.addStyleName(ValoTheme.LABEL_BOLD);
        attributeInfoContainerLayout.addComponent(attributeNameLabel);

        Label dataTypeLabel=null;
        switch(this.customAttribute.getAttributeType()){
            case PropertyType.STRING:
                dataTypeLabel=generateDataTypeLabel(ApplicationConstant.DataFieldType_STRING);
                break;
            case PropertyType.BOOLEAN:
                dataTypeLabel=generateDataTypeLabel(ApplicationConstant.DataFieldType_BOOLEAN);
                break;
            case PropertyType.DATE:
                dataTypeLabel=generateDataTypeLabel(ApplicationConstant.DataFieldType_DATE);
                break;
            case PropertyType.DECIMAL:
                dataTypeLabel=generateDataTypeLabel(ApplicationConstant.DataFieldType_DECIMAL);
                break;
            case PropertyType.LONG:
                dataTypeLabel=generateDataTypeLabel(ApplicationConstant.DataFieldType_LONG);
                break;
            case PropertyType.DOUBLE:
                dataTypeLabel=generateDataTypeLabel(ApplicationConstant.DataFieldType_DOUBLE);
                break;
            case PropertyType.BINARY:
                dataTypeLabel=generateDataTypeLabel(ApplicationConstant.DataFieldType_BINARY);
                break;
        }
        attributeInfoContainerLayout.addComponent(dataTypeLabel);

        Label attributeValueDiv=new Label("&nbsp;&nbsp;:&nbsp;&nbsp;",ContentMode.HTML);
        attributeValueDiv.addStyleName(ValoTheme.LABEL_LIGHT);
        attributeInfoContainerLayout.addComponent(attributeValueDiv);
        attributeValueDisplayLabel=new Label(generateDataValueString());
        attributeInfoContainerLayout.addComponent(attributeValueDisplayLabel);

        if(this.customAttribute.isArrayAttribute()){
            attributeInfoContainerLayout.addComponent(generateDataTypeLabel("A"));
        }

        HorizontalLayout actionButtonsContainerLayout=new HorizontalLayout();
        this.addComponent(actionButtonsContainerLayout);

        Button editDataFieldsButton = new Button();
        editDataFieldsButton.setIcon(FontAwesome.EDIT);
        editDataFieldsButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_Common_EditConfigItemPropertyValueText"));
        editDataFieldsButton.addStyleName("small");
        editDataFieldsButton.addStyleName("borderless");
        actionButtonsContainerLayout.addComponent(editDataFieldsButton);
        editDataFieldsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                editStructureAttribute();
            }
        });

        Button deleteDataFieldButton = new Button();
        deleteDataFieldButton.setIcon(FontAwesome.TRASH_O);
        deleteDataFieldButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_Common_DeleteConfigItemPropertyText"));
        deleteDataFieldButton.addStyleName("small");
        deleteDataFieldButton.addStyleName("borderless");
        actionButtonsContainerLayout.addComponent(deleteDataFieldButton);
        deleteDataFieldButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                deleteStructureAttribute();
            }
        });
    }

    private Label generateDataTypeLabel(String dataType){
        Label dataTypeLabel=new Label("&nbsp;&nbsp;"+ FontAwesome.CHEVRON_LEFT.getHtml()+" "+ dataType+FontAwesome.CHEVRON_RIGHT.getHtml(),ContentMode.HTML);
        dataTypeLabel.addStyleName(ValoTheme.LABEL_TINY);
        dataTypeLabel.addStyleName(ValoTheme.LABEL_LIGHT);
        return dataTypeLabel;
    }

    private String generateDataValueString(){
        Object attributeValue=this.customAttribute.getAttributeValue();
        boolean isArrayValue=this.customAttribute.isArrayAttribute();
        String dataValueString="";
        switch(this.customAttribute.getAttributeType()){
            case PropertyType.STRING:
                if(isArrayValue){
                    String[] dataArray=(String[])attributeValue;
                    if(dataArray.length>0){
                        dataValueString=dataArray[0].toString()+" ......";
                    }
                }else{
                    dataValueString=attributeValue.toString();
                }
                break;
            case PropertyType.BOOLEAN:
                if(isArrayValue){
                    boolean[] dataArray=(boolean[])attributeValue;
                    if(dataArray.length>0){
                        dataValueString=""+dataArray[0]+" ......";
                    }
                }else{
                    dataValueString=""+((Boolean)attributeValue).booleanValue();
                }
                break;
            case PropertyType.DATE:
                if(isArrayValue){
                    Calendar[] dataArray=(Calendar[])attributeValue;
                    if(dataArray.length>0){
                        dataValueString= dateFormatter.format(((Calendar)dataArray[0]).getTime())+" ......";
                    }
                }else{
                    dataValueString= dateFormatter.format(((Calendar)attributeValue).getTime());
                }
                break;
            case PropertyType.DECIMAL:
                if(isArrayValue){
                    BigDecimal[] dataArray=(BigDecimal[])attributeValue;
                    if(dataArray.length>0){
                        dataValueString=dataArray[0].toPlainString()+" ......";
                    }
                }else{
                    dataValueString=((BigDecimal)attributeValue).toPlainString();
                }
                break;
            case PropertyType.LONG:
                if(isArrayValue){
                    long[] dataArray=(long[])attributeValue;
                    if(dataArray.length>0){
                        dataValueString=""+dataArray[0]+" ......";
                    }
                }else{
                    dataValueString=""+((Long)attributeValue).longValue();
                }
                break;
            case PropertyType.DOUBLE:
                if(isArrayValue){
                    double[] dataArray=(double[])attributeValue;
                    if(dataArray.length>0){
                        dataValueString=""+dataArray[0]+" ......";
                    }
                }else{
                    dataValueString=""+((Double)attributeValue).doubleValue();
                }
                break;
            case PropertyType.BINARY:
                if(isArrayValue){

                }else{

                }
                break;
        }
        return dataValueString;
    }

    private void deleteStructureAttribute(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_Common_ConfirmDeleteConfigItemPropertyText")+
                " <b>"+this.customAttribute.getAttributeName() +"</b>.", ContentMode.HTML);
        final ConfirmDialog deletePropertyConfirmDialog = new ConfirmDialog();
        deletePropertyConfirmDialog.setConfirmMessage(confirmMessage);
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                deletePropertyConfirmDialog.close();
                if(getContainerCustomAttributeItemsList()!=null){
                    getContainerCustomAttributeItemsList().deleteStructureAttribute(customAttribute.getAttributeName());
                }
            }
        };
        deletePropertyConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(deletePropertyConfirmDialog);
    }

    private void editStructureAttribute(){
        EditConfigurationItemPropertyPanel editConfigurationItemPropertyPanel=new EditConfigurationItemPropertyPanel(currentUserClientInfo,this.customAttribute);
        editConfigurationItemPropertyPanel.setRelatedCustomAttributeItem(this);
        final Window window = new Window();
        window.setWidth(600.0f, Unit.PIXELS);
        window.setHeight(700.0f, Unit.PIXELS);
        window.setModal(true);
        window.setResizable(false);
        window.center();
        window.setContent(editConfigurationItemPropertyPanel);
        editConfigurationItemPropertyPanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    public CustomAttributeItemsActionList getContainerCustomAttributeItemsList() {
        return containerCustomAttributeItemsList;
    }

    public void setContainerCustomAttributeItemsList(CustomAttributeItemsActionList containerCustomAttributeItemsList) {
        this.containerCustomAttributeItemsList = containerCustomAttributeItemsList;
    }

    public boolean updateCustomAttributeValue(CustomAttribute customAttribute){
        boolean updateAttributeResult=getContainerCustomAttributeItemsList().updateCustomAttributeValue(customAttribute);
        if(updateAttributeResult){
            this.customAttribute =customAttribute;
            attributeValueDisplayLabel.setValue(generateDataValueString());
        }
        return updateAttributeResult;
    }
}
