package app.trackwizz.connect.service.company_details;

import app.trackwizz.connect.dto.ApiResponseDto;
import app.trackwizz.connect.dto.company_details.CompanyDetailsRequestDto;
import app.trackwizz.connect.dto.company_details.CompanyDetailsResponseDto;
import org.springframework.http.ResponseEntity;

public interface ICompanyDetailsService {
    /**
     * @param companyId
     * @param companyName
     * @return
     */
    ResponseEntity<ApiResponseDto<CompanyDetailsResponseDto>> getCompanyDetailsByCompanyIdOrCompanyName(Integer companyId, String companyName, boolean requestHasCompanyId);

    /**
     * @param companyId
     * @param companyDetails
     * @return
     */
    ResponseEntity<ApiResponseDto<CompanyDetailsResponseDto>> updateCompanyDetailsByCompanyId(Integer companyId, CompanyDetailsRequestDto companyDetails);

    /**
     * @param companyId
     * @return
     */
    ResponseEntity<ApiResponseDto<Void>> markCompanyAsDeleted(Integer companyId);

    /**
     * @param companyDetails
     * @return
     */
    ResponseEntity<ApiResponseDto<CompanyDetailsResponseDto>> createCompany(CompanyDetailsRequestDto companyDetails);
}
