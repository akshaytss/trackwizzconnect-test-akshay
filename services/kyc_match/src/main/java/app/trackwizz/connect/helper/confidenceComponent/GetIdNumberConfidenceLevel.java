package app.trackwizz.connect.helper.confidenceComponent;

import app.trackwizz.connect.dto.kycmatch.ConfidenceInput;
import app.trackwizz.connect.enums.ImageExtractionEnums;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class GetIdNumberConfidenceLevel {

    public void getConfidenceLevel(ConfidenceInput extractedData) {
        extractedData.getResponseData().setAadhaarImageExtractedNumber(getLast4Digits(extractedData.getResponseData().getAadhaarImageExtractedNumber()));
        extractedData.getResponseData().setAadhaarNumberConfidence(
                getIdNumberConfidence(extractedData.getResponseData().getAadhaarImageExtractedNumber(), extractedData.getRequestData().getAadhaarNumber(), true));

        extractedData.getResponseData().seteKYCAuthImageExtractedNumber(getLast4Digits(extractedData.getResponseData().geteKYCAuthImageExtractedNumber()));
        extractedData.getResponseData().seteKYCAuthNumberConfidence(
                getIdNumberConfidence(extractedData.getResponseData().geteKYCAuthImageExtractedNumber(), extractedData.getRequestData().getAadhaarNumber(), true));

        extractedData.getResponseData().setOfflineAadhaarImageExtractedNumber(getLast4Digits(extractedData.getResponseData().getOfflineAadhaarImageExtractedNumber()));
        extractedData.getResponseData().setOfflineAadhaarNumberConfidence(
                getIdNumberConfidence(extractedData.getResponseData().getOfflineAadhaarImageExtractedNumber(), extractedData.getRequestData().getAadhaarNumber(), true));

        extractedData.getResponseData().setPanNumberConfidence(
                getIdNumberConfidence(extractedData.getResponseData().getPanImageExtractedNumber(), extractedData.getRequestData().getPanNumber()));

        extractedData.getResponseData().setPassportNumberConfidence(
                getIdNumberConfidence(extractedData.getResponseData().getpPImageExtractedNumber(), extractedData.getRequestData().getPassportNumber()));

        extractedData.getResponseData().setdLNumberConfidence(
                getIdNumberConfidence(extractedData.getResponseData().getdLImageExtractedNumber(), extractedData.getRequestData().getdLNumber()));

        extractedData.getResponseData().setVoterCardNumberConfidence(
                getIdNumberConfidence(extractedData.getResponseData().getVoterCardImageExtractedNumber(), extractedData.getRequestData().getVoterNumber()));
    }

    private String getIdNumberConfidence(String extractedIdNumber, String requestIdNumber) {
        if (!StringUtils.hasText(requestIdNumber)) {
            return null;
        } else if (!StringUtils.hasText(extractedIdNumber)) {
            return ImageExtractionEnums.NotRead.toString();
        }
        requestIdNumber = requestIdNumber.replace(" ", "");
        extractedIdNumber = extractedIdNumber.replace(" ", "");

        if (Objects.equals(extractedIdNumber, requestIdNumber)) {
            return ImageExtractionEnums.High.toString();
        } else {
            return ImageExtractionEnums.Low.toString();
        }
    }


    private String getIdNumberConfidence(String extractedIdNumber, String requestIdNumber, boolean isAadhar) {
        if (!StringUtils.hasText(requestIdNumber)) {
            return null;
        } else if (!StringUtils.hasText(extractedIdNumber)) {
            return ImageExtractionEnums.NotRead.toString();
        }
        requestIdNumber = requestIdNumber.replace(" ", "");
        extractedIdNumber = extractedIdNumber.replace(" ", "");

        if (isAadhar && extractedIdNumber.length() >= 4) {
            String requiredRequestIdDigits = getLast4DigitsInReversed(requestIdNumber);
            String requiredExtractedIdDigits = getLast4DigitsInReversed(extractedIdNumber);
            if (Objects.equals(requiredExtractedIdDigits, requiredRequestIdDigits))
                return ImageExtractionEnums.High.toString();
            else
                return ImageExtractionEnums.Low.toString();
        } else if (Objects.equals(extractedIdNumber, requestIdNumber)) {
            return ImageExtractionEnums.High.toString();
        } else {
            return ImageExtractionEnums.Low.toString();
        }
    }


    private String getLast4DigitsInReversed(String extractedIdNumber) {
        String revStr = new StringBuilder(extractedIdNumber).reverse().toString();
        return revStr.substring(0, 4);
    }

    private String getLast4Digits(String extractedIdNumber) {
        if (extractedIdNumber == null || extractedIdNumber.length() <= 3)
            return extractedIdNumber;
        String last4Digits = getLast4DigitsInReversed(extractedIdNumber);
        last4Digits = getLast4DigitsInReversed(last4Digits);
        return last4Digits;
    }
}

