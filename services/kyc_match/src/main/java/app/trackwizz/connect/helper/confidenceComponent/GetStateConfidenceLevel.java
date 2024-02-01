package app.trackwizz.connect.helper.confidenceComponent;

import app.trackwizz.connect.dto.kycmatch.ConfidenceInput;
import app.trackwizz.connect.enums.ImageExtractionEnums;
import app.trackwizz.connect.utils.KycMatchCache;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.StringJoiner;

public class GetStateConfidenceLevel {
    public void getConfidenceLevel(ConfidenceInput data) {
        if (StringUtils.hasText(data.getRequestData().getCorState())) {
            data.getResponseData().setAadhaarCorStateConfidence(
                    getStateConfidenceBasedOnZipCode(data.getRequestData().getCorState(), data.getResponseData().getAadhaarImageExtractedAddress(), data.getResponseData().getAadhaarCorZipConfidence(), data.getResponseData().getAadhaarCorStateConfidence())
            );
            data.getResponseData().seteKYCAuthCorStateConfidence(
                    StringUtils.hasText(data.getResponseData().geteKYCAuthImageExtractedState()) ?
                            getStateConfidenceBasedOnZipCode(data.getRequestData().getCorState(), data.getResponseData().geteKYCAuthImageExtractedState(), data.getResponseData().geteKYCAuthCorZipConfidence(), data.getResponseData().geteKYCAuthCorStateConfidence()) :
                            getStateConfidenceBasedOnZipCode(data.getRequestData().getCorState(), data.getResponseData().geteKYCAuthImageExtractedAddress(), data.getResponseData().geteKYCAuthCorZipConfidence(), data.getResponseData().geteKYCAuthCorStateConfidence())
            );
            data.getResponseData().setOfflineAadhaarCorStateConfidence(
                    getStateConfidenceBasedOnZipCode(data.getRequestData().getCorState(), data.getResponseData().getOfflineAadhaarImageExtractedAddress(), data.getResponseData().getOfflineAadhaarCorZipConfidence(), data.getResponseData().getOfflineAadhaarCorStateConfidence())
            );
            data.getResponseData().setdLCorStateConfidence(
                    getStateConfidenceBasedOnZipCode(data.getRequestData().getCorState(), data.getResponseData().getdLImageExtractedAddress(), data.getResponseData().getdLCorZipConfidence(), data.getResponseData().getdLCorStateConfidence())
            );
            data.getResponseData().setPassportCorStateConfidence(
                    getStateConfidenceBasedOnZipCode(data.getRequestData().getCorState(), data.getResponseData().getpPImageExtractedAddress(), data.getResponseData().getPassportCorZipConfidence(), data.getResponseData().getPassportCorStateConfidence())
            );
            data.getResponseData().setVoterCardCorStateConfidence(
                    getStateConfidenceBasedOnZipCode(data.getRequestData().getCorState(), data.getResponseData().getVoterCardImageExtractedAddress(), data.getResponseData().getVoterCardCorZipConfidence(), data.getResponseData().getVoterCardCorStateConfidence())
            );
        }

        if (StringUtils.hasText(data.getRequestData().getPerState())) {
            data.getResponseData().setAadhaarPerStateConfidence(
                    getStateConfidenceBasedOnZipCode(data.getRequestData().getPerState(), data.getResponseData().getAadhaarImageExtractedAddress(), data.getResponseData().getAadhaarPerZipConfidence(), data.getResponseData().getAadhaarPerStateConfidence())
            );
            data.getResponseData().seteKYCAuthPerStateConfidence(
                    StringUtils.hasText(data.getResponseData().geteKYCAuthImageExtractedState()) ?
                            getStateConfidenceBasedOnZipCode(data.getRequestData().getPerState(), data.getResponseData().geteKYCAuthImageExtractedState(), data.getResponseData().geteKYCAuthPerZipConfidence(), data.getResponseData().geteKYCAuthPerStateConfidence()) :
                            getStateConfidenceBasedOnZipCode(data.getRequestData().getPerState(), data.getResponseData().geteKYCAuthImageExtractedAddress(), data.getResponseData().geteKYCAuthPerZipConfidence(), data.getResponseData().geteKYCAuthPerStateConfidence())
            );
            data.getResponseData().setOfflineAadhaarPerStateConfidence(
                    getStateConfidenceBasedOnZipCode(data.getRequestData().getPerState(), data.getResponseData().getOfflineAadhaarImageExtractedAddress(), data.getResponseData().getOfflineAadhaarPerZipConfidence(), data.getResponseData().getOfflineAadhaarPerStateConfidence())
            );
            data.getResponseData().setdLPerStateConfidence(
                    getStateConfidenceBasedOnZipCode(data.getRequestData().getPerState(), data.getResponseData().getdLImageExtractedAddress(), data.getResponseData().getdLPerZipConfidence(), data.getResponseData().getdLPerStateConfidence())
            );
            data.getResponseData().setPassportPerStateConfidence(
                    getStateConfidenceBasedOnZipCode(data.getRequestData().getPerState(), data.getResponseData().getpPImageExtractedAddress(), data.getResponseData().getPassportPerZipConfidence(), data.getResponseData().getPassportPerStateConfidence())
            );
            data.getResponseData().setVoterCardPerStateConfidence(
                    getStateConfidenceBasedOnZipCode(data.getRequestData().getPerState(), data.getResponseData().getVoterCardImageExtractedAddress(), data.getResponseData().getVoterCardPerZipConfidence(), data.getResponseData().getVoterCardPerStateConfidence())
            );
        }
    }

