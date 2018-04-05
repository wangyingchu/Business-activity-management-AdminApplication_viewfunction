package com.viewfunction.vfbam.ui.component.common;

import com.vaadin.ui.Label;

public class MainSectionTitle extends Label {
    public MainSectionTitle(String sectionName){
        super(sectionName);
        addStyleName("h2");
        addStyleName("colored");
        addStyleName("ui_appSectionDiv");
        addStyleName("ui_appFadeMargin");
    }
}
