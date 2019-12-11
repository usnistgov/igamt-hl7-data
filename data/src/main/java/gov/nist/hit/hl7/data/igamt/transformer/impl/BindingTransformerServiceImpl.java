package gov.nist.hit.hl7.data.igamt.transformer.impl;

import gov.nist.hit.hl7.data.domain.BindingWrapper;
import gov.nist.hit.hl7.data.domain.RealKey;
import gov.nist.hit.hl7.data.igamt.transformer.BindingTransformerService;
import gov.nist.hit.hl7.igamt.common.base.domain.SubStructElement;
import gov.nist.hit.hl7.igamt.common.base.domain.ValuesetBinding;
import gov.nist.hit.hl7.igamt.common.base.domain.ValuesetStrength;
import gov.nist.hit.hl7.igamt.common.binding.domain.LocationInfo;
import gov.nist.hit.hl7.igamt.common.binding.domain.LocationType;
import gov.nist.hit.hl7.igamt.common.binding.domain.ResourceBinding;
import gov.nist.hit.hl7.igamt.common.binding.domain.StructureElementBinding;
import gov.nist.hit.hl7.igamt.common.config.domain.BindingInfo;
import gov.nist.hit.hl7.igamt.common.config.domain.BindingLocationOption;
import gov.nist.hit.hl7.igamt.common.config.repository.ConfigRepository;
import gov.nist.hit.hl7.igamt.datatype.domain.Component;
import gov.nist.hit.hl7.igamt.segment.domain.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gov.nist.hit.hl7.igamt.common.config.domain.Config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.Set;
@Service
public class BindingTransformerServiceImpl implements BindingTransformerService {

    @Autowired
    ConfigRepository configRepository;

    @Override
    public void addBinding(ResourceBinding binding, SubStructElement ret, RealKey referenceKey, RealKey tableKey, BindingWrapper bindingInfo) {
            StructureElementBinding elmBinding = new StructureElementBinding();
            Set<ValuesetBinding> vsBindings = new HashSet<>();
            ValuesetBinding valuesetBinding = new ValuesetBinding();
            List<String> vs = new ArrayList<String>();
            vs.add(bindingInfo.id);
            valuesetBinding.setValueSets(vs);
            if(bindingInfo.type.equals("HL7")) {
                valuesetBinding.setStrength(ValuesetStrength.R);
            }else {
                valuesetBinding.setStrength(ValuesetStrength.S);
            }
            Config config = configRepository.findAll().get(0);
            if(config.getValueSetBindingConfig().containsKey(referenceKey.getName())) {
                BindingInfo info = config.getValueSetBindingConfig().get(referenceKey.getName());
                if( info != null && info.isComplex()) {
                    List<BindingLocationOption> options = info.getAllowedBindingLocations().get(referenceKey.getVersion().replace(".", "-"));
                    valuesetBinding.setValuesetLocations(new HashSet<>(options.get(0).getValue()));
                }
            }
            vsBindings.add(valuesetBinding);
            elmBinding.setValuesetBindings(vsBindings);
            elmBinding.setElementId(ret.getId());
            LocationInfo info = new LocationInfo();
            info.setPosition(ret.getPosition());
            info.setName(ret.getName());
            if (ret instanceof Component){
                info.setType(LocationType.COMPONENT);
            } else if (ret instanceof Field) {
                info.setType(LocationType.FIELD);
            }
            elmBinding.setLocationInfo(info);
            binding.getChildren().add(elmBinding);

    }
}
