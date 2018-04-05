package com.viewfunction.vfbam.ui.component;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityManagementPanel;
import com.viewfunction.vfbam.ui.component.contentManagement.ContentManagementPanel;
import com.viewfunction.vfbam.ui.component.processManagement.ProcessManagementPanel;
import com.viewfunction.vfbam.ui.component.systemConfiguration.SystemConfigurationPanel;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ApplicationContent extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    public ApplicationContent(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setWidth("100%");
        setHeight("100%");
        setSizeFull();

        TabSheet applicationContentTabSheet = new TabSheet();
        applicationContentTabSheet.addStyleName("framed padded-tabbar");

        ActivityManagementPanel activityManagementPanel=new ActivityManagementPanel(currentUserClientInfo);
        TabSheet.Tab activityManagementTab = applicationContentTabSheet.addTab(activityManagementPanel, userI18NProperties.getProperty("Business_Component_ActivityManagement_Title"));
        activityManagementTab.setIcon(FontAwesome.ARCHIVE);
        activityManagementTab.setClosable(false);
        activityManagementTab.setEnabled(true);

        ProcessManagementPanel processManagementPanel=new ProcessManagementPanel(currentUserClientInfo);
        TabSheet.Tab activityManagementTab1 = applicationContentTabSheet.addTab(processManagementPanel, userI18NProperties.getProperty("Business_Component_ProcessManagement_Title"));
        activityManagementTab1.setIcon(FontAwesome.RETWEET);
        activityManagementTab1.setClosable(false);
        activityManagementTab1.setEnabled(true);

        ContentManagementPanel contentManagementPanel=new ContentManagementPanel(currentUserClientInfo);
        TabSheet.Tab activityManagementTab2 = applicationContentTabSheet.addTab(contentManagementPanel, userI18NProperties.getProperty("Business_Component_ContentManagement_Title"));
        activityManagementTab2.setIcon(FontAwesome.CUBES);
        activityManagementTab2.setClosable(false);
        activityManagementTab2.setEnabled(true);

        SystemConfigurationPanel systemConfigurationPanel=new SystemConfigurationPanel(currentUserClientInfo);
        TabSheet.Tab activityManagementTab3 = applicationContentTabSheet.addTab(systemConfigurationPanel, userI18NProperties.getProperty("Business_Component_SystemConfiguration_Title"));
        activityManagementTab3.setIcon(FontAwesome.COGS);
        activityManagementTab3.setClosable(false);
        activityManagementTab3.setEnabled(true);

        applicationContentTabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                 /*
                 System.out.println(event);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */
            }
        });
        this.addComponent(applicationContentTabSheet);
    }
}
