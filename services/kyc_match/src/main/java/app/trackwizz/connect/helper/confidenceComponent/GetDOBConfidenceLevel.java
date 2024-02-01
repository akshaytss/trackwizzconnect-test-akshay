package app.trackwizz.connect.helper.confidenceComponent;

import app.trackwizz.connect.dto.kycmatch.ConfidenceInput;
import app.trackwizz.connect.enums.ImageExtractionEnums;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GetDOBConfidenceLevel {
    private final String[] formats = {"d-M-yyyy", "dd-MM-yyyy", "dd-M-yyyy", "dd-MMM-yy", "dd-MM-yy", "d-M-yy", "d-MMM-yy",
            "dd-MMM-yyyy", "d-MMM-yyyy", "d/M/yyyy", "dd/MM/yyyy", "dd/M/yyyy", "dd/MMM/yy", "dd/MM/yy", "d/M/yy",
            "d/MMM/yy", "dd/MMM/yyyy", "d/MMM/yyyy", "yyyy"};

    public void getConfidenceLevel(ConfidenceInput data) {
        if (data.getRequestData().getDob() == null || data.getRequestData().getDob().isEmpty()) {
            return;
        }

        LocalDate inputDate = parseDate(data.getRequestData().getDob());
        data.getResponseData().setAadhaarDobConfidence(getDOBConfidence(inputDate, data.getResponseData().getAadhaarImageExtractedDOB()));
        data.getResponseData().seteKYCAuthDobConfidence(getDOBConfidence(inputDate, data.getResponseData().geteKYCAuthImageExtractedDOB()));
        data.getResponseData().setOfflineAadhaarDobConfidence(getDOBConfidence(inputDate, data.getResponseData().getOfflineAadhaarImageExtractedDOB()));
        data.getResponseData().setPanDobConfidence(getDOBConfidence(inputDate, data.getResponseData().getPanImageExtractedDOB()));
        data.getResponseData().setPassportDobConfidence(getDOBConfidence(inputDate, data.getResponseData().getpPImageExtractedDOB()));
        data.getResponseData().setdLDobConfidence(getDOBConfidence(inputDate, data.getResponseData().getdLImageExtractedDOB()));
        data.getResponseData().setVoterCardDobConfidence(getDOBConfidence(inputDate, data.getResponseData().getVoterCardImageExtractedDOB()));
    }

    private LocalDate parseDate(String dateString) {
        for (String format : formats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDate.parse(dateString, formatter);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private String getDOBConfidence(LocalDate requestDate, String extractedData) {
        if (extractedData == null || extractedData.isEmpty()) {
            return ImageExtractionEnums.NotRead.toString();
        }

        int confidence = 0;
        try {
            LocalDate extractedDate = parseDate(extractedData);
            if (requestDate.getYear() == extractedDate.getYear() && extractedDate.getDayOfMonth() == 1 && extractedDate.getMonthValue() == 1) {
                return ImageExtractionEnums.High.toString();
            }

            if (requestDate.getDayOfMonth() == extractedDate.getDayOfMonth()) {
                confidence++;
            }
            if (requestDate.getMonth() == extractedDate.getMonth()) {
                confidence++;
            }
            if (requestDate.getYear() == extractedDate.getYear()) {
                confidence++;
            }

            return switch (confidence) {
                case 0 -> ImageExtractionEnums.Full_Mismatch.toString();
                case 1 -> ImageExtractionEnums.Low.toString();
                case 2 -> ImageExtractionEnums.Medium.toString();
                case 3 -> ImageExtractionEnums.High.toString();
                default -> ImageExtractionEnums.NotRead.toString();
            };
        } catch (Exception e) {
            return ImageExtractionEnums.Full_Mismatch.toString();
        }
    }
}
