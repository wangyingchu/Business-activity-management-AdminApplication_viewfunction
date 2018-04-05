package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.*;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.io.File;
import java.util.Properties;

public class ActivityDefinitionBPMNEditor extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private VerticalLayout xmlSourceLayout;
    private VerticalLayout graphViewLayout;

    private File activityDefinitionBPMNFile;
    private File activityDefinitionBPMNFlowDiagramFile;
    private ActivityDefinitionDetailInfo containerActivityDefinitionDetailInfo;

    public ActivityDefinitionBPMNEditor(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        SectionActionsBar activityTypeDesignSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.STEAM_SQUARE.getHtml() + " "+
                userI18NProperties.
                        getProperty("ActivityManagement_ActivityTypeManagement_BPMNFileActionButtonsLabel")+":", ContentMode.HTML));
        addComponent(activityTypeDesignSectionActionsBar);
        SectionActionButton updateActivityDefinitionActionButton = new SectionActionButton();
        updateActivityDefinitionActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_UpdateActivityTypeButtonLabel"));
        updateActivityDefinitionActionButton.setIcon(FontAwesome.UPLOAD);
        activityTypeDesignSectionActionsBar.addActionComponent(updateActivityDefinitionActionButton);

        String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        final UpdateActivityTypePanel updateActivityTypePanel=new UpdateActivityTypePanel(this.currentUserClientInfo);
        updateActivityTypePanel.setActivityType(activityDefinitionType);
        updateActivityTypePanel.setRelatedActivityDefinitionBPMNEditor(this);
        updateActivityDefinitionActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Unit.PIXELS);
                window.setHeight(300.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(updateActivityTypePanel);
                updateActivityTypePanel.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        SectionActionButton downloadActivityDefinitionFileActionButton = new SectionActionButton();
        downloadActivityDefinitionFileActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_DownloadActivityTypeButtonLabel"));
        downloadActivityDefinitionFileActionButton.setIcon(FontAwesome.CLOUD_DOWNLOAD);
        activityTypeDesignSectionActionsBar.addActionComponent(downloadActivityDefinitionFileActionButton);

        generateBPMNFiles();
        Resource res = new FileResource(activityDefinitionBPMNFile);
        FileDownloader fd = new FileDownloader(res);
        fd.extend(downloadActivityDefinitionFileActionButton);

        TabSheet activityTypeProcessDesignDetailTabSheet = new TabSheet();
        addComponent(activityTypeProcessDesignDetailTabSheet);

        VerticalLayout graphContainerViewLayout=new VerticalLayout();
        VerticalLayout spacingViewLayout=new VerticalLayout();
        spacingViewLayout.setHeight("10px");
        graphContainerViewLayout.addComponent(spacingViewLayout);
        graphViewLayout=new VerticalLayout();
        graphViewLayout.setSpacing(true);
        graphViewLayout.setMargin(true);
        graphContainerViewLayout.addComponent(graphViewLayout);
        TabSheet.Tab graphViewTab = activityTypeProcessDesignDetailTabSheet.addTab(graphContainerViewLayout,userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_GraphViewText"));
        xmlSourceLayout=new VerticalLayout();
        TabSheet.Tab xmlSourceViewTab = activityTypeProcessDesignDetailTabSheet.addTab(xmlSourceLayout,userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_SourceViewText"));
    }

    @Override
    public void attach() {
        super.attach();
        displayBPMNFileContent();
    }

    private void displayBPMNFileContent(){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        String bpmnContent= ActivitySpaceOperationUtil.getActivityTypeBMPNFileContent(activitySpaceName,activityDefinitionType);
        Label contentLabel=new Label(bpmnContent,ContentMode.PREFORMATTED);
        xmlSourceLayout.removeAllComponents();
        xmlSourceLayout.addComponent(contentLabel);
        Resource res = new FileResource(activityDefinitionBPMNFlowDiagramFile);
        Image diagramFileImage = new Image();
        diagramFileImage.setSource(res);
        graphViewLayout.removeAllComponents();
        graphViewLayout.addComponent(diagramFileImage);
        graphViewLayout.setComponentAlignment(diagramFileImage,Alignment.MIDDLE_CENTER);
    }

    private void generateBPMNFiles(){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        activityDefinitionBPMNFile=ActivitySpaceOperationUtil.getActivityTypeBMPNFile(activitySpaceName,activityDefinitionType);
        activityDefinitionBPMNFlowDiagramFile=ActivitySpaceOperationUtil.getActivityTypeBMPNFlowDiagram(activitySpaceName,activityDefinitionType);
    }

    public void refreshActivityDefinitionBPMNUI(){
        generateBPMNFiles();
        displayBPMNFileContent();
    }

    public void cleanActivityDefinitionProcessRelatedUIData(){
        this.containerActivityDefinitionDetailInfo.cleanActivityDefinitionProcessRelatedUIData();
    }

    public void setContainerActivityDefinitionDetailInfo(ActivityDefinitionDetailInfo containerActivityDefinitionDetailInfo) {
        this.containerActivityDefinitionDetailInfo = containerActivityDefinitionDetailInfo;
    }
}
