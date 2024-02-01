package app.trackwizz.connect.controller;

import app.trackwizz.connect.dto.ApiResponseDto;
import app.trackwizz.connect.dto.token.TokenCreationRequestDto;
import app.trackwizz.connect.dto.token.TokenDetailsResponseDto;
import app.trackwizz.connect.exception.InvalidInputTokenCreationException;
import app.trackwizz.connect.helper.log.LogHelper;
import app.trackwizz.connect.service.token_details.ITokenDetailsService;
import app.trackwizz.connect.service.valid_token_scopes.IValidTokenScopesDbService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/token")
public class TokenDetailsController {
    private final ITokenDetailsService ITokenDetailsService;
    private final IValidTokenScopesDbService IValidTokenScopesDbService;
    private final Logger log4j = LogManager.getLogger(TokenDetailsController.class);

    public TokenDetailsController(ITokenDetailsService ITokenDetailsService, IValidTokenScopesDbService IValidTokenScopesDbService) {
        this.ITokenDetailsService = ITokenDetailsService;
        this.IValidTokenScopesDbService = IValidTokenScopesDbService;
    }

    @GetMapping()
    @Operation(description = "Gets token(s) details by either company id or by company name or by scope")
    public ResponseEntity<ApiResponseDto<List<TokenDetailsResponseDto>>> getTokenDetails(@RequestParam(required = false) Integer companyId,
                                                                                         @RequestParam(required = false) String companyName,
                                                                                         @RequestParam(required = false) String scope) {
        boolean requestHasCompanyId = !ObjectUtils.isEmpty(companyId);
        boolean requestHasCompanyName = !ObjectUtils.isEmpty(companyName);
        boolean requestHasScope = !ObjectUtils.isEmpty(scope);

        if ((!requestHasCompanyId && !requestHasCompanyName && !requestHasScope) ||
                (requestHasCompanyId && requestHasCompanyName) || (requestHasCompanyId && requestHasScope) || (requestHasCompanyName && requestHasScope))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(null, "Invalid input. Only one parameter should be passed"));

        try {
            return ITokenDetailsService.getTokenDetails(companyId, companyName, scope, requestHasCompanyId, requestHasCompanyName);
        } catch (Exception e) {
            log4j.error("Error while fetching token details" + LogHelper.getExceptionDetails(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error(null, "Error while fetching token details"));
        }
    }

    @GetMapping("/scopes")
    @Operation(description = "Gets list of all valid scopes")
    public ResponseEntity<ApiResponseDto<List<String>>> getAllValidTokens() {
        try {
            List<String> scopes = IValidTokenScopesDbService.getAllValidTokenScopes();
            log4j.info("Fetched list of valid scopes");
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(scopes, "Fetched list of valid scopes"));
        } catch (Exception e) {
            log4j.error("Error while fetching list of valid scopes" + LogHelper.getExceptionDetails(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error(null, "Error while fetching list of valid scopes"));
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Creates token for given company id and given list of scopes")
    public ResponseEntity<ApiResponseDto<TokenDetailsResponseDto>> createTokenForCompaniesViaJsonBody(@RequestBody TokenCreationRequestDto tokenCreationRequestDto) {
        return createTokenForCompanies(tokenCreationRequestDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Creates token for given company id and given list of scopes")
    public ResponseEntity<ApiResponseDto<TokenDetailsResponseDto>> createTokenForCompaniesViaFormData(@ModelAttribute TokenCreationRequestDto tokenCreationRequestDto) {
        return createTokenForCompanies(tokenCreationRequestDto);
    }

    private ResponseEntity<ApiResponseDto<TokenDetailsResponseDto>> createTokenForCompanies(TokenCreationRequestDto tokenCreationRequestDto) {
        try {
            if (ObjectUtils.isEmpty(tokenCreationRequestDto.getCompanyId()))
                throw new InvalidInputTokenCreationException("Empty companyId. Please provide valid companyId");
            if (ObjectUtils.isEmpty(tokenCreationRequestDto.getScopes()))
                throw new InvalidInputTokenCreationException("Empty scopes. Please provide valid scopes");
            return ITokenDetailsService.createTokenAndGetResponseDto(tokenCreationRequestDto);
        } catch (InvalidInputTokenCreationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(null, e.getMessage()));
        } catch (Exception e) {
            log4j.error("Error while creating token for companyId: " + tokenCreationRequestDto.getCompanyId() + " with scopes: " + tokenCreationRequestDto.getScopes() + LogHelper.getExceptionDetails(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error(null, "Error while creating token"));
        }
    }

    @DeleteMapping()
    @Operation(description = "Deletes token for given token id")
    public ResponseEntity<ApiResponseDto<Void>> expireTokenByTokenId(@RequestParam Integer tokenId) {
        try {
            return ITokenDetailsService.expireTokenByTokenId(tokenId);
        } catch (Exception e) {
            log4j.error("Error while invalidating token tokenId: " + tokenId + LogHelper.getExceptionDetails(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error(null, "Error while invalidating token tokenId: " + tokenId));
        }
    }
}
