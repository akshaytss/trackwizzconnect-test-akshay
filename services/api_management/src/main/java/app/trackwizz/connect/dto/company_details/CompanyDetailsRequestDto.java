package app.trackwizz.connect.dto.company_details;

import io.swagger.v3.oas.annotations.media.Schema;

public class CompanyDetailsRequestDto {
    @Schema(minLength = 1, maxLength = 255)
    private String companyName;
    @Schema(minLength = 1, maxLength = 255)
    private String companyEmail;

    public CompanyDetailsRequestDto() {
    }

    public CompanyDetailsRequestDto(String companyName, String companyEmail) {
        this.companyName = companyName;
        this.companyEmail = companyEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }
}
