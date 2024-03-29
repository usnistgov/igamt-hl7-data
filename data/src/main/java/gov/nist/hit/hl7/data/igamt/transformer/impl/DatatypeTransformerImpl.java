package gov.nist.hit.hl7.data.igamt.transformer.impl;

import gov.nist.hit.hl7.data.IdService;
import gov.nist.hit.hl7.data.domain.*;
import gov.nist.hit.hl7.data.domain.RealKey;
import gov.nist.hit.hl7.data.igamt.transformer.BindingTransformerService;
import gov.nist.hit.hl7.data.igamt.transformer.DatatypeTransformer;
import gov.nist.hit.hl7.data.igamt.transformer.TransformationUtil;
import gov.nist.hit.hl7.data.repository.DatatypeRepository;
import gov.nist.hit.hl7.data.repository.ValueSetRepository;
import gov.nist.hit.hl7.igamt.common.base.domain.*;
import gov.nist.hit.hl7.igamt.common.binding.domain.ResourceBinding;
import gov.nist.hit.hl7.igamt.common.binding.domain.StructureElementBinding;
import gov.nist.hit.hl7.igamt.datatype.domain.ComplexDatatype;
import gov.nist.hit.hl7.igamt.datatype.domain.DateTimeDatatype;
import gov.nist.hit.hl7.igamt.datatype.domain.PrimitiveDatatype;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class DatatypeTransformerImpl implements DatatypeTransformer {

    @Autowired
    DatatypeRepository dataRepo;
    @Autowired
    BindingTransformerService bindingService;
    @Autowired
    IdService idService;
    @Autowired
    gov.nist.hit.hl7.igamt.datatype.repository.DatatypeRepository igamtRepo;

    @Autowired
    ValueSetRepository vsRepo;
    @Autowired
    TransformationUtil util;

    @Override
    public void transformAll() {

        igamtRepo.deleteAll();
        Map<RealKey, BindingWrapper> vsMap = new HashMap<>();
        List<ValueSet>  valueSetList = this.vsRepo.findAll();
        for(ValueSet  vs: valueSetList){
            RealKey temKey=  new RealKey(vs.getVersion(), vs.getBindingIdentifier());
            if(!vsMap.containsKey(temKey)){
                vsMap.put(temKey,new BindingWrapper(vs.getId(), vs.getHl7TableType()));
            }
        }
        List<Datatype> dts = this.dataRepo.findAll();
        Map<RealKey, Datatype> datatypesMap = new HashMap<>();
        for(Datatype  dt: dts){
            RealKey temKey=  new RealKey(dt.getVersion(), dt.getName());
            if(!datatypesMap.containsKey(temKey)){
                datatypesMap.put(temKey,dt);
            }
        }
        List<gov.nist.hit.hl7.igamt.datatype.domain.Datatype> datatypes = transformAll(dts, datatypesMap, vsMap);
        igamtRepo.saveAll(datatypes);
    }

    private List<gov.nist.hit.hl7.igamt.datatype.domain.Datatype> transformAll(List<Datatype> dts, Map<RealKey, Datatype> datatypesMap, Map<RealKey, BindingWrapper> vsMap) {
        List<gov.nist.hit.hl7.igamt.datatype.domain.Datatype> ret = new ArrayList<>();
        for (Datatype dt : dts) {
            if (dt.getType().equals("complex")) {
                ret.add(transformComplexDatatype(dt, dts, datatypesMap, vsMap));
            } else {
                ret.add(transformSimpleDatatype(dt, dts, datatypesMap, vsMap));
            }
        }
        return ret;
    }

    private gov.nist.hit.hl7.igamt.datatype.domain.Datatype transformSimpleDatatype(Datatype dt, List<Datatype> dts, Map<RealKey, Datatype> datatypesMap, Map<RealKey, BindingWrapper> vsMap) {
        if (dt.getName().equals("DTM") || dt.getName().equals("DT") || dt.getName().equals("TM")) {
            DateTimeDatatype ret = new DateTimeDatatype();
            complete(ret, dt);
            return ret;
        } else {
            PrimitiveDatatype ret = new PrimitiveDatatype();
            complete(ret, dt);
            return ret;
        }
    }

    private gov.nist.hit.hl7.igamt.datatype.domain.ComplexDatatype transformComplexDatatype(Datatype dt, List<Datatype> dts, Map<RealKey, Datatype> datatypesMap, Map<RealKey, BindingWrapper> vsMap) {
        ComplexDatatype ret = new ComplexDatatype();
        complete(ret, dt);
        Set<gov.nist.hit.hl7.igamt.datatype.domain.Component> components = new HashSet<gov.nist.hit.hl7.igamt.datatype.domain.Component>();
        ResourceBinding binding = new ResourceBinding();
        binding.setChildren(new HashSet<StructureElementBinding>());
        ret.setBinding(binding);
        for (Component c : dt.getChildren()) {
            components.add(transformComponent(dt.getName(), c, dt.getVersion(), datatypesMap, vsMap, ret.getBinding()));
        }
        ret.setComponents(components);

        return ret;
    }


    public void complete(gov.nist.hit.hl7.igamt.datatype.domain.Datatype elm, Datatype row) {

        elm.setId("HL7-"+ row.getName()+ "V" + row.getVersion().replaceAll("\\.","-" ));
        elm.setName(row.getName());
        elm.setDescription(row.getDescription());
        elm.setType(Type.DATATYPE);
        elm.setCreationDate(new Date());
        elm.setUpdateDate(new Date());
        DomainInfo info = new DomainInfo();
        info.setScope(Scope.HL7STANDARD);
        info.setVersion(row.getVersion());
        List<String> versions = new ArrayList<String>();
        versions.add(row.getVersion());
        info.setCompatibilityVersion(versions);
        elm.setDomainInfo(info);
        elm.setId(idService.buildId(row));

    }

    public gov.nist.hit.hl7.igamt.datatype.domain.Component transformComponent(String dtName, Component c, String version, Map<RealKey, Datatype> datatypesMap, Map<RealKey, BindingWrapper> vsMap, ResourceBinding binding) {
        gov.nist.hit.hl7.igamt.datatype.domain.Component ret = new gov.nist.hit.hl7.igamt.datatype.domain.Component();
        ret.setName(c.getName());
        ret.setId(String.valueOf(c.getPosition()));
        ret.setPosition(c.getPosition());
        ret.setType(Type.COMPONENT);
        ret.setUsage(util.getUsage(c.getUsage()));
        ret.setOldUsage(util.getUsage(c.getUsage()));
        Ref ref = new Ref();
        RealKey referenceKey = new RealKey(version, c.getDatatype());

        Datatype dtRef = datatypesMap.get(referenceKey);
        if (!dtRef.getType().equals("complex")) {
            util.setLength(ret, c);
        } else {
            ret.setMaxLength("NA");
            ret.setMinLength("NA");
            ret.setConfLength("NA");
            ret.setLengthType(LengthType.UNSET);
        }
        ref.setId(dtRef.getId());
        ret.setRef(ref);
        if (c.getTable() != null && !c.getTable().isEmpty() && !c.getTable().equals("0000")) {
            RealKey tableKey = new RealKey(version, c.getTable());
            ret.setConceptDomain(new StandardKey());
            if (vsMap.containsKey(tableKey)) {
                StandardKey conceptDomain= new StandardKey();
                conceptDomain.setName(tableKey.getName());
                conceptDomain.setVersion(tableKey.getVersion());
                ret.setConceptDomain(conceptDomain);
                bindingService.addBinding(binding, ret, referenceKey, tableKey, vsMap.get(tableKey));
            }
        }
        return ret;
    }
}
