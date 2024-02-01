package app.trackwizz.connect.service.company_details.db;

import app.trackwizz.connect.model.CompanyDetails;
import app.trackwizz.connect.repository.ICompanyDetailsRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyDetailsDbService implements ICompanyDetailsDbService {
    private final ICompanyDetailsRepository ICompanyDetailsRepository;

    public CompanyDetailsDbService(ICompanyDetailsRepository ICompanyDetailsRepository) {
        this.ICompanyDetailsRepository = ICompanyDetailsRepository;
    }

    @Override
    public CompanyDetails saveCompanyDetails(CompanyDetails companyDetails) {
        return ICompanyDetailsRepository.save(companyDetails);
    }

    @Override
    public List<CompanyDetails> getAllCompanyDetails() throws Exception {
        try {
            return ICompanyDetailsRepository.findAll();
        } catch (Exception e) {
            throw new Exception("Error will fetching all companyDetails");
        }
    }

    @Override
    public Optional<CompanyDetails> getCompanyDetails(String companyName) {
        return ICompanyDetailsRepository.findByCompanyNameIgnoreCase(companyName);
    }

    @Override
    public Optional<CompanyDetails> getCompanyDetails(Integer companyId) {
        return ICompanyDetailsRepository.findById(companyId);
    }

    @Override
    public void setIsCompanyDeletedToTrueByCompanyId(Integer companyId) {
        CompanyDetails companyDetails = ICompanyDetailsRepository.findById(companyId).orElseThrow(() ->
                new EntityNotFoundException("No company found for companyId: " + companyId));
        companyDetails.setCompanyDeleted(true);
        ICompanyDetailsRepository.save(companyDetails);
    }
}
