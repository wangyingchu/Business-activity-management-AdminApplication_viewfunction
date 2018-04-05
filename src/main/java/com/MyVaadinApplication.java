package com;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

public class MyVaadinApplication extends UI {
    @Override
    public void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        setContent(layout);
        layout.addComponent(new Label("Viewfunction Business Activity Management system init page"));
    }
}
