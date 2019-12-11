package gov.nist.hit.hl7.data.builder;

import gov.nist.hit.hl7.data.domain.Component;
import gov.nist.hit.hl7.data.domain.Datatype;
import gov.nist.hit.hl7.data.enities.ComponentRow;
import gov.nist.hit.hl7.data.enities.DatatypeRow;
import gov.nist.hit.hl7.data.service.DatatypeMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatatypeBuilder implements Builder<Datatype> {

    @Autowired
    DatatypeMappingService datatypeMappingService;

    @Override
    public Datatype buildByIdentifierAndVersion(String identifier, String version) {
        return null;
    }

    @Override
    public List<Datatype> buildByVersion(String version) {
        return null;
    }

    @Override
    public List<Datatype> buildAll() {
        List<DatatypeRow> rows = datatypeMappingService.findAll();
        return convertList(rows);
    }

    private List<Datatype> convertList(List<DatatypeRow> rows) {
        List<Datatype> dts = new ArrayList<Datatype>();
        for(DatatypeRow row : rows)  {
            dts.add(convertDatatype(row));
        }
        return dts;
    }

    private Datatype convertDatatype(DatatypeRow row) {
        Datatype ret = new Datatype();
        ret.setName(row.data_type_code);
        ret.setVersion(row.hl7_version);
        ret.setDescription(row.description);
        ret.setType(row.type);
        if (row.type.equals("complex")) {
            ret.setChildren(getComponents(row));
        }
        return ret;
    }
    private List<Component> getComponents(DatatypeRow row) {
        List<Component> components = new ArrayList<>();
        List<ComponentRow> componentRows = datatypeMappingService.findChildrenByNameAndVersion(row.data_type_code, row.hl7_version);
        if(componentRows != null ){
            for(ComponentRow componentRow: componentRows){
                components.add(convertComponent(componentRow, row.data_type_code));
            }
        }
        return components;
    }

    private Component convertComponent(ComponentRow row, String name) {
        Component c = new Component();
        c.setPosition(row.position);
        c.setName(row.description);
        c.setUsage(row.usage);
        c.setMinLength(row.minLength);
        c.setMaxLength(row.maxLength);
        c.setConfLength(row.confLength);
        c.setVersion(row.version);
        if(!row.table.equals("0000")){
            c.setTable(row.table);
        }
        if(row.datatype.toLowerCase().equals(name)){
            c.setDatatype("-");
        }else {
            c.setDatatype(row.datatype);
        }
        return c;
    }
}
