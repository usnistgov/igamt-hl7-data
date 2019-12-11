package gov.nist.hit.hl7.data.builder;

import gov.nist.hit.hl7.data.domain.*;
import gov.nist.hit.hl7.data.enities.EventRow;
import gov.nist.hit.hl7.data.enities.MessageStructureRow;
import gov.nist.hit.hl7.data.enities.SegmentGroupRow;
import gov.nist.hit.hl7.data.service.MessageStructureMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageBuilder implements Builder<MessageStructure> {

    @Autowired
    MessageStructureMappingService mapper;

    @Override
    public MessageStructure buildByIdentifierAndVersion(String identifier, String version) {

       MessageStructure structure = new MessageStructure();
//        List<MessageStructureElement> children = this.buildChildren(this.mapper.findChildrenByVersionAndStrcutureID(version, identifier));
//        structure.setChildren(children);
//        structure.setEvents(this.buildEventsByVersionAndStructure(version, identifier));
//
        return structure;
    }

    @Override
    public List<MessageStructure> buildByVersion(String version) {
        return null;
    }

    @Override
    public List<MessageStructure> buildAll() {
        List<MessageStructure>  ret = new ArrayList<>();
        List<MessageStructureRow> rows = mapper.findAllStructures();
        for(MessageStructureRow row: rows) {
            ret.add(this.buildMessageFromRow(row));
        }
        return ret;
    }

    private MessageStructure buildMessageFromRow(MessageStructureRow row) {
        MessageStructure structure = new MessageStructure();
        structure.setDescription(row.description);
        structure.setMessageType(row.messageType);
        structure.setStructID(row.structID);
        structure.setVersion(row.hl7Version);
        List<SegmentGroupRow> rows = mapper.findChildrenByVersionAndStrcutureID(row.hl7Version, row.structID);
        structure.setChildren(buildChildren(rows));
        List<Event> events = this.buildEventsByVersionAndStructure(row.hl7Version, row.structID);
        structure.setEvents(events);
        return structure;
    }

    private List<MessageStructureElement> buildChildren(List<SegmentGroupRow> rows) {
        List<MessageStructureElement> ret = new ArrayList<>();
        int relativePosition= 0;
        for (int i = 0 ; i< rows.size(); i++) {
            if (rows.get(i).segmentName != null && !rows.get(i).segmentName.isEmpty()) {
                relativePosition++;
                SegmentRef ref =  buildSegmentRef(rows.get(i),  relativePosition);
                ret.add(ref);
            } else {
                if (rows.get(i).groupStartEnd.equals("START")) {
                    relativePosition++;
                    int index = addGroup(rows.get(i), rows, i, ret);
                    i = index;
                } else {
                    break;
                }
            }
        }
        return ret;
    }

    private int addGroup(SegmentGroupRow segmentGroupRow, List<SegmentGroupRow> rows, int start, List<MessageStructureElement> ret) {
        Group group = new Group();
        int endIndex = start;
        group.setMaxCard(segmentGroupRow.maxCard);
        group.setMinCard(segmentGroupRow.minCard);
        group.setUsage(segmentGroupRow.usage);
        group.setName(segmentGroupRow.groupName);
        group.setPosition(start);
        int relativePosition= 0;

        List<MessageStructureElement> children = new ArrayList<>();
        for ( int i = start+1; i < rows.size(); i++  ) {
            if(rows.get(i).segmentName !=null && !rows.get(i).segmentName.isEmpty()) {
                children.add(buildSegmentRef(rows.get(i), relativePosition));
            } else {
            if (rows.get(i).groupStartEnd.equals("START")) {
                i = addGroup(rows.get(i), rows, i, children);
            } else if(rows.get(i).groupStartEnd !=null &&  rows.get(i).groupStartEnd.equals("END") && segmentGroupRow.groupName.equals(rows.get(i).groupName) ){
                endIndex = i;
               break;
            }
         }
        }
        group.setChildren(children);
        ret.add(group);
        return endIndex;
    }

    private SegmentRef buildSegmentRef(SegmentGroupRow row, int position) {

        SegmentRef ref = new SegmentRef();
        ref.setName(row.segmentName);
        ref.setMinCard(row.minCard);
        ref.setMaxCard(row.maxCard);
        ref.setUsage(row.usage);
        ref.setSegment(row.segmentName);
        ref.setPosition(position);
        return ref;
    }

    private List<Event> buildEventsByVersionAndStructure(String version, String structID ) {
        List<EventRow> eventRows = mapper.findEventByVersionAndStructure(version, structID);
        List<Event> events = new ArrayList<>();
        if (structID.equals("ACK")) {
            Event ev = new Event();
            ev.setHl7Version(version);
            ev.setDescription("General Acknowledgment Message");
            ev.setName("Varies");
            events.add(ev);
            return events;
        }
        for (EventRow eventRow: eventRows) {
            Event ev = new Event();
            ev.setHl7Version(eventRow.hl7Version);
            ev.setDescription(eventRow.description);
            ev.setName(eventRow.name);
            events.add(ev);
        }
        return events;
    }
}
