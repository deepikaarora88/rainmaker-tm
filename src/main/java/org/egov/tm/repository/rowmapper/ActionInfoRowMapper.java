package org.egov.tm.repository.rowmapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.egov.tm.model.ActionInfo;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ActionInfoRowMapper implements ResultSetExtractor<List<ActionInfo>> {

    @Autowired
    ObjectMapper objectMapper;

    public List<ActionInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<ActionInfo> actionInfoList = new ArrayList<>();
        while(rs.next()){
          

            ActionInfo actionInfo = ActionInfo.builder()
                    .uuid(rs.getString("uuid"))
                    .createdBy(rs.getString("createdby"))
                    .isInternal(rs.getBoolean("isinternal"))
                    .createdTime(rs.getLong("createdtime"))
                    .businessKey(rs.getString("businesskey"))
                    .action(rs.getString("action"))
                    .status(rs.getString("status"))
                    .assignee(rs.getString("assignee"))
                    .comment(rs.getString("comments"))
                    .build();
            actionInfoList.add(actionInfo);
        }
        return actionInfoList;
    }
}
