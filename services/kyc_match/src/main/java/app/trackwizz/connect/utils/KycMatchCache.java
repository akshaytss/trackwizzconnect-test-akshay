package app.trackwizz.connect.utils;

import app.trackwizz.connect.constant.KycMatchConstant;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


public class KycMatchCache {
    public static List<String> noiseWords = null;
    public static Map<String,String> stateNameStateCodeCache = new HashMap<String, String>();
    public static void setNoiseWordsCache(){
        noiseWords = getNoiseWordFromFile(KycMatchConstant.NOISE_WORD_DATA_FILE);
    }

    private static List<String> getNoiseWordFromFile(String file) {
        // TODO fix this
        List<String> noiseWords = new ArrayList<>();
        Resource resource = new ClassPathResource(file);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;

            while ((line = br.readLine()) != null) {
                noiseWords.add(line);
            }
        } catch (Exception e) {
            //log.Error("Execption in file reading file name : "+fileName);
            throw new RuntimeException(e);
        }
        return  noiseWords;
    }

    public static void setStateNameStateCodeCache(){
        try {
            stateNameStateCodeCache = getCacheDataFromFile(KycMatchConstant.STATE_NAME_CODES_DATA_FILE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  static  void setAllKycMatchCache(){
        setStateNameStateCodeCache();
        setNoiseWordsCache();
    }

    private static Map<String,String> getCacheDataFromFile(String fileName) throws Exception {
        Map<String,String> result = new HashMap<>();
        Resource resource = new ClassPathResource(fileName);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Split the line using the pipe delimiter
                String[] parts = line.split("\\|");

                result.put(parts[0],parts[1]);

                // Add your logic to handle the data from each line
            }
        } catch (Exception e) {
            //log.Error("Execption in file reading file name : "+fileName);
            throw new RuntimeException(e);
        }
        return result;
    }


}
