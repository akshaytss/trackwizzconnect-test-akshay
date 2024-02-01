package app.trackwizz.connect.service.token_details.db;

import app.trackwizz.connect.constant.MessageConstants;
import app.trackwizz.connect.constant.TokenDetailsConstants;
import app.trackwizz.connect.model.TokenDetails;
import app.trackwizz.connect.repository.ITokenDetailsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TokenDetailsDbService implements ITokenDetailsDbService {
    private final ITokenDetailsRepository ITokenDetailsRepository;
    private final Logger log4j = LogManager.getLogger(TokenDetailsDbService.class);

    public TokenDetailsDbService(ITokenDetailsRepository ITokenDetailsRepository) {
        this.ITokenDetailsRepository = ITokenDetailsRepository;
    }

    @Override
    public TokenDetails saveTokenDetails(TokenDetails tokenDetails) {
        return ITokenDetailsRepository.save(tokenDetails);
    }

    @Override
    public List<TokenDetails> getTokenDetailsByCompanyId(Integer companyId) {
        return ITokenDetailsRepository.findByCompanyId(companyId);
    }

    @Override
    public Optional<TokenDetails> getTokenDetailsById(Integer tokenId) {
        return ITokenDetailsRepository.findById(tokenId);
    }

    @Override
    public void setTokenExpiryToNowByTokenId(Integer tokenId) {
        TokenDetails tokenDetails = getTokenDetailsById(tokenId).orElseThrow(() -> {
            log4j.info(MessageConstants.NO_TOKEN_FOUND + " for tokenId: " + tokenId);
            return new EntityNotFoundException(MessageConstants.NO_TOKEN_FOUND + " for tokenId: " + tokenId);
        });
        tokenDetails.setTokenExpiryAt(Timestamp.from(Instant.now()));
        tokenDetails.setTokenManuallyExpiredByUser(TokenDetailsConstants.TOKEN_MANUALLY_EXPIRED_USER_ADMIN);
        ITokenDetailsRepository.save(tokenDetails);
    }

    @Override
    public List<TokenDetails> getTokenDetailsByScope(String scope) {
        return ITokenDetailsRepository.findByTokenScopesContaining(scope);
    }
}
