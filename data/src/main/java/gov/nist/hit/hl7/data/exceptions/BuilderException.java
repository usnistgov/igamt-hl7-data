package gov.nist.hit.hl7.data.exceptions;
import gov.nist.hit.hl7.igamt.common.base.domain.Type;

public class BuilderException  extends  Exception {
    public BuilderException() {
    }

    public BuilderException(String message) {
        super(message);
    }

    public BuilderException(Type type, String message) {
        super("["+type + message);
    }

    public BuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuilderException(Throwable cause) {
        super(cause);
    }

    public BuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
