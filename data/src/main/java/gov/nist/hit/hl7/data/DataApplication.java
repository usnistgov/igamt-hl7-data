package gov.nist.hit.hl7.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import gov.nist.hit.hl7.data.igamt.transformer.adapters.FiveLevelAdapter;
import gov.nist.hit.hl7.data.repository.SegmentRepository;
import gov.nist.hit.hl7.data.transformer.DatatypeTransformer;
import gov.nist.hit.hl7.data.transformer.MessageTransformer;
import gov.nist.hit.hl7.data.transformer.SegmentTransformer;
import gov.nist.hit.hl7.data.transformer.ValueSetTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"gov.nist.hit.hl7.igamt.common", "gov.nist.hit.hl7.igamt.datatype", "gov.nist.hit.hl7.igamt.segment", "gov.nist.hit.hl7.igamt.valueset",
	"gov.nist.hit.hl7.igamt.conformanceprofile","gov.nist.hit.hl7.igamt.common.config","gov.nist.hit.hl7.data"})
public class DataApplication {

	@PersistenceContext
	public EntityManager em;
	@Autowired
	ValueSetTransformer valueSetTransformerF1;

	@Autowired
	FiveLevelAdapter fiveLevelAdapter;

	@Autowired
	gov.nist.hit.hl7.data.igamt.transformer.ValueSetTransformer  valueSetTransformerF2;


	@Autowired
	gov.nist.hit.hl7.data.igamt.transformer.DatatypeTransformer  datatypeTransformerF2;

	@Autowired
	gov.nist.hit.hl7.data.igamt.transformer.SegmentTransformer  segmentTransformerF2;
	@Autowired
	gov.nist.hit.hl7.data.igamt.transformer.MessageTransformer  messageTransformerF2;

	@Autowired
	SegmentRepository repo;

	@Autowired
	SegmentTransformer segmentTransformerF1;

	@Autowired
	DatatypeTransformer datatypeTransformerF1;

	@Autowired
	MessageTransformer messageTransformerF1;

	@Autowired



	public static void main(String[] args) {
		SpringApplication.run(DataApplication.class, args);
	}

	@PostConstruct
	public void transform() {

		// First transformation
		valueSetTransformerF1.transformAll();
		datatypeTransformerF1.transformAll();
		segmentTransformerF1.transformAll();
		messageTransformerF1.transformAll();
		// add Data to IGAMT Requirement


		fiveLevelAdapter.createNistDatatypes();
		fiveLevelAdapter.fixFiveLevelDatatypes();


		// final transformation
		valueSetTransformerF2.transformAll();
		datatypeTransformerF2.transformAll();
		segmentTransformerF2.transformAll();
		messageTransformerF2.transformAll();

	}


}
