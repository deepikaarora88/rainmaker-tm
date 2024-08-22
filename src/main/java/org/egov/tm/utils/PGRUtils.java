package org.egov.tm.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tm.contract.CountResponse;
import org.egov.tm.contract.RequestInfoWrapper;
import org.egov.tm.contract.SearcherRequest;
import org.egov.tm.contract.ServiceReqSearchCriteria;
import org.egov.tm.contract.ServiceResponse;
import org.egov.tm.model.ActionHistory;
import org.egov.tm.model.ActionInfo;
import org.egov.tm.model.AuditDetails;
import org.egov.tm.model.Services;
import org.egov.tm.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PGRUtils {

	//private static Map<Integer, String> employeeRolesPrecedenceMap = prepareEmployeeRolesPrecedenceMap();

	@Value("${egov.infra.searcher.host}")
	private String searcherHost;

	@Value("${egov.infra.searcher.endpoint}")
	private String searcherEndpoint;

	@Value("${egov.mdms.host}")
	private String mdmsHost;

	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndpoint;

	@Value("${egov.hr.employee.v2.host}")
	private String hrEmployeeV2Host;

	@Value("${egov.hr.employee.v2.search.endpoint}")
	private String hrEmployeeSearchEndpoint;

	@Value("${egov.common.masters.host}")
	private String commonMasterHost;

	@Value("${egov.common.masters.search.endpoint}")
	private String commonMasterSearchEndpoint;

	@Value("${egov.localization.host}")
	private String localizationHost;

	@Value("${egov.localization.search.endpoint}")
	private String localizationSearchEndpoint;

	@Value("${egov.user.host}")
	private String egovUserHost;

	@Value("${egov.user.search.endpoint}")
	private String egovUserSearchEndpoint;
	
	@Value("${egov.location.host}")
	private String locationHost;

	@Value("${egov.location.search.endpoint}")
	private String locationSearchEndpoint;
	
	@Value("${egov.hrms.host}")
	private String egovHRMShost;

	@Value("${egov.hrms.search.endpoint}")
	private String egovHRMSSearchEndpoint;
	
	@Value("${are.inactive.complaintcategories.enabled}")
	private Boolean areInactiveComplaintCategoriesEnabled;	
	
	@Value("${egov.default.expiry.time.before.reopen.in.hours}")
	private Long defaultExpiryTimeBeforeReopen;

	@Autowired
	private ResponseInfoFactory factory;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
	
	private static final String MODULE_NAME = "{moduleName}";

	private static final String SEARCH_NAME = "{searchName}";

	/**
	 * Prepares request and uri for service code search from MDMS
	 * 
	 * @param uri
	 * @param tenantId
	 * @param department
	 * @param requestInfo
	 * @return MdmsCriteriaReq
	 * @author vishal
	 */
	public MdmsCriteriaReq prepareSearchRequestForServiceCodes(StringBuilder uri, String tenantId, List<String> departments,
			RequestInfo requestInfo) {
		uri.append(mdmsHost).append(mdmsEndpoint);
		StringBuilder depts = new StringBuilder();
		depts.append("[");
		for(int i = 0; i < departments.size() ; i++) {
			depts.append("'" + departments.get(i) + "'");
			if(i < departments.size() - 1)
				depts.append(",");
		}
		depts.append("]");
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_SERVICETYPE_MASTER_NAME)
				.filter("[?(@.department IN " + depts.toString() + ")]").build();
		if(!areInactiveComplaintCategoriesEnabled) {
			masterDetail.setFilter("[?((@.department IN " + depts.toString() + ") && (@.active == true))]");
		}
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(PGRConstants.MDMS_PGR_MOD_NAME)
				.masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}

	/**
	 * Util method to return Auditdetails for create and update processes
	 * 
	 * @param by
	 * @param isCreate
	 * @return
	 */
	public AuditDetails getAuditDetails(String by, Boolean isCreate) {

		Long dt = new Date().getTime();
		if (isCreate)
			return AuditDetails.builder().createdBy(by).createdTime(dt).lastModifiedBy(by).lastModifiedTime(dt).build();
		else
			return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(dt).build();
	}

	/**
	 * Prepares request and uri for service type search from MDMS
	 * 
	 * @param uri
	 * @param tenantId
	 * @param department
	 * @param requestInfo
	 * @return MdmsCriteriaReq
	 * @author vishal
	 */
	public MdmsCriteriaReq prepareSearchRequestForServiceType(StringBuilder uri, String tenantId, String serviceCode,
			RequestInfo requestInfo) {
		uri.append(mdmsHost).append(mdmsEndpoint);
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_SERVICETYPE_MASTER_NAME)
				.filter("[?(@.serviceCode=='" + serviceCode + "')]").build();
		if(!areInactiveComplaintCategoriesEnabled) {
			masterDetail.setFilter("[?((@.serviceCode=='" + serviceCode + "') && (@.active == true))]");
		}
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(PGRConstants.MDMS_PGR_MOD_NAME)
				.masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}

	/**
	 * Prepares request and uri for service type search from MDMS
	 * 
	 * @param uri
	 * @param tenantId
	 * @param department
	 * @param requestInfo
	 * @return MdmsCriteriaReq
	 * @author vishal
	 */
	
	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 * @throws JsonProcessingException 
	 */
	public SearcherRequest prepareSearchRequestWithDetails(StringBuilder uri,
			ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo){
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.SEARCHER_PGR_MOD_NAME).replace(SEARCH_NAME,
				PGRConstants.SEARCHER_SRSEARCH_DEF_NAME);
		uri.append(endPoint);
		serviceReqSearchCriteria.setNoOfRecords(null == serviceReqSearchCriteria.getNoOfRecords() ? 200L : serviceReqSearchCriteria.getNoOfRecords()); //be default we retrieve 200 records.
		serviceReqSearchCriteria.setOffset(null == serviceReqSearchCriteria.getOffset() ? 0L : serviceReqSearchCriteria.getOffset());
		/**
		 * This if block is to support substring search on servicerequestid without changing the contract. 
		 * Query uses an IN clause which doesn't support substring search, therefore a new temp variable is added.
		 */		
		if(!CollectionUtils.isEmpty(serviceReqSearchCriteria.getServiceRequestId()) &&
				serviceReqSearchCriteria.getServiceRequestId().size() == 1) {
			ObjectMapper mapper = getObjectMapper();
			Map<String, Object> mapOfValues = mapper.convertValue(serviceReqSearchCriteria, Map.class);
			mapOfValues.put("complaintId", serviceReqSearchCriteria.getServiceRequestId().get(0));
			mapOfValues.put("serviceRequestId", null);
			return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(mapOfValues).build();
		}else {
			return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		}
	}
	
	
	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 * @throws JsonProcessingException 
	 */
	public SearcherRequest preparePlainSearchReq(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo){
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.SEARCHER_PGR_MOD_NAME).replace(SEARCH_NAME,
				PGRConstants.SEARCHER_PLAINSEARCH_DEF_NAME);
		uri.append(endPoint);
		serviceReqSearchCriteria.setNoOfRecords(null == serviceReqSearchCriteria.getNoOfRecords() ? 200L : serviceReqSearchCriteria.getNoOfRecords()); //be default we retrieve 200 records.
		serviceReqSearchCriteria.setOffset(null == serviceReqSearchCriteria.getOffset() ? 0L : serviceReqSearchCriteria.getOffset());
		return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
	}

	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareSearchRequestForAssignedTo(StringBuilder uri,
			ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.SEARCHER_PGR_MOD_NAME).replace(SEARCH_NAME,
				PGRConstants.SEARCHER_SRID_ASSIGNEDTO_DEF_NAME);
		uri.append(endPoint);
		return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
	}

	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareCountRequestWithDetails(StringBuilder uri,
			ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.SEARCHER_PGR_MOD_NAME).replace(SEARCH_NAME,
				PGRConstants.SEARCHER_COUNT_DEF_NAME);
		uri.append(endPoint);
		return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
	}
	
	public MdmsCriteriaReq prepareServiceDefSearchMdmsRequest(StringBuilder uri, String tenantId, RequestInfo requestInfo) {
		uri.append(mdmsHost).append(mdmsEndpoint);
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_SERVICETYPE_MASTER_NAME)
				.build();
		if(!areInactiveComplaintCategoriesEnabled) {
			masterDetail.setFilter("[?(@.active == true)]");
		}		
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(PGRConstants.MDMS_PGR_MOD_NAME)
				.masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}

	public RequestInfoWrapper prepareRequestForEmployeeSearch(StringBuilder uri, RequestInfo requestInfo,
			ServiceReqSearchCriteria serviceReqSearchCriteria) {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		uri.append(egovHRMShost).append(egovHRMSSearchEndpoint).append("?ids=" + requestInfo.getUserInfo().getId())
				.append("&tenantId=" + serviceReqSearchCriteria.getTenantId());

		return requestInfoWrapper;
	}
	
	public RequestInfoWrapper prepareRequestForLocalization(StringBuilder uri, RequestInfo requestInfo, String locale,
			String tenantId, String module) {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		uri.append(localizationHost).append(localizationSearchEndpoint).append("?tenantId=" + tenantId)
				.append("&module=" + module).append("&locale=" + locale);

		return requestInfoWrapper;
	}
	
	public RequestInfoWrapper prepareRequestForLocation(StringBuilder uri, RequestInfo requestInfo, String boundaryType,
			String tenantId, String hierarchyType, List<String> mohallaCodes) {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		String codes = mohallaCodes.toString().substring(1, mohallaCodes.toString().length() - 1);
		uri.append(locationHost).append(locationSearchEndpoint).append("?tenantId=" + tenantId)
				.append("&hierarchyTypeCode=" + hierarchyType).append("&boundaryType=" + boundaryType).append("&codes=" + codes);

		return requestInfoWrapper;
	}

	
	/**
	 * Default response is responseInfo with error status and empty lists
	 * 
	 * @param requestInfo
	 * @return ServiceResponse
	 */
	public ServiceResponse getDefaultServiceResponse(RequestInfo requestInfo) {
		return new ServiceResponse(
				new ArrayList<Services>(), new ArrayList<ActionHistory>());
	}

	/**
	 * Default response is responseInfo with error status and zero count
	 * 
	 * @param requestInfo
	 * @return CountResponse
	 */
	public CountResponse getDefaultCountResponse(RequestInfo requestInfo) {
		return new CountResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false), 0D);
	}

	/**
	 * Returns mapper with all the appropriate properties reqd in our
	 * functionalities.
	 * 
	 * @return ObjectMapper
	 */
	public ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		return mapper;
	}

