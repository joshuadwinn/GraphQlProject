package com.springgraphql.application.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springgraphql.application.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class MyWebClient {
    @Autowired
    private WebClient myWebClient;
    @Autowired
    private ObjectMapper objectMapper;

    public <T> Mono<T> postMono(String to, Map<String, Object> body, String token, Class<T> clazz) {
        checkToken(token);
        return myWebClient.post()
                .uri(to)
                .headers(h -> h.set(HttpHeaders.AUTHORIZATION, token))
                .bodyValue(body)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                        clientResponse -> {
                            if (clientResponse.statusCode().is4xxClientError()) {
                                return Mono.error(new ApiException(HttpStatus.BAD_REQUEST, "Couldn't load: " + clazz.getName()));
                            } else {
                                return Mono.error(new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't load: " + clazz.getName()));
                            }
                        })
                .bodyToMono(clazz);
    }

    public <T> Mono<T> postMono(String to, String token, Class<T> clazz) {
        checkToken(token);
        return myWebClient.post()
                .uri(to)
                .headers(h -> h.set(HttpHeaders.AUTHORIZATION, token))
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                        clientResponse -> {
                            if (clientResponse.statusCode().is4xxClientError()) {
                                return Mono.error(new ApiException(HttpStatus.BAD_REQUEST, "Couldn't load: " + clazz.getName()));
                            } else {
                                return Mono.error(new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't load: " + clazz.getName()));
                            }
                        })
                .bodyToMono(clazz);
    }

    public <T> Mono<T> getMono(String to, String token, Class<T> clazz) {
        checkToken(token);
        return myWebClient.get()
                .uri(to)
                .headers(h -> h.set(HttpHeaders.AUTHORIZATION, token))
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                        clientResponse -> {
                            if (clientResponse.statusCode().is4xxClientError()) {
                                return Mono.error(new ApiException(HttpStatus.BAD_REQUEST, "Couldn't load: " + clazz.getName()));
                            } else {
                                return Mono.error(new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't load: " + clazz.getName()));
                            }
                        })
                .bodyToMono(clazz);
    }

    public <T> Flux<T> postFlux(String to, String token, Class<T> clazz, String field) {
        checkToken(token);
        return myWebClient.post()
                .uri(to)
                .headers(h -> h.set(HttpHeaders.AUTHORIZATION, token))
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                        clientResponse -> {
                            if (clientResponse.statusCode().is4xxClientError()) {
                                return Mono.error(new ApiException(HttpStatus.BAD_REQUEST, "Couldn't load: " + clazz.getName()));
                            } else {
                                return Mono.error(new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't load: " + clazz.getName()));
                            }
                        })
                .bodyToFlux(JsonNode.class)
                .flatMap(jsonNode -> {
                    JsonNode node = jsonNode.get(field);
                    return Mono.just(objectMapper.convertValue(node, clazz));
                });
    }

    public <T> Flux<T> getFlux(String to, String token, Class<T> clazz) {
        checkToken(token);
        return myWebClient.get()
                .uri(to)
                .headers(h -> h.set(HttpHeaders.AUTHORIZATION, token))
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                        clientResponse -> {
                            if (clientResponse.statusCode().is4xxClientError()) {
                                return Mono.error(new ApiException(HttpStatus.BAD_REQUEST, "Couldn't load: " + clazz.getName()));
                            } else {
                                return Mono.error(new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't load: " + clazz.getName()));
                            }
                        })
                .bodyToFlux(clazz);
    }

    private void checkToken(String token) {
        if(token== null || token.isEmpty()){
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }
}
