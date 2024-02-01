package app.trackwizz.connect.service.company_details.db;

import app.trackwizz.connect.model.CompanyDetails;

import java.util.List;
import java.util.Optional;

public interface ICompanyDetailsDbService {
    /**
     * @param companyDetails
     * @return
     */
    CompanyDetails saveCompanyDetails(CompanyDetails companyDetails);

    /**
     * @return
     * @throws Exception
     */
    List<CompanyDetails> getAllCompanyDetails() throws Exception;

    /**
     * @param companyName
     * @return
     */
    Optional<CompanyDetails> getCompanyDetails(String companyName);

    /**
     * @param companyId
     * @return
     */
    Optional<CompanyDetails> getCompanyDetails(Integer companyId);

    /**
     * @param companyId
     */
    void setIsCompanyDeletedToTrueByCompanyId(Integer companyId);
}
