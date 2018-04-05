package com.viewfunction.vfbam.ui.component.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class PropertyValueEditor extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar editPropertyValueActionsBar;
    private Window containerDialog;
    private PropertyValuesActionTable relatedPropertyValuesActionTable;
    public static final String EDITMODE_UPDATE="EDITMODE_UPDATE";
    public static final String EDITMODE_NEW="EDITMODE_NEW";
    private String currentEditMode;
    private String propertyName;
    private HorizontalLayout footer;
    private Button saveButton;
    private Button addButton;
    private Button cancelButton;
    private TextField propertyValue;
    private String currentPropertyValue;
    public PropertyValueEditor(UserClientInfo currentUserClientInfo,String editorType,String propertyName){
        this.currentEditMode=editorType;
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.propertyName=propertyName;
        setSpacing(true);
        setMargin(true);
        MainSectionTitle propertyValueEditSectionTitle=null;

        if(EDITMODE_UPDATE.equals(this.currentEditMode)){
            propertyValueEditSectionTitle=new MainSectionTitle(userI18NProperties.
                    getProperty("ActivityManagement_Common_EditPropertyValueText"));
        }
        if(EDITMODE_NEW.equals(this.currentEditMode)){
            propertyValueEditSectionTitle=new MainSectionTitle(userI18NProperties.
                    getProperty("ActivityManagement_Common_AddPropertyValueText"));
        }
        addComponent(propertyValueEditSectionTitle);
        editPropertyValueActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_Common_ActivitySpaceText")+" <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(editPropertyValueActionsBar);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        propertyValue = new TextField(userI18NProperties.
                getProperty("ActivityManagement_Common_PropertyValueText"));
        propertyValue.setRequired(true);
        propertyValue.setWidth("100%");
        form.addComponent(propertyValue);
        form.setReadOnly(true);

        footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        saveButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_OKButtonLabel"));
        saveButton.setIcon(FontAwesome.CHECK);
        saveButton.addStyleName("primary");

        cancelButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_CancelButtonLabel"));
        cancelButton.setIcon(FontAwesome.TIMES);

        addButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_AddPropertyValueText"));
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");

        cancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                containerDialog.close();
            }
        });

        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(!propertyValue.getValue().equals("")){
                    if(relatedPropertyValuesActionTable.checkValueExistance(propertyValue.getValue())){
                        Notification errorNotification = new Notification(userI18NProperties.
                                getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                                userI18NProperties.
                                        getProperty("ActivityManagement_Common_PropertyValueAlreadyExistText"), Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }else{
                        containerDialog.close();
                        relatedPropertyValuesActionTable.doAddNewPropertyValue(propertyValue.getValue());
                    }

                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                            userI18NProperties.
                                    getProperty("Global_Application_DataOperation_PleaseInputAllFieldText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        });

        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(!propertyValue.getValue().equals("")){
                    if(relatedPropertyValuesActionTable.checkValueExistance(propertyValue.getValue())){
                        Notification errorNotification = new Notification(userI18NProperties.
                                getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                                userI18NProperties.
                                        getProperty("ActivityManagement_Common_PropertyValueAlreadyExistText"), Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }else{
                        containerDialog.close();
                        relatedPropertyValuesActionTable.doUpdateExistPropertyValue(currentPropertyValue,propertyValue.getValue());
                    }

                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                            userI18NProperties.
                                    getProperty("Global_Application_DataOperation_PleaseInputAllFieldText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        });

    }
    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    public void attach() {
        super.attach();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        Label sectionActionBarLabel=new Label(userI18NProperties.
                getProperty("ActivityManagement_Common_PropertyNameText")+" : <b>"+this.propertyName+"</b> " , ContentMode.HTML);
        editPropertyValueActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        footer.removeAllComponents();
        if(EDITMODE_NEW.equals(this.currentEditMode)){
            footer.addComponent(addButton);
            propertyValue.setValue("");
        }
        if(EDITMODE_UPDATE.equals(this.currentEditMode)){
            footer.addComponent(saveButton);
            footer.addComponent(cancelButton);
            if(currentPropertyValue!=null){
                propertyValue.setValue(currentPropertyValue);
            }
        }
    }

    public void setRelatedPropertyValuesActionTable(PropertyValuesActionTable relatedPropertyValuesActionTable) {
        this.relatedPropertyValuesActionTable = relatedPropertyValuesActionTable;
    }

    public void setCurrentPropertyValue(String currentPropertyValue) {
        this.currentPropertyValue = currentPropertyValue;
    }
}
