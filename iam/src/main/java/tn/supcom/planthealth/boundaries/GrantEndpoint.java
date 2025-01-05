package tn.supcom.planthealth.boundaries;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import tn.supcom.planthealth.entities.Grant;
import tn.supcom.planthealth.services.GrantService;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GrantEndpoint {
    @Inject
    GrantService grantService;
    public record GrantInput(short id,Short tenantId,Long identityId,String approvedScopes) { }
    @POST
    @Path("/grant")
    public Grant add(GrantInput input) {
        Grant grant = new Grant(input.id(), input.tenantId(), input.identityId(), input.approvedScopes());
        grantService.addGrant(grant);
        return grant;
    }
    @GET
    @Path("/grant")
    public Grant getGrant() {
        Long identityId=2L;
        Short tenantId=3;
        return grantService.findGrantByTenantAndIdentity(tenantId,identityId);
    }
}
