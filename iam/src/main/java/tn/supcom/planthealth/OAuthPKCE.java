package tn.supcom.planthealth;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import jakarta.ws.rs.core.UriInfo;

import java.io.InputStream;
import java.util.Objects;

@RequestScoped
@Path("/")
public class OAuthPKCE {

    @GET
    public Response getPkce() {
        System.out.println("getPkce");
        StreamingOutput stream = output -> {
            try (InputStream is = Objects.requireNonNull(getClass().getResource("/signin.html")).openStream()){
                output.write(is.readAllBytes());
            }
        };
        return Response.ok(stream).build();
    }

    @GET
    @Path("oauth/callback")
    public Response authenticate(@Context UriInfo uriInfo) {
        var params = uriInfo.getQueryParameters();
        System.out.println(params);
        StreamingOutput stream = output -> {
            try (InputStream is = Objects.requireNonNull(getClass().getResource("/redirect.html")).openStream()){
                output.write(is.readAllBytes());
            }
        };
        return Response.ok(stream).build();

    }
}
