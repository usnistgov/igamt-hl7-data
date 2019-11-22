package gov.nist.hit.hl7.data;
import com.mongodb.MongoClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;


@Configuration
@EnableConfigurationProperties(MultipleMongoProperties.class)
public class MultipleMongoConfig {
	
	  int port = 27017;
	  String host = "localhost";

	  @Autowired
    private  MultipleMongoProperties mongoProperties;

    @Primary
    @Bean(name = "igamtMongoTemplate")
    public MongoTemplate igamtMongoTemplate() throws Exception {
        return new MongoTemplate(igamtFactory(this.mongoProperties.getIgamt_hl7()));
    }

    @Bean(name = "dataMongoTemplate")
    public MongoTemplate dataMongoTemplate() throws Exception {
        return new MongoTemplate(dataFactory(this.mongoProperties.getData_hl7()));
    }

    @Bean
    @Primary
    public MongoDbFactory igamtFactory(MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), port),
                "igamt-hl7");
    }

    @Bean
    public MongoDbFactory dataFactory(MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), port),
                "data");
    }

}