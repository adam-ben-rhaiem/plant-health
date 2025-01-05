package tn.supcom.planthealth.repositories;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import tn.supcom.planthealth.entities.Identity;

@Repository
public interface IdentityRepository extends CrudRepository <Identity, Long> {
    Identity findByUsername(String username);
}
