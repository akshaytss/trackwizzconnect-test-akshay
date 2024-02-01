package app.trackwizz.connect.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResponseDto<T> {
    private boolean success;
    private String message;
    private T data;
    private Object error;
    private String correlationId;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public static <T> ApiResponseDto<T> success(T data, String message, String correlationId) {
        ApiResponseDto<T> responseDto = new ApiResponseDto<>();
        responseDto.setSuccess(true);
        responseDto.setData(data);
        responseDto.setMessage(message);
        responseDto.setCorrelationId(correlationId);
        return responseDto;
    }

    public static <T> ApiResponseDto<T> success(T data, String message) {
        ApiResponseDto<T> responseDto = new ApiResponseDto<>();
        responseDto.setSuccess(true);
        responseDto.setData(data);
        responseDto.setMessage(message);
        return responseDto;
    }

    public static <T> ApiResponseDto<T> error(Object error, String message, String correlationId) {
        ApiResponseDto<T> responseDto = new ApiResponseDto<>();
        responseDto.setSuccess(false);
        responseDto.setError(error);
        responseDto.setMessage(message);
        responseDto.setCorrelationId(correlationId);
        return responseDto;
    }

    public static <T> ApiResponseDto<T> error(Object error, String message) {
        ApiResponseDto<T> responseDto = new ApiResponseDto<>();
        responseDto.setSuccess(false);
        responseDto.setError(error);
        responseDto.setMessage(message);
        return responseDto;
    }
}
