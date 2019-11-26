package gov.nist.hit.hl7.data.service;


import gov.nist.hit.hl7.data.enities.FieldRow;
import gov.nist.hit.hl7.data.enities.SegmentRow;

import java.util.List;

public interface SegmentMappingService {

    public List<SegmentRow> findAll();
    public  SegmentRow findByNameAndVersion();
    public List<FieldRow> findChildrenByNameAndVersion(String name, String version);
}
