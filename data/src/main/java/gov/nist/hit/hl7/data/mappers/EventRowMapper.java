package gov.nist.hit.hl7.data.mappers;

import gov.nist.hit.hl7.data.enities.EventRow;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRowMapper implements RowMapper<EventRow> {
    @Override
    public EventRow mapRow(ResultSet resultSet, int i) throws SQLException {
        EventRow row = new EventRow();

        row.description = resultSet.getString("event description");
        row.hl7Version = resultSet.getString("version");
        row.name = resultSet.getString("event code");

        return row;
    }
}
