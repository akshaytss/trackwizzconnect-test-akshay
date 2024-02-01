package app.trackwizz.connect.service;

import app.trackwizz.connect.constant.common.ExternalServiceConstants;
import app.trackwizz.connect.constant.common.OvdConstants;
import app.trackwizz.connect.dto.kycmatch.ConfidenceInput;
import app.trackwizz.connect.dto.kycmatch.KycMatchRequestDto;
import app.trackwizz.connect.dto.kycmatch.KycMatchResponseDto;
import app.trackwizz.connect.dto.orbo.ORBOKYCExtractionResponseDto;
import app.trackwizz.connect.dto.orbo.ORBOResponseDto;
import app.trackwizz.connect.exception.KycMatchException;
import app.trackwizz.connect.helper.confidenceComponent.*;
import app.trackwizz.connect.helper.log.LogHelper;
import app.trackwizz.connect.helper.massager.*;
import app.trackwizz.connect.util.HttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class KycMatchHelperService implements IKycMatchHelperService {

    private final Logger log4j = LogManager.getLogger(KycMatchHelperService.class);
    private final WebClient webClient;
    private final String endpoint;
    private final Environment environment;

    public KycMatchHelperService(Environment environment) {
        this.environment = environment;
        this.endpoint = environment.getProperty(ExternalServiceConstants.ORBO_ENDPOINT_PROPERTY);
        String baseUrl = environment.getProperty(ExternalServiceConstants.ORBO_BASE_URL_PROPERTY);
        this.webClient = HttpClient.getWebClient(baseUrl);
    }

    @Override
    public void generateAPIResponseData(KycMatchResponseDto responseData, KycMatchRequestDto requestData) {
        try {
            log4j.info(" - Getting Image Input List from Request Data ");
            Map<String, String> requiredImageInput = getRequiredImageInput(requestData, responseData);
            log4j.info(" - Done Getting Image Input List from Request Data ");

            log4j.info(" - Getting Response From External Api");
            Map<String, ORBOResponseDto> responseList = getResponseFromExternalApi(requiredImageInput);
            if (responseList.isEmpty()) throw new Exception("Response From External Api is Null");

            log4j.info(" - Done Getting Response From External Api");

            log4j.info(" - Populating ResponseData");
            populateResponseData(responseList, responseData);
            log4j.info(" - Done Populating ResponseData");

            log4j.info(" - Preparing ResponseData");
            prepareResponseData(responseData);
            log4j.info(" - Done Preparing ResponseData");

            log4j.info(" - Preparing Confidence Component for ResponseData");
            prepareConfidenceComponents(responseData, requestData);
            log4j.info(" - Done Preparing Confidence Component for ResponseData");

            log4j.info(" - Created ResponseData Successfully");
        } catch (KycMatchException ex) {
            responseData.setError(ex.getMessage());
            log4j.error(" - Error : " + LogHelper.getExceptionDetails(ex));
            throw new RuntimeException(ex);
        } catch (Exception ex) {
            log4j.error(" - Error occurred in preparing ResponseData : " + LogHelper.getExceptionDetails(ex));
            throw new RuntimeException(ex);
        }
    }

    private Map<String, ORBOResponseDto> getResponseFromExternalApi(Map<String, String> requiredImageInput) throws KycMatchException {
        long startTime = 0;

        Map<String, ORBOResponseDto> jsonResponseList = new HashMap<>();
        try {
            List<CompletableFuture<Map.Entry<String, ORBOResponseDto>>> futures = requiredImageInput.entrySet().stream().map(entry -> extractionApiCall(entry.getKey(), entry.getValue())).toList();

            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allOf.join();

            for (CompletableFuture<Map.Entry<String, ORBOResponseDto>> future : futures) {
                Map.Entry<String, ORBOResponseDto> entry = future.join();
                jsonResponseList.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception ex) {
            log4j.error(" - Error occurred in Getting Response from ImageExtraction External Api :" + LogHelper.getExceptionDetails(ex));
            throw new KycMatchException(ex.getMessage());
        }

        return jsonResponseList;
    }

    private CompletableFuture<Map.Entry<String, ORBOResponseDto>> extractionApiCall(String ovdImageName, String image) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log4j.info(" - Calling External Api for OVD - " + ovdImageName);
                ORBOResponseDto jsonResponse = apiCall(image);

                return Map.entry(ovdImageName, jsonResponse);
            } catch (Exception ex) {
                log4j.error(" - Error from external api for OVD - " + ovdImageName + " " + LogHelper.getExceptionDetails(ex));
                throw new RuntimeException(ex);
            }
        });
    }


    public ORBOResponseDto apiCall(String image) {


        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("image_base64", image);

        ORBOResponseDto response = webClient.post().uri(endpoint).contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters.fromMultipartData(formData)).retrieve().bodyToMono(ORBOResponseDto.class).block();

        if (response != null && ((response.getError() != null && response.getError().getMessage() != null) || response.getData().isEmpty())) {
            formData.add("doc_type", "aadhar");

            response = webClient.post().uri(endpoint).contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters.fromMultipartData(formData)).retrieve().bodyToMono(ORBOResponseDto.class).block();
        }
        return response;
    }

    public ORBOResponseDto apiCall_Aadhar(String image) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("image_base64", image);
        formData.add("doc_type", "aadhar");

        return webClient.post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .bodyToMono(ORBOResponseDto.class)
                .block();
    }

    private MultiValueMap<String, Object> buildMultipartData(String image, String docType) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("image_base64", image);

        return formData;
    }

    public Map<String, String> getRequiredImageInput(KycMatchRequestDto requestData, KycMatchResponseDto responseData) throws KycMatchException {
        Map<String, String> imageInputList = new HashMap<>();
        responseData.setAcceptedOVDAvailable("");

        if (requestData != null) {
            if (hasContent(requestData.getAadhaarImage())) {
                imageInputList.put("AadhaarImage", requestData.getAadhaarImage());
            }
            if (hasContent(requestData.geteKYCAuthImage())) {
                imageInputList.put("eKYCAuthAadhaarImage", requestData.geteKYCAuthImage());
            }
            if (hasContent(requestData.getOfflineAadharImage())) {
                imageInputList.put("OfflineAadhaarImage", requestData.getOfflineAadharImage());
            }
            if (hasContent(requestData.getPanImage())) {
                imageInputList.put("PanImage", requestData.getPanImage());
            }
            if (hasContent(requestData.getPassportImage())) {
                imageInputList.put(OvdConstants.PassportImage, requestData.getPassportImage());
            }
            if (hasContent(requestData.getVoterCardImage())) {
                imageInputList.put("VoterCardImage", requestData.getVoterCardImage());
            }
            if (hasContent(requestData.getdLImage())) {
                imageInputList.put("dLImage", requestData.getdLImage());
            }
        }

        if (!imageInputList.isEmpty()) {
            responseData.setAcceptedOVDAvailable("Yes");
        } else {
            responseData.setAcceptedOVDAvailable("No");
            throw new KycMatchException("No input image OVD found in Input Request Data.");
        }

        return imageInputList;
    }

    private void populateResponseData(Map<String, ORBOResponseDto> jsonResponseList, KycMatchResponseDto validationResponseData) {
        try {
            for (Map.Entry<String, ORBOResponseDto> responseData : jsonResponseList.entrySet()) {

                prepareOVDDataResponse(validationResponseData, responseData.getValue(), responseData.getKey());
                //System.out.printf(" - Done Populating %s image response%n", responseData.getKey());
            }
        } catch (Exception ex) {
            // TODO fix
//            System.err.println(" - Error occurred in Populating Response Data : " + ex.getMessage());
//            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    private void prepareOVDDataResponse(KycMatchResponseDto validationResponseData, ORBOResponseDto ovdDataList, String currentOVDInputName) {
        // TODO fix
        //System.out.printf(" - Preparing OVD Data response for %s image response%n", currentOVDInputName);

        for (ORBOKYCExtractionResponseDto ovdData : ovdDataList.getData()) {

            String responseOVDClass = ovdData.getClassValue();
            int priority = (currentOVDInputName.equals(responseOVDClass)) ? 1 : 0;

            switch (responseOVDClass) {
                case OvdConstants.Aadhar:
                    validationResponseData.setAadhaarImageType(fillDataBasedOnPriority(validationResponseData.getAadhaarImageType(), ovdData.getSubClass(), priority));
                    validationResponseData.setAadhaarImageExtractedName(fillDataBasedOnPriority(validationResponseData.getAadhaarImageExtractedName(), ovdData.getExtracted().getName(), priority));
                    validationResponseData.setAadhaarImageExtractedFatherName(fillDataBasedOnPriority(validationResponseData.getAadhaarImageExtractedFatherName(), ovdData.getExtracted().getRelativeName(), priority));
                    validationResponseData.setAadhaarImageExtractedDOB(fillDataBasedOnPriority(validationResponseData.getAadhaarImageExtractedDOB(), ovdData.getExtracted().getDateOfBirth(), priority));
                    validationResponseData.setAadhaarImageExtractedGender(fillDataBasedOnPriority(validationResponseData.getAadhaarImageExtractedGender(), ovdData.getExtracted().getGender(), priority));
                    validationResponseData.setAadhaarImageExtractedAddress(fillDataBasedOnPriority(validationResponseData.getAadhaarImageExtractedAddress(), ovdData.getExtracted().getAddress(), priority));
                    validationResponseData.setAadhaarImageExtractedNumber(fillDataBasedOnPriority(validationResponseData.getAadhaarImageExtractedNumber(), ovdData.getExtracted().getAadharID(), priority));
                    validationResponseData.setAadhaarImageExtractedPhoto(fillDataBasedOnPriority(validationResponseData.getAadhaarImageExtractedPhoto(), ovdData.getPhoto(), priority));
                    validationResponseData.setAadhaarMaskedImage(fillDataBasedOnPriority(validationResponseData.getAadhaarMaskedImage(), ovdData.getMaskedAadhar(), priority));
                    validationResponseData.setAadhaarCroppedImage(fillDataBasedOnPriority(validationResponseData.getAadhaarCroppedImage(), ovdData.getCroppedImage(), priority));
                    validationResponseData.setAadhaarImageExtractedMobileNo(fillDataBasedOnPriority(validationResponseData.getAadhaarImageExtractedMobileNo(), ovdData.getExtracted().getMobileNo(), priority));
                    validationResponseData.setAadhaarImageExtractedEmailID(fillDataBasedOnPriority(validationResponseData.getAadhaarImageExtractedEmailID(), ovdData.getExtracted().getEmailId(), priority));

                    break;

                case OvdConstants.eKYC:
                    validationResponseData.seteKYCAuthImageType(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageType(), ovdData.getSubClass(), priority));
                    validationResponseData.seteKYCAuthImageExtractedName(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedName(), ovdData.getExtracted().getName(), priority));
                    validationResponseData.seteKYCAuthImageExtractedFatherName(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedFatherName(), ovdData.getExtracted().getRelativeName(), priority));
                    validationResponseData.seteKYCAuthImageExtractedDOB(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedDOB(), ovdData.getExtracted().getDateOfBirth(), priority));
                    validationResponseData.seteKYCAuthImageExtractedGender(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedGender(), ovdData.getExtracted().getGender(), priority));
                    validationResponseData.seteKYCAuthImageExtractedAddress(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedAddress(), ovdData.getExtracted().getAddress(), priority));
                    validationResponseData.seteKYCAuthImageExtractedHouseNo(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedHouseNo(), ovdData.getExtracted().getHouseNo(), priority));
                    validationResponseData.seteKYCAuthImageExtractedLandmark(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedLandmark(), ovdData.getExtracted().getLandmark(), priority));
                    validationResponseData.seteKYCAuthImageExtractedLocality(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedLocality(), ovdData.getExtracted().getLocality(), priority));
                    validationResponseData.seteKYCAuthImageExtractedVTC(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedVTC(), ovdData.getExtracted().getVTC(), priority));
                    validationResponseData.seteKYCAuthImageExtractedSubdistrict(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedSubdistrict(), ovdData.getExtracted().getSubDistrict(), priority));
                    validationResponseData.seteKYCAuthImageExtractedDistrict(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedDistrict(), ovdData.getExtracted().getDistrict(), priority));
                    validationResponseData.seteKYCAuthImageExtractedState(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedState(), ovdData.getExtracted().getState(), priority));
                    validationResponseData.seteKYCAuthImageExtractedCountry(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedCountry(), ovdData.getExtracted().getCountry(), priority));
                    validationResponseData.seteKYCAuthImageExtractedPostOffice(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedPostOffice(), ovdData.getExtracted().getPostOffice(), priority));
                    validationResponseData.seteKYCAuthImageExtractedZipCode(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedZipCode(), ovdData.getExtracted().getPinCode(), priority));
                    validationResponseData.seteKYCAuthImageExtractedNumber(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedNumber(), ovdData.getExtracted().getAadharID(), priority));
                    validationResponseData.seteKYCAuthImageExtractedPhoto(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedPhoto(), ovdData.getPhoto(), priority));
                    validationResponseData.seteKYCAuthCroppedImage(fillDataBasedOnPriority(validationResponseData.geteKYCAuthCroppedImage(), ovdData.getCroppedImage(), priority));
                    validationResponseData.seteKYCAuthImageExtractedMobileNo(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedMobileNo(), ovdData.getExtracted().getMobileNo(), priority));
                    validationResponseData.seteKYCAuthImageExtractedEmailID(fillDataBasedOnPriority(validationResponseData.geteKYCAuthImageExtractedEmailID(), ovdData.getExtracted().getEmailId(), priority));
                    break;

                case OvdConstants.OfflineAadhaar:
                    validationResponseData.setOfflineAadhaarImageType(fillDataBasedOnPriority(validationResponseData.getOfflineAadhaarImageType(), ovdData.getSubClass(), priority));
                    validationResponseData.setOfflineAadhaarImageExtractedName(fillDataBasedOnPriority(validationResponseData.getOfflineAadhaarImageExtractedName(), ovdData.getExtracted().getName(), priority));
                    validationResponseData.setOfflineAadhaarImageExtractedFatherName(fillDataBasedOnPriority(validationResponseData.getOfflineAadhaarImageExtractedFatherName(), ovdData.getExtracted().getRelativeName(), priority));
                    validationResponseData.setOfflineAadhaarImageExtractedDOB(fillDataBasedOnPriority(validationResponseData.getOfflineAadhaarImageExtractedDOB(), ovdData.getExtracted().getDateOfBirth(), priority));
                    validationResponseData.setOfflineAadhaarImageExtractedGender(fillDataBasedOnPriority(validationResponseData.getOfflineAadhaarImageExtractedGender(), ovdData.getExtracted().getGender(), priority));
                    validationResponseData.setOfflineAadhaarImageExtractedAddress(fillDataBasedOnPriority(validationResponseData.getOfflineAadhaarImageExtractedAddress(), ovdData.getExtracted().getAddress(), priority));
                    validationResponseData.setOfflineAadhaarImageExtractedNumber(fillDataBasedOnPriority(validationResponseData.getOfflineAadhaarImageExtractedNumber(), ovdData.getExtracted().getAadharID(), priority));
                    validationResponseData.setOfflineAadhaarImageExtractedPhoto(fillDataBasedOnPriority(validationResponseData.getOfflineAadhaarImageExtractedPhoto(), ovdData.getPhoto(), priority));
                    validationResponseData.setOfflineAadhaarCroppedImage(fillDataBasedOnPriority(validationResponseData.getOfflineAadhaarCroppedImage(), ovdData.getCroppedImage(), priority));
                    validationResponseData.setOfflineAadhaarImageExtractedMobileNo(fillDataBasedOnPriority(validationResponseData.getOfflineAadhaarImageExtractedMobileNo(), ovdData.getExtracted().getMobileNo(), priority));
                    validationResponseData.setOfflineAadhaarImageExtractedEmailID(fillDataBasedOnPriority(validationResponseData.getOfflineAadhaarImageExtractedEmailID(), ovdData.getExtracted().getEmailId(), priority));
                    break;

                case OvdConstants.PAN_Card:
                    validationResponseData.setPanImageExtractedName(fillDataBasedOnPriority(validationResponseData.getPanImageExtractedName(), ovdData.getExtracted().getName(), priority));
                    validationResponseData.setPanImageExtractedFatherName(fillDataBasedOnPriority(validationResponseData.getPanImageExtractedFatherName(), ovdData.getExtracted().getRelativeName(), priority));
                    validationResponseData.setPanImageExtractedDOB(fillDataBasedOnPriority(validationResponseData.getPanImageExtractedDOB(), ovdData.getExtracted().getDateOfBirth(), priority));
                    validationResponseData.setPanImageExtractedNumber(fillDataBasedOnPriority(validationResponseData.getPanImageExtractedNumber(), ovdData.getExtracted().getPANNo(), priority));
                    validationResponseData.setPanImageExtractedPhoto(fillDataBasedOnPriority(validationResponseData.getPanImageExtractedPhoto(), ovdData.getPhoto(), priority));
                    validationResponseData.setPanCroppedImage(fillDataBasedOnPriority(validationResponseData.getPanCroppedImage(), ovdData.getCroppedImage(), priority));
                    break;

                case OvdConstants.Driving_Licence:
                    validationResponseData.setdLImageExtractedName(fillDataBasedOnPriority(validationResponseData.getdLImageExtractedName(), ovdData.getExtracted().getName(), priority));
                    validationResponseData.setdLImageExtractedFatherName(fillDataBasedOnPriority(validationResponseData.getdLImageExtractedFatherName(), ovdData.getExtracted().getRelativeName(), priority));
                    validationResponseData.setdLImageExtractedDOB(fillDataBasedOnPriority(validationResponseData.getdLImageExtractedDOB(), ovdData.getExtracted().getDateOfBirth(), priority));
                    validationResponseData.setdLImageExtractedAddress(fillDataBasedOnPriority(validationResponseData.getdLImageExtractedAddress(), ovdData.getExtracted().getAddress(), priority));
                    validationResponseData.setdLImageExtractedNumber(fillDataBasedOnPriority(validationResponseData.getdLImageExtractedNumber(), ovdData.getExtracted().getDrivingLicenseNo(), priority));
                    validationResponseData.setdLImageExtractedPhoto(fillDataBasedOnPriority(validationResponseData.getdLImageExtractedPhoto(), ovdData.getPhoto(), priority));
                    validationResponseData.setdLExpiryDate(fillDataBasedOnPriority(validationResponseData.getdLExpiryDate(), ovdData.getExtracted().getDateOfExpiry(), priority));
                    validationResponseData.setdLCroppedImage(fillDataBasedOnPriority(validationResponseData.getdLCroppedImage(), ovdData.getCroppedImage(), priority));
                    break;

                case OvdConstants.Passport:
                case OvdConstants.PassportImage:
                    validationResponseData.setpPImageExtractedName(fillDataBasedOnPriority(validationResponseData.getpPImageExtractedName(), ovdData.getExtracted().getName(), priority));
                    validationResponseData.setpPImageExtractedFatherName(fillDataBasedOnPriority(validationResponseData.getpPImageExtractedFatherName(), ovdData.getExtracted().getRelativeName(), priority));
                    validationResponseData.setpPImageExtractedDOB(fillDataBasedOnPriority(validationResponseData.getpPImageExtractedDOB(), ovdData.getExtracted().getDateOfBirth(), priority));
                    validationResponseData.setpPImageExtractedAddress(fillDataBasedOnPriority(validationResponseData.getpPImageExtractedAddress(), ovdData.getExtracted().getAddress(), priority));
                    validationResponseData.setpPImageExtractedNumber(fillDataBasedOnPriority(validationResponseData.getpPImageExtractedNumber(), ovdData.getExtracted().getPassportID(), priority));
                    validationResponseData.setpPImageExtractedPhoto(fillDataBasedOnPriority(validationResponseData.getpPImageExtractedPhoto(), ovdData.getPhoto(), priority));
                    validationResponseData.setPassportExpiryDate(fillDataBasedOnPriority(validationResponseData.getPassportExpiryDate(), ovdData.getExtracted().getDateOfExpiry(), priority));
                    validationResponseData.setpPCroppedImage(fillDataBasedOnPriority(validationResponseData.getpPCroppedImage(), ovdData.getCroppedImage(), priority));
                    break;

                case OvdConstants.Voter_ID_Card:
                    validationResponseData.setVoterCardImageExtractedName(fillDataBasedOnPriority(validationResponseData.getVoterCardImageExtractedName(), ovdData.getExtracted().getName(), priority));
                    validationResponseData.setVoterCardImageExtractedFatherName(fillDataBasedOnPriority(validationResponseData.getVoterCardImageExtractedFatherName(), ovdData.getExtracted().getRelativeName(), priority));
                    validationResponseData.setVoterCardImageExtractedAddress(fillDataBasedOnPriority(validationResponseData.getVoterCardImageExtractedAddress(), ovdData.getExtracted().getAddress().trim(), priority));
                    validationResponseData.setVoterCardImageExtractedNumber(fillDataBasedOnPriority(validationResponseData.getVoterCardImageExtractedNumber(), ovdData.getExtracted().getVoterID(), priority));
                    validationResponseData.setVoterCardImageExtractedPhoto(fillDataBasedOnPriority(validationResponseData.getVoterCardImageExtractedPhoto(), ovdData.getPhoto(), priority));
                    validationResponseData.setVoterCardCroppedImage(fillDataBasedOnPriority(validationResponseData.getVoterCardCroppedImage(), ovdData.getCroppedImage(), priority));
                    break;
            }

        }
