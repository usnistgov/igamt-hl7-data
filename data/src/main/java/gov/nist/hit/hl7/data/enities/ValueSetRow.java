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
package gov.nist.hit.hl7.data.enities;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

import gov.nist.hit.hl7.data.domain.ValueSet;

/**
 * @author Abdelghani El Ouakili
 *
 */

public class ValueSetRow {
	public String bindingIdentifier;
	public String hl7TableType;
	public String oid;
	public String valueSetOid;
	public String codeSystemOid;
	public String CodeSystem;
	public String version;

	@Override
	public String toString() {
		return "ValueSetRow [bindingIdentifier=" + bindingIdentifier + ", hl7TableType=" + hl7TableType + ", oid=" + oid
				+ ", version=" + version + "]";
	}
}