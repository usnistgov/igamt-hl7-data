package gov.nist.hit.hl7.data.transformer;

import gov.nist.hit.hl7.data.builder.SegmentBuilder;
import gov.nist.hit.hl7.data.domain.Segment;
import gov.nist.hit.hl7.data.repository.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SegmentTransformer implements Transformer<Segment> {
    @Autowired
    SegmentRepository repo;

    @Autowired
    SegmentBuilder builder;

    @Override
    public void tansformAll() {
        this.repo.deleteAll();
        this.repo.saveAll(builder.buildAll());
    }
}
