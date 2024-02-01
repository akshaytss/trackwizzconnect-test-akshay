package app.trackwizz.connect.enums;

public enum RequestStatus {
    NoCall(0, "Service not called"),
    Success(1, "Service called successfully"),
    Failed(2, "Service called failed");

    private final String msg;
    private final int value;

    RequestStatus(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public int getValue() {
        return value;
    }
}
