package app.trackwizz.connect.factory;

import app.trackwizz.connect.constant.common.CommonConstants;
import app.trackwizz.connect.constant.common.OvdConstants;
import app.trackwizz.connect.dto.BaseDataImageModel;
import app.trackwizz.connect.dto.kyc_extraction.KycExtractionResponseDto;
import app.trackwizz.connect.dto.orbo.OrboKycExtractionResponseDto;
import app.trackwizz.connect.dto.orbo.OrboResponseDto;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class KycExtractionFactory implements IKycExtractionFactory {
    @Override
    public KycExtractionResponseDto createDto(String kycExtractionRequestId, OrboResponseDto orboJsonResponse) {
        OrboKycExtractionResponseDto orboResponseObj = orboJsonResponse.getData().stream().findFirst().get();
        KycExtractionResponseDto responseDto = new KycExtractionResponseDto();

        responseDto.setRequestStatus(CommonConstants.ACCEPTED_BY_TW);
        responseDto.setRequestRejectionCode(-1);
        responseDto.setRequestRejectionDescription(null);
        responseDto.setRequestId(kycExtractionRequestId);
        responseDto.setQrCode(null);
        responseDto.setHeader(null);

        if (orboResponseObj.getClassType().equals(OvdConstants.Aadhar))
            responseDto.setIsAadhaar(CommonConstants.TRUE);
        if (orboResponseObj.getClassType().equals(OvdConstants.PAN_Card))
            responseDto.setIsPan(CommonConstants.TRUE);

        responseDto.setIdType(convertClassToIdTypeMapping(orboResponseObj.getClassType()));
        responseDto.setPhoto(new BaseDataImageModel(null, orboResponseObj.getIdPhoto()));
        responseDto.setUid(new BaseDataImageModel(orboResponseObj.getExtracted().getAadharIdNumber(), null));
        responseDto.setDrivingLicenceNumber(new BaseDataImageModel(orboResponseObj.getExtracted().getDrivingLicenseNumber(), null));
        responseDto.setPassportNumber(new BaseDataImageModel(orboResponseObj.getExtracted().getPassportIdNumber(), null));
        responseDto.setVoterIdNumber(new BaseDataImageModel(orboResponseObj.getExtracted().getVoterIdNumber(), null));
        responseDto.setGender(new BaseDataImageModel(orboResponseObj.getExtracted().getGender(), null));
        responseDto.setMaskedAd(new BaseDataImageModel(null, orboResponseObj.getMaskedAadharBase64()));
        responseDto.setName(new BaseDataImageModel(orboResponseObj.getExtracted().getName(), null));
        responseDto.setDob(new BaseDataImageModel(orboResponseObj.getExtracted().getDateOfBirth(), null));
        responseDto.setFatherName(new BaseDataImageModel(orboResponseObj.getExtracted().getGuardianName(), null));
        responseDto.setPanNum(new BaseDataImageModel(orboResponseObj.getExtracted().getPanCardNumber(), null));
        responseDto.setAddress(new BaseDataImageModel(orboResponseObj.getExtracted().getAddress(), null));
        responseDto.setState(null);
        responseDto.setZipCode(null);

        return responseDto;
    }

    private static String convertClassToIdTypeMapping(String input) {
        return switch (input) {
            case "Aadhar" -> "AadharCard";
            case "PAN Card" -> "PanCard";
            case "Driving Licence" -> "DrivingLicence";
            case "Passport" -> "Passport";
            case "Voter ID Card" -> "VoterId";
            default -> input;
        };
    }

    public static boolean isValidBase64String(String str) {
        try {
            Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
