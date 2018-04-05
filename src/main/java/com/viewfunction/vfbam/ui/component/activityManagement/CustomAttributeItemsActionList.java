package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.viewfunction.activityEngine.activityView.common.CustomAttribute;
import com.viewfunction.activityEngine.activityView.common.CustomStructure;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import javax.jcr.PropertyType;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wangychu on 2/1/17.
 */
public class CustomAttributeItemsActionList extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private VerticalLayout customAttributeItemListContainer;
    private CustomStructure customStructure;
    private Map<String,CustomAttributeItem> customAttributeItemMap;

    public CustomAttributeItemsActionList(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setWidth(100,Unit.PERCENTAGE);
        this.customAttributeItemListContainer=new VerticalLayout();
        this.customAttributeItemListContainer.setMargin(true);
        this.customAttributeItemListContainer.setSpacing(true);
        this.customAttributeItemMap=new HashMap<>();

        Panel listContainerPanel=new Panel();
        listContainerPanel.setWidth(100,Unit.PERCENTAGE);
        listContainerPanel.setHeight(485,Unit.PIXELS);
        listContainerPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        this.addComponent(listContainerPanel);
        listContainerPanel.setContent(this.customAttributeItemListContainer);
    }

    public void renderCustomAttributeItems(List<CustomAttribute> customAttributeList){
        this.customAttributeItemListContainer.removeAllComponents();
        this.customAttributeItemMap.clear();
        if(customAttributeList!=null){
            for(CustomAttribute currentCustomAttribute:customAttributeList){
                CustomAttributeItem currentCustomAttributeItem=new CustomAttributeItem(this.currentUserClientInfo,currentCustomAttribute);
                currentCustomAttributeItem.setContainerCustomAttributeItemsList(this);
                this.customAttributeItemListContainer.addComponent(currentCustomAttributeItem);
                this.customAttributeItemMap.put(currentCustomAttribute.getAttributeName(),currentCustomAttributeItem);
            }
        }
    }

    public CustomStructure getCustomStructure() {
        return customStructure;
    }

    public void setCustomStructure(CustomStructure customStructure) {
        this.customStructure = customStructure;
    }

    public boolean deleteStructureAttribute(String attributeName){
        boolean deletePropertyResult=ActivitySpaceOperationUtil.deleteCustomStructureAttribute(getCustomStructure(),attributeName);
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        if(deletePropertyResult){
            CustomAttributeItem targetCustomAttributeItem= this.customAttributeItemMap.get(attributeName);
            if(targetCustomAttributeItem!=null){
                this.customAttributeItemMap.remove(attributeName);
                this.customAttributeItemListContainer.removeComponent(targetCustomAttributeItem);
            }
            Notification resultNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DeleteDataSuccessText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Common_DeleteConfigItemPropertySuccessText"), Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_Common_DeleteConfigItemPropertyErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
        return deletePropertyResult;
    }

    public void addNewCustomAttributeItem(String propertyName,Object propertyValue){
        CustomAttribute customAttribute= ActivityComponentFactory.createCustomAttribute();
        customAttribute.setAttributeName(propertyName);
        customAttribute.setAttributeValue(propertyValue);

        if(propertyValue instanceof String){
            customAttribute.setArrayAttribute(false);
            customAttribute.setAttributeType(PropertyType.STRING);
        }
        if(propertyValue instanceof String[]){
            customAttribute.setArrayAttribute(true);
            customAttribute.setAttributeType(PropertyType.STRING);
        }
        if(propertyValue instanceof Boolean){
            customAttribute.setArrayAttribute(false);
            customAttribute.setAttributeType(PropertyType.BOOLEAN);
        }
        if(propertyValue instanceof boolean[]){
            customAttribute.setArrayAttribute(true);
            customAttribute.setAttributeType(PropertyType.BOOLEAN);
        }
        if(propertyValue instanceof Calendar){
            customAttribute.setArrayAttribute(false);
            customAttribute.setAttributeType(PropertyType.DATE);
        }
        if(propertyValue instanceof Calendar[]){
            customAttribute.setArrayAttribute(true);
            customAttribute.setAttributeType(PropertyType.DATE);
        }
        if(propertyValue instanceof BigDecimal){
            customAttribute.setArrayAttribute(false);
            customAttribute.setAttributeType(PropertyType.DECIMAL);
        }
        if(propertyValue instanceof BigDecimal[]){
            customAttribute.setArrayAttribute(true);
            customAttribute.setAttributeType(PropertyType.DECIMAL);
        }
        if(propertyValue instanceof Long){
            customAttribute.setArrayAttribute(false);
            customAttribute.setAttributeType(PropertyType.LONG);
        }
        if(propertyValue instanceof long[]){
            customAttribute.setArrayAttribute(true);
            customAttribute.setAttributeType(PropertyType.LONG);
        }
        if(propertyValue instanceof Double){
            customAttribute.setArrayAttribute(false);
            customAttribute.setAttributeType(PropertyType.DOUBLE);
        }
        if(propertyValue instanceof double[]){
            customAttribute.setArrayAttribute(true);
            customAttribute.setAttributeType(PropertyType.DOUBLE);
        }

        CustomAttributeItem currentCustomAttributeItem=new CustomAttributeItem(this.currentUserClientInfo,customAttribute);
        currentCustomAttributeItem.setContainerCustomAttributeItemsList(this);
        this.customAttributeItemListContainer.addComponent(currentCustomAttributeItem);
        this.customAttributeItemMap.put(customAttribute.getAttributeName(),currentCustomAttributeItem);
    }

    public boolean updateCustomAttributeValue(CustomAttribute customAttribute){
        boolean updatePropertyResult=ActivitySpaceOperationUtil.updateCustomStructureAttribute(getCustomStructure(),customAttribute);
        return updatePropertyResult;
    }
}
