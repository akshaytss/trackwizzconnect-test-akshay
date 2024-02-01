package app.trackwizz.connect.helper.confidenceComponent;

import app.trackwizz.connect.constant.common.ExternalServiceConstants;
import app.trackwizz.connect.constant.common.OvdConstants;
import app.trackwizz.connect.dto.kycmatch.ConfidenceInput;
import app.trackwizz.connect.dto.kycmatch.KycMatchResponseDto;
import app.trackwizz.connect.dto.photomatch.PhotoMatchingApiResponse;
import app.trackwizz.connect.enums.ImageExtractionEnums;
import app.trackwizz.connect.helper.log.LogHelper;
import app.trackwizz.connect.util.HttpClient;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class GetPhotoConfidenceLevel {
    private WebClient webClient;
    private String endpoint;

    public GetPhotoConfidenceLevel(Environment environment) {
        this.endpoint = environment.getProperty(ExternalServiceConstants.PHOTOMATCH_ORBO_ENDPOINT_PROPERTY);
        String baseUrl = environment.getProperty(ExternalServiceConstants.PHOTOMATCH_ORBO_BASE_URL_PROPERTY);
        this.webClient = HttpClient.getWebClient(baseUrl);
    }

    public GetPhotoConfidenceLevel() {
    }

    public void getConfidenceLevel(ConfidenceInput data) {
        if (!StringUtils.hasText(data.getRequestData().getPhotoImage()))
            return;

        PhotoMatchingApiResponse aadharResponse = null;
        PhotoMatchingApiResponse eKYCAuthAadhaarResponse = null;
        PhotoMatchingApiResponse OfflineAadhaarResponse = null;
        PhotoMatchingApiResponse panResponse = null;
        PhotoMatchingApiResponse passportResponse = null;
        PhotoMatchingApiResponse dlResponse = null;
        PhotoMatchingApiResponse voterResponse = null;

        CompletableFuture<PhotoMatchingApiResponse> aadharResponseFuture = getPhotoMatchingResultAsync(data.getRequestData().getPhotoImage(), getExtractedPhoto(data.getResponseData(), OvdConstants.Aadhaar));
        CompletableFuture<PhotoMatchingApiResponse> eKYCAuthAadhaarResponseFuture = getPhotoMatchingResultAsync(data.getRequestData().getPhotoImage(), getExtractedPhoto(data.getResponseData(), OvdConstants.eKYCAuthAadhaar));
        CompletableFuture<PhotoMatchingApiResponse> OfflineAadhaarResponseFuture = getPhotoMatchingResultAsync(data.getRequestData().getPhotoImage(), getExtractedPhoto(data.getResponseData(), OvdConstants.OfflineAadhaar));
        CompletableFuture<PhotoMatchingApiResponse> panResponseFuture = getPhotoMatchingResultAsync(data.getRequestData().getPhotoImage(), getExtractedPhoto(data.getResponseData(), OvdConstants.PAN_Card));
        CompletableFuture<PhotoMatchingApiResponse> passportResponseFuture = getPhotoMatchingResultAsync(data.getRequestData().getPhotoImage(), getExtractedPhoto(data.getResponseData(), OvdConstants.Passport));
        CompletableFuture<PhotoMatchingApiResponse> dlResponseFuture = getPhotoMatchingResultAsync(data.getRequestData().getPhotoImage(), getExtractedPhoto(data.getResponseData(), OvdConstants.Driving_Licence));
        CompletableFuture<PhotoMatchingApiResponse> voterResponseFuture = getPhotoMatchingResultAsync(data.getRequestData().getPhotoImage(), getExtractedPhoto(data.getResponseData(), OvdConstants.Voter_ID_Card));

        try {
            aadharResponse = aadharResponseFuture.join();
            eKYCAuthAadhaarResponse = eKYCAuthAadhaarResponseFuture.join();
            OfflineAadhaarResponse = OfflineAadhaarResponseFuture.join();
            panResponse = panResponseFuture.join();
            passportResponse = passportResponseFuture.join();
            dlResponse = dlResponseFuture.join();
            voterResponse = voterResponseFuture.join();
        } catch (CompletionException e) {
            throw new RuntimeException(e.getCause());
        }

        data.getResponseData().setAadhaarPhotoConfidence(
                getPhotoConfidence(data.getRequestData().getPhotoImage(), OvdConstants.Aadhaar, data.getResponseData(), aadharResponse)
        );

        data.getResponseData().seteKYCAuthPhotoConfidence(
                getPhotoConfidence(data.getRequestData().getPhotoImage(), OvdConstants.eKYCAuthAadhaar, data.getResponseData(), eKYCAuthAadhaarResponse)
        );

        data.getResponseData().setOfflineAadhaarPhotoConfidence(
                getPhotoConfidence(data.getRequestData().getPhotoImage(), OvdConstants.OfflineAadhaar, data.getResponseData(), OfflineAadhaarResponse)
        );

        data.getResponseData().setPanPhotoConfidence(
                getPhotoConfidence(data.getRequestData().getPhotoImage(), OvdConstants.PAN_Card, data.getResponseData(), panResponse)
        );

        data.getResponseData().setPassportPhotoConfidence(
                getPhotoConfidence(data.getRequestData().getPhotoImage(), OvdConstants.Passport, data.getResponseData(), passportResponse)
        );

        data.getResponseData().setdLPhotoConfidence(
                getPhotoConfidence(data.getRequestData().getPhotoImage(), OvdConstants.Driving_Licence, data.getResponseData(), dlResponse)
        );

        data.getResponseData().setVoterCardPhotoConfidence(
                getPhotoConfidence(data.getRequestData().getPhotoImage(), OvdConstants.Voter_ID_Card, data.getResponseData(), voterResponse)
        );
    }

    private String getPhotoConfidence(String inputPhoto, String oVDName, KycMatchResponseDto validationResponseData, PhotoMatchingApiResponse photoMatchingApiResponse) {
        try {
            String extractedPhoto = getExtractedPhoto(validationResponseData, oVDName);
            if (!StringUtils.hasText(extractedPhoto))
                return ImageExtractionEnums.NotRead.toString();
            if (photoMatchingApiResponse.getError() != null && isFaceNotFoundError(photoMatchingApiResponse)) {
                throw new Exception("Error in " + oVDName + " Image - Photo Matching Api - Face Not Found Error");
            } else if (photoMatchingApiResponse == null || photoMatchingApiResponse.getError() != null) {
                throw new Exception("Error in " + oVDName + " Image - External Photo Matching Api Error - " + photoMatchingApiResponse.getError().getMessage());
            }
            double confidenceScore = Double.parseDouble(photoMatchingApiResponse.getData().getConfidenceScore());
            String photoConfidence = "";

            if (confidenceScore <= 100 && confidenceScore >= 85)
                photoConfidence = ImageExtractionEnums.High.toString();
            else if (confidenceScore < 85 && confidenceScore >= 50)
                photoConfidence = ImageExtractionEnums.Medium.toString();
            else if (confidenceScore > 0 && confidenceScore < 50)
                photoConfidence = ImageExtractionEnums.Low.toString();
            return photoConfidence;
        } catch (Exception ex) {
            //_log.error(" - " + ex.getMessage(), ex);
            if (validationResponseData.getError() == null)
                validationResponseData.setError("");
            validationResponseData.setError(validationResponseData.getError() + ex.getMessage());
            return ImageExtractionEnums.NotRead.toString();
        }
    }

    private String getExtractedPhoto(KycMatchResponseDto responseData, String ovdName) {
        return switch (ovdName) {
            case OvdConstants.Aadhaar -> responseData.getAadhaarImageExtractedPhoto();
            case OvdConstants.eKYCAuthAadhaar -> responseData.geteKYCAuthImageExtractedPhoto();
            case OvdConstants.OfflineAadhaar -> responseData.getOfflineAadhaarImageExtractedPhoto();
            case OvdConstants.PAN_Card -> responseData.getPanImageExtractedPhoto();
            case OvdConstants.Driving_Licence -> responseData.getdLImageExtractedPhoto();
            case OvdConstants.Passport -> responseData.getpPImageExtractedPhoto();
            case OvdConstants.Voter_ID_Card -> responseData.getVoterCardImageExtractedPhoto();
            default -> "";
        };
    }

    private PhotoMatchingApiResponse getPhotoMatchingResult(String srcImageBase64, String targetImageBase64) throws Exception {
        try {
            if (!StringUtils.hasText(targetImageBase64))
                return null;
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("src_image", srcImageBase64);
            formData.add("target_image", targetImageBase64);

            return webClient.post()
                    .uri(endpoint)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromMultipartData(formData))
                    .retrieve()
                    .bodyToMono(PhotoMatchingApiResponse.class)
                    .block();
        } catch (Exception ex) {
            throw new Exception("Error in External Photo Matching Api" + LogHelper.getExceptionDetails(ex));
        }
    }

    private CompletableFuture<PhotoMatchingApiResponse> getPhotoMatchingResultAsync(String srcImageBase64, String targetImageBase64) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!StringUtils.hasText(targetImageBase64))
                    return null;
                MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
                formData.add("src_image", srcImageBase64);
                formData.add("target_image", targetImageBase64);

                return webClient.post()
                        .uri(endpoint)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(BodyInserters.fromMultipartData(formData))
                        .retrieve()
                        .bodyToMono(PhotoMatchingApiResponse.class)
                        .block();
            } catch (Exception ex) {
                throw new CompletionException(new Exception("Error in External Photo Matching Api - \n" + ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace())));
            }
        });
    }


    private boolean isFaceNotFoundError(PhotoMatchingApiResponse response) {
        String errorMessage = response.getError().getMessage().trim().replace(" ", "").toLowerCase();
        return errorMessage.toLowerCase().contains("facenotfoundinsourceimagefile") || errorMessage.toLowerCase().contains("facenotfoundintargetimagefile");
    }
}
