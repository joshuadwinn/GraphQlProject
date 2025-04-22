package com.springgraphql.application.service;

import com.springgraphql.application.dto.Sample;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SampleService {
    Mono<Sample> create(String token);
    Flux<Sample> getSubscription();
    Mono<Sample> getSample(UUID id, String token);
}
