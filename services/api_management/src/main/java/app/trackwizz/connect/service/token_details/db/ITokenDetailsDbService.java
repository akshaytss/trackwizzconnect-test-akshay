package app.trackwizz.connect.service.token_details.db;

import app.trackwizz.connect.model.TokenDetails;

import java.util.List;
import java.util.Optional;

public interface ITokenDetailsDbService {
    /**
     * @param tokenDetails
     * @return
     */
    TokenDetails saveTokenDetails(TokenDetails tokenDetails);

    /**
     * @param companyId
     * @return
     */
    List<TokenDetails> getTokenDetailsByCompanyId(Integer companyId);

    /**
     * @param tokenId
     * @return
     */
    Optional<TokenDetails> getTokenDetailsById(Integer tokenId);
    /**
     * @param tokenId
     */
    void setTokenExpiryToNowByTokenId(Integer tokenId);

    /**
     * @param scope
     * @return
     */
    List<TokenDetails> getTokenDetailsByScope(String scope);
}
