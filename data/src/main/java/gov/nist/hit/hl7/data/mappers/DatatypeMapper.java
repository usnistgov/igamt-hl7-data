package gov.nist.hit.hl7.data.mappers;

import gov.nist.hit.hl7.data.enities.DatatypeRow;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatatypeMapper implements RowMapper<DatatypeRow> {
    @Override
    public DatatypeRow mapRow(ResultSet resultSet, int i) throws SQLException {
        DatatypeRow row = new DatatypeRow();
        row.description = resultSet.getString("description");
        row.data_type_code = resultSet.getString("data_type_code");
        row.hl7_version = resultSet.getString("hl7_version");
        row.type = resultSet.getString("type");
        return row;
    }
}
