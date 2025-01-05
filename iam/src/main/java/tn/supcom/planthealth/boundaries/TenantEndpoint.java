package tn.supcom.planthealth.boundaries;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import tn.supcom.planthealth.entities.Tenant;
import tn.supcom.planthealth.services.TenantService;

import java.util.List;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TenantEndpoint {
    public record TenantInput(short id, String name,String secret,String redirectUri,Long allowedRoles,String requiredScopes,String supportedGrantTypes) { }

    @Inject
    TenantService tenantService;

    @GET
    @Path("/tenant")
    public Tenant getTenant(){
        String clientId="plant-health";
        System.out.println(clientId);
        return tenantService.findTenantByName(clientId);
    }
    @POST
    @Path("/tenant")
    public Tenant add(TenantInput input) {
        Tenant tenant = new Tenant(input.id(), input.name(), input.secret(), input.redirectUri(), input.allowedRoles(), input.requiredScopes(), input.supportedGrantTypes());
        tenantService.addTenant(tenant);
        return tenant;
    }
    @GET
    @Path(("/tenants"))
    public List<Tenant> listAll() {
        return tenantService.getAllTenants();
    }
}
