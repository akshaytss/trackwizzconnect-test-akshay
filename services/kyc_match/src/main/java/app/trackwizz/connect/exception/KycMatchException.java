package app.trackwizz.connect.exception;

import app.trackwizz.connect.service.KycMatchService;

public class KycMatchException extends Exception {

    private static final String DEFAULT_EXCEPTION_PREFIX = String.format("Error while processing request at %s \n", KycMatchService.class.getName());

    public KycMatchException() {
        super(DEFAULT_EXCEPTION_PREFIX);
    }

    public KycMatchException(String message) {
        super(DEFAULT_EXCEPTION_PREFIX + message);
    }
}