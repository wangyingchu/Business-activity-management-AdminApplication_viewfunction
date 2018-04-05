package com.viewfunction.vfbam.ui.component.activityManagement.util;

import java.util.List;

public class ActivityStepVO {
    private String activityStepName;
    private String activityStepDisplayName;
    private List<ActivityDataFieldVO> exposedActivityDataFields;
    private String relatedRole;
    private String userIdentityAttribute;
    private String[] stepProcessVariables;
    private String decisionPointAttribute;
    private String[] decisionPointChooseOption;

    public String getActivityStepName() {
        return activityStepName;
    }

    public void setActivityStepName(String activityStepName) {
        this.activityStepName = activityStepName;
    }

    public String getActivityStepDisplayName() {
        return activityStepDisplayName;
    }

    public void setActivityStepDisplayName(String activityStepDisplayName) {
        this.activityStepDisplayName = activityStepDisplayName;
    }

    public List<ActivityDataFieldVO> getExposedActivityDataFields() {
        return exposedActivityDataFields;
    }

    public void setExposedActivityDataFields(List<ActivityDataFieldVO> exposedActivityDataFields) {
        this.exposedActivityDataFields = exposedActivityDataFields;
    }

    public String getRelatedRole() {
        return relatedRole;
    }

    public void setRelatedRole(String relatedRole) {
        this.relatedRole = relatedRole;
    }

    public String getUserIdentityAttribute() {
        return userIdentityAttribute;
    }

    public void setUserIdentityAttribute(String userIdentityAttribute) {
        this.userIdentityAttribute = userIdentityAttribute;
    }

    public String[] getStepProcessVariables() {
        return stepProcessVariables;
    }

    public void setStepProcessVariables(String[] stepProcessVariables) {
        this.stepProcessVariables = stepProcessVariables;
    }

    public String getDecisionPointAttribute() {
        return decisionPointAttribute;
    }

    public void setDecisionPointAttribute(String decisionPointAttribute) {
        this.decisionPointAttribute = decisionPointAttribute;
    }

    public String[] getDecisionPointChooseOption() {
        return decisionPointChooseOption;
    }

    public void setDecisionPointChooseOption(String[] decisionPointChooseOption) {
        this.decisionPointChooseOption = decisionPointChooseOption;
    }
}
