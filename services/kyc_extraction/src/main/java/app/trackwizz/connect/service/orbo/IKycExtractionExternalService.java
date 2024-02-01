package app.trackwizz.connect.service.orbo;


import app.trackwizz.connect.dto.kyc_extraction.KycExtractionRequestDto;
import app.trackwizz.connect.dto.orbo.OrboResponseDto;
import app.trackwizz.connect.exception.KycExtractionOrboExternalServiceException;

public interface IKycExtractionExternalService {
    /**
     * Calls external service for extracting data from image
     *
     * @param KYCExtractionRequestDto requires instance of {@link KycExtractionRequestDto}
     * @return instance of {@link OrboResponseDto}
     */
    OrboResponseDto callExternalService(KycExtractionRequestDto KYCExtractionRequestDto, String correlationId) throws KycExtractionOrboExternalServiceException;
}
