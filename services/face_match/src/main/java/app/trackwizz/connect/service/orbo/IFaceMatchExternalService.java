package app.trackwizz.connect.service.orbo;


import app.trackwizz.connect.dto.photomatch.FaceMatchOrboApiResponse;

public interface IFaceMatchExternalService {
    /**
     * Calls external service for extracting data from image
     *
     * @return instance of {@link FaceMatchOrboApiResponse}
     */
    FaceMatchOrboApiResponse getPhotoMatchingResult(String srcImageBase64, String targetImageBase64) throws Exception;
}
