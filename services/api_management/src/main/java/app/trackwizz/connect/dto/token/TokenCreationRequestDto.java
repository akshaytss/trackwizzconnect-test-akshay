package app.trackwizz.connect.dto.token;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class TokenCreationRequestDto {
    private Integer companyId;
    private List<String> scopes;

    public TokenCreationRequestDto() {
    }

    public TokenCreationRequestDto(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public List<String> getScopes() {
        return CollectionUtils.isEmpty(scopes) ? new ArrayList<>() : scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }
}
