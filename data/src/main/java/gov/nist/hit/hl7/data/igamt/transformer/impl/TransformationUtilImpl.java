package gov.nist.hit.hl7.data.igamt.transformer.impl;

import gov.nist.hit.hl7.data.domain.StructureElement;
import gov.nist.hit.hl7.data.igamt.transformer.TransformationUtil;
import gov.nist.hit.hl7.igamt.common.base.domain.LengthType;
import gov.nist.hit.hl7.igamt.common.base.domain.SubStructElement;
import gov.nist.hit.hl7.igamt.common.base.domain.Usage;
import org.springframework.stereotype.Service;

@Service
public class TransformationUtilImpl implements TransformationUtil {

    private boolean hasLength(String length) {
        return length != null && !length.isEmpty() && !length.equals("-1");
    }

    @Override
    public Usage getUsage(String usage) {
        if (usage == null || usage.isEmpty()) {
            return Usage.O;
        } else if (usage.trim().toUpperCase().contains("(B")){
            return Usage.B;
        } else if (usage.trim().toUpperCase().contains("C(")){
            return Usage.C;
        } else {
            return Usage.fromString(usage);
        }
    }

    @Override
    public void setLength(SubStructElement ret, StructureElement f) {
        if (this.hasLength(f.getConfLength())) {
            ret.setLengthType(LengthType.ConfLength);
            ret.setMaxLength("NA");
            ret.setMinLength("NA");
            ret.setConfLength(f.getConfLength());
        } else if (this.hasLength(f.getMaxLength())) {
            ret.setLengthType(LengthType.Length);
            ret.setConfLength("NA");
            this.setMinMax(ret, f);
        } else {
            ret.setConfLength("NA");
            ret.setMinLength("NA");
            ret.setMaxLength("NA");
            ret.setLengthType(LengthType.UNSET);
        }
    }

    public void setMinMax(SubStructElement ret, StructureElement f) {
            ret.setMaxLength(f.getMaxLength());
        if (this.hasLength(f.getMinLength())) {
            ret.setMinLength(f.getMinLength());
        } else {
            ret.setMinLength("1");
        }
    }

}
