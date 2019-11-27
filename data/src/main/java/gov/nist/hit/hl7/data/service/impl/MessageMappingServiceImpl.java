package gov.nist.hit.hl7.data.service.impl;

import gov.nist.hit.hl7.data.enities.EventRow;
import gov.nist.hit.hl7.data.enities.MessageStructureRow;
import gov.nist.hit.hl7.data.enities.SegmentGroupRow;
import gov.nist.hit.hl7.data.mappers.EventRowMapper;
import gov.nist.hit.hl7.data.mappers.MessageStructureRowMapper;
import gov.nist.hit.hl7.data.mappers.SegmentAndGroupRowMapper;
import gov.nist.hit.hl7.data.service.MessageStructureMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageMappingServiceImpl implements MessageStructureMappingService {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<MessageStructureRow> findAllStructures() {
        String query = "SELECT DISTINCT emt.message_typ_snd AS 'message type', mt.description as 'message type description',  emt.message_structure_snd as 'message structure', v.hl7_version as 'version'\n" +
                "FROM hl7eventmessagetypes as emt\n" +
                "LEFT JOIN hl7versions as v\n" +
                "ON v.version_id = emt.version_id\n" +
                "LEFT JOIN hl7events AS e\n" +
                "ON e.version_id = v.version_id AND e.event_code = emt.event_code\n" +
                "LEFT JOIN hl7messagetypes AS mt\n" +
                "ON mt.version_id = v.version_id AND emt.message_typ_snd = mt.message_type\n" +
                "WHERE  v.hl7_version IN('2.4', '2.5', '2.5.1', '2.6', '2.7', '2.7.1', '2.7.2', '2.8', '2.8.1', '2.8.2', '2.9')\n" +
                "AND (emt.message_structure_snd, emt.version_id) \n" +
                "IN (\n" +
                "\tSELECT mss.message_structure, mss.version_id \n" +
                "    FROM hl7msgstructidsegments AS mss)\n" +
                "UNION\n" +
                "-- receiving part\n" +
                "SELECT DISTINCT emt.message_typ_return AS 'message type', mt.description as 'message type description', emt.message_structure_return as 'message structure', v.hl7_version as 'version'\n" +
                "FROM hl7eventmessagetypes as emt\n" +
                "LEFT JOIN hl7versions as v\n" +
                "ON v.version_id = emt.version_id\n" +
                "LEFT JOIN hl7events AS e\n" +
                "ON e.version_id = v.version_id AND e.event_code = emt.event_code\n" +
                "LEFT JOIN hl7messagetypes AS mt\n" +
                "ON mt.version_id = v.version_id AND emt.message_typ_return = mt.message_type\n" +
                "WHERE v.hl7_version IN('2.4', '2.5', '2.5.1', '2.6', '2.7', '2.7.1', '2.7.2', '2.8', '2.8.1', '2.8.2', '2.9')\n" +
                "AND (emt.message_structure_return, emt.version_id) \n" +
                "IN (\n" +
                "\tSELECT mss.message_structure, mss.version_id \n" +
                "    FROM hl7msgstructidsegments AS mss);\n";
        return this.namedParameterJdbcTemplate.query(query, new MessageStructureRowMapper());

    }

    @Override
    public List<SegmentGroupRow> findChildrenByVersionAndStrcutureID(String version, String structID) {
        String query = "SELECT mss.message_structure, mss.seq_no, \n" +
                "REGEXP_REPLACE(mss.seg_code,'[\\\\[\\\\{\\\\]\\\\}]','') AS 'segment name',\n" +
                "REGEXP_REPLACE(mss.groupname,'[^[:alnum:]-_]','_') AS 'group name',\n" +
                "REGEXP_REPLACE(mss.seg_code,'[^\\\\[\\\\{\\\\]\\\\}]','') AS' group braces/brackets',\n" +
                "CASE \n" +
                "\tWHEN mss.seg_code LIKE '%[%' OR  mss.seg_code LIKE '%{%'THEN 'START'\n" +
                "\tWHEN mss.seg_code LIKE '%]%' OR  mss.seg_code LIKE '%}%'THEN 'END'\n" +
                "    ELSE ''\n" +
                "END as 'BORDER',\n" +
                "CASE\n" +
                "\tWHEN mss.seg_code LIKE '%]%' OR  mss.seg_code LIKE '%}%'THEN ''\n" +
                "\tWHEN mss.optional = 'FALSE' AND mss.seg_code LIKE '%[%' THEN 'O' \n" +
                "\tWHEN mss.optional = 'FALSE' AND mss.seg_code NOT LIKE '%[%' THEN 'R' \n" +
                "    WHEN mss.optional = 'TRUE' THEN 'O'\n" +
                "END as 'usage', -- convert optional and brackets into usage (R or O)\n" +
                "CASE \n" +
                "\tWHEN mss.seg_code LIKE '%]%' OR  mss.seg_code LIKE '%}%'THEN ''\n" +
                "\tWHEN mss.optional = 'FALSE' AND mss.seg_code LIKE '%[%' THEN 0 \n" +
                "\tWHEN mss.optional = 'FALSE' AND mss.seg_code NOT LIKE '%[%' THEN 1 \n" +
                "    WHEN mss.optional = 'TRUE' THEN 0\n" +
                "END as 'minCard', \n" +
                "CASE \n" +
                "\tWHEN mss.seg_code LIKE '%]%' OR  mss.seg_code LIKE '%}%'THEN ''\n" +
                "\tWHEN mss.repetitional = 'FALSE' AND mss.seg_code LIKE '%{%' THEN '*' \n" +
                "\tWHEN mss.repetitional = 'FALSE' AND mss.seg_code NOT LIKE '%{%' THEN 1 \n" +
                "    WHEN mss.repetitional = 'TRUE' THEN '*'\n" +
                "END as 'maxCard', -- convert repetitional and braces into maximum cardinality\n" +
                "v.hl7_version \n" +
                "FROM hl7msgstructidsegments as mss\n" +
                "LEFT JOIN hl7versions as v\n" +
                "ON v.version_id = mss.version_id\n" +
                "WHERE v.hl7_version = :version\n" +
                "AND mss.message_structure = :message_structure_id\n" +
                "ORDER BY mss.seq_no;";

        SqlParameterSource p = new MapSqlParameterSource().addValue("version", version).addValue("message_structure_id", structID);
        return this.namedParameterJdbcTemplate.query(query ,p,  new SegmentAndGroupRowMapper());
    }

    @Override
    public List<EventRow> findEventByVersionAndStructure(String version, String structID) {
        String query ="SELECT DISTINCT emt.message_typ_snd AS 'message type', mt.description as 'message type description', emt.event_code as 'event code', e.description as 'event description', emt.message_structure_snd as 'message structure', v.hl7_version as 'version'\n" +
                "FROM hl7eventmessagetypes as emt\n" +
                "LEFT JOIN hl7versions as v\n" +
                "ON v.version_id = emt.version_id\n" +
                "LEFT JOIN hl7events AS e\n" +
                "ON e.version_id = v.version_id AND e.event_code = emt.event_code\n" +
                "LEFT JOIN hl7messagetypes AS mt\n" +
                "ON mt.version_id = v.version_id AND emt.message_typ_snd = mt.message_type\n" +
                "WHERE v.hl7_version =:version\n" +
                "AND emt.message_structure_snd= :message_structure_id\n" +
                "AND (emt.message_structure_snd, emt.version_id) \n" +
                "IN (\n" +
                "\tSELECT mss.message_structure, mss.version_id \n" +
                "    FROM hl7msgstructidsegments AS mss)\n" +
                "UNION\n" +
                "SELECT DISTINCT emt.message_typ_return AS 'message type', mt.description as 'message type description', emt.event_code as 'event code', e.description as 'event description', emt.message_structure_return as 'message structure', v.hl7_version as 'version'\n" +
                "FROM hl7eventmessagetypes as emt\n" +
                "LEFT JOIN hl7versions as v\n" +
                "ON v.version_id = emt.version_id\n" +
                "LEFT JOIN hl7events AS e\n" +
                "ON e.version_id = v.version_id AND e.event_code = emt.event_code\n" +
                "LEFT JOIN hl7messagetypes AS mt\n" +
                "ON mt.version_id = v.version_id AND emt.message_typ_return = mt.message_type\n" +
                "WHERE v.hl7_version =:version\n" +
                "AND emt.message_structure_return=:message_structure_id\n" +
                "AND (emt.message_structure_return, emt.version_id) \n" +
                "IN (\n" +
                "\tSELECT mss.message_structure, mss.version_id \n" +
                "    FROM hl7msgstructidsegments AS mss);\n" +
                "\n";
        SqlParameterSource p = new MapSqlParameterSource().addValue("version", version).addValue("message_structure_id", structID);
        return this.namedParameterJdbcTemplate.query(query ,p,  new EventRowMapper());
    }

}
