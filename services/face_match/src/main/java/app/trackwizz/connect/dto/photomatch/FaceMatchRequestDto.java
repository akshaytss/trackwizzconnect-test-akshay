package app.trackwizz.connect.dto.photomatch;

public class FaceMatchRequestDto {
    private String sourceBase64;
    private String targetBase64;
    public String getSourceBase64() {
        return sourceBase64;
    }

    public void setSourceBase64(String sourceBase64) {
        this.sourceBase64 = sourceBase64;
    }

    public String getTargetBase64() {
        return targetBase64;
    }

    public void setTargetBase64(String targetBase64) {
        this.targetBase64 = targetBase64;
    }
}
