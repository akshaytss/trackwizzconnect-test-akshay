package app.trackwizz.connect.dto.orbo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrboKycExtractionResponseDto {
    public static class IndividualDocument {

        @JsonProperty("Aadhar ID")
        private String aadharIdNumber;

        @JsonProperty("Driving License No.")
        private String drivingLicenseNumber;

        @JsonProperty("Passport ID")
        private String passportIdNumber;

        @JsonProperty("Voter ID")
        private String voterIdNumber;

        @JsonProperty("Gender")
        private String gender;

        @JsonProperty("Name")
        private String name;
        @JsonProperty("Date of Birth")
        private String dateOfBirth;

        @JsonProperty("Guardian Name")
        private String guardianName;

        @JsonProperty("PAN No.")
        private String panCardNumber;

        @JsonProperty("Address")
        private String address;

        public String getDrivingLicenseNumber() {
            return drivingLicenseNumber;
        }

        public void setDrivingLicenseNumber(String drivingLicenseNumber) {
            this.drivingLicenseNumber = drivingLicenseNumber;
        }

        public String getPassportIdNumber() {
            return passportIdNumber;
        }

        public void setPassportIdNumber(String passportIdNumber) {
            this.passportIdNumber = passportIdNumber;
        }

        public String getVoterIdNumber() {
            return voterIdNumber;
        }

        public void setVoterIdNumber(String voterIdNumber) {
            this.voterIdNumber = voterIdNumber;
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

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getGuardianName() {
            return guardianName;
        }

        public void setGuardianName(String guardianName) {
            this.guardianName = guardianName;
        }

        public String getPanCardNumber() {
            return panCardNumber;
        }

        public void setPanCardNumber(String panCardNumber) {
            this.panCardNumber = panCardNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAadharIdNumber() {
            return aadharIdNumber;
        }

        public void setAadharIdNumber(String aadharIdNumber) {
            this.aadharIdNumber = aadharIdNumber;
        }
    }

    @JsonProperty("class")
    private String classType;

    public String getMaskedAadharBase64() {
        return maskedAadharBase64;
    }

    public void setMaskedAadharBase64(String maskedAadharBase64) {
        this.maskedAadharBase64 = maskedAadharBase64;
    }

    public String getSubClass() {
        return subClass;
    }

    public void setSubClass(String subClass) {
        this.subClass = subClass;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getCropImage() {
        return cropImage;
    }

    public void setCropImage(String cropImage) {
        this.cropImage = cropImage;
    }

    @JsonProperty("masked_aadhar_b64")
    private String maskedAadharBase64;
    @JsonProperty("id_photo")
    private String idPhoto;

    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }

    @JsonProperty("subClass")
    private String subClass;
    @JsonProperty("page_no")
    private int pageNo;
    @JsonProperty("crop_image")
    private String cropImage;

    @JsonProperty("extracted")
    private IndividualDocument extracted;

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public IndividualDocument getExtracted() {
        return extracted;
    }

    public void setExtracted(IndividualDocument extracted) {
        this.extracted = extracted;
    }
}
