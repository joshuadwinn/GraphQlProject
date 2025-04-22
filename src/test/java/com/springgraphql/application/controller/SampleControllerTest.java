package com.springgraphql.application.controller;

import com.springgraphql.application.dto.Sample;
import com.springgraphql.application.service.impl.SampleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureHttpGraphQlTester
@ActiveProfiles("integrationtest")
public class SampleControllerTest {
    @Autowired
    WebGraphQlTester graphQlTester;

    @MockBean
    private SampleServiceImpl sampleService;
    private String token;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        token = "token";
    }

     @Test
    public void testCreate() {
        UUID id = UUID.randomUUID();

        String query = String.format(
                "query { createSample { id } }",
                id
        );

        Sample sample = new Sample();
        sample.setId(id);
        when(sampleService.create(token)).thenReturn(Mono.just(sample));
        Sample fetchedSample = graphQlTester.mutate()
                .header("Authorization", token).build()
                .document(query)
                .execute()
                .path("createSample").entity(Sample.class).get();

        assertEquals(id, fetchedSample.getId());
    }
}
