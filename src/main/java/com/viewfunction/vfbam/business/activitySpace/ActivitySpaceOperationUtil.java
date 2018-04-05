package com.viewfunction.vfbam.business.activitySpace;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivity;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityBureauImpl.CCRBusinessActivityDefinitionImpl;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.activityEngine.activityView.common.*;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityDataFieldsActionTable;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.util.ApplicationConstant;
import com.viewfunction.vfbam.ui.util.RuntimeEnvironmentUtil;
import com.viewfunction.vfbam.ui.util.StringUnicodeSerializer;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;


import java.io.*;
import java.util.*;

import javax.jcr.PropertyType;

public class ActivitySpaceOperationUtil {

    public static final String ACTIVITYSPACE_METAINFOTYPE_PARTICIPANT="PARTICIPANT";
    public static final String ACTIVITYSPACE_METAINFOTYPE_ROLE="ROLE";
    public static final String ACTIVITYSPACE_METAINFOTYPE_ROLEQUEUE="ROLEQUEUE";
    public static final String ACTIVITYSPACE_METAINFOTYPE_ROSTER="ROSTER";
    public static final String ACTIVITYSPACE_METAINFOTYPE_ACTIVITYTYPE="ACTIVITYTYPE";
    public static final String ACTIVITYSPACE_METAINFOTYPE_BUSINESSCATEGORY="BUSINESSCATEGORY";
    public static final String ACTIVITYSPACE_METAINFOTYPE_EXTENDFEATURECATEGORY="EXTENDFEATURECATEGORY";

    public static final String ExistedDataHandleMethod_IGNORE="IGNORE";
    public static final String ExistedDataHandleMethod_REPLACE="REPLACE";

