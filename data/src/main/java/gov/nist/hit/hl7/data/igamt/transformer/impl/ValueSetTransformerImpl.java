package gov.nist.hit.hl7.data.igamt.transformer.impl;

import gov.nist.hit.hl7.data.IdService;
import gov.nist.hit.hl7.data.domain.ValueSet;
import gov.nist.hit.hl7.data.igamt.transformer.ValueSetTransformer;
import gov.nist.hit.hl7.data.repository.ValueSetRepository;
import gov.nist.hit.hl7.igamt.common.base.domain.DomainInfo;
import gov.nist.hit.hl7.igamt.common.base.domain.Scope;
import gov.nist.hit.hl7.igamt.common.base.domain.Status;
import gov.nist.hit.hl7.igamt.valueset.domain.Code;
import gov.nist.hit.hl7.igamt.valueset.domain.CodeUsage;
import gov.nist.hit.hl7.igamt.valueset.domain.Valueset;
import gov.nist.hit.hl7.igamt.valueset.domain.property.ContentDefinition;
import gov.nist.hit.hl7.igamt.valueset.domain.property.Extensibility;
import gov.nist.hit.hl7.igamt.valueset.domain.property.Stability;
import gov.nist.hit.hl7.igamt.valueset.repository.ValuesetRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ValueSetTransformerImpl  implements ValueSetTransformer {
    @Autowired
    ValuesetRepository igamtRepo;
    @Autowired
    ValueSetRepository dataRepo;
    @Autowired
    IdService idService;

    @Override
    public void transformAll() {

        igamtRepo.deleteAll();
        List<ValueSet> valueSetList = dataRepo.findAll();
        igamtRepo.saveAll(transformValueSet(valueSetList));
    }

    private List<Valueset> transformValueSet(List<ValueSet> valueSetList) {
        List<Valueset> ret = new ArrayList<>();
        for (ValueSet vs: valueSetList) {
            ret.add(convertToFinal(vs));
        }
        return ret;
    }

    private Valueset convertToFinal(ValueSet valueset) {
        Valueset ret= new Valueset();
        ret.setId("HL7-"+ valueset.getBindingIdentifier()+ "V" + valueset.getVersion().replaceAll("\\.","-" ));
        ret.setBindingIdentifier(valueset.getBindingIdentifier());
        ret.setName(valueset.getDescription());
        ret.setName(valueset.getName());
        Set<String> codeSystems = new HashSet<String>();
        codeSystems.add(valueset.getCodeSystem());
        ret.setCodeSystems(codeSystems);
        ret.setHl7Type(valueset.getHl7TableType());
        ret.setDescription(valueset.getDescription());
        ret.setOid(valueset.getOid());
        DomainInfo info = new DomainInfo();
        info.setScope(Scope.HL7STANDARD);
        info.setVersion(valueset.getVersion());
        ret.setDomainInfo(info);
        this.setDefaults(ret);
        Set<Code> codes = new HashSet<>();
        for (int i = 0; i < valueset.getChildren().size(); i++) {
            codes.add(convertCode(valueset.getChildren().get(i)));
        }
        ret.setCodes(codes);
        ret.setNumberOfCodes(codes.size());
        ret.setId(idService.buildId(valueset));

        return ret;
    }

    private Code convertCode(gov.nist.hit.hl7.data.domain.Code code) {
        Code ret = new Code();
        ret.setCodeSystem(code.getCodeSystem());
        ret.setId(new ObjectId().toString());
        ret.setCodeSystem(code.getCodeSystem());
        ret.setCodeSystemOid(code.getCodeSystemOid());
        ret.setValue(code.getValue());
        ret.setDescription(code.getName());
        if(code.getCodeUsage() == null ||  code.getCodeUsage().isEmpty() || code.getCodeUsage().equals("O")) {
            ret.setUsage(CodeUsage.P);
        } else {
           ret.setUsage(CodeUsage.valueOf(code.getCodeUsage()));
        }
        return ret;
    }

    private void setDefaults(Valueset elm) {
        elm.setContentDefinition(ContentDefinition.Undefined);
        elm.setExtensibility(Extensibility.Undefined);
        elm.setStability(Stability.Undefined);
        elm.setStatus(Status.PUBLISHED);
    }


}
