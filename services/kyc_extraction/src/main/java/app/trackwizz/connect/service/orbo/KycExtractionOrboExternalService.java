package app.trackwizz.connect.service.orbo;

import app.trackwizz.connect.constant.common.CommonConstants;
import app.trackwizz.connect.constant.common.ExternalServiceConstants;
import app.trackwizz.connect.constant.common.MessageConstants;
import app.trackwizz.connect.dto.kyc_extraction.KycExtractionRequestDto;
import app.trackwizz.connect.dto.orbo.OrboResponseDto;
import app.trackwizz.connect.exception.KycExtractionOrboExternalServiceException;
import app.trackwizz.connect.helper.log.LogHelper;
import app.trackwizz.connect.util.HttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KycExtractionOrboExternalService implements IKycExtractionExternalService {
    private final Logger log4j = LogManager.getLogger(KycExtractionOrboExternalService.class);
    private final WebClient webClient;
    private final String endpoint;

    public KycExtractionOrboExternalService(Environment environment) {
        this.endpoint = environment.getProperty(ExternalServiceConstants.ORBO_ENDPOINT_PROPERTY);
        String baseUrl = environment.getProperty(ExternalServiceConstants.ORBO_BASE_URL_PROPERTY);
        this.webClient = HttpClient.getWebClient(baseUrl);
    }

    @Override
    public OrboResponseDto callExternalService(KycExtractionRequestDto kycExtractionRequestDto, String correlationId) throws KycExtractionOrboExternalServiceException {
        log4j.info("Request received for ORBO external service call" + MessageConstants.CORRELATION_ID + correlationId);
        try {

            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add(CommonConstants.IMAGE_BASE64, kycExtractionRequestDto.getImageData());

            if (!ObjectUtils.isEmpty(kycExtractionRequestDto.getImage())) {
                formData.add(CommonConstants.IMAGE, kycExtractionRequestDto.getImage());
            }

            OrboResponseDto orboJsonResponse = webClient.post()
                    .uri(endpoint)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromMultipartData(formData))
                    .retrieve()
                    .bodyToMono(OrboResponseDto.class)
                    .block();

            // ORBO empty response check
            if (orboJsonResponse == null || orboJsonResponse.getData().isEmpty()) {
                log4j.info("ORBO external service call returned empty response" + MessageConstants.CORRELATION_ID + correlationId);
                throw new KycExtractionOrboExternalServiceException("Empty response from ORBO service");
            }

            log4j.info("Request processed for ORBO external service call" + MessageConstants.CORRELATION_ID + correlationId);
            return orboJsonResponse;
        } catch (Exception e) {
            log4j.info("ORBO external service call Exception" + MessageConstants.CORRELATION_ID + correlationId + LogHelper.getExceptionDetails(e));
            throw new KycExtractionOrboExternalServiceException("Error while calling ORBO service" + LogHelper.getExceptionDetails(e));
        }
    }
}
