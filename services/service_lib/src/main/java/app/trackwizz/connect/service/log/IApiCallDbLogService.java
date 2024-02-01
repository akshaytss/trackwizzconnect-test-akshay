package app.trackwizz.connect.service.log;


import app.trackwizz.connect.model.ApiCallLog;

public interface IApiCallDbLogService {

    /**
     * To insert single log entry into database
     *
     * @param ApiCallLog instance of {@link ApiCallLog}
     */
    void insertLog(ApiCallLog ApiCallLog);

}
