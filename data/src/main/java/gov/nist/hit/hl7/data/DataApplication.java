package gov.nist.hit.hl7.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import gov.nist.hit.hl7.data.repository.SegmentRepository;
import gov.nist.hit.hl7.data.transformer.DatatypeTransformer;
import gov.nist.hit.hl7.data.transformer.MessageTransformer;
import gov.nist.hit.hl7.data.transformer.SegmentTransformer;
import gov.nist.hit.hl7.data.transformer.ValueSetTransformer;
import gov.nist.hit.hl7.igamt.conformanceprofile.domain.ConformanceProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import gov.nist.hit.hl7.data.domain.ValueSet;
import gov.nist.hit.hl7.data.enities.CodeRow;
import gov.nist.hit.hl7.data.enities.ValueSetRow;
import gov.nist.hit.hl7.data.repository.ValueSetRepository;
import gov.nist.hit.hl7.data.service.ValueSetMappingService;
import gov.nist.hit.hl7.igamt.valueset.service.ValuesetService;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;

import javax.sql.DataSource;


@SpringBootApplication
@ComponentScan({"gov.nist.hit.hl7.igamt.common", "gov.nist.hit.hl7.igamt.datatype", "gov.nist.hit.hl7.igamt.segment", "gov.nist.hit.hl7.igamt.valueset",
	"gov.nist.hit.hl7.igamt.conformanceprofile","gov.nist.hit.hl7.data"})
public class DataApplication {

	@PersistenceContext
	public EntityManager em;
	@Autowired
	ValueSetTransformer valueSetTransformer;

	@Autowired
	SegmentRepository repo;

	@Autowired
	SegmentTransformer segmentTransformer;

	@Autowired
	DatatypeTransformer datatypeTransformer;

	@Autowired
	MessageTransformer messageTransformer;


	public static void main(String[] args) {
		SpringApplication.run(DataApplication.class, args);
	}

	@PostConstruct
	public void testData() {

		messageTransformer.tansformAll();

	}


}
