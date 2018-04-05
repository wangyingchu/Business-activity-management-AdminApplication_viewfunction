package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.data.Property;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.LongRangeValidator;
import com.vaadin.data.validator.BigDecimalRangeValidator;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.viewfunction.activityEngine.activityView.common.CustomStructure;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ApplicationConstant;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.math.BigDecimal;
import java.util.*;
import java.util.Calendar;

/**
 * Created by wangychu on 2/3/17.
 */
public class AddNewConfigurationItemPropertyPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar addNewPropertyActionsBar;
    private CustomStructure configurationItemCustomStructure;
    private TextField propertyNameTextField;
    private ComboBox propertyDataTypeField;
    private VerticalLayout dataFieldsInputContainerLayout;
    private List<Field> propertyValueInputFieldList;
    private String currentSelectedPropertyDataType;
    private Button addMoreValueButton;
    private Window containerDialog;
    private CustomConfigurationItemDataEditor containerCustomConfigurationItemDataEditor;

    public AddNewConfigurationItemPropertyPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        propertyValueInputFieldList=new ArrayList<>();

        MainSectionTitle addNewPropertySectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_Common_AddConfigItemPropertyText"));
        addComponent(addNewPropertySectionTitle);

        addNewPropertyActionsBar =new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_Common_ConfigurationItemNameText")+" <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(addNewPropertyActionsBar);

        HorizontalLayout propertyInfoLabelContainerLayout=new HorizontalLayout();
        propertyInfoLabelContainerLayout.addStyleName("ui_appSectionFadeDiv");
        addComponent(propertyInfoLabelContainerLayout);

        HorizontalLayout titleSpacingLayout1=new HorizontalLayout();
        titleSpacingLayout1.setWidth(15,Unit.PIXELS);
        propertyInfoLabelContainerLayout.addComponent(titleSpacingLayout1);

        Label propertyInfoLabel=new Label(FontAwesome.INFO_CIRCLE.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_Common_ConfigItemPropertyInfoText")+":", ContentMode.HTML);
        propertyInfoLabel.setWidth(562,Unit.PIXELS);
        propertyInfoLabel.addStyleName(ValoTheme.LABEL_COLORED);
        propertyInfoLabel.addStyleName(ValoTheme.LABEL_SMALL);
        propertyInfoLabel.addStyleName(ValoTheme.LABEL_BOLD);
        propertyInfoLabelContainerLayout.addComponent(propertyInfoLabel);

        FormLayout propertyAttributeForm = new FormLayout();
        propertyAttributeForm.setMargin(false);
        propertyAttributeForm.setWidth("100%");
        propertyAttributeForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        addComponent(propertyAttributeForm);

        propertyNameTextField = new TextField(userI18NProperties.
                getProperty("ActivityManagement_Common_ConfigItemPropertyNameText"));
        propertyNameTextField.setRequired(true);
        propertyAttributeForm.addComponent(propertyNameTextField);

        propertyDataTypeField = new ComboBox(userI18NProperties.
                getProperty("ActivityManagement_Common_ConfigItemPropertyDataTypeText"));
        propertyDataTypeField.setRequired(true);
        propertyDataTypeField.setWidth("100%");
        propertyDataTypeField.setTextInputAllowed(false);
        propertyDataTypeField.setNullSelectionAllowed(false);
        propertyDataTypeField.setInputPrompt(userI18NProperties.
                getProperty("ActivityManagement_Common_SelectConfigItemPropertyDataTypeText"));
        propertyDataTypeField.addItem(ApplicationConstant.DataFieldType_STRING);
        propertyDataTypeField.addItem(ApplicationConstant.DataFieldType_BOOLEAN);
        propertyDataTypeField.addItem(ApplicationConstant.DataFieldType_DATE);
        propertyDataTypeField.addItem(ApplicationConstant.DataFieldType_DECIMAL);
        propertyDataTypeField.addItem(ApplicationConstant.DataFieldType_DOUBLE);
        propertyDataTypeField.addItem(ApplicationConstant.DataFieldType_LONG);
        //propertyDataTypeField.addItem(ApplicationConstant.DataFieldType_BINARY);
        propertyAttributeForm.addComponent(propertyDataTypeField);

        propertyDataTypeField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                addPropertyValueInputField(event.getProperty().getValue().toString());
            }
        });

        HorizontalLayout formButtonLineLayout=new HorizontalLayout();
        formButtonLineLayout.addStyleName("ui_appSectionFadeDiv");
        formButtonLineLayout.setWidth(100,Unit.PERCENTAGE);
        addComponent(formButtonLineLayout);

        HorizontalLayout propertyValuesLabelContainerLayout=new HorizontalLayout();
        propertyValuesLabelContainerLayout.addStyleName("ui_appSectionFadeDiv");
        addComponent(propertyValuesLabelContainerLayout);

        HorizontalLayout titleSpacingLayout2=new HorizontalLayout();
        titleSpacingLayout2.setWidth(15,Unit.PIXELS);
        propertyValuesLabelContainerLayout.addComponent(titleSpacingLayout2);

        Label propertyValuesLabel=new Label(VaadinIcons.INPUT.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_Common_ConfigItemPropertyValuesText")+":", ContentMode.HTML);
        propertyValuesLabel.setWidth(525,Unit.PIXELS);
        propertyValuesLabel.addStyleName(ValoTheme.LABEL_COLORED);
        propertyValuesLabel.addStyleName(ValoTheme.LABEL_SMALL);
        propertyValuesLabel.addStyleName(ValoTheme.LABEL_BOLD);
        propertyValuesLabelContainerLayout.addComponent(propertyValuesLabel);

        addMoreValueButton = new Button();
        addMoreValueButton.setEnabled(false);
        addMoreValueButton.setIcon(FontAwesome.PLUS_SQUARE);
        addMoreValueButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_Common_AddMoreConfigItemPropertyButtonLabel"));
        addMoreValueButton.addStyleName("small");
        addMoreValueButton.addStyleName("borderless");
        addMoreValueButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                HorizontalLayout valueInputFieldLayout=generateValueInputFieldLayout(currentSelectedPropertyDataType,true);
                dataFieldsInputContainerLayout.addComponent(valueInputFieldLayout);
            }
        });
        propertyValuesLabelContainerLayout.addComponent(addMoreValueButton);

        Panel dataFieldsInputPanel=new Panel();
        dataFieldsInputPanel.setWidth(580,Unit.PIXELS);
        dataFieldsInputPanel.setHeight(280,Unit.PIXELS);
        dataFieldsInputPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        addComponent(dataFieldsInputPanel);

        dataFieldsInputContainerLayout=new VerticalLayout();
        dataFieldsInputContainerLayout.setMargin(new MarginInfo(false,true,false,true));
        dataFieldsInputPanel.setContent(dataFieldsInputContainerLayout);

        HorizontalLayout operationActionButtonsContainer=new HorizontalLayout();
        operationActionButtonsContainer.setWidth(100,Unit.PERCENTAGE);
        Button addPropertyButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_AddNewConfigItemPropertyButtonLabel"));
        addPropertyButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        addPropertyButton.setIcon(FontAwesome.CHECK);
        addPropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                addPropertyValues();
            }
        });
        operationActionButtonsContainer.addComponent(addPropertyButton);
        operationActionButtonsContainer.setComponentAlignment(addPropertyButton,Alignment.MIDDLE_CENTER);
        addComponent(operationActionButtonsContainer);
    }

    public CustomStructure getConfigurationItemCustomStructure() {
        return configurationItemCustomStructure;
    }

    public void setConfigurationItemCustomStructure(CustomStructure configurationItemCustomStructure) {
        this.configurationItemCustomStructure = configurationItemCustomStructure;
    }

    @Override
    public void attach() {
        super.attach();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        Label sectionLabel=new Label(userI18NProperties.
                getProperty("ActivityManagement_Common_ConfigurationItemNameText")+": <b>"+configurationItemCustomStructure.getStructureName()+"</b>" , ContentMode.HTML);
        addNewPropertyActionsBar.resetSectionActionsBarContent(sectionLabel);
    }

    private void addPropertyValueInputField(String propertyDataType){
        addMoreValueButton.setEnabled(true);
        this.currentSelectedPropertyDataType=propertyDataType;
        propertyValueInputFieldList.clear();
        dataFieldsInputContainerLayout.removeAllComponents();
        HorizontalLayout valueInputFieldLayout=generateValueInputFieldLayout(this.currentSelectedPropertyDataType,false);
        dataFieldsInputContainerLayout.addComponent(valueInputFieldLayout);
    }

    private HorizontalLayout generateValueInputFieldLayout(String propertyDataType,boolean addRemoveButton){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        HorizontalLayout dataFieldContainerLayout=new HorizontalLayout();
        dataFieldContainerLayout.setWidth(560,Unit.PIXELS);
        dataFieldContainerLayout.addStyleName("ui_appSectionFadeDiv");
        Field valueInputField=generateValueInputField(propertyDataType);
        propertyValueInputFieldList.add(valueInputField);
        dataFieldContainerLayout.addComponent(valueInputField);
        if(addRemoveButton) {
            Button removeCurrentFieldButton = new Button();
            removeCurrentFieldButton.setIcon(FontAwesome.TRASH_O);
            removeCurrentFieldButton.setDescription(userI18NProperties.
                    getProperty("ActivityManagement_Common_RemoveConfigItemPropertyButtonLabel"));
            removeCurrentFieldButton.addStyleName("small");
            removeCurrentFieldButton.addStyleName("borderless");
            dataFieldContainerLayout.addComponent(removeCurrentFieldButton);
            dataFieldContainerLayout.setComponentAlignment(removeCurrentFieldButton, Alignment.MIDDLE_RIGHT);

            removeCurrentFieldButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    propertyValueInputFieldList.remove(valueInputField);
                    dataFieldsInputContainerLayout.removeComponent(dataFieldContainerLayout );
                }
            });
        }
        return dataFieldContainerLayout;
    }

    private Field generateValueInputField(String propertyDataType){
        if(ApplicationConstant.DataFieldType_STRING.equals(propertyDataType)){
            TextField valueInputField = new TextField();
            valueInputField.setWidth(500,Unit.PIXELS);
            valueInputField.setRequired(true);
            valueInputField.addStyleName(ValoTheme.DATEFIELD_BORDERLESS);
            return valueInputField;
        } else if(ApplicationConstant.DataFieldType_BOOLEAN.equals(propertyDataType)){
            OptionGroup booleanTypeOption = new OptionGroup();
            booleanTypeOption.setWidth(500,Unit.PIXELS);
            booleanTypeOption.setRequired(true);
            booleanTypeOption.addItem("True");
            booleanTypeOption.addItem("False");
            booleanTypeOption.select("True");
            booleanTypeOption.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
            return booleanTypeOption;
        }
        else if(ApplicationConstant.DataFieldType_DATE.equals(propertyDataType)){
            PopupDateField popupDateField=new PopupDateField();
            popupDateField.addStyleName(ValoTheme.DATEFIELD_BORDERLESS);
            popupDateField.setWidth(500,Unit.PIXELS);
            popupDateField.setRequired(true);
            popupDateField.setDateFormat("yyyy-MM-dd hh:mm:ss");
            popupDateField.setResolution(Resolution.SECOND);
            return popupDateField;
        }
        else if(ApplicationConstant.DataFieldType_DECIMAL.equals(propertyDataType)){
            TextField valueInputField = new TextField();
            valueInputField.setWidth(500,Unit.PIXELS);
            valueInputField.setRequired(true);
            valueInputField.addStyleName(ValoTheme.DATEFIELD_BORDERLESS);
            valueInputField.setConverter(BigDecimal.class);
            valueInputField.addValidator(new BigDecimalRangeValidator("the value of this property must be a DECIMAL type value", null, null));
            valueInputField.setValue("0.0");
            return valueInputField;
        }
        else if(ApplicationConstant.DataFieldType_DOUBLE.equals(propertyDataType)){
            TextField valueInputField = new TextField();
            valueInputField.setWidth(500,Unit.PIXELS);
            valueInputField.setRequired(true);
            valueInputField.addStyleName(ValoTheme.DATEFIELD_BORDERLESS);
            valueInputField.setConverter(Double.class);
            valueInputField.addValidator(new DoubleRangeValidator("the value of this property must be a DOUBLE type value", null, null));
            valueInputField.setValue("0.0");
            return valueInputField;
        }
        else if(ApplicationConstant.DataFieldType_LONG.equals(propertyDataType)){
            TextField valueInputField = new TextField();
            valueInputField.setWidth(500,Unit.PIXELS);
            valueInputField.setRequired(true);
            valueInputField.addStyleName(ValoTheme.DATEFIELD_BORDERLESS);
            valueInputField.setConverter(Long.class);
            valueInputField.addValidator(new LongRangeValidator("the value of this property must be a LONG type value", null, null));
            valueInputField.setValue("0");
            return valueInputField;
        }
        else if(ApplicationConstant.DataFieldType_BINARY.equals(propertyDataType)){
            return null;
        }else{
            return null;
        }
    }

    private void addPropertyValues(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        if(propertyNameTextField.getValue().equals("")||propertyDataTypeField.getValue()==null){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_PleaseInputAllFieldText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean inputtedValueValidateResult=true;
        for(Field currentInputField:propertyValueInputFieldList){
            boolean validateResult=currentInputField.isValid();
            inputtedValueValidateResult=inputtedValueValidateResult&validateResult;
        }
        if(!inputtedValueValidateResult){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Common_PleaseInputAllConfigItemPropertyValuesText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        String propertyName=propertyNameTextField.getValue();

        boolean isExistProperty=ActivitySpaceOperationUtil.checkCustomStructureAttributeExistence(getConfigurationItemCustomStructure(),propertyName);
        if(isExistProperty){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Common_ConfigItemPropertyExistText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_Common_ConfigAddConfigItemPropertyText")+
                " <b>"+propertyName +"</b>.", ContentMode.HTML);
        final ConfirmDialog addPropertyConfirmDialog = new ConfirmDialog();
        addPropertyConfirmDialog.setConfirmMessage(confirmMessage);
        //final ActivityDataFieldEditor self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addPropertyConfirmDialog.close();
                Object propertyValue=buildPropertyValue();
                boolean addPropertyResult=ActivitySpaceOperationUtil.addCustomStructureAttribute(getConfigurationItemCustomStructure(),propertyName,propertyValue);
                if(addPropertyResult){
                    if(getContainerDialog()!=null){
                        getContainerDialog().close();
                    }
                    if(getContainerCustomConfigurationItemDataEditor()!=null){
                        getContainerCustomConfigurationItemDataEditor().addNewCustomAttributeItem(propertyName,propertyValue);
                    }
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_AddDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_Common_AddConfigAddConfigItemPropertySuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("ActivityManagement_Common_AddConfigAddConfigItemPropertyErrorText"),
                            userI18NProperties.
                                    getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        addPropertyConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addPropertyConfirmDialog);
    }

    private Object buildPropertyValue(){
        if(ApplicationConstant.DataFieldType_STRING.equals(currentSelectedPropertyDataType)){
            if(propertyValueInputFieldList.size()==1){
                String propertyValue=((TextField)propertyValueInputFieldList.get(0)).getValue();
                return propertyValue;
            }else{
                String[] propertyValue=new String[propertyValueInputFieldList.size()];
                for(int i=0;i<propertyValueInputFieldList.size();i++){
                    propertyValue[i]=((TextField)propertyValueInputFieldList.get(i)).getValue();
                }
                return propertyValue;
            }
        } else if(ApplicationConstant.DataFieldType_BOOLEAN.equals(currentSelectedPropertyDataType)){
            if(propertyValueInputFieldList.size()==1){
                OptionGroup booleanTypeOption=((OptionGroup)propertyValueInputFieldList.get(0));
                String propertyValueStr=booleanTypeOption.getValue().toString();
                if(propertyValueStr.equals("True")){
                    return new Boolean(true);
                }else{
                    return new Boolean(false);
                }
            }else{
                boolean[] propertyValue=new boolean[propertyValueInputFieldList.size()];
                for(int i=0;i<propertyValueInputFieldList.size();i++){
                    OptionGroup booleanTypeOption=((OptionGroup)propertyValueInputFieldList.get(i));
                    String propertyValueStr=booleanTypeOption.getValue().toString();
                    if(propertyValueStr.equals("True")){
                        propertyValue[i]= true;
                    }else{
                        propertyValue[i]=false;
                    }
                }
                return propertyValue;
            }
        } else if(ApplicationConstant.DataFieldType_DATE.equals(currentSelectedPropertyDataType)){
            if(propertyValueInputFieldList.size()==1){
                PopupDateField popupDateField=((PopupDateField)propertyValueInputFieldList.get(0));
                Calendar propertyValue=Calendar.getInstance();
                propertyValue.setTime(popupDateField.getValue());
                return propertyValue;
            }else{
                Calendar[] propertyValue=new Calendar[propertyValueInputFieldList.size()];
                for(int i=0;i<propertyValueInputFieldList.size();i++){
                    PopupDateField popupDateField=((PopupDateField)propertyValueInputFieldList.get(i));
                    Calendar currentPropertyValue=Calendar.getInstance();
                    currentPropertyValue.setTime(popupDateField.getValue());
                    propertyValue[i]=currentPropertyValue;
                }
                return propertyValue;
            }
        } else if(ApplicationConstant.DataFieldType_DECIMAL.equals(currentSelectedPropertyDataType)){
            if(propertyValueInputFieldList.size()==1){
                BigDecimal propertyValue=(BigDecimal)((TextField)propertyValueInputFieldList.get(0)).getConvertedValue();
                return propertyValue;
            }else{
                BigDecimal[] propertyValue=new BigDecimal[propertyValueInputFieldList.size()];
                for(int i=0;i<propertyValueInputFieldList.size();i++){
                    BigDecimal currentPropertyValue=(BigDecimal)((TextField)propertyValueInputFieldList.get(i)).getConvertedValue();
                    propertyValue[i]=currentPropertyValue;
                }
                return propertyValue;
            }
        } else if(ApplicationConstant.DataFieldType_DOUBLE.equals(currentSelectedPropertyDataType)){
            if(propertyValueInputFieldList.size()==1){
                Double propertyValue=(Double)((TextField)propertyValueInputFieldList.get(0)).getConvertedValue();
                return propertyValue;
            }else{
                double[] propertyValue=new double[propertyValueInputFieldList.size()];
                for(int i=0;i<propertyValueInputFieldList.size();i++){
                    Double currentPropertyValue=(Double)((TextField)propertyValueInputFieldList.get(i)).getConvertedValue();
                    propertyValue[i]=currentPropertyValue.doubleValue();
                }
                return propertyValue;
            }
        } else if(ApplicationConstant.DataFieldType_LONG.equals(currentSelectedPropertyDataType)){
            if(propertyValueInputFieldList.size()==1){
                Long propertyValue=(Long)((TextField)propertyValueInputFieldList.get(0)).getConvertedValue();
                return propertyValue;
            }else{
                long[] propertyValue=new long[propertyValueInputFieldList.size()];
                for(int i=0;i<propertyValueInputFieldList.size();i++){
                    Long currentPropertyValue=(Long)((TextField)propertyValueInputFieldList.get(i)).getConvertedValue();
                    propertyValue[i]=currentPropertyValue.longValue();
                }
                return propertyValue;
            }
        }else if(ApplicationConstant.DataFieldType_BINARY.equals(currentSelectedPropertyDataType)){
            return null;
        }
        return null;
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public CustomConfigurationItemDataEditor getContainerCustomConfigurationItemDataEditor() {
        return containerCustomConfigurationItemDataEditor;
    }

    public void setContainerCustomConfigurationItemDataEditor(CustomConfigurationItemDataEditor containerCustomConfigurationItemDataEditor) {
        this.containerCustomConfigurationItemDataEditor = containerCustomConfigurationItemDataEditor;
    }
}
