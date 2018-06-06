package uk.gov.hmcts.probate;

//import com.nimbusds.jwt.JWTParser;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.authorisation.generators.ServiceAuthTokenGenerator;

import java.text.ParseException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.post;

@Component
public class SolCCDServiceAuthTokenGenerator {

    @Value("${idam.oauth2.client.id}")
    private String clientId;

    @Value("${idam.oauth2.client.secret}")
    private String clientSecret;

    @Value("${idam.oauth2.redirect_uri}")
    private String redirectUri;

    @Value("${service.name}")
    private String serviceName;

    @Value("${service.auth.provider.base.url}")
    private String baseServiceAuthUrl;

    @Value("${user.auth.provider.oauth2.url}")
    private String baseServiceOauth2Url;
    String clientToken;

    private String idamUsername;

    private String idamPassword;
    @Value("${env}")
    private String environment;

    @Value("${idam.secret}")
    private String secret;

    @Value("${user.auth.provider.oauth2.url}")
    private String idamUserBaseUrl;

    private String userToken;


    private String idamCreateUrl() {
        return idamUserBaseUrl + "/testing-support/accounts";
    }

    @Autowired
    private ServiceAuthTokenGenerator tokenGenerator;

    public String generateServiceToken() {
        return tokenGenerator.generate();
    }

    public String getUserId() {
//        String jwt = userToken.replaceFirst("Bearer ", "");
//        Map<String, Object> claims;
//        try {
//            claims = JWTParser.parse(jwt).getJWTClaimsSet().getClaims();
//        } catch (ParseException e) {
//            throw new IllegalStateException("Cannot find user from authorization token ", e);
//        }
//        String userid_local = (String) claims.get("id");
//        return userid_local;

        String userid_local = RestAssured.given()
                .header("Authorization", userToken)
                .get(idamUserBaseUrl + "/details")
                .body()
                .path("id");
        System.out.println("userid generated.." + userid_local);
        return userid_local;
    }


    private void createUserInIdam() {

        idamUsername = "simulate-delivered" + UUID.randomUUID() + "@notifications.service.gov.uk";
        idamPassword = "Test123456";
        Response res = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"email\":\"" + idamUsername + "\", \"forename\":\"Test\",\"surname\":\"User\",\"password\":\"" + idamPassword + "\"}")
                .post(idamCreateUrl());
    }


    public String generateUserTokenWithNoRoles() {
        createUserInIdam();
//        System.out.println("created user in idam");
//        final String encoded = Base64.getEncoder().encodeToString((idamUsername + ":" + idamPassword).getBytes());
//        final String redirectUriEnv = environment.equalsIgnoreCase("saat") == true
//                ? redirectUri
//                : "https://www.preprod.ccd.reform.hmcts.net/oauth2redirect";
//        final String token = RestAssured.given().baseUri(idamUserBaseUrl)
//                .header("Authorization", "Basic " + encoded)
//                .post("/oauth2/authorize?response_type=token&client_id=probate&redirect_uri=" +
//                        redirectUriEnv)
//                .body()
//                .path("access-token");

        final String token = generateClientToken();
        System.out.println("token generated.." + token);
       // userToken = "Bearer " + token;
        return userToken;
    }


    //keeping this code to see if we need this mechanism after sidam integration.
    private String generateClientToken() {
        String code = generateClientCode();
        String token = "";

        token = RestAssured.given().post(idamUserBaseUrl + "/oauth2/token?code=" + code +
                "&client_secret=" + secret +
                "&client_id=probate" +
                "&redirect_uri=" + redirectUri +
                "&grant_type=authorization_code")
                .body().path("access_token");

        return "Bearer " + token;
    }

    private String generateClientCode() {
        String code = "";
        createUserInIdam();
        System.out.println("created user in idam");
        final String encoded = Base64.getEncoder().encodeToString((idamUsername + ":" + idamPassword).getBytes());
        System.out.println("encoded auth is.." + encoded);
        code = RestAssured.given().baseUri(idamUserBaseUrl)
                .header("Authorization", "Basic " + encoded)
                .post("/oauth2/authorize?response_type=code&client_id=probate&redirect_uri=" + redirectUri)
                .body().path("code");
        return code;

    }
}