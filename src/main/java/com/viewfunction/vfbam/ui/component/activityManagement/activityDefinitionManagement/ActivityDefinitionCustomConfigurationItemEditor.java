package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.activityEngine.util.ActivitySpaceCommonConstant;
import com.viewfunction.vfbam.ui.component.activityManagement.CustomConfigurationItemDataEditor;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

/**
 * Created by wangychu on 1/25/17.
 */
public class ActivityDefinitionCustomConfigurationItemEditor extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;

    private String configurationItemName;
    private String configurationItemType;
    private MainSectionTitle customConfigurationItemEditorSectionTitle;
    private SectionActionsBar editorActionsBar;

    public ActivityDefinitionCustomConfigurationItemEditor(UserClientInfo currentUserClientInfo,String configurationItemType,String configurationItemName,int editorHeight){
        setSpacing(true);
        setMargin(true);
        this.currentUserClientInfo=currentUserClientInfo;
        this.setConfigurationItemType(configurationItemType);
        this.setConfigurationItemName(configurationItemName);
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        customConfigurationItemEditorSectionTitle=new MainSectionTitle("-");
        addComponent(customConfigurationItemEditorSectionTitle);
        editorActionsBar=new SectionActionsBar(new Label("-"+" : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(editorActionsBar);
        this.setHeight(editorHeight,Unit.PIXELS);

        HorizontalSplitPanel configurationItemInfoSplitPanel = new HorizontalSplitPanel();
        configurationItemInfoSplitPanel.setSizeFull();
        configurationItemInfoSplitPanel.setSplitPosition(300, Unit.PIXELS);

        ActivityDefinitionConfigurationItemsTree activityDefinitionConfigItemsTree=new ActivityDefinitionConfigurationItemsTree(this.currentUserClientInfo,getConfigurationItemType(),getConfigurationItemName());
        configurationItemInfoSplitPanel.setFirstComponent(activityDefinitionConfigItemsTree);

        CustomConfigurationItemDataEditor customConfigurationItemDataEditor=new CustomConfigurationItemDataEditor(this.currentUserClientInfo);
        configurationItemInfoSplitPanel.setSecondComponent(customConfigurationItemDataEditor);
        activityDefinitionConfigItemsTree.setRelatedCustomConfigurationItemDataEditor(customConfigurationItemDataEditor);

        addComponent(configurationItemInfoSplitPanel);
        setExpandRatio(configurationItemInfoSplitPanel, 1.0F);
    }

    @Override
    public void attach() {
        super.attach();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        Label sectionActionBarLabel=null;
        if(ActivityAdditionalConfigurationEditor.ConfigurationItemType_StepConfig.equals(getConfigurationItemType())){
            String configurationItemName=getConfigurationItemName();
            if(configurationItemName.equals(ActivitySpaceCommonConstant.ActivityDefinition_launchPointLogicStepId)){
                configurationItemName=userI18NProperties.
                        getProperty("ActivityManagement_ActivityTypeManagement_LaunchPointText");
            }
            customConfigurationItemEditorSectionTitle.setValue(userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_StepsConfigurationItemText"));
            sectionActionBarLabel=new Label(userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeText")+" : "+activityDefinitionType+" &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]<br/>"+
                    userI18NProperties.
                        getProperty("ActivityManagement_ActivityTypeManagement_StepText")+" : <b>"+configurationItemName+"</b> " , ContentMode.HTML);
        }
        if(ActivityAdditionalConfigurationEditor.ConfigurationItemType_GlobalConfig.equals(getConfigurationItemType())){
            customConfigurationItemEditorSectionTitle.setValue(userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_GlobalConfigurationItemText"));
            sectionActionBarLabel=new Label(userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeText")+" : "+activityDefinitionType+" &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]<br/>"+
                    userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_ConfigItemNameText")+" : <b>"+getConfigurationItemName()+"</b> " , ContentMode.HTML);
        }
        editorActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
    }

    public String getConfigurationItemName() {
        return configurationItemName;
    }

    private void setConfigurationItemName(String configurationItemName) {
        this.configurationItemName = configurationItemName;
    }

    public String getConfigurationItemType() {
        return configurationItemType;
    }

    private void setConfigurationItemType(String configurationItemType) {
        this.configurationItemType = configurationItemType;
    }
}
