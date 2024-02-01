package app.trackwizz.connect.dto.photomatch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data {

    @JsonProperty("confidence_score")
    private String confidenceScore;

    @JsonProperty("result")
    private String result;

    // Constructors, getters, setters
    public String getConfidenceScore(){ return confidenceScore; }
}
