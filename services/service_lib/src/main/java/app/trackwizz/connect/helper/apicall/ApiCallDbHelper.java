package app.trackwizz.connect.helper.apicall;

import app.trackwizz.connect.constant.AppSettingConstants;
import app.trackwizz.connect.enums.RequestStatus;
import app.trackwizz.connect.helper.httpcontext.IHttpContextHelper;
import app.trackwizz.connect.model.ApiCallLog;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Instant;

@Component
public class ApiCallDbHelper {
    private final Environment environment;
    private final IHttpContextHelper IHttpContextHelper;

    public ApiCallDbHelper(Environment environment, IHttpContextHelper IHttpContextHelper) {
        this.environment = environment;
        this.IHttpContextHelper = IHttpContextHelper;
    }

    public ApiCallLog getDefaultApiCallLog(String correlationId, HttpServletRequest httpServletRequest) {
        return new ApiCallLog(
                correlationId,
                IHttpContextHelper.getCallerIp(httpServletRequest),
                IHttpContextHelper.getRequestUri(httpServletRequest),
                RequestStatus.NoCall.getValue(),
                Timestamp.from(Instant.now()),
                environment.getProperty(AppSettingConstants.TWC_SERVICE_NAME),
                environment.getRequiredProperty(AppSettingConstants.TWC_ENVIRONMENT_NAME)
        );
    }
}
