package gov.nist.hit.hl7.data.igamt.transformer.adapters.impl;

import gov.nist.hit.hl7.data.fixes.service.FixingService;
import gov.nist.hit.hl7.data.igamt.transformer.adapters.FiveLevelAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class FiveLevelAdapterImpl implements FiveLevelAdapter {

    @Autowired
    FixingService fixingService;

    public void createNistDatatypes(){

        fixingService.fixComponentDatatype("2.3", "CQ", "CQ_NIST", "CE", "ST", true);

        fixingService.fixComponentDatatype("2.3", "CN", "CN_NIST", "HD", "IS", true);

        fixingService.fixComponentDatatype("2.3.1", "CQ", "CQ_NIST", "CE", "ST", true);

        fixingService.fixComponentDatatype("2.3.1", "CN", "CN_NIST", "HD", "IS", true);

        fixingService.fixComponentDatatype("2.4", "CQ", "CQ_NIST", "CE", "ST", true);

        fixingService.fixComponentDatatype("2.4", "DR", "DR_NIST", "TS", "ST", true);

        fixingService.fixComponentDatatype("2.5", "CQ", "CQ_NIST", "CE", "ST", true);

        fixingService.fixComponentDatatype("2.5", "DR", "DR_NIST", "TS", "DTM", true);

        fixingService.fixComponentDatatype("2.5.1", "CQ", "CQ_NIST", "CE", "ST", true);

        fixingService.fixComponentDatatype("2.5.1", "DR", "DR_NIST", "TS", "DTM", true);

        fixingService.fixComponentDatatype("2.6", "CQ", "CQ_NIST", "CWE", "ST", true);

    }


    public void fixFiveLevelDatatypes(){
        // TQ
        fixingService.fixComponentDatatype("2.3", "TQ", "TQ", "CQ", "CQ_NIST", false);
        fixingService.fixComponentDatatype("2.3.1", "TQ", "TQ", "CQ", "CQ_NIST", false);
        fixingService.fixComponentDatatype("2.4", "TQ", "TQ", "CQ", "CQ_NIST", false);
        fixingService.fixComponentDatatype("2.5", "TQ", "TQ", "CQ", "CQ_NIST", false);
        fixingService.fixComponentDatatype("2.5.1", "TQ", "TQ", "CQ", "CQ_NIST", false);
        fixingService.fixComponentDatatype("2.6", "TQ", "TQ", "CQ", "CQ_NIST", false);

        // NDL
        fixingService.fixComponentDatatype("2.3", "NDL", "NDL", "CN", "CN_NIST", false);
        fixingService.fixComponentDatatype("2.3.1", "NDL", "NDL", "CN", "CN_NIST", false);
        // PPN
        fixingService.fixComponentDatatype("2.4", "PPN", "PPN", "DR", "DR_NIST", false);
        fixingService.fixComponentDatatype("2.5", "PPN", "PPN", "DR", "DR_NIST", false);
        fixingService.fixComponentDatatype("2.5.1", "PPN", "PPN", "DR", "DR_NIST", false);


        //XAD
        fixingService.fixComponentDatatype("2.4", "XAD", "XAD", "DR", "DR_NIST", false);
        fixingService.fixComponentDatatype("2.5", "XAD", "XAD", "DR", "DR_NIST", false);
        fixingService.fixComponentDatatype("2.5.1", "XAD", "XAD", "DR", "DR_NIST", false);


        //XCN

        fixingService.fixComponentDatatype("2.4", "XCN", "XCN", "DR", "DR_NIST", false);
        fixingService.fixComponentDatatype("2.5", "XCN", "XCN", "DR", "DR_NIST", false);
        fixingService.fixComponentDatatype("2.5.1", "XCN", "XCN", "DR", "DR_NIST", false);

        //XPN

        fixingService.fixComponentDatatype("2.4", "XPN", "XPN", "DR", "DR_NIST", false);
        fixingService.fixComponentDatatype("2.5", "XPN", "XPN", "DR", "DR_NIST", false);
        fixingService.fixComponentDatatype("2.5.1", "XPN", "XPN", "DR", "DR_NIST", false);

    }
    }
