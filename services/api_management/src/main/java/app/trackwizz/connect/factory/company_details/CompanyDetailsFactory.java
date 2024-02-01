package app.trackwizz.connect.factory.company_details;

import app.trackwizz.connect.dto.company_details.CompanyDetailsRequestDto;
import app.trackwizz.connect.dto.company_details.CompanyDetailsResponseDto;
import app.trackwizz.connect.model.CompanyDetails;
import org.springframework.stereotype.Component;

@Component
public class CompanyDetailsFactory implements ICompanyDetailsFactory {

    @Override
    public CompanyDetails getCompanyDetails(CompanyDetailsRequestDto companyDetailsRequestDto) {
        CompanyDetails companyDetails = new CompanyDetails();
        companyDetails.setCompanyName(companyDetailsRequestDto.getCompanyName());
        companyDetails.setCompanyEmail(companyDetailsRequestDto.getCompanyEmail());
        return companyDetails;
    }

    @Override
    public CompanyDetailsResponseDto getCompanyDetailsResponseDto(CompanyDetails companyDetails) {
        return new CompanyDetailsResponseDto(
                companyDetails.getCompanyId(),
                companyDetails.getCompanyName(),
                companyDetails.getCompanyEmail()
        );
    }
}
