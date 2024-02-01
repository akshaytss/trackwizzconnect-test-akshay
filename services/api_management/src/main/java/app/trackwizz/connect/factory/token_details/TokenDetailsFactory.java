package app.trackwizz.connect.factory.token_details;

import app.trackwizz.connect.constant.JwtConstants;
import app.trackwizz.connect.constant.TokenDetailsConstants;
import app.trackwizz.connect.dto.token.TokenDetailsResponseDto;
import app.trackwizz.connect.model.JwtToken;
import app.trackwizz.connect.model.TokenDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenDetailsFactory implements ITokenDetailsFactory {

    @Override
    public TokenDetails mapJwtTokenToTokenDetails(JwtToken jwtToken) {
        TokenDetails tokenDetails = new TokenDetails();
        tokenDetails.setTokenString(jwtToken.getToken());
        tokenDetails.setCompanyId(jwtToken.getCompanyId());
        tokenDetails.setTokenScopes(String.join(JwtConstants.TOKEN_SCOPES_DELIMITER, jwtToken.getScopes()));
        tokenDetails.setTokenIssueAt(Timestamp.from(jwtToken.getCreationTime()));
        tokenDetails.setTokenExpiryAt(Timestamp.from(jwtToken.getExpiryTime()));
        tokenDetails.setTokenManuallyExpiredByUser(TokenDetailsConstants.TOKEN_MANUALLY_EXPIRED_USER_SYSTEM);
        tokenDetails.setEnvironment(jwtToken.getIssuingEnvironment());
        return tokenDetails;
    }

    @Override
    public TokenDetailsResponseDto getTokenDetailsResponseDto(TokenDetails tokenDetails) {
        return new TokenDetailsResponseDto(
                tokenDetails.getTokenId(),
                tokenDetails.getCompanyId(),
                tokenDetails.getTokenString(),
                tokenDetails.getTokenScopes(),
                tokenDetails.getTokenIssueAt(),
                tokenDetails.getTokenExpiryAt()
        );
    }

    @Override
    public List<TokenDetailsResponseDto> getTokenDetailsResponseDto(List<TokenDetails> tokenDetailsList) {
        return tokenDetailsList.stream()
                .map(tokenDetails -> new TokenDetailsResponseDto(
                        tokenDetails.getTokenId(),
                        tokenDetails.getCompanyId(),
                        tokenDetails.getTokenString(),
                        tokenDetails.getTokenScopes(),
                        tokenDetails.getTokenIssueAt(),
                        tokenDetails.getTokenExpiryAt()
                ))
                .collect(Collectors.toList());
    }
}
