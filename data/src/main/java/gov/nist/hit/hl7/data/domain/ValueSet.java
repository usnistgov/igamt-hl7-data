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

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Abdelghani El Ouakili
 *
 */

@Document
public class ValueSet extends Resource {



	@Id
	private String id; 
	private String bindingIdentifier; 
	private String hl7TableType;
	private String oid;
	private String codeSystem;

	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}
	public String getCodeSystem() {
		return codeSystem;
	}
	private List<Code> children = new ArrayList<Code>();
	
	public List<Code> getChildren() {
		return children;
	}
	public void setChildren(List<Code> children) {
		this.children = children;
	}
	public String getBindingIdentifier() {
		return bindingIdentifier;
	}
	public void setBindingIdentifier(String bindingIdentifier) {
		this.bindingIdentifier = bindingIdentifier;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getHl7TableType() {
		return hl7TableType;
	}
	public void setHl7TableType(String hl7TableType) {
		this.hl7TableType = hl7TableType;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
}
