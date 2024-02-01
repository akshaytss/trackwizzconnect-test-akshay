package app.trackwizz.connect.service.valid_token_scopes;

import app.trackwizz.connect.repository.IValidTokenScopesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValidTokenScopesDbService implements IValidTokenScopesDbService {
    private final IValidTokenScopesRepository IValidTokenScopesRepository;

    public ValidTokenScopesDbService(IValidTokenScopesRepository IValidTokenScopesRepository) {
        this.IValidTokenScopesRepository = IValidTokenScopesRepository;
    }

    @Override
    public List<String> getAllValidTokenScopes() {
        return IValidTokenScopesRepository.findAllByScope();
    }

    @Override
    public Optional<String> findScope(String scope) {
        return IValidTokenScopesRepository.findByScopeIgnoreCase(scope);
    }
}
