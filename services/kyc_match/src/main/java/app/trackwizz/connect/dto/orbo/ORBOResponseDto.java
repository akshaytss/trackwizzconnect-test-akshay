package app.trackwizz.connect.dto.orbo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ORBOResponseDto {
    @JsonProperty("data")
    private List<ORBOKYCExtractionResponseDto> data;

    @JsonProperty("error")
    private Error error;

    public List<ORBOKYCExtractionResponseDto> getData() {
        return data;
    }

    public void setData(List<ORBOKYCExtractionResponseDto> data) {
        this.data = data;
    }
    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

}
