package com.viewfunction.vfbam.ui.component.common;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class SectionActionsBar extends HorizontalLayout {
    public SectionActionsBar(Label actionBarTitle){
        addStyleName("ui_appActionsBar");
        /* addStyleName("v-panel-caption");*/
        addStyleName("light");
        setWidth("100%");
        addComponent(actionBarTitle);
        setExpandRatio(actionBarTitle, 1);
    }

    public void addActionComponent(Component actionComponent){
        addComponent(actionComponent);
    }

    public void resetSectionActionsBarContent(Label actionBarTitle){
        this.removeAllComponents();
        addComponent(actionBarTitle);
        setExpandRatio(actionBarTitle, 1);
    }
}
