package gov.nist.hit.hl7.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

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
	private ValueSetRepository dataValueSetRepo;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	NamedParameterJdbcTemplate  namedParameterJdbcTemplate;
	
	@Autowired
	DataSource datasource;
	 
	@Autowired
	private ValuesetService igamtVsService;
	@Autowired
	ValueSetMappingService valueSetMapper;

	public static void main(String[] args) {
		SpringApplication.run(DataApplication.class, args);
	}

	@PostConstruct
	public void testData() {
		
		List<CodeRow> list  = valueSetMapper.findByCodesByBindingIdentifierAndVersion("0001","2.8.1");
		System.out.println(list);
//		this.dataValueSetRepo.save(new ValueSet());
//		System.out.println(this.igamtVsService.findAll().size());
		

	}


}
