package gov.nist.hit.hl7.data.igamt.transformer.impl;

import gov.nist.hit.hl7.data.domain.*;
import gov.nist.hit.hl7.data.domain.RealKey;
import gov.nist.hit.hl7.data.igamt.transformer.BindingTransformerService;
import gov.nist.hit.hl7.data.igamt.transformer.SegmentTransformer;
import gov.nist.hit.hl7.data.repository.DatatypeRepository;
import gov.nist.hit.hl7.data.repository.SegmentRepository;
import gov.nist.hit.hl7.data.repository.ValueSetRepository;
import gov.nist.hit.hl7.igamt.common.base.domain.*;
import gov.nist.hit.hl7.igamt.common.binding.domain.ResourceBinding;
import gov.nist.hit.hl7.igamt.common.binding.domain.StructureElementBinding;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SegmentTransformerImpl implements SegmentTransformer {
    @Autowired
    SegmentRepository dataSegmentRepo;
    @Autowired
    BindingTransformerService bindingService;
    @Autowired
    DatatypeRepository datatypeRepo;

    @Autowired
    gov.nist.hit.hl7.igamt.segment.repository.SegmentRepository igamtRepo;

    @Autowired
    ValueSetRepository vsRepo;

    @Override
    public void transformAll() {
        List<Segment> minifiedSegments = this.dataSegmentRepo.findAll();
        igamtRepo.deleteAll();
        Map<RealKey, BindingWrapper> vsMap = new HashMap<>();
        List<ValueSet>  valueSetList = this.vsRepo.findAll();
        for(ValueSet  vs: valueSetList){
            RealKey temKey=  new RealKey(vs.getVersion(), vs.getBindingIdentifier());
            if(!vsMap.containsKey(temKey)){
                vsMap.put(temKey,new BindingWrapper(vs.getId(), vs.getHl7TableType()));
            }else {
                System.out.println(temKey);
            }
        }
        List<Datatype> dts = this.datatypeRepo.findAll();
        Map<RealKey, String> datatypesMap = new HashMap<>();
        for(Datatype  dt: dts){
            RealKey temKey=  new RealKey(dt.getVersion(), dt.getName());
            if(!datatypesMap.containsKey(temKey)){
                datatypesMap.put(temKey,dt.getId());
            }else {
                System.out.println(temKey);
            }
        }
        List<gov.nist.hit.hl7.igamt.segment.domain.Segment> segments = transformAll(minifiedSegments, datatypesMap, vsMap);
        igamtRepo.saveAll(segments);
    }

    private List<gov.nist.hit.hl7.igamt.segment.domain.Segment> transformAll(List<Segment> segments, Map<RealKey, String> segmentsMap, Map<RealKey, BindingWrapper> vsMap) {
        List<gov.nist.hit.hl7.igamt.segment.domain.Segment> ret = new ArrayList<>();
        for (Segment seg : segments) {
           ret.add(transformSegment(seg, segments, segmentsMap, vsMap));
        }
        return ret;
    }


    private gov.nist.hit.hl7.igamt.segment.domain.Segment transformSegment(Segment seg, List<Segment> dts, Map<RealKey, String> datatypesMap, Map<RealKey, BindingWrapper> vsMap) {
        gov.nist.hit.hl7.igamt.segment.domain.Segment ret = new gov.nist.hit.hl7.igamt.segment.domain.Segment();
        complete(ret, seg);
        Set<gov.nist.hit.hl7.igamt.segment.domain.Field> fields = new HashSet<gov.nist.hit.hl7.igamt.segment.domain.Field>();
        ResourceBinding binding = new ResourceBinding();
        binding.setChildren(new HashSet<StructureElementBinding>());
        ret.setBinding(binding);
        for (gov.nist.hit.hl7.data.domain.Field f : seg.getChildren()) {
            fields.add(transformField(f, seg.getVersion(), datatypesMap, vsMap, ret.getBinding()));
        }
        ret.setChildren(fields);

        return ret;
    }


    public void complete(gov.nist.hit.hl7.igamt.segment.domain.Segment elm, Segment row) {
        elm.setId(row.getId());
        elm.setName(row.getName());
        elm.setDescription(row.getDescription());
        elm.setType(Type.SEGMENT);
        elm.setCreationDate(new Date());
        elm.setUpdateDate(new Date());
        DomainInfo info = new DomainInfo();
        info.setScope(Scope.HL7STANDARD);
        info.setVersion(row.getVersion());
        Set<String> versions = new HashSet<String>();
        versions.add(row.getVersion());
        info.setCompatibilityVersion(versions);
        elm.setDomainInfo(info);
    }

    public gov.nist.hit.hl7.igamt.segment.domain.Field transformField(gov.nist.hit.hl7.data.domain.Field f, String version, Map<RealKey, String> segmentsMap, Map<RealKey, BindingWrapper> vsMap, ResourceBinding binding) {
        gov.nist.hit.hl7.igamt.segment.domain.Field ret = new gov.nist.hit.hl7.igamt.segment.domain.Field();
        ret.setName(f.getName());
        ret.setConfLength(f.getConfLength());
        ret.setMinLength(f.getMinLength());
        ret.setMaxLength(f.getMaxLength());
        ret.setId(new ObjectId().toString());
        ret.setPosition(f.getPosition());
        ret.setType(Type.FIELD);
        ret.setMin(Integer.parseInt(f.getMinCard()));
        ret.setMax(f.getMaxCard());
        ret.setUsage(Usage.fromString(f.getUsage()));
        Ref ref = new Ref();
        RealKey referenceKey = new RealKey(version, f.getDatatype());
        ref.setId(segmentsMap.get(referenceKey));
        if (f.getTable() != null && !f.getTable().isEmpty()) {
            RealKey tableKey = new RealKey(version, f.getTable());
            if (vsMap.containsKey(tableKey)) {
                bindingService.addBinding(binding, ret, referenceKey, tableKey, vsMap.get(tableKey));
            }
        }
        return ret;
    }

}
