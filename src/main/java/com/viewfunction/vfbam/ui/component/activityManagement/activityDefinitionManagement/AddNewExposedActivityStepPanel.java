package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.RoleVO;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AddNewExposedActivityStepPanel  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;

    private SectionActionsBar addExposedActivityStepActionsBar;
    private FormLayout form;
    private HorizontalLayout footer;
    private ComboBox activitySteps;
    private Button addButton;
    private Window containerDialog;
    private ComboBox relatedRoles;

    private List<ActivityStepVO> activityStepsList;
    private Map<String,ActivityStepVO> activityStepsInfoMap;
    private ActivityStepsEditor relatedActivityStepsEditor;
    private List<RoleVO> rolesList;
    private Map<String,RoleVO> rolesInfoMap;

    public AddNewExposedActivityStepPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setSpacing(true);
        setMargin(true);
        MainSectionTitle addNewStepSectionTitle=new MainSectionTitle(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ExposeActivityStepButtonLabel"));
        addComponent(addNewStepSectionTitle);

        activityStepsInfoMap=new HashMap<String, ActivityStepVO>();
        rolesInfoMap=new HashMap<String, RoleVO>();

        addExposedActivityStepActionsBar=new SectionActionsBar(new Label(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_DefinitionText")+" : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(addExposedActivityStepActionsBar);

        form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        activitySteps = new ComboBox(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ExposedStepsText"));
        activitySteps.setRequired(true);
        activitySteps.setWidth("100%");
        activitySteps.setTextInputAllowed(false);
        activitySteps.setNullSelectionAllowed(false);
        activitySteps.setInputPrompt(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_PleaseSelectStepText"));
        form.addComponent(activitySteps);

        relatedRoles = new ComboBox(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_StepRelatedRoleText"));
        relatedRoles.setRequired(false);
        relatedRoles.setWidth("100%");
        relatedRoles.setTextInputAllowed(false);
        relatedRoles.setNullSelectionAllowed(true);
        relatedRoles.setInputPrompt(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_PleaseSelectRoleText"));
        form.addComponent(relatedRoles);

        footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        addButton=new Button(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_ExposeStepButtonLabel"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new data field logic */
                addNewExposedActivityStep();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
    }

    public void attach() {
        super.attach();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String componentId=currentActivitySpaceComponentInfo.getComponentId();
        Label sectionActionBarLabel=new Label(userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_DefinitionText")+" : <b>"+componentId+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
        addExposedActivityStepActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        if(activityStepsList!=null){
            for(ActivityStepVO currentActivityStepVO:activityStepsList){
                String activityStepCombinationStr=currentActivityStepVO.getActivityStepName()+" ("+currentActivityStepVO.getActivityStepDisplayName()+")";
                activitySteps.addItem(activityStepCombinationStr);
                activityStepsInfoMap.put(activityStepCombinationStr, currentActivityStepVO);
            }
        }
        rolesList=relatedActivityStepsEditor.getRolesList();
        if(rolesList!=null){
            for(RoleVO currentRoleVO:rolesList){
                String roleCombinationStr=currentRoleVO.getRoleName()+" ("+currentRoleVO.getRoleDisplayName()+")";
                relatedRoles.addItem(roleCombinationStr);
                rolesInfoMap.put(roleCombinationStr, currentRoleVO);
            }
        }
    }

    public void setActivityStepsList(List<ActivityStepVO> activityStepsList) {
        this.activityStepsList = activityStepsList;
    }

    public void addNewExposedActivityStep(){
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        if(activitySteps.getValue()==null){
            Notification errorNotification = new Notification(userI18NProperties.
                    getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_ActivityTypeManagement_PleaseSelectStepToExposeText"), Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }else{
            String activityStepCombinationStr=activitySteps.getValue().toString();
            ActivityStepVO targetActivityStepVO= activityStepsInfoMap.get(activityStepCombinationStr);
            if(this.relatedActivityStepsEditor.getExistActivityStepByName(targetActivityStepVO.getActivityStepName())!=null){
                Notification errorNotification = new Notification(userI18NProperties.
                        getProperty("Global_Application_DataOperation_DataValidateErrorText"),
                        userI18NProperties.
                                getProperty("ActivityManagement_ActivityTypeManagement_StepAlreadyExposedText"), Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
            }else{
                String relatedRole=null;
                if(relatedRoles.getValue()!=null){
                    String roleCombinationStr=relatedRoles.getValue().toString();
                    RoleVO relatedRoleVO=rolesInfoMap.get(roleCombinationStr);
                    if(relatedRoleVO!=null){
                        relatedRole=relatedRoleVO.getRoleName();
                    }
                }
                this.relatedActivityStepsEditor.addNewExposedActivityStep(targetActivityStepVO.getActivityStepName(),relatedRole);
                if(this.containerDialog!=null){
                    this.containerDialog.close();
                }
            }
        }
    }

    public void setRelatedActivityStepsEditor(ActivityStepsEditor relatedActivityStepsEditor) {
        this.relatedActivityStepsEditor = relatedActivityStepsEditor;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }
}
