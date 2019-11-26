package gov.nist.hit.hl7.data.mappers;

import gov.nist.hit.hl7.data.enities.EventRow;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRowMapper implements RowMapper<EventRow> {
    @Override
    public EventRow mapRow(ResultSet resultSet, int i) throws SQLException {



        return null;
    }
}
