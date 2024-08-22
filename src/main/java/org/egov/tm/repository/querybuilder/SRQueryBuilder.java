package org.egov.tm.repository.querybuilder;

import java.util.Map;

import org.egov.tm.contract.ServiceReqSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class SRQueryBuilder {
	
	public static final String INNER_JOIN = " inner join ";
	public static final String SR_APPLICATIONS_QUERY = "select * from eg_sr_service sr ";
	
	public static String getSRApplications(ServiceReqSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(SR_APPLICATIONS_QUERY);
		return addWhereClause(selectQuery, preparedStatementValues, criteria);
	}


	private static String addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			ServiceReqSearchCriteria searchCriteria) {
		
		if (!checkIfSearchBasedOnParams(searchCriteria)) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" sr.status NOT IN (:status) ");
			preparedStatementValues.put("status", "closed");
		}
		
		if (!StringUtils.isEmpty(searchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" sr.tenantid IN ( :tenantId )");
			preparedStatementValues.put("tenantId",searchCriteria.getTenantId());
		}

		if (!StringUtils.isEmpty(searchCriteria.getServiceRequestId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" sr.servicerequestid in (:serviceRequestId)");
			preparedStatementValues.put("serviceRequestId", searchCriteria.getServiceRequestId());
		}

		if (!StringUtils.isEmpty(searchCriteria.getPhone())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" sr.phone = :phone");
			preparedStatementValues.put("phone", searchCriteria.getPhone());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" sr.status IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		} 
		
		if (!StringUtils.isEmpty(searchCriteria.getAccountId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" sr.accountid = :accountId ");
			preparedStatementValues.put("accountId", searchCriteria.getAccountId());
		}
		
		if (!StringUtils.isEmpty(searchCriteria.getService())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" sr.service = :service ");
			preparedStatementValues.put("service", searchCriteria.getService());
		}
		
		if (!StringUtils.isEmpty(searchCriteria.getCity())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" sr.city = :city ");
			preparedStatementValues.put("city", searchCriteria.getCity());
		}
		
		selectQuery.append(" order by servicerequestid ");
		
		addPaginationWrapper(preparedStatementValues, selectQuery, searchCriteria);
		
		return selectQuery.toString();
	}

	private static boolean checkIfSearchBasedOnParams(ServiceReqSearchCriteria searchCriteria) {
		
		if(StringUtils.isEmpty(searchCriteria.getStatus()) && StringUtils.isEmpty(searchCriteria.getServiceRequestId())
				&& StringUtils.isEmpty(searchCriteria.getCity()) && StringUtils.isEmpty(searchCriteria.getPhone())
				&& StringUtils.isEmpty(searchCriteria.getService())) {
			return false;
		}	
		return true;
	}


	private static void addPaginationWrapper(Map<String, Object> preparedStatementValues, StringBuilder selectQuery,
			ServiceReqSearchCriteria searchCriteria) {

		if (!StringUtils.isEmpty(searchCriteria.getNoOfRecords())) {
			selectQuery.append(" limit :limit ");
			Long limit = searchCriteria.getNoOfRecords();
			
			if(limit < 0 || limit > 50) {
				preparedStatementValues.put("limit", 50);
			} else {
				preparedStatementValues.put("limit", searchCriteria.getNoOfRecords());
			}
			
		}
		
		if (!StringUtils.isEmpty(searchCriteria.getOffset())) {
			selectQuery.append(" offset :offset ");
			preparedStatementValues.put("offset", searchCriteria.getOffset());
		}

	}


	private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}

}
