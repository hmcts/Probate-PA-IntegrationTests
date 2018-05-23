package uk.gov.hmcts.probate;

import io.restassured.RestAssured;
import net.thucydides.junit.spring.SpringIntegration;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.probate.util.TestUtils;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestContextConfiguration.class)
public abstract class IntegrationTestBase {

    @Autowired
    protected SolCCDServiceAuthTokenGenerator serviceAuthTokenGenerator;

    @Autowired
    protected TestUtils utils;

    private String solCcdServiceUrl;

    @Autowired
    public void solCcdServiceUrl(@Value("${ccd.data.store.api.url}") String solCcdServiceUrl) {
        this.solCcdServiceUrl = solCcdServiceUrl;
        RestAssured.baseURI = solCcdServiceUrl;
        System.out.println("base uri..." + RestAssured.baseURI);
    }


    @Rule
    public SpringIntegration springIntegration;

    public IntegrationTestBase() {
        this.springIntegration = new SpringIntegration();

    }
}
