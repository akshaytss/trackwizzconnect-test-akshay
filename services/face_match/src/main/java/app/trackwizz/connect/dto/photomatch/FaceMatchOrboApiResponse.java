package app.trackwizz.connect.dto.photomatch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data",
        "error"
})
public class FaceMatchOrboApiResponse {

    @JsonProperty("data")
    private Data data;

    @JsonProperty("error")
    private Error error;

    // Constructors, getters, setters
    public Data getData(){ return data; }
    public Error getError(){ return error; }
}
