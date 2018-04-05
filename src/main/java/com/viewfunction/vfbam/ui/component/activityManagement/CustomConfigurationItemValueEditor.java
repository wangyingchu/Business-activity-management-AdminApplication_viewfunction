package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

/**
 * Created by wangychu on 1/29/17.
 */
public class CustomConfigurationItemValueEditor extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private TextField configItemValue;
    private ConfigurationItemsTree containerConfigurationItemsTree;
    private String parentConfigurationItem;

    public CustomConfigurationItemValueEditor(UserClientInfo currentUserClientInfo,String parentConfigurationItem){
        this.currentUserClientInfo=currentUserClientInfo;
        this.parentConfigurationItem=parentConfigurationItem;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        MainSectionTitle propertyValueEditSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_Common_AddNewConfigurationItemText"));
        addComponent(propertyValueEditSectionTitle);

        SectionActionsBar editorActionsBar=new SectionActionsBar(new Label(userI18NProperties.
                getProperty("ActivityManagement_Common_ParentConfigurationItemText")+" : <b>"+ this.parentConfigurationItem+"</b>" , ContentMode.HTML));
        addComponent(editorActionsBar);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        configItemValue = new TextField(userI18NProperties.
                getProperty("ActivityManagement_Common_ConfigurationItemNameText"));
        configItemValue.setRequired(true);
        configItemValue.setWidth("100%");
        form.addComponent(configItemValue);
        form.setReadOnly(false);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button saveButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_AddConfigurationItemButtonLabel"));
        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(!configItemValue.getValue().equals("")){
                    getContainerConfigurationItemsTree().addNewConfigurationItem(configItemValue.getValue());
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
        saveButton.setIcon(FontAwesome.CHECK);
        saveButton.addStyleName("primary");
        footer.addComponent(saveButton);
    }

    public ConfigurationItemsTree getContainerConfigurationItemsTree() {
        return containerConfigurationItemsTree;
    }

    public void setContainerConfigurationItemsTree(ConfigurationItemsTree containerConfigurationItemsTree) {
        this.containerConfigurationItemsTree = containerConfigurationItemsTree;
    }
}
