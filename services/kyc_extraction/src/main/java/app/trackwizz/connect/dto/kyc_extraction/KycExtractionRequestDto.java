package app.trackwizz.connect.dto.kyc_extraction;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public class KycExtractionRequestDto {
    private String imageData;
    @Schema(hidden = true)
    private MultipartFile image;

    public KycExtractionRequestDto() {
    }

    public KycExtractionRequestDto(KycExtractionRequestDto requestObj) {
        this.imageData = requestObj.imageData;
        this.image = requestObj.image;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public static KycExtractionRequestDto getMaskedRequestObject(KycExtractionRequestDto request) {
        KycExtractionRequestDto maskedObj = new KycExtractionRequestDto(request);
        maskedObj.setImage(null);
        maskedObj.setImageData(null);
        return maskedObj;
    }
}
