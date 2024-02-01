package app.trackwizz.connect.helper.confidenceComponent;

import app.trackwizz.connect.constant.common.CommonConstants;
import app.trackwizz.connect.dto.kycmatch.ConfidenceInput;
import app.trackwizz.connect.utils.NameMatchUtil;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GetFatherNameConfidenceLevel {
    private static final double minCharPer = 50.0;
    public NameMatchUtil nameMatchUtil;

    public void getConfidenceLevel(ConfidenceInput data) {
        if (!StringUtils.hasText(data.getRequestData().getFatherName())) {
            return;
        }

        nameMatchUtil = new NameMatchUtil();
        List<String> inputNameSplited = nameMatchUtil.removeNoiseWords(data.getRequestData().getFatherName());
        List<String> result = Arrays.stream(data.getRequestData().getFatherName().toLowerCase().trim().split("\\s+"))
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toList());
        List<String> inputNameAllPossibleMatchForBindingCheck = nameMatchUtil.getAllPossibleMatchStringList(result).stream()
                .map(innerList -> String.join("", innerList))
                .distinct()
                .collect(Collectors.toList());

        Map<String, String> inputNameSplitedDic = inputNameSplited.stream().distinct().collect(Collectors.toMap(Function.identity(), Function.identity()));

        int inputNameCount = inputNameSplitedDic.size();

        Map<String, String> inputNameWithoutVowelDic = nameMatchUtil.removeNoiseWords(
                // TODO create constants file move this there
                        Pattern.compile(CommonConstants.REGEX_AEIOU_PLUS, Pattern.CASE_INSENSITIVE).matcher(data.getRequestData().getFatherName().toLowerCase()).replaceAll(""))
                .stream()
                .distinct()
                .collect(Collectors.toMap(Function.identity(), Function.identity()));

        char[] inputCharArray = data.getRequestData().getFatherName().toCharArray();
        int noOfFuzzyCharAllowed = (int) (inputCharArray.length * minCharPer) / 100;

        data.getResponseData().setAadhaarFatherNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount,
                        inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed,
                        Optional.ofNullable(data.getResponseData().getAadhaarImageExtractedFatherName()).map(String::toLowerCase).orElse(null))
        );

        data.getResponseData().setOfflineAadhaarFatherNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount,
                        inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed,
                        Optional.ofNullable(data.getResponseData().getOfflineAadhaarImageExtractedFatherName()).map(String::toLowerCase).orElse(null))
        );

        data.getResponseData().seteKYCAuthFatherNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount,
                        inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed,
                        Optional.ofNullable(data.getResponseData().geteKYCAuthImageExtractedFatherName()).map(String::toLowerCase).orElse(null))
        );

        data.getResponseData().setdLFatherNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount,
                        inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed,
                        Optional.ofNullable(data.getResponseData().getdLImageExtractedFatherName()).map(String::toLowerCase).orElse(null))
        );

        data.getResponseData().setPanFatherNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount,
                        inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed,
                        Optional.ofNullable(data.getResponseData().getPanImageExtractedFatherName()).map(String::toLowerCase).orElse(null))
        );

        data.getResponseData().setPassportFatherNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount,
                        inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed,
                        Optional.ofNullable(data.getResponseData().getpPImageExtractedFatherName()).map(String::toLowerCase).orElse(null))
        );

        data.getResponseData().setVoterCardFatherNameConfidence(
                nameMatchUtil.getConfidence(inputNameSplited, inputNameSplitedDic, inputNameCount,
                        inputNameAllPossibleMatchForBindingCheck, inputNameWithoutVowelDic,
                        inputCharArray, noOfFuzzyCharAllowed,
                        Optional.ofNullable(data.getResponseData().getVoterCardImageExtractedFatherName()).map(String::toLowerCase).orElse(null))
        );
    }
}
