package org.egov.tm.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.tm.model.ActionHistory;
import org.egov.tm.model.Services;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Response to the service request
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-03-23T08:00:37.661Z")

@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse   {
  @JsonProperty("services")
  @Valid
  private List<Services> services = new ArrayList<Services>();

  @JsonProperty("actionHistory")
  @Valid
  private List<ActionHistory> actionHistory = null;


  public ServiceResponse services(List<Services> services) {
    this.services = services;
    return this;
  }

  public ServiceResponse addServicesItem(Services servicesItem) {
    this.services.add(servicesItem);
    return this;
  }

  /**
   * Get services
   * @return services
  **/
  @NotNull

  @Valid

  public List<Services> getServices() {
    return services;
  }

  public void setServices(List<Services> services) {
    this.services = services;
  }

  public ServiceResponse actionHistory(List<ActionHistory> actionHistory) {
    this.actionHistory = actionHistory;
    return this;
  }

  public ServiceResponse addActionHistoryItem(ActionHistory actionHistoryItem) {
    if (this.actionHistory == null) {
      this.actionHistory = new ArrayList<ActionHistory>();
    }
    this.actionHistory.add(actionHistoryItem);
    return this;
  }

  /**
   * Get actionHistory
   * @return actionHistory
  **/
  @Valid

  public List<ActionHistory> getActionHistory() {
    return actionHistory;
  }

  public void setActionHistory(List<ActionHistory> actionHistory) {
    this.actionHistory = actionHistory;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceResponse serviceResponse = (ServiceResponse) o;
    return 
        Objects.equals(this.services, serviceResponse.services) &&
        Objects.equals(this.actionHistory, serviceResponse.actionHistory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(services, actionHistory);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceResponse {\n");
    sb.append("    services: ").append(toIndentedString(services)).append("\n");
    sb.append("    actionHistory: ").append(toIndentedString(actionHistory)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

