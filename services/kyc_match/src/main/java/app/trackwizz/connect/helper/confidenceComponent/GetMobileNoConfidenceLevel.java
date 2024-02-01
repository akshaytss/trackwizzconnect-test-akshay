package app.trackwizz.connect.helper.confidenceComponent;

import app.trackwizz.connect.dto.kycmatch.ConfidenceInput;
import app.trackwizz.connect.enums.ImageExtractionEnums;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class GetMobileNoConfidenceLevel {

    public void getConfidenceLevel(ConfidenceInput data) {
        if (!StringUtils.hasText(data.getRequestData().getMobileNo()))
            return;

        data.getResponseData().setAadhaarMobileNoConfidence(getMobileNoConfidence(data.getRequestData().getMobileNo(), data.getResponseData().getAadhaarImageExtractedMobileNo()));
        data.getResponseData().seteKYCAuthMobileNoConfidence(getMobileNoConfidence(data.getRequestData().getMobileNo(), data.getResponseData().geteKYCAuthImageExtractedMobileNo()));
        data.getResponseData().setOfflineAadhaarMobileNoConfidence(getMobileNoConfidence(data.getRequestData().getMobileNo(), data.getResponseData().getOfflineAadhaarImageExtractedMobileNo()));
    }

    private String getMobileNoConfidence(String inputMobileNo, String extractedMobileNo) {
        if (!StringUtils.hasText(extractedMobileNo))
            return ImageExtractionEnums.NotRead.toString();

        try {
            if (Objects.equals(inputMobileNo, extractedMobileNo))
                return ImageExtractionEnums.High.toString();
            else if (inputMobileNo.length() >= 10 &&
                    (Objects.equals(inputMobileNo.substring(0, 9), extractedMobileNo) ||
                            Objects.equals(inputMobileNo.substring(1, 9), extractedMobileNo)))
                return ImageExtractionEnums.Almost_High.toString();
            else
                return ImageExtractionEnums.Low.toString();

        } catch (Exception e) {
            return ImageExtractionEnums.Low.toString();
        }
    }
}

