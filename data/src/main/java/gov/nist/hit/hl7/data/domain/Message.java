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

import java.util.List;

/**
 * @author Abdelghani El Ouakili
 *
 */
public class Message extends Resource{
	
	
	private String messageType;
	private String structID;
	private String event;
	
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getStructID() {
		return structID;
	}
	public void setStructID(String structID) {
		this.structID = structID;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public List<MessageStructureElement> getChildren() {
		return children;
	}
	public void setChildren(List<MessageStructureElement> children) {
		this.children = children;
	}
	private List<MessageStructureElement> children;

}
