package tn.supcom.planthealth.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Embeddable;
import java.util.Objects;

@Embeddable
public class GrantPK {
    @Column("tenant_id")
    private Short tenantId;
    @Column("identity_id")
    private Long identityId;
    public GrantPK(){
    }
    public GrantPK(Short tenantId, Long identityId) {
        this.tenantId = tenantId;
        this.identityId = identityId;
    }

    public Short getTenantId() {
        return tenantId;
    }

    public void setTenantId(Short tenantId) {
        this.tenantId = tenantId;
    }

    public Long getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Long identityId) {
        this.identityId = identityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrantPK grantPK = (GrantPK) o;
        return tenantId == grantPK.tenantId && identityId == grantPK.identityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, identityId);
    }
}
