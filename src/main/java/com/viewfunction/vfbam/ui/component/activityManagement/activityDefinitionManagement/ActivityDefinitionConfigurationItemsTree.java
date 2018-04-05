package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.viewfunction.activityEngine.activityView.common.CustomStructure;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.ConfigurationItemsTree;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.List;

/**
 * Created by wangychu on 2/2/17.
 */
public class ActivityDefinitionConfigurationItemsTree extends ConfigurationItemsTree {
    private String configurationItemName;
    private String configurationItemType;
    private UserClientInfo currentUserClientInfo;

    public ActivityDefinitionConfigurationItemsTree(UserClientInfo currentUserClientInfo,String configurationItemType,String configurationItemName){
        super(currentUserClientInfo);
        this.currentUserClientInfo = currentUserClientInfo;
        this.configurationItemType=configurationItemType;
        this.configurationItemName=configurationItemName;
    }

    protected Container generateConfigurationItemsContainer() {
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();

        container = new HierarchicalContainer();
        container.addContainerProperty(CAPTION_PROPERTY, String.class, null);
        container.addContainerProperty(ICON_PROPERTY, Resource.class, null);
        container.addContainerProperty(CUSTOMSTRUCTURE_PROPERTY, CustomStructure.class, null);

        if(ActivityAdditionalConfigurationEditor.ConfigurationItemType_StepConfig.equals(configurationItemType)){
            CustomStructure stepRootConfigurationItem= ActivitySpaceOperationUtil.
                    getActivityStepRootCustomConfigItem(activitySpaceName,activityDefinitionType,this.configurationItemName);

            final Item configurationItemItem = container.addItem(ROOT_CONFIGURATION_ITEM);
            configurationItemItem.getItemProperty(CAPTION_PROPERTY).setValue(this.configurationItemName);
            configurationItemItem.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.SQUARE_O);
            configurationItemItem.getItemProperty(CUSTOMSTRUCTURE_PROPERTY).setValue(stepRootConfigurationItem);

            List<CustomStructure> stepCustomConfigItems= ActivitySpaceOperationUtil.
                    getActivityStepCustomConfigItemsList(activitySpaceName,activityDefinitionType,this.configurationItemName);

            loadSubTreeItemsData(stepCustomConfigItems);
        }
        if(ActivityAdditionalConfigurationEditor.ConfigurationItemType_GlobalConfig.equals(configurationItemType)){
            CustomStructure activityTypeGlobalConfigRootConfigurationItem= ActivitySpaceOperationUtil.
                    getActivityDefinitionRootCustomConfigItem(activitySpaceName,activityDefinitionType);
            if(activityTypeGlobalConfigRootConfigurationItem!=null){
                CustomStructure targetCustomStructure=ActivitySpaceOperationUtil.getSubCustomStructure(activityTypeGlobalConfigRootConfigurationItem,this.configurationItemName);
                if(targetCustomStructure!=null){
                    final Item configurationItemItem = container.addItem(ROOT_CONFIGURATION_ITEM);
                    configurationItemItem.getItemProperty(CAPTION_PROPERTY).setValue(this.configurationItemName);
                    configurationItemItem.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.SQUARE_O);
                    configurationItemItem.getItemProperty(CUSTOMSTRUCTURE_PROPERTY).setValue(targetCustomStructure);
                    List<CustomStructure> activityTypeGlobalConfigurationSubCustomConfigItems= ActivitySpaceOperationUtil.getSubCustomConfigItemsList(targetCustomStructure);
                    if(activityTypeGlobalConfigurationSubCustomConfigItems!=null){
                        loadSubTreeItemsData(activityTypeGlobalConfigurationSubCustomConfigItems);
                    }
                }
            }
        }
        return container;
    }

    private void loadSubTreeItemsData(List<CustomStructure> customStructureList){
        for(CustomStructure currentCustomStructure:customStructureList){
            Item subConfigurationItemItem = container.addItem(ROOT_CONFIGURATION_ITEM +"_"+currentCustomStructure.getStructureName());
            subConfigurationItemItem.getItemProperty(CAPTION_PROPERTY).setValue(currentCustomStructure.getStructureName());
            subConfigurationItemItem.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.SQUARE_O);
            subConfigurationItemItem.getItemProperty(CUSTOMSTRUCTURE_PROPERTY).setValue(currentCustomStructure);
            ((Container.Hierarchical) container).setParent(ROOT_CONFIGURATION_ITEM +"_"+currentCustomStructure.getStructureName(), ROOT_CONFIGURATION_ITEM);
            ((Container.Hierarchical) container).setChildrenAllowed(ROOT_CONFIGURATION_ITEM +"_"+currentCustomStructure.getStructureName(), true);
        }
    }
}
