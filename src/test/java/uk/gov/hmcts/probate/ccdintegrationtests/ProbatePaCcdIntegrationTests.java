package uk.gov.hmcts.probate.ccdintegrationtests;

import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.probate.IntegrationTestBase;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityRunner.class)
public class ProbatePaCcdIntegrationTests extends IntegrationTestBase {

    String token;


    @Test
    public void validatePostSuccessCCDCase() {
        token =
                SerenityRest.given()
                        .headers(utils.getHeadersWithUserId())
                        .when().get("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/event-triggers/applyForGrant/token")
                        .then().assertThat().statusCode(200).extract().path("token");

        String rep = utils.getJsonFromFile("success.pa.ccd.json").replace("\"event_token\": \"sampletoken\"", "\"event_token\":\"" + token + "\"");

        Response res = SerenityRest.given()
                .headers(utils.getHeadersWithUserId())
                .body(rep)
                .when().post("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/cases");
        assertTrue(res.getStatusCode() == 201);
        System.out.println("Response body..." + res.getBody().prettyPrint());
    }


    @Test
    public void verifyJurisdictionInTheSuccessResponse() {
        token =
                SerenityRest.given()
                        .headers(utils.getHeadersWithUserId())
                        .when().get("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/event-triggers/applyForGrant/token")
                        .then().assertThat().statusCode(200).extract().path("token");

        String rep = utils.getJsonFromFile("success.pa.ccd.json").replace("\"event_token\": \"sampletoken\"", "\"event_token\":\"" + token + "\"");

        SerenityRest.given()
                .headers(utils.getHeadersWithUserId())
                .body(rep)
                .when().post("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/cases")
                .then()
                .statusCode(201).and().body("jurisdiction", equalToIgnoringCase("PROBATE"));

    }

    @Test
    public void verifyStateIsPresentInTheSuccessResponse() {
        token =
                SerenityRest.given()
                        .headers(utils.getHeadersWithUserId())
                        .when().get("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/event-triggers/applyForGrant/token")
                        .then().assertThat().statusCode(200).extract().path("token");

        String rep = utils.getJsonFromFile("success.pa.ccd.json").replace("\"event_token\": \"sampletoken\"", "\"event_token\":\"" + token + "\"");

        SerenityRest.given()
                .headers(utils.getHeadersWithUserId())
                .body(rep)
                .when().post("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/cases")
                .then()
                .statusCode(201).and().body("state",equalToIgnoringCase("CaseCreated"));

    }

    @Test
    public void verifyCaseTypeIDPresentInTheSuccessResponse() {
        token =
                SerenityRest.given()
                        .headers(utils.getHeadersWithUserId())
                        .when().get("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/event-triggers/applyForGrant/token")
                        .then().assertThat().statusCode(200).extract().path("token");

        String rep = utils.getJsonFromFile("success.pa.ccd.json").replace("\"event_token\": \"sampletoken\"", "\"event_token\":\"" + token + "\"");

        SerenityRest.given()
                .headers(utils.getHeadersWithUserId())
                .body(rep)
                .when().post("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/cases")
                .then()
                .statusCode(201).and().body("case_type_id",equalToIgnoringCase("GrantOfRepresentation"));

    }


    @Test
    public void verifySecurityClassificationIsPresentInTheSuccessResponse() {
        token =
                SerenityRest.given()
                        .headers(utils.getHeadersWithUserId())
                        .when().get("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/event-triggers/applyForGrant/token")
                        .then().assertThat().statusCode(200).extract().path("token");

        String rep = utils.getJsonFromFile("success.pa.ccd.json").replace("\"event_token\": \"sampletoken\"", "\"event_token\":\"" + token + "\"");

        SerenityRest.given()
                .headers(utils.getHeadersWithUserId())
                .body(rep)
                .when().post("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/cases")
                .then()
                .statusCode(201).and().body("security_classification",equalToIgnoringCase("PUBLIC"));

    }

    @Test
    public void verifyCreatedDateIsPresentInTheSuccessResponse() {
        token =
                SerenityRest.given()
                        .headers(utils.getHeadersWithUserId())
                        .when().get("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/event-triggers/applyForGrant/token")
                        .then().assertThat().statusCode(200).extract().path("token");

        String rep = utils.getJsonFromFile("success.pa.ccd.json").replace("\"event_token\": \"sampletoken\"", "\"event_token\":\"" + token + "\"");

        SerenityRest.given()
                .headers(utils.getHeadersWithUserId())
                .body(rep)
                .when().post("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/cases")
                .then()
                .statusCode(201).and().extract().body().asString().contains("created_date");
    }

    @Test
    public void verifyLastModifiedIsPresentInTheSuccessResponse() {
        token =
                SerenityRest.given()
                        .headers(utils.getHeadersWithUserId())
                        .when().get("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/event-triggers/applyForGrant/token")
                        .then().assertThat().statusCode(200).extract().path("token");

        String rep = utils.getJsonFromFile("success.pa.ccd.json").replace("\"event_token\": \"sampletoken\"", "\"event_token\":\"" + token + "\"");

        SerenityRest.given()
                .headers(utils.getHeadersWithUserId())
                .body(rep)
                .when().post("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/cases")
                .then()
                .statusCode(201).and().extract().body().asString().contains("last_modified");
    }

    @Test
    public void verifyIdIsPresentInTheSuccessResponse() {
        token =
                SerenityRest.given()
                        .headers(utils.getHeadersWithUserId())
                        .when().get("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/event-triggers/applyForGrant/token")
                        .then().assertThat().statusCode(200).extract().path("token");

        String rep = utils.getJsonFromFile("success.pa.ccd.json").replace("\"event_token\": \"sampletoken\"", "\"event_token\":\"" + token + "\"");

        SerenityRest.given()
                .headers(utils.getHeadersWithUserId())
                .body(rep)
                .when().post("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/cases")
                .then()
                .statusCode(201).and().extract().body().asString().contentEquals("id");
    }

    @Test
    public void validateFailureWithInvalidCCDCasePayload() {

        String rep = utils.getJsonFromFile("failure.pa.ccd.json").replace("\"event_token\": \"sampletoken\"", "\"event_token\":\"abc\"");

        SerenityRest.given()
                .headers(utils.getHeadersWithUserId())
                .body(rep)
                .when().post("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/cases")
                .then().assertThat().statusCode(500);
    }

}
