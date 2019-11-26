/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.hl7.data.builder;

import java.util.ArrayList;
import java.util.List;

import gov.nist.hit.hl7.data.domain.Code;
import gov.nist.hit.hl7.data.enities.CodeRow;
import gov.nist.hit.hl7.data.enities.ValueSetRow;
import gov.nist.hit.hl7.data.service.ValueSetMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.hit.hl7.data.domain.ValueSet;

/**
 * @author Abdelghani El Ouakili
 *
 */
@Service
public class ValueSetBuilder implements Builder<ValueSet> {


	@Autowired
	ValueSetMappingService valueSetMappingService;

	@Override
	public ValueSet buildByIdentifierAndVersion(String identifier, String version) {
		return null;
	}


	@Override
	public List<ValueSet> buildByVersion(String version) {
		List<ValueSetRow>  valueSetRows = valueSetMappingService.findValueSetByVersion(version);
		return  convertToValueSetList(valueSetRows);
	}


	@Override
	public List<ValueSet> buildAll() {
		List<ValueSetRow>  valueSetRows = valueSetMappingService.findAllValueSets();
		return  convertToValueSetList(valueSetRows);
	}

	private List<ValueSet> convertToValueSetList(List<ValueSetRow> rows){
		List<ValueSet> ret = new ArrayList<ValueSet>();
		for (ValueSetRow row: rows) {
			ret.add(convertToValueSet(row));
		}
		return ret;
	}

	private ValueSet convertToValueSet(ValueSetRow row) {
		ValueSet vs  = new ValueSet();
		vs.setBindingIdentifier(row.bindingIdentifier);
		vs.setOid(row.valueSetOid);
		vs.setCodeSystem(row.codeSystem);
		vs.setOid(row.oid);
		vs.setVersion(row.version);
		vs.setHl7TableType(row.hl7TableType);
		vs.setChildren(buildCodes(row));
		return vs;
	}

	private List<Code> buildCodes(ValueSetRow row) {
		List<Code> ret = new ArrayList<>();
		List<CodeRow> codeRows = valueSetMappingService.findByCodesByBindingIdentifierAndVersion(row.bindingIdentifier, row.version);
		for(CodeRow codeRow: codeRows) {
			ret.add(convertToCode(codeRow));
		}
		return ret;
	}

	private Code convertToCode(CodeRow codeRow) {
		Code code = new Code();
		code.setName(codeRow.name);
		code.setCodeUsage(codeRow.usage);
		code.setValue(codeRow.value);
		code.setCodeSystem(codeRow.codeSystem);
		code.setCodeSystemOid(codeRow.codeSystemOid);

		return code;
	}
}
