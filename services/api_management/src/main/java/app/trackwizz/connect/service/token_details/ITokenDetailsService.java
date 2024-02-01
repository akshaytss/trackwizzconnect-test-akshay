package app.trackwizz.connect.service.token_details;

import app.trackwizz.connect.dto.ApiResponseDto;
import app.trackwizz.connect.dto.token.TokenCreationRequestDto;
import app.trackwizz.connect.dto.token.TokenDetailsResponseDto;
import app.trackwizz.connect.exception.InvalidInputTokenCreationException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITokenDetailsService {
    /**
     * @param tokenCreationRequestDto
     * @return
     */
    ResponseEntity<ApiResponseDto<TokenDetailsResponseDto>> createTokenAndGetResponseDto(TokenCreationRequestDto tokenCreationRequestDto) throws InvalidInputTokenCreationException;

    /**
     * @param companyId
     * @param companyName
     * @param scope
     * @return
     */
    ResponseEntity<ApiResponseDto<List<TokenDetailsResponseDto>>> getTokenDetails(Integer companyId, String companyName, String scope, boolean requestHasCompanyId, boolean requestHasCompanyName);

    /**
     * @param tokenId
     * @return
     */
    ResponseEntity<ApiResponseDto<Void>> expireTokenByTokenId(Integer tokenId);
}
