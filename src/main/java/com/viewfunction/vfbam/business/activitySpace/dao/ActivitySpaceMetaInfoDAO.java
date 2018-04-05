package com.viewfunction.vfbam.business.activitySpace.dao;

import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;

public class ActivitySpaceMetaInfoDAO {
    private Participant[] participants;
    private Role[] roles;
    private BusinessActivityDefinition[] businessActivityDefinitions;
    private RoleQueue[] roleQueues;
    private Roster[] rosters;
    private String[] businessCategories;
    private String[] extendFeatureCategories;

    public String[] getBusinessCategories() {
        return businessCategories;
    }

    public void setBusinessCategories(String[] businessCategories) {
        this.businessCategories = businessCategories;
    }

    public Participant[] getParticipants() {
        return participants;
    }

    public void setParticipants(Participant[] participants) {
        this.participants = participants;
    }

    public Role[] getRoles() {
        return roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    public BusinessActivityDefinition[] getBusinessActivityDefinitions() {
        return businessActivityDefinitions;
    }

    public void setBusinessActivityDefinitions(BusinessActivityDefinition[] businessActivityDefinitions) {
        this.businessActivityDefinitions = businessActivityDefinitions;
    }

    public RoleQueue[] getRoleQueues() {
        return roleQueues;
    }

    public void setRoleQueues(RoleQueue[] roleQueues) {
        this.roleQueues = roleQueues;
    }

    public Roster[] getRosters() {
        return rosters;
    }

    public void setRosters(Roster[] rosters) {
        this.rosters = rosters;
    }

    public String[] getExtendFeatureCategories() {
        return extendFeatureCategories;
    }

    public void setExtendFeatureCategories(String[] extendFeatureCategories) {
        this.extendFeatureCategories = extendFeatureCategories;
    }
}
