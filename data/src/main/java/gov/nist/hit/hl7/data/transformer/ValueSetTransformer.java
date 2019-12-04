package gov.nist.hit.hl7.data.transformer;

import gov.nist.hit.hl7.data.builder.ValueSetBuilder;
import gov.nist.hit.hl7.data.domain.ValueSet;
import gov.nist.hit.hl7.data.repository.ValueSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValueSetTransformer implements Transformer<ValueSet> {

    @Autowired
    ValueSetBuilder valueSetBuilder;

    @Autowired
    ValueSetRepository valueSetRepository;

    @Override
    public void transformAll() {
        valueSetRepository.deleteAll();
        valueSetRepository.saveAll(valueSetBuilder.buildAll());
    }
}
