package gov.nist.hit.hl7.data.fixes.domain;
import gov.nist.hit.hl7.igamt.common.base.domain.Type;
import gov.nist.hit.hl7.igamt.common.change.entity.domain.ChangeType;

import java.util.Map;

public class Fix {

    private String subject;
    private Type subjectType;
    private String version;
    private int location;
    private Type locationType;
    private Map<Object, Object> change;
    private ChangeType changeType;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Type getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(Type subjectType) {
        this.subjectType = subjectType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public Type getLocationType() {
        return locationType;
    }

    public void setLocationType(Type locationType) {
        this.locationType = locationType;
    }

    public Map<Object, Object> getChange() {
        return change;
    }

    public void setChange(Map<Object, Object> change) {
        this.change = change;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }
}
