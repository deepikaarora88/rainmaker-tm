package org.egov.tm.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.egov.tm.model.AuditDetails;
import org.egov.tm.model.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceRowMapper implements ResultSetExtractor<List<Services>> {

    @Autowired
    private ObjectMapper objectMapper;

    public List<Services> extractData(ResultSet rs) throws SQLException, DataAccessException {

       List<Services> serviceList = new ArrayList<>();

        while (rs.next()) {

           
            Services currentService = Services.builder()
                    .module(rs.getString("module"))
                    .serviceRequestId(rs.getString("servicerequestid"))
                    .description(rs.getString("description"))
                    .status(rs.getString("status"))
                    .active(rs.getBoolean("active"))
                    .uuid(rs.getString("uuid"))
                    .ticketType(rs.getString("tickettype"))
                    .ticketSubType(rs.getString("ticketsubtype"))
                    .documentId(rs.getString("documentid"))
                    .priority(rs.getString("priority"))
                    .assignee(rs.getString("assignee"))
                    .applicationNo(rs.getString("applicationno"))
                    .createdBy(rs.getString("createdby"))
                    .createdByType(rs.getString("createdbytype"))
                    .lastModifiedBy(rs.getString("lastmodifiedby"))
                    .lastModifiedByType(rs.getString("lastmodifiedbytype"))
                    .build();


            serviceList.add(currentService);
        }
        return serviceList;
    }
}
