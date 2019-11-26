package gov.nist.hit.hl7.data.builder;

import gov.nist.hit.hl7.data.domain.Field;
import gov.nist.hit.hl7.data.domain.Segment;
import gov.nist.hit.hl7.data.domain.Field;
import gov.nist.hit.hl7.data.domain.Segment;
import gov.nist.hit.hl7.data.enities.FieldRow;
import gov.nist.hit.hl7.data.enities.SegmentRow;
import gov.nist.hit.hl7.data.enities.FieldRow;
import gov.nist.hit.hl7.data.service.SegmentMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class SegmentBuilder implements Builder<Segment> {


    @Autowired
    SegmentMappingService segmentMappingService;

    @Override
    public Segment buildByIdentifierAndVersion(String identifier, String version) {
        return null;
    }

    @Override
    public List<Segment> buildByVersion(String version) {
        return null;
    }

    @Override
    public List<Segment> buildAll() {
        List<SegmentRow> rows = segmentMappingService.findAll();
        return convertList(rows);
    }

    private List<Segment> convertList(List<SegmentRow> rows) {
        List<Segment> dts = new ArrayList<Segment>();
        for(SegmentRow row : rows)  {
            dts.add(convertSegment(row));
        }
        return dts;
    }

    private Segment convertSegment(SegmentRow row) {

        Segment ret = new Segment();
        ret.setName(row.seg_code);
        ret.setVersion(row.hl7_version);
        ret.setDescription(row.description);
        ret.setChildren(getFields(row));

        return ret;
    }
    private List<Field> getFields(SegmentRow row) {
        List<Field> Fields = new ArrayList<>();
        List<FieldRow> FieldRows = segmentMappingService.findChildrenByNameAndVersion(row.seg_code, row.hl7_version);
        if(FieldRows != null ){
            for(FieldRow FieldRow: FieldRows){
                Fields.add(convertField(FieldRow));
            }
        }
        return Fields;
    }

    private Field convertField(FieldRow row) {
        Field f = new Field();
        f.setPosition(row.position);
        f.setName(row.description);
        f.setUsage(row.usage);
        f.setMinLength(row.minLength);
        f.setMaxLength(row.maxLength);
        f.setConfLength(row.confLength);
        f.setVersion(row.version);
        f.setTable(row.table);
        f.setDatatype(row.datatype);
        f.setMaxCard(row.minCard);
        f.setMinCard(row.minCard);

        return f;
    }

}
