package org.egov.tm.contract;

import java.util.List;

import javax.validation.Valid;

import org.egov.tm.model.ActionInfo;
import org.egov.tm.model.Services;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

  
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ServiceRequestDetails {
	
	  @JsonProperty("services")
	  @Valid
	  private Services services;

	  @JsonProperty("actionhistory")
	  @Valid
	  private List<ActionInfo> actionhistory;

}
