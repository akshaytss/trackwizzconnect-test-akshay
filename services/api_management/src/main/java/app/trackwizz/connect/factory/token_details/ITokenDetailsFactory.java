package app.trackwizz.connect.factory.token_details;

import app.trackwizz.connect.dto.token.TokenDetailsResponseDto;
import app.trackwizz.connect.model.JwtToken;
import app.trackwizz.connect.model.TokenDetails;

import java.util.List;

public interface ITokenDetailsFactory {
    /**
     * @param jwtToken
     * @return
     */
    TokenDetails mapJwtTokenToTokenDetails(JwtToken jwtToken);

    /**
     * @param tokenDetails
     * @return
     */
    TokenDetailsResponseDto getTokenDetailsResponseDto(TokenDetails tokenDetails);

    /**
     * @param tokenDetails
     * @return
     */
    List<TokenDetailsResponseDto> getTokenDetailsResponseDto(List<TokenDetails> tokenDetails);
}
