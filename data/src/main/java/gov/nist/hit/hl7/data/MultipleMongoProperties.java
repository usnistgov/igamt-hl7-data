package gov.nist.hit.hl7.data;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mongodb")
public class MultipleMongoProperties {

	private MongoProperties igamt_hl7 = new MongoProperties();
    private MongoProperties data_hl7 = new MongoProperties();
    
    public MongoProperties getIgamt_hl7() {
		return igamt_hl7;
	}
	public void setIgamt_hl7(MongoProperties igamt_hl7) {
		this.igamt_hl7 = igamt_hl7;
	}
	public MongoProperties getData_hl7() {
		return data_hl7;
	}
	public void setData_hl7(MongoProperties data_hl7) {
		this.data_hl7 = data_hl7;
	}

}