package gov.nist.hit.hl7.data.repository;

import gov.nist.hit.hl7.data.domain.Datatype;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("datatypeDataRepo")
public interface DatatypeRepository extends MongoRepository<Datatype, String> {
}
