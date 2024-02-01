package app.trackwizz.connect.repository;

import app.trackwizz.connect.model.TokenDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITokenDetailsRepository extends JpaRepository<TokenDetails, Integer> {
    List<TokenDetails> findByCompanyId(Integer companyId);

    List<TokenDetails> findByTokenScopesContaining(String tokenScopes);
}
