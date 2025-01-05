package tn.supcom.planthealth.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tn.supcom.planthealth.entities.Tenant;
import tn.supcom.planthealth.repositories.TenantRepository;

import java.util.List;

@ApplicationScoped
public class TenantService {
    @Inject
    TenantRepository tenantRepository;

    public Tenant findTenantByName(String tenantName) {
        return tenantRepository.findByName(tenantName);
    }
    public void addTenant(Tenant tenant) {
        tenantRepository.save(tenant);
    }

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll().toList();
    }

}
