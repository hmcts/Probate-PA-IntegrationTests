package uk.gov.hmcts.probate.ccdintegrationtests;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.probate.IntegrationTestBase;


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


}
