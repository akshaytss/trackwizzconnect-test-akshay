package app.trackwizz.connect.service.log;

import app.trackwizz.connect.model.ApiCallLog;
import app.trackwizz.connect.repository.ApiCallLogRepository;
import org.springframework.stereotype.Service;

@Service
public class ApiCallDbLogService implements IApiCallDbLogService {
    private final ApiCallLogRepository apiCallLogRepository;

    public ApiCallDbLogService(ApiCallLogRepository apiCallLogRepository) {
        this.apiCallLogRepository = apiCallLogRepository;
    }

    public void insertLog(ApiCallLog apiCallLog) {
        apiCallLogRepository.save(apiCallLog);
    }
}