//	/**
//	 * prepares and returns a map with integer keys starting from zero and values as
//	 * role codes the integer values decides the precedence by which actions should
//	 * be applied among there roles
//	 */
//	private static Map<Integer, String> prepareEmployeeRolesPrecedenceMap() {
//
//		Map<Integer, String> map = new TreeMap<>();
//
//		map.put(2, PGRConstants.ROLE_L3);
//		map.put(1, PGRConstants.ROLE_L2);
//		map.put(0, PGRConstants.ROLE_L1);
//
//		return map;
//	}
//
//	/**
//	 * @return employeeRolesPrecedenceMap
//	 */
//	public static Map<Integer, String> getEmployeeRolesPrecedenceMap() {
//		return employeeRolesPrecedenceMap;
//	}

	/**
	 * Helper method which returns the precedent role among all the given roles
	 * 
	 * The employee precedent map is a tree map which will have the roles ordered
	 * based on their keys precedence
	 * 
	 * The method will fail if the list of roles is null, so the parameter must be
	 * null checked
	 * 
	 * If the none of roles in the precedence map has a match in roles object then
	 * the method will return null
	 */
//	public String getPrecedentRole(List<String> roles) {
//		
//		for (Entry<Integer, String> entry : PGRUtils.getEmployeeRolesPrecedenceMap().entrySet()) {
//			String currentValue = entry.getValue();
//			if (roles.contains(currentValue))
//				return currentValue;
//		}
//		return null;
//	}


	/**
	 * Splits any camelCase to human readable string
	 * @param String
	 * @return String
	 */
	public static String splitCamelCase(String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}
	
	public Long convertToMilliSec(Integer hours) {
		Long milliseconds = TimeUnit.SECONDS.toMillis(TimeUnit.HOURS.toSeconds(hours));
		log.info("SLA in ms: "+milliseconds);
		return milliseconds;
	}
	
	
	
	/**
	 * returns the current status of the service
	 * 
	 * @param requestInfo
	 * @param actionInfo
	 * @param currentStatusList
	 * @return
	 */
	public String getCurrentStatus(ActionHistory history) {
		List<ActionInfo> infos = history.getActions();
		//FIXME pickup latest status another way which is not hardocoded, put query to searcher to pick latest status
		// or use status from service object
		for (int i = 0; i <= infos.size() - 1; i++) {
			String status = infos.get(i).getStatus();
			if (null != status) {
				return status;
			}
		}
		
		return null;
	}
	
	/**
	 * helper method to add the errors to the error map
	 * 
	 * @param errorMsg
	 * @param key
	 * @param errorMap
	 */
	private void addError(String errorMsg, String key, Map<String, List<String>> errorMap) {

		List<String> errors = errorMap.get(key);
		if (null == errors) {
			errors = Arrays.asList(errorMsg);
			errorMap.put(key, errors);
		} else
			errors.add(errorMsg);
	}
