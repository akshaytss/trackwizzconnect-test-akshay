package app.trackwizz.connect.dto.company_details;

import io.swagger.v3.oas.annotations.media.Schema;

public class CompanyDetailsResponseDto {
    private Integer companyId;
    @Schema(minLength = 1, maxLength = 255)
    private String companyName;
    @Schema(minLength = 1, maxLength = 255)
    private String companyEmail;

    public CompanyDetailsResponseDto() {
    }

    public CompanyDetailsResponseDto(Integer companyId, String companyName, String companyEmail) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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
