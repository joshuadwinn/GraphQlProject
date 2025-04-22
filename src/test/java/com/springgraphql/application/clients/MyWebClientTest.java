package com.springgraphql.application.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springgraphql.application.exception.ApiException;
import com.springgraphql.application.dto.Sample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyWebClientTest {
    @Mock
    private WebClient webClient;
    @Spy
    private ObjectMapper objectMapper;
    @InjectMocks
    private MyWebClient myWebClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    private String token;
    private UUID id;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        token = "token";
        id = UUID.randomUUID();
    }
    @Test
    public void postMonoTest() {
        Sample expectedSample = new Sample();
        expectedSample.setId(id);
        Mono<Sample> responseMono = Mono.just(expectedSample);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(anyMap())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(Mockito.any(), Mockito.any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Sample.class)).thenReturn(responseMono);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", id.toString());
        Mono<Sample> result = myWebClient.postMono("/api", requestBody, token, Sample.class);
        StepVerifier.create(result)
                .assertNext(resultSample -> {
                    assertEquals(id, resultSample.getId());
                })
                .verifyComplete();
    }

    @Test
    public void testFailPostMono() {
        Sample expectedSample = new Sample();
        expectedSample.setId(id);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", id.toString());
        CustomMinimalForTestResponseSpec responseSpecMock = mock(CustomMinimalForTestResponseSpec.class);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(anyMap())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.getStatus()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(responseSpecMock.onStatus(any(Predicate.class), any(Function.class))).thenCallRealMethod();

        assertThrows(ApiException.class, () -> {
            myWebClient.postMono("/api", requestBody, token, Sample.class);
        });

        when(responseSpecMock.getStatus()).thenReturn(HttpStatus.BAD_REQUEST);
        assertThrows(ApiException.class, () -> {
            myWebClient.postMono("/api", requestBody, token, Sample.class);
        });
    }

    @Test
    public void testPostMono(){
        Sample expectedSample = new Sample();
        expectedSample.setId(id);
        Mono<Sample> userMono = Mono.just(expectedSample);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Sample.class)).thenReturn(userMono);
        Mono<Sample> userResult = myWebClient.postMono("/api", token, Sample.class);
        StepVerifier.create(userResult)
                .assertNext(actualSample -> {
                    assertEquals(id, actualSample.getId());
                })
                .verifyComplete();
    }

    @Test
    public void testFailedPostMono() {
        CustomMinimalForTestResponseSpec responseSpecMock = mock(CustomMinimalForTestResponseSpec.class);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.getStatus()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(responseSpecMock.onStatus(any(Predicate.class), any(Function.class))).thenCallRealMethod();

        assertThrows(ApiException.class, () -> {
            myWebClient.postMono("/api", token, Sample.class);
        });

        when(responseSpecMock.getStatus()).thenReturn(HttpStatus.BAD_REQUEST);
        assertThrows(ApiException.class, () -> {
            myWebClient.postMono("/api", token, Sample.class);
        });
    }

    @Test
    public void postFailFluxWithFieldTest() {
        CustomMinimalForTestResponseSpec responseSpecMock = mock(CustomMinimalForTestResponseSpec.class);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.getStatus()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(responseSpecMock.onStatus(any(Predicate.class), any(Function.class))).thenCallRealMethod();
        assertThrows(ApiException.class, () -> {
            myWebClient.postFlux("/api", token, Sample.class, "info");
        });

        when(responseSpecMock.getStatus()).thenReturn(HttpStatus.BAD_REQUEST);
        assertThrows(ApiException.class, () -> {
            myWebClient.postFlux("/api", token, Sample.class, "info");
        });
    }

    @Test
    public void getFluxTest() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Sample sample1 = new Sample();
        Sample sample2 = new Sample();
        sample1.setId(id1);
        sample2.setId(id2);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.headers(any())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(Sample.class)).thenReturn(Flux.just(sample1, sample2));

        Flux<Sample> productFlux = myWebClient.getFlux("/api", token, Sample.class);
        StepVerifier.create(productFlux)
                .assertNext(actualSample1 -> {
                    assertEquals(id1.toString(), actualSample1.getId());
                })
                .assertNext(actualSample2 -> {
                    assertEquals(id2.toString(), actualSample2.getId());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getFailFluxTest(){
        CustomMinimalForTestResponseSpec responseSpecMock = mock(CustomMinimalForTestResponseSpec.class);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.headers(any())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.getStatus()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(responseSpecMock.onStatus(any(Predicate.class), any(Function.class))).thenCallRealMethod();
        assertThrows(ApiException.class, () -> {
            myWebClient.getFlux("/api", token, Sample.class);
        });

        when(responseSpecMock.getStatus()).thenReturn(HttpStatus.BAD_REQUEST);
        assertThrows(ApiException.class, () -> {
            myWebClient.getFlux("/api", token, Sample.class);
        });
    }
}
