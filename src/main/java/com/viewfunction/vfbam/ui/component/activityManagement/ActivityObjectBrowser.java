package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.activitySpaceManagement.ActivitySpacesConfigurationPanel;
import com.viewfunction.vfbam.ui.component.activityManagement.activitySpaceManagement.AddNewActivitySpacePanel;
import com.viewfunction.vfbam.ui.util.UserClientInfo;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentSelectedEvent.ActivitySpaceComponentSelectedListener;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentModifyEvent.ActivitySpaceComponentModifiedListener;
import com.viewfunction.vfbam.ui.component.activityManagement.NewActivitySpaceCreatedEvent.NewActivitySpaceCreatedListener;
import java.util.*;

public class ActivityObjectBrowser extends VerticalLayout  implements ActivitySpaceComponentSelectedListener,ActivitySpaceComponentModifiedListener,NewActivitySpaceCreatedListener {
    private UserClientInfo currentUserClientInfo;
    private Map<String,ActivitySpaceComponentsDetailTree> existActivitySpaceComponentsTreeMap=new HashMap<String,ActivitySpaceComponentsDetailTree>();
    private MenuBar.MenuItem activitySpaceMetaInfoManageMenuItem;
    private MenuBar.MenuItem createActivitySpaceMenuItem;
    private Accordion activitySpacesAccordion;

    public ActivityObjectBrowser(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        this.currentUserClientInfo.getEventBlackBoard().addListener(this);
        Properties userI18NProperties = this.currentUserClientInfo.getUserI18NProperties();
        MenuBar operationMenuBar = getOperationMenuBar(userI18NProperties);
        operationMenuBar.addStyleName("borderless");
        operationMenuBar.addStyleName("small");
        addComponent(operationMenuBar);
        activitySpacesAccordion = getActivitySpacesAccordion(userI18NProperties);
        activitySpacesAccordion.addStyleName("borderless");
        addComponent(activitySpacesAccordion);
    }

    private MenuBar getOperationMenuBar(Properties userI18NProperties) {
        MenuBar.Command click = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                executeOperationMenuItems(selectedItem.getId());
            }
        };
        MenuBar menubar = new MenuBar();
        menubar.setWidth("100%");
        MenuBar.MenuItem operationsPrompt = menubar.addItem(userI18NProperties.
                getProperty("Business_Component_ActivityManagement_OperationMenuBarText"), null);
        operationsPrompt.setIcon(FontAwesome.LIST);

        createActivitySpaceMenuItem = operationsPrompt.addItem(userI18NProperties.
                getProperty("Business_Component_ActivityManagement_CreateActivitySpaceMenuItemText"), click);
        createActivitySpaceMenuItem.setIcon(FontAwesome.PLUS_SQUARE);

        activitySpaceMetaInfoManageMenuItem = operationsPrompt.addItem(userI18NProperties.
                getProperty("Business_Component_ActivityManagement_ActivitySpaceConfigurationMenuItemText"), click);
        activitySpaceMetaInfoManageMenuItem.setIcon(FontAwesome.COGS);

        return menubar;
    }

    private void executeOperationMenuItems(int itemId){
        if(itemId==createActivitySpaceMenuItem.getId()){
            AddNewActivitySpacePanel addNewActivitySpacePanel=new AddNewActivitySpacePanel(this.currentUserClientInfo);
            final Window window = new Window();
            window.setWidth(600.0f, Unit.PIXELS);
            window.setHeight(210.0f, Unit.PIXELS);
            window.setResizable(false);
            window.center();
            window.setModal(true);
            window.setContent(addNewActivitySpacePanel);
            addNewActivitySpacePanel.setContainerDialog(window);
            UI.getCurrent().addWindow(window);
        }
        if(itemId==activitySpaceMetaInfoManageMenuItem.getId()){
            ActivitySpacesConfigurationPanel activitySpacesConfigurationPanel=new ActivitySpacesConfigurationPanel(this.currentUserClientInfo);
            final Window window = new Window();
            window.setWidth(900.0f, Unit.PIXELS);
            window.setHeight(600.0f, Unit.PIXELS);
            window.setResizable(false);
            window.center();
            window.setModal(true);
            window.setContent(activitySpacesConfigurationPanel);
            activitySpacesConfigurationPanel.setContainerDialog(window);
            UI.getCurrent().addWindow(window);
        }
    }

    public void disSelectActivitySpaceComponentsDetailTree(String currentActiveActivitySpace){
        Set<String> treeMapKeySet=existActivitySpaceComponentsTreeMap.keySet();
        Iterator<String> keyIter=treeMapKeySet.iterator();
        while(keyIter.hasNext()){
            String currentActivitySpaceName=keyIter.next();
            if(!currentActivitySpaceName.equals(currentActiveActivitySpace)){
                existActivitySpaceComponentsTreeMap.get(currentActivitySpaceName).cleanSelectedComponentItem();
            }
        }
    }

    private Accordion getActivitySpacesAccordion(Properties userI18NProperties) {
        String[] activitySpaceNamesArray=ActivitySpaceOperationUtil.listActivitySpaces();
        Accordion activitySpacesAccordion = new Accordion();
        if(activitySpaceNamesArray!=null){
            for(String activitySpaceName:activitySpaceNamesArray){
                ActivitySpaceComponentsDetailTree activitySpaceComponentsDetailTree = new ActivitySpaceComponentsDetailTree(activitySpaceName,currentUserClientInfo,this);
                activitySpacesAccordion.addTab(activitySpaceComponentsDetailTree, activitySpaceName, FontAwesome.DATABASE);
                existActivitySpaceComponentsTreeMap.put(activitySpaceName,activitySpaceComponentsDetailTree);
            }
        }else{}

        activitySpacesAccordion.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {}
        });
        return activitySpacesAccordion;
    }

    @Override
    public void receivedActivitySpaceComponentSelectedEvent(ActivitySpaceComponentSelectedEvent event) {
        String componentType=event.getComponentType();
        String componentId=event.getComponentId();
        String activitySpaceName=event.getActivitySpaceName();
        //select corresponds element in browser tree
        ActivitySpaceComponentsDetailTree targetElementsTree=existActivitySpaceComponentsTreeMap.get(activitySpaceName);
        targetElementsTree.selectedComponent(activitySpaceName,componentType,componentId);
    }

    @Override
    public void receivedActivitySpaceComponentModifiedEvent(ActivitySpaceComponentModifyEvent event){
        String componentType=event.getComponentType();
        String componentId=event.getComponentId();
        String activitySpaceName=event.getActivitySpaceName();
        String modifyType=event.getModifyType();
        //modify component element in browser tree
        ActivitySpaceComponentsDetailTree targetElementsTree=existActivitySpaceComponentsTreeMap.get(activitySpaceName);
        targetElementsTree.modifyComponentElement(componentType,componentId,modifyType);
    }

    @Override
    public void receivedNewActivitySpaceCreatedEvent(NewActivitySpaceCreatedEvent event) {
        String activitySpaceName=event.getActivitySpaceName();
        if(activitySpaceName!=null) {
            ActivitySpaceComponentsDetailTree activitySpaceComponentsDetailTree = new ActivitySpaceComponentsDetailTree(activitySpaceName, currentUserClientInfo, this);
            activitySpacesAccordion.addTab(activitySpaceComponentsDetailTree, activitySpaceName, FontAwesome.DATABASE);
            existActivitySpaceComponentsTreeMap.put(activitySpaceName, activitySpaceComponentsDetailTree);
        }
    }
}