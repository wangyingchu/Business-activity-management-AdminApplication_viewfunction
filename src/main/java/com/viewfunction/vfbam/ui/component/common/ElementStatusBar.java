package com.viewfunction.vfbam.ui.component.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class ElementStatusBar extends HorizontalLayout {
    private Label activitySpaceNameProp;
    private HorizontalLayout statusElementsBarLayout;
    public ElementStatusBar(){
        setHeight("32px");
        setWidth("100%");
        setSpacing(false);
        this.setStyleName("ui_compElementStatusBar");

        HorizontalLayout statusElementContainer=new HorizontalLayout();
        this.addComponent(statusElementContainer);

        activitySpaceNameProp = new Label( FontAwesome.TERMINAL.getHtml(),ContentMode.HTML);
        activitySpaceNameProp.setStyleName("ui_appLightDarkMessage");
        statusElementContainer.addComponent(activitySpaceNameProp);

        statusElementsBarLayout=new HorizontalLayout();
        statusElementsBarLayout.setWidth("100%");
        statusElementsBarLayout.setStyleName("ui_appLightDarkMessage");
        statusElementContainer.addComponent(statusElementsBarLayout);
    }

    public void setActivitySpaceName(String activitySpaceName){
        this.activitySpaceNameProp.setValue(FontAwesome.TERMINAL.getHtml() + activitySpaceName + FontAwesome.ANGLE_RIGHT.getHtml());
    }

    public void addStatusElement(Component barElementComponent){
        statusElementsBarLayout.addComponent(barElementComponent);
    }

    public void clearStatusElements(){
        statusElementsBarLayout.removeAllComponents();
    }
}
