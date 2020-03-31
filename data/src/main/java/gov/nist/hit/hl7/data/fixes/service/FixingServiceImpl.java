package gov.nist.hit.hl7.data.fixes.service;

import gov.nist.hit.hl7.data.IdService;
import gov.nist.hit.hl7.data.domain.Component;
import gov.nist.hit.hl7.data.domain.Datatype;
import gov.nist.hit.hl7.data.repository.DatatypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FixingServiceImpl implements FixingService{
    @Autowired
    DatatypeRepository datatypeRepository;
    @Autowired
    IdService idService;

    public void fixComponentDatatype(String version, String name, String newName, String oldDt, String newDT, boolean createNew){
        Datatype original = this.datatypeRepository.findByNameAndVersion(name, version);
        if(original !=null){
            Datatype newDt = original;
            if(createNew) {
                original.setId(null);
                original.setName(newName);
            }
            if(original.getChildren() !=null && !original.getChildren().isEmpty()){
                for( Component c : original.getChildren()){
                    if(c.getDatatype().equals(oldDt)){
                        c.setDatatype(newDT);
                    }
                }
            }
            original.setId(idService.buildId(original));
            datatypeRepository.save(original);
        }
    }

}
