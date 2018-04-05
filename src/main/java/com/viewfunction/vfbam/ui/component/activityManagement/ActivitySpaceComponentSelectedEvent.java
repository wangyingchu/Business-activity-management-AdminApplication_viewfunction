package com.viewfunction.vfbam.ui.component.activityManagement;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ActivitySpaceComponentSelectedEvent implements Event{

    private String activitySpaceName;
    private String componentType;
    private String componentId;

    public ActivitySpaceComponentSelectedEvent(String activitySpaceName, String componentType,String componentId){
        this.activitySpaceName=activitySpaceName;
        this.componentType=componentType;
        this.componentId=componentId;
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

    public interface ActivitySpaceComponentSelectedListener extends Listener {
        public void receivedActivitySpaceComponentSelectedEvent(final ActivitySpaceComponentSelectedEvent event);
    }
}
