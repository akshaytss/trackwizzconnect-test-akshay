package app.trackwizz.connect.repository;

import app.trackwizz.connect.model.CompanyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICompanyDetailsRepository extends JpaRepository<CompanyDetails, Integer> {
    Optional<CompanyDetails> findByCompanyNameIgnoreCase(String companyName);
}
