package org.egov.tm.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.tm.contract.ServiceReqSearchCriteria;
import org.egov.tm.model.Services;
import org.egov.tm.repository.querybuilder.SRQueryBuilder;
import org.egov.tm.repository.rowmapper.ServiceRowMapper;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ServiceRequestRepository {
		
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SRQueryBuilder srQueryBuilder;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
		
	/**
	 * Fetches results from searcher framework based on the uri and request that define what is to be searched.
	 * 
	 * @param requestInfo
	 * @param serviceReqSearchCriteria
	 * @return Object
	 * @author vishal
	 */
	public Object fetchResult(StringBuilder uri, Object request) {
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Object response = null;
		try {
			response = restTemplate.postForObject(uri.toString(), request, Map.class);
		}catch(HttpClientErrorException e) {
			log.error("External Service threw an Exception: ",e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		}catch(Exception e) {
			log.error("Exception while fetching from searcher: ",e);
		}
		
		return response;
		
	}

	public List<Services> searchFromDB(@Valid ServiceReqSearchCriteria serviceReqSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = srQueryBuilder.getSRApplications(serviceReqSearchCriteria, preparedStatementValues);
		log.info("query for fetching records: "+query);
		List<Services> services = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ServiceRowMapper());
		return services;
	}

}
