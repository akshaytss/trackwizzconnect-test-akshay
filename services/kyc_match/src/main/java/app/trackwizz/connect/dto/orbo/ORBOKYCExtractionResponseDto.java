package app.trackwizz.connect.dto.orbo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ORBOKYCExtractionResponseDto {
    public static class IndividualDocument {
        @JsonProperty("Aadhar ID")
        private String AadharID;

        @JsonProperty("Address")
        private String Address;

        @JsonProperty("Age")
        private String Age;

        @JsonProperty("Blood Group")
        private String BloodGroup;

        @JsonProperty("CareOf")
        private String CareOf;

        @JsonProperty("Country")
        private String Country;

        @JsonProperty("Country Code")
        private String CountryCode;

        @JsonProperty("Date of Birth")
        private String DateOfBirth;

        @JsonProperty("Date of Expiry")
        private String DateOfExpiry;

        @JsonProperty("Date of Issue")
        private String DateOfIssue;

        @JsonProperty("District")
        private String District;

        @JsonProperty("Driving License No.")
        private String DrivingLicenseNo;

        @JsonProperty("Email Id")
        private String EmailId;

        @JsonProperty("File No.")
        private String FileNo;

        @JsonProperty("Gender")
        private String Gender;

        @JsonProperty("Given Name")
        private String GivenName;

        @JsonProperty("Guardian")
        private String Guardian;

        @JsonProperty("Guardian Name")
        private String GuardianName;

        @JsonProperty("House")
        private String HouseNo;

        @JsonProperty("Landmark")
        private String Landmark;

        @JsonProperty("Locality")
        private String Locality;

        @JsonProperty("Mobile No")
        private String MobileNo;

        @JsonProperty("Mother's Name")
        private String MothersName;

        @JsonProperty("Name")
        private String Name;

        @JsonProperty("Nationality")
        private String Nationality;

        @JsonProperty("Old ID")
        private String OldID;

        @JsonProperty("Old date")
        private String OldDate;

        @JsonProperty("Old place")
        private String OldPlace;

        @JsonProperty("PAN No.")
        private String PANNo;

        @JsonProperty("Passport ID")
        private String PassportID;

        @JsonProperty("Pin Code")
        private String PinCode;

        @JsonProperty("Place of Birth")
        private String PlaceOfBirth;

        @JsonProperty("Place of Issue")
        private String PlaceOfIssue;

        @JsonProperty("Post Office")
        private String PostOffice;

        @JsonProperty("Relative Name")
        private String RelativeName;

        @JsonProperty("Spouse Name")
        private String SpouseName;

        @JsonProperty("State")
        private String State;

        @JsonProperty("Sub District")
        private String SubDistrict;

        @JsonProperty("Surname")
        private String Surname;

        @JsonProperty("Type")
        private String Type;

        @JsonProperty("VTC")
        private String VTC;

        @JsonProperty("Vehicle Class")
        private String VehicleClass;

        @JsonProperty("Vehicle Class & Date")
        private String VehicleClassAndDate;

        @JsonProperty("Village")
        private String Village;

        @JsonProperty("Virtual ID")
        private String VirtualID;

        @JsonProperty("Voter ID")
        private String VoterID;

        // Getter and Setter methods for each property

        public String getAadharID() {
            return AadharID;
        }

        public void setAadharID(String AadharID) {
            this.AadharID = AadharID;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getAge() {
            return Age;
        }

        public void setAge(String Age) {
            this.Age = Age;
        }

        public String getBloodGroup() {
            return BloodGroup;
        }

        public void setBloodGroup(String BloodGroup) {
            this.BloodGroup = BloodGroup;
        }

        public String getCareOf() {
            return CareOf;
        }

        public void setCareOf(String CareOf) {
            this.CareOf = CareOf;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String Country) {
            this.Country = Country;
        }

        public String getCountryCode() {
            return CountryCode;
        }

        public void setCountryCode(String CountryCode) {
            this.CountryCode = CountryCode;
        }

        public String getDateOfBirth() {
            return DateOfBirth;
        }

        public void setDateOfBirth(String DateOfBirth) {
            this.DateOfBirth = DateOfBirth;
        }

        public String getDateOfExpiry() {
            return DateOfExpiry;
        }

        public void setDateOfExpiry(String DateOfExpiry) {
            this.DateOfExpiry = DateOfExpiry;
        }

        public String getDateOfIssue() {
            return DateOfIssue;
        }

        public void setDateOfIssue(String DateOfIssue) {
            this.DateOfIssue = DateOfIssue;
        }

        public String getDistrict() {
            return District;
        }

        public void setDistrict(String District) {
            this.District = District;
        }

        public String getDrivingLicenseNo() {
            return DrivingLicenseNo;
        }

        public void setDrivingLicenseNo(String DrivingLicenseNo) {
            this.DrivingLicenseNo = DrivingLicenseNo;
        }

        public String getEmailId() {
            return EmailId;
        }

        public void setEmailId(String EmailId) {
            this.EmailId = EmailId;
        }

        public String getFileNo() {
            return FileNo;
        }

        public void setFileNo(String FileNo) {
            this.FileNo = FileNo;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String Gender) {
            this.Gender = Gender;
        }

        public String getGivenName() {
            return GivenName;
        }

        public void setGivenName(String GivenName) {
            this.GivenName = GivenName;
        }

        public String getGuardian() {
            return Guardian;
        }

        public void setGuardian(String Guardian) {
            this.Guardian = Guardian;
        }

        public String getGuardianName() {
            return GuardianName;
        }

        public void setGuardianName(String GuardianName) {
            this.GuardianName = GuardianName;
        }

        public String getHouseNo() {
            return HouseNo;
        }

        public void setHouseNo(String HouseNo) {
            this.HouseNo = HouseNo;
        }

        public String getLandmark() {
            return Landmark;
        }

        public void setLandmark(String Landmark) {
            this.Landmark = Landmark;
        }

        public String getLocality() {
            return Locality;
        }

        public void setLocality(String Locality) {
            this.Locality = Locality;
        }

        public String getMobileNo() {
            return MobileNo;
        }

        public void setMobileNo(String MobileNo) {
            this.MobileNo = MobileNo;
        }

        public String getMothersName() {
            return MothersName;
        }

        public void setMothersName(String MothersName) {
            this.MothersName = MothersName;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getNationality() {
            return Nationality;
        }

        public void setNationality(String Nationality) {
            this.Nationality = Nationality;
        }

        public String getOldID() {
            return OldID;
        }

        public void setOldID(String OldID) {
            this.OldID = OldID;
        }

        public String getOldDate() {
            return OldDate;
        }

        public void setOldDate(String OldDate) {
            this.OldDate = OldDate;
        }

        public String getOldPlace() {
            return OldPlace;
        }

        public void setOldPlace(String OldPlace) {
            this.OldPlace = OldPlace;
        }

        public String getPANNo() {
            return PANNo;
        }

        public void setPANNo(String PANNo) {
            this.PANNo = PANNo;
        }

        public String getPassportID() {
            return PassportID;
        }

        public void setPassportID(String PassportID) {
            this.PassportID = PassportID;
        }

        public String getPinCode() {
            return PinCode;
        }

        public void setPinCode(String PinCode) {
            this.PinCode = PinCode;
        }

        public String getPlaceOfBirth() {
            return PlaceOfBirth;
        }

        public void setPlaceOfBirth(String PlaceOfBirth) {
            this.PlaceOfBirth = PlaceOfBirth;
        }

        public String getPlaceOfIssue() {
            return PlaceOfIssue;
        }

        public void setPlaceOfIssue(String PlaceOfIssue) {
            this.PlaceOfIssue = PlaceOfIssue;
        }

        public String getPostOffice() {
            return PostOffice;
        }

        public void setPostOffice(String PostOffice) {
            this.PostOffice = PostOffice;
        }

        public String getRelativeName() {
            return RelativeName;
        }

        public void setRelativeName(String RelativeName) {
            this.RelativeName = RelativeName;
        }

        public String getSpouseName() {
            return SpouseName;
        }

        public void setSpouseName(String SpouseName) {
            this.SpouseName = SpouseName;
        }

        public String getState() {
            return State;
        }

        public void setState(String State) {
            this.State = State;
        }

        public String getSubDistrict() {
            return SubDistrict;
        }

        public void setSubDistrict(String SubDistrict) {
            this.SubDistrict = SubDistrict;
        }

        public String getSurname() {
            return Surname;
        }

        public void setSurname(String Surname) {
            this.Surname = Surname;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public String getVTC() {
            return VTC;
        }

        public void setVTC(String VTC) {
            this.VTC = VTC;
        }

        public String getVehicleClass() {
            return VehicleClass;
        }

        public void setVehicleClass(String VehicleClass) {
            this.VehicleClass = VehicleClass;
        }

        public String getVehicleClassAndDate() {
            return VehicleClassAndDate;
        }

        public void setVehicleClassAndDate(String VehicleClassAndDate) {
            this.VehicleClassAndDate = VehicleClassAndDate;
        }

        public String getVillage() {
            return Village;
        }

        public void setVillage(String Village) {
            this.Village = Village;
        }

        public String getVirtualID() {
            return VirtualID;
        }

        public void setVirtualID(String VirtualID) {
            this.VirtualID = VirtualID;
        }

        public String getVoterID() {
            return VoterID;
        }

        public void setVoterID(String VoterID) {
            this.VoterID = VoterID;
        }
    }


    @JsonProperty("class")
    private String Class;

    @JsonProperty("subClass")
    private String SubClass;

    @JsonProperty("crop_image")
    private String CroppedImage;

    @JsonProperty("extracted")
    private IndividualDocument extracted;

    @JsonProperty("masked_aadhar_b64")
    private String MaskedAadhar;

    @JsonProperty("id_photo")
    private String Photo;

    // Getter and Setter methods for each property

    public String getClassValue() {
        return Class;
    }

    public void setClassValue(String Class) {
        this.Class = Class;
    }

    public String getSubClass() {
        return SubClass;
    }

    public void setSubClass(String SubClass) {
        this.SubClass = SubClass;
    }

    public String getCroppedImage() {
        return CroppedImage;
    }

    public void setCroppedImage(String CroppedImage) {
        this.CroppedImage = CroppedImage;
    }

    public IndividualDocument getExtracted() {
        return extracted;
    }

    public void setExtracted(IndividualDocument extracted) {
        this.extracted = extracted;
    }

    public String getMaskedAadhar() {
        return MaskedAadhar;
    }

    public void setMaskedAadhar(String MaskedAadhar) {
        this.MaskedAadhar = MaskedAadhar;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }
}
