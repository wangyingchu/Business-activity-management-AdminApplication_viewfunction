package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement.ActivityDefinitionsActionTable;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;
import java.util.Set;

public class RosterRelatedActivityDefinitionsSelector extends VerticalLayout {
    private Window containerDialog;
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar relatedToActivityDefinitionsSectionActionsBar;
    private Roster roster;
    private TwinColSelect rosterActivityDefinitionsSelect;
    private ActivityDefinitionsActionTable relatedActivityDefinitionsActionTable;

    public RosterRelatedActivityDefinitionsSelector(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle relatedToRoleQueuesSectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_ActivityTyeRosterContainsText"));
        addComponent(relatedToRoleQueuesSectionTitle);

        relatedToActivityDefinitionsSectionActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_RosterManagement_RosterText")+" : <b>"+""+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(relatedToActivityDefinitionsSectionActionsBar);

        rosterActivityDefinitionsSelect = new TwinColSelect();
        rosterActivityDefinitionsSelect.setLeftColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AvailableActivityTypesText"));
        rosterActivityDefinitionsSelect.setRightColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_RelatedActivityTypesText"));
        rosterActivityDefinitionsSelect.setNewItemsAllowed(false);
        rosterActivityDefinitionsSelect.setWidth("100%");
        rosterActivityDefinitionsSelect.setHeight("270px");
        addComponent(rosterActivityDefinitionsSelect);

        HorizontalLayout actionButtonsContainer=new HorizontalLayout();
        actionButtonsContainer.setSpacing(true);
        actionButtonsContainer.setMargin(true);
        actionButtonsContainer.setWidth("100%");
        addComponent(actionButtonsContainer);

        Button confirmUpdateButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_UpdateButtonLabel"));
        confirmUpdateButton.setIcon(FontAwesome.SAVE);
        confirmUpdateButton.addStyleName("small");
        confirmUpdateButton.addStyleName("primary");
        actionButtonsContainer.addComponent(confirmUpdateButton);
        actionButtonsContainer.setComponentAlignment(confirmUpdateButton, Alignment.MIDDLE_RIGHT);
        actionButtonsContainer.setExpandRatio(confirmUpdateButton, 1L);

        String rosterName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        final Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_RosterManagement_ConfirmUpdateRosterActivityTypeText")+
                " <b>"+rosterName +"</b>.", ContentMode.HTML);
        final RosterRelatedActivityDefinitionsSelector self=this;
        confirmUpdateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final ConfirmDialog modifyRosterRelatedActivityDefinitionsConfirmDialog = new ConfirmDialog();
                modifyRosterRelatedActivityDefinitionsConfirmDialog.setConfirmMessage(confirmMessage);

                Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        self.doUpdateRosterRelatedActivityDefinitionsInfo();
                        //close confirm dialog
                        modifyRosterRelatedActivityDefinitionsConfirmDialog.close();
                        //close self dialog window
                        if(self.containerDialog!=null){
                            self.containerDialog.close();
                        }
                    }
                };
                modifyRosterRelatedActivityDefinitionsConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
                UI.getCurrent().addWindow(modifyRosterRelatedActivityDefinitionsConfirmDialog);
            }
        });

        Button cancelUpdateButton = new Button(userI18NProperties.
                getProperty("ActivityManagement_Common_CancelButtonLabel"));
        cancelUpdateButton.setIcon(FontAwesome.TIMES);
        cancelUpdateButton.addStyleName("small");
        actionButtonsContainer.addComponent(cancelUpdateButton);
        actionButtonsContainer.setComponentAlignment(cancelUpdateButton, Alignment.MIDDLE_RIGHT);
        cancelUpdateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close self dialog window
                if(self.containerDialog!=null){
                    self.containerDialog.close();
                }
            }
        });
    }

    public void doUpdateRosterRelatedActivityDefinitionsInfo(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        Set rosterActivityTypesSet=(Set)rosterActivityDefinitionsSelect.getValue();
        String[] strActivityTypesArray = new String[rosterActivityTypesSet.size()];
        String[] roleCombinationStrArray=(String[]) rosterActivityTypesSet.toArray(strActivityTypesArray);
        boolean updateRosterRelatedActivityTypeResult=ActivitySpaceOperationUtil.updateRosterActivityTypes(this.roster,roleCombinationStrArray);
        if(updateRosterRelatedActivityTypeResult){
            if(this.relatedActivityDefinitionsActionTable !=null){
                this.relatedActivityDefinitionsActionTable.loadDefinitionsData();
            }
            Notification resultNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_RosterManagement_UpdateRosterSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }else{
            Notification errorNotification = new Notification( userI18NProperties.
                    getProperty("ActivityManagement_RosterManagement_UpdateRosterErrorText"),
                    userI18NProperties.
                            getProperty("Global_Application_DataOperation_ServerSideErrorOccurredText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    public void attach() {
        super.attach();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        rosterActivityDefinitionsSelect.clear();
        rosterActivityDefinitionsSelect.removeAllItems();
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            String participantName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
            Label sectionActionBarLabel=new Label(userI18NProperties.
                    getProperty("ActivityManagement_RosterManagement_RosterText")+" : <b>"+participantName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            relatedToActivityDefinitionsSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        ActivitySpaceMetaInfoDAO activitySpaceMetaInfoDAO=
                ActivitySpaceOperationUtil.getActivitySpaceMetaInfo(activitySpaceName, new String[]{ActivitySpaceOperationUtil.ACTIVITYSPACE_METAINFOTYPE_ACTIVITYTYPE});
        BusinessActivityDefinition[] activityTypes=activitySpaceMetaInfoDAO.getBusinessActivityDefinitions();
        if(activityTypes!=null){
            for(BusinessActivityDefinition currentBusinessActivityDefinition:activityTypes){
                String roleCombinationStr=currentBusinessActivityDefinition.getActivityType();
                rosterActivityDefinitionsSelect.addItem(roleCombinationStr);
            }
        }
        if(this.roster!=null){
            String[] containedActivityTypes=ActivitySpaceOperationUtil.getActivityTypesByRoster(this.roster);
            if(containedActivityTypes!=null){
                for(String currentTypes:containedActivityTypes){
                    rosterActivityDefinitionsSelect.select(currentTypes);
                }
            }
        }
    }

    public void setRoster(Roster roster) {
        this.roster = roster;
    }

    public void setRelatedActivityDefinitionsActionTable(ActivityDefinitionsActionTable relatedActivityDefinitionsActionTable) {
        this.relatedActivityDefinitionsActionTable = relatedActivityDefinitionsActionTable;
    }
}
