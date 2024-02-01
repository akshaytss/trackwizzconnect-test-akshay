package app.trackwizz.connect.service.kyc_extraction;

import app.trackwizz.connect.dto.kyc_extraction.KycExtractionRequestDto;
import app.trackwizz.connect.dto.kyc_extraction.KycExtractionResponseDto;
import app.trackwizz.connect.exception.InvalidBase64Exception;
import app.trackwizz.connect.exception.KycExtractionServiceException;

public interface IKycExtractionService {
    /**
     * To call external api to read given image
     *
     * @param KYCExtractionRequestDto instance of {@link KycExtractionRequestDto}
     * @return instance of {@link KycExtractionResponseDto}
     */
    KycExtractionResponseDto imageExtract(KycExtractionRequestDto KYCExtractionRequestDto, String correlationId) throws InvalidBase64Exception, KycExtractionServiceException;
}
