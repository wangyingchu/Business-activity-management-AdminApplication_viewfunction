package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityDataFieldsActionTable;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityDataFieldsEditor;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityManagementConst;
import com.viewfunction.vfbam.ui.component.activityManagement.util.*;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil.*;

public class ActivityDefinitionDetailInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo;

    private Tab activityDefinitionInfoTab;
    private Tab activityDefinitionDataFieldTab;
    private Tab activityDefinitionStepTab;
    private Tab activityDefinitionBPMNTab;
    private TabSheet tabs;

    private ActivityDefinitionEditor activityDefinitionEditor;
    private ActivityStepsEditor activityStepsEditor;
    private ActivityDataFieldsEditor activityDataFieldsEditor;
    private ActivityDefinitionBPMNEditor activityDefinitionBPMNEditor;

    public ActivityDefinitionDetailInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        setSpacing(false);
        setMargin(true);

        tabs=new TabSheet();
        addComponent(tabs);
        VerticalLayout activityDefinitionInfoLayout=new VerticalLayout();
        activityDefinitionInfoTab =tabs.addTab(activityDefinitionInfoLayout, userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeInfoText"));
        activityDefinitionInfoTab.setIcon(FontAwesome.INFO);
        MainSectionTitle mainSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeInfoText"));
        activityDefinitionInfoLayout.addComponent(mainSectionTitle);
        activityDefinitionEditor=new ActivityDefinitionEditor(this.currentUserClientInfo);
        activityDefinitionInfoLayout.addComponent(activityDefinitionEditor);

        int browserWindowHeight= UI.getCurrent().getPage().getBrowserWindowHeight();
        String tableHeightString=""+(browserWindowHeight-330)+"px";

        VerticalLayout activityDefinitionDataFieldLayout=new VerticalLayout();
        activityDefinitionDataFieldTab =tabs.addTab(activityDefinitionDataFieldLayout, userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeDataFieldText"));
        activityDefinitionDataFieldTab.setIcon(FontAwesome.TH_LIST);
        MainSectionTitle activityDefinitionDataFieldsSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeDataFieldText"));
        activityDefinitionDataFieldLayout.addComponent(activityDefinitionDataFieldsSectionTitle);
        activityDataFieldsEditor=new ActivityDataFieldsEditor(this.currentUserClientInfo,
                ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION,currentActivitySpaceComponentInfo.getComponentId(),tableHeightString);
        activityDefinitionDataFieldLayout.addComponent(activityDataFieldsEditor);

        VerticalLayout activityDefinitionStepLayout=new VerticalLayout();
        activityDefinitionStepTab =tabs.addTab(activityDefinitionStepLayout, userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeStepText"));
        activityDefinitionStepTab.setIcon(FontAwesome.SLIDERS);
        MainSectionTitle activityDefinitionStepsSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeStepText"));
        activityDefinitionStepLayout.addComponent(activityDefinitionStepsSectionTitle);
        activityStepsEditor=new ActivityStepsEditor(this.currentUserClientInfo);
        activityDefinitionStepLayout.addComponent(activityStepsEditor);

        VerticalLayout activityDefinitionBPMNLayout=new VerticalLayout();
        activityDefinitionBPMNTab =tabs.addTab(activityDefinitionBPMNLayout, userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeDefineText"));
        activityDefinitionBPMNTab.setIcon(FontAwesome.STEAM_SQUARE);
        MainSectionTitle _BPMNSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ActivityTypeDefineText"));
        activityDefinitionBPMNLayout.addComponent(_BPMNSectionTitle);
        activityDefinitionBPMNEditor=new ActivityDefinitionBPMNEditor(this.currentUserClientInfo);
        activityDefinitionBPMNEditor.setContainerActivityDefinitionDetailInfo(this);
        activityDefinitionBPMNLayout.addComponent(activityDefinitionBPMNEditor);

        VerticalLayout activityDefinitionAdditionalMetaDataConfigLayout=new VerticalLayout();
        Tab additionalConfigurationTab=tabs.addTab(activityDefinitionAdditionalMetaDataConfigLayout, userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AdditionalConfigurationText"));
        additionalConfigurationTab.setIcon(VaadinIcons.FORM);
        MainSectionTitle _AdditionalConfigTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_AdditionalConfigurationText"));
        activityDefinitionAdditionalMetaDataConfigLayout.addComponent(_AdditionalConfigTitle);
        ActivityAdditionalConfigurationEditor activityAdditionalConfigurationEditor=new ActivityAdditionalConfigurationEditor(this.currentUserClientInfo);
        activityDefinitionAdditionalMetaDataConfigLayout.addComponent(activityAdditionalConfigurationEditor);

        loadActivityDefinitionData();
    }

    @Override
    public void attach() {
        super.attach();
        tabs.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                if (activityDefinitionInfoTab.getComponent()==tabs.getSelectedTab()) {
                    //need refresh activityDefinitionEditor
                    activityDefinitionEditor.refreshUIData();
                }
                if (activityDefinitionStepTab.getComponent()==tabs.getSelectedTab()) {
                    //need refresh activityStepsEditor
                    activityStepsEditor.refreshUIData();
                }
            }
        });
    }

    private void loadActivityDefinitionData(){
        String activityType=currentActivitySpaceComponentInfo.getComponentId();
        String activitySpaceName=currentActivitySpaceComponentInfo.getActivitySpaceName();
        //init ActivitySpace data
        ActivitySpaceMetaInfoDAO metaInfoDAO= getActivitySpaceMetaInfo(activitySpaceName,
                new String[]{ACTIVITYSPACE_METAINFOTYPE_ROLE,ACTIVITYSPACE_METAINFOTYPE_ROSTER,
                        ACTIVITYSPACE_METAINFOTYPE_PARTICIPANT,ACTIVITYSPACE_METAINFOTYPE_BUSINESSCATEGORY});

        Participant[] participantsArray=metaInfoDAO.getParticipants();
        List<ParticipantVO> participantsList=new ArrayList<ParticipantVO>();
        if(participantsArray!=null){
            for(Participant currentParticipant:participantsArray){
                ParticipantVO currentParticipantVO=new ParticipantVO();
                currentParticipantVO.setParticipantName(currentParticipant.getParticipantName());
                currentParticipantVO.setParticipantDisplayName(currentParticipant.getDisplayName());
                participantsList.add(currentParticipantVO);
            }
        }
        activityDefinitionEditor.setParticipantsList(participantsList);

        Role[] roleArray=metaInfoDAO.getRoles();
        List<RoleVO> rolesList=new ArrayList<RoleVO>();
        if(roleArray!=null){
            for(Role currentRole:roleArray){
                RoleVO currentRoleVO=new RoleVO();
                currentRoleVO.setRoleName(currentRole.getRoleName());
                currentRoleVO.setRoleDisplayName(currentRole.getDisplayName());
                rolesList.add(currentRoleVO);
            }
        }
        activityDefinitionEditor.setRolesList(rolesList);
        activityStepsEditor.setRolesList(rolesList);
        activityStepsEditor.getActivityStepItemsActionList().setRolesList(rolesList);

        Roster[] rosterArray=metaInfoDAO.getRosters();
        List<RosterVO> rostersList=new ArrayList<RosterVO>();
        if(rosterArray!=null){
            for(Roster currentRoster:rosterArray){
                RosterVO currentRosterVO=new RosterVO();
                currentRosterVO.setRosterName(currentRoster.getRosterName());
                currentRosterVO.setRosterDisplayName(currentRoster.getDisplayName());
                rostersList.add(currentRosterVO);
            }
        }
        activityDefinitionEditor.setRostersList(rostersList);

        String[] businessCategories=metaInfoDAO.getBusinessCategories();
        activityDefinitionEditor.setBusinessCategories(businessCategories);

        //init ActivityType data
        List<DataFieldDefinition> dataFieldDefinitionList=new ArrayList<DataFieldDefinition>();
        List<ActivityDataFieldVO> activityDataFieldsList=new ArrayList<ActivityDataFieldVO>();

        DataFieldDefinition[] activityTypeFieldDefinitions=
                getDataFieldDefinitions(activitySpaceName, activityType, ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ACTIVITYTYPE);
        if(activityTypeFieldDefinitions!=null){
            for(DataFieldDefinition currentDataFieldDefinition:activityTypeFieldDefinitions){
                dataFieldDefinitionList.add(currentDataFieldDefinition);
                ActivityDataFieldVO currentActivityDataFieldVO=new ActivityDataFieldVO();
                currentActivityDataFieldVO.setDataFieldDisplayName(currentDataFieldDefinition.getDisplayName());
                currentActivityDataFieldVO.setDescription(currentDataFieldDefinition.getDescription());
                currentActivityDataFieldVO.setDataFieldName(currentDataFieldDefinition.getFieldName());
                String dataFieldType=ActivitySpaceOperationUtil.getDataFieldDefinitionTypeString(currentDataFieldDefinition.getFieldType());
                currentActivityDataFieldVO.setDataType(dataFieldType);
                currentActivityDataFieldVO.setArrayField(currentDataFieldDefinition.isArrayField());
                currentActivityDataFieldVO.setMandatoryField(currentDataFieldDefinition.isMandatoryField());
                currentActivityDataFieldVO.setReadableField(currentDataFieldDefinition.isReadableField());
                currentActivityDataFieldVO.setSystemField(currentDataFieldDefinition.isSystemField());
                currentActivityDataFieldVO.setWritableField(currentDataFieldDefinition.isWriteableField());
                activityDataFieldsList.add(currentActivityDataFieldVO);
            }
        }
        activityDataFieldsEditor.getActivityDataFieldsActionTable().setDataFieldDefinitionsList(dataFieldDefinitionList);
        activityDataFieldsEditor.getActivityDataFieldsActionTable().setActivityDataFieldsList(activityDataFieldsList);
        activityDefinitionEditor.setActivityDataFieldsList(activityDataFieldsList);
        activityDefinitionEditor.getActivityExposedDataFieldsEditor().setActivityDataFieldsList(activityDataFieldsList);
        activityStepsEditor.setActivityDataFieldsList(activityDataFieldsList);
        activityStepsEditor.getActivityStepItemsActionList().setActivityDataFieldsList(activityDataFieldsList);
    }

    public void cleanActivityDefinitionProcessRelatedUIData(){
        activityStepsEditor.cleanActivityDefinitionProcessRelatedUIData();
        activityDefinitionEditor.cleanActivityDefinitionProcessRelatedUIData();
    }
}
