package app.trackwizz.connect.factory.company_details;

import app.trackwizz.connect.dto.company_details.CompanyDetailsRequestDto;
import app.trackwizz.connect.dto.company_details.CompanyDetailsResponseDto;
import app.trackwizz.connect.model.CompanyDetails;

public interface ICompanyDetailsFactory {
    /**
     * @param companyDetailsRequestDto
     * @return
     */
    CompanyDetails getCompanyDetails(CompanyDetailsRequestDto companyDetailsRequestDto);

    /**
     * @param companyDetails
     * @return
     */
    CompanyDetailsResponseDto getCompanyDetailsResponseDto(CompanyDetails companyDetails);
}