//	/**
//	 * Check whether the complaint is auto escalated without resolved
//	 * 
//	 * @param actionInfo
//	 * @return boolean
//	 */
//	public boolean checkAutoEscalatedWithoutResolved(ActionHistory history) {
//		
//		List<String> status = history.getActions().stream().map(ActionInfo::getStatus).collect(Collectors.toList());
//		int resolvedCount = 0 ;
//		int rejectedCount = 0 ;
//		
//		for (String actionStatus : status) {
//			if(actionStatus!=null &&  actionStatus.equalsIgnoreCase(WorkFlowConfigs.STATUS_RESOLVED))
//				resolvedCount++;
//		}
//		
//		for (String actionStatus : status) {
//			if(actionStatus!=null && actionStatus.equalsIgnoreCase(WorkFlowConfigs.STATUS_REJECTED))
//				rejectedCount++;
//		}
//		
//		int totalCount = rejectedCount+resolvedCount ;
//		
//		if(status.contains(WorkFlowConfigs.STATUS_ESCALATED_LEVEL4_PENDING) && totalCount ==3)
//			return true;
//		else if(status.contains(WorkFlowConfigs.STATUS_ESCALATED_LEVEL3_PENDING) && totalCount ==2)
//			return true;
//		else if(status.contains(WorkFlowConfigs.STATUS_ESCALATED_LEVEL2_PENDING) && totalCount ==1)
//			return true;
//		else if(status.contains(WorkFlowConfigs.STATUS_ESCALATED_LEVEL1_PENDING) && totalCount ==0)
//			return true;
//
//		return false;
//	}
//	
	
	/**
	 * Check whether the complaint resolved time has passed the closure time . 
	 * 
	 * @param actionInfo
	 * @return boolean
	 */
	public boolean checkClosureTimeCompleted(ActionHistory history) {
		
		String currentStatus = getCurrentStatus(history);
		
		/**
		 * Code to check if the reopen happens within defaultExpiryTimeBeforeReopen no of days after resolve. 
		 */
		if(currentStatus.equals(WorkFlowConfigs.STATUS_RESOLVED)) {
			if(null != getLastModifiedTime(null, history)) {
				Long timeDifference = new Date().getTime() - getLastModifiedTime(null, history);
				if(TimeUnit.MILLISECONDS.toHours(timeDifference) > defaultExpiryTimeBeforeReopen) {
					return true ;
				}
				
			}
		}
		

		return false;
	}
	
	
	/**
	 * Check whether the escalated complaint is resolving/rejecting by any employee
	 * 
	 * @param actionInfo
	 * @return boolean
	 */
