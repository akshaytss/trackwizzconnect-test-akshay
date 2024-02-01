package app.trackwizz.connect.utils;


import app.trackwizz.connect.enums.ImageExtractionEnums;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NameMatchUtil {

    private static final int minPrefixTestLength = 4;
    private static final double prefixAdjustmentScale = 0.1;
    private static final int WorkBufferSize = 200;
    private static final double defaultMismatchScore = 0.0;
    private static final double fuzzyNamePer = 98;
    private static final double minCharPer = 50;


    public String getConfidence(List<String> inputNameSplited, Map<String, String> inputNameDic, int inpuNameCount,
                                List<String> inputNameAllPossibleMatchForBindingCheck,
                                Map<String, String> inputNameWithoutVowelDic, char[] inputCharArray,
                                int noOfFuzzyCharAllowed, String extractedName) {
        if (extractedName == null || extractedName.isEmpty()) {
            return ImageExtractionEnums.NotRead.toString();
        }
        List<String> extractedNameSplit = removeNoiseWords(extractedName);
        if (isExactNameMatchWithAnyOrder(inputNameDic, inpuNameCount, extractedNameSplit)) {
            return ImageExtractionEnums.High.toString();
        }
        if (isBindingNameMatch(inputNameAllPossibleMatchForBindingCheck, extractedName)) {
            return ImageExtractionEnums.High.toString();
        }
        if (isInitialMatchAnyOrderResult(inputNameSplited, extractedName)) {
            return ImageExtractionEnums.Medium.toString();
        }
        if (isNameSubsetAnyOrderCheck(inputNameDic, extractedNameSplit)) {
            return ImageExtractionEnums.Medium.toString();
        }
        if (isSimilarSoundingOrVowelMatch(inputNameWithoutVowelDic, inpuNameCount, extractedName)) {
            return ImageExtractionEnums.Low.toString();
        }
        if (isFuzzyMatch(inputCharArray, noOfFuzzyCharAllowed, extractedName)) {
            return ImageExtractionEnums.Low.toString();
        }
        return ImageExtractionEnums.Full_Mismatch.toString();
    }

    private boolean isExactNameMatchWithAnyOrder(Map<String, String> inputName, int inpuNameCount, List<String> extractedName) {
        if (inpuNameCount != extractedName.size()) return false;

        for (String name : extractedName) {
            if (!inputName.containsKey(name)) {
                return false;
            }
        }
        return true;
    }

    private boolean isBindingNameMatch(List<String> inputNameAllPossibleMatchForBindingCheck, String extractedName) {

        List<String> result = Arrays.stream(extractedName.toLowerCase().trim().split("\\s+"))
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toList());
        List<String> extractedNameForBindingCheck = getAllPossibleMatchStringList(result).stream()
                .map(innerList -> String.join("", innerList))
                .distinct()
                .toList();

        return inputNameAllPossibleMatchForBindingCheck.stream()
                .anyMatch(extractedNameForBindingCheck::contains);
    }

    public List<List<String>> getAllPossibleMatchStringList(List<String> list) {
        List<List<String>> finalList = new ArrayList<>();

        for (int j = 0; j < list.size(); j++) {
            List<String> innerList = new ArrayList<>(list);
            wordSwap(innerList, j);

            List<String> temp = new ArrayList<>(innerList);
            for (int k = 0; k < temp.size(); k++) {
                wordSwap(temp, k);
                finalList.add(new ArrayList<>(temp));
            }
        }
        return finalList;
    }

    private void wordSwap(List<String> innerList, int j) {
        try {
            String startup = innerList.get(j);
            String nextString = "";
            if (j < innerList.size() - 1) {
                nextString = innerList.get(j + 1);
                innerList.set(j + 1, startup);
            } else {
                nextString = innerList.get(0);
                innerList.set(0, startup);
            }
            innerList.set(j, nextString);
        } catch (Exception e) {
            throw e;
        }
    }

    private boolean isInitialMatchAnyOrderResult(List<String> inputNameSplited, String extractedName) {
        boolean isRecordMatchingNameHasSingleChar = inputNameSplited.stream().anyMatch(t -> t.length() == 1);
        boolean isMatch = false;
        List<String> splitedContentInitialRule = Arrays.stream(
                        extractedName.trim().split("[\\s\\.]+"))
                .filter(x -> !x.isEmpty())
                .map(String::toLowerCase)
                .toList();

        List<String> splitedContentWithMoreThanOneWord = splitedContentInitialRule.stream()
                .filter(x -> x.length() > 1)
                .toList();

        List<String> splitedContentWithOneWord = splitedContentInitialRule.stream()
                .filter(x -> x.length() == 1)
                .toList();

        InitialOrderRuleDataModel_WordMatch initialRuleModel = new InitialOrderRuleDataModel_WordMatch();
        initialRuleModel.setContent(new ArrayList<>(splitedContentInitialRule));
        initialRuleModel.setContentWithMoreThanOneWord(new ArrayList<>(splitedContentWithMoreThanOneWord));
        initialRuleModel.setContentWithOneWord(new ArrayList<>(splitedContentWithOneWord));

        List<String> splitedClient = new ArrayList<>(inputNameSplited);

        if (initialRuleModel.getContent().size() == splitedClient.size() &&
                (isRecordMatchingNameHasSingleChar || !initialRuleModel.getContentWithOneWord().isEmpty())) {
            try {
                isMatch = doWordMatchOfOneRecord(initialRuleModel, splitedClient);
            } catch (Exception ex) {
                throw ex;
            }
        }
        return isMatch;
    }

    private boolean doWordMatchOfOneRecord(InitialOrderRuleDataModel_WordMatch watchlistDataModel, List<String> splitedClient) {
        boolean isMatchedWatchlistWithClientMoreThenOneWord = false;
        boolean isMatchedClientWithWatchMoreThenOneWord = false;

        isMatchedWatchlistWithClientMoreThenOneWord = matchWholeWord(watchlistDataModel.getContentWithMoreThanOneWord(), splitedClient);

        if (!isMatchedWatchlistWithClientMoreThenOneWord)
            isMatchedClientWithWatchMoreThenOneWord = matchWholeWord(
                    splitedClient.stream().filter(x -> x.length() > 1).toList(),
                    watchlistDataModel.getContent()
            );

        if (isMatchedWatchlistWithClientMoreThenOneWord || isMatchedClientWithWatchMoreThenOneWord) {
            boolean isMatchClientWithWatchlist = false;
            boolean isMatchWatchlistWithClient = false;

            if (watchlistDataModel.getContentWithOneWord().isEmpty() &&
                    splitedClient.stream().filter(x -> x.length() == 1).count() == 0) {
                return false;
            }

            isMatchClientWithWatchlist = watchlistDataModel.getContentWithOneWord().isEmpty() || MatchSingleWord(watchlistDataModel.getContentWithOneWord(), splitedClient);

            // Check if there's a match for single-word content in both client and watchlist
            isMatchWatchlistWithClient = splitedClient.stream().noneMatch(x -> x.length() == 1) || matchSingleWord(splitedClient.stream().filter(x -> x.length() == 1).toList(), watchlistDataModel.getContent());

            // Check if there's a match for single-word content in both client and watchlist
//            isMatchWatchlistWithClient = splitedClient.stream().filter(x -> x.length() == 1).count() > 0 ?
//                    matchSingleWord(splitedClient.stream().filter(x -> x.length() == 1).toList(),
//                            watchlistDataModel.getContent()) : true;

            return (isMatchClientWithWatchlist && isMatchWatchlistWithClient);
        }
        return false;
    }

    private static boolean MatchSingleWord(List<String> compareFrom, List<String> compareTo) {
        boolean isOneWordMatch = false;
        List<String> compareToInternal = new ArrayList<>(compareTo);

        for (String item : compareFrom) {
            int i = 0;
            while (i < compareToInternal.size()) {
                if (!compareToInternal.get(i).startsWith(item)) {
                    isOneWordMatch = false;
                    i++;
                } else {
                    compareToInternal.remove(i);
                    isOneWordMatch = true;
                    break;
                }
            }
            if (!isOneWordMatch) {
                break;
            }
        }
        return isOneWordMatch;
    }

    private static boolean matchWholeWord(List<String> compareFrom, List<String> compareTo) {
        boolean isOneWordMatch = false;
        List<String> compareToIntrenal = new ArrayList<>(compareTo);

        List<String> removedContentInternal = new ArrayList<>();

        for (String item : compareFrom) {
            int i = 0;
            while (i < compareToIntrenal.size()) {
                if (!compareToIntrenal.get(i).equals(item)) {
                    isOneWordMatch = false;
                    i++;
                } else {
                    removedContentInternal.add(compareToIntrenal.get(i));
                    compareToIntrenal.remove(i);
                    isOneWordMatch = true;
                    break;
                }
            }
            if (!isOneWordMatch) {
                break;
            }
        }
        if (!isOneWordMatch) {
            return false;
        }

        for (String item : removedContentInternal) {
            compareFrom.remove(item);
            compareTo.remove(item);
        }
        return true;
    }

    private static boolean matchSingleWord(List<String> compareFrom, List<String> compareTo) {
        boolean isOneWordMatch = false;
        List<String> compareToInternal = new ArrayList<>(compareTo);

        for (String item : compareFrom) {
            int i = 0;
            while (i < compareToInternal.size()) {
                if (!compareToInternal.get(i).startsWith(item)) {
                    isOneWordMatch = false;
                    i++;
                } else {
                    compareToInternal.remove(i);
                    isOneWordMatch = true;
                    break;
                }
            }
            if (!isOneWordMatch) {
                break;
            }
        }
        return isOneWordMatch;
    }

    public class InitialOrderRuleDataModel {
        private List<String> content;
        private List<String> contentWithMoreThenOneWord;
        private List<String> contentWithOneWord;

        public List<String> getContent() {
            return content;
        }

        public void setContent(List<String> content) {
            this.content = content;
        }

        public List<String> getContentWithMoreThenOneWord() {
            return contentWithMoreThenOneWord;
        }

        public void setContentWithMoreThenOneWord(List<String> contentWithMoreThenOneWord) {
            this.contentWithMoreThenOneWord = contentWithMoreThenOneWord;
        }

        public List<String> getContentWithOneWord() {
            return contentWithOneWord;
        }

        public void setContentWithOneWord(List<String> contentWithOneWord) {
            this.contentWithOneWord = contentWithOneWord;
        }
    }

    // Subset Name match any order
    public boolean isNameSubsetAnyOrderCheck(Map<String, String> inputName, List<String> extractedName) {
        try {
            return doWordMatchOfOneRecord(new HashMap<>(inputName),
                    extractedName.stream().distinct().collect(Collectors.toMap(Function.identity(), Function.identity())));
        } catch (Exception e) {
            throw e;
        }
    }

    private boolean doWordMatchOfOneRecord(Map<String, String> inputName, Map<String, String> extractedName) {
        boolean isMatchedClientWithWatchlist = isExactNameMatchWithAnyOrder(new HashMap<>(inputName), new HashMap<>(extractedName));
        boolean isMatchedWatchlistWithClient = false;
        if (!isMatchedClientWithWatchlist) {
            isMatchedWatchlistWithClient = isExactNameMatchWithAnyOrder(new HashMap<>(extractedName), new HashMap<>(inputName));
        }

        return isMatchedClientWithWatchlist || isMatchedWatchlistWithClient;
    }

    private boolean isExactNameMatchWithAnyOrder(Map<String, String> compareFrom, Map<String, String> compare) {
        for (Map.Entry<String, String> splitFrom : compareFrom.entrySet()) {
            if (compare.containsKey(splitFrom.getKey())) {
                compare.remove(splitFrom.getKey());
            } else {
                return false;
            }
        }
        return true;
    }

    // Vowel match
    private boolean isSimilarSoundingOrVowelMatch(Map<String, String> name1, int name1Count, String extractedName) {
        List<String> extractedSplit = removeNoiseWords(
                Pattern.compile("[aeiou]+", Pattern.CASE_INSENSITIVE)
                        .matcher(extractedName)
                        .replaceAll("")
        );

        return isExactNameMatchWithAnyOrder(name1, name1Count, extractedSplit);
    }

    // Fuzzy match
    private boolean isFuzzyMatch(char[] inputName, int noOfFuzzyCharAllowed, String extractedName) {
        if (extractedName.length() < noOfFuzzyCharAllowed) {
            return false;
        }
        char[] extractedNameChar = extractedName.toCharArray();
        double matchedPercent = getSimilarityNameMatch(inputName, extractedNameChar);
        return matchedPercent >= fuzzyNamePer;
    }

    private double getSimilarityNameMatch(char[] firstWord, char[] secondWord) {
        return getSimilarity(firstWord, secondWord);
    }

    private double getSimilarity(char[] firstWord, char[] secondWord) {
        double dist = getSimilarityMetric(firstWord, secondWord);
        int prefixLength = getPrefixLength(firstWord, secondWord);
        return dist + prefixLength * prefixAdjustmentScale * (1.0 - dist);
    }

    private int getPrefixLength(char[] firstWord, char[] secondWord) {
        int n = Math.min(minPrefixTestLength, Math.min(firstWord.length, secondWord.length));
        for (int i = 0; i < n; i++) {
            if (firstWord[i] != secondWord[i]) {
                return i;
            }
        }
        return n;
    }

    private double getSimilarityMetric(char[] firstWord, char[] secondWord) {
        char[] workBuffer = new char[WorkBufferSize];

        // Get half the length of the string rounded up - (this is the distance used for acceptable transpositions)
        int halflen = Math.min(firstWord.length, secondWord.length) / 2 + 1;

        // Get common characters
        int common1Length = getCommonCharacters(workBuffer, 0, firstWord, secondWord, halflen);
        if (common1Length == 0) {
            return defaultMismatchScore;
        }

        // Check for same length common strings returning 0.0f if not the same
        int common2Length = getCommonCharacters(workBuffer, common1Length, secondWord, firstWord, halflen);
        if (common1Length != common2Length) {
            return defaultMismatchScore;
        }

        // Get the number of transpositions
        int transpositions = 0;
        for (int i = 0; i < common1Length; i++) {
            if (workBuffer[i] != workBuffer[common1Length + i]) {
                transpositions++;
            }
        }

        // Calculate Jaro metric
        return common1Length / (3.0 * firstWord.length) + common1Length / (3.0 * secondWord.length) +
                (common1Length - (transpositions / 2.0)) / (3.0 * common1Length);
    }

    private int getCommonCharacters(char[] commonChars, int startPos, char[] firstWord, char[] secondWord, int distanceSep) {
        int usedFlags = 0;
        int writeIdx = 0;
        for (int i = 0; i < firstWord.length; i++) {
            char ch = firstWord[i];
            int max = Math.max(0, i - distanceSep);
            int min = Math.min(i + distanceSep, secondWord.length);
            for (int j = max; j < min; j++) {
                if (secondWord[j] == ch && (usedFlags & (1 << j)) == 0) {
                    commonChars[startPos + writeIdx++] = ch;
                    usedFlags |= (1 << j);
                    break;
                }
            }
        }
        return writeIdx;
    }

    public List<String> removeNoiseWords(String originalName) {
        // Remove numbers only
        originalName = originalName.replaceAll("[\\d]+", "");

        // Filter string & split
        List<String> splitedContent = Arrays.stream(originalName.toLowerCase()
                        .replace(".", " ") // Assuming you have an equivalent method
                        .trim()
                        .split("\\s+")) // Assuming you have an equivalent method for SplitByWhitespace
                .filter(x -> x.length() > 0)
                .collect(Collectors.toList());

        // Join string by '_'
        String tempString = String.join("", splitedContent.stream()
                .map(t -> "_" + t + "_")
                .collect(Collectors.toList()));

        for (String noiseWord : KycMatchCache.noiseWords) {
            if (tempString.contains(noiseWord)) {
                tempString = tempString.replace(noiseWord, "");
            }
        }

        splitedContent = Arrays.stream(tempString.split("_"))
                .filter(x -> x.length() > 0)
                .collect(Collectors.toList());

        return splitedContent;
    }

    // Additional utility method to replace accented characters
    private String replaceAccentedCharacters(String input) {
        // Implement your logic to replace accented characters here
        return input;
    }

    // Additional utility method for SplitByWhitespace (assuming equivalent logic)
    private String[] splitByWhitespace(String input) {
        // Implement your logic for splitting by whitespace here
        return input.split("\\s+");
    }
}

