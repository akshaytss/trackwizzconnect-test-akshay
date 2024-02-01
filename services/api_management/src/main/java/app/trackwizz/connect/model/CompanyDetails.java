package app.trackwizz.connect.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Table(name = "company_details")
public class CompanyDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Integer companyId;
    @Column(name = "company_name", nullable = false, unique = true)
    @Size(min = 1, max = 255)
    @Schema(minLength = 1, maxLength = 255)
    private String companyName;
    @Column(name = "company_email")
    @Size(min = 1, max = 255)
    @Schema(minLength = 1, maxLength = 255)
    @Email
    private String companyEmail;
    @Column(name = "is_company_deleted")
    private boolean companyDeleted = false;

    public CompanyDetails() {
    }

    public CompanyDetails(Integer companyId) {
        this.companyId = companyId;
    }

    public CompanyDetails(Integer companyId, String companyName, String companyEmail, boolean companyDeleted) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyDeleted = companyDeleted;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public boolean isCompanyDeleted() {
        return companyDeleted;
    }

    public void setCompanyDeleted(boolean companyDeleted) {
        this.companyDeleted = companyDeleted;
    }
}
