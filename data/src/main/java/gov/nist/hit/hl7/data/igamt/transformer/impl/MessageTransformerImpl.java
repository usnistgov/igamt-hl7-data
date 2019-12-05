package gov.nist.hit.hl7.data.igamt.transformer.impl;

import gov.nist.hit.hl7.data.domain.*;
import gov.nist.hit.hl7.data.domain.RealKey;
import gov.nist.hit.hl7.data.igamt.transformer.MessageTransformer;
import gov.nist.hit.hl7.data.repository.ConformanceProfileRepository;
import gov.nist.hit.hl7.data.repository.SegmentRepository;
import gov.nist.hit.hl7.igamt.common.base.domain.*;
import gov.nist.hit.hl7.igamt.conformanceprofile.domain.SegmentRefOrGroup;
import gov.nist.hit.hl7.igamt.conformanceprofile.domain.event.Event;
import gov.nist.hit.hl7.igamt.conformanceprofile.repository.MessageStructureRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageTransformerImpl implements MessageTransformer {

    @Autowired
    MessageStructureRepository messageStructureRepository;

    @Autowired
    ConformanceProfileRepository dataRepo;

    @Autowired
    SegmentRepository segmentRepo;

    @Override
    public void transformAll() {

        messageStructureRepository.deleteAll();
        List<MessageStructure> structures = dataRepo.findAll();

        List<Segment> segments = segmentRepo.findAll();

        Map<RealKey, String> segmentMap = segments.stream().collect(Collectors.toMap(x -> new  RealKey(x.getVersion(), x.getName()), x -> x.getId()));
        List<gov.nist.hit.hl7.igamt.conformanceprofile.domain.MessageStructure> ret = buildMessageStructures(structures,segmentMap);
        messageStructureRepository.saveAll(ret);

    }

    private List<gov.nist.hit.hl7.igamt.conformanceprofile.domain.MessageStructure> buildMessageStructures(List<MessageStructure> structures, Map<RealKey, String> segmentMap) {
        List<gov.nist.hit.hl7.igamt.conformanceprofile.domain.MessageStructure> messageStructures = new ArrayList<>();
        for (MessageStructure msg: structures) {
            messageStructures.add(buildMessage(msg, segmentMap));
        }
        return messageStructures;
    }

    private gov.nist.hit.hl7.igamt.conformanceprofile.domain.MessageStructure buildMessage(MessageStructure msg, Map<RealKey, String> segmentMap) {
        gov.nist.hit.hl7.igamt.conformanceprofile.domain.MessageStructure messageStructure = new gov.nist.hit.hl7.igamt.conformanceprofile.domain.MessageStructure();
        complete(messageStructure, msg);
        for (MessageStructureElement element: msg.getChildren()) {
            messageStructure.addChild(convertSegmentOrGroup(element, msg.getVersion(), segmentMap));
        }

        return messageStructure;
    }

    private SegmentRefOrGroup convertSegmentOrGroup(MessageStructureElement element, String version,Map<RealKey, String> segmentMap) {
        if(element instanceof SegmentRef){
            gov.nist.hit.hl7.igamt.conformanceprofile.domain.SegmentRef child = new gov.nist.hit.hl7.igamt.conformanceprofile.domain.SegmentRef();
            RealKey key = new RealKey(version,((SegmentRef) element).getSegment());
            child.setType(Type.SEGMENTREF);
            completeChild(child, element);
            Ref ref = new Ref();
            ref.setId(segmentMap.get(key));
            child.setRef(ref);
            return child;
        }else if (element instanceof Group){
            gov.nist.hit.hl7.igamt.conformanceprofile.domain.Group child = new gov.nist.hit.hl7.igamt.conformanceprofile.domain.Group();
            completeChild(child, element);
            child.setType(Type.GROUP);
            child.setChildren(new HashSet<SegmentRefOrGroup>());
            for(MessageStructureElement sub: ((Group) element).getChildren()){
                child.getChildren().add(convertSegmentOrGroup(sub,version, segmentMap));
            }
            return child;
        }
        return null;
    }

    public void complete(gov.nist.hit.hl7.igamt.conformanceprofile.domain.MessageStructure elm, MessageStructure row) {
        elm.setId(row.getId());
        elm.setStructID(row.getStructID());
        elm.setType(elm.getType());
        elm.setName(row.getName());
        elm.setDescription(row.getDescription());
        elm.setType(Type.CONFORMANCEPROFILE);
        elm.setCreationDate(new Date());
        elm.setUpdateDate(new Date());
        DomainInfo info = new DomainInfo();
        info.setScope(Scope.HL7STANDARD);
        info.setVersion(row.getVersion());
        Set<String> versions = new HashSet<String>();
        versions.add(row.getVersion());
        info.setCompatibilityVersion(versions);
        elm.setDomainInfo(info);
        List<Event> events= new ArrayList<>();
        for( gov.nist.hit.hl7.data.domain.Event event : row.getEvents()){
            Event newEvent = new Event();
            newEvent.setName(event.getName());
            newEvent.setHl7Version(event.getHl7Version());
            newEvent.setDescription(event.getDescription());
            newEvent.setId(row.getId());
            newEvent.setParentStructId(row.getStructID());
            events.add(newEvent);
        }
        elm.setEvents(events);

    }

    public void completeChild( SegmentRefOrGroup ret , MessageStructureElement elm){
        ret.setId(new ObjectId().toString());
        ret.setMin(Integer.valueOf(elm.getMinCard()));
        ret.setMax(elm.getMaxCard());
        ret.setName(ret.getName());
        ret.setUsage(Usage.fromString(elm.getUsage()));
        ret.setPosition(elm.getPosition());
    }


}
