package com.viewfunction.vfbam.ui.component.activityManagement.util;


public class ActivityDataFieldVO {

    private String dataFieldName;
    private String dataFieldDisplayName;
    private String dataType;
    private String description;
    private boolean arrayField;
    private boolean mandatoryField;
    private boolean systemField;
    private boolean readableField;
    private boolean writableField;


    public String getDataFieldName() {
        return dataFieldName;
    }

    public void setDataFieldName(String dataFieldName) {
        this.dataFieldName = dataFieldName;
    }

    public String getDataFieldDisplayName() {
        return dataFieldDisplayName;
    }

    public void setDataFieldDisplayName(String dataFieldDisplayName) {
        this.dataFieldDisplayName = dataFieldDisplayName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isArrayField() {
        return arrayField;
    }

    public void setArrayField(boolean arrayField) {
        this.arrayField = arrayField;
    }

    public boolean isMandatoryField() {
        return mandatoryField;
    }

    public void setMandatoryField(boolean mandatoryField) {
        this.mandatoryField = mandatoryField;
    }

    public boolean isSystemField() {
        return systemField;
    }

    public void setSystemField(boolean systemField) {
        this.systemField = systemField;
    }

    public boolean isReadableField() {
        return readableField;
    }

    public void setReadableField(boolean readableField) {
        this.readableField = readableField;
    }

    public boolean isWritableField() {
        return writableField;
    }

    public void setWritableField(boolean writableField) {
        this.writableField = writableField;
    }
}
