package tn.supcom.planthealth.services;

import jakarta.data.repository.Repository;
import jakarta.inject.Inject;
import tn.supcom.planthealth.entities.Grant;
import tn.supcom.planthealth.repositories.GrantRepository;

import java.util.List;

@Repository
public class GrantService {
    @Inject
    GrantRepository grantRepository;

    public Grant findGrantByTenantAndIdentity(Short tenantId, Long identityId) {
        return grantRepository.findBytenantIdAndidentityId(tenantId,identityId);
    }
    public void addGrant(Grant grant) {
        grantRepository.save(grant);
    }

    public List<Grant> getAllGrants() {
        return grantRepository.findAll().toList();
    }
}
