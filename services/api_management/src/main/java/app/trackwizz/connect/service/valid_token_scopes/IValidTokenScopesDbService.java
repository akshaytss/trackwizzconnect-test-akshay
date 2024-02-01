package app.trackwizz.connect.service.valid_token_scopes;

import java.util.List;
import java.util.Optional;

public interface IValidTokenScopesDbService {
    /**
     * @return
     */
    List<String> getAllValidTokenScopes();

    /**
     * @param scope
     * @return
     */
    Optional<String> findScope(String scope);
}
