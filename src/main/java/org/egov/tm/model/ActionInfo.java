package org.egov.tm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.egov.tm.model.Services.StatusEnum;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Capture the details of action on service request.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Entity
@Table(name = "eg_sr_action")
public class ActionInfo   {
	
	 @Id
	 @Column(name="uuid")
  @JsonProperty("uuid")
  private String uuid;

  @JsonProperty("createdBy")
  @Column(name="createdby")
  private String createdBy = null;

  @JsonProperty("isInternal")
  @Column(name="isinternal")
  private Boolean isInternal = true;

  @JsonProperty("createdTime")
  @Column(name="createdtime")
  private Long createdTime = null;

  @JsonProperty("businessKey")
  @Column(name="businesskey")
  private String businessKey = null;

  @JsonProperty("action")
  @Column(name="action")
  private String action = null;

  @JsonProperty("status")
  @Column(name="status")
  private String status = null;

  @JsonProperty("assignee")
  @Column(name="assignee")
  private String assignee = null;

  @JsonProperty("comments")
  @Pattern(regexp = "^[a-zA-Z0-9!@#.,/:; ()&']*$")
  @Size(max=256)
  @Column(name="comments")
  private String comment = null;

 
}
