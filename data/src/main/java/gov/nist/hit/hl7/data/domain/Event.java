package gov.nist.hit.hl7.data.domain;

public class Event {


    private String name;
    private String description;
    private String hl7Version;


    public String getHl7Version() {
        return hl7Version;
    }

    public void setHl7Version(String hl7Version) {
        this.hl7Version = hl7Version;
    }


}
