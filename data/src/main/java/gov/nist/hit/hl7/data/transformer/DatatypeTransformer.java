package gov.nist.hit.hl7.data.transformer;

import gov.nist.hit.hl7.data.builder.DatatypeBuilder;
import gov.nist.hit.hl7.data.domain.Datatype;
import gov.nist.hit.hl7.data.repository.DatatypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatatypeTransformer implements Transformer<Datatype> {
    @Autowired
    DatatypeRepository repo;
    @Autowired
    DatatypeBuilder builder;

    @Override
    public void tansformAll() {
        this.repo.deleteAll();
        this.repo.saveAll(this.builder.buildAll());
    }
}
