package com.viewfunction.vfbam.ui.component.common;

import com.vaadin.ui.Label;

public class SecondarySectionTitle extends Label {

    public SecondarySectionTitle(String sectionName){
        super(sectionName);
        addStyleName("h3");
        addStyleName("colored");
        addStyleName("ui_appSectionDiv");
        addStyleName("ui_appFadeMargin");
    }
}
