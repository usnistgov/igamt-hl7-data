package gov.nist.hit.hl7.data.service.impl;

import gov.nist.hit.hl7.data.enities.FieldRow;
import gov.nist.hit.hl7.data.enities.SegmentRow;
import gov.nist.hit.hl7.data.mappers.FieldMapper;
import gov.nist.hit.hl7.data.mappers.SegmentMapper;
import gov.nist.hit.hl7.data.service.SegmentMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SegmentMappingServiceImpl implements SegmentMappingService {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<SegmentRow> findAll() {
        String query = "SELECT s.seg_code, s.description, v.hl7_version \n" +
                "FROM hl7segments s\n" +
                "LEFT JOIN hl7versions as v\n" +
                "ON s.version_id = v.version_id\n" +
                "WHERE s.seg_code REGEXP '[A-Z0-1]{3}'\n" +
                "AND v.hl7_version IN ('2.3.1', '2.4', '2.5', '2.5.1', '2.6', '2.7', '2.7.1', '2.7.2', '2.8', '2.8.1', '2.8.2', '2.9')";
        return this.namedParameterJdbcTemplate.query(query, new SegmentMapper());
    }

    @Override
    public SegmentRow findByNameAndVersion() {
        return null;
    }

    @Override
    public List<FieldRow> findChildrenByNameAndVersion(String name, String version) {
        String query = "SELECT sde.seg_code as segment, sde.seq_no as position,\n" +
                "de.description AS 'description',\n" +
                "sde.req_opt AS 'usage',\n" +
                "CASE sde.req_opt \n" +
                "\tWHEN 'R' THEN 1 \n" +
                "    ELSE 0 \n" +
                "END as minCard,\n" +
                "CASE \n" +
                "\tWHEN sde.repetitional='Y' AND sde.repetitions REGEXP '[1-9][0-9]*' THEN sde.repetitions\n" +
                "\tWHEN sde.repetitional='Y' THEN '*' \n" +
                "    ELSE '1'\n" +
                "END as maxCard,\n" +
                "de.length_old AS length, de.min_length AS minLength, de.max_length AS maxLength, de.conf_length AS confLength,\n" +
                "de.table_id as table_id, LPAD(de.table_id, 4,'0') AS 'table',\n" +
                "ds.data_type_code as datatype,\n" +
                "v.hl7_version as version \n" +
                "FROM hl7segmentdataelements sde\n" +
                "LEFT JOIN hl7versions as v\n" +
                "ON sde.version_id = v.version_id\n" +
                "LEFT JOIN hl7dataelements as de\n" +
                "ON sde.data_item = de.data_item\n" +
                "AND sde.version_id = de.version_id\n" +
                "LEFT JOIN hl7datastructures as ds\n" +
                "ON de.data_structure = ds.data_structure\n" +
                "AND de.version_id = ds.version_id\n" +
                "WHERE v.hl7_version= :hl7_version\n" +
                "AND sde.seg_code = :segment_name;";

        SqlParameterSource p = new MapSqlParameterSource().addValue("hl7_version", version).addValue("segment_name", name);
        return this.namedParameterJdbcTemplate.query(query ,p , new FieldMapper());
    }
}
