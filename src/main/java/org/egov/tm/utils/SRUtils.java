package org.egov.tm.utils;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tm.repository.ServiceRequestRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SRUtils {
	
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
	
	@Value("${egov.localization.host}")
	private String localizationHost;

	@Value("${egov.localization.search.endpoint}")
	private String localizationSearchEndpoint;
	
	@SuppressWarnings("rawtypes")
	public String getLocalizationMessages(String tenantId, RequestInfo requestInfo) {

		LinkedHashMap responseMap = (LinkedHashMap) serviceRequestRepository.fetchResult(getUri(tenantId, requestInfo),
				requestInfo);
		String jsonString = new JSONObject(responseMap).toString();
		return jsonString;
	}
	
	public StringBuilder getUri(String tenantId, RequestInfo requestInfo) {

		String locale = "en_IN";

		StringBuilder uri = new StringBuilder();
		uri.append(localizationHost).append(localizationSearchEndpoint).append("?")
				.append("locale=").append(locale)
				.append("&tenantId=").append(tenantId).append("&module=").append(PGRConstants.LOCALIZATION_MODULE);
		return uri;
	}
	
	@SuppressWarnings("rawtypes")
	public String getMessageTemplate(String notificationCode, String localizationMessage) {
		String path = "$..messages[?(@.code==\"{}\")].message";
		path = path.replace("{}", notificationCode);
		String message = null;
		try {
			List data = JsonPath.parse(localizationMessage).read(path);
			if (!CollectionUtils.isEmpty(data))
				message = data.get(0).toString();
			else
				log.error("Fetching from localization failed with code " + notificationCode);
		} catch (Exception e) {
			log.warn("Fetching from localization failed", e);
		}
		return message;
	}

}
