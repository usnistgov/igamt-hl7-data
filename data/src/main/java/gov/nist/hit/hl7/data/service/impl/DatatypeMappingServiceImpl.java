package gov.nist.hit.hl7.data.service.impl;

import gov.nist.hit.hl7.data.enities.ComponentRow;
import gov.nist.hit.hl7.data.enities.DatatypeRow;
import gov.nist.hit.hl7.data.mappers.ComponentMapper;
import gov.nist.hit.hl7.data.mappers.DatatypeMapper;
import gov.nist.hit.hl7.data.mappers.FieldMapper;
import gov.nist.hit.hl7.data.service.DatatypeMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DatatypeMappingServiceImpl  implements DatatypeMappingService {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<DatatypeRow> findAll() {
        String query = "SELECT DISTINCT d.data_type_code, d.description, \n" +
                "CASE ds.elementary \n" +
                "\tWHEN 'TRUE' THEN 'primitive'\n" +
                "    WHEN 'FALSE' THEN 'complex'\n" +
                "END as type, \n" +
                "v.hl7_version \n" +
                "FROM hl7datatypes d\n" +
                "LEFT JOIN hl7versions as v\n" +
                "ON d.version_id = v.version_id\n" +
                "LEFT JOIN hl7datastructures as ds\n" +
                "ON (ds.data_type_code = d.data_type_code OR ds.data_structure = d.data_type_code)\n" +
                "AND ds.version_id = v.version_id\n" +
                "WHERE v.hl7_version IN ('2.3.1', '2.4', '2.5', '2.5.1', '2.6', '2.7', '2.7.1', '2.7.2', '2.8', '2.8.1', '2.8.2', '2.9');";

        return this.namedParameterJdbcTemplate.query(query , new DatatypeMapper());
    }

    @Override
    public DatatypeRow findByNameAndVersion() {
        return null;
    }

    @Override
    public List<ComponentRow> findChildrenByNameAndVersion(String name, String version) {
        String query = "SELECT v.hl7_version AS version, dsc.data_structure AS element, dsc.seq_no AS position, c.description AS 'description', \n" +
                "dsc.req_opt AS 'usage', dsc.length_old AS length, dsc.min_length AS minLength, dsc.max_length AS maxLength, dsc.conf_length AS confLength, \n" +
                "CASE c.table_id WHEN 0 THEN dsc.table_id ELSE c.table_id END as 'table_id', \n" +
                "LPAD(CASE c.table_id WHEN 0 THEN dsc.table_id ELSE c.table_id END, 4,'0') as 'table', \n" +
                "c.data_type_code AS datatype\n" +
                "FROM hl7datastructurecomponents AS dsc\n" +
                "LEFT JOIN hl7components AS c\n" +
                "ON dsc.comp_no = c.comp_no\n" +
                "AND dsc.version_id = c.version_id\n" +
                "LEFT JOIN hl7versions as v\n" +
                "ON dsc.version_id = v.version_id\n" +
                "WHERE dsc.data_structure = :datatype\n" +
                "AND v.hl7_version= :hl7_version ;";

        SqlParameterSource p = new MapSqlParameterSource().addValue("hl7_version", version).addValue("datatype", name);
        return this.namedParameterJdbcTemplate.query(query ,p , new ComponentMapper());
    }
}
