package app.trackwizz.connect.dto.photomatch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

    @JsonProperty("message")
    private String message;

    // Constructors, getters, setters
    public String getMessage(){ return message; }
}
