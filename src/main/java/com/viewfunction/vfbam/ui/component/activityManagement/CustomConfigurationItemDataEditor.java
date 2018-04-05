package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import com.vaadin.ui.themes.ValoTheme;
import com.viewfunction.activityEngine.activityView.common.CustomAttribute;
import com.viewfunction.activityEngine.activityView.common.CustomStructure;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.List;
import java.util.Properties;

/**
 * Created by wangychu on 2/1/17.
 */
public class CustomConfigurationItemDataEditor extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private VerticalLayout dataEditorContainerLayout;
    private CustomStructure configurationItemCustomStructure;
    private CustomAttributeItemsActionList customAttributeItemsList;

    public CustomConfigurationItemDataEditor(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setMargin(true);
        this.setMargin(new MarginInfo(false,true,true,true));
        Properties userI18NProperties = this.currentUserClientInfo.getUserI18NProperties();

        Label sectionLabel=new Label(userI18NProperties.
                getProperty("ActivityManagement_Common_ConfigurationItemDataText"));
        sectionLabel.addStyleName(ValoTheme.LABEL_BOLD);
        sectionLabel.addStyleName("colored");
        sectionLabel.addStyleName("ui_appSectionDiv");
        sectionLabel.addStyleName("ui_appFadeMargin");
        addComponent(sectionLabel);

        dataEditorContainerLayout=new VerticalLayout();
        SectionActionsBar itemDataPropertiesSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.LIST.getHtml() + " "+
                userI18NProperties.
                        getProperty("ActivityManagement_Common_ConfigurationItemPropertiesText")+":", ContentMode.HTML));
        dataEditorContainerLayout.addComponent(itemDataPropertiesSectionActionsBar);
        SectionActionButton addNewPropertiesButton = new SectionActionButton();
        addNewPropertiesButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_Common_AddConfigurationItemPropertyButtonLabel"));
        addNewPropertiesButton.setIcon(FontAwesome.PLUS_SQUARE);

        CustomConfigurationItemDataEditor customConfigurationItemDataEditor=this;
        addNewPropertiesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                AddNewConfigurationItemPropertyPanel addNewConfigurationItemPropertyPanel=new AddNewConfigurationItemPropertyPanel(currentUserClientInfo);
                addNewConfigurationItemPropertyPanel.setContainerCustomConfigurationItemDataEditor(customConfigurationItemDataEditor);
                addNewConfigurationItemPropertyPanel.setConfigurationItemCustomStructure(configurationItemCustomStructure);
                final Window window = new Window();
                window.setWidth(600.0f, Unit.PIXELS);
                window.setHeight(700.0f, Unit.PIXELS);
                window.setModal(true);
                window.setResizable(false);
                window.center();
                window.setContent(addNewConfigurationItemPropertyPanel);
                addNewConfigurationItemPropertyPanel.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });
        itemDataPropertiesSectionActionsBar.addActionComponent(addNewPropertiesButton);

        customAttributeItemsList=new CustomAttributeItemsActionList(this.currentUserClientInfo);
        dataEditorContainerLayout.addComponent(customAttributeItemsList);
    }

    public void renderConfigurationItemData(CustomStructure targetCustomStructure){
        this.configurationItemCustomStructure=targetCustomStructure;
        this.addComponent(dataEditorContainerLayout);
        List<CustomAttribute> customAttributeList=ActivitySpaceOperationUtil.getCustomStructureAttributes(targetCustomStructure);
        customAttributeItemsList.setCustomStructure(targetCustomStructure);
        customAttributeItemsList.renderCustomAttributeItems(customAttributeList);
    }

    public void clearConfigurationItemData(){
        this.configurationItemCustomStructure=null;
        this.removeComponent(dataEditorContainerLayout);
    }

    public void addNewCustomAttributeItem(String propertyName,Object propertyValue){
        customAttributeItemsList.addNewCustomAttributeItem(propertyName,propertyValue);
    }
}
