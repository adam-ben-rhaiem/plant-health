package tn.supcom.planthealth.boundaries;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import tn.supcom.planthealth.entities.Identity;
import tn.supcom.planthealth.security.Argon2Utility;
import tn.supcom.planthealth.services.IdentityService;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Path("/")
public class IdentityEndpoint {

    @Inject
    IdentityService identityService;
    public record IdentityInput(long id, String username, String password, String providedScopes, Long roles) { }

    @GET
    @Path("register")
    @Produces(MediaType.TEXT_HTML)
    public Response register() {
        StreamingOutput stream = output -> {
            try (InputStream is = Objects.requireNonNull(getClass().getResource("/register.html")).openStream()){
                output.write(is.readAllBytes());
            }
        };
        return Response.ok(stream).build();
    }

    @POST
    @Path("auth/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response register (@FormParam("username") String username,@FormParam("password") String password){
        String providedScopes = "resource.read resource.write";
        Long roles = 2L;
        Argon2Utility utility = new Argon2Utility();
        String hash= utility.generate(password.toCharArray());
        Identity identity = new Identity(username,hash,providedScopes,roles);
        identityService.addIdentity(identity);

        return Response.ok().build();

    }

    @GET
    @Path("identities")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Identity> listAll() {
        return identityService.getAllIdentities();
    }

    @GET
    @Path("identity")
    @Produces(MediaType.APPLICATION_JSON)
    public Identity listOne() {
        return identityService.findIdentityByUsername("amir");
    }

}
