package uk.gov.hmcts.probate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.model.ClientAuthorizationCodeResponse;
import uk.gov.hmcts.probate.model.ClientAuthorizationResponse;
import uk.gov.hmcts.probate.util.TestUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
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

    @Value("${user.auth.provider.oauth2.url}")
    private String idamUserBaseUrl;

    private String userToken;


    private String idamCreateUrl() {
        return idamUserBaseUrl + "/testing-support/accounts";
    }

    public String generateServiceToken() {
        return "Bearer " + post(baseServiceAuthUrl + "/testing-support/lease?microservice={microservice}", serviceName)
                .body().asString();
    }

    public String getUserId() {
      //  String jwt = userToken.replaceFirst("Bearer ", "");
        Map<String, Object> claims;
        try {
            claims = JWTParser.parse(userToken).getJWTClaimsSet().getClaims();
        } catch (ParseException e) {
            throw new IllegalStateException("Cannot find user from authorization token ", e);
        }
        String userid_local = (String) claims.get("id");
        System.out.println("userid_local...." + userid_local);
        return userid_local;

    }


    private void createUserInIdam() {

        idamUsername = "simulate-delivered" + UUID.randomUUID() + "@notifications.service.gov.uk";
        idamPassword =  "Venu1234";
        System.out.println("idam Password..." + idamPassword);
       Response res= RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"email\":\"" + idamUsername + "\", \"forename\":\"Test\",\"surname\":\"User\",\"password\":\"" + idamPassword + "\"}")
                .post(idamCreateUrl());

        System.out.println("Idamcreateurl...." + idamCreateUrl());

        System.out.println("response status code from create user..." + res.getStatusCode());
        System.out.println("response from create user..." + res.getBody().prettyPrint());
        System.out.println("in create useridam ");
    }


    public String generateUserTokenWithNoRoles() {
        createUserInIdam();
//        Map<String, String >m = new HashMap();
//        m.put("username", "vm1234567@gmail.com");
//        m.put("password", "Vm123456");

//        given().urlEncodingEnabled(true)
//                .param("username", SMOKE_TEST_USER)
//                .param("password", SMOKE_TEST_USER_PASSWORD)
//                .header("Accept", ContentType.JSON.getAcceptHeader()).post()

       final String token = RestAssured.given()
                        .param("username", "vm1234567@gmail.com")
                        .param("password","Vm123456" )
                              .post(idamUserBaseUrl+"/loginUser")
                              .body()
                              .path("api_auth_token");


//        System.out.println("created user in idam");
//        final String encoded = Base64.getEncoder().encodeToString((idamUsername + ":" + idamPassword).getBytes());
//        System.out.println("encoded auth is.." + encoded);
//        final String redirectUriEnv = environment.equalsIgnoreCase("saat") == true
//                ? redirectUri
//                : "https://www.preprod.ccd.reform.hmcts.net/oauth2redirect";
////        Map<String, String> header = new HashMap<>();
////        header.put("username", idamUsername);
////        header.put("password", idamPassword);
//        Response res1
//                = RestAssured.given().baseUri(idamUserBaseUrl)
//                .header("Authorization",  "Basic "+encoded)
//               // .headers(header)
//                .post("/oauth2/authorize?username=vm1234567@gmail.com&password=Vm123456&response_type=token&client_id=probate&redirect_uri=https://www-test.probate.reform.hmcts.net/");
//        System.out.println("res1 status code..." + res1.getStatusCode());
//        System.out.println("res1 body.." + res1.getHeader("Location"));
//
////                .body()
////                .path("access-token");
////        System.out.println("token generated.."+token);
//
       userToken = token;
        System.out.println("token generated.."+userToken);
////        return userToken;
////        userToken = generateClientToken();

        return userToken;
    }


//    private String generateClientToken() {
//        String code = generateClientCode();
//        String token = "";
//
//        String jsonResponse = post(baseServiceOauth2Url + "/oauth2/token?code=" + code +
//                "&client_secret=secret/test/ccidam/idam-api/oauth2/client-secrets/probate"+
//                "&client_id=probate"+
//                "&redirect_uri=https://www-test.probate.reform.hmcts.net"+
//                "&grant_type=authorization_code")
//                .body().asString();
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        try {
//            token = mapper.readValue(jsonResponse, ClientAuthorizationResponse.class).accessToken;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Generated user token...." + "Bearer "+token );
//
//        return "Bearer" +token;
//    }
//
//    private String generateClientCode() {
//        String code = "";
//        createUserInIdam();
//        System.out.println("created user in idam");
//        final String encoded = Base64.getEncoder().encodeToString((idamUsername + ":" + idamPassword).getBytes());
//        System.out.println("encoded auth is.." + encoded);
//        String jsonResponse = given()
//                .header("Authorization", "Basic "+encoded)
//                .post(baseServiceOauth2Url + "/oauth2/authorize?response_type=code" +
//                        "&client_id=probate"+
//                        "&redirect_uri=https://www-test.probate.reform.hmcts.net")
//                .asString();
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        try {
//            code = mapper.readValue(jsonResponse, ClientAuthorizationCodeResponse.class).code;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Generated code..." + code);
//        return code;
//    }
}