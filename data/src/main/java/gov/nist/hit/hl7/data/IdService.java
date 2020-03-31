package gov.nist.hit.hl7.data;

import gov.nist.hit.hl7.data.domain.MessageStructure;
import gov.nist.hit.hl7.data.domain.Resource;
import gov.nist.hit.hl7.data.domain.ValueSet;
import org.springframework.stereotype.Service;

@Service
public class IdService {

    public String buildId(Resource r) {
        String version = "V" + r.getVersion().replaceAll("\\.","-" );
        if (r instanceof ValueSet ) {
           return  ((ValueSet) r).getBindingIdentifier() + version;
        } else {
            return "HL7" + r.getName() +"-" +version;
        }
    }
}
