package org.egov.tm.contract;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>IdRequest</h1>
 * 
 * @author Narendra
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdRequest {

	@JsonProperty("idName")
	@NotNull
	private String idName;


	@JsonProperty("format")
	private String format;
	
	@JsonProperty("count")
	private Integer count;

}
