package uk.gov.hmcts.probate.ccdintegrationtests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.probate.IntegrationTestBase;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

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

        SerenityRest.given()
                .headers(utils.getHeadersWithUserId())
                .body(rep)
                .when().post("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/cases")
                .then().assertThat().statusCode(201);
    }


    @Test
    public void validateFailureWithInvalidCCDCasePayload() {

        token =
                SerenityRest.given()
                        .headers(utils.getHeadersWithUserId())
                        .when().get("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/event-triggers/applyForGrant/token")
                        .then().assertThat().statusCode(200).extract().path("token");

        String rep = utils.getJsonFromFile("failure.pa.ccd.json").replace("\"event_token\": \"sampletoken\"", "\"event_token\":\"abc\"");

        SerenityRest.given()
                .headers(utils.getHeadersWithUserId())
                .body(rep)
                .when().post("/citizens/" + utils.getUserId() + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/cases")
                .then().assertThat().statusCode(500);
    }


    private String replaceString(String oldJson, String newJson) {
        return utils.getJsonFromFile("success.pa.ccd.json").replace(oldJson, newJson);
    }

}