    public static String[] listActivitySpaces(){
        try {
            ActivitySpace[] activitySpaceArray=ActivityComponentFactory.getActivitySpaces();
            if(activitySpaceArray!=null){
                String[] activitySpaceNameArray=new String[activitySpaceArray.length];
                for(int i=0;i<activitySpaceArray.length;i++){
                    ActivitySpace currentActivitySpace=activitySpaceArray[i];
                    activitySpaceNameArray[i]=currentActivitySpace.getActivitySpaceName();
                }
                return activitySpaceNameArray;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkActivitySpaceExistance(String activitySpaceName){
        try {
            ActivitySpace[] activitySpaceArray=ActivityComponentFactory.getActivitySpaces();
            if(activitySpaceArray!=null){
                for(ActivitySpace currentSpace :activitySpaceArray){
                    if(currentSpace.getActivitySpaceName().equals(activitySpaceName)){
                        return true;
                    }

                }
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createActivitySpace(String activitySpaceName){
        try {
            ActivitySpace newActivitySpace=ActivityComponentFactory.createActivitySpace(activitySpaceName);
            if(newActivitySpace!=null&&newActivitySpace.getActivitySpaceName().equals(activitySpaceName)){
                return true;
            }else{
                return false;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setActivitySpaceBusinessCategories(String activitySpaceName,String[] categories){
        if(categories==null){
            return false;
        }
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            return targetActivitySpace.setActivityTypeCategories(categories);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setActivitySpaceExtendFeatureCategories(String activitySpaceName,String[] categories){
        if(categories==null){
            return false;
        }
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            return targetActivitySpace.setActivitySpaceExtendFeatureCategories(categories);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ActivitySpaceMetaInfoDAO getActivitySpaceMetaInfo(String activitySpaceName,String[] metaInfoType){
        ActivitySpaceMetaInfoDAO activitySpaceMetaInfoDAO=new ActivitySpaceMetaInfoDAO();
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            if(metaInfoType!=null){
                for(String currentType:metaInfoType){
                    if(ACTIVITYSPACE_METAINFOTYPE_PARTICIPANT.equals(currentType)){
                        Participant[] participants=targetActivitySpace.getParticipants();
                        activitySpaceMetaInfoDAO.setParticipants(participants);
                    }
                    if(ACTIVITYSPACE_METAINFOTYPE_ROLE.equals(currentType)){
                        Role[] roles=targetActivitySpace.getRoles();
                        activitySpaceMetaInfoDAO.setRoles(roles);
                    }
                    if(ACTIVITYSPACE_METAINFOTYPE_ROLEQUEUE.equals(currentType)){
                        RoleQueue[] roleQueues=targetActivitySpace.getRoleQueues();
                        activitySpaceMetaInfoDAO.setRoleQueues(roleQueues);
                    }
                    if(ACTIVITYSPACE_METAINFOTYPE_ROSTER.equals(currentType)){
                        Roster[] rosters=targetActivitySpace.getRosters();
                        activitySpaceMetaInfoDAO.setRosters(rosters);
                    }
                    if(ACTIVITYSPACE_METAINFOTYPE_ACTIVITYTYPE.equals(currentType)){
                        BusinessActivityDefinition[] businessActivityDefinitions=targetActivitySpace.getBusinessActivityDefinitions();
                        activitySpaceMetaInfoDAO.setBusinessActivityDefinitions(businessActivityDefinitions);
                    }
                    if(ACTIVITYSPACE_METAINFOTYPE_BUSINESSCATEGORY.equals(currentType)){
                        String[] spaceBusinessCategories=targetActivitySpace.getActivityTypeCategories();
                        activitySpaceMetaInfoDAO.setBusinessCategories(spaceBusinessCategories);
                    }
                    if(ACTIVITYSPACE_METAINFOTYPE_EXTENDFEATURECATEGORY.equals(currentType)){
                        String[] spaceExtendFeatureCategories=targetActivitySpace.getActivitySpaceExtendFeatureCategories();
                        activitySpaceMetaInfoDAO.setExtendFeatureCategories(spaceExtendFeatureCategories);
                    }
                }
            }else{
                Participant[] participants=targetActivitySpace.getParticipants();
                activitySpaceMetaInfoDAO.setParticipants(participants);
                Role[] roles=targetActivitySpace.getRoles();
                activitySpaceMetaInfoDAO.setRoles(roles);
                BusinessActivityDefinition[] businessActivityDefinitions=targetActivitySpace.getBusinessActivityDefinitions();
                activitySpaceMetaInfoDAO.setBusinessActivityDefinitions(businessActivityDefinitions);
                RoleQueue[] roleQueues=targetActivitySpace.getRoleQueues();
                activitySpaceMetaInfoDAO.setRoleQueues(roleQueues);
                Roster[] rosters=targetActivitySpace.getRosters();
                activitySpaceMetaInfoDAO.setRosters(rosters);
                String[] spaceBusinessCategories=targetActivitySpace.getActivityTypeCategories();
                activitySpaceMetaInfoDAO.setBusinessCategories(spaceBusinessCategories);
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return activitySpaceMetaInfoDAO;
    }

    public static Participant getParticipantByName(String activitySpaceName,String participantName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            Participant targetParticipant=targetActivitySpace.getParticipant(participantName);
            return targetParticipant;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Participant[] getParticipantsByRoleName(String activitySpaceName,String roleName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            Role targetRole=targetActivitySpace.getRole(roleName);
            if(targetRole!=null){
                Participant[] targetParticipants=targetRole.getParticipants();
                return targetParticipants;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Participant[] getParticipantsByRole(Role targetRole){
        try {
            if(targetRole!=null){
                Participant[] targetParticipants=targetRole.getParticipants();
                return targetParticipants;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addNewParticipant(String activitySpaceName,String participantName,String participantDisplayName,String participantType){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            String newParticipantType=Participant.PARTICIPANT_TYPE_USER;
            if(participantType.equals("User")){
                newParticipantType=Participant.PARTICIPANT_TYPE_USER;
            }
            if(participantType.equals("Group")){
                newParticipantType=Participant.PARTICIPANT_TYPE_GROUP;
            }
            Participant targetParticipant=ActivityComponentFactory.createParticipant(participantName,newParticipantType,activitySpaceName);
            targetParticipant.setDisplayName(participantDisplayName);
            return targetActivitySpace.addParticipant(targetParticipant);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateParticipant(Participant participantToUpdate,String participantType){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(participantToUpdate.getActivitySpaceName());
            String newParticipantType=Participant.PARTICIPANT_TYPE_USER;
            if(participantType.equals("User")){
                newParticipantType=Participant.PARTICIPANT_TYPE_USER;
            }
            if(participantType.equals("Group")){
                newParticipantType=Participant.PARTICIPANT_TYPE_GROUP;
            }
            String[] newRolesArray=null;
            Role[] currentRoles=participantToUpdate.getRoles();
            if(currentRoles!=null){
                newRolesArray=new String[currentRoles.length];
                for(int i=0;i<currentRoles.length;i++){
                    newRolesArray[i]=currentRoles[i].getRoleName();
                }
            }else{
                newRolesArray=new String[0];
            }
            Participant updatedParticipant= targetActivitySpace.updateParticipant(
                    participantToUpdate.getParticipantName(),participantToUpdate.getDisplayName(),newParticipantType,newRolesArray);
            if(updatedParticipant!=null){
                return true;
            }else{
                return false;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateParticipantRoles(Participant participantToUpdate,String[] roleNames){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(participantToUpdate.getActivitySpaceName());
            String newParticipantType;
            if(participantToUpdate.isGroup()){
                newParticipantType=Participant.PARTICIPANT_TYPE_GROUP;
            }else{
                newParticipantType=Participant.PARTICIPANT_TYPE_USER;
            }
            String[] newRolesArray=(roleNames!=null)?roleNames:new String[0];
            Participant updatedParticipant= targetActivitySpace.updateParticipant(
                    participantToUpdate.getParticipantName(),participantToUpdate.getDisplayName(),newParticipantType,newRolesArray);
            if(updatedParticipant!=null){
                return true;
            }else{
                return false;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<ParticipantTask> getParticipantWorkTasks(String activitySpaceName,String participantName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            Participant targetParticipant=targetActivitySpace.getParticipant(participantName);
            if(targetParticipant!=null){
                List<ParticipantTask> targetWorkTasks=targetParticipant.fetchParticipantTasks();
                return targetWorkTasks;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ActivityStep> getRoleQueueActivitySteps(String activitySpaceName,String roleQueueName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            RoleQueue roleQueue=targetActivitySpace.getRoleQueue(roleQueueName);
            List<ActivityStep> activityStepList=roleQueue.fetchActivitySteps();
            if(activityStepList!=null){
                return activityStepList;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Role getRoleByName(String activitySpaceName,String roleName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            Role targetRole=targetActivitySpace.getRole(roleName);
            return targetRole;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addNewRole(String activitySpaceName,String roleName,String roleDisplayName,String roleDescription){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            Role targetRole=ActivityComponentFactory.createRole(activitySpaceName,roleName);
            targetRole.setDisplayName(roleDisplayName);
            if(roleDescription!=null){
                targetRole.setDescription(roleDescription);
            }
            return targetActivitySpace.addRole(targetRole);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateRole(String activitySpaceName,String roleName,String roleDisplayName,String roleDescription){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            Role targetRole=targetActivitySpace.updateRole(roleName,roleDisplayName,roleDescription);
            if(targetRole!=null){
                return true;
            }else{
                return false;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Role[] getRolesByParticipantName(String activitySpaceName,String participantName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            Participant targetParticipant=targetActivitySpace.getParticipant(participantName);
            if(targetParticipant!=null){
                Role[] targetRoles=targetParticipant.getRoles();
                return targetRoles;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Role[] getRolesByParticipant(Participant participant){
        try {
            Role[] targetRoles=participant.getRoles();
            return targetRoles;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Role[] getRolesByRoleQueueName(String activitySpaceName,String roleQueueName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            RoleQueue targetRoleQueue=targetActivitySpace.getRoleQueue(roleQueueName);
            if(targetRoleQueue!=null){
                Role[] targetRoles=targetRoleQueue.getRelatedRoles();
                return targetRoles;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RoleQueue[] getRoleQueuesByRoleName(String activitySpaceName,String roleName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            Role targetRole=targetActivitySpace.getRole(roleName);
            if(targetRole!=null){
                RoleQueue[] targetRoleQueues=targetRole.getRelatedRoleQueues();
                return targetRoleQueues;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RoleQueue[] getRoleQueuesByRole(Role targetRole){
        try {
            if(targetRole!=null){
                RoleQueue[] targetRoleQueues=targetRole.getRelatedRoleQueues();
                return targetRoleQueues;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateRoleRoleQueues(Role roleToUpdate,String[] roleQueueNames){
        try {
            boolean updateRoleQueueInfoResult=true;
            RoleQueue[] currentRoleQueuesArray=roleToUpdate.getRelatedRoleQueues();
            if(currentRoleQueuesArray!=null){
                for(RoleQueue currentRoleQueue:currentRoleQueuesArray){
                    updateRoleQueueInfoResult=updateRoleQueueInfoResult&roleToUpdate.removeFromRoleQueue(currentRoleQueue.getQueueName());
                }
            }
            if(roleQueueNames!=null){
                for(String currentRoleQueue:roleQueueNames){
                    updateRoleQueueInfoResult=updateRoleQueueInfoResult&roleToUpdate.addInRoleQueue(currentRoleQueue);
                }
            }
            return updateRoleQueueInfoResult;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateRoleParticipants(Role roleToUpdate,String[] participantNames){
        try {
            boolean updateParticipantInfoResult=true;
            Participant[] currentParticipantsInRoleArray=roleToUpdate.getParticipants();
            if(currentParticipantsInRoleArray!=null){
                for(Participant currentParticipant:currentParticipantsInRoleArray){
                    updateParticipantInfoResult=
                            updateParticipantInfoResult&roleToUpdate.removeParticipant(currentParticipant.getParticipantName());
                }
            }
            if(participantNames!=null){
                for(String currentParticipantName:participantNames){
                    updateParticipantInfoResult=
                            updateParticipantInfoResult&roleToUpdate.addParticipant(currentParticipantName);
                }
            }
            return updateParticipantInfoResult;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addNewRoleQueue(String activitySpaceName,String roleQueueName,String roleQueueDisplayName,String roleQueueDescription){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            RoleQueue targetRoleQueue=ActivityComponentFactory.createRoleQueue(roleQueueName, activitySpaceName, roleQueueDisplayName, roleQueueDescription);
            return targetActivitySpace.addRoleQueue(targetRoleQueue);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean removeRoleQueue(String activitySpaceName,String roleQueueName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            return targetActivitySpace.removeRoleQueue(roleQueueName);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static RoleQueue getRoleQueueByName(String activitySpaceName,String roleQueueName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            RoleQueue targetRoleQueue=targetActivitySpace.getRoleQueue(roleQueueName);
            return targetRoleQueue;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateRoleQueue(String activitySpaceName,String roleQueueName,String roleQueueDisplayName,String roleQueueDescription){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            RoleQueue targetRoleQueue=targetActivitySpace.updateRoleQueue(roleQueueName,roleQueueDisplayName,roleQueueDescription);
            if(targetRoleQueue!=null){
                return true;
            }else{
                return false;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Role[] getRolesByRoleQueue(RoleQueue targetRoleQueue){
        try {
            if(targetRoleQueue!=null){
                Role[] targetRoles=targetRoleQueue.getRelatedRoles();
                return targetRoles;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateRoleQueueRoles(RoleQueue roleQueueToUpdate,String[] roleNames){
        try {
            boolean updateRoleQueueInfoResult=true;
            Role[] currentRolesArray=roleQueueToUpdate.getRelatedRoles();
            if(currentRolesArray!=null){
                for(Role currentRole:currentRolesArray){
                    updateRoleQueueInfoResult=updateRoleQueueInfoResult&roleQueueToUpdate.removeRole(currentRole.getRoleName());
                }
            }
            if(roleNames!=null){
                for(String currentRole:roleNames){
                    updateRoleQueueInfoResult=updateRoleQueueInfoResult&roleQueueToUpdate.addRole(currentRole);
                }
            }
            return updateRoleQueueInfoResult;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static DataFieldDefinition[] getDataFieldDefinitions(String activitySpaceName,
                                                                           String targetElementName,String dataFieldQueryType){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            if(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ACTIVITYTYPE.equals(dataFieldQueryType)){
                BusinessActivityDefinition businessActivityDefinition=targetActivitySpace.getBusinessActivityDefinition(targetElementName);
                DataFieldDefinition[] activityTypeDataFieldDefineArray=businessActivityDefinition.getActivityDataFields();
                return activityTypeDataFieldDefineArray;
            }
            if(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROSTER.equals(dataFieldQueryType)){
                Roster roster=targetActivitySpace.getRoster(targetElementName);
                DataFieldDefinition[] exposedDataFieldDefineArray=roster.getExposedDataFields();
                return exposedDataFieldDefineArray;
            }
            if(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROLEQUEUE.equals(dataFieldQueryType)){
                RoleQueue roleQueue=targetActivitySpace.getRoleQueue(targetElementName);
                DataFieldDefinition[] exposedDataFieldDefineArray=roleQueue.getExposedDataFields();
                return exposedDataFieldDefineArray;
            }else{
                return null;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDataFieldDefinitionTypeString(int fieldType){
        if(fieldType==PropertyType.STRING){
            return ApplicationConstant.DataFieldType_STRING;
        }else if(fieldType==PropertyType.BINARY){
            return ApplicationConstant.DataFieldType_BINARY;
        }else if(fieldType==PropertyType.BOOLEAN){
            return ApplicationConstant.DataFieldType_BOOLEAN;
        }else if(fieldType==PropertyType.DATE){
            return ApplicationConstant.DataFieldType_DATE;
        }else if(fieldType==PropertyType.DECIMAL){
            return ApplicationConstant.DataFieldType_DECIMAL;
        }else if(fieldType==PropertyType.DOUBLE){
            return ApplicationConstant.DataFieldType_DOUBLE;
        }else if(fieldType==PropertyType.LONG){
            return ApplicationConstant.DataFieldType_LONG;
        }else{
            return null;
        }
    }

    public static int getDataFieldDefinitionTypeCode(String fieldType){
        if(ApplicationConstant.DataFieldType_STRING.equals(fieldType)){
            return PropertyType.STRING;
        }else if(ApplicationConstant.DataFieldType_BINARY.equals(fieldType)){
            return PropertyType.BINARY;
        }else if(ApplicationConstant.DataFieldType_BOOLEAN.equals(fieldType)){
            return PropertyType.BOOLEAN;
        }else if(ApplicationConstant.DataFieldType_DATE.equals(fieldType)){
            return PropertyType.DATE;
        }else if(ApplicationConstant.DataFieldType_DECIMAL.equals(fieldType)){
            return PropertyType.DECIMAL;
        }else if(ApplicationConstant.DataFieldType_DOUBLE.equals(fieldType)){
            return PropertyType.DOUBLE;
        }else if(ApplicationConstant.DataFieldType_LONG.equals(fieldType)){
            return PropertyType.LONG;
        }else{
            return 100000000;
        }
    }

    public static boolean addNewRoster(String activitySpaceName,String rosterName,String rosterDisplayName,String rosterDescription){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            Roster targetRoster=ActivityComponentFactory.createRoster(activitySpaceName,rosterName);
            targetRoster.setDisplayName(rosterDisplayName);
            targetRoster.setDescription(rosterDescription);
            return targetActivitySpace.addRoster(targetRoster);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean removeRoster(String activitySpaceName,String rosterName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            return targetActivitySpace.removeRoster(rosterName);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Roster getRosterByName(String activitySpaceName,String rosterName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            Roster targetRoster=targetActivitySpace.getRoster(rosterName);
            return targetRoster;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateRoster(String activitySpaceName,String rosterName,String rosterDisplayName,String rosterDescription){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            Roster targetRoster=targetActivitySpace.updateRoster(rosterName,rosterDisplayName,rosterDescription);
            if(targetRoster!=null){
                return true;
            }else{
                return false;
            }

        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String[] getActivityTypesByRoster(Roster targetRoster){
        try {
            if(targetRoster!=null){
                String[] targetActivityTypes=targetRoster.getContainedActivityTypes();
                if(targetActivityTypes!=null){
                    return targetActivityTypes;
                }
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateRosterActivityTypes(Roster rosterToUpdate,String[] activityTypes){
        try {
            boolean updateRosterInfoResult=true;
            String[] currentContainedActivityTypes=rosterToUpdate.getContainedActivityTypes();
            if(currentContainedActivityTypes!=null){
                for(String currentActivityType:currentContainedActivityTypes){
                    updateRosterInfoResult=updateRosterInfoResult&rosterToUpdate.removeActivityType(currentActivityType);
                }
            }
            if(activityTypes!=null){
                ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(rosterToUpdate.getActivitySpaceName());
                for(String currentActivityType:activityTypes){
                    BusinessActivityDefinition currentActivityDefinition=targetActivitySpace.getBusinessActivityDefinition(currentActivityType);
                    String currentRoster=currentActivityDefinition.getRosterName();
                    if(currentRoster!=null){
                        updateRosterInfoResult=updateRosterInfoResult&targetActivitySpace.getRoster(currentRoster).removeActivityType(currentActivityType);
                    }
                    updateRosterInfoResult=updateRosterInfoResult&rosterToUpdate.addActivityType(currentActivityType);
                }
            }
            return updateRosterInfoResult;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static  BusinessActivityDefinition[] getRosterContainsActivityDefinitions(String activitySpaceName,String rosterName ){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            Roster targetRoster=targetActivitySpace.getRoster(rosterName);
            String[] activityTypeNames=targetRoster.getContainedActivityTypes();
            if(activityTypeNames!=null){
                BusinessActivityDefinition[] activityTypeArray=new BusinessActivityDefinition[activityTypeNames.length];
                for(int i=0;i<activityTypeNames.length;i++){
                    String activityType=activityTypeNames[i];
                    BusinessActivityDefinition currentActivityType=targetActivitySpace.getBusinessActivityDefinition(activityType);
                    activityTypeArray[i]=currentActivityType;
                }
                return activityTypeArray;
            }else{
                return new BusinessActivityDefinition[0];
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
            return null;
    }

    public static boolean addDataFieldDefinition(String activitySpaceName,String targetElementName,String dataFieldQueryType,
                                                 String dataFieldName,String dataFieldDisplayName,String dataFieldType,
                                                 String dataFieldDesc,boolean isArray,boolean isMandatory,boolean isSystem){
        DataFieldDefinition targetDataFieldDefinition=
                ActivityComponentFactory.cteateDataFieldDefinition(dataFieldName, getDataFieldDefinitionTypeCode(dataFieldType),isArray);
        targetDataFieldDefinition.setDescription(dataFieldDesc);
        targetDataFieldDefinition.setDisplayName(dataFieldDisplayName);
        targetDataFieldDefinition.setMandatoryField(isMandatory);
        targetDataFieldDefinition.setSystemField(isSystem);
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            if(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ACTIVITYTYPE.equals(dataFieldQueryType)){
                return targetActivitySpace.addBusinessActivityDefinitionDataFieldDefinition(targetElementName,targetDataFieldDefinition);
            }
            if(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROSTER.equals(dataFieldQueryType)){
                Roster roster=targetActivitySpace.getRoster(targetElementName);
                return roster.addExposedDataField(targetDataFieldDefinition);
            }
            if(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROLEQUEUE.equals(dataFieldQueryType)){
                RoleQueue roleQueue=targetActivitySpace.getRoleQueue(targetElementName);
                return roleQueue.addExposedDataField(targetDataFieldDefinition);
            }else{
                return false;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteDataFieldDefinition(String activitySpaceName,String targetElementName,String dataFieldQueryType,
                                                 String dataFieldName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            if(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ACTIVITYTYPE.equals(dataFieldQueryType)){
                return targetActivitySpace.removeBusinessActivityDefinitionDataFieldDefinition(targetElementName,dataFieldName);
            }
            if(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROSTER.equals(dataFieldQueryType)){
                Roster roster=targetActivitySpace.getRoster(targetElementName);
                return roster.removeExposedDataField(dataFieldName);
            }
            if(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROLEQUEUE.equals(dataFieldQueryType)){
                RoleQueue roleQueue=targetActivitySpace.getRoleQueue(targetElementName);
                return roleQueue.removeExposedDataField(dataFieldName);
            }else{
                return false;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateDataFieldDefinition(String activitySpaceName,String targetElementName,String dataFieldQueryType,
                                                 String dataFieldName,String dataFieldDisplayName,String dataFieldType,
                                                 String dataFieldDesc,boolean isArray,boolean isMandatory,boolean isSystem){
        DataFieldDefinition targetDataFieldDefinition=
                ActivityComponentFactory.cteateDataFieldDefinition(dataFieldName, getDataFieldDefinitionTypeCode(dataFieldType),isArray);
        targetDataFieldDefinition.setDescription(dataFieldDesc);
        targetDataFieldDefinition.setDisplayName(dataFieldDisplayName);
        targetDataFieldDefinition.setMandatoryField(isMandatory);
        targetDataFieldDefinition.setSystemField(isSystem);
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            if(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ACTIVITYTYPE.equals(dataFieldQueryType)){
                return targetActivitySpace.updateBusinessActivityDefinitionDataFieldDefinition(targetElementName,targetDataFieldDefinition);
            }
            if(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROSTER.equals(dataFieldQueryType)){
                Roster roster=targetActivitySpace.getRoster(targetElementName);
                return roster.updateExposedDataField(targetDataFieldDefinition);
            }
            if(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROLEQUEUE.equals(dataFieldQueryType)){
                RoleQueue roleQueue=targetActivitySpace.getRoleQueue(targetElementName);
                return roleQueue.updateExposedDataField(targetDataFieldDefinition);
            }else{
                return false;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isDataFieldDefinitionUsedInActivity(String activitySpaceName,String targetElementName, String dataFieldName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            BusinessActivityDefinition targetBusinessActivityDefinition=targetActivitySpace.getBusinessActivityDefinition(targetElementName);
            boolean isDataFieldDefinitionUsedInActivity=false;
            DataFieldDefinition[] launchPointDFs=targetBusinessActivityDefinition.getLaunchPointExposedDataFields();
            isDataFieldDefinitionUsedInActivity=isDataFieldDefinitionUsedInActivity|checkDataFieldDefinitionExist(launchPointDFs,dataFieldName);
            String[] launchProcessVas=targetBusinessActivityDefinition.getLaunchProcessVariableList();
            isDataFieldDefinitionUsedInActivity=isDataFieldDefinitionUsedInActivity|checkProcessVaribleExist(launchProcessVas,dataFieldName);
            String[] exposedSteps= targetBusinessActivityDefinition.getExposedSteps();
            Map<String,DataFieldDefinition[]> stepDataFieldsMap=targetBusinessActivityDefinition.getActivityStepsExposedDataField();
            if(exposedSteps!=null) {
                for (String currentStep : exposedSteps) {
                    String[] stepProcessVas=targetBusinessActivityDefinition.getStepProcessVariableList(currentStep);
                    isDataFieldDefinitionUsedInActivity=isDataFieldDefinitionUsedInActivity|checkProcessVaribleExist(stepProcessVas,dataFieldName);
                    if(stepDataFieldsMap!=null){
                        DataFieldDefinition[] stepPointDFs=stepDataFieldsMap.get(currentStep);
                        isDataFieldDefinitionUsedInActivity=isDataFieldDefinitionUsedInActivity|checkDataFieldDefinitionExist(stepPointDFs,dataFieldName);
                    }
                }
            }
            return isDataFieldDefinitionUsedInActivity;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean checkDataFieldDefinitionExist(DataFieldDefinition[] dfs,String dataFieldName){
        if(dfs==null){
            return false;
        }else{
            for(DataFieldDefinition currentDf:dfs){
                if(currentDf.getFieldName().equals(dataFieldName)){
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean checkProcessVaribleExist(String[] processVariableList,String dataFieldName){
        if(processVariableList==null){
            return false;
        }else{
            for(String currentField:processVariableList){
                if(currentField.equals(dataFieldName)){
                    return true;
                }
            }
            return false;
        }
    }

    public static List<BusinessActivity> getActivityInstancesByRoster(Roster targetRoster){
        try {
            if(targetRoster!=null){
                List<BusinessActivity> businessActivityList=targetRoster.fetchBusinessActivitys();
                return businessActivityList;
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BusinessActivityDefinition getActivityTypeDetailInfo(String activitySpaceName,String activityType){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            BusinessActivityDefinition targetActivityType=targetActivitySpace.getBusinessActivityDefinition(activityType);
            return targetActivityType;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ActivityStepDefinition[] getActivityTypeDefinedSteps(String activitySpaceName,String activityType){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            BusinessActivityDefinition targetBusinessActivityDefinition=targetActivitySpace.getBusinessActivityDefinition(activityType);
            ActivityStepDefinition[] definedStepsArray=targetBusinessActivityDefinition.getDefinedSteps();
            return definedStepsArray;
        } catch (ActivityEngineActivityException e) {
            e.printStackTrace();
        } catch (ActivityEngineRuntimeException e) {
            e.printStackTrace();
        } catch (ActivityEngineDataException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean removeActivityTypeExposedStep(String activitySpaceName,String activityType,String activityStepName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            return targetActivitySpace.removeBusinessActivityDefinitionExposedStep(activityType,activityStepName);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addActivityTypeExposedStep(String activitySpaceName,String activityType,String activityStepName,String roleName){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            String relatedRole=null;
            if(roleName!=null&&!roleName.trim().equals("")){
                relatedRole=roleName;
            }
            return targetActivitySpace.addBusinessActivityDefinitionExposedStep(activityType,activityStepName,relatedRole);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setActivityTypeExposedStepDataFieldDefinitions(
            String activitySpaceName,String activityType,String activityStepName,List<ActivityDataFieldVO> dataFieldDefinitions){
        DataFieldDefinition[] dataFieldDefinitionArray=new DataFieldDefinition[dataFieldDefinitions.size()];
        for(int i=0;i<dataFieldDefinitions.size();i++){
            ActivityDataFieldVO currentActivityDataFieldVO=dataFieldDefinitions.get(i);
            DataFieldDefinition targetDataFieldDefinition=
                    ActivityComponentFactory.cteateDataFieldDefinition(currentActivityDataFieldVO.getDataFieldName(),
                            getDataFieldDefinitionTypeCode(currentActivityDataFieldVO.getDataType()),currentActivityDataFieldVO.isArrayField());
            targetDataFieldDefinition.setDescription(currentActivityDataFieldVO.getDescription());
            targetDataFieldDefinition.setDisplayName(currentActivityDataFieldVO.getDataFieldDisplayName());
            targetDataFieldDefinition.setMandatoryField(currentActivityDataFieldVO.isMandatoryField());
            targetDataFieldDefinition.setSystemField(currentActivityDataFieldVO.isSystemField());
            targetDataFieldDefinition.setReadableField(currentActivityDataFieldVO.isReadableField());
            targetDataFieldDefinition.setWriteableField(currentActivityDataFieldVO.isWritableField());
            dataFieldDefinitionArray[i]=targetDataFieldDefinition;
        }
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            return targetActivitySpace.setBusinessActivityDefinitionExposedStepDataFieldDefinitions(activityType,activityStepName,dataFieldDefinitionArray);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setActivityTypeExposedStepInfo(
            String activitySpaceName,String activityType,String activityStepName,String stepRole,
            String stepUserIdentityAttribute,String[] stepProcessVariablesList){
        try {
            ActivityStepDefinition activityStepDefinition=new ActivityStepDefinition();
            activityStepDefinition.setStepId(activityStepName);
            activityStepDefinition.setStepRole(stepRole);
            activityStepDefinition.setStepUserIdentityAttribute(stepUserIdentityAttribute);
            activityStepDefinition.setStepProcessVariables(stepProcessVariablesList);
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            return targetActivitySpace.setBusinessActivityDefinitionExposedStepProcessProperties(activityType, activityStepDefinition);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setActivityTypeStepDecisionPointProperties(
            String activitySpaceName, String activityType, String activityStepName,
            String stepDecisionPointAttribute, String[] stepDecisionPointChooseOptions){
        try {
            ActivityStepDefinition activityStepDefinition=new ActivityStepDefinition();
            activityStepDefinition.setStepId(activityStepName);
            if(stepDecisionPointAttribute!=null&stepDecisionPointAttribute.trim().equals("")){
                activityStepDefinition.setStepDecisionPointAttribute(null);
            }else{
                activityStepDefinition.setStepDecisionPointAttribute(stepDecisionPointAttribute);
            }
            if(stepDecisionPointChooseOptions!=null&stepDecisionPointChooseOptions.length==0){
                activityStepDefinition.setStepDecisionPointChooseOptions(null);
            }else{
                activityStepDefinition.setStepDecisionPointChooseOptions(stepDecisionPointChooseOptions);
            }
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            return targetActivitySpace.setBusinessActivityDefinitionExposedStepDecisionPointProperties(activityType,activityStepDefinition);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setActivityTypeDefinitionProperties(String activitySpaceName, String activityType,String activityTypeDescription,
                                                              String rosterName,boolean isEnabled,String launchUserIdentityAttribute,
                                                              String[] launchRoles,String[] launchParticipants,String[] launchProcessVariables,
                                                              String[] businessCategories,String launchDecisionPointAttribute,String[] launchDecisionPointOptions,
                                                              List<ActivityDataFieldVO> launchPointDataFields){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            BusinessActivityDefinition targetDefinition=targetActivitySpace.getBusinessActivityDefinition(activityType);
            BusinessActivityDefinition voBusinessActivityDefinition=ActivityComponentFactory.createBusinessActivityDefinition(
                    targetDefinition.getActivityType(),targetDefinition.getActivitySpaceName(),targetDefinition.getExposedSteps());
            voBusinessActivityDefinition.setActivityDataFields(targetDefinition.getActivityDataFields());
            voBusinessActivityDefinition.setActivityDescription(activityTypeDescription);
            ((CCRBusinessActivityDefinitionImpl)voBusinessActivityDefinition).setRosterName(rosterName);
            ((CCRBusinessActivityDefinitionImpl)voBusinessActivityDefinition).setIsEnabled(isEnabled);
            voBusinessActivityDefinition.setLaunchUserIdentityAttributeName(launchUserIdentityAttribute);
            voBusinessActivityDefinition.setActivityLaunchRoles(launchRoles);
            voBusinessActivityDefinition.setActivityLaunchParticipants(launchParticipants);
            voBusinessActivityDefinition.setLaunchProcessVariableList(launchProcessVariables);
            voBusinessActivityDefinition.setActivityCategories(businessCategories);
            voBusinessActivityDefinition.setLaunchDecisionPointAttributeName(launchDecisionPointAttribute);
            voBusinessActivityDefinition.setLaunchDecisionPointChoiseList(launchDecisionPointOptions);
            if(launchPointDataFields!=null){
                DataFieldDefinition[] launchPointDataFieldsArray=new DataFieldDefinition[launchPointDataFields.size()];
                for(int i=0;i<launchPointDataFields.size();i++){
                    ActivityDataFieldVO currentActivityDataFieldVO=launchPointDataFields.get(i);
                    DataFieldDefinition currentDataFieldDefinition= ActivityComponentFactory.cteateDataFieldDefinition(
                            currentActivityDataFieldVO.getDataFieldName(),getDataFieldDefinitionTypeCode(currentActivityDataFieldVO.getDataType()),
                            currentActivityDataFieldVO.isArrayField()
                    );

                    currentDataFieldDefinition.setWriteableField(currentActivityDataFieldVO.isWritableField());
                    currentDataFieldDefinition.setReadableField(currentActivityDataFieldVO.isReadableField());
                    currentDataFieldDefinition.setSystemField(currentActivityDataFieldVO.isSystemField());
                    currentDataFieldDefinition.setMandatoryField(currentActivityDataFieldVO.isMandatoryField());
                    currentDataFieldDefinition.setDisplayName(currentActivityDataFieldVO.getDataFieldDisplayName());
                    currentDataFieldDefinition.setDescription(currentActivityDataFieldVO.getDescription());
                    launchPointDataFieldsArray[i]=currentDataFieldDefinition;
                }
                voBusinessActivityDefinition.setLaunchPointExposedDataFields(launchPointDataFieldsArray);
            }
            return targetActivitySpace.updateBusinessActivityDefinitionProperties(voBusinessActivityDefinition);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addNewActivityType(String activitySpaceName, String activityType,String definitionFileLocation){
        if(definitionFileLocation==null){
            return false;
        }else{
            try {
                ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
                String[] exposedSteps=new String[0];
                BusinessActivityDefinition bad=ActivityComponentFactory.createBusinessActivityDefinition(activityType,activitySpaceName,exposedSteps);
                FileInputStream fileInputStream= new FileInputStream(definitionFileLocation);
                bad.setDefinitionResource(fileInputStream);
                boolean addNewActivityTypeResult=targetActivitySpace.addBusinessActivityDefinition(bad);
                return addNewActivityTypeResult;
            } catch (ActivityEngineException e) {
                e.printStackTrace();
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                File definitionFile=new File(definitionFileLocation);
                definitionFile.delete();
            }
            return true;
        }
    }

    public static String getActivityTypeBMPNFileContent(String activitySpaceName, String activityType){
        StringBuffer strBuf=new StringBuffer();
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            BusinessActivityDefinition realtimeBusinessActivityDefinition=targetActivitySpace.getBusinessActivityDefinition(activityType);
            Object definitionResource=realtimeBusinessActivityDefinition.getDefinitionFlowXML();
            try (InputStream businessActivityDefineFileInputStream = (InputStream) definitionResource) {
                byte buf[] = new byte[1024];
                int len;
                while ((len = businessActivityDefineFileInputStream.read(buf)) > 0) {
                    strBuf.append(new String(buf, 0, len,"UTF-8"));
                }
                businessActivityDefineFileInputStream.close();
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return strBuf.toString();
    }

    private final static String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();

    public static File getActivityTypeBMPNFile(String activitySpaceName, String activityType){
        File activityTypeBPMNFile=new File(tempFileDir+activitySpaceName+"_"+activityType+".bpmn2.xml");
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            BusinessActivityDefinition realtimeBusinessActivityDefinition=targetActivitySpace.getBusinessActivityDefinition(activityType);
            Object definitionResource=realtimeBusinessActivityDefinition.getDefinitionFlowXML();
            try (InputStream businessActivityDefineFileInputStream = (InputStream) definitionResource) {
                OutputStream os = new FileOutputStream(activityTypeBPMNFile);
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = businessActivityDefineFileInputStream.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                businessActivityDefineFileInputStream.close();
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return activityTypeBPMNFile;
    }

    public static File getActivityTypeBMPNFlowDiagram(String activitySpaceName, String activityType){
        File activityTypeBPMNFlowDiagramFile=new File(tempFileDir+activitySpaceName+"_"+activityType+".png");
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            BusinessActivityDefinition realtimeBusinessActivityDefinition=targetActivitySpace.getBusinessActivityDefinition(activityType);
            Object definitionResource=realtimeBusinessActivityDefinition.getDefinitionFlowDiagram();
            try (InputStream businessActivityDefineFileInputStream = (InputStream) definitionResource) {
                OutputStream os = new FileOutputStream(activityTypeBPMNFlowDiagramFile);
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = businessActivityDefineFileInputStream.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                businessActivityDefineFileInputStream.close();
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return activityTypeBPMNFlowDiagramFile;
    }

    public static boolean updateActivityTypeProcessDefinition(String activitySpaceName, String activityType,String definitionFileLocation){
        if(definitionFileLocation==null){
            return false;
        }else{
            try {
                ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
                File definitionFile=new File(definitionFileLocation);
                boolean updateActivityTypeResult=targetActivitySpace.refreshBusinessActivityDefinitionWorkflow(activityType,definitionFile);
                return updateActivityTypeResult;
            } catch (ActivityEngineException e) {
                e.printStackTrace();
            }finally {
                File definitionFile=new File(definitionFileLocation);
                definitionFile.delete();
            }
            return true;
        }
    }

    public static Map<String,String> getActivityTypeStepProcessEditors(String activitySpaceName, String activityType){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            return targetActivitySpace.getBusinessActivityDefinitionStepProcessEditorsInfo(activityType);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateActivityTypeStepProcessEditors(String activitySpaceName, String activityType,Map<String,String> stepEditorMap){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            return targetActivitySpace.setBusinessActivityDefinitionStepProcessEditorInfo(activityType,stepEditorMap);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static CustomStructure getActivityStepRootCustomConfigItem(String activitySpaceName, String activityType,String activityStep){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            CustomStructure stepCustomConfigItemRootStructure=targetActivitySpace.getBusinessActivityDefinitionStepCustomStructure(activityType,activityStep);
            return stepCustomConfigItemRootStructure;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CustomStructure getActivityDefinitionRootCustomConfigItem(String activitySpaceName, String activityType){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            CustomStructure activityTypeCustomConfigItemRootStructure=targetActivitySpace.getBusinessActivityDefinitionGlobalCustomStructure(activityType);
            return activityTypeCustomConfigItemRootStructure;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<CustomStructure> getActivityStepCustomConfigItemsList(String activitySpaceName, String activityType,String activityStep){
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            CustomStructure stepCustomConfigItemRootStructure=targetActivitySpace.getBusinessActivityDefinitionStepCustomStructure(activityType,activityStep);
            List<CustomStructure> subStructureList=stepCustomConfigItemRootStructure.getSubCustomStructures();
            return subStructureList;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<CustomStructure> getSubCustomConfigItemsList(CustomStructure targetStructure){
        try {
            List<CustomStructure> subStructureList=targetStructure.getSubCustomStructures();
            return subStructureList;
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CustomStructure addSubCustomStructure(CustomStructure parentCustomStructure, String subStructureName){
        CustomStructure resultStructure=null;
        try {
            boolean addStructureResult=parentCustomStructure.addSubCustomStructure(subStructureName);
            if(addStructureResult){
                resultStructure =parentCustomStructure.getSubCustomStructure(subStructureName);
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return resultStructure;
    }

    public static boolean deleteSubCustomStructure(CustomStructure parentCustomStructure, String subStructureName){
        try {
            return parentCustomStructure.deleteSubCustomStructure(subStructureName);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static CustomStructure getSubCustomStructure(CustomStructure parentCustomStructure, String subStructureName){
        try {
            return parentCustomStructure.getSubCustomStructure(subStructureName);
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkCustomStructureAttributeExistence(CustomStructure customStructure,String attributeName){
        try {
            CustomAttribute targetCustomAttribute=customStructure.getCustomAttribute(attributeName);
            if(targetCustomAttribute!=null){
                return true;
            }else{
                return false;
            }
        } catch (ActivityEngineRuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addCustomStructureAttribute(CustomStructure customStructure,String attributeName,Object attributeValue){
        try {
            CustomAttribute customAttribute=ActivityComponentFactory.createCustomAttribute();
            customAttribute.setAttributeName(attributeName);
            customAttribute.setAttributeValue(attributeValue);
            return customStructure.addCustomAttribute(customAttribute);
        } catch (ActivityEngineRuntimeException e) {
            e.printStackTrace();
        } catch (ActivityEngineDataException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<CustomAttribute> getCustomStructureAttributes(CustomStructure customStructure){
        try {
            List<CustomAttribute> customAttributeList=customStructure.getCustomAttributes();
            return customAttributeList;
        } catch (ActivityEngineRuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteCustomStructureAttribute(CustomStructure customStructure,String attributeName){
        try {
            return customStructure.deleteCustomAttribute(attributeName);
        } catch (ActivityEngineRuntimeException e) {
            e.printStackTrace();
        } catch (ActivityEngineDataException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateCustomStructureAttribute(CustomStructure customStructure,CustomAttribute updatedCustomAttribute){
        try {
            return customStructure.updateCustomAttribute(updatedCustomAttribute);
        } catch (ActivityEngineRuntimeException e) {
            e.printStackTrace();
        } catch (ActivityEngineDataException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static File generateActivityTypeDataFieldsJsonFile(String activitySpaceName,String activityType){
        File targetFile=new File(RuntimeEnvironmentUtil.getBinaryTempFileDirLocation()+activitySpaceName+"_"+activityType+"_DataFieldDefinitions.json");
        List<Map<String,Object>> fieldDefinitionsList=new ArrayList<>();
        try {
            ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
            BusinessActivityDefinition targetDefinition=targetActivitySpace.getBusinessActivityDefinition(activityType);
            if(targetDefinition!=null){
                DataFieldDefinition[] targetDefinitions=targetDefinition.getActivityDataFields();
                if(targetDefinitions!=null){
                    for(DataFieldDefinition currentDefinition:targetDefinitions){
                        Map<String,Object> fieldInfoMap=new HashMap<>();
                        String fieldName=currentDefinition.getFieldName();
                        fieldInfoMap.put("FieldName",fieldName);
                        String fieldDescription=currentDefinition.getDescription();
                        fieldInfoMap.put("Description",fieldDescription);
                        String fieldDisplayName=currentDefinition.getDisplayName();
                        fieldInfoMap.put("DisplayName",fieldDisplayName);
                        int fieldType=currentDefinition.getFieldType();
                        fieldInfoMap.put("FieldType",fieldType);
                        boolean arrayField=currentDefinition.isArrayField();
                        fieldInfoMap.put("ArrayField",arrayField);
                        boolean mandatoryField=currentDefinition.isMandatoryField();
                        fieldInfoMap.put("MandatoryField",mandatoryField);
                        boolean systemField=currentDefinition.isSystemField();
                        fieldInfoMap.put("SystemField",systemField);
                        fieldDefinitionsList.add(fieldInfoMap);
                    }
                }
            }
        } catch (ActivityEngineException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();

        //当找不到对应的序列化器时 忽略此字段
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        //使Jackson JSON支持Unicode编码非ASCII字符
        CustomSerializerFactory serializerFactory= new CustomSerializerFactory();
        serializerFactory.addSpecificMapping(String.class, new StringUnicodeSerializer());
        mapper.setSerializerFactory(serializerFactory);
        //支持结束
        String json = null;
        try {
            json = mapper.writeValueAsString(fieldDefinitionsList);
            try(PrintStream ps = new PrintStream(targetFile)) {
                ps.println(json);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetFile;
    }

    public static boolean importActivityTypeDataFieldDefinitionsFromJsonFile(String activitySpaceName,String activityType,String jsonFileLocation,String existDataFieldHandleMethod){
        File file = new File(jsonFileLocation);
        if(file.exists()){
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode dataNode=mapper.readTree(file);
                if(dataNode.isArray()){
                    Iterator<JsonNode> dataFieldDefinitionIterator = dataNode.getElements();
                    try {
                        ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
                        BusinessActivityDefinition targetDefinition=targetActivitySpace.getBusinessActivityDefinition(activityType);
                        if(targetDefinition!=null){
                            DataFieldDefinition[] targetDataFieldDefinitions=targetDefinition.getActivityDataFields();
                            while(dataFieldDefinitionIterator.hasNext()){
                                JsonNode currentDataFieldDefinition=dataFieldDefinitionIterator.next();
                                if(!currentDataFieldDefinition.isArray()){
                                    JsonNode descriptionProp=currentDataFieldDefinition.get("Description");
                                    JsonNode displayNameProp=currentDataFieldDefinition.get("DisplayName");
                                    JsonNode fieldNameProp=currentDataFieldDefinition.get("FieldName");
                                    JsonNode fieldTypeProp=currentDataFieldDefinition.get("FieldType");
                                    JsonNode arrayFieldProp=currentDataFieldDefinition.get("ArrayField");
                                    JsonNode mandatoryFieldProp=currentDataFieldDefinition.get("MandatoryField");
                                    JsonNode systemFieldProp=currentDataFieldDefinition.get("SystemField");
                                    if(descriptionProp!=null&&displayNameProp!=null&&fieldNameProp!=null&&fieldTypeProp!=null
                                            &&arrayFieldProp!=null&&mandatoryFieldProp!=null&&systemFieldProp!=null){

                                        String fieldName=fieldNameProp.getTextValue();
                                        String displayName=displayNameProp.getTextValue();
                                        String description=descriptionProp.getTextValue();
                                        int fieldType=fieldTypeProp.getIntValue();
                                        boolean isArratField=arrayFieldProp.getBooleanValue();
                                        boolean isMandatoryField=mandatoryFieldProp.getBooleanValue();
                                        boolean isSystemField=systemFieldProp.getBooleanValue();

                                        boolean isExistDataFieldCheck=checkDataFieldDefinitionExist(targetDataFieldDefinitions,fieldName);

                                        if(isExistDataFieldCheck){
                                            if(ExistedDataHandleMethod_REPLACE.equals(existDataFieldHandleMethod)){
                                                DataFieldDefinition targetDataFieldDefinition=
                                                        ActivityComponentFactory.cteateDataFieldDefinition(fieldName, fieldType,isArratField);
                                                targetDataFieldDefinition.setDescription(description);
                                                targetDataFieldDefinition.setDisplayName(displayName);
                                                targetDataFieldDefinition.setMandatoryField(isMandatoryField);
                                                targetDataFieldDefinition.setSystemField(isSystemField);
                                                targetActivitySpace.updateBusinessActivityDefinitionDataFieldDefinition(activityType,targetDataFieldDefinition);
                                            }
                                        }else{
                                            DataFieldDefinition targetDataFieldDefinition=
                                                    ActivityComponentFactory.cteateDataFieldDefinition(fieldName, fieldType,isArratField);
                                            targetDataFieldDefinition.setDescription(description);
                                            targetDataFieldDefinition.setDisplayName(displayName);
                                            targetDataFieldDefinition.setMandatoryField(isMandatoryField);
                                            targetDataFieldDefinition.setSystemField(isSystemField);
                                            targetActivitySpace.addBusinessActivityDefinitionDataFieldDefinition(activityType,targetDataFieldDefinition);
                                        }
                                    }
                                }
                            }
                        }else{
                            return false;
                        }
                    } catch (ActivityEngineException e) {
                        e.printStackTrace();
                    }
                    return true;
                }else{
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    FileUtils.forceDelete(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
