package gov.nist.hit.hl7.data.repository;

import gov.nist.hit.hl7.data.domain.MessageStructure;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;


@Repository("ConformanceProfileDataRepo")
public interface ConformanceProfileRepository extends MongoRepository<MessageStructure, String>{

}


