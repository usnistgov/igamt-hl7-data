package gov.nist.hit.hl7.data.igamt.transformer;

import gov.nist.hit.hl7.igamt.common.base.domain.SubStructElement;
import gov.nist.hit.hl7.igamt.common.base.domain.Usage;

public interface TransformationUtil {

// public String  getLength(String length);
 public Usage getUsage(String usage);
 public void setLength(SubStructElement ret, gov.nist.hit.hl7.data.domain.StructureElement f);
}
