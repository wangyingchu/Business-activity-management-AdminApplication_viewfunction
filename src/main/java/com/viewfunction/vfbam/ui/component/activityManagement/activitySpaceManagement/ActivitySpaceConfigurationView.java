package com.viewfunction.vfbam.ui.component.activityManagement.activitySpaceManagement;

import com.vaadin.server.FontAwesome;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ActivitySpaceConfigurationView extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;

    private String activitySpaceName;
    public ActivitySpaceConfigurationView(UserClientInfo currentUserClientInfo,String activitySpaceName){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.activitySpaceName=activitySpaceName;
        setSpacing(true);
        setMargin(true);
        setWidth("100%");
        setHeight("430px");

        TabSheet tabs=new TabSheet();
        addComponent(tabs);
        ActivitySpaceBusinessCategoryEditor activitySpaceBusinessCategoryEditor=new ActivitySpaceBusinessCategoryEditor(this.currentUserClientInfo,this.activitySpaceName);
        TabSheet.Tab businessCategoriesInfoLayoutTab =tabs.addTab(activitySpaceBusinessCategoryEditor, userI18NProperties.
                getProperty("ActivitySpaceManagement_SpaceConfiguration_BusinessCatgsText"));
        businessCategoriesInfoLayoutTab.setIcon(FontAwesome.CHECK_CIRCLE_O);

        ActivitySpaceExtendFeatureCategoryEditor activitySpaceExtendFeatureCategoryEditor=new ActivitySpaceExtendFeatureCategoryEditor(this.currentUserClientInfo,this.activitySpaceName);
        TabSheet.Tab extendFeatureCategoriesInfoLayoutTab =tabs.addTab(activitySpaceExtendFeatureCategoryEditor, userI18NProperties.
                getProperty("ActivitySpaceManagement_SpaceConfiguration_ExtendCatgsText"));
        extendFeatureCategoriesInfoLayoutTab.setIcon(FontAwesome.LAPTOP);
        /*
        ActivitySpaceBusinessCategoryEditor knowledgeBaseIntegrationEditor=new ActivitySpaceBusinessCategoryEditor(this.currentUserClientInfo,this.activitySpaceName);
        TabSheet.Tab knowledgeBaseEntegationInfoLayoutTab =tabs.addTab(knowledgeBaseIntegrationEditor, "KnowledgeBase Integration");
        knowledgeBaseEntegationInfoLayoutTab.setIcon(FontAwesome.BOOK);
        */
    }
}
