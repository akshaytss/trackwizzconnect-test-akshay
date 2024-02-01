package app.trackwizz.connect.helper.confidenceComponent;

import app.trackwizz.connect.dto.kycmatch.ConfidenceInput;
import app.trackwizz.connect.enums.ImageExtractionEnums;
import org.springframework.util.StringUtils;

public class GetEmailIdConfidenceLevel {

    public void getConfidenceLevel(ConfidenceInput data) {
        if (!StringUtils.hasText(data.getRequestData().getMobileNo())) {
            return;
        }

        data.getResponseData().setAadhaarEmailIDConfidence(getEmailConfidence(data.getRequestData().getEmailId(), data.getResponseData().getAadhaarImageExtractedEmailID()));
        data.getResponseData().seteKYCAuthEmailIDConfidence(getEmailConfidence(data.getRequestData().getEmailId(), data.getResponseData().geteKYCAuthImageExtractedEmailID()));
        data.getResponseData().setOfflineAadhaarEmailIDConfidence(getEmailConfidence(data.getRequestData().getEmailId(), data.getResponseData().getOfflineAadhaarImageExtractedEmailID()));
    }

    private String getEmailConfidence(String inputEmail, String extractedEmail) {
        if (!StringUtils.hasText(extractedEmail)) {
            return ImageExtractionEnums.NotRead.toString();
        }

        try {
            if (inputEmail.equalsIgnoreCase(extractedEmail)) {
                return ImageExtractionEnums.High.toString();
            } else {
                return ImageExtractionEnums.Low.toString();
            }
        } catch (Exception e) {
            return ImageExtractionEnums.Low.toString();
        }
    }
}

