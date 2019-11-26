package gov.nist.hit.hl7.data.service;

import gov.nist.hit.hl7.data.enities.ComponentRow;
import gov.nist.hit.hl7.data.enities.DatatypeRow;

import java.util.List;

public interface DatatypeMappingService {

    public  List<DatatypeRow> findAll();
    public  DatatypeRow findByNameAndVersion();
    public List<ComponentRow> findChildrenByNameAndVersion(String name, String version);
}
