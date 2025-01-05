package tn.supcom.planthealth.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.time.LocalDateTime;

@Entity("issued_grants")
public class Grant {
    private Tenant tenant;
    private Identity identity;

    public short getId() {
        return id;
    }

    @Id
    private short id;

    public Short getTenantId() {
        return tenantId;
    }

    public void setTenantId(Short tenantId) {
        this.tenantId = tenantId;
    }

    @Column("tenant_id")
    private Short tenantId;

    public Long getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Long identityId) {
        this.identityId = identityId;
    }

    @Column("identity_id")
    private Long identityId;

    @Column("approved_scopes")
    private String approvedScopes;

    @Column("issuance_date_time")
    private LocalDateTime issuanceDateTime;
    public Grant(){}

    public Grant(short id,Short tenantId,Long identityId,String approvedScopes){
        this.id=id;
        this.tenantId=tenantId;
        this.identityId=identityId;
        this.approvedScopes=approvedScopes;
        this.issuanceDateTime=LocalDateTime.now();
    }



//    public GrantPK getId() {
//        return id;
//    }

    public void setId(short id) {
        this.id = id;
    }



    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public String getApprovedScopes() {
        return approvedScopes;
    }

    public void setApprovedScopes(String approvedScopes) {
        this.approvedScopes = approvedScopes;
    }

    public LocalDateTime getIssuanceDateTime() {
        return issuanceDateTime;
    }

    public void setIssuanceDateTime(LocalDateTime issuanceDateTime) {
        this.issuanceDateTime = issuanceDateTime;
    }

}
