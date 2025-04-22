package com.springgraphql.application.service.impl;

import com.springgraphql.application.clients.MyWebClient;
import com.springgraphql.application.dto.Sample;
import com.springgraphql.application.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SampleServiceImpl implements SampleService {
   
    private final Sinks.Many<Sample> sink = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    @Autowired
    private MyWebClient myWebClient;

    @Override
    public Mono<Sample> create(String token) {
        UUID id = UUID.randomUUID();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", id);
        return myWebClient.postMono("/api", requestBody, token, Sample.class);
    }

    //Publish to the sink
    public void sendUpdate(Sample data) {
        sink.tryEmitNext(data);
    }

    //subscribe from the sink
    @Override
    public Flux<Sample> getSubscription() {
        return sink.asFlux();
    }

    @Override
    public Mono<Sample> getSample(UUID id, String token) {
        return myWebClient.getMono("/api/" + id, token, Sample.class);
    }
}
