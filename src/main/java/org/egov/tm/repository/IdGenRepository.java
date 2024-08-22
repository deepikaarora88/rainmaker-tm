package org.egov.tm.repository;


import java.util.ArrayList;
import java.util.List;

import org.egov.tm.contract.IdGenerationRequest;
import org.egov.tm.contract.IdGenerationResponse;
import org.egov.tm.contract.IdRequest;
import org.egov.tm.service.IdGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class IdGenRepository {



    private RestTemplate restTemplate;


    @Autowired
	private IdGenerationService idGenerationService;

    @Autowired
    public IdGenRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
       
    }


    /**
     * Call iDgen to generateIds
     * @param requestInfo The rquestInfo of the request
     * @param tenantId The tenantiD of the service request
     * @param name Name of the foramt
     * @param format Format of the ids
     * @param count Total Number of idGen ids required
     * @return
     * @throws Exception 
     */
    public IdGenerationResponse getId(String name, String format, int count) throws Exception {

        List<IdRequest> reqList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            reqList.add(IdRequest.builder().idName(name).format(format).build());
        }
        IdGenerationRequest req = IdGenerationRequest.builder().idRequests(reqList).requestInfo(null).build();
        IdGenerationResponse response = idGenerationService.generateIdResponse(req);
        return response;
    }



}
