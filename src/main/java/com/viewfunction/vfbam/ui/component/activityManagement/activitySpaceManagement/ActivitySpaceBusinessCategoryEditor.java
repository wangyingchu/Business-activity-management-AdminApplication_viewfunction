package com.viewfunction.vfbam.ui.component.activityManagement.activitySpaceManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;

import com.vaadin.ui.*;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.PropertyValuesActionTable;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ActivitySpaceBusinessCategoryEditor  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private String activitySpaceName;
    private PropertyValuesActionTable propertyValuesActionTable;
    private Button updateButton;
    private Button cancelButton;
    private String[] currentBusinessCategories;
    private SectionActionButton addCategoryActionButton;
    final HorizontalLayout operationButtonsLayout = new HorizontalLayout();
    public ActivitySpaceBusinessCategoryEditor(UserClientInfo currentUserClientInfo,String activitySpaceName){
        this.currentUserClientInfo=currentUserClientInfo;
        this.activitySpaceName=activitySpaceName;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        SectionActionsBar businessCategoriesSectionActionsBar=new SectionActionsBar(new Label(FontAwesome.LIST.getHtml() + " "+userI18NProperties.
                getProperty("ActivitySpaceManagement_SpaceConfiguration_BusinessCatgOptionsText"), ContentMode.HTML));
        addComponent(businessCategoriesSectionActionsBar);
        addCategoryActionButton = new SectionActionButton();
        addCategoryActionButton.setCaption(userI18NProperties.
                getProperty("ActivitySpaceManagement_SpaceConfiguration_AddCategoryButtonLabel"));
        addCategoryActionButton.setIcon(FontAwesome.PLUS_SQUARE);
        addCategoryActionButton.setEnabled(false);
        businessCategoriesSectionActionsBar.addActionComponent(addCategoryActionButton);
        propertyValuesActionTable=new PropertyValuesActionTable(this.currentUserClientInfo,"270",userI18NProperties.
                getProperty("ActivitySpaceManagement_SpaceConfiguration_CategoryOptionsText"),userI18NProperties.
                getProperty("ActivitySpaceManagement_SpaceConfiguration_BusinessCatgOptionText"),false,false);
        addComponent(propertyValuesActionTable);
        addCategoryActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                propertyValuesActionTable.addPropertyValue();
            }
        });

        operationButtonsLayout.setMargin(new MarginInfo(true, false, true, false));
        operationButtonsLayout.setSpacing(true);
        operationButtonsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        addComponent(operationButtonsLayout);

        updateButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_UpdateButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(updateButton.getCaption().equals(userI18NProperties.
                        getProperty("ActivityManagement_Common_UpdateButtonLabel"))){
                    updateButton.setCaption(userI18NProperties.
                            getProperty("ActivityManagement_Common_SaveButtonLabel"));
                    updateButton.setIcon(FontAwesome.SAVE);
                    updateButton.addStyleName("primary");
                    operationButtonsLayout.addComponent(cancelButton);
                    addCategoryActionButton.setEnabled(true);
                    propertyValuesActionTable.enableTableFullEdit();
                }else{
                    saveCategoriesData();
                }
            }
        });
        updateButton.setIcon(FontAwesome.HAND_O_RIGHT);
        operationButtonsLayout.addComponent(updateButton);

        cancelButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_CancelButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                disableEdit();
            }
        });
        cancelButton.setIcon(FontAwesome.TIMES);
    }

    @Override
    public void attach() {
        super.attach();
        ActivitySpaceMetaInfoDAO metaInfoDAO= ActivitySpaceOperationUtil.getActivitySpaceMetaInfo(this.activitySpaceName, new String[]{ActivitySpaceOperationUtil.ACTIVITYSPACE_METAINFOTYPE_BUSINESSCATEGORY});
        currentBusinessCategories=metaInfoDAO.getBusinessCategories();
        setBusinessCategoriesValue();
    }

    private void setBusinessCategoriesValue(){
        propertyValuesActionTable.setPropertyValues(currentBusinessCategories);
    }

    private void disableEdit(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        updateButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_Common_UpdateButtonLabel"));
        updateButton.removeStyleName("primary");
        updateButton.setIcon(FontAwesome.HAND_O_RIGHT);
        operationButtonsLayout.removeComponent(cancelButton);
        addCategoryActionButton.setEnabled(false);
        propertyValuesActionTable.disableTableFullEdit();
        setBusinessCategoriesValue();
    }

    private void saveCategoriesData(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                getProperty("ActivitySpaceManagement_SpaceConfiguration_ConfirmUpdateBusinessCatgText")+
                " <b>"+this.activitySpaceName +"</b>", ContentMode.HTML);
        final ActivitySpaceBusinessCategoryEditor self=this;
        final String[] newCategories=propertyValuesActionTable.getPropertyValues();
        final ConfirmDialog updatePropertyValueConfirmDialog = new ConfirmDialog();
        updatePropertyValueConfirmDialog.setConfirmMessage(confirmMessage);
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                updatePropertyValueConfirmDialog.close();

                //do update change
                boolean updateCategoryResult=ActivitySpaceOperationUtil.setActivitySpaceBusinessCategories(self.activitySpaceName,newCategories);
                if(updateCategoryResult){
                    self.currentBusinessCategories=newCategories;
                    disableEdit();
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivitySpaceManagement_SpaceConfiguration_UpdateBusinessCatgSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("ActivitySpaceManagement_SpaceConfiguration_UpdateBusinessCatgErrorText"),
                            userI18NProperties.
                                    getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        updatePropertyValueConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(updatePropertyValueConfirmDialog);
    }
}
