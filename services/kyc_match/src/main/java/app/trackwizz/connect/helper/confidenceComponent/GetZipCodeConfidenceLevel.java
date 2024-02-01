package app.trackwizz.connect.helper.confidenceComponent;

import app.trackwizz.connect.dto.kycmatch.ConfidenceInput;
import app.trackwizz.connect.enums.ImageExtractionEnums;
import org.springframework.util.StringUtils;

public class GetZipCodeConfidenceLevel {
    public void getConfidenceLevel(ConfidenceInput data) {
        if (isValidZipCode(data.getRequestData().getCorPin())) {
            var aadhaarZipDetails = getZipCodeConfidence(data.getRequestData().getCorPin(), data.getResponseData().getAadhaarImageExtractedAddress());
            data.getResponseData().setAadhaarCorZipConfidence(aadhaarZipDetails.getZipConfidence());
            data.getResponseData().setAadhaarCorStateConfidence(aadhaarZipDetails.getStateConfidence());

            var ekycAuthZipDetails = StringUtils.hasText(data.getResponseData().geteKYCAuthImageExtractedZipCode()) ? getZipCodeConfidence(data.getRequestData().getCorPin(), data.getResponseData().geteKYCAuthImageExtractedZipCode()) : getZipCodeConfidence(data.getRequestData().getCorPin(), data.getResponseData().geteKYCAuthImageExtractedAddress());
            data.getResponseData().seteKYCAuthCorZipConfidence(ekycAuthZipDetails.getZipConfidence());
            data.getResponseData().seteKYCAuthCorStateConfidence(ekycAuthZipDetails.getStateConfidence());

            var offlineAadhaarZipDetails = getZipCodeConfidence(data.getRequestData().getCorPin(), data.getResponseData().getOfflineAadhaarImageExtractedAddress());
            data.getResponseData().setOfflineAadhaarCorZipConfidence(offlineAadhaarZipDetails.getZipConfidence());
            data.getResponseData().setOfflineAadhaarCorStateConfidence(offlineAadhaarZipDetails.getStateConfidence());

            var passportZipDetails = getZipCodeConfidence(data.getRequestData().getCorPin(), data.getResponseData().getpPImageExtractedAddress());
            data.getResponseData().setPassportCorZipConfidence(passportZipDetails.getZipConfidence());
            data.getResponseData().setPassportCorStateConfidence(passportZipDetails.getStateConfidence());

            var voterZipDetails = getZipCodeConfidence(data.getRequestData().getCorPin(), data.getResponseData().getVoterCardImageExtractedAddress());
            data.getResponseData().setVoterCardCorZipConfidence(voterZipDetails.getZipConfidence());
            data.getResponseData().setVoterCardCorStateConfidence(voterZipDetails.getStateConfidence());

            var dLZipDetails = getZipCodeConfidence(data.getRequestData().getCorPin(), data.getResponseData().getdLImageExtractedAddress());
            data.getResponseData().setdLCorZipConfidence(dLZipDetails.getZipConfidence());
            data.getResponseData().setdLCorStateConfidence(dLZipDetails.getStateConfidence());
        }

        if (isValidZipCode(data.getRequestData().getPerPin())) {
            var aadhaarZipDetails = getZipCodeConfidence(data.getRequestData().getPerPin(), data.getResponseData().getAadhaarImageExtractedAddress());
            data.getResponseData().setAadhaarPerZipConfidence(aadhaarZipDetails.getZipConfidence());
            data.getResponseData().setAadhaarPerStateConfidence(aadhaarZipDetails.getStateConfidence());

            var ekycAuthZipDetails = StringUtils.hasText(data.getResponseData().geteKYCAuthImageExtractedZipCode()) ? getZipCodeConfidence(data.getRequestData().getPerPin(), data.getResponseData().geteKYCAuthImageExtractedZipCode()) : getZipCodeConfidence(data.getRequestData().getPerPin(), data.getResponseData().geteKYCAuthImageExtractedAddress());
            data.getResponseData().seteKYCAuthPerZipConfidence(ekycAuthZipDetails.getZipConfidence());
            data.getResponseData().seteKYCAuthPerStateConfidence(ekycAuthZipDetails.getStateConfidence());

            var offlineAadhaarZipDetails = getZipCodeConfidence(data.getRequestData().getPerPin(), data.getResponseData().getOfflineAadhaarImageExtractedAddress());
            data.getResponseData().setOfflineAadhaarPerZipConfidence(offlineAadhaarZipDetails.getZipConfidence());
            data.getResponseData().setOfflineAadhaarPerStateConfidence(offlineAadhaarZipDetails.getStateConfidence());

            var passportZipDetails = getZipCodeConfidence(data.getRequestData().getPerPin(), data.getResponseData().getpPImageExtractedAddress());
            data.getResponseData().setPassportPerZipConfidence(passportZipDetails.getZipConfidence());
            data.getResponseData().setPassportPerStateConfidence(passportZipDetails.getStateConfidence());

            var voterZipDetails = getZipCodeConfidence(data.getRequestData().getPerPin(), data.getResponseData().getVoterCardImageExtractedAddress());
            data.getResponseData().setVoterCardPerZipConfidence(voterZipDetails.getZipConfidence());
            data.getResponseData().setVoterCardPerStateConfidence(voterZipDetails.getStateConfidence());

            var dLZipDetails = getZipCodeConfidence(data.getRequestData().getPerPin(), data.getResponseData().getdLImageExtractedAddress());
            data.getResponseData().setdLPerZipConfidence(dLZipDetails.getZipConfidence());
            data.getResponseData().setdLPerStateConfidence(dLZipDetails.getStateConfidence());
        }
    }

