package app.trackwizz.connect.model;

import javax.persistence.*;

@Entity
@Table(name = "valid_token_scopes")
public class ValidTokenScopes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scope_id")
    private Integer id;
    @Column(nullable = false, name = "scope")
    private String scope;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
