package com.viewfunction.vfbam.ui.component.activityManagement;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class NewActivitySpaceCreatedEvent implements Event {

    private String activitySpaceName;

    public NewActivitySpaceCreatedEvent(String activitySpaceName){
        this.activitySpaceName=activitySpaceName;
    }

    public String getActivitySpaceName() {
        return activitySpaceName;
    }

    public interface NewActivitySpaceCreatedListener extends Listener {
        public void receivedNewActivitySpaceCreatedEvent(final NewActivitySpaceCreatedEvent event);
    }
}
