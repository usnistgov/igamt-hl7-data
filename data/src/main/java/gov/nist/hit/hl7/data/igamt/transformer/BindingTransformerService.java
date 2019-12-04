package gov.nist.hit.hl7.data.igamt.transformer;
import gov.nist.hit.hl7.data.domain.BindingWrapper;
import gov.nist.hit.hl7.data.domain.RealKey;
import gov.nist.hit.hl7.igamt.common.base.domain.SubStructElement;
import gov.nist.hit.hl7.igamt.common.binding.domain.ResourceBinding;

public interface BindingTransformerService {
    public void addBinding(ResourceBinding binding, SubStructElement ret, RealKey referenceKey, RealKey tableKey, BindingWrapper bindingInfo);
}
