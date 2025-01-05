package tn.supcom.planthealth.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.util.UUID;

@Entity("identities")
public class Identity {
    @Id
    private long id;

    @Column
    private String username;

    public String getPassword() {
        return password;
    }

    @Column
    private String password;

    @Column("provided_scopes")
    private String providedScopes;

    @Column
    private Long roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRoles() {
        return roles;
    }

    public void setRoles(Long roles) {
        this.roles = roles;
    }

    public String getProvidedScopes() {
        return providedScopes;
    }

    public void setProvidedScopes(String providedScopes) {
        this.providedScopes = providedScopes;
    }
    public Identity (){}
    public Identity(String username, String password, String providedScopes, Long roles) {
        this.id = UUID.randomUUID().getMostSignificantBits();
        this.username = username;
        this.password = password;
        this.providedScopes = providedScopes;
        this.roles = roles;
    }

}
