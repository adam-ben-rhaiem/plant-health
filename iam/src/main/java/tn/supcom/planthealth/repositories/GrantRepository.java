package tn.supcom.planthealth.repositories;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import tn.supcom.planthealth.entities.Grant;

@Repository
public interface GrantRepository extends CrudRepository<Grant, Integer> {
    public Grant findBytenantIdAndidentityId(Short tenantId, Long identityId);
}
