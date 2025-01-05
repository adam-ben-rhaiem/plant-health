package tn.supcom.planthealth.boundaries;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import tn.supcom.planthealth.entities.Identity;
import tn.supcom.planthealth.security.AuthorizationCode;
import tn.supcom.planthealth.services.IdentityService;
import tn.supcom.planthealth.services.TenantService;
import tn.supcom.planthealth.security.Argon2Utility;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Path("/")
public class OAuthAuthorizationEndpoint {
    public static final String CHALLENGE_RESPONSE_COOKIE_ID = "signInId";
    @Inject
    TenantService tenantService;
    @Inject
    IdentityService identityService;
    //@Inject
   // private Logger logger;
    @GET
    @Path("authorize")
    public Response authorize(@Context UriInfo uriInfo) {
        System.out.println(uriInfo);
        var params = uriInfo.getQueryParameters();
        System.out.println(params);
        // 1. Check tenant
        var clientId = params.getFirst("client_id");
        if (clientId == null || clientId.isEmpty()) {
            return informUserAboutError("Invalid client_id :" + clientId);
        }

        var tenant = tenantService.findTenantByName(clientId);
        if (tenant == null) {
            return informUserAboutError("Invalid client_id :" + clientId);
        }
        //2. Client Authorized Grant Type : Authorization Code
        if (tenant.getSupportedGrantTypes() != null && !tenant.getSupportedGrantTypes().contains("authorization_code")) {
            return informUserAboutError("Authorization Grant type, authorization_code, is not allowed for this tenant :" + clientId);
        }

        //3. redirectUri
        String redirectUri = params.getFirst("redirect_uri");
        if (tenant.getRedirectUri() != null && !tenant.getRedirectUri().isEmpty()) {
            if (redirectUri != null && !redirectUri.isEmpty() && !tenant.getRedirectUri().equals(redirectUri)) {
                return informUserAboutError("redirect_uri is pre registred and should match");
            }
            redirectUri = tenant.getRedirectUri();
        } else {
            if (redirectUri == null || redirectUri.isEmpty()) {
                return informUserAboutError("redirect_uri is not pre-registred and should be provided");
            }
        }

        //4. response_type : code
        String responseType = params.getFirst("response_type");
        if (!"code".equals(responseType) && !"token".equals(responseType)) {
            String error = "invalid_grant :" + responseType + ", response_type params should be code or token:";
            return informUserAboutError(error);
        }

        //5. check scope :  resource.read resource.write
        String requestedScope = params.getFirst("scope");
        if (requestedScope == null || requestedScope.isEmpty()) {
            requestedScope = tenant.getRequiredScopes();
        }
        //6. code_challenge_method must be S256
        String codeChallengeMethod = params.getFirst("code_challenge_method");
        if(codeChallengeMethod==null || !codeChallengeMethod.equals("S256")){
            String error = "invalid_grant :" + codeChallengeMethod + ", code_challenge_method must be 'S256'";
            return informUserAboutError(error);
        }


        StreamingOutput stream = output -> {
            try (InputStream is = Objects.requireNonNull(getClass().getResource("/login.html")).openStream()){
                output.write(is.readAllBytes());
            }
        };
        return Response.ok(stream).location(uriInfo.getBaseUri().resolve("/login/authorization"))
                .cookie(new NewCookie.Builder(CHALLENGE_RESPONSE_COOKIE_ID)
                        .httpOnly(true).secure(true).sameSite(NewCookie.SameSite.STRICT).value(tenant.getName()+"#"+requestedScope+"$"+redirectUri).build()).build();
    }

    @POST
    @Path("/login/authorization")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response login(@CookieParam(CHALLENGE_RESPONSE_COOKIE_ID) Cookie cookie,
                          @FormParam("username")String username,
                          @FormParam("password")String password,
                          @Context UriInfo uriInfo) throws Exception {
        Identity identity = identityService.findIdentityByUsername(username);
        // check(hashPassword,password)
        //System.out.println(identity);
        System.out.println("username is " + username + " and " + "password is " + password);
        if (Argon2Utility.check(identity.getPassword(),password.toCharArray())) {
            //logger.info("Authenticated identity:"+username);
            System.out.println("Authenticated identity:"+username);
            MultivaluedMap<String, String> params = uriInfo.getQueryParameters();
            System.out.println(params);
            String grantGetApprovedScopes = "resource.read resource.write"; //grant.get().getApprovedScopes()
            String redirectURI = buildActualRedirectURI(
                    cookie.getValue().split("\\$")[1],params.getFirst("response_type"),
                    cookie.getValue().split("#")[0],
                    username,
                    checkUserScopes(grantGetApprovedScopes,cookie.getValue().split("#")[1].split("\\$")[0])
                    ,params.getFirst("code_challenge"),params.getFirst("state")
            );
            return Response.seeOther(UriBuilder.fromUri(redirectURI).build()).build();
        }
        return Response.ok().build();

    }

//            JsonObject obj = Json.createObjectBuilder()
//                    .add("clientId", 1)
//                    .add("codeChallenge", 2)
//                    .build();
//        return Response.ok(obj).build();
//    }
    private String buildActualRedirectURI(String redirectUri,String responseType,String clientId,String userId,String approvedScopes,String codeChallenge,String state) throws Exception {
        StringBuilder sb = new StringBuilder(redirectUri);
        if ("code".equals(responseType)) {
            AuthorizationCode authorizationCode = new AuthorizationCode(clientId,userId,
                    approvedScopes, Instant.now().plus(2, ChronoUnit.MINUTES).getEpochSecond(),redirectUri);

            sb.append("?code=").append(URLEncoder.encode(authorizationCode.getCode(codeChallenge), StandardCharsets.UTF_8));
        } else {
            //Implicit: responseType=token : Not Supported
            return null;
        }
        if (state != null) {
            sb.append("&state=").append(state);
        }
        return sb.toString();
    }
    private String checkUserScopes(String userScopes, String requestedScope) {
        Set<String> allowedScopes = new LinkedHashSet<>();
        Set<String> rScopes = new HashSet<>(Arrays.asList(requestedScope.split(" ")));
        Set<String> uScopes = new HashSet<>(Arrays.asList(userScopes.split(" ")));
        for (String scope : uScopes) {
            if (rScopes.contains(scope)) allowedScopes.add(scope);
        }
        return String.join( " ", allowedScopes);
    }

    private Response informUserAboutError(String error) {
        return Response.status(Response.Status.BAD_REQUEST).entity("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8"/>
                    <title>Error</title>
                </head>
                <body>
                <aside class="container">
                    <p>%s</p>
                </aside>
                </body>
                </html>
                """.formatted(error)).build();
    }
}
