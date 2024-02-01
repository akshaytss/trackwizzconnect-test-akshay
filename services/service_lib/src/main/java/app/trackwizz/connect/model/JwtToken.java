package app.trackwizz.connect.model;

import java.time.Instant;
import java.util.List;

public class JwtToken {
    private String token;
    private Integer companyId;
    private List<String> scopes;
    private Instant creationTime;
    private Instant expiryTime;
    private String issuingEnvironment;

    public JwtToken() {
    }

    public JwtToken(String token, Integer companyId, List<String> scopes, Instant creationTime, Instant expiryTime, String issuingEnvironment) {
        this.token = token;
        this.companyId = companyId;
        this.scopes = scopes;
        this.creationTime = creationTime;
        this.expiryTime = expiryTime;
        this.issuingEnvironment = issuingEnvironment;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public Instant getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Instant expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getIssuingEnvironment() {
        return issuingEnvironment;
    }

    public void setIssuingEnvironment(String issuingEnvironment) {
        this.issuingEnvironment = issuingEnvironment;
    }
}
