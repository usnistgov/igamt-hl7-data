package gov.nist.hit.hl7.data.mappers;

import gov.nist.hit.hl7.data.enities.FieldRow;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FieldMapper implements RowMapper<FieldRow> {

    @Override
    public FieldRow mapRow(ResultSet resultSet, int i) throws SQLException {
        FieldRow row  = new FieldRow();
        row.description = resultSet.getString("description");
        row.position = resultSet.getInt("position");
        row.usage = resultSet.getString("usage");
        row.length = resultSet.getString("length");
        row.minLength = resultSet.getString("minLength");
        row.maxLength = resultSet.getString("maxLength");
        row.confLength = resultSet.getString("confLength");
        row.table = resultSet.getString("table");
        row.datatype = resultSet.getString("datatype");
        row.minCard = resultSet.getString("minCard");
        row.maxCard = resultSet.getString("maxCard");
        row.version = resultSet.getString("version");

        return row;

    }
}
