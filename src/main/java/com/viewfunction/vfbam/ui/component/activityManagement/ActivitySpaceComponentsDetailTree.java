package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.IndexedContainer;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

import com.vaadin.ui.HorizontalLayout;

import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeDragMode;
import com.vaadin.ui.VerticalLayout;

import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.activityEngine.security.Role;

import java.util.Properties;

public class ActivitySpaceComponentsDetailTree extends VerticalLayout{
    private static final String CAPTION_PROPERTY = "caption";
    private static final String COMPONENTTYPE_PROPERTY = "componentType";
    private static final String ICON_PROPERTY = "icon";
    private static final String COMPONENT_ROOTID_PARTICIPANT = "PARTICIPANT_ROOT";
    private static final String COMPONENT_ROOTID_ROLE = "ROLE_ROOT";
    private static final String COMPONENT_ROOTID_ACTIVITYDEFINITION = "ACTIVITYDEFINITION_ROOT";
    private static final String COMPONENT_ROOTID_ROSTER = "ROSTER_ROOT";
    private static final String COMPONENT_ROOTID_ROLEQUEUE = "ROLEQUEUE_ROOT";

    private Container activitySpaceDataContainer;
    private UserClientInfo currentUserClientInfo;
    private ActivityObjectBrowser parentActivityObjectBrowser;
    private String activitySpaceName;
    private Tree activitySpaceComponentsTree;
    private String currentSelectedItemId;
    private IndexedContainer container;

