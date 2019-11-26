package gov.nist.hit.hl7.data.repository;

import gov.nist.hit.hl7.data.domain.Segment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("SegmentDataRepo")
public interface SegmentRepository extends MongoRepository<Segment, String> {
}
