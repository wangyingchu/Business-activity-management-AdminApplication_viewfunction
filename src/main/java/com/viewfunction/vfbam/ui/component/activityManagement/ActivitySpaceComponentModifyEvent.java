package com.viewfunction.vfbam.ui.component.activityManagement;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ActivitySpaceComponentModifyEvent implements Event {

    private String activitySpaceName;
    private String componentType;
    private String componentId;
    private String modifyType;

    public static String MODIFYTYPE_ADD="MODIFYTYPE_ADD";
    public static String MODIFYTYPE_REMOVE="MODIFYTYPE_REMOVE";

    public ActivitySpaceComponentModifyEvent(String activitySpaceName, String componentType,String componentId,String modifyType){
        this.activitySpaceName=activitySpaceName;
        this.componentType=componentType;
        this.componentId=componentId;
        this.modifyType=modifyType;
    }

    public String getActivitySpaceName() {
        return activitySpaceName;
    }

    public String getComponentType() {
        return componentType;
    }

    public String getComponentId() {
        return componentId;
    }

    public String getModifyType() {
        return modifyType;
    }

    public interface ActivitySpaceComponentModifiedListener extends Listener {
        public void receivedActivitySpaceComponentModifiedEvent(final ActivitySpaceComponentModifyEvent event);
    }
}
