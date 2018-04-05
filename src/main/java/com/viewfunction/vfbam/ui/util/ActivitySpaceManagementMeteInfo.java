package com.viewfunction.vfbam.ui.util;

import java.io.Serializable;

public class ActivitySpaceManagementMeteInfo implements Serializable {
    private String activitySpaceName;
    private String componentType;
    private String componentId;

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getActivitySpaceName() {
        return activitySpaceName;
    }

    public void setActivitySpaceName(String activitySpaceName) {
        this.activitySpaceName = activitySpaceName;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }
}
