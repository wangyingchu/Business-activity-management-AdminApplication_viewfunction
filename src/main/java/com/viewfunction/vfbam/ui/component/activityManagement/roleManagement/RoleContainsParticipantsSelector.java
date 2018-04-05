package com.viewfunction.vfbam.ui.component.activityManagement.roleManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.activityManagement.participantManagement.ParticipantsActionTable;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class RoleContainsParticipantsSelector extends VerticalLayout {
    private Window containerDialog;
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar containedParticipantsSectionActionsBar;
    private Role role;
    private TwinColSelect roleParticipantsSelect;
    private Map<String,Participant> participantsInfoMap;
    private ParticipantsActionTable relatedParticipantsActionTable;
    public RoleContainsParticipantsSelector(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle containedParticipantsSectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RolesManagement_ParticipantsInRoleText"));
        addComponent(containedParticipantsSectionTitle);
        participantsInfoMap=new HashMap<String,Participant>();
        containedParticipantsSectionActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_RolesManagement_RoleText")+" : <b>"+""+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(containedParticipantsSectionActionsBar);

        roleParticipantsSelect = new TwinColSelect();
        roleParticipantsSelect.setLeftColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_AvailableParticipantsText"));
        roleParticipantsSelect.setRightColumnCaption(" "+userI18NProperties.
                getProperty("ActivityManagement_ParticipantsManagement_ContainsParticipantsText"));
        roleParticipantsSelect.setNewItemsAllowed(false);
        roleParticipantsSelect.setWidth("100%");
        roleParticipantsSelect.setHeight("270px");
        addComponent(roleParticipantsSelect);

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

        String roleName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        final Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+" "+userI18NProperties.
                getProperty("ActivityManagement_RolesManagement_ConfirmUpdateParticipantsText")+
                " <b>"+roleName +"</b>.", ContentMode.HTML);
        final RoleContainsParticipantsSelector self=this;
        confirmUpdateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final ConfirmDialog modifyRoleContainedParticipantsConfirmDialog = new ConfirmDialog();
                modifyRoleContainedParticipantsConfirmDialog.setConfirmMessage(confirmMessage);

                Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        self.doUpdateRoleContainedParticipantsInfo();
                        //close confirm dialog
                        modifyRoleContainedParticipantsConfirmDialog.close();
                        //close self dialog window
                        if(self.containerDialog!=null){
                            self.containerDialog.close();
                        }
                    }
                };
                modifyRoleContainedParticipantsConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
                UI.getCurrent().addWindow(modifyRoleContainedParticipantsConfirmDialog);
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

    public void doUpdateRoleContainedParticipantsInfo(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        Set roleParticipantsSet=(Set)roleParticipantsSelect.getValue();
        String[] strRoleParticipantArray = new String[roleParticipantsSet.size()];
        String[] participantCombinationStrArray=(String[]) roleParticipantsSet.toArray(strRoleParticipantArray);
        String[] newRoleParticipantsArray=new String[participantCombinationStrArray.length];
        for(int i=0;i<participantCombinationStrArray.length;i++){
            String participantName=participantsInfoMap.get(participantCombinationStrArray[i]).getParticipantName();
            newRoleParticipantsArray[i]=participantName;
        }
        boolean updateParticipantsResult=ActivitySpaceOperationUtil.updateRoleParticipants(this.role, newRoleParticipantsArray);
        if(updateParticipantsResult){
            if(this.relatedParticipantsActionTable!=null){
                this.relatedParticipantsActionTable.loadParticipantsData();
            }
            Notification resultNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_UpdateDataSuccessText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_RolesManagement_UpdateRoleInfoSuccessText"), Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }else{
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("ActivityManagement_RolesManagement_UpdateRoleInfoErrorText"),
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
        roleParticipantsSelect.clear();
        roleParticipantsSelect.removeAllItems();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            String participantName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
            Label sectionActionBarLabel=new Label(userI18NProperties.
                    getProperty("ActivityManagement_RolesManagement_RoleText")+" : <b>"+participantName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            containedParticipantsSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        ActivitySpaceMetaInfoDAO activitySpaceMetaInfoDAO=
                ActivitySpaceOperationUtil.getActivitySpaceMetaInfo(activitySpaceName, new String[]{ActivitySpaceOperationUtil.ACTIVITYSPACE_METAINFOTYPE_PARTICIPANT});
        Participant[] participants=activitySpaceMetaInfoDAO.getParticipants();
        if(participants!=null){
            for(Participant currentParticipant:participants){
                String participantCombinationStr=currentParticipant.getParticipantName()+" ("+currentParticipant.getDisplayName()+")";
                roleParticipantsSelect.addItem(participantCombinationStr);
                participantsInfoMap.put(participantCombinationStr,currentParticipant);
            }
        }
        if(this.role!=null){
            Participant[] participantsOfRole=ActivitySpaceOperationUtil.getParticipantsByRole(this.role);
            if(participantsOfRole!=null){
                for(Participant currentParticipant:participantsOfRole){
                    String participantCombinationStr=currentParticipant.getParticipantName()+" ("+currentParticipant.getDisplayName()+")";
                    roleParticipantsSelect.select(participantCombinationStr);
                }
            }
        }
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRelatedParticipantsActionTable(ParticipantsActionTable relatedParticipantsActionTable) {
        this.relatedParticipantsActionTable = relatedParticipantsActionTable;
    }
}
