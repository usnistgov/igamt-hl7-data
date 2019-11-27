package gov.nist.hit.hl7.data.mappers;

import gov.nist.hit.hl7.data.enities.MessageStructureRow;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageStructureRowMapper implements RowMapper<MessageStructureRow> {
    @Override
    public MessageStructureRow mapRow(ResultSet resultSet, int i) throws SQLException {
        MessageStructureRow row = new MessageStructureRow();
        row.description = resultSet.getString("message type description");
        row.messageType = resultSet.getString("message type");
        row.hl7Version = resultSet.getString("version");
        row.structID = resultSet.getString("message structure");

        return row;
    }
}