// TODO fix
//        System.out.printf(" - Done Preparing OVD Data response for %s image response%n", currentOVDInputName);
    }

    private String fillDataBasedOnPriority(String existingValue, String responseValue, int priority) {
        String response = responseValue;

        if (priority == 0) {
            if (!hasContent(existingValue)) {
                response = responseValue;
            } else {
                response = existingValue;
            }
        }

        return response;
    }

    private boolean hasContent(String value) {
        return value != null && !value.isEmpty();
    }

    private void prepareConfidenceComponents(KycMatchResponseDto validationResponseData, KycMatchRequestDto validationRequestData) {
        try {
            ConfidenceInput confidenceInput = new ConfidenceInput();
            confidenceInput.setRequestData(validationRequestData);
            confidenceInput.setResponseData(validationResponseData);

            //log.debug(" - Preparing Name Confidence Component ");
            new GetNameConfidenceLevel().getConfidenceLevel(confidenceInput);

            //log.debug(" - Preparing DOB Confidence Component ");
            new GetDOBConfidenceLevel().getConfidenceLevel(confidenceInput);

            //log.debug(" - Preparing Father Name Confidence Component ");
            new GetFatherNameConfidenceLevel().getConfidenceLevel(confidenceInput);

            //log.debug(" - Preparing ID Number Confidence Component ");
            new GetIdNumberConfidenceLevel().getConfidenceLevel(confidenceInput);

            //log.debug(" - Preparing Zip Code Confidence Component ");
            new GetZipCodeConfidenceLevel().getConfidenceLevel(confidenceInput);

            //log.debug(" - Preparing State Confidence Component ");
            new GetStateConfidenceLevel().getConfidenceLevel(confidenceInput);

            //log.debug(" - Preparing MobileNo Confidence Component ");
            new GetMobileNoConfidenceLevel().getConfidenceLevel(confidenceInput);

            //log.debug(" - Preparing EmailID Confidence Component ");
            new GetEmailIdConfidenceLevel().getConfidenceLevel(confidenceInput);

            //log.debug(" - Preparing Photo Confidence Component ");
            new GetPhotoConfidenceLevel(environment).getConfidenceLevel(confidenceInput);

        } catch (Exception ex) {
            //log.error(" - Error occurred in preparing Confidence components : " + ex.getMessage(), ex);
            throw ex;
        }
    }

    private void prepareResponseData(KycMatchResponseDto responseData) {
        if (responseData == null) return;

        try {
            //log.debug(" - Processing Data through Massage Layer");
            responseData.setAadhaarImageExtractedName(new RemoveSpecialCharactersName().clear(responseData.getAadhaarImageExtractedName()));
            responseData.setAadhaarImageExtractedDOB(new RemoveSpecialCharactersDOB().clear(responseData.getAadhaarImageExtractedDOB()));
            responseData.setAadhaarImageExtractedNumber(new RemoveSpecialCharactersUid().clear(responseData.getAadhaarImageExtractedNumber()));
            responseData.setAadhaarImageExtractedZipCode(new RemoveSpecialCharactersZipCode().clear(responseData.getAadhaarImageExtractedAddress()));
            responseData.setAadhaarImageExtractedMobileNo(responseData.getAadhaarImageExtractedMobileNo() != null ? new RemoveSpecialCharactersMobileNo().clear(responseData.getAadhaarImageExtractedMobileNo()) : new RemoveSpecialCharactersMobileNo().clear("#" + responseData.getAadhaarImageExtractedAddress()));
            responseData.setAadhaarImageExtractedEmailID(responseData.getAadhaarImageExtractedEmailID() != null ? responseData.getAadhaarImageExtractedEmailID().trim() : null);

            responseData.seteKYCAuthImageExtractedName(new RemoveSpecialCharactersName().clear(responseData.geteKYCAuthImageExtractedName()));
            responseData.seteKYCAuthImageExtractedDOB(new RemoveSpecialCharactersDOB().clear(responseData.geteKYCAuthImageExtractedDOB()));
            responseData.seteKYCAuthImageExtractedNumber(new RemoveSpecialCharactersUid().clear(responseData.geteKYCAuthImageExtractedNumber()));
            responseData.seteKYCAuthImageExtractedZipCode((responseData.geteKYCAuthImageExtractedZipCode() != null && responseData.geteKYCAuthImageExtractedZipCode().length() >= 5) ? new RemoveSpecialCharactersZipCode().clear(responseData.geteKYCAuthImageExtractedZipCode()) : new RemoveSpecialCharactersZipCode().clear(responseData.geteKYCAuthImageExtractedAddress()));
            responseData.seteKYCAuthImageExtractedMobileNo(responseData.geteKYCAuthImageExtractedMobileNo() != null ? new RemoveSpecialCharactersMobileNo().clear(responseData.geteKYCAuthImageExtractedMobileNo()) : new RemoveSpecialCharactersMobileNo().clear("#" + responseData.geteKYCAuthImageExtractedAddress()));
            responseData.seteKYCAuthImageExtractedEmailID(responseData.geteKYCAuthImageExtractedEmailID() != null ? responseData.geteKYCAuthImageExtractedEmailID().trim() : null);

            responseData.setOfflineAadhaarImageExtractedName(new RemoveSpecialCharactersName().clear(responseData.getOfflineAadhaarImageExtractedName()));
            responseData.setOfflineAadhaarImageExtractedDOB(new RemoveSpecialCharactersDOB().clear(responseData.getOfflineAadhaarImageExtractedDOB()));
            responseData.setOfflineAadhaarImageExtractedNumber(new RemoveSpecialCharactersUid().clear(responseData.getOfflineAadhaarImageExtractedNumber()));
            responseData.setOfflineAadhaarImageExtractedZipCode(new RemoveSpecialCharactersZipCode().clear(responseData.getOfflineAadhaarImageExtractedAddress()));
            responseData.setOfflineAadhaarImageExtractedMobileNo(responseData.getOfflineAadhaarImageExtractedMobileNo() != null ? new RemoveSpecialCharactersMobileNo().clear(responseData.getOfflineAadhaarImageExtractedMobileNo()) : new RemoveSpecialCharactersMobileNo().clear("#" + responseData.getOfflineAadhaarImageExtractedAddress()));
            responseData.setOfflineAadhaarImageExtractedEmailID(responseData.getOfflineAadhaarImageExtractedEmailID() != null ? responseData.getOfflineAadhaarImageExtractedEmailID().trim() : null);

            responseData.setdLImageExtractedName(new RemoveSpecialCharactersName().clear(responseData.getdLImageExtractedName()));
            responseData.setdLImageExtractedDOB(new RemoveSpecialCharactersDOB().clear(responseData.getdLImageExtractedDOB()));
            responseData.setdLImageExtractedZipCode(new RemoveSpecialCharactersDOB().clear(responseData.getdLImageExtractedAddress()));

            responseData.setpPImageExtractedName(new RemoveSpecialCharactersName().clear(responseData.getpPImageExtractedName()));
            responseData.setpPImageExtractedDOB(new RemoveSpecialCharactersDOB().clear(responseData.getpPImageExtractedDOB()));
            responseData.setpPImageExtractedZipCode(new RemoveSpecialCharactersDOB().clear(responseData.getpPImageExtractedAddress()));

            responseData.setPanImageExtractedName(new RemoveSpecialCharactersName().clear(responseData.getPanImageExtractedName()));
            responseData.setPanImageExtractedDOB(new RemoveSpecialCharactersDOB().clear(responseData.getPanImageExtractedDOB()));
            responseData.setPanImageExtractedNumber(new RemoveSpecialCharactersPanNumber().clear(responseData.getPanImageExtractedNumber()));

            responseData.setVoterCardImageExtractedName(new RemoveSpecialCharactersName().clear(responseData.getVoterCardImageExtractedName()));
            responseData.setVoterCardImageExtractedZipCode(new RemoveSpecialCharactersDOB().clear(responseData.getVoterCardImageExtractedAddress()));

            //log.debug(" - Done Processing Data through Massage Layer");
        } catch (Exception ex) {
            //log.error(" - Error occurred in preparing Response Data through Massage Layer : " + ex.getMessage(), ex);
            throw ex;
        }
    }


}
