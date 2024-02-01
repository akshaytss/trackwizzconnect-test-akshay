package app.trackwizz.connect.dto.kycmatch;

public class ConfidenceInput {

    public KycMatchRequestDto requestData;
    public KycMatchResponseDto responseData;

    public KycMatchRequestDto getRequestData() {
        return requestData;
    }

    public void setRequestData(KycMatchRequestDto requestData) {
        this.requestData = requestData;
    }

    public KycMatchResponseDto getResponseData() {
        return responseData;
    }

    public void setResponseData(KycMatchResponseDto responseData) {
        this.responseData = responseData;
    }
}
