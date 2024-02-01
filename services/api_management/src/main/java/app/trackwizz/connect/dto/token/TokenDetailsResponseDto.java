package app.trackwizz.connect.dto.token;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TokenDetailsResponseDto {
    @Schema(hidden = true)
    private Integer tokenId;
    private Integer companyId;
    private String token;
    private String tokenScopes;
    private Timestamp tokenIssueAt;
    private Timestamp tokenExpiryAt;

    public TokenDetailsResponseDto() {
    }

    public TokenDetailsResponseDto(Integer tokenId, Integer companyId, String token, String tokenScopes, Timestamp tokenIssueAt, Timestamp tokenExpiryAt) {
        this.tokenId = tokenId;
        this.companyId = companyId;
        this.token = token;
        this.tokenScopes = tokenScopes;
        this.tokenIssueAt = tokenIssueAt;
        this.tokenExpiryAt = tokenExpiryAt;
    }

    public Integer getTokenId() {
        return tokenId;
    }

    public void setTokenId(Integer tokenId) {
        this.tokenId = tokenId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenScopes() {
        return tokenScopes;
    }

    public void setTokenScopes(String tokenScopes) {
        this.tokenScopes = tokenScopes;
    }

    public Timestamp getTokenIssueAt() {
        return tokenIssueAt;
    }

    public void setTokenIssueAt(Timestamp tokenIssueAt) {
        this.tokenIssueAt = tokenIssueAt;
    }

    public Timestamp getTokenExpiryAt() {
        return tokenExpiryAt;
    }

    public void setTokenExpiryAt(Timestamp tokenExpiryAt) {
        this.tokenExpiryAt = tokenExpiryAt;
    }
}
