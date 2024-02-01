package app.trackwizz.connect.dto.orbo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OrboResponseDto {
    @JsonProperty("data")
    private List<OrboKycExtractionResponseDto> data;

    public List<OrboKycExtractionResponseDto> getData() {
        return data;
    }

    public void setData(List<OrboKycExtractionResponseDto> data) {
        this.data = data;
    }
}
