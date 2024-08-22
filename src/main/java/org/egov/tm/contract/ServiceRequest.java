package org.egov.tm.contract;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.tm.model.ActionInfo;
import org.egov.tm.model.Services;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object to fetch the report data
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceRequest   {
  
  @JsonProperty("services")
  @Valid
  @NotNull
  private List<Services> services = new ArrayList<Services>();

  @JsonProperty("actionInfo")
  @Valid
  private List<ActionInfo> actionInfo = new ArrayList<ActionInfo>();

}

