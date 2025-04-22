package com.springgraphql.application.context;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RequestHeaderIntercepter implements WebGraphQlInterceptor {
    @Override
    public Mono<WebGraphQlResponse>intercept(WebGraphQlRequest request, Chain chain) {
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(authorization!= null && !authorization.isEmpty()) {
            request.configureExecutionInput((input, inputBuilder) ->
                    inputBuilder.graphQLContext(contextBuilder -> contextBuilder.put("Authorization", authorization)).build());
        }
        return chain.next(request);
    }
}
