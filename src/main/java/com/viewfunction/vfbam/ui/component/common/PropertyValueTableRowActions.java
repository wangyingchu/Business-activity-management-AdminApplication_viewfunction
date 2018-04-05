package com.viewfunction.vfbam.ui.component.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class PropertyValueTableRowActions extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private PropertyValuesActionTable containerPropertyValuesActionTable;
    private String componentId;
    private Button editDataFieldsButton;
    private Button deleteDataFieldButton;
    public PropertyValueTableRowActions(UserClientInfo currentUserClientInfo,String componentId,boolean allowEditOperation,boolean allowRemoveOperation){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.componentId=componentId;
        editDataFieldsButton = new Button();
        editDataFieldsButton.setIcon(FontAwesome.EDIT);
        editDataFieldsButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_Common_EditPropertyValueText"));
        editDataFieldsButton.addStyleName("small");
        editDataFieldsButton.addStyleName("borderless");
        addComponent(editDataFieldsButton);
        final String propertyValue=this.componentId;
        editDataFieldsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if(containerPropertyValuesActionTable!=null){
                    containerPropertyValuesActionTable.updatePropertyValue(propertyValue);
                }
            }
        });
        editDataFieldsButton.setEnabled(allowEditOperation);

        deleteDataFieldButton = new Button();
        deleteDataFieldButton.setIcon(FontAwesome.TRASH_O);
        deleteDataFieldButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_Common_DeletePropertyValueText"));
        deleteDataFieldButton.addStyleName("small");
        deleteDataFieldButton.addStyleName("borderless");
        addComponent(deleteDataFieldButton);
        deleteDataFieldButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if (containerPropertyValuesActionTable != null) {
                    containerPropertyValuesActionTable.deletePropertyValue(propertyValue);
                }
            }
        });
        deleteDataFieldButton.setEnabled(allowRemoveOperation);
    }

    public void setContainerPropertyValuesActionTable(PropertyValuesActionTable containerPropertyValuesActionTable) {
        this.containerPropertyValuesActionTable = containerPropertyValuesActionTable;
    }

    public void enableEditAction(boolean allowEditOperation){
        editDataFieldsButton.setEnabled(allowEditOperation);
    }

    public void enableDeleteAction(boolean allowRemoveOperation){
        deleteDataFieldButton.setEnabled(allowRemoveOperation);
    }
}
