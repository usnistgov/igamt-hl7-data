/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.hl7.data.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import gov.nist.hit.hl7.data.enities.CodeRow;
import gov.nist.hit.hl7.data.enities.ValueSetRow;
import gov.nist.hit.hl7.data.mappers.CodeRowMapper;
import gov.nist.hit.hl7.data.mappers.ValueSetRowMapper;
import gov.nist.hit.hl7.data.service.ValueSetMappingService;

/**
 * @author Abdelghani El Ouakili
 *
 */
@Service
public class ValueSetMappingServiceImpl implements ValueSetMappingService {

//	SqlParameterSource p = new MapSqlParameterSource()
//			.addValue("user", "wakili");
	@Autowired
	NamedParameterJdbcTemplate  namedParameterJdbcTemplate;
	
	
	@Override
	public List<ValueSetRow> findAllValueSets() {
		String query = "SELECT  LPAD(table_id, 4, '0') as bindingIdentifier, t.vs_oid as 'oid', tt.description as hl7TableType, v.hl7_version as version\n" + 
				"FROM hl7tables as t\n" + 
				"LEFT JOIN hl7versions as v\n" + 
				"ON t.version_id = v.version_id\n" + 
				"LEFT JOIN hl7tabletypes as tt\n" + 
				"ON t.table_type = tt.table_type\n" + 
				"WHERE v.hl7_version IN ('2.3.1', '2.4', '2.5', '2.5.1', '2.6', '2.7', '2.7.1', '2.7.2', '2.8', '2.8.1', '2.8.2', '2.9')\n" + 
				"ORDER BY t.table_id;";
		
		return this.namedParameterJdbcTemplate.query(query , new ValueSetRowMapper());
	}


	@Override
	public List<CodeRow> findCodesByOid(String oid) {
		String query = "SELECT DISTINCT t.vs_oid as 'value set oid', tv.table_value as value, tv.display_name as name, t.cs_oid as 'code system oid' , CONCAT('HL7',LPAD(t.table_id, 4, '0')) as 'code system','P' as 'usage' \n" + 
				"FROM hl7tablevalues as tv\n" + 
				"LEFT JOIN hl7tables as t\n" + 
				"ON tv.table_id = t.table_id\n" + 
				"AND tv.version_id = t.version_id\n" + 
				"WHERE t.vs_oid = :oid";
		
		SqlParameterSource p = new MapSqlParameterSource().addValue("oid", oid);
		return this.namedParameterJdbcTemplate.query(query ,p, new  CodeRowMapper());
	}


	@Override
	public List<ValueSetRow> findValueSetByVersion(String version) {
		String query = "SELECT t.table_id as table_id, LPAD(table_id, 4, '0') as bindingIdentifier, t.vs_oid as 'value set oid', t.cs_oid as 'code system oid' , CONCAT('HL7',LPAD(t.table_id, 4, '0')) as 'code system', tt.description as hl7TableType, v.hl7_version as version\n" + 
				"FROM hl7tables as t\n" + 
				"LEFT JOIN hl7versions as v\n" + 
				"ON t.version_id = v.version_id\n" + 
				"LEFT JOIN hl7tabletypes as tt\n" + 
				"ON t.table_type = tt.table_type\n" + 
				"WHERE v.hl7_version = :version";
		
		SqlParameterSource p = new MapSqlParameterSource().addValue("version", version);
		return this.namedParameterJdbcTemplate.query(query ,p, new ValueSetRowMapper());
	}


	@Override
	public List<CodeRow> findByCodesByBindingIdentifierAndVersion(String bindingIdentifer, String version) {
		// TODO Auto-generated method stub

		String query = " SELECT DISTINCT t.vs_oid as 'value set oid', LPAD(t.table_id, 4, '0') as bindingIdentifier, v.hl7_version as version, tv.table_value as value, tv.display_name as name, t.cs_oid as 'code system oid' , CONCAT('HL7',LPAD(t.table_id, 4, '0')) as 'code system', 'P' as 'usage' FROM hl7tablevalues as tv\n" + 
				"LEFT JOIN hl7tables as t\n" + 
				"ON tv.table_id = t.table_id\n" + 
				"AND tv.version_id = t.version_id\n" + 
				"LEFT JOIN hl7versions as v\n" + 
				"ON t.version_id = v.version_id\n" + 
				"WHERE t.table_id = :bindingIdentifier\n" + 
				"AND v.hl7_version = :version ";
		
		SqlParameterSource p = new MapSqlParameterSource().addValue("version", version).addValue("bindingIdentifier", bindingIdentifer);
		return this.namedParameterJdbcTemplate.query(query ,p, new CodeRowMapper());
	}


}
