package app.trackwizz.connect.exception;

import app.trackwizz.connect.service.kyc_extraction.KycExtractionService;

public class KycExtractionServiceException extends Exception {

    private static final String DEFAULT_EXCEPTION_PREFIX = String.format("Error while processing request at %s \n", KycExtractionService.class.getName());

    public KycExtractionServiceException() {
        super(DEFAULT_EXCEPTION_PREFIX);
    }

    public KycExtractionServiceException(String message) {
        super(DEFAULT_EXCEPTION_PREFIX + message);
    }
}
