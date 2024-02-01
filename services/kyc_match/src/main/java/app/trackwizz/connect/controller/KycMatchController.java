package app.trackwizz.connect.controller;

import app.trackwizz.connect.annotation.Authorize;
import app.trackwizz.connect.constant.AppSettingConstants;
import app.trackwizz.connect.constant.common.MessageConstants;
import app.trackwizz.connect.dto.ApiResponseDto;
import app.trackwizz.connect.dto.kycmatch.KycMatchRequestDto;
import app.trackwizz.connect.dto.kycmatch.KycMatchResponseDto;
import app.trackwizz.connect.enums.RequestStatus;
import app.trackwizz.connect.enums.Scope;
import app.trackwizz.connect.helper.apicall.ApiCallDbHelper;
import app.trackwizz.connect.helper.log.LogHelper;
import app.trackwizz.connect.model.ApiCallLog;
import app.trackwizz.connect.service.KycMatchService;
import app.trackwizz.connect.service.log.IApiCallDbLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class KycMatchController {
    private final Logger log4j = LogManager.getLogger(KycMatchController.class);
    private final KycMatchService kycMatchService;
    private final IApiCallDbLogService apiCallDbLogService;
    private final ApiCallDbHelper ApiCallDbHelper;
    private final Environment environment;

    public KycMatchController(KycMatchService kycMatchService, IApiCallDbLogService apiCallDbLogService, ApiCallDbHelper apiCallDbHelper, Environment environment) {
        this.kycMatchService = kycMatchService;
        this.apiCallDbLogService = apiCallDbLogService;
        ApiCallDbHelper = apiCallDbHelper;
        this.environment = environment;
    }

    @Operation(description = "Gets image match details", security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Authorize(scope = Scope.KYC_MATCH)
    public ResponseEntity<ApiResponseDto<KycMatchResponseDto>> matchFromImageViaJsonBody(@RequestHeader(required = false) String correlationId, @RequestBody KycMatchRequestDto kycMatchRequestDto, HttpServletRequest httpServletRequest) {
        return match(kycMatchRequestDto, correlationId, httpServletRequest);
    }

    private ResponseEntity<ApiResponseDto<KycMatchResponseDto>> match(KycMatchRequestDto request, String correlationId, HttpServletRequest httpServletRequest) {
        long startTime = System.currentTimeMillis();

        if (ObjectUtils.isEmpty(correlationId)) {
            correlationId = UUID.randomUUID().toString();
            log4j.info(MessageConstants.EMPTY_CORRELATION_ID_RECEIVED_NEW_ID + correlationId);
        }

        ApiCallLog apiCallLogDbObj = ApiCallDbHelper.getDefaultApiCallLog(correlationId, httpServletRequest);
        apiCallLogDbObj.setApiType(environment.getProperty(AppSettingConstants.TWC_SERVICE_NAME));

        try {
            // TODO input validation
            KycMatchResponseDto response = kycMatchService.matchData(request);
            // TODO exception handling
            return response == null || StringUtils.hasText(response.getError()) ?
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error(null, "Error while processing kyc match request", correlationId)) :
                    ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(response, correlationId));
        } catch (Exception e) {
            apiCallLogDbObj.setRequestStatus(RequestStatus.Failed.getValue());
            apiCallLogDbObj.setException(LogHelper.getExceptionDetails(e));
            log4j.error("Error while processing kyc match request" + MessageConstants.CORRELATION_ID + correlationId + LogHelper.getExceptionDetails(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error(null, "Error while processing kyc match request", correlationId));
        } finally {
            apiCallLogDbObj.setResponseTimestamp(Timestamp.from(Instant.now()));
            long endTime = System.currentTimeMillis();
            apiCallLogDbObj.setProcessingTimeMilliseconds((int) (endTime - startTime));
            apiCallDbLogService.insertLog(apiCallLogDbObj);
            log4j.info("Api call logged to db" + MessageConstants.CORRELATION_ID + correlationId);
            log4j.info("Request processed" + MessageConstants.CORRELATION_ID + correlationId);
        }
    }
}

