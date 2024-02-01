package app.trackwizz.connect.dto.photomatch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FaceMatchResponseDto {
    private String confidenceScore;
    private String result;
    private String error;

    public String getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(String confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