//	public boolean checkComplaintAlreadyEscalated(ActionHistory history, String action) {
//		List<ActionInfo> infos = history.getActions();
//		
//		for (int i = 0; i <= infos.size() - 1; i++) {
//			String status = infos.get(i).getStatus();
//			if ((WorkFlowConfigs.STATUS_ESCALATED_LEVEL1_PENDING.equalsIgnoreCase(status)
//					|| WorkFlowConfigs.STATUS_ESCALATED_LEVEL2_PENDING.equalsIgnoreCase(status) || WorkFlowConfigs.STATUS_ESCALATED_LEVEL3_PENDING.equalsIgnoreCase(status)
//					|| WorkFlowConfigs.STATUS_ESCALATED_LEVEL4_PENDING.equalsIgnoreCase(status))
//					&& (WorkFlowConfigs.ACTION_RESOLVE.equalsIgnoreCase(action) 
//						|| WorkFlowConfigs.ACTION_REJECT.equalsIgnoreCase(action))){
//				return true;
//			}
//		}
//		return false;
//	}

//	/**
//	 * Check whether the complaint is reopened for 2nd,3rd and 4th time or not
//	 * 
//	 * @param actionInfo
//	 * @return String
//	 */
//	public String checkReopenForEscalation(RequestInfo requestInfo, ActionHistory history, String action ,String tenantId) {
//		List<ActionInfo> infos = history.getActions();
//		List<String>  statusList = new ArrayList() ;
//		
//				
//		for (int i = 0; i <= infos.size() - 1; i++) {
//			String status = infos.get(i).getStatus();
//			
//			if(status!= null && !status.trim().isEmpty())
//			statusList.add(status.toLowerCase());
//		}
//		
//		
//		
//		if( WorkFlowConfigs.ACTION_REOPEN.equalsIgnoreCase(action))
//		{
//		
//		if(statusList.contains(WorkFlowConfigs.STATUS_ESCALATED_LEVEL3_PENDING.toLowerCase()))
//		{
//			return WorkFlowConfigs.STATUS_ESCALATED_LEVEL4_PENDING;
//		}else if(statusList.contains(WorkFlowConfigs.STATUS_ESCALATED_LEVEL2_PENDING.toLowerCase()))
//		{
//			return WorkFlowConfigs.STATUS_ESCALATED_LEVEL3_PENDING;
//		}else if(statusList.contains(WorkFlowConfigs.STATUS_ESCALATED_LEVEL1_PENDING.toLowerCase()))
//		{
//			//Customized logic based on the ULB Grade
//			List<String>   ulbGrade = getUlbGrade(requestInfo, tenantId);
//			
//			if(ulbGrade.get(0).equalsIgnoreCase(PGRConstants.ULB_GRADE_MUNICIPAL_CORPORATION))
//			{
//				return WorkFlowConfigs.STATUS_ESCALATED_LEVEL4_PENDING;
//			}
//			
//			
//			return WorkFlowConfigs.STATUS_ESCALATED_LEVEL2_PENDING;
//		}else 
//			return null;
//		}
//		
//		
//		return null;
//	}
//	
	
	public long getLastDayTime(int slaDays) {
		
		LocalDate today = LocalDate.now(ZoneId.of("Asia/Kolkata"));
		LocalDateTime todayMidnight = today.atTime(LocalTime.MAX);
		LocalDateTime slaendMidnight = todayMidnight.plusDays(slaDays);
		log.info("SLA end Date Midnight in IST="+slaendMidnight);
		return slaendMidnight.atZone(ZoneId.of("Asia/Kolkata")).toInstant().toEpochMilli();
	}
	
	public Long getLastModifiedTime(Services service, ActionHistory history) {
		Long lasModifiedTime = null;
//		//Search will always return actions in the order of latest action - oldest action.
//		if(null == service.getAuditDetails().getLastModifiedTime())
//			lasModifiedTime = history.getActions().get(0).getWhen(); //time when the latest action was taken
//		else
//			lasModifiedTime = service.getAuditDetails().getLastModifiedTime();
		
		// Getting the last resolved time 
		List<ActionInfo> actions = history.getActions().stream()
				.filter(obj -> !StringUtils.isEmpty(obj.getStatus()) && WorkFlowConfigs.STATUS_RESOLVED.equalsIgnoreCase(obj.getStatus()) ).sorted(Comparator.comparing(ActionInfo::getCreatedTime).reversed()).collect(Collectors.toList());
		
		lasModifiedTime = actions.get(0).getCreatedTime();
		
		return lasModifiedTime;
		
	}
	
	
}
