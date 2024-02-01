package app.trackwizz.connect.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "api_call_log")
public class ApiCallLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer id;
    @Column(name = "correlation_id")
    private String correlationId;
    @Column(name = "source_user_id")
    private String sourceUserId;
    @Column(name = "source_user_ip")
    private String sourceUserIP;
    @Column(name = "request_uri")
    private String requestUri;
    @Column(nullable = false, name = "request_status")
    private Integer requestStatus;
    @Column(nullable = false, name = "request_timestamp")
    private Timestamp requestTimeStamp;
    @Column(nullable = false, name = "response_timestamp")
    private Timestamp responseTimestamp;
    @Column(nullable = false, name = "processing_time")
    private Integer processingTimeMilliseconds;
    @Type(type = "jsonb")
    @Column(name = "masked_request_json", columnDefinition = "jsonb")
    private String maskedRequestJson;
    @Type(type = "jsonb")
    @Column(name = "masked_response_json", columnDefinition = "jsonb")
    private String maskedResponseJson;
    @Column(name = "exception", columnDefinition = "TEXT")
    private String exception;
    @Column(name = "environment")
    private String environment;
    @Column(name = "api_type")
    private String apiType;

    public ApiCallLog() {
    }

    public ApiCallLog(Timestamp requestTimeStamp) {
        this.requestTimeStamp = requestTimeStamp;
    }

    public ApiCallLog(String correlationId, String sourceUserIP, String requestUri, Integer requestStatus, Timestamp requestTimeStamp, String apiType, String environment) {
        this.correlationId = correlationId;
        this.sourceUserIP = sourceUserIP;
        this.requestUri = requestUri;
        this.requestStatus = requestStatus;
        this.requestTimeStamp = requestTimeStamp;
        this.apiType = apiType;
        this.environment = environment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(String sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public String getSourceUserIP() {
        return sourceUserIP;
    }

    public void setSourceUserIP(String sourceUserIP) {
        this.sourceUserIP = sourceUserIP;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public Integer getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Integer requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Timestamp getRequestTimeStamp() {
        return requestTimeStamp;
    }

    public void setRequestTimeStamp(Timestamp requestTimeStamp) {
        this.requestTimeStamp = requestTimeStamp;
    }

    public Timestamp getResponseTimestamp() {
        return responseTimestamp;
    }

    public void setResponseTimestamp(Timestamp responseTimestamp) {
        this.responseTimestamp = responseTimestamp;
    }

    public Integer getProcessingTimeMilliseconds() {
        return processingTimeMilliseconds;
    }

    public void setProcessingTimeMilliseconds(Integer processingTimeMilliseconds) {
        this.processingTimeMilliseconds = processingTimeMilliseconds;
    }

    public String getMaskedRequestJson() {
        return maskedRequestJson;
    }

    public void setMaskedRequestJson(String maskedRequestJson) {
        this.maskedRequestJson = maskedRequestJson;
    }

    public String getMaskedResponseJson() {
        return maskedResponseJson;
    }

    public void setMaskedResponseJson(String maskedResponseJson) {
        this.maskedResponseJson = maskedResponseJson;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apitype) {
        this.apiType = apitype;
    }
}
