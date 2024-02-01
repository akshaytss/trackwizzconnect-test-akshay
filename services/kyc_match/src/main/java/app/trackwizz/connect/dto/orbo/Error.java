package app.trackwizz.connect.dto.orbo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

    @JsonProperty("message")
    private String Message;

    @JsonProperty("code")
    private String Code ;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
