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
package gov.nist.hit.hl7.data.domain;

/**
 * @author Abdelghani El Ouakili
 *
 */
public class Field extends StructureElement {

	private String table;
	private String datatype;
	private String minCard;
	private String maxCard;

	public String getMinCard() {
		return minCard;
	}

	public void setMinCard(String minCard) {
		this.minCard = minCard;
	}

	public String getMaxCard() {
		return maxCard;
	}

	public void setMaxCard(String maxCard) {
		this.maxCard = maxCard;
	}

	public Field() {
        super();
    }

    public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	
}