    private boolean isValidZipCode(String zipCode) {
        return StringUtils.hasText(zipCode) && zipCode.length() == 6 && zipCode.chars().allMatch(Character::isDigit);
    }

    private ZipDetails getZipCodeConfidence(String inputZipCode, String extractedAddress) {
        if (!StringUtils.hasText(extractedAddress))
            return new ZipDetails(ImageExtractionEnums.NotRead.toString(), null);
        try {
            String extractedZipCode = getZipCodeFromAddress(extractedAddress);
            if (inputZipCode.equals(extractedZipCode)) // exact match
                return new ZipDetails(ImageExtractionEnums.High.toString(), null);
            else if (inputZipCode.length() == 6 && inputZipCode.startsWith(extractedZipCode)) // left to right
                return new ZipDetails(ImageExtractionEnums.Almost_High.toString(), null);
            else if (inputZipCode.length() == 6 && inputZipCode.substring(1).equals(extractedZipCode)) // right to left
                return new ZipDetails(ImageExtractionEnums.Almost_High.toString(), "");
            else
                return new ZipDetails(ImageExtractionEnums.Low.toString(), null);
        } catch (Exception e) {
            return new ZipDetails(ImageExtractionEnums.Low.toString(), null);
        }
    }

    private String getZipCodeFromAddress(String addressInput) {
        addressInput = addressInput.trim();
        String address = getReadablePartOfAddress(addressInput);
        int currInd = address.length() - 1;
        int numLength = 0;
        StringBuilder str = new StringBuilder();
        while (currInd >= 0) {
            if (Character.isDigit(address.charAt(currInd))) {
                numLength++;
                str.insert(0, address.charAt(currInd));
            } else
                break;

            currInd--;
        }
        if (str.length() > 6) {
            str = new StringBuilder();
            numLength = 0;
            while (currInd >= 0 && !Character.isDigit(address.charAt(currInd))) {
                currInd--;
            }
            while (currInd >= 0 && numLength < 6) {
                if (Character.isDigit(address.charAt(currInd))) {
                    numLength++;
                    str.insert(0, address.charAt(currInd));
                } else {
                    break;
                }
                currInd--;
            }
        }
        return reverseZipString(str.toString());
    }

    private String getReadablePartOfAddress(String address) {
        StringBuilder str = new StringBuilder(address);
        while (!Character.isDigit(str.charAt(str.length() - 1))) {
            str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }

    private String reverseZipString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    private static class ZipDetails {
        private final String zipConfidence;
        private final String stateConfidence;

        public ZipDetails(String zipConfidence, String stateConfidence) {
            this.zipConfidence = zipConfidence;
            this.stateConfidence = stateConfidence;
        }

        public String getZipConfidence() {
            return zipConfidence;
        }

        public String getStateConfidence() {
            return stateConfidence;
        }
    }
}
