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
package gov.nist.hit.hl7.data.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import gov.nist.hit.hl7.data.enities.ValueSetRow;

/**
 * @author Abdelghani El Ouakili
 *
 */
public class ValueSetRowMapper implements RowMapper<ValueSetRow> {

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public ValueSetRow mapRow(ResultSet result, int arg1) throws SQLException {
		ValueSetRow ret= new ValueSetRow();
		ret.bindingIdentifier = result.getString("bindingIdentifier");
		ret.hl7TableType = result.getString("hl7TableType");
		ret.version = result.getString("version");
		ret.codeSystemOid = result.getString("code system oid");
		ret.valueSetOid = result.getString("value set oid");
		ret.codeSystem = result.getString("code system");
		ret.name= result.getString("description");

		return ret;
	}

}
