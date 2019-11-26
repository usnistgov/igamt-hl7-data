package gov.nist.hit.hl7.data.service;

import gov.nist.hit.hl7.data.domain.MessageStructure;
import gov.nist.hit.hl7.data.enities.*;

import java.util.List;

public interface MessageStructureMappingService {

    public List<MessageStructureRow> findAllStructures();
    public List<SegmentGroupRow> findChildrenByVersionAndStrcutureID(String version, String structID);
    public List<EventRow>  findEventByVersionAndStructure(String version, String structID);



}
