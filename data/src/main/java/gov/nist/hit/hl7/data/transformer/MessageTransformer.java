package gov.nist.hit.hl7.data.transformer;

import gov.nist.hit.hl7.data.builder.MessageBuilder;
import gov.nist.hit.hl7.data.builder.SegmentBuilder;
import gov.nist.hit.hl7.data.domain.MessageStructure;
import gov.nist.hit.hl7.data.repository.ConformanceProfileRepository;
import gov.nist.hit.hl7.data.repository.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageTransformer implements Transformer<MessageStructure> {

    @Autowired
    ConformanceProfileRepository repo;

    @Autowired
    MessageBuilder builder;

    @Override
    public void tansformAll() {
        this.repo.saveAll(this.builder.buildAll());
    }
    public void transformByStrcutAndVersion(String structId, String version) {
      this.repo.save(  this.builder.buildByIdentifierAndVersion(structId, version));
    }

}
