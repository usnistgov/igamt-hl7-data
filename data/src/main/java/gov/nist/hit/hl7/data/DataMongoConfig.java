package gov.nist.hit.hl7.data;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "gov.nist.hit.hl7.data.repository",
        mongoTemplateRef = "dataMongoTemplate")
public class DataMongoConfig {

}