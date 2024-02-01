package app.trackwizz.connect.service.kyc_extraction;

import app.trackwizz.connect.constant.common.MessageConstants;
import app.trackwizz.connect.dto.kyc_extraction.KycExtractionRequestDto;
import app.trackwizz.connect.dto.kyc_extraction.KycExtractionResponseDto;
import app.trackwizz.connect.dto.orbo.OrboResponseDto;
import app.trackwizz.connect.exception.InvalidBase64Exception;
import app.trackwizz.connect.exception.KycExtractionOrboExternalServiceException;
import app.trackwizz.connect.exception.KycExtractionServiceException;
import app.trackwizz.connect.factory.IKycExtractionFactory;
import app.trackwizz.connect.factory.KycExtractionFactory;
import app.trackwizz.connect.helper.log.LogHelper;
import app.trackwizz.connect.service.orbo.IKycExtractionExternalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class KycExtractionService implements IKycExtractionService {
    private final IKycExtractionFactory IKYCExtractionFactory;
    private final IKycExtractionExternalService IKYCExtractionExternalService;
    private final Logger log4j = LogManager.getLogger(KycExtractionService.class);

    public KycExtractionService(IKycExtractionFactory IKYCExtractionFactory, IKycExtractionExternalService IKYCExtractionExternalService) {
        this.IKYCExtractionFactory = IKYCExtractionFactory;
        this.IKYCExtractionExternalService = IKYCExtractionExternalService;
    }

    @Override
    public KycExtractionResponseDto imageExtract(KycExtractionRequestDto kycExtractionRequestDto, String correlationId) throws InvalidBase64Exception, KycExtractionServiceException {
        if (ObjectUtils.isEmpty(kycExtractionRequestDto.getImageData()) && ObjectUtils.isEmpty(kycExtractionRequestDto.getImage()))
            throw new KycExtractionServiceException("Invalid input. Please provide either image or image_base64 as param");

        if (!KycExtractionFactory.isValidBase64String(kycExtractionRequestDto.getImageData()))
            throw new InvalidBase64Exception("Invalid input image base64 string");

        // External Service call and createDto
        OrboResponseDto orboJsonResponse = null;
        try {
            orboJsonResponse = IKYCExtractionExternalService.callExternalService(kycExtractionRequestDto, correlationId);
        } catch (KycExtractionOrboExternalServiceException e) {
            throw new KycExtractionServiceException("Exception while calling ORBO service " + LogHelper.getExceptionDetails(e));
        }

        if (orboJsonResponse == null)
            throw new KycExtractionServiceException("ORBO Service returned null response");

        KycExtractionResponseDto kycExtractionResponseDto = IKYCExtractionFactory.createDto(correlationId, orboJsonResponse);
        log4j.info("ORBO response successfully mapped to KYCExtractionResponseDto" + MessageConstants.CORRELATION_ID + correlationId);

        return kycExtractionResponseDto;
    }
}
