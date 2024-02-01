package app.trackwizz.connect.dto.kyc_extraction;

import app.trackwizz.connect.constant.common.CommonConstants;
import app.trackwizz.connect.dto.BaseDataImageModel;

public class KycExtractionResponseDto {
    public String requestStatus;
    public int requestRejectionCode;
    public String requestRejectionDescription;
    public String requestId;
    public String isAadhaar;
    public String isPan;
    public String qrCode;
    public String header;
    public BaseDataImageModel photo;
    public BaseDataImageModel uid;
    public BaseDataImageModel drivingLicenceNumber;
    public BaseDataImageModel passportNumber;
    public BaseDataImageModel voterIdNumber;
    public BaseDataImageModel gender;
    public BaseDataImageModel maskedAd;
    public BaseDataImageModel name;
    public BaseDataImageModel dob;
    public BaseDataImageModel fatherName;
    public String signature;
    public BaseDataImageModel panNum;
    public String error;
    public String idType;
    public BaseDataImageModel address;
    public BaseDataImageModel state;
    public BaseDataImageModel zipCode;

    public KycExtractionResponseDto() {
        this.isAadhaar = CommonConstants.FALSE;
        this.isPan = CommonConstants.FALSE;
    }

    private KycExtractionResponseDto(KycExtractionResponseDto responseObj) {
        this.requestStatus = responseObj.requestStatus;
        this.requestRejectionCode = responseObj.requestRejectionCode;
        this.requestRejectionDescription = responseObj.requestRejectionDescription;
        this.requestId = responseObj.requestId;
        this.isAadhaar = responseObj.isAadhaar;
        this.isPan = responseObj.isPan;
        this.qrCode = responseObj.qrCode;
        this.header = responseObj.header;
        this.photo = responseObj.photo;
        this.uid = responseObj.uid;
        this.drivingLicenceNumber = responseObj.drivingLicenceNumber;
        this.passportNumber = responseObj.passportNumber;
        this.voterIdNumber = responseObj.voterIdNumber;
        this.gender = responseObj.gender;
        this.maskedAd = responseObj.maskedAd;
        this.name = responseObj.name;
        this.dob = responseObj.dob;
        this.fatherName = responseObj.fatherName;
        this.signature = responseObj.signature;
        this.panNum = responseObj.panNum;
        this.error = responseObj.error;
        this.idType = responseObj.idType;
        this.address = responseObj.address;
        this.state = responseObj.state;
        this.zipCode = responseObj.zipCode;
    }

    public String getIsAadhaar() {
        return isAadhaar;
    }

    public void setIsAadhaar(String isAadhaar) {
        this.isAadhaar = isAadhaar;
    }

    public String getIsPan() {
        return isPan;
    }

    public void setIsPan(String isPan) {
        this.isPan = isPan;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public int getRequestRejectionCode() {
        return requestRejectionCode;
    }

    public void setRequestRejectionCode(int requestRejectionCode) {
        this.requestRejectionCode = requestRejectionCode;
    }

    public String getRequestRejectionDescription() {
        return requestRejectionDescription;
    }

    public void setRequestRejectionDescription(String requestRejectionDescription) {
        this.requestRejectionDescription = requestRejectionDescription;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public BaseDataImageModel getPhoto() {
        return photo;
    }

    public void setPhoto(BaseDataImageModel photo) {
        this.photo = photo;
    }

    public BaseDataImageModel getUid() {
        return uid;
    }

    public void setUid(BaseDataImageModel uid) {
        this.uid = uid;
    }

    public BaseDataImageModel getDrivingLicenceNumber() {
        return drivingLicenceNumber;
    }

    public void setDrivingLicenceNumber(BaseDataImageModel drivingLicenceNumber) {
        this.drivingLicenceNumber = drivingLicenceNumber;
    }

    public BaseDataImageModel getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(BaseDataImageModel passportNumber) {
        this.passportNumber = passportNumber;
    }

    public BaseDataImageModel getVoterIdNumber() {
        return voterIdNumber;
    }

    public void setVoterIdNumber(BaseDataImageModel voterIdNumber) {
        this.voterIdNumber = voterIdNumber;
    }

    public BaseDataImageModel getGender() {
        return gender;
    }

    public void setGender(BaseDataImageModel gender) {
        this.gender = gender;
    }

    public BaseDataImageModel getMaskedAd() {
        return maskedAd;
    }

    public void setMaskedAd(BaseDataImageModel maskedAd) {
        this.maskedAd = maskedAd;
    }

    public BaseDataImageModel getName() {
        return name;
    }

    public void setName(BaseDataImageModel name) {
        this.name = name;
    }

    public BaseDataImageModel getDob() {
        return dob;
    }

    public void setDob(BaseDataImageModel dob) {
        this.dob = dob;
    }

    public BaseDataImageModel getFatherName() {
        return fatherName;
    }

    public void setFatherName(BaseDataImageModel fatherName) {
        this.fatherName = fatherName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public BaseDataImageModel getPanNum() {
        return panNum;
    }

    public void setPanNum(BaseDataImageModel panNum) {
        this.panNum = panNum;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public BaseDataImageModel getAddress() {
        return address;
    }

    public void setAddress(BaseDataImageModel address) {
        this.address = address;
    }

    public BaseDataImageModel getState() {
        return state;
    }

    public void setState(BaseDataImageModel state) {
        this.state = state;
    }

    public BaseDataImageModel getZipCode() {
        return zipCode;
    }

    public void setZipCode(BaseDataImageModel zipCode) {
        this.zipCode = zipCode;
    }

    public static KycExtractionResponseDto getMaskedResponseObject(KycExtractionResponseDto response) {
        KycExtractionResponseDto maskedObj = new KycExtractionResponseDto(response);
        maskedObj.setPhoto(null);
        maskedObj.setMaskedAd(new BaseDataImageModel(null, null));
        return maskedObj;
    }
}
