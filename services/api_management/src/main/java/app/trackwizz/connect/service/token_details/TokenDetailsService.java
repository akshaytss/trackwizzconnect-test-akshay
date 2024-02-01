package app.trackwizz.connect.service.token_details;

import app.trackwizz.connect.constant.JwtConstants;
import app.trackwizz.connect.constant.MessageConstants;
import app.trackwizz.connect.dto.ApiResponseDto;
import app.trackwizz.connect.dto.token.TokenCreationRequestDto;
import app.trackwizz.connect.dto.token.TokenDetailsResponseDto;
import app.trackwizz.connect.exception.InvalidInputTokenCreationException;
import app.trackwizz.connect.factory.token_details.ITokenDetailsFactory;
import app.trackwizz.connect.model.CompanyDetails;
import app.trackwizz.connect.model.JwtToken;
import app.trackwizz.connect.model.TokenDetails;
import app.trackwizz.connect.service.company_details.db.ICompanyDetailsDbService;
import app.trackwizz.connect.service.token.ITokenService;
import app.trackwizz.connect.service.token_details.db.ITokenDetailsDbService;
import app.trackwizz.connect.service.valid_token_scopes.IValidTokenScopesDbService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public class TokenDetailsService implements ITokenDetailsService {
    private final ICompanyDetailsDbService ICompanyDetailsDbService;
    private final app.trackwizz.connect.service.token_details.db.ITokenDetailsDbService ITokenDetailsDbService;
    private final IValidTokenScopesDbService IValidTokenScopesDbService;
    private final ITokenService ITokenService;
    private final ITokenDetailsFactory ITokenDetailsFactory;
    private final Logger log4j = LogManager.getLogger(TokenDetailsService.class);

    public TokenDetailsService(ICompanyDetailsDbService ICompanyDetailsDbService, ITokenDetailsDbService ITokenDetailsDbService, IValidTokenScopesDbService IValidTokenScopesDbService, ITokenService ITokenService, ITokenDetailsFactory ITokenDetailsFactory) {
        this.ICompanyDetailsDbService = ICompanyDetailsDbService;
        this.ITokenDetailsDbService = ITokenDetailsDbService;
        this.IValidTokenScopesDbService = IValidTokenScopesDbService;
        this.ITokenService = ITokenService;
        this.ITokenDetailsFactory = ITokenDetailsFactory;
    }

    @Override
    public ResponseEntity<ApiResponseDto<TokenDetailsResponseDto>> createTokenAndGetResponseDto(TokenCreationRequestDto tokenCreationRequestDto) throws InvalidInputTokenCreationException {
        TokenDetailsResponseDto tokenDetailsResponseDto = new TokenDetailsResponseDto();

        CompanyDetails companyDetails = new CompanyDetails(tokenCreationRequestDto.getCompanyId());
        tokenDetailsResponseDto.setCompanyId(companyDetails.getCompanyId());

        // verify if company exists
        Optional<CompanyDetails> companyDetailsDb = ICompanyDetailsDbService.getCompanyDetails(companyDetails.getCompanyId());
        if (companyDetailsDb.isEmpty() || companyDetailsDb.get().isCompanyDeleted())
            throw new InvalidInputTokenCreationException("Invalid companyId. No matching company in database");

        companyDetails.setCompanyName(companyDetailsDb.get().getCompanyName());
        List<String> validTokenScopesList = IValidTokenScopesDbService.getAllValidTokenScopes();
        List<String> requestScopes = tokenCreationRequestDto.getScopes();

        // verify if scopes exists
        if (!(new HashSet<>(validTokenScopesList).containsAll(requestScopes)))
            throw new InvalidInputTokenCreationException("Invalid scopes. Requested scopes did not match valid list of scopes in database");

        List<TokenDetails> tokenDetailsDbList = ITokenDetailsDbService.getTokenDetailsByCompanyId(companyDetails.getCompanyId());
        for (TokenDetails tokenDetails : tokenDetailsDbList) {
            // check if a token with same scope exists and is still valid
            HashSet<String> tokenScopesList = new HashSet<>(Arrays.stream(tokenDetails.getTokenScopes().split("\\" + JwtConstants.TOKEN_SCOPES_DELIMITER)).toList());
            HashSet<String> requestScopeList = new HashSet<>(requestScopes);
            if (tokenScopesList.equals(requestScopeList) && tokenDetails.getTokenExpiryAt().after(Timestamp.from(Instant.now()))) {
                log4j.info("Token already exists with requestScopes tokenId: " + tokenDetails.getTokenId());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseDto.error(tokenDetailsResponseDto, "Token already exists with requestScopes tokenId: " + tokenDetails.getTokenId()));
            }
        }

        // create token and save to db
        JwtToken jwtToken = ITokenService.createToken(companyDetails.getCompanyId(), requestScopes);
        TokenDetails createdTokenDetails = ITokenDetailsDbService.saveTokenDetails(ITokenDetailsFactory.mapJwtTokenToTokenDetails(jwtToken));
        tokenDetailsResponseDto = ITokenDetailsFactory.getTokenDetailsResponseDto(createdTokenDetails);

        log4j.info("Token created TokenId: " + tokenDetailsResponseDto.getTokenId() + " for companyId: " + tokenDetailsResponseDto.getCompanyId() + " with scopes: " + tokenDetailsResponseDto.getTokenScopes());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(tokenDetailsResponseDto, MessageConstants.TOKEN_CREATED_SUCCESSFULLY));
    }

    @Override
    public ResponseEntity<ApiResponseDto<List<TokenDetailsResponseDto>>> getTokenDetails(Integer companyId, String companyName, String scope, boolean requestHasCompanyId, boolean requestHasCompanyName) {
        if (requestHasCompanyId) { // find by companyId
            if (ICompanyDetailsDbService.getCompanyDetails(companyId).isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(null, MessageConstants.COMPANY_DETAILS_DOES_NOT_EXISTS + " companyId: " + companyId));

            List<TokenDetails> tokenDetails = ITokenDetailsDbService.getTokenDetailsByCompanyId(companyId);
            if (!tokenDetails.isEmpty()) {
                log4j.info(MessageConstants.TOKEN_RETRIEVED_SUCCESSFULLY + " for companyId: " + companyId);
                return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(
                        ITokenDetailsFactory.getTokenDetailsResponseDto(tokenDetails),
                        MessageConstants.TOKEN_RETRIEVED_SUCCESSFULLY));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error(null, MessageConstants.NO_TOKEN_FOUND + " for companyId: " + companyId));

        } else if (requestHasCompanyName) { // find by companyName
            Optional<CompanyDetails> companyDetails = ICompanyDetailsDbService.getCompanyDetails(companyName);
            if (companyDetails.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(null, MessageConstants.COMPANY_DETAILS_DOES_NOT_EXISTS + " companyName: " + companyName));

            List<TokenDetails> tokenDetails = ITokenDetailsDbService.getTokenDetailsByCompanyId(companyDetails.get().getCompanyId());
            if (!tokenDetails.isEmpty()) {
                log4j.info(MessageConstants.TOKEN_RETRIEVED_SUCCESSFULLY + " for companyName: " + companyName);
                return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(
                        ITokenDetailsFactory.getTokenDetailsResponseDto(tokenDetails),
                        MessageConstants.TOKEN_RETRIEVED_SUCCESSFULLY));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error(null, MessageConstants.NO_TOKEN_FOUND + " for companyName: " + companyName));

        } else { // find by scopes
            if (IValidTokenScopesDbService.findScope(scope).isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(null, "Invalid scope: " + scope + " No such scope exists in database"));

            List<TokenDetails> tokenDetails = ITokenDetailsDbService.getTokenDetailsByScope(scope);
            if (!tokenDetails.isEmpty()) {
                log4j.info(MessageConstants.TOKEN_RETRIEVED_SUCCESSFULLY + " with scope: " + scope);
                return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(
                        ITokenDetailsFactory.getTokenDetailsResponseDto(tokenDetails),
                        MessageConstants.TOKEN_RETRIEVED_SUCCESSFULLY));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error(null, MessageConstants.NO_TOKEN_FOUND + " containing scope: " + scope));
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<Void>> expireTokenByTokenId(Integer tokenId) {
        try {
            Optional<TokenDetails> tokenDetails = ITokenDetailsDbService.getTokenDetailsById(tokenId);
            if(tokenDetails.isEmpty())
                throw new EntityNotFoundException();

            if(tokenDetails.get().getTokenExpiryAt().before(Timestamp.from(Instant.now()))){
                log4j.info("Token tokenId: " + tokenId + " was already expired at "+tokenDetails.get().getTokenExpiryAt());
                return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(null, "Token tokenId: " + tokenId + " is already expired"));
            }

            ITokenDetailsDbService.setTokenExpiryToNowByTokenId(tokenId);
            log4j.info("Token tokenId: " + tokenId + " invalidated");
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(null, "Token tokenId: " + tokenId + " invalidated"));
        } catch (EntityNotFoundException e) {
            log4j.info(MessageConstants.NO_TOKEN_FOUND + " for tokenId: " + tokenId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error(null, MessageConstants.NO_TOKEN_FOUND + " for tokenId: " + tokenId));
        }
    }
}
