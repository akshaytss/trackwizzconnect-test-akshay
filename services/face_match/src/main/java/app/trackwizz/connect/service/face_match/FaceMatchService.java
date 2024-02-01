package app.trackwizz.connect.service.face_match;

import app.trackwizz.connect.constant.AppSettingConstants;
import app.trackwizz.connect.constant.common.MessageConstants;
import app.trackwizz.connect.dto.photomatch.FaceMatchRequestDto;
import app.trackwizz.connect.dto.photomatch.FaceMatchResponseDto;
import app.trackwizz.connect.enums.RequestStatus;
import app.trackwizz.connect.model.ApiCallLog;
import app.trackwizz.connect.service.log.IApiCallDbLogService;
import app.trackwizz.connect.service.orbo.IFaceMatchExternalService;
import app.trackwizz.connect.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;

@Service
public class FaceMatchService implements IFaceMatchService {
    private final IApiCallDbLogService IApiCallDbLogService;
    private final IFaceMatchExternalService IFaceMatchExternalService;
    private final Environment environment;
    private final Logger log4j = LogManager.getLogger(FaceMatchService.class);

    public FaceMatchService(IApiCallDbLogService IApiCallDbLogService, IFaceMatchExternalService IFaceMatchExternalService, Environment environment) {
        this.IApiCallDbLogService = IApiCallDbLogService;
        this.IFaceMatchExternalService = IFaceMatchExternalService;
        this.environment = environment;
    }

    @Override
    public FaceMatchResponseDto faceMatch(FaceMatchRequestDto faceMatchRequestDto, String correlationId) {
        ApiCallLog dbApiCallLogObj = new ApiCallLog(Timestamp.from(Instant.now()));
        dbApiCallLogObj.setRequestStatus(RequestStatus.NoCall.getValue());
        dbApiCallLogObj.setApiType("face-match");
        dbApiCallLogObj.setCorrelationId(correlationId);
        dbApiCallLogObj.setEnvironment(environment.getProperty(AppSettingConstants.TWC_ENVIRONMENT_NAME));
        FaceMatchResponseDto response = new FaceMatchResponseDto();
        long startTime = System.currentTimeMillis();

        try {
//            dbApiCallLogObj.setMaskedRequestJson(JsonUtil.convertEntityToJson(FaceMatchRequestDto.getMaskedRequestObject(requestData)));
//            kycMatchHelperService.generateAPIResponseData(response, requestData, dbApiCallLogObj);
        } catch (Exception ex) {
            log4j.info("Exception while processing request correlationId: " + correlationId + "\n" + Arrays.toString(ex.getStackTrace()));
            dbApiCallLogObj.setRequestStatus(RequestStatus.Failed.getValue());
            dbApiCallLogObj.setException(ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
            response.setError(ex.getMessage());
        } finally {
            dbApiCallLogObj.setResponseTimestamp(Timestamp.from(Instant.now()));
            long endTime = System.currentTimeMillis();
            dbApiCallLogObj.setProcessingTimeMilliseconds((int) (endTime - startTime));
            IApiCallDbLogService.insertLog(dbApiCallLogObj);
            log4j.info("Api call logged to db" + MessageConstants.CORRELATION_ID + correlationId);
            log4j.info("Request processed" + MessageConstants.CORRELATION_ID + correlationId);
        }
        return response;
    }
}