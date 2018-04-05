package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.Action;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import com.viewfunction.activityEngine.activityView.common.CustomStructure;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by wangychu on 1/28/17.
 */
public abstract class ConfigurationItemsTree extends VerticalLayout implements Action.Handler{

    private Container configurationItemsDataContainer;
    private UserClientInfo currentUserClientInfo;
    private Tree configurationItemsTree;
    protected IndexedContainer container;
    protected static final String CAPTION_PROPERTY = "caption";
    protected static final String ICON_PROPERTY = "icon";
    protected static final String CUSTOMSTRUCTURE_PROPERTY = "CustomStructure";
    protected static final String ROOT_CONFIGURATION_ITEM ="ROOT_CONFIGURATION_ITEM";
    private Action[] contextActions;
    private Window addSubConfigurationItemWindow;
    private CustomStructure currentContextSelectedCustomStructure;
    private Object currentContextSelectedCustomStructureItemId;
    private CustomConfigurationItemDataEditor relatedCustomConfigurationItemDataEditor;

    public ConfigurationItemsTree(UserClientInfo currentUserClientInfo){
        this.setMargin(false);
        this.currentUserClientInfo = currentUserClientInfo;
        Properties userI18NProperties = this.currentUserClientInfo.getUserI18NProperties();

        HorizontalLayout labelContainerLayout=new HorizontalLayout();
        addComponent(labelContainerLayout);
        HorizontalLayout titleSpacingLayout1=new HorizontalLayout();
        titleSpacingLayout1.setWidth(15,Unit.PIXELS);
        labelContainerLayout.addComponent(titleSpacingLayout1);

        Label treeTitleLabel=new Label(VaadinIcons.FORM.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_Common_ConfigurationItemsText")+":", ContentMode.HTML);
        treeTitleLabel.addStyleName(ValoTheme.LABEL_COLORED);
        treeTitleLabel.addStyleName(ValoTheme.LABEL_SMALL);
        labelContainerLayout.addComponent(treeTitleLabel);

        HorizontalLayout treeContainerLayout = new HorizontalLayout();
        treeContainerLayout.setSpacing(false);
        addComponent(treeContainerLayout);

        configurationItemsTree = new Tree();
        configurationItemsTree.setSelectable(true);
        configurationItemsTree.setMultiSelect(false);
        configurationItemsTree.setDragMode(Tree.TreeDragMode.NONE);

        configurationItemsTree.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                //the second time click on the same tree node will get a null event.getProperty().getValue()
                //this means unselect current tree node
                configurationItemSelected(event.getProperty().getValue());
            }
        });

        configurationItemsTree.addActionHandler(this);

        configurationItemsTree.addExpandListener(new Tree.ExpandListener() {
            @Override
            public void nodeExpand(Tree.ExpandEvent event) {
                retrieveChildCustomStructures(event.getItemId());
            }
        });
        treeContainerLayout.addComponent(configurationItemsTree);
    }

    protected abstract Container generateConfigurationItemsContainer();

    private void retrieveChildCustomStructures(Object itemId){
        if(!((Container.Hierarchical) container).hasChildren(itemId)){
            //if not expended(has no children), try to retrieve the children custom structures
            Item configurationItemItem = container.getItem(itemId);
            CustomStructure structureToExpend=(CustomStructure)configurationItemItem.getItemProperty(CUSTOMSTRUCTURE_PROPERTY).getValue();
            List<CustomStructure> subCustomConfigItems= ActivitySpaceOperationUtil.getSubCustomConfigItemsList(structureToExpend);

            for(CustomStructure currentCustomStructure:subCustomConfigItems){
                Item subConfigurationItemItem = container.addItem(itemId+"_"+currentCustomStructure.getStructureName());
                subConfigurationItemItem.getItemProperty(CAPTION_PROPERTY).setValue(currentCustomStructure.getStructureName());
                subConfigurationItemItem.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.SQUARE_O);
                subConfigurationItemItem.getItemProperty(CUSTOMSTRUCTURE_PROPERTY).setValue(currentCustomStructure);
                ((Container.Hierarchical) container).setParent(itemId+"_"+currentCustomStructure.getStructureName(), itemId);
                ((Container.Hierarchical) container).setChildrenAllowed(itemId+"_"+currentCustomStructure.getStructureName(), true);
            }
        }
    }

    @Override
    public void attach() {
        super.attach();
        configurationItemsDataContainer = generateConfigurationItemsContainer();
        configurationItemsTree.setContainerDataSource(configurationItemsDataContainer);
        configurationItemsTree.setItemCaptionPropertyId(CAPTION_PROPERTY);
        configurationItemsTree.setItemIconPropertyId(ICON_PROPERTY);
        configurationItemsTree.setImmediate(true);
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        Properties userI18NProperties = this.currentUserClientInfo.getUserI18NProperties();
        if(contextActions==null){
            Action addSubConfigurationItem = new Action(userI18NProperties.
                    getProperty("ActivityManagement_Common_AddSubConfigurationItemText"));
            addSubConfigurationItem.setIcon(FontAwesome.PLUS);
            Action deleteCurrentConfigurationItem = new Action(userI18NProperties.
                    getProperty("ActivityManagement_Common_DeleteConfigurationItemText"));
            deleteCurrentConfigurationItem.setIcon(FontAwesome.TRASH_O);
            contextActions=new Action[] { addSubConfigurationItem,deleteCurrentConfigurationItem};
        }
        return contextActions;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        Properties userI18NProperties = this.currentUserClientInfo.getUserI18NProperties();
        Item configurationItemItem = null;
        if(target!=null){
            configurationItemItem = container.getItem(target);
            if(configurationItemItem!=null){
                currentContextSelectedCustomStructure=(CustomStructure)configurationItemItem.getItemProperty(CUSTOMSTRUCTURE_PROPERTY).getValue();
                currentContextSelectedCustomStructureItemId=target;
            }
        }
        String currentActionCaption=action.getCaption();
        if(currentActionCaption.equals(userI18NProperties.
                getProperty("ActivityManagement_Common_AddSubConfigurationItemText"))){
            CustomConfigurationItemValueEditor customConfigurationItemValueEditor
                    =new CustomConfigurationItemValueEditor(this.currentUserClientInfo,configurationItemItem.getItemProperty(CAPTION_PROPERTY).getValue().toString());
            customConfigurationItemValueEditor.setContainerConfigurationItemsTree(this);
            addSubConfigurationItemWindow = new Window();
            addSubConfigurationItemWindow.setWidth(500.0f, Sizeable.Unit.PIXELS);
            addSubConfigurationItemWindow.setHeight(250.0f, Sizeable.Unit.PIXELS);
            addSubConfigurationItemWindow.setResizable(false);
            addSubConfigurationItemWindow.setModal(true);
            addSubConfigurationItemWindow.center();
            addSubConfigurationItemWindow.setContent(customConfigurationItemValueEditor);
            UI.getCurrent().addWindow(addSubConfigurationItemWindow);
        }
        if(currentActionCaption.equals(userI18NProperties.
                getProperty("ActivityManagement_Common_DeleteConfigurationItemText"))){
            if(target.toString().equals(ROOT_CONFIGURATION_ITEM)){
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_CantDeleteRootConfigurationItemText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            Label confirmMessage = new Label(FontAwesome.INFO.getHtml() +" "+userI18NProperties.
                    getProperty("ActivityManagement_Common_ConfirmDeleteConfigurationItemText")+" "+
                    " <b>" + configurationItemItem.getItemProperty(CAPTION_PROPERTY).getValue().toString() + "</b>.", ContentMode.HTML);
            final ConfirmDialog removeConfigItemConfirmDialog = new ConfirmDialog();
            removeConfigItemConfirmDialog.setConfirmMessage(confirmMessage);
            final String itemIdToDelete=target.toString();
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    removeConfigItemConfirmDialog.close();
                    deleteContextSelectedConfigurationItem(itemIdToDelete);
                }
            };
            removeConfigItemConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(removeConfigItemConfirmDialog);
        }
    }

    private void deleteContextSelectedConfigurationItem(String itemId){
        Properties userI18NProperties = this.currentUserClientInfo.getUserI18NProperties();
        Object parentItemId=((Container.Hierarchical) container).getParent(itemId);
        Item parentConfigItemItem=container.getItem(parentItemId);
        CustomStructure parentCustomStructure=(CustomStructure)parentConfigItemItem.getItemProperty(CUSTOMSTRUCTURE_PROPERTY).getValue();
        String structureNameForDelete=currentContextSelectedCustomStructure.getStructureName();

        boolean deleteResult=ActivitySpaceOperationUtil.deleteSubCustomStructure(parentCustomStructure,structureNameForDelete);
        if(deleteResult){
            currentContextSelectedCustomStructure=null;
            currentContextSelectedCustomStructureItemId=null;
            ((HierarchicalContainer)container).removeItemRecursively(itemId);
            Notification resultNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DeleteDataSuccessText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Common_DeleteConfigurationItemSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_Common_DeleteConfigurationItemErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    public void addNewConfigurationItem(String itemName){
        Properties userI18NProperties = this.currentUserClientInfo.getUserI18NProperties();
        if(currentContextSelectedCustomStructure!=null&&currentContextSelectedCustomStructureItemId!=null){
            Collection childItemIdCollection= ((Container.Hierarchical) container).getChildren(currentContextSelectedCustomStructureItemId);
            if(childItemIdCollection!=null){
                Iterator childItemIdIterator=childItemIdCollection.iterator();
                while(childItemIdIterator.hasNext()){
                    Object currentId=childItemIdIterator.next();
                    Item currentChildItem = container.getItem(currentId);
                    if( currentChildItem.getItemProperty(CAPTION_PROPERTY).getValue().equals(itemName)){
                        Notification errorNotification = new Notification(userI18NProperties.
                                getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                                userI18NProperties.
                                getProperty("ActivityManagement_Common_SubConfigurationItemExistText"), Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                        return;
                    }
                }
            }
            CustomStructure resultStructure=ActivitySpaceOperationUtil.addSubCustomStructure(currentContextSelectedCustomStructure,itemName);
            if(resultStructure!=null){
                final Item configurationItemItem = container.addItem(currentContextSelectedCustomStructureItemId+"_"+resultStructure.getStructureName());
                configurationItemItem.getItemProperty(CAPTION_PROPERTY).setValue(itemName);
                configurationItemItem.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.SQUARE_O);
                configurationItemItem.getItemProperty(CUSTOMSTRUCTURE_PROPERTY).setValue(resultStructure);
                ((Container.Hierarchical) container).setParent(currentContextSelectedCustomStructureItemId+"_"+resultStructure.getStructureName(), currentContextSelectedCustomStructureItemId);
                ((Container.Hierarchical) container).setChildrenAllowed(currentContextSelectedCustomStructureItemId+"_"+resultStructure.getStructureName(), true);
                addSubConfigurationItemWindow.close();
                Notification resultNotification = new Notification(userI18NProperties.
                        getProperty("Global_Application_DataOperation_AddDataSuccessText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_Common_AddSubConfigurationItemSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                resultNotification.setPosition(Position.MIDDLE_CENTER);
                resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                resultNotification.show(Page.getCurrent());
            }else{
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("ActivityManagement_Common_AddSubConfigurationItemErrorText"),
                        userI18NProperties.
                                getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
            }
        }
    }

    private void configurationItemSelected(Object selectedItemId){
        if(selectedItemId==null){
            //unselected a configuration
            if(getRelatedCustomConfigurationItemDataEditor()!=null){
                getRelatedCustomConfigurationItemDataEditor().clearConfigurationItemData();
            }
        }else{
            Item selectedConfigItemItem=container.getItem(selectedItemId);
            CustomStructure selectedCustomStructure=(CustomStructure)selectedConfigItemItem.getItemProperty(CUSTOMSTRUCTURE_PROPERTY).getValue();
            if(getRelatedCustomConfigurationItemDataEditor()!=null){
                getRelatedCustomConfigurationItemDataEditor().renderConfigurationItemData(selectedCustomStructure);
            }
        }
    }

    public CustomConfigurationItemDataEditor getRelatedCustomConfigurationItemDataEditor() {
        return relatedCustomConfigurationItemDataEditor;
    }

    public void setRelatedCustomConfigurationItemDataEditor(CustomConfigurationItemDataEditor relatedCustomConfigurationItemDataEditor) {
        this.relatedCustomConfigurationItemDataEditor = relatedCustomConfigurationItemDataEditor;
    }
}
