package uk.gov.hmcts.probate;

import feign.Feign;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.feign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.authorisation.generators.ServiceAuthTokenGenerator;

public class TestContextConfiguration {

    @Bean
    public ServiceAuthTokenGenerator serviceAuthTokenGenerator(@Value("${service.auth.provider.base.url}") String s2sUrl,
                                                               @Value("${s2s.auth.totp.secret}") String secret,
                                                               @Value("${service.name}") String microservice) {
        final ServiceAuthorisationApi serviceAuthorisationApi = Feign.builder()
                .encoder(new JacksonEncoder())
                .contract(new SpringMvcContract())
                .target(ServiceAuthorisationApi.class, s2sUrl);
        return new ServiceAuthTokenGenerator(secret, microservice, serviceAuthorisationApi);
    }
}