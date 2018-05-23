package uk.gov.hmcts.probate.util;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.probate.SolCCDServiceAuthTokenGenerator;
import uk.gov.hmcts.probate.TestContextConfiguration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@ContextConfiguration(classes = TestContextConfiguration.class)
@Component
public class TestUtils {

    @Autowired
    protected SolCCDServiceAuthTokenGenerator serviceAuthTokenGenerator;

    private String serviceToken;


    @PostConstruct
    public void init() {
        serviceToken = serviceAuthTokenGenerator.generateServiceToken();
    }

    public String getJsonFromFile(String fileName) {
        try {
            File file = ResourceUtils.getFile(this.getClass().getResource("/json/" + fileName));
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Headers getHeaders() {
        return getHeaders(serviceToken);
    }

    public Headers getHeaders(String serviceToken) {
        return Headers.headers(
                new Header("ServiceAuthorization", serviceToken),
                new Header("Content-Type", ContentType.JSON.toString()));
    }

    public Headers getHeadersWithUserId() {
        return getHeadersWithUserId(serviceToken);
    }

    public Headers getHeadersWithUserId(String serviceToken) {
        return Headers.headers(
                new Header("ServiceAuthorization", serviceToken),
                new Header("Content-Type", ContentType.JSON.toString()),
                new Header("Authorization", serviceAuthTokenGenerator.generateUserTokenWithNoRoles()));


    }

    public String getUserId() {
        return serviceAuthTokenGenerator.getUserId();
    }



}
