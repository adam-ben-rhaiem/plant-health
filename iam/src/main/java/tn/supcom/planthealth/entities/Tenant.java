package tn.supcom.planthealth.entities;

import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import jakarta.nosql.Column;

@Entity("tenants")
public class Tenant {
    @Id
    private short id;

    @Column("tenant_id")
    private String name;
    @Column("tenant_secret")
    private String secret;
    @Column("redirect_uri")
    private String redirectUri;

    @Column("allowed_roles")
    private Long allowedRoles;

    @Column("required_scopes")
    private String requiredScopes; // "mail,phone_number"


    @Column("supported_grant_types")
    private String supportedGrantTypes; // "authorization_code,refresh_code"
    public Tenant() {}
    public Tenant(short id, String name, String secret, String s, Long aLong, String s1, String s2) {
        this.id = id;
        this.name = name;
        this.secret = secret;
        this.redirectUri = s;
        this.allowedRoles = aLong;
        this.requiredScopes = s1;
        this.supportedGrantTypes = s2;
    }


    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public Long getAllowedRoles() {
        return allowedRoles;
    }

    public void setAllowedRoles(Long allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    public String getRequiredScopes() {
        return requiredScopes;
    }

    public void setRequiredScopes(String requiredScopes) {
        this.requiredScopes = requiredScopes;
    }

    public String getSupportedGrantTypes() {
        return supportedGrantTypes;
    }

    public void setSupportedGrantTypes(String supportedGrantTypes) {
        this.supportedGrantTypes = supportedGrantTypes;
    }
}
