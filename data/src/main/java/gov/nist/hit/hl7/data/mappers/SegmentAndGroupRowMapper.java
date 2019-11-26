package gov.nist.hit.hl7.data.mappers;

import gov.nist.hit.hl7.data.enities.FieldRow;
import gov.nist.hit.hl7.data.enities.SegmentGroupRow;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SegmentAndGroupRowMapper implements RowMapper<SegmentGroupRow> {

    @Override
    public SegmentGroupRow mapRow(ResultSet resultSet, int i) throws SQLException {
        SegmentGroupRow row  = new SegmentGroupRow();
        row.groupStartEnd = resultSet.getString("BORDER");
        row.groupName = resultSet.getString("group name");
        row.segmentName = resultSet.getString("segment name");
        row.hl7Version = resultSet.getString("hl7_version");
        row.minCard = resultSet.getString("minCard");
        row.maxCard = resultSet.getString("maxCard");
        row.usage = resultSet.getString("usage");

        return row;
    }
}