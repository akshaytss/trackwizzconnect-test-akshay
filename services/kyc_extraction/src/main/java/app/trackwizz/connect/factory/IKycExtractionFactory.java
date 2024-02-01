package app.trackwizz.connect.factory;

import app.trackwizz.connect.dto.kyc_extraction.KycExtractionResponseDto;
import app.trackwizz.connect.dto.orbo.OrboResponseDto;

public interface IKycExtractionFactory {
    /**
     * Converts {@link OrboResponseDto} into {@link KycExtractionResponseDto}
     *
     * @param kycExtractionRequestId request id from the api request
     * @param orboJsonResponse       instance of {@link OrboResponseDto} from orbo service response
     * @return KYCExtractionResponseDto object
     */
    KycExtractionResponseDto createDto(String kycExtractionRequestId, OrboResponseDto orboJsonResponse);
}
