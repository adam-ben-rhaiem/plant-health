package tn.supcom.planthealth.boundaries;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.ConfigProvider;
import tn.supcom.planthealth.security.AuthorizationCode;
import tn.supcom.planthealth.security.JwtManager;
import tn.supcom.planthealth.services.IdentityService;

import java.security.GeneralSecurityException;
import java.util.Arrays;

//http://localhost:8080/iam-1.0/rest-iam/oauth/token/authorization_code=amir&code_verifier=jribi
@Path("/oauth/token")
public class OAuthTokenEndpoint {
    @Inject
    IdentityService identityService;

    @EJB
    private JwtManager jwtManager;


    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateToken(@QueryParam("authorization_code") String authorizationCode,
                                  @QueryParam("code_verifier")String codeVerifier){
        //validate authorization code and code verifier according to OAuth 2.0 Authorization Flow with PKCE
        //Extract tenantId and subjectId from authorization code
        //Extract subject, approvedScopes and roles from database using tenantId and subjectId
//        System.out.println(authorizationCode);
//        System.out.println(codeVerifier);
//        JsonObject obj= Json.createObjectBuilder()
//                .add("authorization_code",authorizationCode)
//                .add("code_verifier",codeVerifier)
//                .build();
//        return Response.ok(obj).build();
        try{
              AuthorizationCode decoded  = AuthorizationCode.decode(authorizationCode,codeVerifier);
              System.out.println(decoded);

              assert decoded != null;
//            var tenantName=decoded.tenantName();
//
//            String accessToken = jwtManager.generateToken(tenantName, decoded.identityUsername(), decoded.approvedScopes(),identityService.getRoles(decoded.identityUsername()));
//
//
//            return Response.ok(Json.createObjectBuilder()
//                            .add("token_type", "Bearer")
//                            .add("access_token", accessToken)
//                            .add("expires_in", ConfigProvider.getConfig().getValue("jwt.lifetime.duration",Integer.class))
//                            .build())
//                    .header("Cache-Control", "no-store")
//                    .header("Pragma", "no-cache")
//                    .build();

        }
        catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
       catch (WebApplicationException e) {
            return e.getResponse();
        } catch (Exception e) {
            return responseError("Invalid_request", "Can't get token", Response.Status.INTERNAL_SERVER_ERROR);
        }
        var tenantId = "watermarking123";
        var subject = "john.doe";
        var approvedScopes = "resource:read,resource:write";
        var roles = new String[]{"Moderator","Client"};
        var token = jwtManager.generateToken(tenantId,subject,approvedScopes,roles);
        return Response
                .ok(Json.createObjectBuilder()
                        .add("access-token", token)
                        .add("token_type", "Bearer")
                        .add("expires_in", 1020)
                        .build())
                .build();


//        var tenantId = "watermarking123";
//        var subject = "john.doe";
//        var approvedScopes = "resource:read,resource:write";
//        var roles = new String[]{"Moderator","Client"};
//        var token = jwtManager.generateToken(tenantId,subject,approvedScopes,roles);
//        return Response
//                .ok(Json.createObjectBuilder()
//                        .add("access-token", token)
//                        .add("token_type", "Bearer")
//                        .add("expires_in", 1020)
//                        .build())
//                .build();
    }
    private Response responseError(String error, String errorDescription, Response.Status status) {
        JsonObject errorResponse = Json.createObjectBuilder()
                .add("error", error)
                .add("error_description", errorDescription)
                .build();
        return Response.status(status)
                .entity(errorResponse).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response token(@FormParam("authorization_code") String authCode,
                          @FormParam("code_verifier")String codeVerifier){
        System.out.println("authorization code is : "+ authCode);
        System.out.println("code verifier is : " + codeVerifier);
        try{
            AuthorizationCode decoded  = AuthorizationCode.decode(authCode,codeVerifier);
            assert decoded!=null;
            System.out.println("decoded is" + decoded);
            System.out.println("tenantName is " + decoded.tenantName());
            System.out.println("identityUsername is " + decoded.identityUsername());
            System.out.println("approvedScopes is " + decoded.approvedScopes());
            System.out.println(Arrays.toString(identityService.getRoles(decoded.identityUsername())));
            String accessToken = jwtManager.generateToken(decoded.tenantName(), decoded.identityUsername(), decoded.approvedScopes(),identityService.getRoles(decoded.identityUsername()));
            return Response.ok(Json.createObjectBuilder()
                            .add("token_type", "Bearer")
                            .add("access_token", accessToken)
                            .add("expires_in", ConfigProvider.getConfig().getValue("jwt.lifetime.duration",Integer.class))
                            .build())
                    .header("Cache-Control", "no-store")
                    .header("Pragma", "no-cache")
                    .build();
//            var tenantId = "watermarking123";
//           var subject = "john.doe";
//           var approvedScopes = "resource:read,resource:write";
//           var roles = new String[]{"Moderator","Client"};
//           String accessToken = jwtManager.generateToken(tenantId,subject,approvedScopes,roles);
//           return Response.ok(Json.createObjectBuilder()
//                            .add("token_type", "Bearer")
//                            .add("access_token", accessToken)
//                            .add("expires_in", ConfigProvider.getConfig().getValue("jwt.lifetime.duration",Integer.class))
//                            .build())
//                    .header("Cache-Control", "no-store")
//                    .header("Pragma", "no-cache")
//                    .build();
        }
        catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (WebApplicationException e) {
            return e.getResponse();
        } catch (Exception e) {
            return responseError("Invalid_request", "Can't get token", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
