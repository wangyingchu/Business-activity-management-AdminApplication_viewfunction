package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.LongRangeValidator;
import com.vaadin.data.validator.BigDecimalRangeValidator;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.viewfunction.activityEngine.activityView.common.CustomAttribute;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.util.ApplicationConstant;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import javax.jcr.PropertyType;
import java.math.BigDecimal;
import java.util.*;
import java.util.Calendar;

/**
 * Created by wangychu on 2/7/17.
 */
public class EditConfigurationItemPropertyPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private TextField propertyNameTextField;
    private ComboBox propertyDataTypeField;
    private VerticalLayout dataFieldsInputContainerLayout;
    private List<Field> propertyValueInputFieldList;
    private String currentSelectedPropertyDataType;
    private Button addMoreValueButton;
    private Window containerDialog;
    private CustomAttribute customAttribute;
    private CustomAttributeItem relatedCustomAttributeItem;

    public EditConfigurationItemPropertyPanel(UserClientInfo currentUserClientInfo,CustomAttribute customAttribute){
        this.currentUserClientInfo=currentUserClientInfo;
        this.customAttribute=customAttribute;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        propertyValueInputFieldList=new ArrayList<>();

        MainSectionTitle addNewPropertySectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_Common_EditConfigItemPropertyText"));
        addComponent(addNewPropertySectionTitle);

        HorizontalLayout propertyInfoLabelContainerLayout=new HorizontalLayout();
        propertyInfoLabelContainerLayout.addStyleName("ui_appSectionFadeDiv");
        addComponent(propertyInfoLabelContainerLayout);

        HorizontalLayout titleSpacingLayout1=new HorizontalLayout();
        titleSpacingLayout1.setWidth(15, Sizeable.Unit.PIXELS);
        propertyInfoLabelContainerLayout.addComponent(titleSpacingLayout1);

        Label propertyInfoLabel=new Label(FontAwesome.INFO_CIRCLE.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_Common_ConfigItemPropertyInfoText")+":", ContentMode.HTML);
        propertyInfoLabel.setWidth(562, Sizeable.Unit.PIXELS);
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
        propertyNameTextField.setValue(this.customAttribute.getAttributeName());
        propertyNameTextField.setEnabled(false);
        propertyAttributeForm.addComponent(propertyNameTextField);

        propertyDataTypeField = new ComboBox(userI18NProperties.
                getProperty("ActivityManagement_Common_ConfigItemPropertyDataTypeText"));
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
        propertyDataTypeField.addItem(ApplicationConstant.DataFieldType_BINARY);

        switch(this.customAttribute.getAttributeType()){
            case PropertyType.STRING:
                propertyDataTypeField.select(ApplicationConstant.DataFieldType_STRING);
                currentSelectedPropertyDataType=ApplicationConstant.DataFieldType_STRING;
                break;
            case PropertyType.BOOLEAN:
                propertyDataTypeField.select(ApplicationConstant.DataFieldType_BOOLEAN);
                currentSelectedPropertyDataType=ApplicationConstant.DataFieldType_BOOLEAN;
                break;
            case PropertyType.DATE:
                propertyDataTypeField.select(ApplicationConstant.DataFieldType_DATE);
                currentSelectedPropertyDataType=ApplicationConstant.DataFieldType_DATE;
                break;
            case PropertyType.DECIMAL:
                propertyDataTypeField.select(ApplicationConstant.DataFieldType_DECIMAL);
                currentSelectedPropertyDataType=ApplicationConstant.DataFieldType_DECIMAL;
                break;
            case PropertyType.LONG:
                propertyDataTypeField.select(ApplicationConstant.DataFieldType_LONG);
                currentSelectedPropertyDataType=ApplicationConstant.DataFieldType_LONG;
                break;
            case PropertyType.DOUBLE:
                propertyDataTypeField.select(ApplicationConstant.DataFieldType_DOUBLE);
                currentSelectedPropertyDataType=ApplicationConstant.DataFieldType_DOUBLE;
                break;
            case PropertyType.BINARY:
                propertyDataTypeField.select(ApplicationConstant.DataFieldType_BINARY);
                currentSelectedPropertyDataType=ApplicationConstant.DataFieldType_BINARY;
                break;
        }
        propertyDataTypeField.setEnabled(false);
        propertyAttributeForm.addComponent(propertyDataTypeField);

        HorizontalLayout formButtonLineLayout=new HorizontalLayout();
        formButtonLineLayout.addStyleName("ui_appSectionFadeDiv");
        formButtonLineLayout.setWidth(100, Sizeable.Unit.PERCENTAGE);
        addComponent(formButtonLineLayout);

        HorizontalLayout propertyValuesLabelContainerLayout=new HorizontalLayout();
        propertyValuesLabelContainerLayout.addStyleName("ui_appSectionFadeDiv");
        addComponent(propertyValuesLabelContainerLayout);

        HorizontalLayout titleSpacingLayout2=new HorizontalLayout();
        titleSpacingLayout2.setWidth(15, Sizeable.Unit.PIXELS);
        propertyValuesLabelContainerLayout.addComponent(titleSpacingLayout2);

        Label propertyValuesLabel=new Label(VaadinIcons.INPUT.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_Common_ConfigItemPropertyValuesText")+":", ContentMode.HTML);
        propertyValuesLabel.setWidth(525, Sizeable.Unit.PIXELS);
        propertyValuesLabel.addStyleName(ValoTheme.LABEL_COLORED);
        propertyValuesLabel.addStyleName(ValoTheme.LABEL_SMALL);
        propertyValuesLabel.addStyleName(ValoTheme.LABEL_BOLD);
        propertyValuesLabelContainerLayout.addComponent(propertyValuesLabel);

        addMoreValueButton = new Button();
        addMoreValueButton.setEnabled(this.customAttribute.isArrayAttribute());
        addMoreValueButton.setIcon(FontAwesome.PLUS_SQUARE);
        addMoreValueButton.setDescription(userI18NProperties.
                getProperty("ActivityManagement_Common_AddMoreConfigItemPropertyButtonLabel"));
        addMoreValueButton.addStyleName("small");
        addMoreValueButton.addStyleName("borderless");
        addMoreValueButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Field newValueEditor=generateValueInputField(propertyDataTypeField.getValue().toString());
                HorizontalLayout valueInputFieldLayout=generateValueInputFieldLayout(newValueEditor,true);
                dataFieldsInputContainerLayout.addComponent(valueInputFieldLayout);
            }
        });
        propertyValuesLabelContainerLayout.addComponent(addMoreValueButton);

        Panel dataFieldsInputPanel=new Panel();
        dataFieldsInputPanel.setWidth(580, Sizeable.Unit.PIXELS);
        dataFieldsInputPanel.setHeight(320, Sizeable.Unit.PIXELS);
        dataFieldsInputPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        addComponent(dataFieldsInputPanel);

        dataFieldsInputContainerLayout=new VerticalLayout();
        dataFieldsInputContainerLayout.setMargin(new MarginInfo(false,true,false,true));
        dataFieldsInputPanel.setContent(dataFieldsInputContainerLayout);

        HorizontalLayout operationActionButtonsContainer=new HorizontalLayout();
        operationActionButtonsContainer.setWidth(100, Sizeable.Unit.PERCENTAGE);
        Button addPropertyButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_UpdateConfigItemPropertyButtonLabel"));
        addPropertyButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        addPropertyButton.setIcon(FontAwesome.CHECK);
        addPropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updatePropertyValues();
            }
        });
        operationActionButtonsContainer.addComponent(addPropertyButton);
        operationActionButtonsContainer.setComponentAlignment(addPropertyButton,Alignment.MIDDLE_CENTER);
        addComponent(operationActionButtonsContainer);
    }

    @Override
    public void attach() {
        super.attach();
        setCurrentPropertyValue();
    }

    private void setCurrentPropertyValue(){
        boolean isArrayValue=this.customAttribute.isArrayAttribute();
        Object propertyValue=this.customAttribute.getAttributeValue();
        switch(this.customAttribute.getAttributeType()){
            case PropertyType.STRING:
                if(isArrayValue){
                    String[] currentPropertyValue=(String[])propertyValue;
                    for(int i=0;i<currentPropertyValue.length;i++){
                        boolean addRemoveButton= i==0?false:true;
                        Field currentValueEditor=generateValueInputField(ApplicationConstant.DataFieldType_STRING);
                        currentValueEditor.setValue(currentPropertyValue[i]);
                        HorizontalLayout currentValueEditorLayout=generateValueInputFieldLayout(currentValueEditor,addRemoveButton);
                        dataFieldsInputContainerLayout.addComponent(currentValueEditorLayout);
                    }
                }else{
                    String currentPropertyValue=(String)propertyValue;
                    Field currentValueEditor=generateValueInputField(ApplicationConstant.DataFieldType_STRING);
                    currentValueEditor.setValue(currentPropertyValue);
                    HorizontalLayout currentValueEditorLayout=generateValueInputFieldLayout(currentValueEditor,false);
                    dataFieldsInputContainerLayout.addComponent(currentValueEditorLayout);
                }
                break;
            case PropertyType.BOOLEAN:
                if(isArrayValue){
                    boolean[] currentPropertyValue=(boolean[])propertyValue;
                    for(int i=0;i<currentPropertyValue.length;i++){
                        boolean addRemoveButton= i==0?false:true;
                        OptionGroup booleanTypeOption=(OptionGroup)generateValueInputField(ApplicationConstant.DataFieldType_BOOLEAN);
                        if(currentPropertyValue[i]){
                            booleanTypeOption.select("True");
                        }else{
                            booleanTypeOption.select("False");
                        }
                        HorizontalLayout currentValueEditorLayout=generateValueInputFieldLayout(booleanTypeOption,addRemoveButton);
                        dataFieldsInputContainerLayout.addComponent(currentValueEditorLayout);
                    }
                }else{
                    Boolean currentPropertyValue=(Boolean)propertyValue;
                    boolean currentBooleanValue=currentPropertyValue.booleanValue();
                    OptionGroup booleanTypeOption=(OptionGroup)generateValueInputField(ApplicationConstant.DataFieldType_BOOLEAN);
                    if(currentBooleanValue){
                        booleanTypeOption.select("True");
                    }else{
                        booleanTypeOption.select("False");
                    }
                    HorizontalLayout currentValueEditorLayout=generateValueInputFieldLayout(booleanTypeOption,false);
                    dataFieldsInputContainerLayout.addComponent(currentValueEditorLayout);
                }
                break;
            case PropertyType.DATE:
                if(isArrayValue){
                    Calendar[] currentPropertyValue=( Calendar[])propertyValue;
                    for(int i=0;i<currentPropertyValue.length;i++){
                        boolean addRemoveButton= i==0?false:true;
                        Field currentValueEditor=generateValueInputField(ApplicationConstant.DataFieldType_DATE);
                        currentValueEditor.setValue(currentPropertyValue[i].getTime());
                        HorizontalLayout currentValueEditorLayout=generateValueInputFieldLayout(currentValueEditor,addRemoveButton);
                        dataFieldsInputContainerLayout.addComponent(currentValueEditorLayout);
                    }
                }else{
                    Calendar currentPropertyValue=(Calendar)propertyValue;
                    Date currentDateValue=currentPropertyValue.getTime();
                    Field currentValueEditor=generateValueInputField(ApplicationConstant.DataFieldType_DATE);
                    currentValueEditor.setValue(currentDateValue);
                    HorizontalLayout currentValueEditorLayout=generateValueInputFieldLayout(currentValueEditor,false);
                    dataFieldsInputContainerLayout.addComponent(currentValueEditorLayout);
                }
                break;
            case PropertyType.DECIMAL:
                if(isArrayValue){
                    BigDecimal[] currentPropertyValue=( BigDecimal[])propertyValue;
                    for(int i=0;i<currentPropertyValue.length;i++){
                        boolean addRemoveButton= i==0?false:true;
                        Field currentValueEditor=generateValueInputField(ApplicationConstant.DataFieldType_DECIMAL);
                        currentValueEditor.setValue(currentPropertyValue[i].toPlainString());
                        HorizontalLayout currentValueEditorLayout=generateValueInputFieldLayout(currentValueEditor,addRemoveButton);
                        dataFieldsInputContainerLayout.addComponent(currentValueEditorLayout);
                    }
                }else{
                    BigDecimal currentPropertyValue=(BigDecimal)propertyValue;
                    Field currentValueEditor=generateValueInputField(ApplicationConstant.DataFieldType_DECIMAL);
                    currentValueEditor.setValue(currentPropertyValue.toPlainString());
                    HorizontalLayout currentValueEditorLayout=generateValueInputFieldLayout(currentValueEditor,false);
                    dataFieldsInputContainerLayout.addComponent(currentValueEditorLayout);
                }
                break;
            case PropertyType.LONG:
                if(isArrayValue){
                    long[] currentPropertyValue=(long[])propertyValue;
                    for(int i=0;i<currentPropertyValue.length;i++){
                        boolean addRemoveButton= i==0?false:true;
                        Field currentValueEditor=generateValueInputField(ApplicationConstant.DataFieldType_LONG);
                        currentValueEditor.setValue(""+currentPropertyValue[i]);
                        HorizontalLayout currentValueEditorLayout=generateValueInputFieldLayout(currentValueEditor,addRemoveButton);
                        dataFieldsInputContainerLayout.addComponent(currentValueEditorLayout);
                    }
                }else{
                    Long currentPropertyValue=(Long)propertyValue;
                    Field currentValueEditor=generateValueInputField(ApplicationConstant.DataFieldType_LONG);
                    currentValueEditor.setValue(currentPropertyValue.toString());
                    HorizontalLayout currentValueEditorLayout=generateValueInputFieldLayout(currentValueEditor,false);
                    dataFieldsInputContainerLayout.addComponent(currentValueEditorLayout);
                }
                break;
            case PropertyType.DOUBLE:
                if(isArrayValue){
                    double[] currentPropertyValue=(double[])propertyValue;
                    for(int i=0;i<currentPropertyValue.length;i++){
                        boolean addRemoveButton= i==0?false:true;
                        Field currentValueEditor=generateValueInputField(ApplicationConstant.DataFieldType_DOUBLE);
                        currentValueEditor.setValue(""+currentPropertyValue[i]);
                        HorizontalLayout currentValueEditorLayout=generateValueInputFieldLayout(currentValueEditor,addRemoveButton);
                        dataFieldsInputContainerLayout.addComponent(currentValueEditorLayout);
                    }
                }else{
                    Double currentPropertyValue=(Double)propertyValue;
                    Field currentValueEditor=generateValueInputField(ApplicationConstant.DataFieldType_DOUBLE);
                    currentValueEditor.setValue(currentPropertyValue.toString());
                    HorizontalLayout currentValueEditorLayout=generateValueInputFieldLayout(currentValueEditor,false);
                    dataFieldsInputContainerLayout.addComponent(currentValueEditorLayout);
                }
                break;
            case PropertyType.BINARY:
                //propertyDataTypeField.select(ApplicationConstant.DataFieldType_BINARY);
                break;
        }
    }

    private HorizontalLayout generateValueInputFieldLayout(Field valueInputField,boolean addRemoveButton){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        HorizontalLayout dataFieldContainerLayout=new HorizontalLayout();
        dataFieldContainerLayout.setWidth(560,Unit.PIXELS);
        dataFieldContainerLayout.addStyleName("ui_appSectionFadeDiv");
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

    private void updatePropertyValues(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
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
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_Common_ConfirmUpdateConfigItemPropertyText")+
                " <b>"+propertyName +"</b>.", ContentMode.HTML);
        final ConfirmDialog updatePropertyConfirmDialog = new ConfirmDialog();
        updatePropertyConfirmDialog.setConfirmMessage(confirmMessage);
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                updatePropertyConfirmDialog.close();
                Object propertyValue=buildPropertyValue();
                CustomAttribute updatedCustomAttribute= ActivityComponentFactory.createCustomAttribute();
                updatedCustomAttribute.setAttributeType(customAttribute.getAttributeType());
                updatedCustomAttribute.setArrayAttribute(customAttribute.isArrayAttribute());
                updatedCustomAttribute.setAttributeValue(propertyValue);
                updatedCustomAttribute.setAttributeName(customAttribute.getAttributeName());
                boolean updatePropertyResult=getRelatedCustomAttributeItem().updateCustomAttributeValue(updatedCustomAttribute);
                if(updatePropertyResult){
                    if(getContainerDialog()!=null){
                        getContainerDialog().close();
                    }
                    Notification resultNotification = new Notification(userI18NProperties.
                            getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                            userI18NProperties.
                                    getProperty("ActivityManagement_Common_UpdateConfigItemPropertySuccessText"), Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification(userI18NProperties.
                            getProperty("ActivityManagement_Common_UpdateConfigItemPropertyErrorText"),
                            userI18NProperties.
                                    getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        updatePropertyConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(updatePropertyConfirmDialog);
    }

    private Object buildPropertyValue(){
        if(ApplicationConstant.DataFieldType_STRING.equals(currentSelectedPropertyDataType)){
            if(!this.customAttribute.isArrayAttribute()){
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
            if(!this.customAttribute.isArrayAttribute()){
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
            if(!this.customAttribute.isArrayAttribute()){
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
            if(!this.customAttribute.isArrayAttribute()){
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
            if(!this.customAttribute.isArrayAttribute()){
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
            if(!this.customAttribute.isArrayAttribute()){
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

    public CustomAttributeItem getRelatedCustomAttributeItem() {
        return relatedCustomAttributeItem;
    }

    public void setRelatedCustomAttributeItem(CustomAttributeItem relatedCustomAttributeItem) {
        this.relatedCustomAttributeItem = relatedCustomAttributeItem;
    }
}
