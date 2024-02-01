package app.trackwizz.connect.controller;

import app.trackwizz.connect.annotation.Authorize;
import app.trackwizz.connect.constant.AppSettingConstants;
import app.trackwizz.connect.constant.KycExtractionMessageConstants;
import app.trackwizz.connect.constant.common.MessageConstants;
import app.trackwizz.connect.dto.ApiResponseDto;
import app.trackwizz.connect.dto.kyc_extraction.KycExtractionRequestDto;
import app.trackwizz.connect.dto.kyc_extraction.KycExtractionResponseDto;
import app.trackwizz.connect.enums.RequestStatus;
import app.trackwizz.connect.enums.Scope;
import app.trackwizz.connect.exception.InvalidBase64Exception;
import app.trackwizz.connect.helper.apicall.ApiCallDbHelper;
import app.trackwizz.connect.helper.log.LogHelper;
import app.trackwizz.connect.model.ApiCallLog;
import app.trackwizz.connect.service.kyc_extraction.IKycExtractionService;
import app.trackwizz.connect.service.log.IApiCallDbLogService;
import app.trackwizz.connect.util.JsonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class KycExtractionController {
    private final Logger log4j = LogManager.getLogger(KycExtractionController.class);
    private final IKycExtractionService IKycExtractionService;
    private final IApiCallDbLogService IApiCallDbLogService;
    private final ApiCallDbHelper ApiCallDbHelper;
    private final Environment environment;

    public KycExtractionController(IKycExtractionService IKycExtractionService, IApiCallDbLogService IApiCallDbLogService, ApiCallDbHelper ApiCallDbHelper, Environment environment) {
        this.IKycExtractionService = IKycExtractionService;
        this.IApiCallDbLogService = IApiCallDbLogService;
        this.ApiCallDbHelper = ApiCallDbHelper;
        this.environment = environment;
    }

    @Operation(description = "Gets image details", security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Authorize(scope = Scope.KYC_EXTRACTION)
    public ResponseEntity<ApiResponseDto<KycExtractionResponseDto>> extractFromImageViaFormData(@RequestHeader(required = false) String correlationId, @ModelAttribute KycExtractionRequestDto kycextractionrequestdto, HttpServletRequest httpServletRequest) {
        return getKycExtractionResponseDtoResponseEntity(correlationId, kycextractionrequestdto, httpServletRequest);
    }

    @Operation(description = "Gets image details", security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Authorize(scope = Scope.KYC_EXTRACTION)
    public ResponseEntity<ApiResponseDto<KycExtractionResponseDto>> extractFromImageViaJsonBody(@RequestHeader(required = false) String correlationId, @RequestBody KycExtractionRequestDto kycextractionrequestdto, HttpServletRequest httpServletRequest) {
        return getKycExtractionResponseDtoResponseEntity(correlationId, kycextractionrequestdto, httpServletRequest);
    }

    private ResponseEntity<ApiResponseDto<KycExtractionResponseDto>> getKycExtractionResponseDtoResponseEntity(String correlationId, KycExtractionRequestDto kycExtractionRequestDto, HttpServletRequest httpServletRequest) {
        long startTime = System.currentTimeMillis();

        if (ObjectUtils.isEmpty(correlationId)) {
            correlationId = UUID.randomUUID().toString();
            log4j.info(MessageConstants.EMPTY_CORRELATION_ID_RECEIVED_NEW_ID + correlationId);
        }

        ApiCallLog apiCallLogDbObj = ApiCallDbHelper.getDefaultApiCallLog(correlationId, httpServletRequest);
        apiCallLogDbObj.setApiType(environment.getProperty(AppSettingConstants.TWC_SERVICE_NAME));

        try {
            apiCallLogDbObj.setMaskedRequestJson(JsonUtil.convertEntityToJson(KycExtractionRequestDto.getMaskedRequestObject(kycExtractionRequestDto)));

            KycExtractionResponseDto responseDto = IKycExtractionService.imageExtract(kycExtractionRequestDto, correlationId);
            if (responseDto == null) {
                log4j.error(KycExtractionMessageConstants.UNEXPECTED_ERROR_OCCURRED_DURING_THE_DATA_EXTRACTION_PROCESS + MessageConstants.CORRELATION_ID + correlationId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(null, KycExtractionMessageConstants.UNEXPECTED_ERROR_OCCURRED_DURING_THE_DATA_EXTRACTION_PROCESS, correlationId));
            }
            log4j.info(KycExtractionMessageConstants.DOCUMENT_EXTRACTED_SUCCESSFULLY + MessageConstants.CORRELATION_ID + correlationId);

            apiCallLogDbObj.setRequestStatus(RequestStatus.Success.getValue());
            apiCallLogDbObj.setMaskedResponseJson(JsonUtil.convertEntityToJson(KycExtractionResponseDto.getMaskedResponseObject(responseDto)));

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(responseDto, KycExtractionMessageConstants.DOCUMENT_EXTRACTED_SUCCESSFULLY, correlationId));
        } catch (InvalidBase64Exception e) {
            apiCallLogDbObj.setRequestStatus(RequestStatus.Failed.getValue());
            apiCallLogDbObj.setException(LogHelper.getExceptionDetails(e));
            log4j.info(KycExtractionMessageConstants.OVD_PROVIDED_IS_NOT_BASE_64_FORMAT_OR_IS_CORRUPTED + MessageConstants.CORRELATION_ID + correlationId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(null, KycExtractionMessageConstants.OVD_PROVIDED_IS_NOT_BASE_64_FORMAT_OR_IS_CORRUPTED, correlationId));
        } catch (Exception e) {
            apiCallLogDbObj.setRequestStatus(RequestStatus.Failed.getValue());
            apiCallLogDbObj.setException(LogHelper.getExceptionDetails(e));
            log4j.error(KycExtractionMessageConstants.UNEXPECTED_ERROR_OCCURRED_DURING_THE_DATA_EXTRACTION_PROCESS + MessageConstants.CORRELATION_ID + correlationId + LogHelper.getExceptionDetails(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error(null, KycExtractionMessageConstants.UNEXPECTED_ERROR_OCCURRED_DURING_THE_DATA_EXTRACTION_PROCESS, correlationId));
        } finally {
            apiCallLogDbObj.setResponseTimestamp(Timestamp.from(Instant.now()));
            long endTime = System.currentTimeMillis();
            apiCallLogDbObj.setProcessingTimeMilliseconds((int) (endTime - startTime));
            IApiCallDbLogService.insertLog(apiCallLogDbObj);
            log4j.info("Api call logged to db" + MessageConstants.CORRELATION_ID + correlationId);
            log4j.info("Request processed" + MessageConstants.CORRELATION_ID + correlationId);
        }
    }
}
