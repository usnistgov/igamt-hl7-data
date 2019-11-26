package gov.nist.hit.hl7.data.builder;

import gov.nist.hit.hl7.data.domain.Group;
import gov.nist.hit.hl7.data.domain.MessageStructure;
import gov.nist.hit.hl7.data.domain.MessageStructureElement;
import gov.nist.hit.hl7.data.domain.SegmentRef;
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
        List<MessageStructureElement>  children= this.buildChildren(this.mapper.findChildrenByVersionAndStrcutureID("2.4", "ORU_R01"));
        structure.setChildren(children);
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
        for(MessageStructureRow row: rows){
            ret.add(buildMessageFromRow(row));
        }
        return ret;
    }

    private MessageStructure buildMessageFromRow(MessageStructureRow row) {
        MessageStructure structure = new MessageStructure();
        structure.setDescription(row.description);
        structure.setMessageType(row.messageType);
        structure.setStructID(row.structID);
        List<SegmentGroupRow> rows = mapper.findChildrenByVersionAndStrcutureID(row.hl7Version, row.structID);
        structure.setChildren(buildChildren(rows));
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
                    int index = adddGroup(rows.get(i), rows, i, ret);
                    i = index;
                } else {
                    break;
                }
            }
        }
        return ret;
    }

    private int adddGroup(SegmentGroupRow segmentGroupRow, List<SegmentGroupRow> rows, int start, List<MessageStructureElement> ret) {
        Group group = new Group();
        int endIndex = start;
        group.setMaxCard(segmentGroupRow.maxCard);
        group.setMinCard(segmentGroupRow.minCard);
        group.setUsage(segmentGroupRow.usage);
        group.setName(segmentGroupRow.groupName);
        int relativePosition= 0;

        List<MessageStructureElement> children = new ArrayList<>();
        for ( int i = start+1; i < rows.size(); i++  ) {
            if(rows.get(i).segmentName !=null && !rows.get(i).segmentName.isEmpty()) {
                children.add(buildSegmentRef(rows.get(i), relativePosition));
            } else {
            if (rows.get(i).groupStartEnd.equals("START")) {
                i = adddGroup(rows.get(i), rows, i, children);
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


}
