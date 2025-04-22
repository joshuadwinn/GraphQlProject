package com.springgraphql.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springgraphql.application.clients.MyWebClient;
import com.springgraphql.application.dto.Sample;
import com.springgraphql.application.service.impl.SampleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SampleServiceImplTest {

    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private SampleServiceImpl accountService;
    @Mock
    private MyWebClient myWebClient;

    private String token;
    @BeforeEach
    public void setUp(){
        token = "token";
    }
    @Test
    public void testCreateSample() {
        UUID expectedAccountId = UUID.randomUUID();
        Sample expectedSample = new Sample();
        expectedSample.setId(expectedAccountId);
        when(myWebClient.postMono(any(),anyMap(), any(), any())).thenReturn(Mono.just(expectedSample));
        Mono<Sample> result = accountService.create(token);

        StepVerifier.create(result)
                .assertNext(actualAccount -> {
                    assertEquals(expectedAccountId, actualAccount.getId());
                })
                .verifyComplete();
    }
}