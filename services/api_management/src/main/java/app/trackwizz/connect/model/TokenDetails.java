package app.trackwizz.connect.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "token_details")
public class TokenDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Integer tokenId;
    @Column(name = "token_string", columnDefinition = "TEXT")
    private String tokenString;
    @Column(name = "company_id")
    private Integer companyId;
    @Column(name = "token_scopes", columnDefinition = "TEXT")
    private String tokenScopes;
    @Column(name = "token_issued_at")
    private Timestamp tokenIssueAt;
    @Column(name = "token_expiry_at")
    private Timestamp tokenExpiryAt;
    @Column(name = "token_manually_expired_by_user")
    @Schema(hidden = true)
    private String tokenManuallyExpiredByUser;
    @Column(name = "environment")
    @Schema(hidden = true)
    private String environment;

    public TokenDetails() {
    }

    public TokenDetails(String tokenString, Integer companyId, String tokenScopes, Timestamp tokenIssueAt, Timestamp tokenExpiryAt, String environment) {
        this.tokenString = tokenString;
        this.companyId = companyId;
        this.tokenScopes = tokenScopes;
        this.tokenIssueAt = tokenIssueAt;
        this.tokenExpiryAt = tokenExpiryAt;
        this.tokenManuallyExpiredByUser = "System";
        this.environment = environment;
    }

    public Integer getTokenId() {
        return tokenId;
    }

    public void setTokenId(Integer tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public String getTokenManuallyExpiredByUser() {
        return tokenManuallyExpiredByUser;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setTokenManuallyExpiredByUser(String tokenManuallyExpiredByUser) {
        this.tokenManuallyExpiredByUser = tokenManuallyExpiredByUser;
    }

    public String getEnvironment() {
        return environment;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
