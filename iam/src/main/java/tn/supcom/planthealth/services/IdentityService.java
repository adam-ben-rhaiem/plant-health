package tn.supcom.planthealth.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tn.supcom.planthealth.controllers.Role;
import tn.supcom.planthealth.entities.Identity;
import tn.supcom.planthealth.repositories.IdentityRepository;

import java.util.HashSet;
import java.util.List;

@ApplicationScoped
public class IdentityService {
    @Inject
    IdentityRepository identityRepository;


    public Identity findIdentityByUsername(String userName) {
        return identityRepository.findByUsername(userName);
    }
    public void addIdentity(Identity identity) {
        identityRepository.save(identity);
    }

    public List<Identity> getAllIdentities() {
        return identityRepository.findAll().toList();
    }
    public String[] getRoles(String username){

        Identity subject = identityRepository.findByUsername(username);
        var roles = subject.getRoles();

        var ret = new HashSet<String>();
        for(Role role:Role.values()){
            if((roles&role.getValue())!=0L){
                String value = Role.byValue(role.getValue());
                if (value==null){
                    continue;
                }
                ret.add(value);
            }
        }
        return ret.toArray(new String[0]);
    }

}
