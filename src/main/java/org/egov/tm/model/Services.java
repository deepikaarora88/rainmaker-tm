package org.egov.tm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Instance of Service request raised for a particular service. As per extension propsed in the Service definition \&quot;attributes\&quot; carry the input values requried by metadata definition in the structure as described by the corresponding schema.  * Any one of &#39;address&#39; or &#39;(lat and lang)&#39; or &#39;addressid&#39; is mandatory 
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Entity
@Table(name = "eg_sr_service")
public class Services   {
	
  
  @Id
  @JsonProperty("uuid")
  @Column(name="uuid")
  private String uuid = null;

  @JsonProperty("module")
  @Column(name="module")
  private String module;

  @JsonProperty("ticketType")
  @Column(name="tickettype")
  private String ticketType;
  
  @JsonProperty("ticketSubType")
  @Column(name="ticketsubtype")
  private String ticketSubType;


  @JsonProperty("assignee")
  @Column(name="assignee")
  private String assignee;
  
  @JsonProperty("priority")
  @Column(name="priority")
  private String priority;

  @JsonProperty("active")
  @Column(name="active")
  private Boolean active;

  /**
   * The current status of the service request.
   */
  public enum StatusEnum {
	  
	OPENED("OPENED"),
	
	ASSIGNED("ASSIGNED"),
	        
    CLOSED("CLOSED"),
    
    REOPENED("REOPENED"),
    
    REJECTED("REJECTED"),
      
    RESOLVED("RESOLVED"),
	;

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equalsIgnoreCase(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("status")
  @Column(name="status")
  private String status;

  @JsonProperty("serviceRequestId")
  @Column(name="servicerequestid")
  private String serviceRequestId;
  
  @JsonProperty("applicationno")
  @Column(name="applicationno")
  private String applicationNo;

  @JsonProperty("description")
  @Column(name="description")
  @Size(max=1000)
  private String description;

  @JsonProperty("createdBy")
  @Column(name="createdby")
  private String createdBy = null;

  @JsonProperty("lastModifiedBy")
  @Column(name="lastmodifiedby")
  private String lastModifiedBy = null;

  @JsonProperty("createdByType")
  @Column(name="createdbytype")
  private String createdByType = null;

  @JsonProperty("lastModifiedByType")
  @Column(name="lastmodifiedbytype")
  private String lastModifiedByType = null;

  
  @JsonProperty("documentId")
  @Column(name="documentid")
  private String documentId = null;
  

  }

