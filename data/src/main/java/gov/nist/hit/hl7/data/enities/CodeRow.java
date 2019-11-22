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

/**
 * @author Abdelghani El Ouakili
 *
 */
public class CodeRow {
	
	public String value;
	public String name;
	public String valueSetOid;
	public String codeSystemOid;
	public String codeSystem;
	public String usage;
	@Override
	public String toString() {
		return "CodeRow [value=" + value + ", name=" + name + ", valueSetOid=" + valueSetOid + ", codeSystemOid="
				+ codeSystemOid + ", codeSystem=" + codeSystem + ", usage=" + usage + "]";
	}

}
