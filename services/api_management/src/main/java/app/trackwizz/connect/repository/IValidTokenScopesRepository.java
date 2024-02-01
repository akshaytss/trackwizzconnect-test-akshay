package app.trackwizz.connect.repository;

import app.trackwizz.connect.model.ValidTokenScopes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IValidTokenScopesRepository extends JpaRepository<ValidTokenScopes, Integer> {
    @Query(value = "SELECT scope FROM valid_token_scopes", nativeQuery = true)
    List<String> findAllByScope();

    Optional<String> findByScopeIgnoreCase(String scope);

}
