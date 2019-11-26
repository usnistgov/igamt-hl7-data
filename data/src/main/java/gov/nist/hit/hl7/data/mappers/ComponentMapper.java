package gov.nist.hit.hl7.data.mappers;

import gov.nist.hit.hl7.data.enities.ComponentRow;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ComponentMapper implements RowMapper<ComponentRow> {
    @Override
    public ComponentRow mapRow(ResultSet resultSet, int i) throws SQLException {
        ComponentRow row = new ComponentRow();
        row.description = resultSet.getString("description");
        row.position = resultSet.getInt("position");
        row.usage = resultSet.getString("usage");
        row.length = resultSet.getString("length");
        row.minLength = resultSet.getString("minLength");
        row.maxLength = resultSet.getString("maxLength");
        row.confLength = resultSet.getString("confLength");
        row.table = resultSet.getString("table");
        row.datatype = resultSet.getString("datatype");
        return row;
    }
}
