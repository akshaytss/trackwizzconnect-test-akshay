package app.trackwizz.connect.service.orbo;

import app.trackwizz.connect.constant.common.ExternalServiceConstants;
import app.trackwizz.connect.dto.photomatch.FaceMatchOrboApiResponse;
import app.trackwizz.connect.util.HttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

@Service
public class FaceMatchOrboExternalService implements IFaceMatchExternalService {
    private final Logger log4j = LogManager.getLogger(FaceMatchOrboExternalService.class);
    private final WebClient webClient;
    private final String endpoint;

    public FaceMatchOrboExternalService(Environment environment) {
        this.endpoint = environment.getProperty(ExternalServiceConstants.ORBO_ENDPOINT_PROPERTY);
        String baseUrl = environment.getProperty(ExternalServiceConstants.ORBO_BASE_URL_PROPERTY);
        this.webClient = HttpClient.getWebClient(baseUrl);
    }

    @Override
    public FaceMatchOrboApiResponse getPhotoMatchingResult(String srcImageBase64, String targetImageBase64) throws Exception {
        try {
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("src_image", srcImageBase64);
            formData.add("target_image", targetImageBase64);

            FaceMatchOrboApiResponse orboJsonResponse = webClient.post()
                    .uri(endpoint)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromMultipartData(formData))
                    .retrieve()
                    .bodyToMono(FaceMatchOrboApiResponse.class)
                    .block();

            return  orboJsonResponse;
        } catch (Exception ex) {
            log4j.error("Error in External Photo Matching Api - \n" + ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
            throw new Exception("Error in External Photo Matching Api - \n" + ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
        }
    }
}
