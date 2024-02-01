package app.trackwizz.connect.exception;

import app.trackwizz.connect.service.orbo.KycExtractionOrboExternalService;

public class KycExtractionOrboExternalServiceException extends Exception {
    private static final String DEFAULT_EXCEPTION_PREFIX = String.format("Error while processing request at %s \n", KycExtractionOrboExternalService.class.getName());

    public KycExtractionOrboExternalServiceException() {
        super(DEFAULT_EXCEPTION_PREFIX);
    }

    public KycExtractionOrboExternalServiceException(String message) {
        super(DEFAULT_EXCEPTION_PREFIX + message);
    }
}