    public ActivitySpaceComponentsDetailTree(String activitySpaceName,UserClientInfo currentUserClientInfo,ActivityObjectBrowser parentActivityObjectBrowser) {
        this.activitySpaceName=activitySpaceName;
        this.parentActivityObjectBrowser=parentActivityObjectBrowser;
        this.currentUserClientInfo = currentUserClientInfo;
        this.setMargin(false);
        HorizontalLayout treeContainerLayout = new HorizontalLayout();
        treeContainerLayout.setSpacing(false);
        addComponent(treeContainerLayout);
        activitySpaceComponentsTree = new Tree();
        activitySpaceComponentsTree.setSelectable(true);
        activitySpaceComponentsTree.setMultiSelect(false);
        activitySpaceComponentsTree.setDragMode(TreeDragMode.NONE);
        activitySpaceDataContainer = generateActivitySpaceComponentsContainer();
        activitySpaceComponentsTree.setContainerDataSource(activitySpaceDataContainer);
        treeContainerLayout.addComponent(activitySpaceComponentsTree);
        activitySpaceComponentsTree.setItemCaptionPropertyId(CAPTION_PROPERTY);
        activitySpaceComponentsTree.setItemIconPropertyId(ICON_PROPERTY);
        activitySpaceComponentsTree.setImmediate(true);
        activitySpaceComponentsTree.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                //the second time click on the same tree node will get a null event.getProperty().getValue()
                //this means unselect current tree node
                if (event.getProperty().getValue() != null) {
                    handleTreeItemClickEvent(event.getProperty().getValue());
                }
            }
        });
    }

    public void cleanSelectedComponentItem(){
        activitySpaceComponentsTree.clear();
        this.currentSelectedItemId=null;
    }

    public void selectedComponent(String activitySpaceName,String componentType,String componentId){
        if(this.activitySpaceName.equals(activitySpaceName)){
            if(this.currentSelectedItemId!=null){
                Item currentSelectedItem=activitySpaceDataContainer.getItem(this.currentSelectedItemId);
                String currentSelectedComponentType=currentSelectedItem.getItemProperty(COMPONENTTYPE_PROPERTY).getValue().toString();
                String currentSelectedComponentId=currentSelectedItem.getItemProperty(CAPTION_PROPERTY).getValue().toString();
                if(componentId!=null){
                    if(componentId.equals(currentSelectedComponentId)&componentType.equals(currentSelectedComponentType)){
                        return;
                    }
                }else{
                    if(currentSelectedComponentId==null&componentType.equals(currentSelectedComponentType)){
                        return;
                    }
                }
            }
            String componentTypeElementId=null;
            if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_PARTICIPANT)){
                componentTypeElementId=COMPONENT_ROOTID_PARTICIPANT;
            }
            if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLE)){
                componentTypeElementId=COMPONENT_ROOTID_ROLE;
            }
            if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE)){
                componentTypeElementId=COMPONENT_ROOTID_ROLEQUEUE;
            }
            if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROSTER)){
                componentTypeElementId=COMPONENT_ROOTID_ROSTER;
            }
            if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION)){
                componentTypeElementId=COMPONENT_ROOTID_ACTIVITYDEFINITION;
            }
            if(componentId==null){
                activitySpaceComponentsTree.select(componentTypeElementId);
            }else{
                System.out.println(componentTypeElementId);
                Item targetComponentTypeItem= activitySpaceDataContainer.getItem(componentTypeElementId);
                System.out.println(targetComponentTypeItem);
                Object[] componentIdArray= ((Container.Hierarchical) activitySpaceDataContainer).getChildren(componentTypeElementId).toArray();
                for(Object itemIdObj:componentIdArray){
                    String itemIdStr=itemIdObj.toString();
                    Item targetComponentElementItem= activitySpaceDataContainer.getItem(itemIdStr);
                    String targetComponentId=targetComponentElementItem.getItemProperty(CAPTION_PROPERTY).getValue().toString();
                    if(componentId.equals(targetComponentId)){
                        activitySpaceComponentsTree.select(itemIdStr);
                        return;
                    }
                }
            }
        }
    }

    private void handleTreeItemClickEvent(Object itemId){
        parentActivityObjectBrowser.disSelectActivitySpaceComponentsDetailTree(activitySpaceName);
        String itemKey=String.valueOf(itemId);
        this.currentSelectedItemId=itemKey;
        Item clickedItem=activitySpaceDataContainer.getItem(itemId);
        String componentID=clickedItem.getItemProperty(CAPTION_PROPERTY).getValue().toString();
        String componentType=clickedItem.getItemProperty(COMPONENTTYPE_PROPERTY).getValue().toString();
        String eventComponentId;
        if(itemKey.equals(COMPONENT_ROOTID_PARTICIPANT)||itemKey.equals(COMPONENT_ROOTID_ROLE)||
                itemKey.equals(COMPONENT_ROOTID_ACTIVITYDEFINITION)||itemKey.equals(COMPONENT_ROOTID_ROSTER)||
                itemKey.equals(COMPONENT_ROOTID_ROLEQUEUE)){
            eventComponentId=null;
        }else{
            eventComponentId=componentID;
        }
        ActivitySpaceComponentSelectedEvent activitySpaceComponentSelectedEvent=
                new ActivitySpaceComponentSelectedEvent(this.activitySpaceName,componentType,eventComponentId);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentSelectedEvent);
    }

    private Container generateActivitySpaceComponentsContainer() {
        Properties userI18NProperties = this.currentUserClientInfo.getUserI18NProperties();
        container = new HierarchicalContainer();
        container.addContainerProperty(CAPTION_PROPERTY, String.class, null);
        container.addContainerProperty(ICON_PROPERTY, Resource.class, null);
        container.addContainerProperty(COMPONENTTYPE_PROPERTY, String.class, null);
        ActivitySpaceMetaInfoDAO activitySpaceMetaInfoDAO= ActivitySpaceOperationUtil.getActivitySpaceMetaInfo(this.activitySpaceName,null);

        //Render Participants Info
        Participant[] participants=activitySpaceMetaInfoDAO.getParticipants();
        int participantsNumber= participants!=null?participants.length:0;
        final Item participantsItem = container.addItem(COMPONENT_ROOTID_PARTICIPANT);
        String participantsCaption=userI18NProperties.
                getProperty("Business_Component_ActivityManagement_ComponentType_Participant")+"("+participantsNumber+")";
        participantsItem.getItemProperty(CAPTION_PROPERTY).setValue(participantsCaption);
        participantsItem.getItemProperty(COMPONENTTYPE_PROPERTY).setValue(ActivityManagementConst.COMPONENT_TYPE_PARTICIPANT);
        participantsItem.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.USER);
        if(participants!=null){
            for(Participant currentParticipant:participants){
                String id=COMPONENT_ROOTID_PARTICIPANT+"_"+currentParticipant.getParticipantName();
                Item childParticipantItem = container.addItem(id);
                childParticipantItem.getItemProperty(CAPTION_PROPERTY).setValue(currentParticipant.getParticipantName());
                childParticipantItem.getItemProperty(COMPONENTTYPE_PROPERTY).setValue(ActivityManagementConst.COMPONENT_TYPE_PARTICIPANT);
                childParticipantItem.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.ANGLE_DOUBLE_RIGHT);
                ((Container.Hierarchical) container).setParent(id, COMPONENT_ROOTID_PARTICIPANT);
                ((Container.Hierarchical) container).setChildrenAllowed(id, false);
            }
        }

        //Render Roles Info
        Role[] roles=activitySpaceMetaInfoDAO.getRoles();
        int rolesNumber= roles!=null?roles.length:0;
        final Item rolesItem = container.addItem(COMPONENT_ROOTID_ROLE);
        String rolesCaption=userI18NProperties.
                getProperty("Business_Component_ActivityManagement_ComponentType_Roles")+"("+rolesNumber+")";
        rolesItem.getItemProperty(CAPTION_PROPERTY).setValue(rolesCaption);
        rolesItem.getItemProperty(COMPONENTTYPE_PROPERTY).setValue(ActivityManagementConst.COMPONENT_TYPE_ROLE);
        rolesItem.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.USERS);
        if(roles!=null){
            for(Role currentRole:roles){
                String id=COMPONENT_ROOTID_ROLE+"_"+currentRole.getRoleName();
                Item childRoleItem = container.addItem(id);
                childRoleItem.getItemProperty(CAPTION_PROPERTY).setValue(currentRole.getRoleName());
                childRoleItem.getItemProperty(COMPONENTTYPE_PROPERTY).setValue(ActivityManagementConst.COMPONENT_TYPE_ROLE);
                childRoleItem.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.ANGLE_DOUBLE_RIGHT);
                ((Container.Hierarchical) container).setParent(id, COMPONENT_ROOTID_ROLE);
                ((Container.Hierarchical) container).setChildrenAllowed(id, false);
            }
        }

        //Render ActivityDefinitions Info
        BusinessActivityDefinition[] businessActivityDefinitions=activitySpaceMetaInfoDAO.getBusinessActivityDefinitions();
        int activityTypesNumber= businessActivityDefinitions!=null?businessActivityDefinitions.length:0;
        final Item activityDefinitionsItem = container.addItem(COMPONENT_ROOTID_ACTIVITYDEFINITION);
        String activityDefinitionsCaption=userI18NProperties.
                getProperty("Business_Component_ActivityManagement_ComponentType_ActivityDefinitions")+"("+activityTypesNumber+")";
        activityDefinitionsItem.getItemProperty(CAPTION_PROPERTY).setValue(activityDefinitionsCaption);
        activityDefinitionsItem.getItemProperty(COMPONENTTYPE_PROPERTY).setValue(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION);
        activityDefinitionsItem.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.SHARE_ALT_SQUARE);
        if(businessActivityDefinitions!=null){
            for(BusinessActivityDefinition currentBusinessActivityDefinition:businessActivityDefinitions){
                String id=COMPONENT_ROOTID_ACTIVITYDEFINITION+"_"+currentBusinessActivityDefinition.getActivityType();
                Item childDefinitionItem1 = container.addItem(id);
                childDefinitionItem1.getItemProperty(CAPTION_PROPERTY).setValue(currentBusinessActivityDefinition.getActivityType());
                childDefinitionItem1.getItemProperty(COMPONENTTYPE_PROPERTY).setValue(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION);
                childDefinitionItem1.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.ANGLE_DOUBLE_RIGHT);
                ((Container.Hierarchical) container).setParent(id, COMPONENT_ROOTID_ACTIVITYDEFINITION);
                ((Container.Hierarchical) container).setChildrenAllowed(id, false);
            }
        }

        //Render Rosters Info
        Roster[] rosters=activitySpaceMetaInfoDAO.getRosters();
        int rostersNumber= rosters!=null?rosters.length:0;
        final Item rostersItem = container.addItem(COMPONENT_ROOTID_ROSTER);
        String rostersCaption=userI18NProperties.
                getProperty("Business_Component_ActivityManagement_ComponentType_Rosters")+"("+rostersNumber+")";
        rostersItem.getItemProperty(CAPTION_PROPERTY).setValue(rostersCaption);
        rostersItem.getItemProperty(COMPONENTTYPE_PROPERTY).setValue(ActivityManagementConst.COMPONENT_TYPE_ROSTER);
        rostersItem.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.INBOX);
        if(rosters!=null){
            for(Roster currentRoster:rosters){
                String id=COMPONENT_ROOTID_ROSTER+"_"+currentRoster.getRosterName();
                Item childRosterItem1 = container.addItem(id);
                childRosterItem1.getItemProperty(CAPTION_PROPERTY).setValue(currentRoster.getRosterName());
                childRosterItem1.getItemProperty(COMPONENTTYPE_PROPERTY).setValue(ActivityManagementConst.COMPONENT_TYPE_ROSTER);
                childRosterItem1.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.ANGLE_DOUBLE_RIGHT);
                ((Container.Hierarchical) container).setParent(id, COMPONENT_ROOTID_ROSTER);
                ((Container.Hierarchical) container).setChildrenAllowed(id, false);
            }
        }

        //Render RoleQueues Info
        RoleQueue[] roleQueues=activitySpaceMetaInfoDAO.getRoleQueues();
        int roleQueuesNumber= roleQueues!=null?roleQueues.length:0;
        final Item roleQueuesItem = container.addItem(COMPONENT_ROOTID_ROLEQUEUE);
        String roleQueuesCaption=userI18NProperties.
                getProperty("Business_Component_ActivityManagement_ComponentType_RoleQueues")+"("+roleQueuesNumber+")";
        roleQueuesItem.getItemProperty(CAPTION_PROPERTY).setValue(roleQueuesCaption);
        roleQueuesItem.getItemProperty(COMPONENTTYPE_PROPERTY).setValue(ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE);
        roleQueuesItem.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.ALIGN_JUSTIFY);
        if(roleQueues!=null){
            for(RoleQueue currentRoleQueue:roleQueues){
                String id=COMPONENT_ROOTID_ROLEQUEUE+"_"+currentRoleQueue.getQueueName();
                Item childRoleQueueItem1 = container.addItem(id);
                childRoleQueueItem1.getItemProperty(CAPTION_PROPERTY).setValue(currentRoleQueue.getQueueName());
                childRoleQueueItem1.getItemProperty(COMPONENTTYPE_PROPERTY).setValue(ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE);
                childRoleQueueItem1.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.ANGLE_DOUBLE_RIGHT);
                ((Container.Hierarchical) container).setParent(id, COMPONENT_ROOTID_ROLEQUEUE);
                ((Container.Hierarchical) container).setChildrenAllowed(id, false);
            }
        }
        return container;
    }

    public void modifyComponentElement(String componentType,String componentID,String modifyType){
        String componentTypeElementId=null;
        if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_PARTICIPANT)){
            componentTypeElementId=COMPONENT_ROOTID_PARTICIPANT;
        }
        if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLE)){
            componentTypeElementId=COMPONENT_ROOTID_ROLE;
        }
        if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE)){
            componentTypeElementId=COMPONENT_ROOTID_ROLEQUEUE;
        }
        if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROSTER)){
            componentTypeElementId=COMPONENT_ROOTID_ROSTER;
        }
        if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION)){
            componentTypeElementId=COMPONENT_ROOTID_ACTIVITYDEFINITION;
        }
        if(modifyType.equals(ActivitySpaceComponentModifyEvent.MODIFYTYPE_ADD)){
            String newElementId=componentTypeElementId+"_"+componentID;
            Item itemToADD = activitySpaceDataContainer.addItem(newElementId);
            itemToADD.getItemProperty(CAPTION_PROPERTY).setValue(componentID);
            itemToADD.getItemProperty(COMPONENTTYPE_PROPERTY).setValue(componentType);
            itemToADD.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.ANGLE_DOUBLE_RIGHT);
            ((Container.Hierarchical) activitySpaceDataContainer).setParent(newElementId, componentTypeElementId);
            ((Container.Hierarchical) activitySpaceDataContainer).setChildrenAllowed(newElementId, false);
        }
        if(modifyType.equals(ActivitySpaceComponentModifyEvent.MODIFYTYPE_REMOVE)){
            String removeElementId=componentTypeElementId+"_"+componentID;
            Item itemToRemove = activitySpaceDataContainer.getItem(removeElementId);
            if(itemToRemove!=null){
                activitySpaceDataContainer.removeItem(removeElementId);
            }
        }
        if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_PARTICIPANT)){
            Item componentContainerItem = container.getItem(COMPONENT_ROOTID_PARTICIPANT);
            long subElementsNumber=((Container.Hierarchical) container).getChildren(COMPONENT_ROOTID_PARTICIPANT).size();
            String participantsCaption=this.currentUserClientInfo.getUserI18NProperties().
                    getProperty("Business_Component_ActivityManagement_ComponentType_Participant")+"("+subElementsNumber+")";
            componentContainerItem.getItemProperty(CAPTION_PROPERTY).setValue(participantsCaption);
        }
        if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLE)){
            Item componentContainerItem = container.getItem(COMPONENT_ROOTID_ROLE);
            long subElementsNumber=((Container.Hierarchical) container).getChildren(COMPONENT_ROOTID_ROLE).size();
            String participantsCaption=this.currentUserClientInfo.getUserI18NProperties().
                    getProperty("Business_Component_ActivityManagement_ComponentType_Roles")+"("+subElementsNumber+")";
            componentContainerItem.getItemProperty(CAPTION_PROPERTY).setValue(participantsCaption);
        }
        if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE)){
            Item componentContainerItem = container.getItem(COMPONENT_ROOTID_ROLEQUEUE);
            long subElementsNumber=((Container.Hierarchical) container).getChildren(COMPONENT_ROOTID_ROLEQUEUE).size();
            String participantsCaption=this.currentUserClientInfo.getUserI18NProperties().
                    getProperty("Business_Component_ActivityManagement_ComponentType_RoleQueues")+"("+subElementsNumber+")";
            componentContainerItem.getItemProperty(CAPTION_PROPERTY).setValue(participantsCaption);
        }
        if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROSTER)){
            Item componentContainerItem = container.getItem(COMPONENT_ROOTID_ROSTER);
            long subElementsNumber=((Container.Hierarchical) container).getChildren(COMPONENT_ROOTID_ROSTER).size();
            String participantsCaption=this.currentUserClientInfo.getUserI18NProperties().
                    getProperty("Business_Component_ActivityManagement_ComponentType_Rosters")+"("+subElementsNumber+")";
            componentContainerItem.getItemProperty(CAPTION_PROPERTY).setValue(participantsCaption);
        }
        if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION)){
            Item componentContainerItem = container.getItem(COMPONENT_ROOTID_ACTIVITYDEFINITION);
            long subElementsNumber=((Container.Hierarchical) container).getChildren(COMPONENT_ROOTID_ACTIVITYDEFINITION).size();
            String participantsCaption=this.currentUserClientInfo.getUserI18NProperties().
                    getProperty("Business_Component_ActivityManagement_ComponentType_ActivityDefinitions")+"("+subElementsNumber+")";
            componentContainerItem.getItemProperty(CAPTION_PROPERTY).setValue(participantsCaption);
        }
    }
}
