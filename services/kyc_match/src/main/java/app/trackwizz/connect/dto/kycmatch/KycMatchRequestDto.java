package app.trackwizz.connect.dto.kycmatch;


public class KycMatchRequestDto {

    public KycMatchRequestDto(String requestId, String spouseName, String fatherName, String gender, String name, String dob, String emailId, String mobileNo, String aadhaarNumber, String dLNumber, String panNumber, String passportNumber, String voterNumber, String corAddress, String corCity, String corState, String corCountry, String corPin, String perAddress, String perCity, String perState, String perCountry, String perPin, String aadhaarImage, String dLImage, String panImage, String passportImage, String voterCardImage, String photoImage, String eKYCAuthImage, String offlineAadharImage) {
        this.requestId = requestId;
        this.spouseName = spouseName;
        this.fatherName = fatherName;
        this.gender = gender;
        this.name = name;
        this.dob = dob;
        this.emailId = emailId;
        this.mobileNo = mobileNo;
        this.aadhaarNumber = aadhaarNumber;
        this.dLNumber = dLNumber;
        this.panNumber = panNumber;
        this.passportNumber = passportNumber;
        this.voterNumber = voterNumber;
        this.corAddress = corAddress;
        this.corCity = corCity;
        this.corState = corState;
        this.corCountry = corCountry;
        this.corPin = corPin;
        this.perAddress = perAddress;
        this.perCity = perCity;
        this.perState = perState;
        this.perCountry = perCountry;
        this.perPin = perPin;
        this.aadhaarImage = aadhaarImage;
        this.dLImage = dLImage;
        this.panImage = panImage;
        this.passportImage = passportImage;
        this.voterCardImage = voterCardImage;
        this.photoImage = photoImage;
        this.eKYCAuthImage = eKYCAuthImage;
        this.offlineAadharImage = offlineAadharImage;
    }

    public KycMatchRequestDto(KycMatchRequestDto request){
        this.requestId = request.requestId;
        this.spouseName = request.spouseName;
        this.fatherName = request.fatherName;
        this.gender = request.gender;
        this.name = request.name;
        this.dob = request.dob;
        this.emailId = request.emailId;
        this.mobileNo = request.mobileNo;
        this.aadhaarNumber = request.aadhaarNumber;
        this.dLNumber = request.dLNumber;
        this.panNumber = request.panNumber;
        this.passportNumber = request.passportNumber;
        this.voterNumber = request.voterNumber;
        this.corAddress = request.corAddress;
        this.corCity = request.corCity;
        this.corState = request.corState;
        this.corCountry = request.corCountry;
        this.corPin = request.corPin;
        this.perAddress = request.perAddress;
        this.perCity = request.perCity;
        this.perState = request.perState;
        this.perCountry = request.perCountry;
        this.perPin = request.perPin;
        this.aadhaarImage = request.aadhaarImage;
        this.dLImage = request.dLImage;
        this.panImage = request.panImage;
        this.passportImage = request.passportImage;
        this.voterCardImage = request.voterCardImage;
        this.photoImage = request.photoImage;
        this.eKYCAuthImage = request.eKYCAuthImage;
        this.offlineAadharImage = request.offlineAadharImage;
    }

    private String requestId;
    private String spouseName;
    private String fatherName;
    private String gender;
    private String name;
    private String dob;
    private String emailId;
    private String mobileNo;
    private String aadhaarNumber;
    private String dLNumber;
    private String panNumber;
    private String passportNumber;
    private String voterNumber;
    private String corAddress;
    private String corCity;
    private String corState;
    private String corCountry;
    private String corPin;
    private String perAddress;
    private String perCity;
    private String perState;
    private String perCountry;
    private String perPin;
    private String aadhaarImage;
    private String dLImage;
    private String panImage;
    private String passportImage;
    private String voterCardImage;
    private String photoImage;
    private String eKYCAuthImage;
    private String offlineAadharImage;
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getdLNumber() {
        return dLNumber;
    }

    public void setdLNumber(String dLNumber) {
        this.dLNumber = dLNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getVoterNumber() {
        return voterNumber;
    }

    public void setVoterNumber(String voterNumber) {
        this.voterNumber = voterNumber;
    }

    public String getCorAddress() {
        return corAddress;
    }

    public void setCorAddress(String corAddress) {
        this.corAddress = corAddress;
    }

    public String getCorCity() {
        return corCity;
    }

    public void setCorCity(String corCity) {
        this.corCity = corCity;
    }

    public String getCorState() {
        return corState;
    }

    public void setCorState(String corState) {
        this.corState = corState;
    }

    public String getCorCountry() {
        return corCountry;
    }

    public void setCorCountry(String corCountry) {
        this.corCountry = corCountry;
    }

    public String getCorPin() {
        return corPin;
    }

    public void setCorPin(String corPin) {
        this.corPin = corPin;
    }

    public String getPerAddress() {
        return perAddress;
    }

    public void setPerAddress(String perAddress) {
        this.perAddress = perAddress;
    }

    public String getPerCity() {
        return perCity;
    }

    public void setPerCity(String perCity) {
        this.perCity = perCity;
    }

    public String getPerState() {
        return perState;
    }

    public void setPerState(String perState) {
        this.perState = perState;
    }

    public String getPerCountry() {
        return perCountry;
    }

    public void setPerCountry(String perCountry) {
        this.perCountry = perCountry;
    }

    public String getPerPin() {
        return perPin;
    }

    public void setPerPin(String perPin) {
        this.perPin = perPin;
    }

    public String getAadhaarImage() {
        return aadhaarImage;
    }
    public void setAadhaarImage(String aadhaarImage) {
        this.aadhaarImage = aadhaarImage;
    }

    public String getdLImage() {
        return dLImage;
    }

    public void setdLImage(String dLImage) {
        this.dLImage = dLImage;
    }

    public String getPanImage() {
        return panImage;
    }

    public void setPanImage(String panImage) {
        this.panImage = panImage;
    }

    public String getPassportImage() {
        return passportImage;
    }

    public void setPassportImage(String passportImage) {
        this.passportImage = passportImage;
    }

    public String getVoterCardImage() {
        return voterCardImage;
    }

    public void setVoterCardImage(String voterCardImage) {
        this.voterCardImage = voterCardImage;
    }

    public String getPhotoImage() {
        return photoImage;
    }

    public void setPhotoImage(String photoImage) {
        this.photoImage = photoImage;
    }

    public String geteKYCAuthImage() { return eKYCAuthImage; }

    public void seteKYCAuthImage(String eKYCAuthImage) {
        this.eKYCAuthImage = eKYCAuthImage;
    }

    public String getOfflineAadharImage() {
        return offlineAadharImage;
    }

    public void setOfflineAadharImage(String offlineAadharImage) {
        this.offlineAadharImage = offlineAadharImage;
    }

    public static KycMatchRequestDto getMaskedRequestObject(KycMatchRequestDto request) {
        KycMatchRequestDto maskedObj = new KycMatchRequestDto(request);
        maskedObj.setAadhaarImage(null);
        maskedObj.setOfflineAadharImage(null);
        maskedObj.seteKYCAuthImage(null);
        maskedObj.setdLImage(null);
        maskedObj.setPanImage(null);
        maskedObj.setPassportImage(null);
        maskedObj.setVoterCardImage(null);
        maskedObj.setPhotoImage(null);
        return maskedObj;
    }
}