    private String getStateConfidenceBasedOnZipCode(String inputStateCode, String extractedAddress, String zipConfidence, String dummyValueStateConfidence) {
        try {
            if ("Low".equals(zipConfidence))
                return ImageExtractionEnums.Not_Applicable.toString();
            else if ("High".equals(zipConfidence))
                return ImageExtractionEnums.No_Check_Required.toString();
            else if ("Almost_High".equals(zipConfidence) && dummyValueStateConfidence == null)
                return ImageExtractionEnums.No_Check_Required.toString();
            else if (!StringUtils.hasText(extractedAddress))
                return ImageExtractionEnums.NotRead.toString();
            else if ("Almost_High".equals(zipConfidence) && dummyValueStateConfidence != null) {
                String extractedState = getStateFromAddress(extractedAddress);
                String extractedStateCode = KycMatchCache.stateNameStateCodeCache.get(extractedState);
                if (("AP".equalsIgnoreCase(extractedStateCode) && "TS".equalsIgnoreCase(inputStateCode)) || ("TS".equalsIgnoreCase(extractedStateCode) && "AP".equalsIgnoreCase(inputStateCode)))
                    return ImageExtractionEnums.High.toString();
                if (extractedStateCode.equalsIgnoreCase(inputStateCode))
                    return ImageExtractionEnums.High.toString();
                else
                    return ImageExtractionEnums.No_Match.toString();
            }
            return zipConfidence;
        } catch (Exception e) {
            return ImageExtractionEnums.No_Match.toString();
        }
    }

    private int getZipCodeStartIndexFromAddress(String address) {
        int currInd = address.length() - 1;
        int numLength = 0;
        StringBuilder str = new StringBuilder();
        while (currInd >= 0) {
            if (Character.isDigit(address.charAt(currInd))) {
                currInd--;
                numLength++;
            } else {
                break;
            }
        }
        if (str.length() > 6) {
            str = str.delete(0, str.length());
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
        return currInd + 1;
    }

    private String getStateFromAddress(String address) {
        address = address.trim().toLowerCase();
        if (!Character.isDigit(address.charAt(address.length() - 1))) {
            address = address.substring(0, address.length() - 1);
        }
        String[] addressWordsList;
        String stateName = "";
        address.replace("&", "and");
        if (address.contains(",")) {
            addressWordsList = address.split(",");
            String temp = addressWordsList[addressWordsList.length - 1];
            if (temp.contains("-")) // cases like: delhi-400006
                stateName = temp.split("-")[0];
            else {
                stateName = addressWordsList[addressWordsList.length - 2];
                if (stateName.contains("-"))
                    stateName = getState(stateName);
            }
            return stateName;
        }

        address = getReadablePartOfAddress(address);
        int zipCodeStartIndex = getZipCodeStartIndexFromAddress(address);
        address = address.substring(0, zipCodeStartIndex).trim();
        addressWordsList = address.split(" ");
        String leftMostWord = addressWordsList[addressWordsList.length - 1];
        HashSet<String> twoWordStateLeftMostWordSet = new HashSet<>();
        twoWordStateLeftMostWordSet.add("pradesh");
        twoWordStateLeftMostWordSet.add("nadu");
        twoWordStateLeftMostWordSet.add("bengal");
        twoWordStateLeftMostWordSet.add("kashmir");
        if (twoWordStateLeftMostWordSet.contains(leftMostWord)) {
            stateName = String.join("", addressWordsList[addressWordsList.length - 2], addressWordsList[addressWordsList.length - 1]);
        } else if ("islands".contains(leftMostWord) || "nicobar".contains(leftMostWord))
            stateName = "andamanandnicobar";
        else if ("diu".contains(leftMostWord) || "haveli".contains(leftMostWord))
            stateName = "dadraandnagarhavelianddamananddiu";
        else
            stateName = leftMostWord;
        return stateName;
    }

    private String getState(String stateName) {
        int ind = stateName.indexOf('-');
        String temp = stateName.substring(0, ind);
        StringBuilder str = new StringBuilder(temp.trim());
        ind = str.length() - 1;
        StringBuilder state = new StringBuilder();
        while (ind >= 0) {
            if (str.charAt(ind) == '.' || str.charAt(ind) == ',')
                break;
            if (str.charAt(ind) == ' ') {
                String s = state.toString().toLowerCase();
                if (!"pradesh".contains(s) && !"nadu".contains(s) && !"bengal".contains(s))
                    break;
            }
            state.insert(0, str.charAt(ind));
            ind--;
        }
        return state.toString();
    }

    private String getReadablePartOfAddress(String address) {
        StringBuilder str = new StringBuilder(address);
        while (!Character.isDigit(str.charAt(str.length() - 1))) {
            str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }
}
