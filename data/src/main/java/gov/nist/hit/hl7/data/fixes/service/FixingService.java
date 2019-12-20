package gov.nist.hit.hl7.data.fixes.service;

public interface FixingService {

    public void fixComponentDatatype(String version, String name, String newName, String oldDt, String newDT, boolean createNew);

}
