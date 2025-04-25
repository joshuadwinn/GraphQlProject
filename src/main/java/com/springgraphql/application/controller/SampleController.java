package com.springgraphql.application.controller;

import com.springgraphql.application.exception.ApiException;
import com.springgraphql.application.dto.Sample;
import com.springgraphql.application.service.SampleService;
import graphql.GraphQLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Controller
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @QueryMapping("sample")
    public Mono<Sample> getSample(GraphQLContext context, @Argument String id){
       return Mono.empty();
    }

    @MutationMapping("sample")
    public Mono<Sample> createSample(GraphQLContext context){
       return Mono.empty();
    }

    @SubscriptionMapping("sub")
    public Flux<Sample> subscribe(){
        return sampleService.getSubscription();
    }
}
