package gov.nist.hit.hl7.data.mappers;

import gov.nist.hit.hl7.data.enities.SegmentRow;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SegmentMapper implements RowMapper<SegmentRow> {
    @Override
    public SegmentRow mapRow(ResultSet resultSet, int i) throws SQLException {
        SegmentRow row = new SegmentRow();
        row.description = resultSet.getString("description");
        row.seg_code = resultSet.getString("seg_code");
        row.hl7_version = resultSet.getString("hl7_version");
        return row;
    }
}
