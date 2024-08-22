package org.egov.tm.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;


@Component
public class PGRConstants {
	
	private PGRConstants() {}

	public static final String SERV_REQ_ID_NAME = "rainmaker.sr.servicerequestid";
	public static final String SERV_REQ_ID_FORMAT = "SR-[CITY.CODE]-[SEQ_EG_SR_SERVICEREQUESTID]";
		
	//Notification
	public static final String TEMPLATE_COMPLAINT_EMAIL = "./src/main/resources/email-templates/velocityEmailNotifSample.vm";
	
	public static final String  TENANT_ID = "od";
	
	public static final String SEARCHER_PGR_MOD_NAME = "rainmaker-pgr-V2";
	public static final String SEARCHER_SRSEARCH_DEF_NAME = "serviceSearchWithDetails";
	public static final String SEARCHER_PLAINSEARCH_DEF_NAME = "plainSearch";
	public static final String SEARCHER_COUNT_DEF_NAME = "count";
	public static final String PG_JSONPATH_COUNT = "$.count[0].count";
	public static final String SEARCHER_SRID_ASSIGNEDTO_DEF_NAME = "getServiceRequestsOnAssignedTo";
	public static final String SRID_ASSIGNEDTO_JSONPATH = "$.servicesRequestIds.*.businesskey";
	public static final String MDMS_PGR_MOD_NAME = "RAINMAKER-PGR";
	public static final String MDMS_SERVICETYPE_MASTER_NAME = "ServiceDefs";
	public static final String MDMS_ESCALATIONLEVEL4_COMPLAINTS_MASTER_NAME = "Escalationlevel4Complaints";
	public static final String MDMS_COMMON_MASTERS_MODULE_NAME = "common-masters";
	public static final String MDMS_TENANT_MODULE_NAME = "tenant";
	public static final String MDMS_DEPT_MASTERS_MASTER_NAME = "Department";
	public static final String MDMS_MASTER_TENANTS = "tenants";
	public static final String MDMS_DESIGNATION_MASTERS_MASTER_NAME = "Designation";
	public static final String LOCALIZATION_MODULE_NAME = "rainmaker-pgr";
	

	public static final String  SERVICE_REQID_REGEX = "(^[0-9a-zA-Z/-]*$)";

	
	public static final String ROLE_CREATOR="TM-CREATOR";
	public static final String ROLE_ADMIN="TM-ADMIN";
	public static final String ROLE_ASSIGNER="TM-L3-SUPPORT";

	public static final String LOCALIZATION_MODULE = "rainmaker-tm";

//	private static Map<String, String> statusNotifKeyMap = prepareStatusNotifKeyMap();
//
//	private static Map<String, String> actionNotifKeyMap = prepareActionNotifKeyMap();
//	
//	private static Map<String, String> statusRoleLocalizationKeyMap = prepareStatusRoleLocalizationKeyMap();
//
//	private static Map<String, String> actionRoleLocalizationKeyMap = prepareActionRoleLocalizationKeyMap();
	
//	private static Map<String, String> prepareStatusNotifKeyMap() {
//		
//		Map<String, String> map = new HashMap<>();
//		map.put(WorkFlowConfigs.STATUS_OPENED, "OPENED");
//		map.put(WorkFlowConfigs.STATUS_ASSIGNED, "ASSIGNED");
//		map.put(WorkFlowConfigs.STATUS_REJECTED, "REJECTED");
//		map.put(WorkFlowConfigs.STATUS_RESOLVED, "RESOLVED");
//	
//		return map;
//	}
//	
//	private static Map<String, String> prepareActionNotifKeyMap() {
//
//		Map<String, String> map = new HashMap<>();
//		map.put(WorkFlowConfigs.ACTION_REOPEN, "REOPENED");
//		map.put(WorkFlowConfigs.ACTION_REASSIGN, "reassigned");
//
//		return map;
//	}
	
	
	
//	
//	public static Map<String, String> getStatusNotifKeyMap(){
//		return statusNotifKeyMap;
//	}
//	
//	public static Map<String, String> getActionNotifKeyMap(){
//		return actionNotifKeyMap;
//	}
//	
//	public static Map<String, String> getStatusRoleLocalizationKeyMap(){
//		return statusRoleLocalizationKeyMap;
//	}
//	
//	public static Map<String, String> getActionRoleLocalizationKeyMap(){
//		return actionRoleLocalizationKeyMap;
//	}
		
}
