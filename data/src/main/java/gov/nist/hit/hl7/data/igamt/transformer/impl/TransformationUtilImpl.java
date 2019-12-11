package gov.nist.hit.hl7.data.igamt.transformer.impl;

import gov.nist.hit.hl7.data.igamt.transformer.TransformationUtil;
import gov.nist.hit.hl7.igamt.common.base.domain.Usage;
import org.springframework.stereotype.Service;

@Service
public class TransformationUtilImpl implements TransformationUtil {


    @Override
    public String getLength(String length) {
        if(length ==null || length.isEmpty() || length.equals("-1")){
            return "NA";
        }else return length;
    }

    @Override
    public Usage getUsage(String usage) {

        if(usage ==null ||usage.equals("B") || usage.equals("W") || usage.isEmpty()){
            return Usage.X;
        }else {
            return Usage.fromString(usage);
        }
    }
}
