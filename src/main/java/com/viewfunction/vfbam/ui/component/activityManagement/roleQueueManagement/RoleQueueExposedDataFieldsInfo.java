package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityDataFieldsActionTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RoleQueueExposedDataFieldsInfo  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar exposedDataFieldsSectionActionsBar;
    private String roleQueueName;
    public RoleQueueExposedDataFieldsInfo(UserClientInfo currentUserClientInfo,String roleQueueName){
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        this.roleQueueName=roleQueueName;
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle belongsToRolesSectionTitle=new SecondarySectionTitle(userI18NProperties.
                getProperty("ActivityManagement_RoleQueuesManagement_DataFilterText"));
        addComponent(belongsToRolesSectionTitle);

        exposedDataFieldsSectionActionsBar=new SectionActionsBar(
                new Label(userI18NProperties.
                        getProperty("ActivityManagement_RoleQueuesManagement_RoleQueueText")+" : <b>"+this.roleQueueName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(exposedDataFieldsSectionActionsBar);

        ActivityDataFieldsActionTable activityDataFieldsActionTable =new ActivityDataFieldsActionTable(this.currentUserClientInfo,"300px",false,true);
        activityDataFieldsActionTable.setDataFieldQueryType(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROLEQUEUE);
        activityDataFieldsActionTable.setDataFieldsQueryId(this.roleQueueName);
        addComponent(activityDataFieldsActionTable);
    }

    @Override
    public void attach() {
        super.attach();
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        if(currentActivitySpaceComponentInfo!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label sectionActionBarLabel=new Label(userI18NProperties.
                    getProperty("ActivityManagement_RoleQueuesManagement_RoleQueueText")+" : <b>"+this.roleQueueName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            exposedDataFieldsSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}
