package gov.nist.hit.hl7.data.domain;

import java.util.Objects;

public class RealKey {
    private String version;
    private String name;

    public RealKey(String version, String name) {
        this.version = version;
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RealKey{" +
                "version='" + version + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RealKey)) return false;
        RealKey realKey = (RealKey) o;
        return version.equals(realKey.version) &&
                name.equals(realKey.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, name);
    }

}
