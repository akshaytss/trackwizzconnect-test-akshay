package app.trackwizz.connect.helper.confidenceComponent;

import app.trackwizz.connect.constant.common.CommonConstants;
import app.trackwizz.connect.dto.kycmatch.ConfidenceInput;
import app.trackwizz.connect.utils.NameMatchUtil;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GetNameConfidenceLevel {
    private static final double MIN_CHAR_PERCENTAGE = 50.0;

    public void getConfidenceLevel(ConfidenceInput data) {
        if (!StringUtils.hasText(data.getRequestData().getName()))
            return;

        NameMatchUtil nameMatchUtil = new NameMatchUtil();

        List<String> inputNameSplited = nameMatchUtil.removeNoiseWords(data.getRequestData().getName());
        Map<String, String> inputNameSplitedDic = inputNameSplited.stream().distinct().collect(Collectors.toMap(t -> t, t -> t));
        int inputNameCount = inputNameSplitedDic.size();

        List<String> result = Arrays.stream(data.getRequestData().getName().toLowerCase().trim().split("\\s+"))
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toList());
        List<String> inputNameAllPossibleMatchForBindingCheck = nameMatchUtil.getAllPossibleMatchStringList(result).stream()
                .map(innerList -> String.join("", innerList))
                .distinct()
                .collect(Collectors.toList());


        Map<String, String> inputNameWithoutVowelDic = nameMatchUtil.removeNoiseWords(
                Pattern.compile(CommonConstants.REGEX_AEIOU_PLUS, Pattern.CASE_INSENSITIVE).matcher(data.getRequestData().getName().toLowerCase()).replaceAll("")
        ).stream().distinct().collect(Collectors.toMap(t -> t, t -> t));
        char[] inputCharArray = data.getRequestData().getName().toCharArray();
        int noOfFuzzyCharAllowed = (int) (inputCharArray.length * MIN_CHAR_PERCENTAGE) / 100;

        data.getResponseData().setAadhaarNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount, inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed, Objects.requireNonNull(data.getResponseData().getAadhaarImageExtractedName()).toLowerCase())
        );

        data.getResponseData().setOfflineAadhaarNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount, inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed, Objects.requireNonNull(data.getResponseData().getOfflineAadhaarImageExtractedName()).toLowerCase())
        );

        data.getResponseData().seteKYCAuthNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount, inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed, Objects.requireNonNull(data.getResponseData().geteKYCAuthImageExtractedName()).toLowerCase())
        );

        data.getResponseData().setdLNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount, inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed, Objects.requireNonNull(data.getResponseData().getdLImageExtractedName()).toLowerCase())
        );

        data.getResponseData().setPanNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount, inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed, Objects.requireNonNull(data.getResponseData().getPanImageExtractedName()).toLowerCase())
        );

        data.getResponseData().setPassportNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount, inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed, Objects.requireNonNull(data.getResponseData().getpPImageExtractedName()).toLowerCase())
        );

        data.getResponseData().setVoterCardNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount, inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed, Objects.requireNonNull(data.getResponseData().getVoterCardImageExtractedName()).toLowerCase())
        );
    }
}
