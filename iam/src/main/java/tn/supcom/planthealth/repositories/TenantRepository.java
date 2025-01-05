package tn.supcom.planthealth.repositories;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import tn.supcom.planthealth.entities.Tenant;

@Repository
public interface TenantRepository extends CrudRepository<Tenant, Short> {
    Tenant findByName(String name);
}


